package org.bouncycastle2.ocsp;

public class OCSPException extends Exception
{
  Exception e;

  public OCSPException(String paramString)
  {
    super(paramString);
  }

  public OCSPException(String paramString, Exception paramException)
  {
    super(paramString);
    this.e = paramException;
  }

  public Throwable getCause()
  {
    return this.e;
  }

  public Exception getUnderlyingException()
  {
    return this.e;
  }
}