package github.com.jakubDoka.directions.ui;
import java.util.ArrayList;
import java.util.Iterator;

public class FrameManager {
    private static final long NANOS_IN_SECOND = 1_000_000_000;

    private long last;
    private double time;
    private double delta;
    private int counter;
    private int frameRate;

    private final ArrayList<ITask> tasks = new ArrayList<>();

    public FrameManager() {
        this.last = System.nanoTime();
        this.time = 0d;
        this.frameRate = 0;
        this.counter = 0;
    }

    public void update() {
        long now = System.nanoTime();
        long elapsed = now - this.last;
        this.last = now;
        this.delta = elapsed / (double)NANOS_IN_SECOND;
        
        this.time += this.delta;
        this.counter++;

        if (this.time > 1d) {
            this.time = 0d;
            this.frameRate = this.counter;
            this.counter = 0;
        }

        this.doTasks();
    }

    public void addTask(ITask task) {
        this.tasks.add(task);
    }

    private void doTasks() {
        Iterator<ITask> iter = this.tasks.iterator();
        while (iter.hasNext()) {
            ITask task = iter.next();
            if (task.tick(this.delta)) {
                iter.remove();
            }
        }
    }

    public int getFrameRate() {
        return this.frameRate;
    }

    public double getDelta() {
        return this.delta;
    }

    public static class Tween implements ITask {
        private double duration;
        private double progress;
        private ITweenTarget target;

        public Tween(double duration, ITweenTarget target) {
            this.duration = duration;
            this.target = target;
            this.progress = 0d;
        }

        public boolean tick(double delta) {
            this.progress += delta;
            if (this.progress > this.duration) {
                this.progress = this.duration;
            }
            if (this.target.progress(this.progress)) {
                this.progress = this.duration;
            }
            return this.progress >= this.duration;
        }
    }

    public interface ITweenTarget {
        boolean progress(double progress);
    }

    public static class Timer implements ITask {
        private boolean repeat;
        private boolean killed;
        private Runnable runnable;
        private double delay;

        private double time;

        public Timer(double delay, boolean repeat, Runnable runnable) {
            this.set(delay, repeat, runnable);
        }

        public void set(double delay, boolean repeat, Runnable runnable) {
            this.runnable = runnable;
            this.delay = delay;
            this.repeat = repeat;
            
            this.killed = false;
            this.time = 0d;
        }

        @Override
        public boolean tick(double delta) {
            if (this.killed) {
                return true;
            }

            this.time += delta;
            if (this.time >= this.delay) {
                this.time = 0d;
                this.runnable.run();
                if (!this.repeat) {
                    return true;
                }
            }
            return false;
        }

        public void kill() {
            this.killed = true;
        }
    }

    public interface ITask {
        boolean tick(double delta);
    }
}