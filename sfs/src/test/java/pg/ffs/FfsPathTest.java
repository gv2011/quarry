package pg.ffs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

public class FfsPathTest {

  @Test
  @Ignore //TODO fails
  public void testParseToArray() {
    assertThat(Arrays.asList(FfsPath.parseToArray("")), is(Arrays.asList(new String[]{})));
    assertThat(Arrays.asList(FfsPath.parseToArray("/")), is(Arrays.asList(new String[]{""})));
    assertThat(Arrays.asList(FfsPath.parseToArray("\"")), is(Arrays.asList(new String[]{""})));
  }

  @Test
  @Ignore //TODO fails
  public void testNew() {
    final FlexibleFileSystem fs = (FlexibleFileSystem) new FfsProvider().getFileSystem(null);
    assertThat(new FfsPath(fs , new String[]{"/"}).toString(), is(""));
  }

  @Test
  public void testToString() {
    final FlexibleFileSystem fs = (FlexibleFileSystem) new FfsProvider().getFileSystem(null);
    final FfsPath p1 = new FfsPath(fs , new String[]{"/"});
    assertThat(p1.asList(), is(Arrays.asList(new String[]{"/"})));
    assertThat(p1.toString(), is("\"/\""));

    final FfsPath p2 = new FfsPath(fs , new String[]{"abc","\"Hallo\"","def/ghi"});
    assertThat(p2.asList(), is(Arrays.asList(new String[]{"abc","\"Hallo\"","def/ghi"})));
    assertThat(p2.toString(), is("abc/\"\\\"Hallo\\\"\"/\"def/ghi\""));
  }

  @Test
  public void testJson() {
    for(int i=0; i<32; i++){
      System.out.println(i+": "+new Gson().toJson(""+((char)i)));
    }
    for(char c=32; c<34; c++){
      final String json = new Gson().toJson(""+c);
      System.out.println(((int)c)+": "+json);
      assertThat(new Gson().toJson(""+c), is("\""+c+"\""));
    }
    for(char c=35; c<38; c++){
      final String json = new Gson().toJson(""+c);
      System.out.println(((int)c)+": "+c+" : "+json);
      assertThat(new Gson().toJson(""+c), is("\""+c+"\""));
    }
    for(char c=40; c<60; c++){
      final String json = new Gson().toJson(""+c);
      System.out.println(((int)c)+": "+c+" : "+json);
      assertThat(new Gson().toJson(""+c), is("\""+c+"\""));
    }
    for(char c=63; c<92; c++){
      final String json = new Gson().toJson(""+c);
      System.out.println(((int)c)+": "+c+" : "+json);
      assertThat(new Gson().toJson(""+c), is("\""+c+"\""));
    }
    for(char c=93; c<8232; c++){
      final String json = new Gson().toJson(""+c);
      System.out.println(((int)c)+": "+c+" : "+json);
      assertThat(new Gson().toJson(""+c), is("\""+c+"\""));
    }
    for(char c=8234; c<=Character.MAX_VALUE && c>0; ){
      final String json = new Gson().toJson(""+c);
      System.out.println(((int)c)+": "+c+" : "+json);
      assertThat(new Gson().toJson(""+c), is("\""+c+"\""));
      c++;
    }
    final char c = Character.MAX_VALUE;
    System.out.println(((int)c)+": "+c);
  }

  @Test
  public void testUri() throws URISyntaxException {
    System.out.println(new URI(null,null, "\"lala/ %&$'..lulu\\lila?a=6€", null));
  }

}
