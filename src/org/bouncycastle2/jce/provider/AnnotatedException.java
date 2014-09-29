package org.bouncycastle2.jce.provider;

import org.bouncycastle2.jce.exception.ExtException;

public class AnnotatedException extends Exception
  implements ExtException
{
  private Throwable _underlyingException;

  AnnotatedException(String paramString)
  {
    this(paramString, null);
  }

  AnnotatedException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this._underlyingException = paramThrowable;
  }

  public Throwable getCause()
  {
    return this._underlyingException;
  }

  Throwable getUnderlyingException()
  {
    return this._underlyingException;
  }
}