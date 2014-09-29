package org.bouncycastle2.openssl;

import java.io.IOException;

public class EncryptionException extends IOException
{
  private Throwable cause;

  public EncryptionException(String paramString)
  {
    super(paramString);
  }

  public EncryptionException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.cause = paramThrowable;
  }

  public Throwable getCause()
  {
    return this.cause;
  }
}