package eu.letero.venonta.test.experiments.performance;

import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

public class CastTest {

@Test
public void test() {
  final Duration testTime = Duration.ofMillis(500);
  final Instant until = Instant.now().plus(testTime);
  long i=0;
  final ClassA a = new ClassB();
  final ClassB b = new ClassB();
  Interrupt.interruptIn(testTime);
  while(!Thread.interrupted()){
//    i++;
    i = ((ClassB)a).increment(i);
//    i = b.increment(i);
  }
  System.out.println((testTime.toMillis()*1000000f/i)+" ns");
}

}
