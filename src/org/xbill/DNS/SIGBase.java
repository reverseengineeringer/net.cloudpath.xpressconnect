package org.xbill.DNS;

import java.io.IOException;
import java.util.Date;
import org.xbill.DNS.utils.base64;

abstract class SIGBase extends Record
{
  private static final long serialVersionUID = -3738444391533812369L;
  protected int alg;
  protected int covered;
  protected Date expire;
  protected int footprint;
  protected int labels;
  protected long origttl;
  protected byte[] signature;
  protected Name signer;
  protected Date timeSigned;

  protected SIGBase()
  {
  }

  public SIGBase(Name paramName1, int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, long paramLong2, Date paramDate1, Date paramDate2, int paramInt5, Name paramName2, byte[] paramArrayOfByte)
  {
    super(paramName1, paramInt1, paramInt2, paramLong1);
    Type.check(paramInt3);
    TTL.check(paramLong2);
    this.covered = paramInt3;
    this.alg = checkU8("alg", paramInt4);
    this.labels = (-1 + paramName1.labels());
    if (paramName1.isWild())
      this.labels = (-1 + this.labels);
    this.origttl = paramLong2;
    this.expire = paramDate1;
    this.timeSigned = paramDate2;
    this.footprint = checkU16("footprint", paramInt5);
    this.signer = checkName("signer", paramName2);
    this.signature = paramArrayOfByte;
  }

  public int getAlgorithm()
  {
    return this.alg;
  }

  public Date getExpire()
  {
    return this.expire;
  }

  public int getFootprint()
  {
    return this.footprint;
  }

  public int getLabels()
  {
    return this.labels;
  }

  public long getOrigTTL()
  {
    return this.origttl;
  }

  public byte[] getSignature()
  {
    return this.signature;
  }

  public Name getSigner()
  {
    return this.signer;
  }

  public Date getTimeSigned()
  {
    return this.timeSigned;
  }

  public int getTypeCovered()
  {
    return this.covered;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    String str1 = paramTokenizer.getString();
    this.covered = Type.value(str1);
    if (this.covered < 0)
      throw paramTokenizer.exception("Invalid type: " + str1);
    String str2 = paramTokenizer.getString();
    this.alg = DNSSEC.Algorithm.value(str2);
    if (this.alg < 0)
      throw paramTokenizer.exception("Invalid algorithm: " + str2);
    this.labels = paramTokenizer.getUInt8();
    this.origttl = paramTokenizer.getTTL();
    this.expire = FormattedTime.parse(paramTokenizer.getString());
    this.timeSigned = FormattedTime.parse(paramTokenizer.getString());
    this.footprint = paramTokenizer.getUInt16();
    this.signer = paramTokenizer.getName(paramName);
    this.signature = paramTokenizer.getBase64();
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.covered = paramDNSInput.readU16();
    this.alg = paramDNSInput.readU8();
    this.labels = paramDNSInput.readU8();
    this.origttl = paramDNSInput.readU32();
    this.expire = new Date(1000L * paramDNSInput.readU32());
    this.timeSigned = new Date(1000L * paramDNSInput.readU32());
    this.footprint = paramDNSInput.readU16();
    this.signer = new Name(paramDNSInput);
    this.signature = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(Type.string(this.covered));
    localStringBuffer.append(" ");
    localStringBuffer.append(this.alg);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.labels);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.origttl);
    localStringBuffer.append(" ");
    if (Options.check("multiline"))
      localStringBuffer.append("(\n\t");
    localStringBuffer.append(FormattedTime.format(this.expire));
    localStringBuffer.append(" ");
    localStringBuffer.append(FormattedTime.format(this.timeSigned));
    localStringBuffer.append(" ");
    localStringBuffer.append(this.footprint);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.signer);
    if (Options.check("multiline"))
    {
      localStringBuffer.append("\n");
      localStringBuffer.append(base64.formatString(this.signature, 64, "\t", true));
    }
    while (true)
    {
      return localStringBuffer.toString();
      localStringBuffer.append(" ");
      localStringBuffer.append(base64.toString(this.signature));
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.covered);
    paramDNSOutput.writeU8(this.alg);
    paramDNSOutput.writeU8(this.labels);
    paramDNSOutput.writeU32(this.origttl);
    paramDNSOutput.writeU32(this.expire.getTime() / 1000L);
    paramDNSOutput.writeU32(this.timeSigned.getTime() / 1000L);
    paramDNSOutput.writeU16(this.footprint);
    this.signer.toWire(paramDNSOutput, null, paramBoolean);
    paramDNSOutput.writeByteArray(this.signature);
  }

  void setSignature(byte[] paramArrayOfByte)
  {
    this.signature = paramArrayOfByte;
  }
}