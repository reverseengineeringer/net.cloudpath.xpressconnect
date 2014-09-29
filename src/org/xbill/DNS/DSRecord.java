package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class DSRecord extends Record
{
  public static final int SHA1_DIGEST_ID = 1;
  public static final int SHA256_DIGEST_ID = 2;
  private static final long serialVersionUID = -9001819329700081493L;
  private int alg;
  private byte[] digest;
  private int digestid;
  private int footprint;

  DSRecord()
  {
  }

  public DSRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
  {
    super(paramName, 43, paramInt1, paramLong);
    this.footprint = checkU16("footprint", paramInt2);
    this.alg = checkU8("alg", paramInt3);
    this.digestid = checkU8("digestid", paramInt4);
    this.digest = paramArrayOfByte;
  }

  public DSRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, DNSKEYRecord paramDNSKEYRecord)
  {
    this(paramName, paramInt1, paramLong, paramInt2, paramDNSKEYRecord.getAlgorithm(), paramInt3, DNSSEC.generateDS(paramDNSKEYRecord, paramInt3));
  }

  public int getAlgorithm()
  {
    return this.alg;
  }

  public byte[] getDigest()
  {
    return this.digest;
  }

  public int getDigestID()
  {
    return this.digestid;
  }

  public int getFootprint()
  {
    return this.footprint;
  }

  Record getObject()
  {
    return new DSRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.footprint = paramTokenizer.getUInt16();
    this.alg = paramTokenizer.getUInt8();
    this.digestid = paramTokenizer.getUInt8();
    this.digest = paramTokenizer.getHex();
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.footprint = paramDNSInput.readU16();
    this.alg = paramDNSInput.readU8();
    this.digestid = paramDNSInput.readU8();
    this.digest = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.footprint);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.alg);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.digestid);
    if (this.digest != null)
    {
      localStringBuffer.append(" ");
      localStringBuffer.append(base16.toString(this.digest));
    }
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.footprint);
    paramDNSOutput.writeU8(this.alg);
    paramDNSOutput.writeU8(this.digestid);
    if (this.digest != null)
      paramDNSOutput.writeByteArray(this.digest);
  }

  public static class Digest
  {
    public static final int SHA1 = 1;
    public static final int SHA256 = 2;
  }
}