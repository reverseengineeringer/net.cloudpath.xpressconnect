package org.bouncycastle2.crypto.digests;

import org.bouncycastle2.crypto.ExtendedDigest;
import org.bouncycastle2.crypto.util.Pack;

public abstract class LongDigest
  implements ExtendedDigest
{
  private static final int BYTE_LENGTH = 128;
  static final long[] K = { 4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L };
  protected long H1;
  protected long H2;
  protected long H3;
  protected long H4;
  protected long H5;
  protected long H6;
  protected long H7;
  protected long H8;
  private long[] W = new long[80];
  private long byteCount1;
  private long byteCount2;
  private int wOff;
  private byte[] xBuf;
  private int xBufOff;

  protected LongDigest()
  {
    this.xBuf = new byte[8];
    this.xBufOff = 0;
    reset();
  }

  protected LongDigest(LongDigest paramLongDigest)
  {
    this.xBuf = new byte[paramLongDigest.xBuf.length];
    System.arraycopy(paramLongDigest.xBuf, 0, this.xBuf, 0, paramLongDigest.xBuf.length);
    this.xBufOff = paramLongDigest.xBufOff;
    this.byteCount1 = paramLongDigest.byteCount1;
    this.byteCount2 = paramLongDigest.byteCount2;
    this.H1 = paramLongDigest.H1;
    this.H2 = paramLongDigest.H2;
    this.H3 = paramLongDigest.H3;
    this.H4 = paramLongDigest.H4;
    this.H5 = paramLongDigest.H5;
    this.H6 = paramLongDigest.H6;
    this.H7 = paramLongDigest.H7;
    this.H8 = paramLongDigest.H8;
    System.arraycopy(paramLongDigest.W, 0, this.W, 0, paramLongDigest.W.length);
    this.wOff = paramLongDigest.wOff;
  }

  private long Ch(long paramLong1, long paramLong2, long paramLong3)
  {
    return paramLong1 & paramLong2 ^ paramLong3 & (0xFFFFFFFF ^ paramLong1);
  }

  private long Maj(long paramLong1, long paramLong2, long paramLong3)
  {
    return paramLong1 & paramLong2 ^ paramLong1 & paramLong3 ^ paramLong2 & paramLong3;
  }

  private long Sigma0(long paramLong)
  {
    return (paramLong << 63 | paramLong >>> 1) ^ (paramLong << 56 | paramLong >>> 8) ^ paramLong >>> 7;
  }

  private long Sigma1(long paramLong)
  {
    return (paramLong << 45 | paramLong >>> 19) ^ (paramLong << 3 | paramLong >>> 61) ^ paramLong >>> 6;
  }

  private long Sum0(long paramLong)
  {
    return (paramLong << 36 | paramLong >>> 28) ^ (paramLong << 30 | paramLong >>> 34) ^ (paramLong << 25 | paramLong >>> 39);
  }

  private long Sum1(long paramLong)
  {
    return (paramLong << 50 | paramLong >>> 14) ^ (paramLong << 46 | paramLong >>> 18) ^ (paramLong << 23 | paramLong >>> 41);
  }

  private void adjustByteCounts()
  {
    if (this.byteCount1 > 2305843009213693951L)
    {
      this.byteCount2 += (this.byteCount1 >>> 61);
      this.byteCount1 = (0xFFFFFFFF & this.byteCount1);
    }
  }

  public void finish()
  {
    adjustByteCounts();
    long l1 = this.byteCount1 << 3;
    long l2 = this.byteCount2;
    update((byte)-128);
    while (true)
    {
      if (this.xBufOff == 0)
      {
        processLength(l1, l2);
        processBlock();
        return;
      }
      update((byte)0);
    }
  }

  public int getByteLength()
  {
    return 128;
  }

  protected void processBlock()
  {
    adjustByteCounts();
    int i = 16;
    long l1;
    long l2;
    long l3;
    long l4;
    long l5;
    long l6;
    long l7;
    long l8;
    int j;
    int k;
    if (i > 79)
    {
      l1 = this.H1;
      l2 = this.H2;
      l3 = this.H3;
      l4 = this.H4;
      l5 = this.H5;
      l6 = this.H6;
      l7 = this.H7;
      l8 = this.H8;
      j = 0;
      k = 0;
      label66: if (j < 10)
        break label234;
      this.H1 = (l1 + this.H1);
      this.H2 = (l2 + this.H2);
      this.H3 = (l3 + this.H3);
      this.H4 = (l4 + this.H4);
      this.H5 = (l5 + this.H5);
      this.H6 = (l6 + this.H6);
      this.H7 = (l7 + this.H7);
      this.H8 = (l8 + this.H8);
      this.wOff = 0;
    }
    for (int i6 = 0; ; i6++)
    {
      if (i6 >= 16)
      {
        return;
        this.W[i] = (Sigma1(this.W[(i - 2)]) + this.W[(i - 7)] + Sigma0(this.W[(i - 15)]) + this.W[(i - 16)]);
        i++;
        break;
        label234: long l9 = Sum1(l5) + Ch(l5, l6, l7) + K[k];
        long[] arrayOfLong1 = this.W;
        int m = k + 1;
        long l10 = l8 + (l9 + arrayOfLong1[k]);
        long l11 = l4 + l10;
        long l12 = l10 + (Sum0(l1) + Maj(l1, l2, l3));
        long l13 = Sum1(l11) + Ch(l11, l5, l6) + K[m];
        long[] arrayOfLong2 = this.W;
        int n = m + 1;
        long l14 = l7 + (l13 + arrayOfLong2[m]);
        long l15 = l3 + l14;
        long l16 = l14 + (Sum0(l12) + Maj(l12, l1, l2));
        long l17 = Sum1(l15) + Ch(l15, l11, l5) + K[n];
        long[] arrayOfLong3 = this.W;
        int i1 = n + 1;
        long l18 = l6 + (l17 + arrayOfLong3[n]);
        long l19 = l2 + l18;
        long l20 = l18 + (Sum0(l16) + Maj(l16, l12, l1));
        long l21 = Sum1(l19) + Ch(l19, l15, l11) + K[i1];
        long[] arrayOfLong4 = this.W;
        int i2 = i1 + 1;
        long l22 = l5 + (l21 + arrayOfLong4[i1]);
        long l23 = l1 + l22;
        long l24 = l22 + (Sum0(l20) + Maj(l20, l16, l12));
        long l25 = Sum1(l23) + Ch(l23, l19, l15) + K[i2];
        long[] arrayOfLong5 = this.W;
        int i3 = i2 + 1;
        long l26 = l11 + (l25 + arrayOfLong5[i2]);
        l8 = l12 + l26;
        l4 = l26 + (Sum0(l24) + Maj(l24, l20, l16));
        long l27 = Sum1(l8) + Ch(l8, l23, l19) + K[i3];
        long[] arrayOfLong6 = this.W;
        int i4 = i3 + 1;
        long l28 = l15 + (l27 + arrayOfLong6[i3]);
        l7 = l16 + l28;
        l3 = l28 + (Sum0(l4) + Maj(l4, l24, l20));
        long l29 = Sum1(l7) + Ch(l7, l8, l23) + K[i4];
        long[] arrayOfLong7 = this.W;
        int i5 = i4 + 1;
        long l30 = l19 + (l29 + arrayOfLong7[i4]);
        l6 = l20 + l30;
        l2 = l30 + (Sum0(l3) + Maj(l3, l4, l24));
        long l31 = Sum1(l6) + Ch(l6, l7, l8) + K[i5];
        long[] arrayOfLong8 = this.W;
        k = i5 + 1;
        long l32 = l23 + (l31 + arrayOfLong8[i5]);
        l5 = l24 + l32;
        l1 = l32 + (Sum0(l2) + Maj(l2, l3, l4));
        j++;
        break label66;
      }
      this.W[i6] = 0L;
    }
  }

  protected void processLength(long paramLong1, long paramLong2)
  {
    if (this.wOff > 14)
      processBlock();
    this.W[14] = paramLong2;
    this.W[15] = paramLong1;
  }

  protected void processWord(byte[] paramArrayOfByte, int paramInt)
  {
    this.W[this.wOff] = Pack.bigEndianToLong(paramArrayOfByte, paramInt);
    int i = 1 + this.wOff;
    this.wOff = i;
    if (i == 16)
      processBlock();
  }

  public void reset()
  {
    this.byteCount1 = 0L;
    this.byteCount2 = 0L;
    this.xBufOff = 0;
    int i = 0;
    if (i >= this.xBuf.length)
      this.wOff = 0;
    for (int j = 0; ; j++)
    {
      if (j == this.W.length)
      {
        return;
        this.xBuf[i] = 0;
        i++;
        break;
      }
      this.W[j] = 0L;
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
    this.byteCount1 = (1L + this.byteCount1);
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
        this.byteCount1 += this.xBuf.length;
        break label11;
      }
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
  }
}