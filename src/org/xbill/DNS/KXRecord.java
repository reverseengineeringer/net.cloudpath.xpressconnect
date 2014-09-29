package org.xbill.DNS;

public class KXRecord extends U16NameBase
{
  private static final long serialVersionUID = 7448568832769757809L;

  KXRecord()
  {
  }

  public KXRecord(Name paramName1, int paramInt1, long paramLong, int paramInt2, Name paramName2)
  {
    super(paramName1, 36, paramInt1, paramLong, paramInt2, "preference", paramName2, "target");
  }

  public Name getAdditionalName()
  {
    return getNameField();
  }

  Record getObject()
  {
    return new KXRecord();
  }

  public int getPreference()
  {
    return getU16Field();
  }

  public Name getTarget()
  {
    return getNameField();
  }
}