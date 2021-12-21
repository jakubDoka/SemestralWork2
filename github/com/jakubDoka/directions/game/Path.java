package github.com.jakubDoka.directions.game;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

import github.com.jakubDoka.directions.ui.CanvasObject;
import github.com.jakubDoka.directions.ui.Colors;
import github.com.jakubDoka.directions.ui.Vec;

/**
 * Path represents a game path trough which the player is going.
 * Important role of paths is to not overlap in a way that disallows
 * game-play. It has to be always obvious where the path is heading.
 */
public class Path extends CanvasObject {
    
    private final Random random;
    private final Rectangle drawer;
    private final Vec temp;
    private final Vec temp2;
    private final Segment[] segments;
    private final Vec[] sort_temp;
    private int current;
    private final int size;
    private final double[] possibilities;

    public Path(int length, int size, double x, double y) {
        this.random = new Random();
        this.drawer = new Rectangle();
        this.temp = new Vec();
        this.temp2 = new Vec();
        this.segments = new Segment[length];
        for(int i = 0; i < length; i++) {
            this.segments[i] = new Segment(new Vec(x, y));
        }
        this.sort_temp = new Vec[length];
        for(int i = 0; i < length; i++) {
            this.sort_temp[i] = new Vec();
        }
        this.current = length - 1;
        this.size = size;
        this.possibilities = new double[length + 1];
    }

    public void makeLastFirst() {
        Segment last = this.segments[0];
        for(int i = 1; i < this.segments.length; i++) {
            this.segments[i - 1] = this.segments[i];
        }
        this.segments[this.segments.length - 1] = last;
    }

    public void shift(double x, double y) {
        this.temp.set(x, y);
        for(Segment segment : this.segments) {
            segment.setPos(segment.getPos(this.temp2).add(this.temp));
        }
    }

    public boolean move(Direction direction) {
        Segment current = this.segments[this.current];

        if (current.getDirection() != direction) {
            this.expand();
            return false;
        }
        
        this.current++;
        
        this.expand();

        Vec shift = current.getPos(this.temp2).sub(this.segments[this.current].getPos(this.temp));

        this.shift(shift.getX(), shift.getY());

        return true;
    }

    public void expand() {
        Direction direction = this.segments[this.segments.length - 2].getDirection();;
        Segment current = this.segments[this.segments.length - 1];
        
        Vec currentPos = this.segments[this.segments.length - 1].getPos(this.temp);

        this.segments[0].setPos(currentPos);
        Vec step = this.getStep(direction, currentPos);
        Direction next_direction = Direction.of(step);
        current.setDirection(next_direction);
        this.segments[0].shift(step);
        this.makeLastFirst();

        this.current--;
    }

    /**
     * Returns best possible step so that the path does not overlap weirdly.
     * 
     * @param direction
     * @return
     */
    public Vec getStep(Direction direction, Vec currentPos) {
        // creating the vector mapping
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i].getPos(this.sort_temp[i]);
        }

        boolean horizontal = direction.horizontal();        
        
        double current; 
        if (horizontal) {
            current = currentPos.getY();
            Util.sort(this.sort_temp, (a, b) -> a.getY() > b.getY());
        } else {
            current = currentPos.getX();
            Util.sort(this.sort_temp, (a, b) -> a.getX() > b.getX());
        }

        // finding best gap
        int found = 0;
        for (int i = 1; i < this.sort_temp.length; i++) {
            double a;
            double b;
            if (horizontal) {
                a = this.sort_temp[i].getY();

                b = this.sort_temp[i - 1].getY();
            } else {
                a = this.sort_temp[i].getX();
                b = this.sort_temp[i - 1].getX();
            }
            
            if (Math.abs(a - b) < this.size * 4) {
                continue;
            }
            
            double mid = (a + b) / 2 - current;
            
            if (Math.abs(mid) > this.size * 4) {
                this.possibilities[found + 2] = mid;
                found++;
            } 
        }

        
        double last;
        double first;

        if (horizontal) {
            first = this.sort_temp[0].getY();
            last = this.sort_temp[this.sort_temp.length - 1].getY();
        } else {
            first = this.sort_temp[0].getX();
            last = this.sort_temp[this.sort_temp.length - 1].getX();
        }

        first -= current;
        last -= current;

        int spacing = this.size * 6;

        if (first > 0) {
            first += spacing;
        } else {
            first -= spacing;
        }

        if (last >= 0) {
            last += spacing;
        } else {
            last -= spacing;
        }

        this.possibilities[0] = first;
        this.possibilities[1] = last;

        int pick;
        if(found == 0) {
            pick = this.random.nextInt(2);
        } else {
            pick = 2 + this.random.nextInt(found);
        }

        double final_step = this.possibilities[pick];

        if (horizontal) {
            return this.temp.set(0, final_step);
        } else { 
            return this.temp.set(final_step, 0);
        }
    }

    /**
     * Draws the path.
     */
    @Override
    public void drawImpl(Graphics2D g) {
        // draw segment centers
        for (Segment segment : this.segments) {
            g.setColor(segment.getColor(0));
            this.drawer.setSize(this.size * 2, this.size * 2);
            this.drawer.setLocation(
                (int)segment.getPos(this.temp).getX() - this.size, 
                (int)segment.getPos(this.temp).getY() - this.size
            );
            g.fill(this.drawer);
        }
        
        // connect segments
        for (int i = 0; i < this.segments.length - 1; i++) {
            Segment a = this.segments[i];
            Segment b = this.segments[i + 1];
            Vec difference = b.getPos(this.temp).sub(a.getPos(this.temp2));
            if (Math.round(difference.getX()) == 0) {
                this.drawer.setSize(this.size, (int)Math.abs(difference.getY()) + this.size);
            } else {
                this.drawer.setSize((int)Math.abs(difference.getX()) + this.size, this.size);
            }
            Vec min = a.getPos(this.temp).min(b.getPos(this.temp2));
            this.drawer.setLocation(
                (int)min.getX() - this.size / 2,
                (int)min.getY() - this.size / 2
            );
            g.setColor(a.getColor(1 - (double)i / this.segments.length));
            g.fill(this.drawer);
        }
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        
        for (Segment segment : this.segments) {
            segment.setColor(color);
        }
    }

    public void restart() {
        this.current = this.segments.length - 1;
        this.temp.set(0, 0);
        for(Segment segment : this.segments) {
            segment.setPos(this.temp);
        }
    }

    /**
     * Segment glues up information to be stored in array.
     */
    public static class Segment {
        private final Vec pos;
        
        private Direction direction;
        private int color;
        
        public void setDirection(Path.Direction next_dir) {
            this.direction = next_dir;
        }

        public void setColor(Color color) {
            this.color = color.getRGB();
        }

        /**
         * Creates a new segment that is black and heading UP.
         * @param pos - Initial position.
         */
        public Segment(Vec pos) {
            this.color = 0xFF000000;
            this.pos = pos;
            this.direction = Direction.UP;
        }

        public Vec getPos(Vec target) {
            if (target == null) {
                target = new Vec();
            }
            return target.set(this.pos);
        }

        public void setPos(Vec pos) {
            this.pos.set(pos);
        }

        public void shift(Vec delta) {
            this.pos.add(delta);
        }

        public Direction getDirection() {
            return this.direction;
        }

        /**
         * Returns color of the segment interpolated towards transparent.
         * 
         * @param fade is assumed to be from 0 to 1.
         */
        public Color getColor(double fade) {
            return Colors.create(Colors.lerp(this.color, this.color & 0x00FFFFFF, (int)(fade * 225)));
        }
    }

    public enum Direction {
        UP, LEFT, DOWN, RIGHT;

        private static int counter;
        private final int index;

        /**
         * Returns internal index counter and increments it.
         * This is only used in constructor to determinate indexes.
         * @return
         */
        private static int next() {
            return counter++;
        }

        /**
         * Returns vector direction, assuming vector is always axis aligned.
         * @param step
         * @return
         */
        public static Direction of(Vec step) {
            if (step.getX() > 0) {
                return RIGHT;
            } else if (step.getX() < 0) {
                return LEFT;
            } else if (step.getY() > 0) {
                return DOWN;
            } else {
                return UP;
            }
        }

        /**
         * Returns whether direction is horizontal.
         */
        public boolean horizontal() {
            return this.index % 2 == 1;
        }

        /**
         * Constructs direction with appropriate index
         */
        private Direction() {
            this.index = Direction.next();
        }

        /**
         * Returns index of direction.
         */
        public int getIndex() {
            return this.index;
        }
    }
}
