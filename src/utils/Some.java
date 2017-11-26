package utils;

public class Some<T> implements Option<T> {
  private final T contents;
  public Some(T type){
    contents = type;
  }

  @Override
  public T get() throws NullPointerException {
    return contents;
  }

  @Override
  public T getOrElse(T type) {
    return contents;
  }

  public boolean isSome(){
    return true;
  }

  public boolean isNone() {
    return false;
  }

}
