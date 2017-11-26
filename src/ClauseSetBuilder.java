import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClauseSetBuilder {

  private static Class type;
  private static BufferedReader source;
  private static String clauseProblem;
  private static String clauseForm;

  public static class StdBuilder {

    private StdBuilder(){} // only here can we build

    public ClauseSet build() throws IOException {
      try {
        String line = source.readLine();

        // skip comments at the start
        while (line != null && line.startsWith("c")){
          line = source.readLine();
        }

        // string is null so there is no preamble, err
        if (line == null) {
          throw new IOException("malformed preamble line: " + line);
        }

        // the number of clauses and literals stated in the preamble
        int noClauses;
        int noLiterals;

        try { // parse the preamble
          String[] preamble = line.split(" ");

          clauseProblem = preamble[0];
          clauseForm = preamble[1];

          noLiterals = Integer.parseInt(preamble[2]);
          noClauses = Integer.parseInt(preamble[3]);

        } catch (ArrayIndexOutOfBoundsException | NullPointerException | IllegalArgumentException e) {
          throw new IOException("malformed preamble", e);
        }

        // clauses for the ClauseSet
        Set<Clause> clauses = new HashSet<>();

        // read the all the clauses, the number of them being given in the preamble
        for (int body = 0; body < noClauses; body++){

          String currentLine = source.readLine(); // preamble string

          // skip comments
          if (currentLine != null && currentLine.startsWith("c")){ // skip comments
            continue;
          }


          if (currentLine == null){ // null line
            throw new IOException("malformed body: line is null");
          }

          // parse body
          String[] stringLiterals = currentLine.split(" ");

          // store the literals
          Set<Literal> clauseLiterals = new HashSet<>();
          // map to check for tautologies
          Map<Integer, Parity> mapToCheckTautology = new HashMap<>();

          boolean tautology = false;

          for (String stringLiteral: stringLiterals) {
            try {
              int literalAsInt = Integer.parseInt(stringLiteral);

              Parity parity = literalAsInt > 0? Parity.POSITIVE : Parity.NEGATIVE;
              int id = Math.abs(literalAsInt);

              if (id > noLiterals){
                throw new IOException("malformed body exceeded max int in preamble");
              }

              if (id == 0) { // end of line
                break;
              }

              // handle tautologies and repeats
              Parity nullableParity = mapToCheckTautology.put(id, parity);
              if (!clauseLiterals.add(new Literal(id, parity))){
                tautology = !nullableParity.equals(parity);
              }

              // we don't need to read anymore if we have a single tautology
              if (tautology){
                break;
              }


            } catch (IllegalArgumentException e) {
              throw new IOException("malformed body", e);
            }
          }

          // don't add tautological clauses to the set
          if (!tautology){
            clauses.add(new Clause(clauseLiterals));
          }
        }

        return new ClauseSet(clauses, clauseProblem, clauseForm);

      } catch (IOException ioe){
        System.err.println("Failed to read from stdin, clause set empty\nProblem: " + ioe);
        return new ClauseSet();
      }
    }

  }

  public static StdBuilder stdin(){

    type = String.class;
    source = new BufferedReader(new InputStreamReader(System.in));

    return new StdBuilder();
  }

  public static StdBuilder file(String fileName) throws IOException {

    type = String.class;
    source = new BufferedReader(new FileReader(fileName));

    return new StdBuilder();
  }


}
