package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class SSHFPRecord extends Record
{
  private static final long serialVersionUID = -8104701402654687025L;
  private int alg;
  private int digestType;
  private byte[] fingerprint;

  SSHFPRecord()
  {
  }

  public SSHFPRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    super(paramName, 44, paramInt1, paramLong);
    this.alg = checkU8("alg", paramInt2);
    this.digestType = checkU8("digestType", paramInt3);
    this.fingerprint = paramArrayOfByte;
  }

  public int getAlgorithm()
  {
    return this.alg;
  }

  public int getDigestType()
  {
    return this.digestType;
  }

  public byte[] getFingerPrint()
  {
    return this.fingerprint;
  }

  Record getObject()
  {
    return new SSHFPRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.alg = paramTokenizer.getUInt8();
    this.digestType = paramTokenizer.getUInt8();
    this.fingerprint = paramTokenizer.getHex(true);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.alg = paramDNSInput.readU8();
    this.digestType = paramDNSInput.readU8();
    this.fingerprint = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.alg);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.digestType);
    localStringBuffer.append(" ");
    localStringBuffer.append(base16.toString(this.fingerprint));
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU8(this.alg);
    paramDNSOutput.writeU8(this.digestType);
    paramDNSOutput.writeByteArray(this.fingerprint);
  }

  public static class Algorithm
  {
    public static final int DSS = 2;
    public static final int RSA = 1;
  }

  public static class Digest
  {
    public static final int SHA1 = 1;
  }
}