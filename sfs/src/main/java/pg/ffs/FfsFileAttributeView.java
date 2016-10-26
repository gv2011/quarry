package pg.ffs;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class FfsFileAttributeView implements BasicFileAttributeView{

  @Override
  public String name() {
    return "basic";
  }

  @Override
  public BasicFileAttributes readAttributes() throws IOException {
    return null;
  }

  @Override
  public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
    // TODO Auto-generated method stub
    
  }

}
