public class Literal {
  public final int id;
  public final Parity parity;

  public Literal(int requiredId, Parity requiredParity){
    id = requiredId;
    parity = requiredParity;
  }

  @Override
  public String toString() {
    return "< \"" + id + "\" | " + parity.toString() + " >";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Literal)){
      return super.equals(o);
    }

    return id == ((Literal)o).id;
  }

  @Override
  public int hashCode(){
    return Integer.valueOf(id).hashCode();
  }
}
