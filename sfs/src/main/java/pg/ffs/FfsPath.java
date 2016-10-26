package pg.ffs;


import static com.github.gv2011.util.Verify.verify;
import static com.github.gv2011.util.ex.Exceptions.notYetImplementedException;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

public final class FfsPath implements Path{

  private final FlexibleFileSystem fileSystem;
  private final String[] path;
  private final int offset;
  private final int size;

  FfsPath(final FlexibleFileSystem fileSystem, final String[] path) {
    this.fileSystem = fileSystem;
    this.path = path;
    offset = 0;
    size = path.length;
  }

  FfsPath(final FlexibleFileSystem fileSystem, final String[] path, final int offset, final int size) {
    assert(
      size>0 &&
      offset>=0 &&
      size+offset<=path.length
    );
    this.fileSystem = fileSystem;
    this.path = path;
    this.offset = offset;
    this.size = size;
  }

  private Path newPath(final int offset, final int size) {
    if(size==0) return new FfsPath(fileSystem, null, 0, 0);
    else{
      assert
        offset>=this.offset &&
        size>0 &&
        size<=this.size &&
        offset+size<=this.offset+this.size
      ;
      return new FfsPath(fileSystem, path, this.offset, size);
    }
  }

  private Path newPath(final String[] strings) {
    if(strings.length==0) return empty();
    else return new FfsPath(fileSystem, strings, 0, strings.length);
  }

  private Path empty() {
    return size==0 ? this : new FfsPath(fileSystem, null, 0, 0);
  }

  static String[] parseToArray(final String p) {
    if(p.isEmpty()) return new String[0];
    else{
      if(p.trim().startsWith("[")){
        final List<?> list = new Gson().fromJson(p, List.class);
        verify(list.stream().allMatch(e->e==null?false:e.getClass()==String.class));
      }
      final List<String> result = new ArrayList<>();
      boolean finished = false;

      int i=0;
      while(!finished){
        if(p.charAt(i)=='\"'){
          i = readEscaped(p, i, result);
        }else{
          final int idx = p.indexOf('/',i);
          if(i==-1){
            result.add(p.substring(i, p.length()));
            finished = true;
          }else{
            result.add(p.substring(i, idx));
            i = idx+1;
            finished = i == p.length();
          }
        }
      }
      return result.toArray(new String[result.size()]);
    }
  }

  private static int readEscaped(final String p, int i, final List<String> result) {
    boolean finished = false;
    final StringBuilder element = new StringBuilder();
    while(!finished){
      i++;
      final char c = p.charAt(i);
      if(c=='\\'){
        i++;
        element.append(p.charAt(i));
      }else{
        if(c=='\"') finished=true;
        else element.append(p.charAt(i));
      }
    }
    result.add(element.toString());
    return i;
  }

  @Override
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  @Override
  public boolean isAbsolute() {
    return size==0?false:path[0].isEmpty();
  }

  @Override
  public Path getRoot() {
    return null;
  }

  @Override
  public Path getFileName() {
    return size==0?null:newPath(offset+size-1, 1);
  }

  @Override
  public Path getParent() {
    return size==0?null:newPath(offset, size-1);
  }

  @Override
  public int getNameCount() {
    return size;
  }

  @Override
  public Path getName(final int index) {
    return newPath(offset+index, 1);
  }

  public List<String> asList() {
    return new AbstractList<String>(){
      @Override
      public String get(final int index) {
        if(index<0||index>=size) throw new IndexOutOfBoundsException();
        return element(index);
      }
      @Override
      public int size() {return size;}
    };
  }

  @Override
  public Path subpath(final int beginIndex, final int endIndex) {
    return newPath(offset+beginIndex, endIndex-beginIndex);
  }

  private final String element(final int i){
    return path[offset+i];
  }

  @Override
  public boolean startsWith(final Path other) {
    if(!other.getFileSystem().equals(fileSystem)) return false;
    else{
      final FfsPath o = (FfsPath)other;
      if(o.size>size) return false;
      else{
        boolean equal = true;
        int i=0;
        while(equal && i<o.size){
          if(!element(i).equals(o.element(i))) equal = false;
          i++;
        }
        return equal;
      }
    }
  }

  @Override
  public boolean startsWith(final String other) {
    return startsWith(parse(other));
  }

  private Path parse(final String path) {
    final String[] array = parseToArray(path);
    return array.length==0 ? fileSystem.empty() : new FfsPath(fileSystem, array);
  }

  @Override
  public boolean endsWith(final Path other) {
    throw notYetImplementedException();
  }

  @Override
  public boolean endsWith(final String other) {
    return endsWith(parse(other));
  }

  @Override
  public Path normalize() {
    return this;
  }

  @Override
  public Path resolve(final Path other) {
    checkProvider(other);
    throw notYetImplementedException();
  }

  @Override
  public Path resolve(final String other) {
    return resolve(parse(other));
  }

  @Override
  public Path resolveSibling(final Path other) {
    checkProvider(other);
    throw notYetImplementedException();
  }

  @Override
  public Path resolveSibling(final String other) {
    return resolveSibling(parse(other));
  }

  @Override
  public Path relativize(final Path other) {
    checkProvider(other);
    throw notYetImplementedException();
  }

  @Override
  public URI toUri() {
    throw notYetImplementedException();
  }

  @Override
  public Path toAbsolutePath() {
    if(isAbsolute()) return this;
    else {
      final String[] abs = new String[size+1];
      abs[0] = "";
      System.arraycopy(path, offset, abs, 1, size);
      return newPath(abs);
    }
  }


  @Override
  public Path toRealPath(final LinkOption... options) throws IOException {
    return this;
  }

  @Override
  public File toFile() {
    throw new UnsupportedOperationException();
  }

  @Override
  public WatchKey register(final WatchService watcher, final Kind<?>[] events, final Modifier... modifiers) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public WatchKey register(final WatchService watcher, final Kind<?>... events) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterator<Path> iterator() {
    return Arrays.stream(path, offset, offset+size)
    .map(e->newPath(new String[]{e}))
    .collect(toList())
    .iterator();
  }

  @Override
  public int compareTo(final Path other) {
    checkProvider(other);
    throw notYetImplementedException();
  }

  private void checkProvider(final Path other) {
    if(!other.getFileSystem().equals(fileSystem)) throw new IllegalArgumentException();
  }

  @Override
  public int hashCode() {
    int result = 1;
    for(int i=offset; i<offset+size; i++) result = 31 * result + path[i].hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == this) return true;
    else if(other==null) return false;
    else if(other.getClass()!=FfsPath.class) return false;
    else{
      final FfsPath o = (FfsPath)other;
      if(size!=o.size) return false;
      else{
        boolean equal = true;
        final int i=0;
        while(equal && i<size){
          if(!element(i).equals(o.element(i))) equal = false;
        }
        return equal;
      }
    }
  }

  @Override
  public String toString() {
    final StringBuilder result = new StringBuilder();
    for(int i=0; i<size; i++){
      if(i!=0) result.append('/');
      final String e = element(i);
      result.append(escape(e));
    }
    return result.toString();
  }

  private String escape(final String e) {
    final String json = new Gson().toJson(e);
    if(!json.substring(1, json.length()-1).equals(e) || e.indexOf('/')!=-1) return json;
    else return e;
  }

  private String escape2(final String e) {
    if(e.isEmpty()) return e;
    else{
      StringBuilder result = null;
      int i=0;
      while(i<e.length()){
        char c = e.charAt(i);
        if(c<' ' || c=='\"' || c=='\\' || c=='/' || c==' '){
          result = new StringBuilder("\"");
          result.append(e, 0, i);
          if(c<' ' || c=='\"' || c=='\\')result.append('\\');
          result.append(c);
          i++;
          while(i<e.length()){
            c = e.charAt(i);
            if(c<' ' || c=='\"' || c=='\\')result.append('\\');
            result.append(c);
            i++;
          }
        }
        else i++;
      }
      return result==null?e:result.append("\"").toString();
    }
  }


}
