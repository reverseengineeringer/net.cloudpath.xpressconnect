package org.xbill.DNS;

public class NSIDOption extends GenericEDNSOption
{
  private static final long serialVersionUID = 74739759292589056L;

  NSIDOption()
  {
    super(3);
  }

  public NSIDOption(byte[] paramArrayOfByte)
  {
    super(3, paramArrayOfByte);
  }
}