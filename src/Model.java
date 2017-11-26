public class Model {

  public final int id;
  public final boolean constant;

  public Model(int requiredId, boolean requiredConstant){
    id = requiredId;
    constant = requiredConstant;
  }

  @Override
  public String toString() {
    return Integer.valueOf(id).toString() + ": " + constant;
  }
}
