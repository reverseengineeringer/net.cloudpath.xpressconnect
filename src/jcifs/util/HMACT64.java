package jcifs.util;

import java.security.MessageDigest;

public class HMACT64 extends MessageDigest
  implements Cloneable
{
  private static final int BLOCK_LENGTH = 64;
  private static final byte IPAD = 54;
  private static final byte OPAD = 92;
  private byte[] ipad = new byte[64];
  private MessageDigest md5;
  private byte[] opad = new byte[64];

  private HMACT64(HMACT64 paramHMACT64)
    throws CloneNotSupportedException
  {
    super("HMACT64");
    this.ipad = paramHMACT64.ipad;
    this.opad = paramHMACT64.opad;
    this.md5 = ((MessageDigest)paramHMACT64.md5.clone());
  }

  public HMACT64(byte[] paramArrayOfByte)
  {
    super("HMACT64");
    int i = Math.min(paramArrayOfByte.length, 64);
    for (int j = 0; j < i; j++)
    {
      this.ipad[j] = ((byte)(0x36 ^ paramArrayOfByte[j]));
      this.opad[j] = ((byte)(0x5C ^ paramArrayOfByte[j]));
    }
    for (int k = i; k < 64; k++)
    {
      this.ipad[k] = 54;
      this.opad[k] = 92;
    }
    try
    {
      this.md5 = MessageDigest.getInstance("MD5");
      engineReset();
      return;
    }
    catch (Exception localException)
    {
      throw new IllegalStateException(localException.getMessage());
    }
  }

  public Object clone()
  {
    try
    {
      HMACT64 localHMACT64 = new HMACT64(this);
      return localHMACT64;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new IllegalStateException(localCloneNotSupportedException.getMessage());
    }
  }

  protected int engineDigest(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = this.md5.digest();
    this.md5.update(this.opad);
    this.md5.update(arrayOfByte);
    try
    {
      int i = this.md5.digest(paramArrayOfByte, paramInt1, paramInt2);
      return i;
    }
    catch (Exception localException)
    {
    }
    throw new IllegalStateException();
  }

  protected byte[] engineDigest()
  {
    byte[] arrayOfByte = this.md5.digest();
    this.md5.update(this.opad);
    return this.md5.digest(arrayOfByte);
  }

  protected int engineGetDigestLength()
  {
    return this.md5.getDigestLength();
  }

  protected void engineReset()
  {
    this.md5.reset();
    this.md5.update(this.ipad);
  }

  protected void engineUpdate(byte paramByte)
  {
    this.md5.update(paramByte);
  }

  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.md5.update(paramArrayOfByte, paramInt1, paramInt2);
  }
}