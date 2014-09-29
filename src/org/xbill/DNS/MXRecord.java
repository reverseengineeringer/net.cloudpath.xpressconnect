package org.xbill.DNS;

public class MXRecord extends U16NameBase
{
  private static final long serialVersionUID = 2914841027584208546L;

  MXRecord()
  {
  }

  public MXRecord(Name paramName1, int paramInt1, long paramLong, int paramInt2, Name paramName2)
  {
    super(paramName1, 15, paramInt1, paramLong, paramInt2, "priority", paramName2, "target");
  }

  public Name getAdditionalName()
  {
    return getNameField();
  }

  Record getObject()
  {
    return new MXRecord();
  }

  public int getPriority()
  {
    return getU16Field();
  }

  public Name getTarget()
  {
    return getNameField();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.u16Field);
    this.nameField.toWire(paramDNSOutput, paramCompression, paramBoolean);
  }
}