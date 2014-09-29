package jcifs.util.transport;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TransportException extends IOException
{
  private Throwable rootCause;

  public TransportException()
  {
  }

  public TransportException(String paramString)
  {
    super(paramString);
  }

  public TransportException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.rootCause = paramThrowable;
  }

  public TransportException(Throwable paramThrowable)
  {
    this.rootCause = paramThrowable;
  }

  public Throwable getRootCause()
  {
    return this.rootCause;
  }

  public String toString()
  {
    if (this.rootCause != null)
    {
      StringWriter localStringWriter = new StringWriter();
      PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
      this.rootCause.printStackTrace(localPrintWriter);
      return super.toString() + "\n" + localStringWriter;
    }
    return super.toString();
  }
}