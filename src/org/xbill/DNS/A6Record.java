package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class A6Record extends Record
{
  private static final long serialVersionUID = -8815026887337346789L;
  private Name prefix;
  private int prefixBits;
  private InetAddress suffix;

  A6Record()
  {
  }

  public A6Record(Name paramName1, int paramInt1, long paramLong, int paramInt2, InetAddress paramInetAddress, Name paramName2)
  {
    super(paramName1, 38, paramInt1, paramLong);
    this.prefixBits = checkU8("prefixBits", paramInt2);
    if ((paramInetAddress != null) && (Address.familyOf(paramInetAddress) != 2))
      throw new IllegalArgumentException("invalid IPv6 address");
    this.suffix = paramInetAddress;
    if (paramName2 != null)
      this.prefix = checkName("prefix", paramName2);
  }

  Record getObject()
  {
    return new A6Record();
  }

  public Name getPrefix()
  {
    return this.prefix;
  }

  public int getPrefixBits()
  {
    return this.prefixBits;
  }

  public InetAddress getSuffix()
  {
    return this.suffix;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.prefixBits = paramTokenizer.getUInt8();
    if (this.prefixBits > 128)
      throw paramTokenizer.exception("prefix bits must be [0..128]");
    String str;
    if (this.prefixBits < 128)
      str = paramTokenizer.getString();
    try
    {
      this.suffix = Address.getByAddress(str, 2);
      if (this.prefixBits > 0)
        this.prefix = paramTokenizer.getName(paramName);
      return;
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
    throw paramTokenizer.exception("invalid IPv6 address: " + str);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.prefixBits = paramDNSInput.readU8();
    int i = (7 + (128 - this.prefixBits)) / 8;
    if (this.prefixBits < 128)
    {
      byte[] arrayOfByte = new byte[16];
      paramDNSInput.readByteArray(arrayOfByte, 16 - i, i);
      this.suffix = InetAddress.getByAddress(arrayOfByte);
    }
    if (this.prefixBits > 0)
      this.prefix = new Name(paramDNSInput);
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.prefixBits);
    if (this.suffix != null)
    {
      localStringBuffer.append(" ");
      localStringBuffer.append(this.suffix.getHostAddress());
    }
    if (this.prefix != null)
    {
      localStringBuffer.append(" ");
      localStringBuffer.append(this.prefix);
    }
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU8(this.prefixBits);
    if (this.suffix != null)
    {
      int i = (7 + (128 - this.prefixBits)) / 8;
      paramDNSOutput.writeByteArray(this.suffix.getAddress(), 16 - i, i);
    }
    if (this.prefix != null)
      this.prefix.toWire(paramDNSOutput, null, paramBoolean);
  }
}