package org.xbill.DNS;

import java.io.IOException;
import java.security.PublicKey;
import org.xbill.DNS.utils.base64;

abstract class KEYBase extends Record
{
  private static final long serialVersionUID = 3469321722693285454L;
  protected int alg;
  protected int flags;
  protected int footprint = -1;
  protected byte[] key;
  protected int proto;
  protected PublicKey publicKey = null;

  protected KEYBase()
  {
  }

  public KEYBase(Name paramName, int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
  {
    super(paramName, paramInt1, paramInt2, paramLong);
    this.flags = checkU16("flags", paramInt3);
    this.proto = checkU8("proto", paramInt4);
    this.alg = checkU8("alg", paramInt5);
    this.key = paramArrayOfByte;
  }

  public int getAlgorithm()
  {
    return this.alg;
  }

  public int getFlags()
  {
    return this.flags;
  }

  public int getFootprint()
  {
    if (this.footprint >= 0)
      return this.footprint;
    int i = 0;
    DNSOutput localDNSOutput = new DNSOutput();
    rrToWire(localDNSOutput, null, false);
    byte[] arrayOfByte = localDNSOutput.toByteArray();
    int n;
    if (this.alg == 1)
      n = 0xFF & arrayOfByte[(-3 + arrayOfByte.length)];
    for (int k = (0xFF & arrayOfByte[(-2 + arrayOfByte.length)]) + (n << 8); ; k = i + (0xFFFF & i >> 16))
    {
      this.footprint = (k & 0xFFFF);
      return this.footprint;
      for (int j = 0; j < -1 + arrayOfByte.length; j += 2)
      {
        int m = 0xFF & arrayOfByte[j];
        i += (0xFF & arrayOfByte[(j + 1)]) + (m << 8);
      }
      if (j < arrayOfByte.length)
        i += ((0xFF & arrayOfByte[j]) << 8);
    }
  }

  public byte[] getKey()
  {
    return this.key;
  }

  public int getProtocol()
  {
    return this.proto;
  }

  public PublicKey getPublicKey()
    throws DNSSEC.DNSSECException
  {
    if (this.publicKey != null)
      return this.publicKey;
    this.publicKey = DNSSEC.toPublicKey(this);
    return this.publicKey;
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.flags = paramDNSInput.readU16();
    this.proto = paramDNSInput.readU8();
    this.alg = paramDNSInput.readU8();
    if (paramDNSInput.remaining() > 0)
      this.key = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.flags);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.proto);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.alg);
    if (this.key != null)
    {
      if (!Options.check("multiline"))
        break label109;
      localStringBuffer.append(" (\n");
      localStringBuffer.append(base64.formatString(this.key, 64, "\t", true));
      localStringBuffer.append(" ; key_tag = ");
      localStringBuffer.append(getFootprint());
    }
    while (true)
    {
      return localStringBuffer.toString();
      label109: localStringBuffer.append(" ");
      localStringBuffer.append(base64.toString(this.key));
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.flags);
    paramDNSOutput.writeU8(this.proto);
    paramDNSOutput.writeU8(this.alg);
    if (this.key != null)
      paramDNSOutput.writeByteArray(this.key);
  }
}