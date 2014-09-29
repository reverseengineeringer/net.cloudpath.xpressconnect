package org.bouncycastle2.crypto.macs;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.paddings.BlockCipherPadding;

public class CFBBlockCipherMac
  implements Mac
{
  private byte[] buf;
  private int bufOff;
  private MacCFBBlockCipher cipher;
  private byte[] mac;
  private int macSize;
  private BlockCipherPadding padding = null;

  public CFBBlockCipherMac(BlockCipher paramBlockCipher)
  {
    this(paramBlockCipher, 8, 8 * paramBlockCipher.getBlockSize() / 2, null);
  }

  public CFBBlockCipherMac(BlockCipher paramBlockCipher, int paramInt1, int paramInt2)
  {
    this(paramBlockCipher, paramInt1, paramInt2, null);
  }

  public CFBBlockCipherMac(BlockCipher paramBlockCipher, int paramInt1, int paramInt2, BlockCipherPadding paramBlockCipherPadding)
  {
    if (paramInt2 % 8 != 0)
      throw new IllegalArgumentException("MAC size must be multiple of 8");
    this.mac = new byte[paramBlockCipher.getBlockSize()];
    this.cipher = new MacCFBBlockCipher(paramBlockCipher, paramInt1);
    this.padding = paramBlockCipherPadding;
    this.macSize = (paramInt2 / 8);
    this.buf = new byte[this.cipher.getBlockSize()];
    this.bufOff = 0;
  }

  public CFBBlockCipherMac(BlockCipher paramBlockCipher, BlockCipherPadding paramBlockCipherPadding)
  {
    this(paramBlockCipher, 8, 8 * paramBlockCipher.getBlockSize() / 2, paramBlockCipherPadding);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    int i = this.cipher.getBlockSize();
    if (this.padding == null)
      if (this.bufOff < i);
    while (true)
    {
      this.cipher.processBlock(this.buf, 0, this.mac, 0);
      this.cipher.getMacBlock(this.mac);
      System.arraycopy(this.mac, 0, paramArrayOfByte, paramInt, this.macSize);
      reset();
      return this.macSize;
      this.buf[this.bufOff] = 0;
      this.bufOff = (1 + this.bufOff);
      break;
      this.padding.addPadding(this.buf, this.bufOff);
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
    this.cipher.init(paramCipherParameters);
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
    int k;
    if (paramInt2 > j)
    {
      System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.bufOff, j);
      k = 0 + this.cipher.processBlock(this.buf, 0, this.mac, 0);
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
      k += this.cipher.processBlock(paramArrayOfByte, paramInt1, this.mac, 0);
      paramInt2 -= i;
      paramInt1 += i;
    }
  }
}