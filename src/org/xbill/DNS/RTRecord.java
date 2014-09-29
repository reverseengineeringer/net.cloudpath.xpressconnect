package org.xbill.DNS;

public class RTRecord extends U16NameBase
{
  private static final long serialVersionUID = -3206215651648278098L;

  RTRecord()
  {
  }

  public RTRecord(Name paramName1, int paramInt1, long paramLong, int paramInt2, Name paramName2)
  {
    super(paramName1, 21, paramInt1, paramLong, paramInt2, "preference", paramName2, "intermediateHost");
  }

  public Name getIntermediateHost()
  {
    return getNameField();
  }

  Record getObject()
  {
    return new RTRecord();
  }

  public int getPreference()
  {
    return getU16Field();
  }
}