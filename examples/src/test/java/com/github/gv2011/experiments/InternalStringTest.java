package com.github.gv2011.experiments;

import static com.github.gv2011.util.ex.Exceptions.bug;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.junit.Test;

public class InternalStringTest {

//@Test Manual execution only
public void test() {
  long i=0;
  final WeakHashMap<String,Integer> numeric = new WeakHashMap<>();
  final List<Reference<String>> byInt = new ArrayList<>();
  Integer pos = Integer.valueOf(0);
  while(true){
    String s = null;
    for(int j=0; j<1000; j++){
      s = ("S-"+i).intern();
      boolean done = false;
      if(pos>=byInt.size()) pos=0;
      if(!byInt.isEmpty()){
        final int start = pos;
        while(!done){
          final String string = byInt.get(pos).get();
          if(string==null) done = true;
          else {
            if(++pos>=byInt.size()) pos=0;
            if(pos==start){
              pos = byInt.size();
              done = true;
            }
          }
        }
      }
      final Integer before = numeric.put(s, pos);
      if(before!=null) throw bug();
      if(pos<byInt.size()) {
        byInt.set(pos, new WeakReference<>(s));
        System.out.println(pos+" = "+s+" ("+byInt.size()+")");
      }
      else if(pos==byInt.size()) {
        byInt.add(new WeakReference<>(s));
      }
      else throw bug();
      i++;
      if(i>Integer.MAX_VALUE) throw new RuntimeException(""+i);
    }
//    System.out.println(byInt.size());
  }
}

}
