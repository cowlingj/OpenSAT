import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

public class ClauseBuilderTest {

  static InputStream stdin = System.in;

  @BeforeClass
  public static void setupClass(){
    try{

      URL url = ClauseBuilderTest.class.getClassLoader().getResource("input1.txt");
      FileInputStream f = new FileInputStream(new File(url.toURI()));
      System.setIn(f);
    } catch (IOException | URISyntaxException | NullPointerException e) {
      fail("exception " + e);
    }
  }

  @Test
  public void StdinBuilder(){
    ClauseSet c;
    try {
      c = ClauseSetBuilder.stdin().build();
      System.out.println(c);
    } catch (IOException ioe) {
      fail("builder failed to build exception: " + ioe);
    }
  }
}
