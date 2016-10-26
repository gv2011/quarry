package pg.ffs;

import static java.util.stream.Collectors.toList;

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
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

class FlexibleFileSystem extends FileSystem{

  private final FfsProvider provider;
  private final Iterable<Path> rootDirectories;
  private final Set<String> fileAttributeViews;
  private final Path empty;


  FlexibleFileSystem(final FfsProvider provider) {
    this.provider = provider;

    final ArrayList<Path> dirs = new ArrayList<Path>(1);
    dirs.add(getPath("/"));
    rootDirectories = Collections.unmodifiableList(dirs);

    final Set<String> faws = new HashSet<>();
    faws.add("basic");

    fileAttributeViews = Collections.unmodifiableSet(faws);

    empty = new FfsPath(this, new String[0]);
  }

  @Override
  public FileSystemProvider provider() {
    return provider;
  }

  @Override
  public void close() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isOpen() {
    return true;
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  public String getSeparator() {
    return "/";
  }

  @Override
  public Iterable<Path> getRootDirectories() {
    return rootDirectories;
  }

  @Override
  public Iterable<FileStore> getFileStores() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> supportedFileAttributeViews() {
    return fileAttributeViews;
  }

  Path empty(){
    return empty;
  }

  @Override
  public Path getPath(final String first, final String... more) {
    String[] path;
    if(more.length==0){
      if(first.isEmpty()) path = new String[0];
      else path = FfsPath.parseToArray(first);
    }else{
      final List<String> nonEmptyElements =
        Stream.concat(
          Stream.of(first),
          Arrays.stream(more)
        )
        .filter(s->!s.isEmpty())
        .collect(toList())
      ;
      if(first.isEmpty() && nonEmptyElements.isEmpty()) path = new String[0];
      else path = nonEmptyElements.toArray(new String[nonEmptyElements.size()]);
    }
    return path.length==0 ? empty : new FfsPath(this, path);
  }

  @Override
  public PathMatcher getPathMatcher(final String syntaxAndPattern) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UserPrincipalLookupService getUserPrincipalLookupService() {
    throw new UnsupportedOperationException();
  }

  @Override
  public WatchService newWatchService() throws IOException {
    throw new UnsupportedOperationException();
  }

  Path getPath(final URI uri) {
    // TODO Auto-generated method stub
    return null;
  }

  SeekableByteChannel newByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) {
    // TODO Auto-generated method stub
    return null;
  }

  DirectoryStream<Path> newDirectoryStream(final Path dir, final Filter<? super Path> filter) {
    // TODO Auto-generated method stub
    return null;
  }

  void createDirectory(final Path dir, final FileAttribute<?>[] attrs) {
    // TODO Auto-generated method stub

  }

  void delete(final Path path) {
    // TODO Auto-generated method stub

  }

  void copy(final Path source, final Path target, final CopyOption[] options) {
    // TODO Auto-generated method stub

  }

  void move(final Path source, final Path target, final CopyOption[] options) {
    // TODO Auto-generated method stub

  }

  boolean isSameFile(final Path path1, final Path path2) {
    // TODO Auto-generated method stub
    return false;
  }

  void checkAccess(final Path path, final AccessMode[] modes) {
    // TODO Auto-generated method stub

  }

  BasicFileAttributeView getFileAttributeView(final Path path, final LinkOption[] options) {
    // TODO Auto-generated method stub
    return null;
  }

  BasicFileAttributeView readAttributes(final Path path, final LinkOption[] options) {
    // TODO Auto-generated method stub
    return null;
  }

  Map<String, Object> readAttributesMap(final Path path, final LinkOption[] options) {
    // TODO Auto-generated method stub
    return null;
  }

  void setAttribute(final Path path, final LinkOption[] options) {
    // TODO Auto-generated method stub

  }

}
