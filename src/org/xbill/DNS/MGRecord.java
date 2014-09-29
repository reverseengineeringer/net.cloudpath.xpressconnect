package org.xbill.DNS;

public class MGRecord extends SingleNameBase
{
  private static final long serialVersionUID = -3980055550863644582L;

  MGRecord()
  {
  }

  public MGRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 8, paramInt, paramLong, paramName2, "mailbox");
  }

  public Name getMailbox()
  {
    return getSingleName();
  }

  Record getObject()
  {
    return new MGRecord();
  }
}