import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class LiteralTest {

  private static Literal literal1;
  private static Literal literal2;
  private static Literal literal3;

  @BeforeClass
  public static void setupLiterals(){
    literal1 = new Literal(0, Parity.POSITIVE);
    literal2 = new Literal(1, Parity.POSITIVE);
    literal3 = new Literal(1, Parity.NEGATIVE);
  }

  @Test
  public void testLiteralIsCreatedProperly(){
    assertEquals("Literal ID is incorrect", literal1.id, 0);
    assertEquals("Literal Parity is incorrect", literal1.parity, Parity.POSITIVE);
  }

  @Test
  public void testLiteralEqualsMethod(){
    assertTrue("Literals with the same ID aren't equal", literal2.equals(literal3));
    assertFalse("Literals with deiiferent IDs are equal", literal1.equals(literal2));
  }



}
