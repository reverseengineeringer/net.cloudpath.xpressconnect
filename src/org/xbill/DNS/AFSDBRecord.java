package org.xbill.DNS;

public class AFSDBRecord extends U16NameBase
{
  private static final long serialVersionUID = 3034379930729102437L;

  AFSDBRecord()
  {
  }

  public AFSDBRecord(Name paramName1, int paramInt1, long paramLong, int paramInt2, Name paramName2)
  {
    super(paramName1, 18, paramInt1, paramLong, paramInt2, "subtype", paramName2, "host");
  }

  public Name getHost()
  {
    return getNameField();
  }

  Record getObject()
  {
    return new AFSDBRecord();
  }

  public int getSubtype()
  {
    return getU16Field();
  }
}