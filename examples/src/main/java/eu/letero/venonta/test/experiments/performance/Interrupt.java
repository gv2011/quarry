package eu.letero.venonta.test.experiments.performance;

import java.time.Duration;

public class Interrupt {

public static void interruptIn(final Duration time){
  final Thread toInterrupt = Thread.currentThread();
  final Thread it = new Thread(
    ()->{
      try {
        Thread.sleep(time.toMillis());
      } catch (final InterruptedException e) {throw new RuntimeException(e);}
      toInterrupt.interrupt();
    },
    "Interruptor"
  );
  it.setDaemon(true);
  it.start();
}


}
