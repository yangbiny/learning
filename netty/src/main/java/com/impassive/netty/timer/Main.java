package com.impassive.netty.timer;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author impassive
 */
public class Main {

  public static void main(String[] args) throws InterruptedException {

    HashedWheelTimer timer = new HashedWheelTimer();

    System.out.println(System.currentTimeMillis());

    Timeout timeout = timer.newTimeout(new TimeRunner(), 1, TimeUnit.SECONDS);
    TimerTask task = timeout.task();
    System.out.println(task);

    Thread.sleep(10 * 1000);
    timeout.cancel();
  }

}


class TimeRunner implements TimerTask {

  @Override
  public void run(Timeout timeout) throws Exception {
    System.out.println(System.currentTimeMillis());
  }

}
