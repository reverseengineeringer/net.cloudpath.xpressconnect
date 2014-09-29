package org.bouncycastle2.util.io.pem;

import java.io.IOException;

public class PemGenerationException extends IOException
{
  private Throwable cause;

  public PemGenerationException(String paramString)
  {
    super(paramString);
  }

  public PemGenerationException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.cause = paramThrowable;
  }

  public Throwable getCause()
  {
    return this.cause;
  }
}