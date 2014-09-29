package org.bouncycastle2.crypto.digests;

import org.bouncycastle2.crypto.ExtendedDigest;

public abstract class GeneralDigest
  implements ExtendedDigest
{
  private static final int BYTE_LENGTH = 64;
  private long byteCount;
  private byte[] xBuf;
  private int xBufOff;

  protected GeneralDigest()
  {
    this.xBuf = new byte[4];
    this.xBufOff = 0;
  }

  protected GeneralDigest(GeneralDigest paramGeneralDigest)
  {
    this.xBuf = new byte[paramGeneralDigest.xBuf.length];
    System.arraycopy(paramGeneralDigest.xBuf, 0, this.xBuf, 0, paramGeneralDigest.xBuf.length);
    this.xBufOff = paramGeneralDigest.xBufOff;
    this.byteCount = paramGeneralDigest.byteCount;
  }

  public void finish()
  {
    long l = this.byteCount << 3;
    update((byte)-128);
    while (true)
    {
      if (this.xBufOff == 0)
      {
        processLength(l);
        processBlock();
        return;
      }
      update((byte)0);
    }
  }

  public int getByteLength()
  {
    return 64;
  }

  protected abstract void processBlock();

  protected abstract void processLength(long paramLong);

  protected abstract void processWord(byte[] paramArrayOfByte, int paramInt);

  public void reset()
  {
    this.byteCount = 0L;
    this.xBufOff = 0;
    for (int i = 0; ; i++)
    {
      if (i >= this.xBuf.length)
        return;
      this.xBuf[i] = 0;
    }
  }

  public void update(byte paramByte)
  {
    byte[] arrayOfByte = this.xBuf;
    int i = this.xBufOff;
    this.xBufOff = (i + 1);
    arrayOfByte[i] = paramByte;
    if (this.xBufOff == this.xBuf.length)
    {
      processWord(this.xBuf, 0);
      this.xBufOff = 0;
    }
    this.byteCount = (1L + this.byteCount);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((this.xBufOff == 0) || (paramInt2 <= 0))
      label11: if (paramInt2 > this.xBuf.length)
        break label41;
    while (true)
    {
      if (paramInt2 <= 0)
      {
        return;
        update(paramArrayOfByte[paramInt1]);
        paramInt1++;
        paramInt2--;
        break;
        label41: processWord(paramArrayOfByte, paramInt1);
        paramInt1 += this.xBuf.length;
        paramInt2 -= this.xBuf.length;
        this.byteCount += this.xBuf.length;
        break label11;
      }
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
  }
}