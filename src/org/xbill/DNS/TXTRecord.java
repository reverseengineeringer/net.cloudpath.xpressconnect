package org.xbill.DNS;

import java.util.List;

public class TXTRecord extends TXTBase
{
  private static final long serialVersionUID = -5780785764284221342L;

  TXTRecord()
  {
  }

  public TXTRecord(Name paramName, int paramInt, long paramLong, String paramString)
  {
    super(paramName, 16, paramInt, paramLong, paramString);
  }

  public TXTRecord(Name paramName, int paramInt, long paramLong, List paramList)
  {
    super(paramName, 16, paramInt, paramLong, paramList);
  }

  Record getObject()
  {
    return new TXTRecord();
  }
}