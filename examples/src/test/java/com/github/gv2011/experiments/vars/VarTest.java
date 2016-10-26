package com.github.gv2011.experiments.vars;

import static com.github.gv2011.util.Verify.verify;
import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.github.gv2011.util.PingPong;

public class VarTest {

  private AbstractVar<StrValue> var1;
  private AbstractVar<StrValue> var2;
  private final PingPong pingPong = new PingPong();

  @Test
  public void test() throws InterruptedException {
    final AtomicBoolean failed = new AtomicBoolean();
    Thread.setDefaultUncaughtExceptionHandler((t, e)->{
      e.printStackTrace();
      failed.set(true);
    });

    final ExecutorService es = Executors.newCachedThreadPool();
    try{
      var1 = new LocalVar<>(es, new StrValue("a"));
      var2 = new LocalVar<>(es, new StrValue("b"));

      final Thread t1 = new Thread(this::one);
      t1.start();
      final Thread t2 = new Thread(this::two);
      t2.start();
      t1.join();
      t2.join();
      assertFalse(failed.get());
    }finally{
      es.shutdown();
      es.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
  }


  private void one(){
    Transaction.open();
    final String concat = var1.get().toString()+var2.get().toString();
    var1.set(new StrValue(concat));
    verify(var1.get().toString().equals("ab"));
    pingPong.ping();
    Transaction.commit();
    pingPong.ping();
  }

  private void two(){
    Transaction.open();
    pingPong.pong();
    verify(var1.get().toString().equals("a"));
    pingPong.pong();
    try{
      var1.get();
      fail();
    }catch(final TransactionFailedException e){}//expected
    Transaction.open();
    verify(var1.get().toString().equals("ab"));
  }


}
