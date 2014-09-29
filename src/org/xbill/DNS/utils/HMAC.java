package org.xbill.DNS.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HMAC
{
  private static final byte IPAD = 54;
  private static final byte OPAD = 92;
  private int blockLength;
  private MessageDigest digest;
  private byte[] ipad;
  private byte[] opad;

  public HMAC(String paramString, int paramInt, byte[] paramArrayOfByte)
  {
    try
    {
      this.digest = MessageDigest.getInstance(paramString);
      this.blockLength = paramInt;
      init(paramArrayOfByte);
      return;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
    }
    throw new IllegalArgumentException("unknown digest algorithm " + paramString);
  }

  public HMAC(String paramString, byte[] paramArrayOfByte)
  {
    this(paramString, 64, paramArrayOfByte);
  }

  public HMAC(MessageDigest paramMessageDigest, int paramInt, byte[] paramArrayOfByte)
  {
    paramMessageDigest.reset();
    this.digest = paramMessageDigest;
    this.blockLength = paramInt;
    init(paramArrayOfByte);
  }

  public HMAC(MessageDigest paramMessageDigest, byte[] paramArrayOfByte)
  {
    this(paramMessageDigest, 64, paramArrayOfByte);
  }

  private void init(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length > this.blockLength)
    {
      paramArrayOfByte = this.digest.digest(paramArrayOfByte);
      this.digest.reset();
    }
    this.ipad = new byte[this.blockLength];
    this.opad = new byte[this.blockLength];
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      this.ipad[i] = ((byte)(0x36 ^ paramArrayOfByte[i]));
      this.opad[i] = ((byte)(0x5C ^ paramArrayOfByte[i]));
    }
    while (i < this.blockLength)
    {
      this.ipad[i] = 54;
      this.opad[i] = 92;
      i++;
    }
    this.digest.update(this.ipad);
  }

  public void clear()
  {
    this.digest.reset();
    this.digest.update(this.ipad);
  }

  public int digestLength()
  {
    return this.digest.getDigestLength();
  }

  public byte[] sign()
  {
    byte[] arrayOfByte = this.digest.digest();
    this.digest.reset();
    this.digest.update(this.opad);
    return this.digest.digest(arrayOfByte);
  }

  public void update(byte[] paramArrayOfByte)
  {
    this.digest.update(paramArrayOfByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public boolean verify(byte[] paramArrayOfByte)
  {
    return verify(paramArrayOfByte, false);
  }

  public boolean verify(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    Object localObject = sign();
    if ((paramBoolean) && (paramArrayOfByte.length < localObject.length))
    {
      byte[] arrayOfByte = new byte[paramArrayOfByte.length];
      System.arraycopy(localObject, 0, arrayOfByte, 0, arrayOfByte.length);
      localObject = arrayOfByte;
    }
    return Arrays.equals(paramArrayOfByte, (byte[])localObject);
  }
}