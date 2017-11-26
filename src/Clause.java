import utils.None;
import utils.Option;
import utils.Some;

import java.util.HashSet;
import java.util.Set;

public class Clause {
  private final Set<Literal> literals;
  public final int size;
  public final int positiveLiterals;
  public final int negativeLiterals;

  public Clause(){
    literals = new HashSet<Literal>();
    size = 0;
    positiveLiterals = 0;
    negativeLiterals = 0;
  }

  public Clause(Set<Literal> requiredLiterals){
    literals = requiredLiterals;
    int sizeSoFar = 0;
    int positiveLiteralsSoFar = 0;
    int negativeLiteralsSoFar = 0;
    for (Literal literal : requiredLiterals){
      sizeSoFar++;
      if(literal.parity.equals(Parity.POSITIVE)){
        positiveLiteralsSoFar++;
      } else {
        negativeLiteralsSoFar++;
      }
    }
    size = sizeSoFar;
    positiveLiterals = positiveLiteralsSoFar;
    negativeLiterals = negativeLiteralsSoFar;
  }

  @Override
  public String toString() {
    return literals.toString();
  }

  public Set<Literal> getLiterals() {
    return literals;
  }

  public boolean isHorn(){
    return positiveLiterals <= 1;
  }

  public boolean isAntiHorn(){
    return negativeLiterals <= 1;
  }

  public Option<Clause> propagate(Literal literalToCompare){
    Set<Literal> dupLiterals = new HashSet<Literal>();
    // our little internal mutable part that we will manipulate
    dupLiterals.addAll(literals);

    for (Literal literalInClause: dupLiterals){

      // literal not in dupLiterals
      if(!literalInClause.equals(literalToCompare)){
        continue;
      }

      // parities of the two literals match that means propagation removes this clause
      if (literalInClause.parity.equals(literalToCompare.parity)){
        return new None<Clause>();
      } else { // parities don't match that mean s we remove the corresponding literal from the set
        dupLiterals.remove(literalInClause);
        return new Some<Clause>(new Clause(dupLiterals));
      }
    }

    return new Some<Clause>(new Clause(dupLiterals));
  }
}
