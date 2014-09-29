package org.xbill.DNS;

public class MDRecord extends SingleNameBase
{
  private static final long serialVersionUID = 5268878603762942202L;

  MDRecord()
  {
  }

  public MDRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 3, paramInt, paramLong, paramName2, "mail agent");
  }

  public Name getAdditionalName()
  {
    return getSingleName();
  }

  public Name getMailAgent()
  {
    return getSingleName();
  }

  Record getObject()
  {
    return new MDRecord();
  }
}