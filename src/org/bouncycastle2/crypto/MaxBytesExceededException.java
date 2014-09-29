package org.bouncycastle2.crypto;

public class MaxBytesExceededException extends RuntimeCryptoException
{
  public MaxBytesExceededException()
  {
  }

  public MaxBytesExceededException(String paramString)
  {
    super(paramString);
  }
}