package com.github.gv2011.jsplit.imp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.gv2011.jsplit.SliceWriter;
import com.github.gv2011.quarry.bits.Serializer;
import com.github.gv2011.quarry.util.Hash;

class Index {

  private final int headerSize = 5;
  private final int entrySize = Entry.SIZE;

  private final int blockSize;
  private final int maxEntries;

  private final List<Entry> entries = new ArrayList<>();

  Index() {
    this(8192);
  }


  Index(final int blockSize) {
    this.blockSize = blockSize;
    maxEntries = (blockSize-headerSize)/entrySize;
    assert maxEntries>0;
    assert blockSize>0 && blockSize<=0xFFFF;
  }

  private boolean inlined;


  Hash split(final InputStream in, final SliceWriter writer) {
    final byte[] buffer = new byte[blockSize];
    boolean done = false;
    int sizePl = 0;
    while(!done){
      sizePl = readBlock(in, buffer);
      if(sizePl<blockSize) done=true;
      else{
        final Entry entry = finishData(buffer, sizePl);
        write(buffer, entry, writer);
        add(entry);
        }
    }
    Entry entry;
    final Optional<Entry> optEntry = tryAddAndFinish(buffer, sizePl);
    if(optEntry.isPresent()){
      entry = optEntry.get();
    }else if(sizePl==0){
      entry = finishIndex(buffer);
    }else{
      entry = finishData(buffer, sizePl);
      write(buffer, entry, writer);
      add(entry);
      entry = finishIndex(buffer);
    }
    write(buffer, entry, writer);
    return entry.hash;
  }

  private Entry finishData(final byte[] buffer, final int sizePl) {
    assert sizePl>=0 && sizePl<=blockSize;
    assert buffer.length>=sizePl;
    //contract.
    return new Entry(buffer, sizePl);
  }

  private int readBlock(final InputStream in, final byte[] buffer) {
    assert buffer.length == blockSize;
    boolean finished = false;
    int offset = 0;
    int length = blockSize;
    int total = 0;
    while(!finished){
      int count;
      try {
        count = in.read(buffer, offset, length);
      } catch (final IOException e) {throw new RuntimeException(e);}
      if(count==-1) finished = true;
      else{
        total  += count;
        offset += count;
        length -= count;
        if(length==0){
          finished = true;
        }
      }
    }
    assert total>=0 && total<=blockSize;
    return total;
  }

  private void write(final byte[] buffer, final Entry entry, final SliceWriter writer) {
    try(OutputStream out = writer.getOutputStream(entry.hash, entry.size)){
      out.write(buffer, 0, entry.size);
    } catch (final IOException e) {throw new RuntimeException(e);}
  }

  private void add(final Entry entry) {
    assert entries.size()<maxEntries;
    entries.add(entry);
  }



  boolean fits(final int sizePl) {
    return false;
  }

  Optional<Entry> tryAddAndFinish(final byte[] buffer, final int size) {
    return Optional.empty();
  }

  Entry finishIndex(final byte[] buffer) {
    assert !inlined;
    final int entriesCount = entries.size();
    assert entriesCount>=0 && entriesCount<=maxEntries;
    //contract.
    int offset = 0;
    offset+=Serializer.writeShort(entriesCount, buffer, offset);
    final int lastEntrySize = entriesCount==0?0:entries.get(entriesCount-1).size;
    offset+=Serializer.writeShort(lastEntrySize, buffer, offset);
    offset+=Serializer.writeBoolean(inlined, buffer, offset);
    assert offset==headerSize;
    for(final Entry e: entries){
      e.write(buffer, offset);
      offset+=entrySize;
    }
    return new Entry(buffer, offset);
  }

}
