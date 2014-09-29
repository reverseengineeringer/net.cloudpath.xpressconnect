package org.xbill.DNS;

public class InvalidTypeException extends IllegalArgumentException
{
  public InvalidTypeException(int paramInt)
  {
    super("Invalid DNS type: " + paramInt);
  }
}