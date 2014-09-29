package org.bouncycastle2.util;

public abstract interface Selector extends Cloneable
{
  public abstract Object clone();

  public abstract boolean match(Object paramObject);
}