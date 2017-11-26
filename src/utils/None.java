package utils;

public class None<T> implements Option<T> {

  @Override
  public T get() throws NullPointerException {
    throw new NullPointerException("get() from a utils.None type");
  }

  @Override
  public T getOrElse(T type) {
    return type;
  }

  public boolean isSome(){
    return false;
  }

  public boolean isNone() {
    return true;
  }
}
