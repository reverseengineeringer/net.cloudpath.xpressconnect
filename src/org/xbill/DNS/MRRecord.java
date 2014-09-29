package org.xbill.DNS;

public class MRRecord extends SingleNameBase
{
  private static final long serialVersionUID = -5617939094209927533L;

  MRRecord()
  {
  }

  public MRRecord(Name paramName1, int paramInt, long paramLong, Name paramName2)
  {
    super(paramName1, 9, paramInt, paramLong, paramName2, "new name");
  }

  public Name getNewName()
  {
    return getSingleName();
  }

  Record getObject()
  {
    return new MRRecord();
  }
}