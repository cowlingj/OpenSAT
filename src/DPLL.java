import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.None;
import utils.Option;
import utils.Some;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DPLL {

  public static void main(String[] args) {
    String file = null;
    for (int i = 0; i < args.length; i++){
      if (args[i].equals("-f")){
        file = args[++i];
      }
    }

    ClauseSet cs;

    try {
      if (file != null) {
        cs = ClauseSetBuilder.file(file).build();
      } else {
        cs = ClauseSetBuilder.stdin().build();
      }

      System.out.println(cs);
      Option<Set<Model>> possibleSolution = DPLL.solve(cs);

      if (possibleSolution.isSome()) {
        System.out.println("SATISFIABLE");
      } else {
        System.out.println("UNSATISFIABLE");
      }
    } catch (IOException ioe){
      System.err.println("Exception in building ClauseSet " + ioe.getMessage());
      System.exit(-1);
    }
  }

  public static Option<Set<Model>> solve(ClauseSet clauses) throws NotImplementedException {

    if( !clauses.type.equals("p") || !clauses.form.equals("cnf")){
      throw new NotImplementedException();
    }

    //
    // SPECIAL CASES
    //

    // solved - base case
    if (clauses.isEmpty()){
      return new Some<>(new HashSet<Model>());
    }

    // solution - horn and anti horn
    if (clauses.isUsefulHorn()){
      Set<Model> model = new HashSet<>();
      for (Literal l : clauses.getLiterals()){
        model.add(new Model(l.id, false));
      }
      return new Some<>(model);
    }
    if (clauses.isUsefulAntiHorn()){
      Set<Model> model = new HashSet<>();
      for (Literal l : clauses.getLiterals()){
        model.add(new Model(l.id, true));
      }
      return new Some<>(model);
    }

    //
    // GENERAL CASE
    //

    // look for lone literals to propagate
    // while we're looking at clauses, we can check for the empty clause
    Set<Literal> loneLiterals = new HashSet<>();
    for (Clause clause: clauses.getClauses()) {

      switch (clause.size) {
        case 0: // we have an enpty clause, UNSATISFIABLE
          return new None<>();
        case 1: // we have a single literal in a clause -
          loneLiterals.add(clause.getLiterals().iterator().next());
          break;
      }
    }

    Set<Model> model = new HashSet<>();
    ClauseSet dupClauseSet = new ClauseSet(clauses.getClauses(), clauses.type, clauses.form); // <- remove as soon as possible


    // do actual propagation
    if (loneLiterals.size() > 0) {
      // COLLECT!!!
      // return DPLL.solve(loneLiterals.stream().reduce(new ClauseSet(), (clauseSet, lit)->{clauses.propagate(lit);}));
      for (Literal literal : loneLiterals) {
        model.add(new Model(literal.id, literal.parity.equals(Parity.POSITIVE)));
        dupClauseSet = new ClauseSet(dupClauseSet.propagate(literal).getClauses(), dupClauseSet.type, dupClauseSet.form);
      }
      Option<Set<Model>> possibleSolution = DPLL.solve(dupClauseSet);
      if (possibleSolution.isSome()){
        possibleSolution.get().addAll(model);
      }
      return possibleSolution;
    } else { // cannot propagate, must split
      // dupClauses not empty, one of the first things we check is for the Set<Clause> being empty
      Literal firstLiteral = clauses.getLiterals().iterator().next();

      // first literal = 1
      Option<Set<Model>> positiveSolve = DPLL.solve(clauses.splitPositive(firstLiteral.id));
      if(positiveSolve.isSome()){
        positiveSolve.get().add(new Model(firstLiteral.id, true));
        return positiveSolve;
      }

      // first literal = 0
      Option<Set<Model>> negativeSolve = DPLL.solve(clauses.splitNegative(firstLiteral.id));
      if(negativeSolve.isSome()){
        negativeSolve.get().add(new Model(firstLiteral.id, false));
        return negativeSolve;
      }

      // positive and negative split both UNSATISFIABLE
      return new None<>();
    }
  }
}
