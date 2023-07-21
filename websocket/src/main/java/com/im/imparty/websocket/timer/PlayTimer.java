package com.im.imparty.websocket.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class PlayTimer {

    private TimeWatch stopWatch;

    private long startTime;

    private long totalTime;

    private Timer timer;

    private Function<Boolean, Void> callback;

    private TimerTask task = null;

    public boolean isRunning(){
        return stopWatch.isRunning();
    }

    public PlayTimer(long totalTime, Function<Boolean, Void> callback) {
        this.totalTime = totalTime;
        this.startTime = 0;
        this.callback = callback;
        timer = new Timer();
    }

    public void initTimer() {
        if (task != null) {
            task.cancel();
            timer.purge();
        }
        task = new PlayTask();
        timer.schedule(task, 5000, 1000);
    }

    public void play() {
        if (!stopWatch.isRunning()) {
            stopWatch.start();
            initTimer();
        }
    }

    public void play(long startTime) {
        if (startTime <= totalTime) {
            this.startTime = startTime;
            this.stopWatch = new TimeWatch();
            this.stopWatch.start();
            initTimer();
        }
    }

    public void playSong(long totalTime) {
        initTimer();
        this.totalTime = totalTime;
    }

    public void stop() {
        if (stopWatch.isRunning()) {
            stopWatch.stop();
        }
        if (task != null) {
            task.cancel();
        }
    }

    public long getCurrentTime() {
        return startTime + (stopWatch.getTotalTime() / 1000);
    }

    public boolean checkOver() {
        return getCurrentTime() >= totalTime;
    }

    public long remainTime() {
        return checkOver() ? 0 : totalTime - getCurrentTime();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        timer.cancel();
    }

    private class PlayTask extends TimerTask {
        @Override
        public void run() {
            if (checkOver()) {
                callback.apply(true);
                super.cancel();
            } else {
                callback.apply(false);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private class TimeWatch {

        private long startTimeStamp;

        private long totalTimeStamp;

        public TimeWatch() {
            this.startTimeStamp = 0;
            this.totalTimeStamp = 0;
        }

        public synchronized void start() {
            if (this.startTimeStamp == 0) {
                this.startTimeStamp = System.currentTimeMillis();
            }
        }

        public synchronized void stop() {
            if (this.startTimeStamp > 0) {
                this.totalTimeStamp += (System.currentTimeMillis() - startTimeStamp);
                this.startTimeStamp = 0;
            }
        }

        public boolean isRunning() {
            return this.startTimeStamp > 0;
        }

        public long getTotalTime() {
            return this.totalTimeStamp + (this.startTimeStamp > 0 ? (System.currentTimeMillis() - this.startTimeStamp) : 0);
        }
    }
}
