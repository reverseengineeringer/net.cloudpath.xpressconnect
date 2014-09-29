package org.xbill.DNS;

public class NSRecord extends SingleCompressedNameBase
{
  private static final long serialVersionUID = 487170758138268838L;

  NSRecord()
  {
  }

  public NSRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 2, paramInt, paramLong, paramName2, "target");
  }

  public Name getAdditionalName()
  {
    return getSingleName();
  }

  Record getObject()
  {
    return new NSRecord();
  }

  public Name getTarget()
  {
    return getSingleName();
  }
}