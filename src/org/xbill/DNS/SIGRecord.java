package org.xbill.DNS;

import java.util.Date;

public class SIGRecord extends SIGBase
{
  private static final long serialVersionUID = 4963556060953589058L;

  SIGRecord()
  {
  }

  public SIGRecord(Name paramName1, int paramInt1, long paramLong1, int paramInt2, int paramInt3, long paramLong2, Date paramDate1, Date paramDate2, int paramInt4, Name paramName2, byte[] paramArrayOfByte)
  {
    super(paramName1, 24, paramInt1, paramLong1, paramInt2, paramInt3, paramLong2, paramDate1, paramDate2, paramInt4, paramName2, paramArrayOfByte);
  }

  Record getObject()
  {
    return new SIGRecord();
  }
}