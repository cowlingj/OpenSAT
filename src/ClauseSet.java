import utils.Option;
import utils.Some;

import java.util.HashSet;
import java.util.Set;

public class ClauseSet {
  private final Set<Clause> clauses;
  private final Set<Literal> literals;
  public final String type;
  public final String form;


  public boolean isUsefulHorn(){
    for (Clause c: clauses){
      if (!c.isHorn() || c.size < 2){
        return false;
      }
    }
    return true;
  }

  public boolean isUsefulAntiHorn(){
    for (Clause c: clauses){
      if (!c.isAntiHorn() || c.size < 2){
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuffer b = new  StringBuffer();
    b.append("ClauseSet " + type + " " + form + " {");
    b.append(clauses.toString());
    b.append("}");

    return b.toString();
  }

  protected ClauseSet() {
    clauses = new HashSet<Clause>();
    literals = new HashSet<Literal>();
    type = "undefined";
    form = "undefined";
  }

  protected ClauseSet(Set<Clause> requiredClauses, String requiredType, String requiredForm){
    clauses = requiredClauses;
    type = requiredType;
    form = requiredForm;
    literals = new HashSet<Literal>();
    for (Clause c : requiredClauses){
      literals.addAll(c.getLiterals());
    }
  }

  public Set<Clause> getClauses() {
    return clauses;
  }

  public Set<Literal> getLiterals() {
    return literals;
  }

  public ClauseSet propagate(Literal literalToPropagate){
    Set<Clause> dupClauses = new HashSet<Clause>();

    for (Clause clause: clauses){
      Option<Clause> optionalPropagatedClause = clause.propagate(literalToPropagate);
      if (optionalPropagatedClause.isSome()){
        dupClauses.add(optionalPropagatedClause.get());
      }
    }

    return new ClauseSet(dupClauses, type, form);
  }

  public boolean isEmpty(){
    return clauses.isEmpty();
  }

  public ClauseSet splitPositive(int id) {
    return propagate(new Literal(id, Parity.POSITIVE));
  }

  public ClauseSet splitNegative(int id) {
    return propagate(new Literal(id, Parity.NEGATIVE));
  }

  public ClauseSet split(Literal literalToSplit) {
    return propagate(literalToSplit);
  }

}
