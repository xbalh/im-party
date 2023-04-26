package com.im.imparty.room;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

@Slf4j
public class StopWatchTest {

    @Test
    public void test1() throws InterruptedException {
        StopWatch stopWatch = new StopWatch("test");
        stopWatch.start("test1");
        Thread.sleep(1000);
        stopWatch.stop();
        Thread.sleep(2000);
        stopWatch.start("test2");
        Thread.sleep(1500);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

}
