package com.github.gv2011.jsplit.imp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.gv2011.jsplit.SliceWriter;
import com.github.gv2011.quarry.cfs.HashImp;
import com.github.gv2011.quarry.util.Hash;

class Index {

  private final List<Entry> entries = new ArrayList<>();
  private final int headerSize = 5;
  private final int entrySize = Entry.SIZE;

  private final int blockSize;
  private final int maxEntries;
  private final SliceWriter writer;


  Index(final SliceWriter writer) {
    this(writer, 8192);
  }


  Index(final SliceWriter writer, final int blockSize) {
    this.blockSize = blockSize;
    maxEntries = (blockSize-headerSize)/entrySize;
    assert maxEntries>0;
    assert blockSize>0 && blockSize<=0xFFFF;
    this.writer = writer;
  }


  private boolean inlined;


  Hash split(final InputStream in) {
    final byte[] buffer = new byte[blockSize];
    boolean done = false;
    int sizePl = 0;
    while(!done){
      sizePl = readBlock(in, buffer);
      if(sizePl<blockSize) done=true;
      else{
        final Entry entry = finishData(buffer, sizePl);
        write(buffer, entry);
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
      write(buffer, entry);
      add(entry);
      entry = finishIndex(buffer);
    }
    write(buffer, entry);
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

  private void write(final byte[] buffer, final Entry entry) {
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
    offset+=writeShort(entriesCount, buffer, offset);
    final int lastEntrySize = entriesCount==0?0:entries.get(entriesCount-1).size;
    offset+=writeShort(lastEntrySize, buffer, offset);
    buffer[offset++] = (byte) (inlined?1:0);
    assert offset==headerSize;
    for(final Entry e: entries){
      e.write(buffer, offset);
      offset+=entrySize;
    }
    return new Entry(buffer, offset);
  }

  private int writeShort(final int sh, final byte[] buffer, final int offset) {
    assert sh>=0 && sh <= 0xFFFF;
    //contract.
    buffer[offset] = (byte) (sh>>8);
    buffer[offset+1] = (byte) sh;
    return 2;
  }

  private static class Entry{
    public static final int SIZE = HashImp.SIZE;
    private final int size;
    private final Hash hash;
    private Entry(final byte[] buffer, final int size) {
      this.size = size;
      hash = HashImp.digest(buffer, 0, size);
    }
    int write(final byte[] buffer, final int offset) {
      return hash.write(buffer, offset);
    }
  }


}
