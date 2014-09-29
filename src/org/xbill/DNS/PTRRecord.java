package org.xbill.DNS;

public class PTRRecord extends SingleCompressedNameBase
{
  private static final long serialVersionUID = -8321636610425434192L;

  PTRRecord()
  {
  }

  public PTRRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 12, paramInt, paramLong, paramName2, "target");
  }

  Record getObject()
  {
    return new PTRRecord();
  }

  public Name getTarget()
  {
    return getSingleName();
  }
}