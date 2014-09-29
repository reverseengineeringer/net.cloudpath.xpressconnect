package org.bouncycastle2.openssl;

import java.io.IOException;

public class PEMException extends IOException
{
  Exception underlying;

  public PEMException(String paramString)
  {
    super(paramString);
  }

  public PEMException(String paramString, Exception paramException)
  {
    super(paramString);
    this.underlying = paramException;
  }

  public Throwable getCause()
  {
    return this.underlying;
  }

  public Exception getUnderlyingException()
  {
    return this.underlying;
  }
}