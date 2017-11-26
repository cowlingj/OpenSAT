import org.junit.BeforeClass;
import org.junit.Test;
import utils.Option;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ClauseTest {

  private static Clause emptyClause;

  private static Clause hornClause1;
  private static Clause hornClause2;
  private static Clause notHornClause;

  private static Clause antiHornClause1;
  private static Clause antiHornClause2;
  private static Clause notAntiHornClause;


  private static Clause clause1;
  private static Clause clause2;
  private static Set<Literal> literals2;

  private static Literal positivePropagateLiteral;
  private static Clause clausePositivePropagate;
  private static Literal negativePropagateLiteral;
  private static Clause clauseNegativePropagate;

  @BeforeClass
  public static void setupClauses(){
    // empty
    emptyClause = new Clause();

    // horn
    Set<Literal> builder = new HashSet<>();
    builder.add(new Literal(0, Parity.POSITIVE));
    hornClause1 = new Clause(builder);
    builder.add(new Literal(1, Parity.NEGATIVE));
    builder.add(new Literal(2, Parity.NEGATIVE));
    hornClause2 = new Clause(builder);
    builder.add(new Literal(3, Parity.POSITIVE));
    notHornClause = new Clause(builder);

    // antihorn
    builder = new HashSet<>();
    builder.add(new Literal(0, Parity.NEGATIVE));
    antiHornClause1 = new Clause(builder);
    builder.add(new Literal(1, Parity.POSITIVE));
    builder.add(new Literal(2, Parity.POSITIVE));
    antiHornClause2 = new Clause(builder);
    builder.add(new Literal(3, Parity.NEGATIVE));
    notAntiHornClause = new Clause(builder);

    // clause1
    builder = new HashSet<>();
    builder.add(new Literal(0, Parity.POSITIVE));
    clause1 = new Clause(builder);

    // clause2
    builder = new HashSet<>();
    builder.add(new Literal(0, Parity.POSITIVE));
    builder.add(new Literal(1, Parity.POSITIVE));
    literals2 = new HashSet<>(builder);
    clause2 = new Clause(builder);

    // clausePositivePropagate
    builder = new HashSet<>();
    positivePropagateLiteral = new Literal(0, Parity.POSITIVE);
    builder.add(positivePropagateLiteral);
    builder.add(new Literal(1, Parity.POSITIVE));
    clausePositivePropagate = new Clause(builder);

    // clauseNegativePropagate
    builder = new HashSet<>();
    negativePropagateLiteral = new Literal(0, Parity.POSITIVE);
    Parity oppositeParity = negativePropagateLiteral.parity.equals(Parity.POSITIVE)? Parity.NEGATIVE : Parity.POSITIVE;
    builder.add(new Literal(negativePropagateLiteral.id, oppositeParity));
    builder.add(new Literal(1, Parity.POSITIVE));
    clauseNegativePropagate = new Clause(builder);

  }

  @Test
  public void testEmptyClause(){
    assertNotNull("emptyClause is null", emptyClause);
    assertNotNull("clause1 is null", clause1);

    assertEquals("Empty clause is not empty", 0, emptyClause.size);
    assertNotEquals("Non empty clause is empty", 0, clause1.size);
  }

  @Test
  public void testLiteralsAreInClause(){
    assertNotNull("clause2 is null", clause2);
    assertNotNull("Literals are null", clause2.getLiterals());

    assertNotEquals("Literals are not in clause", 0, clause2.getLiterals().size());
    assertEquals("Literals are not in the clause", literals2, clause2.getLiterals());
  }

  @Test
  public void testIsHornAndAntiHorn(){
    assertNotNull("hornClause1 is null", hornClause1);
    assertNotNull("hornClause2 is null", hornClause2);
    assertNotNull("notHornClause is null", notHornClause);
    assertNotNull("antiHornClause1 is null", antiHornClause1);
    assertNotNull("antiHornClause2 is null", antiHornClause2);
    assertNotNull("notAntiHornClause is null", notAntiHornClause);

    assertTrue("horn clause is not horn", hornClause1.isHorn());
    assertTrue("horn clause is not horn", hornClause2.isHorn());
    assertFalse("not horn clause is horn", notHornClause.isHorn());
    assertTrue("antiHorn clause is not antiHorn", antiHornClause1.isAntiHorn());
    assertTrue("antiHorn clause is not antiHorn", antiHornClause2.isAntiHorn());
    assertFalse("not antiHorn clause is antiHorn", notAntiHornClause.isAntiHorn());
  }

  @Test
  public void testPositivePropagate(){
    assertTrue("Clause should be none", clausePositivePropagate.propagate(positivePropagateLiteral).isNone());
  }

  @Test
  public void testNegativePropagate(){
    Option<Clause> postPropagate = clauseNegativePropagate.propagate(negativePropagateLiteral);
    assertTrue("clause should be some", postPropagate.isSome());
    assertEquals("clause does not have the correct amount of literals", 1, postPropagate.get().size);

  }
}
