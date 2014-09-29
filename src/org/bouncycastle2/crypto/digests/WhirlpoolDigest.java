package org.bouncycastle2.crypto.digests;

import org.bouncycastle2.crypto.ExtendedDigest;
import org.bouncycastle2.util.Arrays;

public final class WhirlpoolDigest
  implements ExtendedDigest
{
  private static final int BITCOUNT_ARRAY_SIZE = 32;
  private static final int BYTE_LENGTH = 64;
  private static final long[] C0;
  private static final long[] C1;
  private static final long[] C2;
  private static final long[] C3;
  private static final long[] C4;
  private static final long[] C5;
  private static final long[] C6;
  private static final long[] C7;
  private static final int DIGEST_LENGTH_BYTES = 64;
  private static final short[] EIGHT;
  private static final int REDUCTION_POLYNOMIAL = 285;
  private static final int ROUNDS = 10;
  private static final int[] SBOX;
  private long[] _K = new long[8];
  private long[] _L = new long[8];
  private short[] _bitCount = new short[32];
  private long[] _block = new long[8];
  private byte[] _buffer = new byte[64];
  private int _bufferPos = 0;
  private long[] _hash = new long[8];
  private final long[] _rc = new long[11];
  private long[] _state = new long[8];

  static
  {
    int[] arrayOfInt = new int[256];
    arrayOfInt[0] = 24;
    arrayOfInt[1] = 35;
    arrayOfInt[2] = 198;
    arrayOfInt[3] = 232;
    arrayOfInt[4] = 135;
    arrayOfInt[5] = 184;
    arrayOfInt[6] = 1;
    arrayOfInt[7] = 79;
    arrayOfInt[8] = 54;
    arrayOfInt[9] = 166;
    arrayOfInt[10] = 210;
    arrayOfInt[11] = 245;
    arrayOfInt[12] = 121;
    arrayOfInt[13] = 111;
    arrayOfInt[14] = 145;
    arrayOfInt[15] = 82;
    arrayOfInt[16] = 96;
    arrayOfInt[17] = 188;
    arrayOfInt[18] = 155;
    arrayOfInt[19] = 142;
    arrayOfInt[20] = 163;
    arrayOfInt[21] = 12;
    arrayOfInt[22] = 123;
    arrayOfInt[23] = 53;
    arrayOfInt[24] = 29;
    arrayOfInt[25] = 224;
    arrayOfInt[26] = 215;
    arrayOfInt[27] = 194;
    arrayOfInt[28] = 46;
    arrayOfInt[29] = 75;
    arrayOfInt[30] = 254;
    arrayOfInt[31] = 87;
    arrayOfInt[32] = 21;
    arrayOfInt[33] = 119;
    arrayOfInt[34] = 55;
    arrayOfInt[35] = 229;
    arrayOfInt[36] = 159;
    arrayOfInt[37] = 240;
    arrayOfInt[38] = 74;
    arrayOfInt[39] = 218;
    arrayOfInt[40] = 88;
    arrayOfInt[41] = 201;
    arrayOfInt[42] = 41;
    arrayOfInt[43] = 10;
    arrayOfInt[44] = 177;
    arrayOfInt[45] = 160;
    arrayOfInt[46] = 107;
    arrayOfInt[47] = 133;
    arrayOfInt[48] = 189;
    arrayOfInt[49] = 93;
    arrayOfInt[50] = 16;
    arrayOfInt[51] = 244;
    arrayOfInt[52] = 203;
    arrayOfInt[53] = 62;
    arrayOfInt[54] = 5;
    arrayOfInt[55] = 103;
    arrayOfInt[56] = 228;
    arrayOfInt[57] = 39;
    arrayOfInt[58] = 65;
    arrayOfInt[59] = 139;
    arrayOfInt[60] = 167;
    arrayOfInt[61] = 125;
    arrayOfInt[62] = 149;
    arrayOfInt[63] = 216;
    arrayOfInt[64] = 251;
    arrayOfInt[65] = 238;
    arrayOfInt[66] = 124;
    arrayOfInt[67] = 102;
    arrayOfInt[68] = 221;
    arrayOfInt[69] = 23;
    arrayOfInt[70] = 71;
    arrayOfInt[71] = 158;
    arrayOfInt[72] = 202;
    arrayOfInt[73] = 45;
    arrayOfInt[74] = 191;
    arrayOfInt[75] = 7;
    arrayOfInt[76] = 173;
    arrayOfInt[77] = 90;
    arrayOfInt[78] = 131;
    arrayOfInt[79] = 51;
    arrayOfInt[80] = 99;
    arrayOfInt[81] = 2;
    arrayOfInt[82] = 170;
    arrayOfInt[83] = 113;
    arrayOfInt[84] = 200;
    arrayOfInt[85] = 25;
    arrayOfInt[86] = 73;
    arrayOfInt[87] = 217;
    arrayOfInt[88] = 242;
    arrayOfInt[89] = 227;
    arrayOfInt[90] = 91;
    arrayOfInt[91] = 136;
    arrayOfInt[92] = 154;
    arrayOfInt[93] = 38;
    arrayOfInt[94] = 50;
    arrayOfInt[95] = 176;
    arrayOfInt[96] = 233;
    arrayOfInt[97] = 15;
    arrayOfInt[98] = 213;
    arrayOfInt[99] = 128;
    arrayOfInt[100] = 190;
    arrayOfInt[101] = 205;
    arrayOfInt[102] = 52;
    arrayOfInt[103] = 72;
    arrayOfInt[104] = 255;
    arrayOfInt[105] = 122;
    arrayOfInt[106] = 144;
    arrayOfInt[107] = 95;
    arrayOfInt[108] = 32;
    arrayOfInt[109] = 104;
    arrayOfInt[110] = 26;
    arrayOfInt[111] = 174;
    arrayOfInt[112] = 180;
    arrayOfInt[113] = 84;
    arrayOfInt[114] = 147;
    arrayOfInt[115] = 34;
    arrayOfInt[116] = 100;
    arrayOfInt[117] = 241;
    arrayOfInt[118] = 115;
    arrayOfInt[119] = 18;
    arrayOfInt[120] = 64;
    arrayOfInt[121] = 8;
    arrayOfInt[122] = 195;
    arrayOfInt[123] = 236;
    arrayOfInt[124] = 219;
    arrayOfInt[125] = 161;
    arrayOfInt[126] = 141;
    arrayOfInt[127] = 61;
    arrayOfInt[''] = 151;
    arrayOfInt[''] = 207;
    arrayOfInt[''] = 43;
    arrayOfInt[''] = 118;
    arrayOfInt[''] = 130;
    arrayOfInt[''] = 214;
    arrayOfInt[''] = 27;
    arrayOfInt[''] = 181;
    arrayOfInt[''] = 175;
    arrayOfInt[''] = 106;
    arrayOfInt[''] = 80;
    arrayOfInt[''] = 69;
    arrayOfInt[''] = 243;
    arrayOfInt[''] = 48;
    arrayOfInt[''] = 239;
    arrayOfInt[''] = 63;
    arrayOfInt[''] = 85;
    arrayOfInt[''] = 162;
    arrayOfInt[''] = 234;
    arrayOfInt[''] = 101;
    arrayOfInt[''] = 186;
    arrayOfInt[''] = 47;
    arrayOfInt[''] = 192;
    arrayOfInt[''] = 222;
    arrayOfInt[''] = 28;
    arrayOfInt[''] = 253;
    arrayOfInt[''] = 77;
    arrayOfInt[''] = 146;
    arrayOfInt[''] = 117;
    arrayOfInt[''] = 6;
    arrayOfInt[''] = 138;
    arrayOfInt[' '] = 178;
    arrayOfInt['¡'] = 230;
    arrayOfInt['¢'] = 14;
    arrayOfInt['£'] = 31;
    arrayOfInt['¤'] = 98;
    arrayOfInt['¥'] = 212;
    arrayOfInt['¦'] = 168;
    arrayOfInt['§'] = 150;
    arrayOfInt['¨'] = 249;
    arrayOfInt['©'] = 197;
    arrayOfInt['ª'] = 37;
    arrayOfInt['«'] = 89;
    arrayOfInt['¬'] = 132;
    arrayOfInt['­'] = 114;
    arrayOfInt['®'] = 57;
    arrayOfInt['¯'] = 76;
    arrayOfInt['°'] = 94;
    arrayOfInt['±'] = 120;
    arrayOfInt['²'] = 56;
    arrayOfInt['³'] = 140;
    arrayOfInt['´'] = 209;
    arrayOfInt['µ'] = 165;
    arrayOfInt['¶'] = 226;
    arrayOfInt['·'] = 97;
    arrayOfInt['¸'] = 179;
    arrayOfInt['¹'] = 33;
    arrayOfInt['º'] = 156;
    arrayOfInt['»'] = 30;
    arrayOfInt['¼'] = 67;
    arrayOfInt['½'] = 199;
    arrayOfInt['¾'] = 252;
    arrayOfInt['¿'] = 4;
    arrayOfInt['À'] = 81;
    arrayOfInt['Á'] = 153;
    arrayOfInt['Â'] = 109;
    arrayOfInt['Ã'] = 13;
    arrayOfInt['Ä'] = 250;
    arrayOfInt['Å'] = 223;
    arrayOfInt['Æ'] = 126;
    arrayOfInt['Ç'] = 36;
    arrayOfInt['È'] = 59;
    arrayOfInt['É'] = 171;
    arrayOfInt['Ê'] = 206;
    arrayOfInt['Ë'] = 17;
    arrayOfInt['Ì'] = 143;
    arrayOfInt['Í'] = 78;
    arrayOfInt['Î'] = 183;
    arrayOfInt['Ï'] = 235;
    arrayOfInt['Ð'] = 60;
    arrayOfInt['Ñ'] = 129;
    arrayOfInt['Ò'] = 148;
    arrayOfInt['Ó'] = 247;
    arrayOfInt['Ô'] = 185;
    arrayOfInt['Õ'] = 19;
    arrayOfInt['Ö'] = 44;
    arrayOfInt['×'] = 211;
    arrayOfInt['Ø'] = 231;
    arrayOfInt['Ù'] = 110;
    arrayOfInt['Ú'] = 196;
    arrayOfInt['Û'] = 3;
    arrayOfInt['Ü'] = 86;
    arrayOfInt['Ý'] = 68;
    arrayOfInt['Þ'] = 127;
    arrayOfInt['ß'] = 169;
    arrayOfInt['à'] = 42;
    arrayOfInt['á'] = 187;
    arrayOfInt['â'] = 193;
    arrayOfInt['ã'] = 83;
    arrayOfInt['ä'] = 220;
    arrayOfInt['å'] = 11;
    arrayOfInt['æ'] = 157;
    arrayOfInt['ç'] = 108;
    arrayOfInt['è'] = 49;
    arrayOfInt['é'] = 116;
    arrayOfInt['ê'] = 246;
    arrayOfInt['ë'] = 70;
    arrayOfInt['ì'] = 172;
    arrayOfInt['í'] = 137;
    arrayOfInt['î'] = 20;
    arrayOfInt['ï'] = 225;
    arrayOfInt['ð'] = 22;
    arrayOfInt['ñ'] = 58;
    arrayOfInt['ò'] = 105;
    arrayOfInt['ó'] = 9;
    arrayOfInt['ô'] = 112;
    arrayOfInt['õ'] = 182;
    arrayOfInt['ö'] = 208;
    arrayOfInt['÷'] = 237;
    arrayOfInt['ø'] = 204;
    arrayOfInt['ù'] = 66;
    arrayOfInt['ú'] = 152;
    arrayOfInt['û'] = 164;
    arrayOfInt['ü'] = 40;
    arrayOfInt['ý'] = 92;
    arrayOfInt['þ'] = 248;
    arrayOfInt['ÿ'] = 134;
    SBOX = arrayOfInt;
    C0 = new long[256];
    C1 = new long[256];
    C2 = new long[256];
    C3 = new long[256];
    C4 = new long[256];
    C5 = new long[256];
    C6 = new long[256];
    C7 = new long[256];
    EIGHT = new short[32];
    EIGHT[31] = 8;
  }

  public WhirlpoolDigest()
  {
    int i = 0;
    if (i >= 256)
      this._rc[0] = 0L;
    for (int i3 = 1; ; i3++)
    {
      if (i3 > 10)
      {
        return;
        int j = SBOX[i];
        int k = maskWithReductionPolynomial(j << 1);
        int m = maskWithReductionPolynomial(k << 1);
        int n = m ^ j;
        int i1 = maskWithReductionPolynomial(m << 1);
        int i2 = i1 ^ j;
        C0[i] = packIntoLong(j, j, m, j, i1, n, k, i2);
        C1[i] = packIntoLong(i2, j, j, m, j, i1, n, k);
        C2[i] = packIntoLong(k, i2, j, j, m, j, i1, n);
        C3[i] = packIntoLong(n, k, i2, j, j, m, j, i1);
        C4[i] = packIntoLong(i1, n, k, i2, j, j, m, j);
        C5[i] = packIntoLong(j, i1, n, k, i2, j, j, m);
        C6[i] = packIntoLong(m, j, i1, n, k, i2, j, j);
        C7[i] = packIntoLong(j, m, j, i1, n, k, i2, j);
        i++;
        break;
      }
      int i4 = 8 * (i3 - 1);
      this._rc[i3] = (0x0 & C0[i4] ^ 0x0 & C1[(i4 + 1)] ^ 0x0 & C2[(i4 + 2)] ^ 0x0 & C3[(i4 + 3)] ^ 0xFF000000 & C4[(i4 + 4)] ^ 0xFF0000 & C5[(i4 + 5)] ^ 0xFF00 & C6[(i4 + 6)] ^ 0xFF & C7[(i4 + 7)]);
    }
  }

  public WhirlpoolDigest(WhirlpoolDigest paramWhirlpoolDigest)
  {
    System.arraycopy(paramWhirlpoolDigest._rc, 0, this._rc, 0, this._rc.length);
    System.arraycopy(paramWhirlpoolDigest._buffer, 0, this._buffer, 0, this._buffer.length);
    this._bufferPos = paramWhirlpoolDigest._bufferPos;
    System.arraycopy(paramWhirlpoolDigest._bitCount, 0, this._bitCount, 0, this._bitCount.length);
    System.arraycopy(paramWhirlpoolDigest._hash, 0, this._hash, 0, this._hash.length);
    System.arraycopy(paramWhirlpoolDigest._K, 0, this._K, 0, this._K.length);
    System.arraycopy(paramWhirlpoolDigest._L, 0, this._L, 0, this._L.length);
    System.arraycopy(paramWhirlpoolDigest._block, 0, this._block, 0, this._block.length);
    System.arraycopy(paramWhirlpoolDigest._state, 0, this._state, 0, this._state.length);
  }

  private long bytesToLongFromBuffer(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFF & paramArrayOfByte[(paramInt + 0)]) << 56 | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 48 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 40 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 32 | (0xFF & paramArrayOfByte[(paramInt + 4)]) << 24 | (0xFF & paramArrayOfByte[(paramInt + 5)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 6)]) << 8 | 0xFF & paramArrayOfByte[(paramInt + 7)];
  }

  private void convertLongToByteArray(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 8)
        return;
      paramArrayOfByte[(paramInt + i)] = ((byte)(int)(0xFF & paramLong >> 56 - i * 8));
    }
  }

  private byte[] copyBitLength()
  {
    byte[] arrayOfByte = new byte[32];
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)(0xFF & this._bitCount[i]));
    }
  }

  private void finish()
  {
    byte[] arrayOfByte1 = copyBitLength();
    byte[] arrayOfByte2 = this._buffer;
    int i = this._bufferPos;
    this._bufferPos = (i + 1);
    arrayOfByte2[i] = ((byte)(0x80 | arrayOfByte2[i]));
    if (this._bufferPos == this._buffer.length)
      processFilledBuffer(this._buffer, 0);
    if (this._bufferPos > 32)
      if (this._bufferPos != 0)
        break label102;
    while (true)
    {
      if (this._bufferPos > 32)
      {
        System.arraycopy(arrayOfByte1, 0, this._buffer, 32, arrayOfByte1.length);
        processFilledBuffer(this._buffer, 0);
        return;
        label102: update((byte)0);
        break;
      }
      update((byte)0);
    }
  }

  private void increment()
  {
    int i = 0;
    for (int j = -1 + this._bitCount.length; ; j--)
    {
      if (j < 0)
        return;
      int k = i + ((0xFF & this._bitCount[j]) + EIGHT[j]);
      i = k >>> 8;
      this._bitCount[j] = ((short)(k & 0xFF));
    }
  }

  private int maskWithReductionPolynomial(int paramInt)
  {
    int i = paramInt;
    if (i >= 256L)
      i ^= 285;
    return i;
  }

  private long packIntoLong(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    return paramInt1 << 56 ^ paramInt2 << 48 ^ paramInt3 << 40 ^ paramInt4 << 32 ^ paramInt5 << 24 ^ paramInt6 << 16 ^ paramInt7 << 8 ^ paramInt8;
  }

  private void processFilledBuffer(byte[] paramArrayOfByte, int paramInt)
  {
    for (int i = 0; ; i++)
    {
      if (i >= this._state.length)
      {
        processBlock();
        this._bufferPos = 0;
        Arrays.fill(this._buffer, (byte)0);
        return;
      }
      this._block[i] = bytesToLongFromBuffer(this._buffer, i * 8);
    }
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    for (int i = 0; ; i++)
    {
      if (i >= 8)
      {
        reset();
        return getDigestSize();
      }
      convertLongToByteArray(this._hash[i], paramArrayOfByte, paramInt + i * 8);
    }
  }

  public String getAlgorithmName()
  {
    return "Whirlpool";
  }

  public int getByteLength()
  {
    return 64;
  }

  public int getDigestSize()
  {
    return 64;
  }

  protected void processBlock()
  {
    int i = 0;
    int j;
    if (i >= 8)
    {
      j = 1;
      if (j <= 10)
        break label74;
    }
    for (int n = 0; ; n++)
    {
      if (n >= 8)
      {
        return;
        long[] arrayOfLong1 = this._state;
        long l1 = this._block[i];
        long[] arrayOfLong2 = this._K;
        long l2 = this._hash[i];
        arrayOfLong2[i] = l2;
        arrayOfLong1[i] = (l1 ^ l2);
        i++;
        break;
        label74: int k = 0;
        label77: if (k >= 8)
        {
          System.arraycopy(this._L, 0, this._K, 0, this._K.length);
          long[] arrayOfLong11 = this._K;
          arrayOfLong11[0] ^= this._rc[j];
        }
        for (int m = 0; ; m++)
        {
          if (m >= 8)
          {
            System.arraycopy(this._L, 0, this._state, 0, this._state.length);
            j++;
            break;
            this._L[k] = 0L;
            long[] arrayOfLong3 = this._L;
            arrayOfLong3[k] ^= C0[(0xFF & (int)(this._K[(0x7 & k + 0)] >>> 56))];
            long[] arrayOfLong4 = this._L;
            arrayOfLong4[k] ^= C1[(0xFF & (int)(this._K[(0x7 & k - 1)] >>> 48))];
            long[] arrayOfLong5 = this._L;
            arrayOfLong5[k] ^= C2[(0xFF & (int)(this._K[(0x7 & k - 2)] >>> 40))];
            long[] arrayOfLong6 = this._L;
            arrayOfLong6[k] ^= C3[(0xFF & (int)(this._K[(0x7 & k - 3)] >>> 32))];
            long[] arrayOfLong7 = this._L;
            arrayOfLong7[k] ^= C4[(0xFF & (int)(this._K[(0x7 & k - 4)] >>> 24))];
            long[] arrayOfLong8 = this._L;
            arrayOfLong8[k] ^= C5[(0xFF & (int)(this._K[(0x7 & k - 5)] >>> 16))];
            long[] arrayOfLong9 = this._L;
            arrayOfLong9[k] ^= C6[(0xFF & (int)(this._K[(0x7 & k - 6)] >>> 8))];
            long[] arrayOfLong10 = this._L;
            arrayOfLong10[k] ^= C7[(0xFF & (int)this._K[(0x7 & k - 7)])];
            k++;
            break label77;
          }
          this._L[m] = this._K[m];
          long[] arrayOfLong12 = this._L;
          arrayOfLong12[m] ^= C0[(0xFF & (int)(this._state[(0x7 & m + 0)] >>> 56))];
          long[] arrayOfLong13 = this._L;
          arrayOfLong13[m] ^= C1[(0xFF & (int)(this._state[(0x7 & m - 1)] >>> 48))];
          long[] arrayOfLong14 = this._L;
          arrayOfLong14[m] ^= C2[(0xFF & (int)(this._state[(0x7 & m - 2)] >>> 40))];
          long[] arrayOfLong15 = this._L;
          arrayOfLong15[m] ^= C3[(0xFF & (int)(this._state[(0x7 & m - 3)] >>> 32))];
          long[] arrayOfLong16 = this._L;
          arrayOfLong16[m] ^= C4[(0xFF & (int)(this._state[(0x7 & m - 4)] >>> 24))];
          long[] arrayOfLong17 = this._L;
          arrayOfLong17[m] ^= C5[(0xFF & (int)(this._state[(0x7 & m - 5)] >>> 16))];
          long[] arrayOfLong18 = this._L;
          arrayOfLong18[m] ^= C6[(0xFF & (int)(this._state[(0x7 & m - 6)] >>> 8))];
          long[] arrayOfLong19 = this._L;
          arrayOfLong19[m] ^= C7[(0xFF & (int)this._state[(0x7 & m - 7)])];
        }
      }
      long[] arrayOfLong20 = this._hash;
      arrayOfLong20[n] ^= this._state[n] ^ this._block[n];
    }
  }

  public void reset()
  {
    this._bufferPos = 0;
    Arrays.fill(this._bitCount, (short)0);
    Arrays.fill(this._buffer, (byte)0);
    Arrays.fill(this._hash, 0L);
    Arrays.fill(this._K, 0L);
    Arrays.fill(this._L, 0L);
    Arrays.fill(this._block, 0L);
    Arrays.fill(this._state, 0L);
  }

  public void update(byte paramByte)
  {
    this._buffer[this._bufferPos] = paramByte;
    this._bufferPos = (1 + this._bufferPos);
    if (this._bufferPos == this._buffer.length)
      processFilledBuffer(this._buffer, 0);
    increment();
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    while (true)
    {
      if (paramInt2 <= 0)
        return;
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
  }
}