package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle2.crypto.modes.gcm.Tables8kGCMMultiplier;
import org.bouncycastle2.crypto.params.AEADParameters;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.crypto.util.Pack;
import org.bouncycastle2.util.Arrays;

public class GCMBlockCipher
  implements AEADBlockCipher
{
  private static final int BLOCK_SIZE = 16;
  private static final byte[] ZEROES = new byte[16];
  private byte[] A;
  private byte[] H;
  private byte[] J0;
  private byte[] S;
  private byte[] bufBlock;
  private int bufOff;
  private BlockCipher cipher;
  private byte[] counter;
  private boolean forEncryption;
  private byte[] initS;
  private KeyParameter keyParam;
  private byte[] macBlock;
  private int macSize;
  private GCMMultiplier multiplier;
  private byte[] nonce;
  private long totalLength;

  public GCMBlockCipher(BlockCipher paramBlockCipher)
  {
    this(paramBlockCipher, null);
  }

  public GCMBlockCipher(BlockCipher paramBlockCipher, GCMMultiplier paramGCMMultiplier)
  {
    if (paramBlockCipher.getBlockSize() != 16)
      throw new IllegalArgumentException("cipher required with a block size of 16.");
    if (paramGCMMultiplier == null)
      paramGCMMultiplier = new Tables8kGCMMultiplier();
    this.cipher = paramBlockCipher;
    this.multiplier = paramGCMMultiplier;
  }

  private void gCTRBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = 15;
    label11: byte[] arrayOfByte1;
    byte[] arrayOfByte2;
    if (i < 12)
    {
      arrayOfByte1 = new byte[16];
      this.cipher.processBlock(this.counter, 0, arrayOfByte1, 0);
      if (!this.forEncryption)
        break label140;
      System.arraycopy(ZEROES, paramInt1, arrayOfByte1, paramInt1, 16 - paramInt1);
      arrayOfByte2 = arrayOfByte1;
    }
    label60: for (int k = paramInt1 - 1; ; k--)
    {
      if (k < 0)
      {
        xor(this.S, arrayOfByte2);
        this.multiplier.multiplyH(this.S);
        this.totalLength += paramInt1;
        return;
        int j = (byte)(0xFF & 1 + this.counter[i]);
        this.counter[i] = j;
        if (j != 0)
          break label11;
        i--;
        break;
        arrayOfByte2 = paramArrayOfByte1;
        break label60;
      }
      arrayOfByte1[k] = ((byte)(arrayOfByte1[k] ^ paramArrayOfByte1[k]));
      paramArrayOfByte2[(paramInt2 + k)] = arrayOfByte1[k];
    }
  }

  private byte[] gHASH(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte1 = new byte[16];
    for (int i = 0; ; i += 16)
    {
      if (i >= paramArrayOfByte.length)
        return arrayOfByte1;
      byte[] arrayOfByte2 = new byte[16];
      System.arraycopy(paramArrayOfByte, i, arrayOfByte2, 0, Math.min(paramArrayOfByte.length - i, 16));
      xor(arrayOfByte1, arrayOfByte2);
      this.multiplier.multiplyH(arrayOfByte1);
    }
  }

  private static void packLength(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    Pack.intToBigEndian((int)(paramLong >>> 32), paramArrayOfByte, paramInt);
    Pack.intToBigEndian((int)paramLong, paramArrayOfByte, paramInt + 4);
  }

  private int process(byte paramByte, byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException
  {
    byte[] arrayOfByte = this.bufBlock;
    int i = this.bufOff;
    this.bufOff = (i + 1);
    arrayOfByte[i] = paramByte;
    if (this.bufOff == this.bufBlock.length)
    {
      gCTRBlock(this.bufBlock, 16, paramArrayOfByte, paramInt);
      if (!this.forEncryption)
        System.arraycopy(this.bufBlock, 16, this.bufBlock, 0, this.macSize);
      this.bufOff = (-16 + this.bufBlock.length);
      return 16;
    }
    return 0;
  }

  private void reset(boolean paramBoolean)
  {
    this.S = Arrays.clone(this.initS);
    this.counter = Arrays.clone(this.J0);
    this.bufOff = 0;
    this.totalLength = 0L;
    if (this.bufBlock != null)
      Arrays.fill(this.bufBlock, (byte)0);
    if (paramBoolean)
      this.macBlock = null;
    this.cipher.reset();
  }

  private static void xor(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    for (int i = 15; ; i--)
    {
      if (i < 0)
        return;
      paramArrayOfByte1[i] = ((byte)(paramArrayOfByte1[i] ^ paramArrayOfByte2[i]));
    }
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws IllegalStateException, InvalidCipherTextException
  {
    int i = this.bufOff;
    if (!this.forEncryption)
    {
      if (i < this.macSize)
        throw new InvalidCipherTextException("data too short");
      i -= this.macSize;
    }
    if (i > 0)
    {
      byte[] arrayOfByte4 = new byte[16];
      System.arraycopy(this.bufBlock, 0, arrayOfByte4, 0, i);
      gCTRBlock(arrayOfByte4, i, paramArrayOfByte, paramInt);
    }
    byte[] arrayOfByte1 = new byte[16];
    packLength(8L * this.A.length, arrayOfByte1, 0);
    packLength(8L * this.totalLength, arrayOfByte1, 8);
    xor(this.S, arrayOfByte1);
    this.multiplier.multiplyH(this.S);
    byte[] arrayOfByte2 = new byte[16];
    this.cipher.processBlock(this.J0, 0, arrayOfByte2, 0);
    xor(arrayOfByte2, this.S);
    int j = i;
    this.macBlock = new byte[this.macSize];
    System.arraycopy(arrayOfByte2, 0, this.macBlock, 0, this.macSize);
    if (this.forEncryption)
    {
      System.arraycopy(this.macBlock, 0, paramArrayOfByte, paramInt + this.bufOff, this.macSize);
      j += this.macSize;
    }
    byte[] arrayOfByte3;
    do
    {
      reset(false);
      return j;
      arrayOfByte3 = new byte[this.macSize];
      System.arraycopy(this.bufBlock, i, arrayOfByte3, 0, this.macSize);
    }
    while (Arrays.constantTimeAreEqual(this.macBlock, arrayOfByte3));
    throw new InvalidCipherTextException("mac check in GCM failed");
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/GCM";
  }

  public byte[] getMac()
  {
    return Arrays.clone(this.macBlock);
  }

  public int getOutputSize(int paramInt)
  {
    if (this.forEncryption)
      return paramInt + this.bufOff + this.macSize;
    return paramInt + this.bufOff - this.macSize;
  }

  public BlockCipher getUnderlyingCipher()
  {
    return this.cipher;
  }

  public int getUpdateOutputSize(int paramInt)
  {
    return 16 * ((paramInt + this.bufOff) / 16);
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.forEncryption = paramBoolean;
    this.macBlock = null;
    if ((paramCipherParameters instanceof AEADParameters))
    {
      AEADParameters localAEADParameters = (AEADParameters)paramCipherParameters;
      this.nonce = localAEADParameters.getNonce();
      this.A = localAEADParameters.getAssociatedText();
      int j = localAEADParameters.getMacSize();
      if ((j < 96) || (j > 128) || (j % 8 != 0))
        throw new IllegalArgumentException("Invalid value for MAC size: " + j);
      this.macSize = (j / 8);
      this.keyParam = localAEADParameters.getKey();
      if (!paramBoolean)
        break label211;
    }
    label211: for (int i = 16; ; i = 16 + this.macSize)
    {
      this.bufBlock = new byte[i];
      if ((this.nonce != null) && (this.nonce.length >= 1))
        break label223;
      throw new IllegalArgumentException("IV must be at least 1 byte");
      if ((paramCipherParameters instanceof ParametersWithIV))
      {
        ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
        this.nonce = localParametersWithIV.getIV();
        this.A = null;
        this.macSize = 16;
        this.keyParam = ((KeyParameter)localParametersWithIV.getParameters());
        break;
      }
      throw new IllegalArgumentException("invalid parameters passed to GCM");
    }
    label223: if (this.A == null)
      this.A = new byte[0];
    this.cipher.init(true, this.keyParam);
    this.H = new byte[16];
    this.cipher.processBlock(ZEROES, 0, this.H, 0);
    this.multiplier.init(this.H);
    this.initS = gHASH(this.A);
    if (this.nonce.length == 12)
    {
      this.J0 = new byte[16];
      System.arraycopy(this.nonce, 0, this.J0, 0, this.nonce.length);
      this.J0[15] = 1;
    }
    while (true)
    {
      this.S = Arrays.clone(this.initS);
      this.counter = Arrays.clone(this.J0);
      this.bufOff = 0;
      this.totalLength = 0L;
      return;
      this.J0 = gHASH(this.nonce);
      byte[] arrayOfByte = new byte[16];
      packLength(8L * this.nonce.length, arrayOfByte, 8);
      xor(this.J0, arrayOfByte);
      this.multiplier.multiplyH(this.J0);
    }
  }

  public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException
  {
    return process(paramByte, paramArrayOfByte, paramInt);
  }

  public int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws DataLengthException
  {
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j == paramInt2)
        return i;
      byte[] arrayOfByte = this.bufBlock;
      int k = this.bufOff;
      this.bufOff = (k + 1);
      arrayOfByte[k] = paramArrayOfByte1[(paramInt1 + j)];
      if (this.bufOff == this.bufBlock.length)
      {
        gCTRBlock(this.bufBlock, 16, paramArrayOfByte2, paramInt3 + i);
        if (!this.forEncryption)
          System.arraycopy(this.bufBlock, 16, this.bufBlock, 0, this.macSize);
        this.bufOff = (-16 + this.bufBlock.length);
        i += 16;
      }
    }
  }

  public void reset()
  {
    reset(true);
  }
}