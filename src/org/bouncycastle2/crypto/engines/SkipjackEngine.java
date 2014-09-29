package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class SkipjackEngine
  implements BlockCipher
{
  static final int BLOCK_SIZE = 8;
  static short[] ftable = arrayOfShort;
  private boolean encrypting;
  private int[] key0;
  private int[] key1;
  private int[] key2;
  private int[] key3;

  static
  {
    short[] arrayOfShort = new short[256];
    arrayOfShort[0] = 163;
    arrayOfShort[1] = 215;
    arrayOfShort[2] = 9;
    arrayOfShort[3] = 131;
    arrayOfShort[4] = 248;
    arrayOfShort[5] = 72;
    arrayOfShort[6] = 246;
    arrayOfShort[7] = 244;
    arrayOfShort[8] = 179;
    arrayOfShort[9] = 33;
    arrayOfShort[10] = 21;
    arrayOfShort[11] = 120;
    arrayOfShort[12] = 153;
    arrayOfShort[13] = 177;
    arrayOfShort[14] = 175;
    arrayOfShort[15] = 249;
    arrayOfShort[16] = 231;
    arrayOfShort[17] = 45;
    arrayOfShort[18] = 77;
    arrayOfShort[19] = 138;
    arrayOfShort[20] = 206;
    arrayOfShort[21] = 76;
    arrayOfShort[22] = 202;
    arrayOfShort[23] = 46;
    arrayOfShort[24] = 82;
    arrayOfShort[25] = 149;
    arrayOfShort[26] = 217;
    arrayOfShort[27] = 30;
    arrayOfShort[28] = 78;
    arrayOfShort[29] = 56;
    arrayOfShort[30] = 68;
    arrayOfShort[31] = 40;
    arrayOfShort[32] = 10;
    arrayOfShort[33] = 223;
    arrayOfShort[34] = 2;
    arrayOfShort[35] = 160;
    arrayOfShort[36] = 23;
    arrayOfShort[37] = 241;
    arrayOfShort[38] = 96;
    arrayOfShort[39] = 104;
    arrayOfShort[40] = 18;
    arrayOfShort[41] = 183;
    arrayOfShort[42] = 122;
    arrayOfShort[43] = 195;
    arrayOfShort[44] = 233;
    arrayOfShort[45] = 250;
    arrayOfShort[46] = 61;
    arrayOfShort[47] = 83;
    arrayOfShort[48] = 150;
    arrayOfShort[49] = 132;
    arrayOfShort[50] = 107;
    arrayOfShort[51] = 186;
    arrayOfShort[52] = 242;
    arrayOfShort[53] = 99;
    arrayOfShort[54] = 154;
    arrayOfShort[55] = 25;
    arrayOfShort[56] = 124;
    arrayOfShort[57] = 174;
    arrayOfShort[58] = 229;
    arrayOfShort[59] = 245;
    arrayOfShort[60] = 247;
    arrayOfShort[61] = 22;
    arrayOfShort[62] = 106;
    arrayOfShort[63] = 162;
    arrayOfShort[64] = 57;
    arrayOfShort[65] = 182;
    arrayOfShort[66] = 123;
    arrayOfShort[67] = 15;
    arrayOfShort[68] = 193;
    arrayOfShort[69] = 147;
    arrayOfShort[70] = 129;
    arrayOfShort[71] = 27;
    arrayOfShort[72] = 238;
    arrayOfShort[73] = 180;
    arrayOfShort[74] = 26;
    arrayOfShort[75] = 234;
    arrayOfShort[76] = 208;
    arrayOfShort[77] = 145;
    arrayOfShort[78] = 47;
    arrayOfShort[79] = 184;
    arrayOfShort[80] = 85;
    arrayOfShort[81] = 185;
    arrayOfShort[82] = 218;
    arrayOfShort[83] = 133;
    arrayOfShort[84] = 63;
    arrayOfShort[85] = 65;
    arrayOfShort[86] = 191;
    arrayOfShort[87] = 224;
    arrayOfShort[88] = 90;
    arrayOfShort[89] = 88;
    arrayOfShort[90] = 128;
    arrayOfShort[91] = 95;
    arrayOfShort[92] = 102;
    arrayOfShort[93] = 11;
    arrayOfShort[94] = 216;
    arrayOfShort[95] = 144;
    arrayOfShort[96] = 53;
    arrayOfShort[97] = 213;
    arrayOfShort[98] = 192;
    arrayOfShort[99] = 167;
    arrayOfShort[100] = 51;
    arrayOfShort[101] = 6;
    arrayOfShort[102] = 101;
    arrayOfShort[103] = 105;
    arrayOfShort[104] = 69;
    arrayOfShort[106] = 148;
    arrayOfShort[107] = 86;
    arrayOfShort[108] = 109;
    arrayOfShort[109] = 152;
    arrayOfShort[110] = 155;
    arrayOfShort[111] = 118;
    arrayOfShort[112] = 151;
    arrayOfShort[113] = 252;
    arrayOfShort[114] = 178;
    arrayOfShort[115] = 194;
    arrayOfShort[116] = 176;
    arrayOfShort[117] = 254;
    arrayOfShort[118] = 219;
    arrayOfShort[119] = 32;
    arrayOfShort[120] = 225;
    arrayOfShort[121] = 235;
    arrayOfShort[122] = 214;
    arrayOfShort[123] = 228;
    arrayOfShort[124] = 221;
    arrayOfShort[125] = 71;
    arrayOfShort[126] = 74;
    arrayOfShort[127] = 29;
    arrayOfShort[''] = 66;
    arrayOfShort[''] = 237;
    arrayOfShort[''] = 158;
    arrayOfShort[''] = 110;
    arrayOfShort[''] = 73;
    arrayOfShort[''] = 60;
    arrayOfShort[''] = 205;
    arrayOfShort[''] = 67;
    arrayOfShort[''] = 39;
    arrayOfShort[''] = 210;
    arrayOfShort[''] = 7;
    arrayOfShort[''] = 212;
    arrayOfShort[''] = 222;
    arrayOfShort[''] = 199;
    arrayOfShort[''] = 103;
    arrayOfShort[''] = 24;
    arrayOfShort[''] = 137;
    arrayOfShort[''] = 203;
    arrayOfShort[''] = 48;
    arrayOfShort[''] = 31;
    arrayOfShort[''] = 141;
    arrayOfShort[''] = 198;
    arrayOfShort[''] = 143;
    arrayOfShort[''] = 170;
    arrayOfShort[''] = 200;
    arrayOfShort[''] = 116;
    arrayOfShort[''] = 220;
    arrayOfShort[''] = 201;
    arrayOfShort[''] = 93;
    arrayOfShort[''] = 92;
    arrayOfShort[''] = 49;
    arrayOfShort[''] = 164;
    arrayOfShort[' '] = 112;
    arrayOfShort['¡'] = 136;
    arrayOfShort['¢'] = 97;
    arrayOfShort['£'] = 44;
    arrayOfShort['¤'] = 159;
    arrayOfShort['¥'] = 13;
    arrayOfShort['¦'] = 43;
    arrayOfShort['§'] = 135;
    arrayOfShort['¨'] = 80;
    arrayOfShort['©'] = 130;
    arrayOfShort['ª'] = 84;
    arrayOfShort['«'] = 100;
    arrayOfShort['¬'] = 38;
    arrayOfShort['­'] = 125;
    arrayOfShort['®'] = 3;
    arrayOfShort['¯'] = 64;
    arrayOfShort['°'] = 52;
    arrayOfShort['±'] = 75;
    arrayOfShort['²'] = 28;
    arrayOfShort['³'] = 115;
    arrayOfShort['´'] = 209;
    arrayOfShort['µ'] = 196;
    arrayOfShort['¶'] = 253;
    arrayOfShort['·'] = 59;
    arrayOfShort['¸'] = 204;
    arrayOfShort['¹'] = 251;
    arrayOfShort['º'] = 127;
    arrayOfShort['»'] = 171;
    arrayOfShort['¼'] = 230;
    arrayOfShort['½'] = 62;
    arrayOfShort['¾'] = 91;
    arrayOfShort['¿'] = 165;
    arrayOfShort['À'] = 173;
    arrayOfShort['Á'] = 4;
    arrayOfShort['Â'] = 35;
    arrayOfShort['Ã'] = 156;
    arrayOfShort['Ä'] = 20;
    arrayOfShort['Å'] = 81;
    arrayOfShort['Æ'] = 34;
    arrayOfShort['Ç'] = 240;
    arrayOfShort['È'] = 41;
    arrayOfShort['É'] = 121;
    arrayOfShort['Ê'] = 113;
    arrayOfShort['Ë'] = 126;
    arrayOfShort['Ì'] = 255;
    arrayOfShort['Í'] = 140;
    arrayOfShort['Î'] = 14;
    arrayOfShort['Ï'] = 226;
    arrayOfShort['Ð'] = 12;
    arrayOfShort['Ñ'] = 239;
    arrayOfShort['Ò'] = 188;
    arrayOfShort['Ó'] = 114;
    arrayOfShort['Ô'] = 117;
    arrayOfShort['Õ'] = 111;
    arrayOfShort['Ö'] = 55;
    arrayOfShort['×'] = 161;
    arrayOfShort['Ø'] = 236;
    arrayOfShort['Ù'] = 211;
    arrayOfShort['Ú'] = 142;
    arrayOfShort['Û'] = 98;
    arrayOfShort['Ü'] = 139;
    arrayOfShort['Ý'] = 134;
    arrayOfShort['Þ'] = 16;
    arrayOfShort['ß'] = 232;
    arrayOfShort['à'] = 8;
    arrayOfShort['á'] = 119;
    arrayOfShort['â'] = 17;
    arrayOfShort['ã'] = 190;
    arrayOfShort['ä'] = 146;
    arrayOfShort['å'] = 79;
    arrayOfShort['æ'] = 36;
    arrayOfShort['ç'] = 197;
    arrayOfShort['è'] = 50;
    arrayOfShort['é'] = 54;
    arrayOfShort['ê'] = 157;
    arrayOfShort['ë'] = 207;
    arrayOfShort['ì'] = 243;
    arrayOfShort['í'] = 166;
    arrayOfShort['î'] = 187;
    arrayOfShort['ï'] = 172;
    arrayOfShort['ð'] = 94;
    arrayOfShort['ñ'] = 108;
    arrayOfShort['ò'] = 169;
    arrayOfShort['ó'] = 19;
    arrayOfShort['ô'] = 87;
    arrayOfShort['õ'] = 37;
    arrayOfShort['ö'] = 181;
    arrayOfShort['÷'] = 227;
    arrayOfShort['ø'] = 189;
    arrayOfShort['ù'] = 168;
    arrayOfShort['ú'] = 58;
    arrayOfShort['û'] = 1;
    arrayOfShort['ü'] = 5;
    arrayOfShort['ý'] = 89;
    arrayOfShort['þ'] = 42;
    arrayOfShort['ÿ'] = 70;
  }

  private int g(int paramInt1, int paramInt2)
  {
    int i = 0xFF & paramInt2 >> 8;
    int j = paramInt2 & 0xFF;
    int k = i ^ ftable[(j ^ this.key0[paramInt1])];
    int m = j ^ ftable[(k ^ this.key1[paramInt1])];
    int n = k ^ ftable[(m ^ this.key2[paramInt1])];
    return (m ^ ftable[(n ^ this.key3[paramInt1])]) + (n << 8);
  }

  private int h(int paramInt1, int paramInt2)
  {
    int i = paramInt2 & 0xFF;
    int j = 0xFF & paramInt2 >> 8;
    int k = i ^ ftable[(j ^ this.key3[paramInt1])];
    int m = j ^ ftable[(k ^ this.key2[paramInt1])];
    int n = k ^ ftable[(m ^ this.key1[paramInt1])];
    return n + ((m ^ ftable[(n ^ this.key0[paramInt1])]) << 8);
  }

  public int decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = (paramArrayOfByte1[(paramInt1 + 0)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 1)]);
    int j = (paramArrayOfByte1[(paramInt1 + 2)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 3)]);
    int k = (paramArrayOfByte1[(paramInt1 + 4)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 5)]);
    int m = (paramArrayOfByte1[(paramInt1 + 6)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 7)]);
    int n = 31;
    int i1 = 0;
    if (i1 >= 2)
    {
      paramArrayOfByte2[(paramInt2 + 0)] = ((byte)(i >> 8));
      paramArrayOfByte2[(paramInt2 + 1)] = ((byte)i);
      paramArrayOfByte2[(paramInt2 + 2)] = ((byte)(j >> 8));
      paramArrayOfByte2[(paramInt2 + 3)] = ((byte)j);
      paramArrayOfByte2[(paramInt2 + 4)] = ((byte)(k >> 8));
      paramArrayOfByte2[(paramInt2 + 5)] = ((byte)k);
      paramArrayOfByte2[(paramInt2 + 6)] = ((byte)(m >> 8));
      paramArrayOfByte2[(paramInt2 + 7)] = ((byte)m);
      return 8;
    }
    int i2 = 0;
    label187: if (i2 >= 8);
    for (int i4 = 0; ; i4++)
    {
      if (i4 >= 8)
      {
        i1++;
        break;
        int i3 = k;
        k = m;
        m = i;
        i = h(n, j);
        j = i ^ i3 ^ n + 1;
        n--;
        i2++;
        break label187;
      }
      int i5 = k;
      k = m;
      m = j ^ i ^ n + 1;
      i = h(n, j);
      j = i5;
      n--;
    }
  }

  public int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = (paramArrayOfByte1[(paramInt1 + 0)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 1)]);
    int j = (paramArrayOfByte1[(paramInt1 + 2)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 3)]);
    int k = (paramArrayOfByte1[(paramInt1 + 4)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 5)]);
    int m = (paramArrayOfByte1[(paramInt1 + 6)] << 8) + (0xFF & paramArrayOfByte1[(paramInt1 + 7)]);
    int n = 0;
    int i1 = 0;
    if (i1 >= 2)
    {
      paramArrayOfByte2[(paramInt2 + 0)] = ((byte)(i >> 8));
      paramArrayOfByte2[(paramInt2 + 1)] = ((byte)i);
      paramArrayOfByte2[(paramInt2 + 2)] = ((byte)(j >> 8));
      paramArrayOfByte2[(paramInt2 + 3)] = ((byte)j);
      paramArrayOfByte2[(paramInt2 + 4)] = ((byte)(k >> 8));
      paramArrayOfByte2[(paramInt2 + 5)] = ((byte)k);
      paramArrayOfByte2[(paramInt2 + 6)] = ((byte)(m >> 8));
      paramArrayOfByte2[(paramInt2 + 7)] = ((byte)m);
      return 8;
    }
    int i2 = 0;
    label186: if (i2 >= 8);
    for (int i4 = 0; ; i4++)
    {
      if (i4 >= 8)
      {
        i1++;
        break;
        int i3 = m;
        m = k;
        k = j;
        j = g(n, i);
        i = j ^ i3 ^ n + 1;
        n++;
        i2++;
        break label186;
      }
      int i5 = m;
      m = k;
      k = i ^ j ^ n + 1;
      j = g(n, i);
      i = i5;
      n++;
    }
  }

  public String getAlgorithmName()
  {
    return "SKIPJACK";
  }

  public int getBlockSize()
  {
    return 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("invalid parameter passed to SKIPJACK init - " + paramCipherParameters.getClass().getName());
    byte[] arrayOfByte = ((KeyParameter)paramCipherParameters).getKey();
    this.encrypting = paramBoolean;
    this.key0 = new int[32];
    this.key1 = new int[32];
    this.key2 = new int[32];
    this.key3 = new int[32];
    for (int i = 0; ; i++)
    {
      if (i >= 32)
        return;
      this.key0[i] = (0xFF & arrayOfByte[(i * 4 % 10)]);
      this.key1[i] = (0xFF & arrayOfByte[((1 + i * 4) % 10)]);
      this.key2[i] = (0xFF & arrayOfByte[((2 + i * 4) % 10)]);
      this.key3[i] = (0xFF & arrayOfByte[((3 + i * 4) % 10)]);
    }
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.key1 == null)
      throw new IllegalStateException("SKIPJACK engine not initialised");
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