package org.xbill.DNS;

public class RelativeNameException extends IllegalArgumentException
{
  public RelativeNameException(String paramString)
  {
    super(paramString);
  }

  public RelativeNameException(Name paramName)
  {
    super("'" + paramName + "' is not an absolute name");
  }
}