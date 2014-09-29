package org.xbill.DNS;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.xbill.DNS.utils.base16;

public class NSEC3PARAMRecord extends Record
{
  private static final long serialVersionUID = -8689038598776316533L;
  private int flags;
  private int hashAlg;
  private int iterations;
  private byte[] salt;

  NSEC3PARAMRecord()
  {
  }

  public NSEC3PARAMRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
  {
    super(paramName, 51, paramInt1, paramLong);
    this.hashAlg = checkU8("hashAlg", paramInt2);
    this.flags = checkU8("flags", paramInt3);
    this.iterations = checkU16("iterations", paramInt4);
    if (paramArrayOfByte != null)
    {
      if (paramArrayOfByte.length > 255)
        throw new IllegalArgumentException("Invalid salt length");
      if (paramArrayOfByte.length > 0)
      {
        this.salt = new byte[paramArrayOfByte.length];
        System.arraycopy(paramArrayOfByte, 0, this.salt, 0, paramArrayOfByte.length);
      }
    }
  }

  public int getFlags()
  {
    return this.flags;
  }

  public int getHashAlgorithm()
  {
    return this.hashAlg;
  }

  public int getIterations()
  {
    return this.iterations;
  }

  Record getObject()
  {
    return new NSEC3PARAMRecord();
  }

  public byte[] getSalt()
  {
    return this.salt;
  }

  public byte[] hashName(Name paramName)
    throws NoSuchAlgorithmException
  {
    return NSEC3Record.hashName(paramName, this.hashAlg, this.iterations, this.salt);
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.hashAlg = paramTokenizer.getUInt8();
    this.flags = paramTokenizer.getUInt8();
    this.iterations = paramTokenizer.getUInt16();
    if (paramTokenizer.getString().equals("-"))
      this.salt = null;
    do
    {
      return;
      paramTokenizer.unget();
      this.salt = paramTokenizer.getHexString();
    }
    while (this.salt.length <= 255);
    throw paramTokenizer.exception("salt value too long");
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.hashAlg = paramDNSInput.readU8();
    this.flags = paramDNSInput.readU8();
    this.iterations = paramDNSInput.readU16();
    int i = paramDNSInput.readU8();
    if (i > 0)
    {
      this.salt = paramDNSInput.readByteArray(i);
      return;
    }
    this.salt = null;
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.hashAlg);
    localStringBuffer.append(' ');
    localStringBuffer.append(this.flags);
    localStringBuffer.append(' ');
    localStringBuffer.append(this.iterations);
    localStringBuffer.append(' ');
    if (this.salt == null)
      localStringBuffer.append('-');
    while (true)
    {
      return localStringBuffer.toString();
      localStringBuffer.append(base16.toString(this.salt));
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU8(this.hashAlg);
    paramDNSOutput.writeU8(this.flags);
    paramDNSOutput.writeU16(this.iterations);
    if (this.salt != null)
    {
      paramDNSOutput.writeU8(this.salt.length);
      paramDNSOutput.writeByteArray(this.salt);
      return;
    }
    paramDNSOutput.writeU8(0);
  }
}