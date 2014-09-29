package jcifs.util;

import java.io.PrintStream;

public class LogStream extends PrintStream
{
  private static LogStream inst;
  public static int level = 1;

  public LogStream(PrintStream paramPrintStream)
  {
    super(paramPrintStream);
  }

  public static LogStream getInstance()
  {
    if (inst == null)
      setInstance(System.err);
    return inst;
  }

  public static void setInstance(PrintStream paramPrintStream)
  {
    inst = new LogStream(paramPrintStream);
  }

  public static void setLevel(int paramInt)
  {
    level = paramInt;
  }
}