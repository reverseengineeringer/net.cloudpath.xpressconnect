package org.bouncycastle2.crypto;

import org.bouncycastle2.util.Strings;

public abstract class PBEParametersGenerator
{
  protected int iterationCount;
  protected byte[] password;
  protected byte[] salt;

  public static byte[] PKCS12PasswordToBytes(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar.length > 0)
    {
      byte[] arrayOfByte = new byte[2 * (1 + paramArrayOfChar.length)];
      for (int i = 0; ; i++)
      {
        if (i == paramArrayOfChar.length)
          return arrayOfByte;
        arrayOfByte[(i * 2)] = ((byte)(paramArrayOfChar[i] >>> '\b'));
        arrayOfByte[(1 + i * 2)] = ((byte)paramArrayOfChar[i]);
      }
    }
    return new byte[0];
  }

  public static byte[] PKCS5PasswordToBytes(char[] paramArrayOfChar)
  {
    byte[] arrayOfByte = new byte[paramArrayOfChar.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)paramArrayOfChar[i]);
    }
  }

  public static byte[] PKCS5PasswordToUTF8Bytes(char[] paramArrayOfChar)
  {
    return Strings.toUTF8ByteArray(paramArrayOfChar);
  }

  public abstract CipherParameters generateDerivedMacParameters(int paramInt);

  public abstract CipherParameters generateDerivedParameters(int paramInt);

  public abstract CipherParameters generateDerivedParameters(int paramInt1, int paramInt2);

  public int getIterationCount()
  {
    return this.iterationCount;
  }

  public byte[] getPassword()
  {
    return this.password;
  }

  public byte[] getSalt()
  {
    return this.salt;
  }

  public void init(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    this.password = paramArrayOfByte1;
    this.salt = paramArrayOfByte2;
    this.iterationCount = paramInt;
  }
}