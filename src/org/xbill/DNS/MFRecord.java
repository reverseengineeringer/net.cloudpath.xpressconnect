package org.xbill.DNS;

public class MFRecord extends SingleNameBase
{
  private static final long serialVersionUID = -6670449036843028169L;

  MFRecord()
  {
  }

  public MFRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 4, paramInt, paramLong, paramName2, "mail agent");
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
    return new MFRecord();
  }
}