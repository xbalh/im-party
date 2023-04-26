package com.im.imparty.websocket.timer;

import org.springframework.util.StopWatch;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class PlayTimer {

    private StopWatch stopWatch;

    private long startTime;

    private long totalTime;

    private Timer timer;

    private Function<Boolean, Void> callback;

    private TimerTask task = null;

    public PlayTimer(long totalTime, Function<Boolean, Void> callback) {
        this.totalTime = totalTime;
        this.startTime = 0;
        this.callback = callback;
        timer = new Timer();
    }

    public void initTimer() {
        if (task != null) {
            task.cancel();
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
            this.stopWatch = new StopWatch();
            this.stopWatch.start();
            initTimer();
        }
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
        stopWatch.stop();
        stopWatch.start();
        return startTime + (stopWatch.getTotalTimeNanos() / 1000000000);
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
}
