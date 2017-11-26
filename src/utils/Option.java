package utils;

public interface Option<T> {
  T get() throws NullPointerException;
  T getOrElse(T type);
  boolean isSome();
  boolean isNone();
}
