package org.xbill.DNS;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;

public class NSEC3Record extends Record
{
  public static final int SHA1_DIGEST_ID = 1;
  private static final base32 b32 = new base32("0123456789ABCDEFGHIJKLMNOPQRSTUV=", false, false);
  private static final long serialVersionUID = -7123504635968932855L;
  private int flags;
  private int hashAlg;
  private int iterations;
  private byte[] next;
  private byte[] salt;
  private TypeBitmap types;

  NSEC3Record()
  {
  }

  public NSEC3Record(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt)
  {
    super(paramName, 50, paramInt1, paramLong);
    this.hashAlg = checkU8("hashAlg", paramInt2);
    this.flags = checkU8("flags", paramInt3);
    this.iterations = checkU16("iterations", paramInt4);
    if (paramArrayOfByte1 != null)
    {
      if (paramArrayOfByte1.length > 255)
        throw new IllegalArgumentException("Invalid salt");
      if (paramArrayOfByte1.length > 0)
      {
        this.salt = new byte[paramArrayOfByte1.length];
        System.arraycopy(paramArrayOfByte1, 0, this.salt, 0, paramArrayOfByte1.length);
      }
    }
    if (paramArrayOfByte2.length > 255)
      throw new IllegalArgumentException("Invalid next hash");
    this.next = new byte[paramArrayOfByte2.length];
    System.arraycopy(paramArrayOfByte2, 0, this.next, 0, paramArrayOfByte2.length);
    this.types = new TypeBitmap(paramArrayOfInt);
  }

  static byte[] hashName(Name paramName, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    throws NoSuchAlgorithmException
  {
    switch (paramInt1)
    {
    default:
      throw new NoSuchAlgorithmException("Unknown NSEC3 algorithmidentifier: " + paramInt1);
    case 1:
    }
    MessageDigest localMessageDigest = MessageDigest.getInstance("sha-1");
    byte[] arrayOfByte = null;
    int i = 0;
    if (i <= paramInt2)
    {
      localMessageDigest.reset();
      if (i == 0)
        localMessageDigest.update(paramName.toWireCanonical());
      while (true)
      {
        if (paramArrayOfByte != null)
          localMessageDigest.update(paramArrayOfByte);
        arrayOfByte = localMessageDigest.digest();
        i++;
        break;
        localMessageDigest.update(arrayOfByte);
      }
    }
    return arrayOfByte;
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

  public byte[] getNext()
  {
    return this.next;
  }

  Record getObject()
  {
    return new NSEC3Record();
  }

  public byte[] getSalt()
  {
    return this.salt;
  }

  public int[] getTypes()
  {
    return this.types.toArray();
  }

  public boolean hasType(int paramInt)
  {
    return this.types.contains(paramInt);
  }

  public byte[] hashName(Name paramName)
    throws NoSuchAlgorithmException
  {
    return hashName(paramName, this.hashAlg, this.iterations, this.salt);
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
      this.next = paramTokenizer.getBase32String(b32);
      this.types = new TypeBitmap(paramTokenizer);
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
    if (i > 0);
    for (this.salt = paramDNSInput.readByteArray(i); ; this.salt = null)
    {
      this.next = paramDNSInput.readByteArray(paramDNSInput.readU8());
      this.types = new TypeBitmap(paramDNSInput);
      return;
    }
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
      localStringBuffer.append(' ');
      localStringBuffer.append(b32.toString(this.next));
      if (!this.types.empty())
      {
        localStringBuffer.append(' ');
        localStringBuffer.append(this.types.toString());
      }
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
    }
    while (true)
    {
      paramDNSOutput.writeU8(this.next.length);
      paramDNSOutput.writeByteArray(this.next);
      this.types.toWire(paramDNSOutput);
      return;
      paramDNSOutput.writeU8(0);
    }
  }

  public static class Digest
  {
    public static final int SHA1 = 1;
  }

  public static class Flags
  {
    public static final int OPT_OUT = 1;
  }
}