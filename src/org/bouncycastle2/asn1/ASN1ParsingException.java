package org.bouncycastle2.asn1;

public class ASN1ParsingException extends IllegalStateException
{
  private Throwable cause;

  ASN1ParsingException(String paramString)
  {
    super(paramString);
  }

  ASN1ParsingException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.cause = paramThrowable;
  }

  public Throwable getCause()
  {
    return this.cause;
  }
}