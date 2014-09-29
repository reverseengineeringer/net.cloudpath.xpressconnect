package org.bouncycastle2.crypto.modes;

import java.io.ByteArrayOutputStream;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle2.crypto.params.AEADParameters;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.util.Arrays;

public class CCMBlockCipher
  implements AEADBlockCipher
{
  private byte[] associatedText;
  private int blockSize;
  private BlockCipher cipher;
  private ByteArrayOutputStream data = new ByteArrayOutputStream();
  private boolean forEncryption;
  private CipherParameters keyParam;
  private byte[] macBlock;
  private int macSize;
  private byte[] nonce;

  public CCMBlockCipher(BlockCipher paramBlockCipher)
  {
    this.cipher = paramBlockCipher;
    this.blockSize = paramBlockCipher.getBlockSize();
    this.macBlock = new byte[this.blockSize];
    if (this.blockSize != 16)
      throw new IllegalArgumentException("cipher required with a block size of 16.");
  }

  private int calculateMac(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2)
  {
    CBCBlockCipherMac localCBCBlockCipherMac = new CBCBlockCipherMac(this.cipher, 8 * this.macSize);
    localCBCBlockCipherMac.init(this.keyParam);
    byte[] arrayOfByte = new byte[16];
    if (hasAssociatedText())
      arrayOfByte[0] = ((byte)(0x40 | arrayOfByte[0]));
    arrayOfByte[0] = ((byte)(arrayOfByte[0] | (0x7 & (-2 + localCBCBlockCipherMac.getMacSize()) / 2) << 3));
    arrayOfByte[0] = ((byte)(arrayOfByte[0] | 0x7 & -1 + (15 - this.nonce.length)));
    System.arraycopy(this.nonce, 0, arrayOfByte, 1, this.nonce.length);
    int i = paramInt2;
    int j = 1;
    int k;
    label195: int m;
    if (i <= 0)
    {
      localCBCBlockCipherMac.update(arrayOfByte, 0, arrayOfByte.length);
      if (hasAssociatedText())
      {
        if (this.associatedText.length >= 65280)
          break label293;
        localCBCBlockCipherMac.update((byte)(this.associatedText.length >> 8));
        localCBCBlockCipherMac.update((byte)this.associatedText.length);
        k = 2;
        localCBCBlockCipherMac.update(this.associatedText, 0, this.associatedText.length);
        m = (k + this.associatedText.length) % 16;
        if (m == 0);
      }
    }
    for (int n = 0; ; n++)
    {
      if (n == 16 - m)
      {
        localCBCBlockCipherMac.update(paramArrayOfByte1, paramInt1, paramInt2);
        return localCBCBlockCipherMac.doFinal(paramArrayOfByte2, 0);
        arrayOfByte[(arrayOfByte.length - j)] = ((byte)(i & 0xFF));
        i >>>= 8;
        j++;
        break;
        label293: localCBCBlockCipherMac.update((byte)-1);
        localCBCBlockCipherMac.update((byte)-2);
        localCBCBlockCipherMac.update((byte)(this.associatedText.length >> 24));
        localCBCBlockCipherMac.update((byte)(this.associatedText.length >> 16));
        localCBCBlockCipherMac.update((byte)(this.associatedText.length >> 8));
        localCBCBlockCipherMac.update((byte)this.associatedText.length);
        k = 6;
        break label195;
      }
      localCBCBlockCipherMac.update((byte)0);
    }
  }

  private boolean hasAssociatedText()
  {
    return (this.associatedText != null) && (this.associatedText.length != 0);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws IllegalStateException, InvalidCipherTextException
  {
    byte[] arrayOfByte1 = this.data.toByteArray();
    byte[] arrayOfByte2 = processPacket(arrayOfByte1, 0, arrayOfByte1.length);
    System.arraycopy(arrayOfByte2, 0, paramArrayOfByte, paramInt, arrayOfByte2.length);
    reset();
    return arrayOfByte2.length;
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/CCM";
  }

  public byte[] getMac()
  {
    byte[] arrayOfByte = new byte[this.macSize];
    System.arraycopy(this.macBlock, 0, arrayOfByte, 0, arrayOfByte.length);
    return arrayOfByte;
  }

  public int getOutputSize(int paramInt)
  {
    if (this.forEncryption)
      return paramInt + this.data.size() + this.macSize;
    return paramInt + this.data.size() - this.macSize;
  }

  public BlockCipher getUnderlyingCipher()
  {
    return this.cipher;
  }

  public int getUpdateOutputSize(int paramInt)
  {
    return 0;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.forEncryption = paramBoolean;
    if ((paramCipherParameters instanceof AEADParameters))
    {
      AEADParameters localAEADParameters = (AEADParameters)paramCipherParameters;
      this.nonce = localAEADParameters.getNonce();
      this.associatedText = localAEADParameters.getAssociatedText();
      this.macSize = (localAEADParameters.getMacSize() / 8);
      this.keyParam = localAEADParameters.getKey();
      return;
    }
    if ((paramCipherParameters instanceof ParametersWithIV))
    {
      ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
      this.nonce = localParametersWithIV.getIV();
      this.associatedText = null;
      this.macSize = (this.macBlock.length / 2);
      this.keyParam = localParametersWithIV.getParameters();
      return;
    }
    throw new IllegalArgumentException("invalid parameters passed to CCM");
  }

  public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException
  {
    this.data.write(paramByte);
    return 0;
  }

  public int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws DataLengthException, IllegalStateException
  {
    this.data.write(paramArrayOfByte1, paramInt1, paramInt2);
    return 0;
  }

  public byte[] processPacket(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IllegalStateException, InvalidCipherTextException
  {
    if (this.keyParam == null)
      throw new IllegalStateException("CCM cipher unitialized.");
    SICBlockCipher localSICBlockCipher = new SICBlockCipher(this.cipher);
    byte[] arrayOfByte1 = new byte[this.blockSize];
    arrayOfByte1[0] = ((byte)(0x7 & -1 + (15 - this.nonce.length)));
    System.arraycopy(this.nonce, 0, arrayOfByte1, 1, this.nonce.length);
    localSICBlockCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, arrayOfByte1));
    if (this.forEncryption)
    {
      int n = paramInt1;
      int i1 = 0;
      arrayOfByte2 = new byte[paramInt2 + this.macSize];
      calculateMac(paramArrayOfByte, paramInt1, paramInt2, this.macBlock);
      localSICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
      while (true)
      {
        if (n >= paramInt2 - this.blockSize)
        {
          byte[] arrayOfByte5 = new byte[this.blockSize];
          System.arraycopy(paramArrayOfByte, n, arrayOfByte5, 0, paramInt2 - n);
          localSICBlockCipher.processBlock(arrayOfByte5, 0, arrayOfByte5, 0);
          System.arraycopy(arrayOfByte5, 0, arrayOfByte2, i1, paramInt2 - n);
          int i2 = i1 + (paramInt2 - n);
          System.arraycopy(this.macBlock, 0, arrayOfByte2, i2, arrayOfByte2.length - i2);
          return arrayOfByte2;
        }
        localSICBlockCipher.processBlock(paramArrayOfByte, n, arrayOfByte2, i1);
        i1 += this.blockSize;
        n += this.blockSize;
      }
    }
    int i = paramInt1;
    byte[] arrayOfByte2 = new byte[paramInt2 - this.macSize];
    System.arraycopy(paramArrayOfByte, paramInt1 + paramInt2 - this.macSize, this.macBlock, 0, this.macSize);
    localSICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
    int j = this.macSize;
    label333: int k = this.macBlock.length;
    int m = 0;
    if (j == k);
    while (true)
    {
      if (m >= arrayOfByte2.length - this.blockSize)
      {
        byte[] arrayOfByte3 = new byte[this.blockSize];
        System.arraycopy(paramArrayOfByte, i, arrayOfByte3, 0, arrayOfByte2.length - m);
        localSICBlockCipher.processBlock(arrayOfByte3, 0, arrayOfByte3, 0);
        System.arraycopy(arrayOfByte3, 0, arrayOfByte2, m, arrayOfByte2.length - m);
        byte[] arrayOfByte4 = new byte[this.blockSize];
        calculateMac(arrayOfByte2, 0, arrayOfByte2.length, arrayOfByte4);
        if (Arrays.constantTimeAreEqual(this.macBlock, arrayOfByte4))
          break;
        throw new InvalidCipherTextException("mac check in CCM failed");
        this.macBlock[j] = 0;
        j++;
        break label333;
      }
      localSICBlockCipher.processBlock(paramArrayOfByte, i, arrayOfByte2, m);
      m += this.blockSize;
      i += this.blockSize;
    }
  }

  public void reset()
  {
    this.cipher.reset();
    this.data.reset();
  }
}