package org.xbill.DNS;

import java.io.IOException;

public class WireParseException extends IOException
{
  public WireParseException()
  {
  }

  public WireParseException(String paramString)
  {
    super(paramString);
  }

  public WireParseException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    initCause(paramThrowable);
  }
}