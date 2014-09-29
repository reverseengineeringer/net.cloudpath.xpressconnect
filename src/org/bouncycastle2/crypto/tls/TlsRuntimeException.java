package org.bouncycastle2.crypto.tls;

public class TlsRuntimeException extends RuntimeException
{
  private static final long serialVersionUID = 1928023487348344086L;
  Throwable e;

  public TlsRuntimeException(String paramString)
  {
    super(paramString);
  }

  public TlsRuntimeException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.e = paramThrowable;
  }

  public Throwable getCause()
  {
    return this.e;
  }
}