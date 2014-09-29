package org.bouncycastle2.crypto.encodings;

import java.math.BigInteger;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.RSAKeyParameters;

public class ISO9796d1Encoding
  implements AsymmetricBlockCipher
{
  private static final BigInteger SIX;
  private static final BigInteger SIXTEEN = BigInteger.valueOf(16L);
  private static byte[] inverse = arrayOfByte2;
  private static byte[] shadows;
  private int bitSize;
  private AsymmetricBlockCipher engine;
  private boolean forEncryption;
  private BigInteger modulus;
  private int padBits = 0;

  static
  {
    SIX = BigInteger.valueOf(6L);
    byte[] arrayOfByte1 = new byte[16];
    arrayOfByte1[0] = 14;
    arrayOfByte1[1] = 3;
    arrayOfByte1[2] = 5;
    arrayOfByte1[3] = 8;
    arrayOfByte1[4] = 9;
    arrayOfByte1[5] = 4;
    arrayOfByte1[6] = 2;
    arrayOfByte1[7] = 15;
    arrayOfByte1[9] = 13;
    arrayOfByte1[10] = 11;
    arrayOfByte1[11] = 6;
    arrayOfByte1[12] = 7;
    arrayOfByte1[13] = 10;
    arrayOfByte1[14] = 12;
    arrayOfByte1[15] = 1;
    shadows = arrayOfByte1;
    byte[] arrayOfByte2 = new byte[16];
    arrayOfByte2[0] = 8;
    arrayOfByte2[1] = 15;
    arrayOfByte2[2] = 6;
    arrayOfByte2[3] = 1;
    arrayOfByte2[4] = 5;
    arrayOfByte2[5] = 2;
    arrayOfByte2[6] = 11;
    arrayOfByte2[7] = 12;
    arrayOfByte2[8] = 3;
    arrayOfByte2[9] = 4;
    arrayOfByte2[10] = 13;
    arrayOfByte2[11] = 10;
    arrayOfByte2[12] = 14;
    arrayOfByte2[13] = 9;
    arrayOfByte2[15] = 7;
  }

  public ISO9796d1Encoding(AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.engine = paramAsymmetricBlockCipher;
  }

  private static byte[] convertOutputDecryptOnly(BigInteger paramBigInteger)
  {
    byte[] arrayOfByte1 = paramBigInteger.toByteArray();
    if (arrayOfByte1[0] == 0)
    {
      byte[] arrayOfByte2 = new byte[-1 + arrayOfByte1.length];
      System.arraycopy(arrayOfByte1, 1, arrayOfByte2, 0, arrayOfByte2.length);
      return arrayOfByte2;
    }
    return arrayOfByte1;
  }

  private byte[] decodeBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    byte[] arrayOfByte1 = this.engine.processBlock(paramArrayOfByte, paramInt1, paramInt2);
    int i = 1;
    int j = (13 + this.bitSize) / 16;
    BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte1);
    if (localBigInteger1.mod(SIXTEEN).equals(SIX));
    byte[] arrayOfByte2;
    for (BigInteger localBigInteger2 = localBigInteger1; ; localBigInteger2 = this.modulus.subtract(localBigInteger1))
    {
      arrayOfByte2 = convertOutputDecryptOnly(localBigInteger2);
      if ((0xF & arrayOfByte2[(-1 + arrayOfByte2.length)]) == 6)
        break label143;
      throw new InvalidCipherTextException("invalid forcing byte in block");
      if (!this.modulus.subtract(localBigInteger1).mod(SIXTEEN).equals(SIX))
        break;
    }
    throw new InvalidCipherTextException("resulting integer iS or (modulus - iS) is not congruent to 6 mod 16");
    label143: arrayOfByte2[(-1 + arrayOfByte2.length)] = ((byte)((0xFF & arrayOfByte2[(-1 + arrayOfByte2.length)]) >>> 4 | inverse[((0xFF & arrayOfByte2[(-2 + arrayOfByte2.length)]) >> 4)] << 4));
    arrayOfByte2[0] = ((byte)(shadows[((0xFF & arrayOfByte2[1]) >>> 4)] << 4 | shadows[(0xF & arrayOfByte2[1])]));
    int k = 0;
    int m = 0;
    int n = -1 + arrayOfByte2.length;
    byte[] arrayOfByte3;
    if (n < arrayOfByte2.length - j * 2)
    {
      arrayOfByte2[m] = 0;
      arrayOfByte3 = new byte[(arrayOfByte2.length - m) / 2];
    }
    for (int i2 = 0; ; i2++)
    {
      if (i2 >= arrayOfByte3.length)
      {
        this.padBits = (i - 1);
        return arrayOfByte3;
        int i1 = shadows[((0xFF & arrayOfByte2[n]) >>> 4)] << 4 | shadows[(0xF & arrayOfByte2[n])];
        if ((0xFF & (i1 ^ arrayOfByte2[(n - 1)])) != 0)
        {
          if (k == 0)
          {
            k = 1;
            i = 0xFF & (i1 ^ arrayOfByte2[(n - 1)]);
            m = n - 1;
          }
        }
        else
        {
          n -= 2;
          break;
        }
        throw new InvalidCipherTextException("invalid tsums in block");
      }
      arrayOfByte3[i2] = arrayOfByte2[(1 + (m + i2 * 2))];
    }
  }

  private byte[] encodeBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    byte[] arrayOfByte = new byte[(7 + this.bitSize) / 8];
    int i = 1 + this.padBits;
    int j = (13 + this.bitSize) / 16;
    int k = 0;
    int m;
    label54: int i3;
    if (k >= j)
    {
      m = arrayOfByte.length - j * 2;
      if (m != arrayOfByte.length)
        break label246;
      int i1 = arrayOfByte.length - paramInt2 * 2;
      arrayOfByte[i1] = ((byte)(i ^ arrayOfByte[i1]));
      arrayOfByte[(-1 + arrayOfByte.length)] = ((byte)(0x6 | arrayOfByte[(-1 + arrayOfByte.length)] << 4));
      int i2 = 8 - (-1 + this.bitSize) % 8;
      i3 = 0;
      if (i2 == 8)
        break label307;
      arrayOfByte[0] = ((byte)(arrayOfByte[0] & 255 >>> i2));
      arrayOfByte[0] = ((byte)(arrayOfByte[0] | 128 >>> i2));
    }
    while (true)
    {
      return this.engine.processBlock(arrayOfByte, i3, arrayOfByte.length - i3);
      if (k > j - paramInt2)
        System.arraycopy(paramArrayOfByte, paramInt1 + paramInt2 - (j - k), arrayOfByte, arrayOfByte.length - j, j - k);
      while (true)
      {
        k += paramInt2;
        break;
        System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, arrayOfByte.length - (k + paramInt2), paramInt2);
      }
      label246: int n = arrayOfByte[(arrayOfByte.length - j + m / 2)];
      arrayOfByte[m] = ((byte)(shadows[((n & 0xFF) >>> 4)] << 4 | shadows[(n & 0xF)]));
      arrayOfByte[(m + 1)] = n;
      m += 2;
      break label54;
      label307: arrayOfByte[0] = 0;
      arrayOfByte[1] = ((byte)(0x80 | arrayOfByte[1]));
      i3 = 1;
    }
  }

  public int getInputBlockSize()
  {
    int i = this.engine.getInputBlockSize();
    if (this.forEncryption)
      i = (i + 1) / 2;
    return i;
  }

  public int getOutputBlockSize()
  {
    int i = this.engine.getOutputBlockSize();
    if (this.forEncryption)
      return i;
    return (i + 1) / 2;
  }

  public int getPadBits()
  {
    return this.padBits;
  }

  public AsymmetricBlockCipher getUnderlyingCipher()
  {
    return this.engine;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof ParametersWithRandom));
    for (RSAKeyParameters localRSAKeyParameters = (RSAKeyParameters)((ParametersWithRandom)paramCipherParameters).getParameters(); ; localRSAKeyParameters = (RSAKeyParameters)paramCipherParameters)
    {
      this.engine.init(paramBoolean, paramCipherParameters);
      this.modulus = localRSAKeyParameters.getModulus();
      this.bitSize = this.modulus.bitLength();
      this.forEncryption = paramBoolean;
      return;
    }
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.forEncryption)
      return encodeBlock(paramArrayOfByte, paramInt1, paramInt2);
    return decodeBlock(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void setPadBits(int paramInt)
  {
    if (paramInt > 7)
      throw new IllegalArgumentException("padBits > 7");
    this.padBits = paramInt;
  }
}