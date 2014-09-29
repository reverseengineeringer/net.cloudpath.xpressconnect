package org.bouncycastle2.crypto.macs;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.paddings.ISO7816d4Padding;

public class CMac
  implements Mac
{
  private static final byte CONSTANT_128 = -121;
  private static final byte CONSTANT_64 = 27;
  private byte[] L;
  private byte[] Lu;
  private byte[] Lu2;
  private byte[] ZEROES;
  private byte[] buf;
  private int bufOff;
  private BlockCipher cipher;
  private byte[] mac;
  private int macSize;

  public CMac(BlockCipher paramBlockCipher)
  {
    this(paramBlockCipher, 8 * paramBlockCipher.getBlockSize());
  }

  public CMac(BlockCipher paramBlockCipher, int paramInt)
  {
    if (paramInt % 8 != 0)
      throw new IllegalArgumentException("MAC size must be multiple of 8");
    if (paramInt > 8 * paramBlockCipher.getBlockSize())
      throw new IllegalArgumentException("MAC size must be less or equal to " + 8 * paramBlockCipher.getBlockSize());
    if ((paramBlockCipher.getBlockSize() != 8) && (paramBlockCipher.getBlockSize() != 16))
      throw new IllegalArgumentException("Block size must be either 64 or 128 bits");
    this.cipher = new CBCBlockCipher(paramBlockCipher);
    this.macSize = (paramInt / 8);
    this.mac = new byte[paramBlockCipher.getBlockSize()];
    this.buf = new byte[paramBlockCipher.getBlockSize()];
    this.ZEROES = new byte[paramBlockCipher.getBlockSize()];
    this.bufOff = 0;
  }

  private byte[] doubleLu(byte[] paramArrayOfByte)
  {
    int i = (0xFF & paramArrayOfByte[0]) >> 7;
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    int j = 0;
    int k;
    int m;
    if (j >= -1 + paramArrayOfByte.length)
    {
      arrayOfByte[(-1 + paramArrayOfByte.length)] = ((byte)(paramArrayOfByte[(-1 + paramArrayOfByte.length)] << 1));
      if (i == 1)
      {
        k = -1 + paramArrayOfByte.length;
        m = arrayOfByte[k];
        if (paramArrayOfByte.length != 16)
          break label114;
      }
    }
    label114: for (int n = -121; ; n = 27)
    {
      arrayOfByte[k] = ((byte)(n ^ m));
      return arrayOfByte;
      arrayOfByte[j] = ((byte)((paramArrayOfByte[j] << 1) + ((0xFF & paramArrayOfByte[(j + 1)]) >> 7)));
      j++;
      break;
    }
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    int i = this.cipher.getBlockSize();
    byte[] arrayOfByte1;
    if (this.bufOff == i)
      arrayOfByte1 = this.Lu;
    for (int j = 0; ; j++)
    {
      if (j >= this.mac.length)
      {
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
        System.arraycopy(this.mac, 0, paramArrayOfByte, paramInt, this.macSize);
        reset();
        return this.macSize;
        new ISO7816d4Padding().addPadding(this.buf, this.bufOff);
        arrayOfByte1 = this.Lu2;
        break;
      }
      byte[] arrayOfByte2 = this.buf;
      arrayOfByte2[j] = ((byte)(arrayOfByte2[j] ^ arrayOfByte1[j]));
    }
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName();
  }

  public int getMacSize()
  {
    return this.macSize;
  }

  public void init(CipherParameters paramCipherParameters)
  {
    reset();
    this.cipher.init(true, paramCipherParameters);
    this.L = new byte[this.ZEROES.length];
    this.cipher.processBlock(this.ZEROES, 0, this.L, 0);
    this.Lu = doubleLu(this.L);
    this.Lu2 = doubleLu(this.Lu);
    this.cipher.init(true, paramCipherParameters);
  }

  public void reset()
  {
    for (int i = 0; ; i++)
    {
      if (i >= this.buf.length)
      {
        this.bufOff = 0;
        this.cipher.reset();
        return;
      }
      this.buf[i] = 0;
    }
  }

  public void update(byte paramByte)
  {
    if (this.bufOff == this.buf.length)
    {
      this.cipher.processBlock(this.buf, 0, this.mac, 0);
      this.bufOff = 0;
    }
    byte[] arrayOfByte = this.buf;
    int i = this.bufOff;
    this.bufOff = (i + 1);
    arrayOfByte[i] = paramByte;
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 < 0)
      throw new IllegalArgumentException("Can't have a negative input length!");
    int i = this.cipher.getBlockSize();
    int j = i - this.bufOff;
    if (paramInt2 > j)
    {
      System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.bufOff, j);
      this.cipher.processBlock(this.buf, 0, this.mac, 0);
      this.bufOff = 0;
      paramInt2 -= j;
      paramInt1 += j;
    }
    while (true)
    {
      if (paramInt2 <= i)
      {
        System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.bufOff, paramInt2);
        this.bufOff = (paramInt2 + this.bufOff);
        return;
      }
      this.cipher.processBlock(paramArrayOfByte, paramInt1, this.mac, 0);
      paramInt2 -= i;
      paramInt1 += i;
    }
  }
}