package org.bouncycastle2.crypto.tls;

public class TlsNullCipher
  implements TlsCipher
{
  protected byte[] copyData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }

  public byte[] decodeCiphertext(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return copyData(paramArrayOfByte, paramInt1, paramInt2);
  }

  public byte[] encodePlaintext(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return copyData(paramArrayOfByte, paramInt1, paramInt2);
  }
}