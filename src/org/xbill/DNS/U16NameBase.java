package org.xbill.DNS;

import java.io.IOException;

abstract class U16NameBase extends Record
{
  private static final long serialVersionUID = -8315884183112502995L;
  protected Name nameField;
  protected int u16Field;

  protected U16NameBase()
  {
  }

  protected U16NameBase(Name paramName, int paramInt1, int paramInt2, long paramLong)
  {
    super(paramName, paramInt1, paramInt2, paramLong);
  }

  protected U16NameBase(Name paramName1, int paramInt1, int paramInt2, long paramLong, int paramInt3, String paramString1, Name paramName2, String paramString2)
  {
    super(paramName1, paramInt1, paramInt2, paramLong);
    this.u16Field = checkU16(paramString1, paramInt3);
    this.nameField = checkName(paramString2, paramName2);
  }

  protected Name getNameField()
  {
    return this.nameField;
  }

  protected int getU16Field()
  {
    return this.u16Field;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.u16Field = paramTokenizer.getUInt16();
    this.nameField = paramTokenizer.getName(paramName);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.u16Field = paramDNSInput.readU16();
    this.nameField = new Name(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.u16Field);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.nameField);
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.u16Field);
    this.nameField.toWire(paramDNSOutput, null, paramBoolean);
  }
}