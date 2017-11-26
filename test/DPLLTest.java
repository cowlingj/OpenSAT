import org.junit.BeforeClass;
import org.junit.Test;
import utils.Option;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import static org.junit.Assert.fail;

public class DPLLTest {
  @BeforeClass
  public static void setupClass(){
    try{
      URL url = ClauseBuilderTest.class.getClassLoader().getResource("input2.txt");
      FileInputStream f = new FileInputStream(new File(url.toURI()));
      System.setIn(f);
    } catch (IOException | URISyntaxException | NullPointerException e) {
      fail("exception " + e);
    }
  }

  @Test
  public void testDPLL(){
    ClauseSet c;
    try {
      c = ClauseSetBuilder.stdin().build();
      Option<Set<Model>> s = DPLL.solve(c);
      System.out.println(c);

      if (s.isSome()){
        System.out.println("SAT: " + s.get());
      } else {
        System.out.println("UNSAT");
      }


    } catch (IOException ioe) {
      fail("builder failed to build exception: " + ioe);
    }
  }
}
