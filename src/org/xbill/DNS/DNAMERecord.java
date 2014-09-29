package org.xbill.DNS;

public class DNAMERecord extends SingleNameBase
{
  private static final long serialVersionUID = 2670767677200844154L;

  DNAMERecord()
  {
  }

  public DNAMERecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 39, paramInt, paramLong, paramName2, "alias");
  }

  public Name getAlias()
  {
    return getSingleName();
  }

  Record getObject()
  {
    return new DNAMERecord();
  }

  public Name getTarget()
  {
    return getSingleName();
  }
}