package org.xbill.DNS;

import java.io.IOException;

public class GPOSRecord extends Record
{
  private static final long serialVersionUID = -6349714958085750705L;
  private byte[] altitude;
  private byte[] latitude;
  private byte[] longitude;

  GPOSRecord()
  {
  }

  public GPOSRecord(Name paramName, int paramInt, long paramLong, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    super(paramName, 27, paramInt, paramLong);
    validate(paramDouble1, paramDouble2);
    this.longitude = Double.toString(paramDouble1).getBytes();
    this.latitude = Double.toString(paramDouble2).getBytes();
    this.altitude = Double.toString(paramDouble3).getBytes();
  }

  public GPOSRecord(Name paramName, int paramInt, long paramLong, String paramString1, String paramString2, String paramString3)
  {
    super(paramName, 27, paramInt, paramLong);
    try
    {
      this.longitude = byteArrayFromString(paramString1);
      this.latitude = byteArrayFromString(paramString2);
      validate(getLongitude(), getLatitude());
      this.altitude = byteArrayFromString(paramString3);
      return;
    }
    catch (TextParseException localTextParseException)
    {
      throw new IllegalArgumentException(localTextParseException.getMessage());
    }
  }

  private void validate(double paramDouble1, double paramDouble2)
    throws IllegalArgumentException
  {
    if ((paramDouble1 < -90.0D) || (paramDouble1 > 90.0D))
      throw new IllegalArgumentException("illegal longitude " + paramDouble1);
    if ((paramDouble2 < -180.0D) || (paramDouble2 > 180.0D))
      throw new IllegalArgumentException("illegal latitude " + paramDouble2);
  }

  public double getAltitude()
  {
    return Double.parseDouble(getAltitudeString());
  }

  public String getAltitudeString()
  {
    return byteArrayToString(this.altitude, false);
  }

  public double getLatitude()
  {
    return Double.parseDouble(getLatitudeString());
  }

  public String getLatitudeString()
  {
    return byteArrayToString(this.latitude, false);
  }

  public double getLongitude()
  {
    return Double.parseDouble(getLongitudeString());
  }

  public String getLongitudeString()
  {
    return byteArrayToString(this.longitude, false);
  }

  Record getObject()
  {
    return new GPOSRecord();
  }

  // ERROR //
  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 119	org/xbill/DNS/Tokenizer:getString	()Ljava/lang/String;
    //   5: invokestatic 49	org/xbill/DNS/GPOSRecord:byteArrayFromString	(Ljava/lang/String;)[B
    //   8: putfield 38	org/xbill/DNS/GPOSRecord:longitude	[B
    //   11: aload_0
    //   12: aload_1
    //   13: invokevirtual 119	org/xbill/DNS/Tokenizer:getString	()Ljava/lang/String;
    //   16: invokestatic 49	org/xbill/DNS/GPOSRecord:byteArrayFromString	(Ljava/lang/String;)[B
    //   19: putfield 40	org/xbill/DNS/GPOSRecord:latitude	[B
    //   22: aload_0
    //   23: aload_1
    //   24: invokevirtual 119	org/xbill/DNS/Tokenizer:getString	()Ljava/lang/String;
    //   27: invokestatic 49	org/xbill/DNS/GPOSRecord:byteArrayFromString	(Ljava/lang/String;)[B
    //   30: putfield 42	org/xbill/DNS/GPOSRecord:altitude	[B
    //   33: aload_0
    //   34: aload_0
    //   35: invokevirtual 53	org/xbill/DNS/GPOSRecord:getLongitude	()D
    //   38: aload_0
    //   39: invokevirtual 56	org/xbill/DNS/GPOSRecord:getLatitude	()D
    //   42: invokespecial 24	org/xbill/DNS/GPOSRecord:validate	(DD)V
    //   45: return
    //   46: astore_3
    //   47: aload_1
    //   48: aload_3
    //   49: invokevirtual 62	org/xbill/DNS/TextParseException:getMessage	()Ljava/lang/String;
    //   52: invokevirtual 123	org/xbill/DNS/Tokenizer:exception	(Ljava/lang/String;)Lorg/xbill/DNS/TextParseException;
    //   55: athrow
    //   56: astore 4
    //   58: new 125	org/xbill/DNS/WireParseException
    //   61: dup
    //   62: aload 4
    //   64: invokevirtual 126	java/lang/IllegalArgumentException:getMessage	()Ljava/lang/String;
    //   67: invokespecial 127	org/xbill/DNS/WireParseException:<init>	(Ljava/lang/String;)V
    //   70: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	33	46	org/xbill/DNS/TextParseException
    //   33	45	56	java/lang/IllegalArgumentException
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.longitude = paramDNSInput.readCountedString();
    this.latitude = paramDNSInput.readCountedString();
    this.altitude = paramDNSInput.readCountedString();
    try
    {
      validate(getLongitude(), getLatitude());
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new WireParseException(localIllegalArgumentException.getMessage());
    }
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(byteArrayToString(this.longitude, true));
    localStringBuffer.append(" ");
    localStringBuffer.append(byteArrayToString(this.latitude, true));
    localStringBuffer.append(" ");
    localStringBuffer.append(byteArrayToString(this.altitude, true));
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeCountedString(this.longitude);
    paramDNSOutput.writeCountedString(this.latitude);
    paramDNSOutput.writeCountedString(this.altitude);
  }
}