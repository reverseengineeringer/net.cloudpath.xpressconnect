package org.xbill.DNS;

public class InvalidDClassException extends IllegalArgumentException
{
  public InvalidDClassException(int paramInt)
  {
    super("Invalid DNS class: " + paramInt);
  }
}