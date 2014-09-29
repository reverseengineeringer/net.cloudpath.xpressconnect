package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.util.Arrays;

public class TlsBlockCipher
  implements TlsCipher
{
  protected TlsClientContext context;
  protected BlockCipher decryptCipher;
  protected BlockCipher encryptCipher;
  protected TlsMac readMac;
  protected TlsMac writeMac;

  public TlsBlockCipher(TlsClientContext paramTlsClientContext, BlockCipher paramBlockCipher1, BlockCipher paramBlockCipher2, Digest paramDigest1, Digest paramDigest2, int paramInt)
  {
    this.context = paramTlsClientContext;
    this.encryptCipher = paramBlockCipher1;
    this.decryptCipher = paramBlockCipher2;
    int i = paramInt * 2 + paramDigest1.getDigestSize() + paramDigest2.getDigestSize() + paramBlockCipher1.getBlockSize() + paramBlockCipher2.getBlockSize();
    SecurityParameters localSecurityParameters = paramTlsClientContext.getSecurityParameters();
    byte[] arrayOfByte = TlsUtils.PRF(localSecurityParameters.masterSecret, "key expansion", TlsUtils.concat(localSecurityParameters.serverRandom, localSecurityParameters.clientRandom), i);
    this.writeMac = new TlsMac(paramDigest1, arrayOfByte, 0, paramDigest1.getDigestSize());
    int j = 0 + paramDigest1.getDigestSize();
    this.readMac = new TlsMac(paramDigest2, arrayOfByte, j, paramDigest2.getDigestSize());
    int k = j + paramDigest2.getDigestSize();
    initCipher(true, paramBlockCipher1, arrayOfByte, paramInt, k, k + paramInt * 2);
    int m = k + paramInt;
    initCipher(false, paramBlockCipher2, arrayOfByte, paramInt, m, m + paramInt + paramBlockCipher1.getBlockSize());
  }

  protected int chooseExtraPadBlocks(SecureRandom paramSecureRandom, int paramInt)
  {
    return Math.min(lowestBitSet(paramSecureRandom.nextInt()), paramInt);
  }

  public byte[] decodeCiphertext(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 1 + this.readMac.getSize();
    int j = this.decryptCipher.getBlockSize();
    if (paramInt2 < i)
      throw new TlsFatalAlert((short)50);
    if (paramInt2 % j != 0)
      throw new TlsFatalAlert((short)21);
    int k = 0;
    int m;
    int n;
    int i1;
    int i4;
    int i5;
    while (true)
    {
      if (k >= paramInt2)
      {
        m = -1 + (paramInt1 + paramInt2);
        n = paramArrayOfByte[m];
        i1 = n & 0xFF;
        if (i1 <= paramInt2 - i)
          break;
        i4 = 1;
        i1 = 0;
        i5 = paramInt2 - i - i1;
        byte[] arrayOfByte1 = this.readMac.calculateMac(paramShort, paramArrayOfByte, paramInt1, i5);
        byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
        System.arraycopy(paramArrayOfByte, paramInt1 + i5, arrayOfByte2, 0, arrayOfByte1.length);
        if (!Arrays.constantTimeAreEqual(arrayOfByte1, arrayOfByte2))
          i4 = 1;
        if (i4 == 0)
          break label261;
        throw new TlsFatalAlert((short)20);
      }
      this.decryptCipher.processBlock(paramArrayOfByte, k + paramInt1, paramArrayOfByte, k + paramInt1);
      k += j;
    }
    int i2 = 0;
    for (int i3 = m - i1; ; i3++)
    {
      if (i3 >= m)
      {
        i4 = 0;
        if (i2 == 0)
          break;
        i4 = 1;
        i1 = 0;
        break;
      }
      i2 = (byte)(i2 | n ^ paramArrayOfByte[i3]);
    }
    label261: byte[] arrayOfByte3 = new byte[i5];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte3, 0, i5);
    return arrayOfByte3;
  }

  public byte[] encodePlaintext(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = this.encryptCipher.getBlockSize();
    int j = i - (1 + (paramInt2 + this.writeMac.getSize())) % i;
    int k = (255 - j) / i;
    int m = j + i * chooseExtraPadBlocks(this.context.getSecureRandom(), k);
    int n = 1 + (m + (paramInt2 + this.writeMac.getSize()));
    byte[] arrayOfByte1 = new byte[n];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, paramInt2);
    byte[] arrayOfByte2 = this.writeMac.calculateMac(paramShort, paramArrayOfByte, paramInt1, paramInt2);
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, paramInt2, arrayOfByte2.length);
    int i1 = paramInt2 + arrayOfByte2.length;
    int i2 = 0;
    int i3;
    if (i2 > m)
      i3 = 0;
    while (true)
    {
      if (i3 >= n)
      {
        return arrayOfByte1;
        arrayOfByte1[(i2 + i1)] = ((byte)m);
        i2++;
        break;
      }
      this.encryptCipher.processBlock(arrayOfByte1, i3, arrayOfByte1, i3);
      i3 += i;
    }
  }

  protected void initCipher(boolean paramBoolean, BlockCipher paramBlockCipher, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    paramBlockCipher.init(paramBoolean, new ParametersWithIV(new KeyParameter(paramArrayOfByte, paramInt2, paramInt1), paramArrayOfByte, paramInt3, paramBlockCipher.getBlockSize()));
  }

  protected int lowestBitSet(int paramInt)
  {
    int i;
    if (paramInt == 0)
      i = 32;
    while (true)
    {
      return i;
      i = 0;
      while ((paramInt & 0x1) == 0)
      {
        i++;
        paramInt >>= 1;
      }
    }
  }
}