package pg.ffs;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

public class FfsProvider extends FileSystemProvider{
  
  private FlexibleFileSystem fs;

  public FfsProvider(){
    fs = new FlexibleFileSystem(this);
  }

  @Override
  public String getScheme() {
    return "ffs";
  }

  @Override
  public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
    return fs;
  }

  @Override
  public FileSystem getFileSystem(URI uri) {
    return fs;
  }

  @Override
  public Path getPath(URI uri) {
    return fs.getPath(uri);
  }

  @Override
  public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
      throws IOException {
    return fs.newByteChannel(path, options, attrs);
  }

  @Override
  public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
    return fs.newDirectoryStream(dir, filter);
  }

  @Override
  public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
    fs.createDirectory(dir, attrs);
  }

  @Override
  public void delete(Path path) throws IOException {
    fs.delete(path);
  }

  @Override
  public void copy(Path source, Path target, CopyOption... options) throws IOException {
    fs.copy(source, target, options);    
  }

  @Override
  public void move(Path source, Path target, CopyOption... options) throws IOException {
    fs.move(source, target, options);    
  }

  @Override
  public boolean isSameFile(Path path1, Path path2) throws IOException {
    return fs.isSameFile(path1, path2);
  }

  @Override
  public boolean isHidden(Path path) throws IOException {
    return false;
  }

  @Override
  public FileStore getFileStore(Path path) throws IOException {
    return null;
  }

  @Override
  public void checkAccess(Path path, AccessMode... modes) throws IOException {
    fs.checkAccess(path, modes);
    
  }

  @SuppressWarnings("unchecked")
  @Override
  public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
    if(type.equals(BasicFileAttributeView.class)) return (V) fs.getFileAttributeView(path, options);
    else return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options)
      throws IOException {
    return (A) fs.readAttributes(path, options);
  }

  @Override
  public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
    return fs.readAttributesMap(path, options);
  }

  @Override
  public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
    fs.setAttribute(path, options);    
  }

}
