package org.xbill.DNS;

import java.util.List;

public class SPFRecord extends TXTBase
{
  private static final long serialVersionUID = -2100754352801658722L;

  SPFRecord()
  {
  }

  public SPFRecord(Name paramName, int paramInt, long paramLong, String paramString)
  {
    super(paramName, 99, paramInt, paramLong, paramString);
  }

  public SPFRecord(Name paramName, int paramInt, long paramLong, List paramList)
  {
    super(paramName, 99, paramInt, paramLong, paramList);
  }

  Record getObject()
  {
    return new SPFRecord();
  }
}