package org.xbill.DNS;

public class NSAP_PTRRecord extends SingleNameBase
{
  private static final long serialVersionUID = 2386284746382064904L;

  NSAP_PTRRecord()
  {
  }

  public NSAP_PTRRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 23, paramInt, paramLong, paramName2, "target");
  }

  Record getObject()
  {
    return new NSAP_PTRRecord();
  }

  public Name getTarget()
  {
    return getSingleName();
  }
}