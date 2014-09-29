package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.RC2Parameters;

public class RC2Engine
  implements BlockCipher
{
  private static final int BLOCK_SIZE = 8;
  private static byte[] piTable = arrayOfByte;
  private boolean encrypting;
  private int[] workingKey;

  static
  {
    byte[] arrayOfByte = new byte[256];
    arrayOfByte[0] = -39;
    arrayOfByte[1] = 120;
    arrayOfByte[2] = -7;
    arrayOfByte[3] = -60;
    arrayOfByte[4] = 25;
    arrayOfByte[5] = -35;
    arrayOfByte[6] = -75;
    arrayOfByte[7] = -19;
    arrayOfByte[8] = 40;
    arrayOfByte[9] = -23;
    arrayOfByte[10] = -3;
    arrayOfByte[11] = 121;
    arrayOfByte[12] = 74;
    arrayOfByte[13] = -96;
    arrayOfByte[14] = -40;
    arrayOfByte[15] = -99;
    arrayOfByte[16] = -58;
    arrayOfByte[17] = 126;
    arrayOfByte[18] = 55;
    arrayOfByte[19] = -125;
    arrayOfByte[20] = 43;
    arrayOfByte[21] = 118;
    arrayOfByte[22] = 83;
    arrayOfByte[23] = -114;
    arrayOfByte[24] = 98;
    arrayOfByte[25] = 76;
    arrayOfByte[26] = 100;
    arrayOfByte[27] = -120;
    arrayOfByte[28] = 68;
    arrayOfByte[29] = -117;
    arrayOfByte[30] = -5;
    arrayOfByte[31] = -94;
    arrayOfByte[32] = 23;
    arrayOfByte[33] = -102;
    arrayOfByte[34] = 89;
    arrayOfByte[35] = -11;
    arrayOfByte[36] = -121;
    arrayOfByte[37] = -77;
    arrayOfByte[38] = 79;
    arrayOfByte[39] = 19;
    arrayOfByte[40] = 97;
    arrayOfByte[41] = 69;
    arrayOfByte[42] = 109;
    arrayOfByte[43] = -115;
    arrayOfByte[44] = 9;
    arrayOfByte[45] = -127;
    arrayOfByte[46] = 125;
    arrayOfByte[47] = 50;
    arrayOfByte[48] = -67;
    arrayOfByte[49] = -113;
    arrayOfByte[50] = 64;
    arrayOfByte[51] = -21;
    arrayOfByte[52] = -122;
    arrayOfByte[53] = -73;
    arrayOfByte[54] = 123;
    arrayOfByte[55] = 11;
    arrayOfByte[56] = -16;
    arrayOfByte[57] = -107;
    arrayOfByte[58] = 33;
    arrayOfByte[59] = 34;
    arrayOfByte[60] = 92;
    arrayOfByte[61] = 107;
    arrayOfByte[62] = 78;
    arrayOfByte[63] = -126;
    arrayOfByte[64] = 84;
    arrayOfByte[65] = -42;
    arrayOfByte[66] = 101;
    arrayOfByte[67] = -109;
    arrayOfByte[68] = -50;
    arrayOfByte[69] = 96;
    arrayOfByte[70] = -78;
    arrayOfByte[71] = 28;
    arrayOfByte[72] = 115;
    arrayOfByte[73] = 86;
    arrayOfByte[74] = -64;
    arrayOfByte[75] = 20;
    arrayOfByte[76] = -89;
    arrayOfByte[77] = -116;
    arrayOfByte[78] = -15;
    arrayOfByte[79] = -36;
    arrayOfByte[80] = 18;
    arrayOfByte[81] = 117;
    arrayOfByte[82] = -54;
    arrayOfByte[83] = 31;
    arrayOfByte[84] = 59;
    arrayOfByte[85] = -66;
    arrayOfByte[86] = -28;
    arrayOfByte[87] = -47;
    arrayOfByte[88] = 66;
    arrayOfByte[89] = 61;
    arrayOfByte[90] = -44;
    arrayOfByte[91] = 48;
    arrayOfByte[92] = -93;
    arrayOfByte[93] = 60;
    arrayOfByte[94] = -74;
    arrayOfByte[95] = 38;
    arrayOfByte[96] = 111;
    arrayOfByte[97] = -65;
    arrayOfByte[98] = 14;
    arrayOfByte[99] = -38;
    arrayOfByte[100] = 70;
    arrayOfByte[101] = 105;
    arrayOfByte[102] = 7;
    arrayOfByte[103] = 87;
    arrayOfByte[104] = 39;
    arrayOfByte[105] = -14;
    arrayOfByte[106] = 29;
    arrayOfByte[107] = -101;
    arrayOfByte[108] = -68;
    arrayOfByte[109] = -108;
    arrayOfByte[110] = 67;
    arrayOfByte[111] = 3;
    arrayOfByte[112] = -8;
    arrayOfByte[113] = 17;
    arrayOfByte[114] = -57;
    arrayOfByte[115] = -10;
    arrayOfByte[116] = -112;
    arrayOfByte[117] = -17;
    arrayOfByte[118] = 62;
    arrayOfByte[119] = -25;
    arrayOfByte[120] = 6;
    arrayOfByte[121] = -61;
    arrayOfByte[122] = -43;
    arrayOfByte[123] = 47;
    arrayOfByte[124] = -56;
    arrayOfByte[125] = 102;
    arrayOfByte[126] = 30;
    arrayOfByte[127] = -41;
    arrayOfByte[''] = 8;
    arrayOfByte[''] = -24;
    arrayOfByte[''] = -22;
    arrayOfByte[''] = -34;
    arrayOfByte[''] = -128;
    arrayOfByte[''] = 82;
    arrayOfByte[''] = -18;
    arrayOfByte[''] = -9;
    arrayOfByte[''] = -124;
    arrayOfByte[''] = -86;
    arrayOfByte[''] = 114;
    arrayOfByte[''] = -84;
    arrayOfByte[''] = 53;
    arrayOfByte[''] = 77;
    arrayOfByte[''] = 106;
    arrayOfByte[''] = 42;
    arrayOfByte[''] = -106;
    arrayOfByte[''] = 26;
    arrayOfByte[''] = -46;
    arrayOfByte[''] = 113;
    arrayOfByte[''] = 90;
    arrayOfByte[''] = 21;
    arrayOfByte[''] = 73;
    arrayOfByte[''] = 116;
    arrayOfByte[''] = 75;
    arrayOfByte[''] = -97;
    arrayOfByte[''] = -48;
    arrayOfByte[''] = 94;
    arrayOfByte[''] = 4;
    arrayOfByte[''] = 24;
    arrayOfByte[''] = -92;
    arrayOfByte[''] = -20;
    arrayOfByte[' '] = -62;
    arrayOfByte['¡'] = -32;
    arrayOfByte['¢'] = 65;
    arrayOfByte['£'] = 110;
    arrayOfByte['¤'] = 15;
    arrayOfByte['¥'] = 81;
    arrayOfByte['¦'] = -53;
    arrayOfByte['§'] = -52;
    arrayOfByte['¨'] = 36;
    arrayOfByte['©'] = -111;
    arrayOfByte['ª'] = -81;
    arrayOfByte['«'] = 80;
    arrayOfByte['¬'] = -95;
    arrayOfByte['­'] = -12;
    arrayOfByte['®'] = 112;
    arrayOfByte['¯'] = 57;
    arrayOfByte['°'] = -103;
    arrayOfByte['±'] = 124;
    arrayOfByte['²'] = 58;
    arrayOfByte['³'] = -123;
    arrayOfByte['´'] = 35;
    arrayOfByte['µ'] = -72;
    arrayOfByte['¶'] = -76;
    arrayOfByte['·'] = 122;
    arrayOfByte['¸'] = -4;
    arrayOfByte['¹'] = 2;
    arrayOfByte['º'] = 54;
    arrayOfByte['»'] = 91;
    arrayOfByte['¼'] = 37;
    arrayOfByte['½'] = 85;
    arrayOfByte['¾'] = -105;
    arrayOfByte['¿'] = 49;
    arrayOfByte['À'] = 45;
    arrayOfByte['Á'] = 93;
    arrayOfByte['Â'] = -6;
    arrayOfByte['Ã'] = -104;
    arrayOfByte['Ä'] = -29;
    arrayOfByte['Å'] = -118;
    arrayOfByte['Æ'] = -110;
    arrayOfByte['Ç'] = -82;
    arrayOfByte['È'] = 5;
    arrayOfByte['É'] = -33;
    arrayOfByte['Ê'] = 41;
    arrayOfByte['Ë'] = 16;
    arrayOfByte['Ì'] = 103;
    arrayOfByte['Í'] = 108;
    arrayOfByte['Î'] = -70;
    arrayOfByte['Ï'] = -55;
    arrayOfByte['Ð'] = -45;
    arrayOfByte['Ò'] = -26;
    arrayOfByte['Ó'] = -49;
    arrayOfByte['Ô'] = -31;
    arrayOfByte['Õ'] = -98;
    arrayOfByte['Ö'] = -88;
    arrayOfByte['×'] = 44;
    arrayOfByte['Ø'] = 99;
    arrayOfByte['Ù'] = 22;
    arrayOfByte['Ú'] = 1;
    arrayOfByte['Û'] = 63;
    arrayOfByte['Ü'] = 88;
    arrayOfByte['Ý'] = -30;
    arrayOfByte['Þ'] = -119;
    arrayOfByte['ß'] = -87;
    arrayOfByte['à'] = 13;
    arrayOfByte['á'] = 56;
    arrayOfByte['â'] = 52;
    arrayOfByte['ã'] = 27;
    arrayOfByte['ä'] = -85;
    arrayOfByte['å'] = 51;
    arrayOfByte['æ'] = -1;
    arrayOfByte['ç'] = -80;
    arrayOfByte['è'] = -69;
    arrayOfByte['é'] = 72;
    arrayOfByte['ê'] = 12;
    arrayOfByte['ë'] = 95;
    arrayOfByte['ì'] = -71;
    arrayOfByte['í'] = -79;
    arrayOfByte['î'] = -51;
    arrayOfByte['ï'] = 46;
    arrayOfByte['ð'] = -59;
    arrayOfByte['ñ'] = -13;
    arrayOfByte['ò'] = -37;
    arrayOfByte['ó'] = 71;
    arrayOfByte['ô'] = -27;
    arrayOfByte['õ'] = -91;
    arrayOfByte['ö'] = -100;
    arrayOfByte['÷'] = 119;
    arrayOfByte['ø'] = 10;
    arrayOfByte['ù'] = -90;
    arrayOfByte['ú'] = 32;
    arrayOfByte['û'] = 104;
    arrayOfByte['ü'] = -2;
    arrayOfByte['ý'] = 127;
    arrayOfByte['þ'] = -63;
    arrayOfByte['ÿ'] = -83;
  }

  private void decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = ((0xFF & paramArrayOfByte1[(paramInt1 + 7)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 6)]);
    int j = ((0xFF & paramArrayOfByte1[(paramInt1 + 5)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 4)]);
    int k = ((0xFF & paramArrayOfByte1[(paramInt1 + 3)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 2)]);
    int m = ((0xFF & paramArrayOfByte1[(paramInt1 + 1)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 0)]);
    int n = 60;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    label173: int i6;
    int i7;
    int i8;
    int i9;
    if (n < 44)
    {
      i1 = i - this.workingKey[(j & 0x3F)];
      i2 = j - this.workingKey[(k & 0x3F)];
      i3 = k - this.workingKey[(m & 0x3F)];
      i4 = m - this.workingKey[(i1 & 0x3F)];
      i5 = 40;
      if (i5 >= 20)
        break label476;
      i6 = i1 - this.workingKey[(i2 & 0x3F)];
      i7 = i2 - this.workingKey[(i3 & 0x3F)];
      i8 = i3 - this.workingKey[(i4 & 0x3F)];
      i9 = i4 - this.workingKey[(i6 & 0x3F)];
    }
    for (int i10 = 16; ; i10 -= 4)
    {
      if (i10 < 0)
      {
        paramArrayOfByte2[(paramInt2 + 0)] = ((byte)i9);
        paramArrayOfByte2[(paramInt2 + 1)] = ((byte)(i9 >> 8));
        paramArrayOfByte2[(paramInt2 + 2)] = ((byte)i8);
        paramArrayOfByte2[(paramInt2 + 3)] = ((byte)(i8 >> 8));
        paramArrayOfByte2[(paramInt2 + 4)] = ((byte)i7);
        paramArrayOfByte2[(paramInt2 + 5)] = ((byte)(i7 >> 8));
        paramArrayOfByte2[(paramInt2 + 6)] = ((byte)i6);
        paramArrayOfByte2[(paramInt2 + 7)] = ((byte)(i6 >> 8));
        return;
        i = rotateWordLeft(i, 11) - ((m & (j ^ 0xFFFFFFFF)) + (k & j) + this.workingKey[(n + 3)]);
        j = rotateWordLeft(j, 13) - ((i & (k ^ 0xFFFFFFFF)) + (m & k) + this.workingKey[(n + 2)]);
        k = rotateWordLeft(k, 14) - ((j & (m ^ 0xFFFFFFFF)) + (i & m) + this.workingKey[(n + 1)]);
        m = rotateWordLeft(m, 15) - ((k & (i ^ 0xFFFFFFFF)) + (j & i) + this.workingKey[n]);
        n -= 4;
        break;
        label476: i1 = rotateWordLeft(i1, 11) - ((i4 & (i2 ^ 0xFFFFFFFF)) + (i3 & i2) + this.workingKey[(i5 + 3)]);
        i2 = rotateWordLeft(i2, 13) - ((i1 & (i3 ^ 0xFFFFFFFF)) + (i4 & i3) + this.workingKey[(i5 + 2)]);
        i3 = rotateWordLeft(i3, 14) - ((i2 & (i4 ^ 0xFFFFFFFF)) + (i1 & i4) + this.workingKey[(i5 + 1)]);
        i4 = rotateWordLeft(i4, 15) - ((i3 & (i1 ^ 0xFFFFFFFF)) + (i2 & i1) + this.workingKey[i5]);
        i5 -= 4;
        break label173;
      }
      i6 = rotateWordLeft(i6, 11) - ((i9 & (i7 ^ 0xFFFFFFFF)) + (i8 & i7) + this.workingKey[(i10 + 3)]);
      i7 = rotateWordLeft(i7, 13) - ((i6 & (i8 ^ 0xFFFFFFFF)) + (i9 & i8) + this.workingKey[(i10 + 2)]);
      i8 = rotateWordLeft(i8, 14) - ((i7 & (i9 ^ 0xFFFFFFFF)) + (i6 & i9) + this.workingKey[(i10 + 1)]);
      i9 = rotateWordLeft(i9, 15) - ((i8 & (i6 ^ 0xFFFFFFFF)) + (i7 & i6) + this.workingKey[i10]);
    }
  }

  private void encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = ((0xFF & paramArrayOfByte1[(paramInt1 + 7)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 6)]);
    int j = ((0xFF & paramArrayOfByte1[(paramInt1 + 5)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 4)]);
    int k = ((0xFF & paramArrayOfByte1[(paramInt1 + 3)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 2)]);
    int m = ((0xFF & paramArrayOfByte1[(paramInt1 + 1)]) << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 0)]);
    int n = 0;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    label172: int i6;
    int i7;
    int i8;
    int i9;
    if (n > 16)
    {
      i1 = m + this.workingKey[(i & 0x3F)];
      i2 = k + this.workingKey[(i1 & 0x3F)];
      i3 = j + this.workingKey[(i2 & 0x3F)];
      i4 = i + this.workingKey[(i3 & 0x3F)];
      i5 = 20;
      if (i5 <= 40)
        break label473;
      i6 = i1 + this.workingKey[(i4 & 0x3F)];
      i7 = i2 + this.workingKey[(i6 & 0x3F)];
      i8 = i3 + this.workingKey[(i7 & 0x3F)];
      i9 = i4 + this.workingKey[(i8 & 0x3F)];
    }
    for (int i10 = 44; ; i10 += 4)
    {
      if (i10 >= 64)
      {
        paramArrayOfByte2[(paramInt2 + 0)] = ((byte)i6);
        paramArrayOfByte2[(paramInt2 + 1)] = ((byte)(i6 >> 8));
        paramArrayOfByte2[(paramInt2 + 2)] = ((byte)i7);
        paramArrayOfByte2[(paramInt2 + 3)] = ((byte)(i7 >> 8));
        paramArrayOfByte2[(paramInt2 + 4)] = ((byte)i8);
        paramArrayOfByte2[(paramInt2 + 5)] = ((byte)(i8 >> 8));
        paramArrayOfByte2[(paramInt2 + 6)] = ((byte)i9);
        paramArrayOfByte2[(paramInt2 + 7)] = ((byte)(i9 >> 8));
        return;
        m = rotateWordLeft(m + (k & (i ^ 0xFFFFFFFF)) + (j & i) + this.workingKey[n], 1);
        k = rotateWordLeft(k + (j & (m ^ 0xFFFFFFFF)) + (i & m) + this.workingKey[(n + 1)], 2);
        j = rotateWordLeft(j + (i & (k ^ 0xFFFFFFFF)) + (m & k) + this.workingKey[(n + 2)], 3);
        i = rotateWordLeft(i + (m & (j ^ 0xFFFFFFFF)) + (k & j) + this.workingKey[(n + 3)], 5);
        n += 4;
        break;
        label473: i1 = rotateWordLeft(i1 + (i2 & (i4 ^ 0xFFFFFFFF)) + (i3 & i4) + this.workingKey[i5], 1);
        i2 = rotateWordLeft(i2 + (i3 & (i1 ^ 0xFFFFFFFF)) + (i4 & i1) + this.workingKey[(i5 + 1)], 2);
        i3 = rotateWordLeft(i3 + (i4 & (i2 ^ 0xFFFFFFFF)) + (i1 & i2) + this.workingKey[(i5 + 2)], 3);
        i4 = rotateWordLeft(i4 + (i1 & (i3 ^ 0xFFFFFFFF)) + (i2 & i3) + this.workingKey[(i5 + 3)], 5);
        i5 += 4;
        break label172;
      }
      i6 = rotateWordLeft(i6 + (i7 & (i9 ^ 0xFFFFFFFF)) + (i8 & i9) + this.workingKey[i10], 1);
      i7 = rotateWordLeft(i7 + (i8 & (i6 ^ 0xFFFFFFFF)) + (i9 & i6) + this.workingKey[(i10 + 1)], 2);
      i8 = rotateWordLeft(i8 + (i9 & (i7 ^ 0xFFFFFFFF)) + (i6 & i7) + this.workingKey[(i10 + 2)], 3);
      i9 = rotateWordLeft(i9 + (i6 & (i8 ^ 0xFFFFFFFF)) + (i7 & i8) + this.workingKey[(i10 + 3)], 5);
    }
  }

  private int[] generateWorkingKey(byte[] paramArrayOfByte, int paramInt)
  {
    int[] arrayOfInt1 = new int[''];
    int i = 0;
    int j;
    int i2;
    int i3;
    if (i == paramArrayOfByte.length)
    {
      j = paramArrayOfByte.length;
      if (j < 128)
      {
        i2 = 0;
        i3 = arrayOfInt1[(j - 1)];
      }
    }
    while (true)
    {
      byte[] arrayOfByte = piTable;
      int i4 = i2 + 1;
      i3 = 0xFF & arrayOfByte[(0xFF & i3 + arrayOfInt1[i2])];
      int i5 = j + 1;
      arrayOfInt1[j] = i3;
      if (i5 >= 128)
      {
        int k = paramInt + 7 >> 3;
        int m = 0xFF & piTable[(arrayOfInt1[(128 - k)] & 255 >> (0x7 & -paramInt))];
        arrayOfInt1[(128 - k)] = m;
        int n = -1 + (128 - k);
        label146: int[] arrayOfInt2;
        if (n < 0)
          arrayOfInt2 = new int[64];
        for (int i1 = 0; ; i1++)
        {
          if (i1 == arrayOfInt2.length)
          {
            return arrayOfInt2;
            arrayOfInt1[i] = (0xFF & paramArrayOfByte[i]);
            i++;
            break;
            m = 0xFF & piTable[(m ^ arrayOfInt1[(n + k)])];
            arrayOfInt1[n] = m;
            n--;
            break label146;
          }
          arrayOfInt2[i1] = (arrayOfInt1[(i1 * 2)] + (arrayOfInt1[(1 + i1 * 2)] << 8));
        }
      }
      i2 = i4;
      j = i5;
    }
  }

  private int rotateWordLeft(int paramInt1, int paramInt2)
  {
    int i = paramInt1 & 0xFFFF;
    return i << paramInt2 | i >> 16 - paramInt2;
  }

  public String getAlgorithmName()
  {
    return "RC2";
  }

  public int getBlockSize()
  {
    return 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.encrypting = paramBoolean;
    if ((paramCipherParameters instanceof RC2Parameters))
    {
      RC2Parameters localRC2Parameters = (RC2Parameters)paramCipherParameters;
      this.workingKey = generateWorkingKey(localRC2Parameters.getKey(), localRC2Parameters.getEffectiveKeyBits());
      return;
    }
    if ((paramCipherParameters instanceof KeyParameter))
    {
      byte[] arrayOfByte = ((KeyParameter)paramCipherParameters).getKey();
      this.workingKey = generateWorkingKey(arrayOfByte, 8 * arrayOfByte.length);
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to RC2 init - " + paramCipherParameters.getClass().getName());
  }

  public final int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.workingKey == null)
      throw new IllegalStateException("RC2 engine not initialised");
    if (paramInt1 + 8 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 8 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this.encrypting)
      encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    while (true)
    {
      return 8;
      decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    }
  }

  public void reset()
  {
  }
}