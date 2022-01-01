package github.com.jakubDoka.directions.ui;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Frame manager handles everything related to time. It can measure 
 * timestamps in floating point seconds. Register tasks that should 
 * be repeated in certain periods of time and also tween tools.
 * 
 * (Most of this garbage is not even used.)
 */
public class FrameManager {
    private static final long NANOS_IN_SECOND = 1_000_000_000;

    private long last;
    private double time;
    private double delta;
    private int counter;
    private int frameRate;

    private final ArrayList<ITask> tasks = new ArrayList<>();

    /**
     * Creates new frame manager that is up to date.
     */
    public FrameManager() {
        this.last = System.nanoTime();
        this.time = 0d;
        this.delta = 0d;
        this.counter = 0;
        this.frameRate = 0;
    }

    /**
     * Updates the delta time since last update, performs tasks and 
     * runs tween if possible. Needs to be called every frame for higher 
     * accuracy of task execution. 
     */
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

    /**
     * Registers a task.
     */
    public void addTask(ITask task) {
        this.tasks.add(task);
    }

    /**
     * Updates and performs tasks if possible.
     * Done tasks are discarded.
     */
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

    public void setDelta(double delta) {
        this.delta = delta;
    }

    /**
     * Tween is specialized task that reports linear progress to 
     * its target every fame.
     */
    public static class Tween implements ITask {
        private double duration;
        private double progress;
        private ITweenTarget target;

        /**
         * Creates new tween.
         * @param duration - duration of tween in seconds
         * @param target - target object that will receive progress
         */
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

    /**
     * Interface for objects that can be tweened.
     */
    public interface ITweenTarget {
        boolean progress(double progress);
    }

    /**
     * Timer is specialized task for executing repeated or one-time
     * Runnables.
     */
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

    /**
     * Interface for objects that can be passed to manager.
     * Tick is called every frame manager update and it contains the 
     * delta time from last tick.
     */
    public interface ITask {
        boolean tick(double delta);
    }
}
