package org.xbill.DNS;

public class InvalidTTLException extends IllegalArgumentException
{
  public InvalidTTLException(long paramLong)
  {
    super("Invalid DNS TTL: " + paramLong);
  }
}