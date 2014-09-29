package org.xbill.DNS;

import java.io.IOException;

public class PXRecord extends Record
{
  private static final long serialVersionUID = 1811540008806660667L;
  private Name map822;
  private Name mapX400;
  private int preference;

  PXRecord()
  {
  }

  public PXRecord(Name paramName1, int paramInt1, long paramLong, int paramInt2, Name paramName2, Name paramName3)
  {
    super(paramName1, 26, paramInt1, paramLong);
    this.preference = checkU16("preference", paramInt2);
    this.map822 = checkName("map822", paramName2);
    this.mapX400 = checkName("mapX400", paramName3);
  }

  public Name getMap822()
  {
    return this.map822;
  }

  public Name getMapX400()
  {
    return this.mapX400;
  }

  Record getObject()
  {
    return new PXRecord();
  }

  public int getPreference()
  {
    return this.preference;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.preference = paramTokenizer.getUInt16();
    this.map822 = paramTokenizer.getName(paramName);
    this.mapX400 = paramTokenizer.getName(paramName);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.preference = paramDNSInput.readU16();
    this.map822 = new Name(paramDNSInput);
    this.mapX400 = new Name(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.preference);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.map822);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.mapX400);
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.preference);
    this.map822.toWire(paramDNSOutput, null, paramBoolean);
    this.mapX400.toWire(paramDNSOutput, null, paramBoolean);
  }
}