package org.xbill.DNS;

public class MBRecord extends SingleNameBase
{
  private static final long serialVersionUID = 532349543479150419L;

  MBRecord()
  {
  }

  public MBRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 7, paramInt, paramLong, paramName2, "mailbox");
  }

  public Name getAdditionalName()
  {
    return getSingleName();
  }

  public Name getMailbox()
  {
    return getSingleName();
  }

  Record getObject()
  {
    return new MBRecord();
  }
}