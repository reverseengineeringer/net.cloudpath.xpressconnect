package org.xbill.DNS;

public class CNAMERecord extends SingleCompressedNameBase
{
  private static final long serialVersionUID = -4020373886892538580L;

  CNAMERecord()
  {
  }

  public CNAMERecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 5, paramInt, paramLong, paramName2, "alias");
  }

  public Name getAlias()
  {
    return getSingleName();
  }

  Record getObject()
  {
    return new CNAMERecord();
  }

  public Name getTarget()
  {
    return getSingleName();
  }
}