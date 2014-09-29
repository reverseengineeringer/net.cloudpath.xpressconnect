package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class CamelliaLightEngine
  implements BlockCipher
{
  private static final int BLOCK_SIZE = 16;
  private static final int MASK8 = 255;
  private static final byte[] SBOX1 = arrayOfByte;
  private static final int[] SIGMA = { -1600231809, 1003262091, -1233459112, 1286239154, -957401297, -380665154, 1426019237, -237801700, 283453434, -563598051, -1336506174, -1276722691 };
  private boolean _keyis128;
  private boolean initialized;
  private int[] ke = new int[12];
  private int[] kw = new int[8];
  private int[] state = new int[4];
  private int[] subkey = new int[96];

  static
  {
    byte[] arrayOfByte = new byte[256];
    arrayOfByte[0] = 112;
    arrayOfByte[1] = -126;
    arrayOfByte[2] = 44;
    arrayOfByte[3] = -20;
    arrayOfByte[4] = -77;
    arrayOfByte[5] = 39;
    arrayOfByte[6] = -64;
    arrayOfByte[7] = -27;
    arrayOfByte[8] = -28;
    arrayOfByte[9] = -123;
    arrayOfByte[10] = 87;
    arrayOfByte[11] = 53;
    arrayOfByte[12] = -22;
    arrayOfByte[13] = 12;
    arrayOfByte[14] = -82;
    arrayOfByte[15] = 65;
    arrayOfByte[16] = 35;
    arrayOfByte[17] = -17;
    arrayOfByte[18] = 107;
    arrayOfByte[19] = -109;
    arrayOfByte[20] = 69;
    arrayOfByte[21] = 25;
    arrayOfByte[22] = -91;
    arrayOfByte[23] = 33;
    arrayOfByte[24] = -19;
    arrayOfByte[25] = 14;
    arrayOfByte[26] = 79;
    arrayOfByte[27] = 78;
    arrayOfByte[28] = 29;
    arrayOfByte[29] = 101;
    arrayOfByte[30] = -110;
    arrayOfByte[31] = -67;
    arrayOfByte[32] = -122;
    arrayOfByte[33] = -72;
    arrayOfByte[34] = -81;
    arrayOfByte[35] = -113;
    arrayOfByte[36] = 124;
    arrayOfByte[37] = -21;
    arrayOfByte[38] = 31;
    arrayOfByte[39] = -50;
    arrayOfByte[40] = 62;
    arrayOfByte[41] = 48;
    arrayOfByte[42] = -36;
    arrayOfByte[43] = 95;
    arrayOfByte[44] = 94;
    arrayOfByte[45] = -59;
    arrayOfByte[46] = 11;
    arrayOfByte[47] = 26;
    arrayOfByte[48] = -90;
    arrayOfByte[49] = -31;
    arrayOfByte[50] = 57;
    arrayOfByte[51] = -54;
    arrayOfByte[52] = -43;
    arrayOfByte[53] = 71;
    arrayOfByte[54] = 93;
    arrayOfByte[55] = 61;
    arrayOfByte[56] = -39;
    arrayOfByte[57] = 1;
    arrayOfByte[58] = 90;
    arrayOfByte[59] = -42;
    arrayOfByte[60] = 81;
    arrayOfByte[61] = 86;
    arrayOfByte[62] = 108;
    arrayOfByte[63] = 77;
    arrayOfByte[64] = -117;
    arrayOfByte[65] = 13;
    arrayOfByte[66] = -102;
    arrayOfByte[67] = 102;
    arrayOfByte[68] = -5;
    arrayOfByte[69] = -52;
    arrayOfByte[70] = -80;
    arrayOfByte[71] = 45;
    arrayOfByte[72] = 116;
    arrayOfByte[73] = 18;
    arrayOfByte[74] = 43;
    arrayOfByte[75] = 32;
    arrayOfByte[76] = -16;
    arrayOfByte[77] = -79;
    arrayOfByte[78] = -124;
    arrayOfByte[79] = -103;
    arrayOfByte[80] = -33;
    arrayOfByte[81] = 76;
    arrayOfByte[82] = -53;
    arrayOfByte[83] = -62;
    arrayOfByte[84] = 52;
    arrayOfByte[85] = 126;
    arrayOfByte[86] = 118;
    arrayOfByte[87] = 5;
    arrayOfByte[88] = 109;
    arrayOfByte[89] = -73;
    arrayOfByte[90] = -87;
    arrayOfByte[91] = 49;
    arrayOfByte[92] = -47;
    arrayOfByte[93] = 23;
    arrayOfByte[94] = 4;
    arrayOfByte[95] = -41;
    arrayOfByte[96] = 20;
    arrayOfByte[97] = 88;
    arrayOfByte[98] = 58;
    arrayOfByte[99] = 97;
    arrayOfByte[100] = -34;
    arrayOfByte[101] = 27;
    arrayOfByte[102] = 17;
    arrayOfByte[103] = 28;
    arrayOfByte[104] = 50;
    arrayOfByte[105] = 15;
    arrayOfByte[106] = -100;
    arrayOfByte[107] = 22;
    arrayOfByte[108] = 83;
    arrayOfByte[109] = 24;
    arrayOfByte[110] = -14;
    arrayOfByte[111] = 34;
    arrayOfByte[112] = -2;
    arrayOfByte[113] = 68;
    arrayOfByte[114] = -49;
    arrayOfByte[115] = -78;
    arrayOfByte[116] = -61;
    arrayOfByte[117] = -75;
    arrayOfByte[118] = 122;
    arrayOfByte[119] = -111;
    arrayOfByte[120] = 36;
    arrayOfByte[121] = 8;
    arrayOfByte[122] = -24;
    arrayOfByte[123] = -88;
    arrayOfByte[124] = 96;
    arrayOfByte[125] = -4;
    arrayOfByte[126] = 105;
    arrayOfByte[127] = 80;
    arrayOfByte[''] = -86;
    arrayOfByte[''] = -48;
    arrayOfByte[''] = -96;
    arrayOfByte[''] = 125;
    arrayOfByte[''] = -95;
    arrayOfByte[''] = -119;
    arrayOfByte[''] = 98;
    arrayOfByte[''] = -105;
    arrayOfByte[''] = 84;
    arrayOfByte[''] = 91;
    arrayOfByte[''] = 30;
    arrayOfByte[''] = -107;
    arrayOfByte[''] = -32;
    arrayOfByte[''] = -1;
    arrayOfByte[''] = 100;
    arrayOfByte[''] = -46;
    arrayOfByte[''] = 16;
    arrayOfByte[''] = -60;
    arrayOfByte[''] = 72;
    arrayOfByte[''] = -93;
    arrayOfByte[''] = -9;
    arrayOfByte[''] = 117;
    arrayOfByte[''] = -37;
    arrayOfByte[''] = -118;
    arrayOfByte[''] = 3;
    arrayOfByte[''] = -26;
    arrayOfByte[''] = -38;
    arrayOfByte[''] = 9;
    arrayOfByte[''] = 63;
    arrayOfByte[''] = -35;
    arrayOfByte[''] = -108;
    arrayOfByte[' '] = -121;
    arrayOfByte['¡'] = 92;
    arrayOfByte['¢'] = -125;
    arrayOfByte['£'] = 2;
    arrayOfByte['¤'] = -51;
    arrayOfByte['¥'] = 74;
    arrayOfByte['¦'] = -112;
    arrayOfByte['§'] = 51;
    arrayOfByte['¨'] = 115;
    arrayOfByte['©'] = 103;
    arrayOfByte['ª'] = -10;
    arrayOfByte['«'] = -13;
    arrayOfByte['¬'] = -99;
    arrayOfByte['­'] = 127;
    arrayOfByte['®'] = -65;
    arrayOfByte['¯'] = -30;
    arrayOfByte['°'] = 82;
    arrayOfByte['±'] = -101;
    arrayOfByte['²'] = -40;
    arrayOfByte['³'] = 38;
    arrayOfByte['´'] = -56;
    arrayOfByte['µ'] = 55;
    arrayOfByte['¶'] = -58;
    arrayOfByte['·'] = 59;
    arrayOfByte['¸'] = -127;
    arrayOfByte['¹'] = -106;
    arrayOfByte['º'] = 111;
    arrayOfByte['»'] = 75;
    arrayOfByte['¼'] = 19;
    arrayOfByte['½'] = -66;
    arrayOfByte['¾'] = 99;
    arrayOfByte['¿'] = 46;
    arrayOfByte['À'] = -23;
    arrayOfByte['Á'] = 121;
    arrayOfByte['Â'] = -89;
    arrayOfByte['Ã'] = -116;
    arrayOfByte['Ä'] = -97;
    arrayOfByte['Å'] = 110;
    arrayOfByte['Æ'] = -68;
    arrayOfByte['Ç'] = -114;
    arrayOfByte['È'] = 41;
    arrayOfByte['É'] = -11;
    arrayOfByte['Ê'] = -7;
    arrayOfByte['Ë'] = -74;
    arrayOfByte['Ì'] = 47;
    arrayOfByte['Í'] = -3;
    arrayOfByte['Î'] = -76;
    arrayOfByte['Ï'] = 89;
    arrayOfByte['Ð'] = 120;
    arrayOfByte['Ñ'] = -104;
    arrayOfByte['Ò'] = 6;
    arrayOfByte['Ó'] = 106;
    arrayOfByte['Ô'] = -25;
    arrayOfByte['Õ'] = 70;
    arrayOfByte['Ö'] = 113;
    arrayOfByte['×'] = -70;
    arrayOfByte['Ø'] = -44;
    arrayOfByte['Ù'] = 37;
    arrayOfByte['Ú'] = -85;
    arrayOfByte['Û'] = 66;
    arrayOfByte['Ü'] = -120;
    arrayOfByte['Ý'] = -94;
    arrayOfByte['Þ'] = -115;
    arrayOfByte['ß'] = -6;
    arrayOfByte['à'] = 114;
    arrayOfByte['á'] = 7;
    arrayOfByte['â'] = -71;
    arrayOfByte['ã'] = 85;
    arrayOfByte['ä'] = -8;
    arrayOfByte['å'] = -18;
    arrayOfByte['æ'] = -84;
    arrayOfByte['ç'] = 10;
    arrayOfByte['è'] = 54;
    arrayOfByte['é'] = 73;
    arrayOfByte['ê'] = 42;
    arrayOfByte['ë'] = 104;
    arrayOfByte['ì'] = 60;
    arrayOfByte['í'] = 56;
    arrayOfByte['î'] = -15;
    arrayOfByte['ï'] = -92;
    arrayOfByte['ð'] = 64;
    arrayOfByte['ñ'] = 40;
    arrayOfByte['ò'] = -45;
    arrayOfByte['ó'] = 123;
    arrayOfByte['ô'] = -69;
    arrayOfByte['õ'] = -55;
    arrayOfByte['ö'] = 67;
    arrayOfByte['÷'] = -63;
    arrayOfByte['ø'] = 21;
    arrayOfByte['ù'] = -29;
    arrayOfByte['ú'] = -83;
    arrayOfByte['û'] = -12;
    arrayOfByte['ü'] = 119;
    arrayOfByte['ý'] = -57;
    arrayOfByte['þ'] = -128;
    arrayOfByte['ÿ'] = -98;
  }

  private int bytes2int(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j >= 4)
        return i;
      i = (i << 8) + (0xFF & paramArrayOfByte[(j + paramInt)]);
    }
  }

  private void camelliaF2(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    int i = paramArrayOfInt1[0] ^ paramArrayOfInt2[(paramInt + 0)];
    int j = sbox4(i & 0xFF) | sbox3(0xFF & i >>> 8) << 8 | sbox2(0xFF & i >>> 16) << 16 | (0xFF & SBOX1[(0xFF & i >>> 24)]) << 24;
    int k = paramArrayOfInt1[1] ^ paramArrayOfInt2[(paramInt + 1)];
    int m = leftRotate(0xFF & SBOX1[(k & 0xFF)] | sbox4(0xFF & k >>> 8) << 8 | sbox3(0xFF & k >>> 16) << 16 | sbox2(0xFF & k >>> 24) << 24, 8);
    int n = j ^ m;
    int i1 = n ^ leftRotate(m, 8);
    int i2 = i1 ^ rightRotate(n, 8);
    paramArrayOfInt1[2] ^= i2 ^ leftRotate(i1, 16);
    paramArrayOfInt1[3] ^= leftRotate(i2, 8);
    int i3 = paramArrayOfInt1[2] ^ paramArrayOfInt2[(paramInt + 2)];
    int i4 = sbox4(i3 & 0xFF) | sbox3(0xFF & i3 >>> 8) << 8 | sbox2(0xFF & i3 >>> 16) << 16 | (0xFF & SBOX1[(0xFF & i3 >>> 24)]) << 24;
    int i5 = paramArrayOfInt1[3] ^ paramArrayOfInt2[(paramInt + 3)];
    int i6 = leftRotate(0xFF & SBOX1[(i5 & 0xFF)] | sbox4(0xFF & i5 >>> 8) << 8 | sbox3(0xFF & i5 >>> 16) << 16 | sbox2(0xFF & i5 >>> 24) << 24, 8);
    int i7 = i4 ^ i6;
    int i8 = i7 ^ leftRotate(i6, 8);
    int i9 = i8 ^ rightRotate(i7, 8);
    paramArrayOfInt1[0] ^= i9 ^ leftRotate(i8, 16);
    paramArrayOfInt1[1] ^= leftRotate(i9, 8);
  }

  private void camelliaFLs(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    paramArrayOfInt1[1] ^= leftRotate(paramArrayOfInt1[0] & paramArrayOfInt2[(paramInt + 0)], 1);
    paramArrayOfInt1[0] ^= (paramArrayOfInt2[(paramInt + 1)] | paramArrayOfInt1[1]);
    paramArrayOfInt1[2] ^= (paramArrayOfInt2[(paramInt + 3)] | paramArrayOfInt1[3]);
    paramArrayOfInt1[3] ^= leftRotate(paramArrayOfInt2[(paramInt + 2)] & paramArrayOfInt1[2], 1);
  }

  private static void decroldq(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3)
  {
    paramArrayOfInt2[(paramInt3 + 2)] = (paramArrayOfInt1[(paramInt2 + 0)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 1)] >>> 32 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 3)] = (paramArrayOfInt1[(paramInt2 + 1)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 2)] >>> 32 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 0)] = (paramArrayOfInt1[(paramInt2 + 2)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 3)] >>> 32 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 1)] = (paramArrayOfInt1[(paramInt2 + 3)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 0)] >>> 32 - paramInt1);
    paramArrayOfInt1[(paramInt2 + 0)] = paramArrayOfInt2[(paramInt3 + 2)];
    paramArrayOfInt1[(paramInt2 + 1)] = paramArrayOfInt2[(paramInt3 + 3)];
    paramArrayOfInt1[(paramInt2 + 2)] = paramArrayOfInt2[(paramInt3 + 0)];
    paramArrayOfInt1[(paramInt2 + 3)] = paramArrayOfInt2[(paramInt3 + 1)];
  }

  private static void decroldqo32(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3)
  {
    paramArrayOfInt2[(paramInt3 + 2)] = (paramArrayOfInt1[(paramInt2 + 1)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 2)] >>> 64 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 3)] = (paramArrayOfInt1[(paramInt2 + 2)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 3)] >>> 64 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 0)] = (paramArrayOfInt1[(paramInt2 + 3)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 0)] >>> 64 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 1)] = (paramArrayOfInt1[(paramInt2 + 0)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 1)] >>> 64 - paramInt1);
    paramArrayOfInt1[(paramInt2 + 0)] = paramArrayOfInt2[(paramInt3 + 2)];
    paramArrayOfInt1[(paramInt2 + 1)] = paramArrayOfInt2[(paramInt3 + 3)];
    paramArrayOfInt1[(paramInt2 + 2)] = paramArrayOfInt2[(paramInt3 + 0)];
    paramArrayOfInt1[(paramInt2 + 3)] = paramArrayOfInt2[(paramInt3 + 1)];
  }

  private void int2bytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 4)
        return;
      paramArrayOfByte[(paramInt2 + (3 - i))] = ((byte)paramInt1);
      paramInt1 >>>= 8;
    }
  }

  private byte lRot8(byte paramByte, int paramInt)
  {
    return (byte)(paramByte << paramInt | (paramByte & 0xFF) >>> 8 - paramInt);
  }

  private static int leftRotate(int paramInt1, int paramInt2)
  {
    return (paramInt1 << paramInt2) + (paramInt1 >>> 32 - paramInt2);
  }

  private int processBlock128(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 4)
      {
        camelliaF2(this.state, this.subkey, 0);
        camelliaF2(this.state, this.subkey, 4);
        camelliaF2(this.state, this.subkey, 8);
        camelliaFLs(this.state, this.ke, 0);
        camelliaF2(this.state, this.subkey, 12);
        camelliaF2(this.state, this.subkey, 16);
        camelliaF2(this.state, this.subkey, 20);
        camelliaFLs(this.state, this.ke, 4);
        camelliaF2(this.state, this.subkey, 24);
        camelliaF2(this.state, this.subkey, 28);
        camelliaF2(this.state, this.subkey, 32);
        int[] arrayOfInt2 = this.state;
        arrayOfInt2[2] ^= this.kw[4];
        int[] arrayOfInt3 = this.state;
        arrayOfInt3[3] ^= this.kw[5];
        int[] arrayOfInt4 = this.state;
        arrayOfInt4[0] ^= this.kw[6];
        int[] arrayOfInt5 = this.state;
        arrayOfInt5[1] ^= this.kw[7];
        int2bytes(this.state[2], paramArrayOfByte2, paramInt2);
        int2bytes(this.state[3], paramArrayOfByte2, paramInt2 + 4);
        int2bytes(this.state[0], paramArrayOfByte2, paramInt2 + 8);
        int2bytes(this.state[1], paramArrayOfByte2, paramInt2 + 12);
        return 16;
      }
      this.state[i] = bytes2int(paramArrayOfByte1, paramInt1 + i * 4);
      int[] arrayOfInt1 = this.state;
      arrayOfInt1[i] ^= this.kw[i];
    }
  }

  private int processBlock192or256(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    for (int i = 0; ; i++)
    {
      if (i >= 4)
      {
        camelliaF2(this.state, this.subkey, 0);
        camelliaF2(this.state, this.subkey, 4);
        camelliaF2(this.state, this.subkey, 8);
        camelliaFLs(this.state, this.ke, 0);
        camelliaF2(this.state, this.subkey, 12);
        camelliaF2(this.state, this.subkey, 16);
        camelliaF2(this.state, this.subkey, 20);
        camelliaFLs(this.state, this.ke, 4);
        camelliaF2(this.state, this.subkey, 24);
        camelliaF2(this.state, this.subkey, 28);
        camelliaF2(this.state, this.subkey, 32);
        camelliaFLs(this.state, this.ke, 8);
        camelliaF2(this.state, this.subkey, 36);
        camelliaF2(this.state, this.subkey, 40);
        camelliaF2(this.state, this.subkey, 44);
        int[] arrayOfInt2 = this.state;
        arrayOfInt2[2] ^= this.kw[4];
        int[] arrayOfInt3 = this.state;
        arrayOfInt3[3] ^= this.kw[5];
        int[] arrayOfInt4 = this.state;
        arrayOfInt4[0] ^= this.kw[6];
        int[] arrayOfInt5 = this.state;
        arrayOfInt5[1] ^= this.kw[7];
        int2bytes(this.state[2], paramArrayOfByte2, paramInt2);
        int2bytes(this.state[3], paramArrayOfByte2, paramInt2 + 4);
        int2bytes(this.state[0], paramArrayOfByte2, paramInt2 + 8);
        int2bytes(this.state[1], paramArrayOfByte2, paramInt2 + 12);
        return 16;
      }
      this.state[i] = bytes2int(paramArrayOfByte1, paramInt1 + i * 4);
      int[] arrayOfInt1 = this.state;
      arrayOfInt1[i] ^= this.kw[i];
    }
  }

  private static int rightRotate(int paramInt1, int paramInt2)
  {
    return (paramInt1 >>> paramInt2) + (paramInt1 << 32 - paramInt2);
  }

  private static void roldq(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3)
  {
    paramArrayOfInt2[(paramInt3 + 0)] = (paramArrayOfInt1[(paramInt2 + 0)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 1)] >>> 32 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 1)] = (paramArrayOfInt1[(paramInt2 + 1)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 2)] >>> 32 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 2)] = (paramArrayOfInt1[(paramInt2 + 2)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 3)] >>> 32 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 3)] = (paramArrayOfInt1[(paramInt2 + 3)] << paramInt1 | paramArrayOfInt1[(paramInt2 + 0)] >>> 32 - paramInt1);
    paramArrayOfInt1[(paramInt2 + 0)] = paramArrayOfInt2[(paramInt3 + 0)];
    paramArrayOfInt1[(paramInt2 + 1)] = paramArrayOfInt2[(paramInt3 + 1)];
    paramArrayOfInt1[(paramInt2 + 2)] = paramArrayOfInt2[(paramInt3 + 2)];
    paramArrayOfInt1[(paramInt2 + 3)] = paramArrayOfInt2[(paramInt3 + 3)];
  }

  private static void roldqo32(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3)
  {
    paramArrayOfInt2[(paramInt3 + 0)] = (paramArrayOfInt1[(paramInt2 + 1)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 2)] >>> 64 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 1)] = (paramArrayOfInt1[(paramInt2 + 2)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 3)] >>> 64 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 2)] = (paramArrayOfInt1[(paramInt2 + 3)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 0)] >>> 64 - paramInt1);
    paramArrayOfInt2[(paramInt3 + 3)] = (paramArrayOfInt1[(paramInt2 + 0)] << paramInt1 - 32 | paramArrayOfInt1[(paramInt2 + 1)] >>> 64 - paramInt1);
    paramArrayOfInt1[(paramInt2 + 0)] = paramArrayOfInt2[(paramInt3 + 0)];
    paramArrayOfInt1[(paramInt2 + 1)] = paramArrayOfInt2[(paramInt3 + 1)];
    paramArrayOfInt1[(paramInt2 + 2)] = paramArrayOfInt2[(paramInt3 + 2)];
    paramArrayOfInt1[(paramInt2 + 3)] = paramArrayOfInt2[(paramInt3 + 3)];
  }

  private int sbox2(int paramInt)
  {
    return 0xFF & lRot8(SBOX1[paramInt], 1);
  }

  private int sbox3(int paramInt)
  {
    return 0xFF & lRot8(SBOX1[paramInt], 7);
  }

  private int sbox4(int paramInt)
  {
    return 0xFF & SBOX1[(0xFF & lRot8((byte)paramInt, 1))];
  }

  private void setKey(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    int[] arrayOfInt1 = new int[8];
    int[] arrayOfInt2 = new int[4];
    int[] arrayOfInt3 = new int[4];
    int[] arrayOfInt4 = new int[4];
    int i;
    switch (paramArrayOfByte.length)
    {
    default:
      throw new IllegalArgumentException("key sizes are only 16/24/32 bytes.");
    case 16:
      this._keyis128 = true;
      arrayOfInt1[0] = bytes2int(paramArrayOfByte, 0);
      arrayOfInt1[1] = bytes2int(paramArrayOfByte, 4);
      arrayOfInt1[2] = bytes2int(paramArrayOfByte, 8);
      arrayOfInt1[3] = bytes2int(paramArrayOfByte, 12);
      arrayOfInt1[7] = 0;
      arrayOfInt1[6] = 0;
      arrayOfInt1[5] = 0;
      arrayOfInt1[4] = 0;
      i = 0;
      label130: if (i >= 4)
        camelliaF2(arrayOfInt2, SIGMA, 0);
      break;
    case 24:
    case 32:
    }
    for (int j = 0; ; j++)
    {
      if (j >= 4)
      {
        camelliaF2(arrayOfInt2, SIGMA, 4);
        if (!this._keyis128)
          break label942;
        if (!paramBoolean)
          break label664;
        this.kw[0] = arrayOfInt1[0];
        this.kw[1] = arrayOfInt1[1];
        this.kw[2] = arrayOfInt1[2];
        this.kw[3] = arrayOfInt1[3];
        roldq(15, arrayOfInt1, 0, this.subkey, 4);
        roldq(30, arrayOfInt1, 0, this.subkey, 12);
        roldq(15, arrayOfInt1, 0, arrayOfInt4, 0);
        this.subkey[18] = arrayOfInt4[2];
        this.subkey[19] = arrayOfInt4[3];
        roldq(17, arrayOfInt1, 0, this.ke, 4);
        roldq(17, arrayOfInt1, 0, this.subkey, 24);
        roldq(17, arrayOfInt1, 0, this.subkey, 32);
        this.subkey[0] = arrayOfInt2[0];
        this.subkey[1] = arrayOfInt2[1];
        this.subkey[2] = arrayOfInt2[2];
        this.subkey[3] = arrayOfInt2[3];
        roldq(15, arrayOfInt2, 0, this.subkey, 8);
        roldq(15, arrayOfInt2, 0, this.ke, 0);
        roldq(15, arrayOfInt2, 0, arrayOfInt4, 0);
        this.subkey[16] = arrayOfInt4[0];
        this.subkey[17] = arrayOfInt4[1];
        roldq(15, arrayOfInt2, 0, this.subkey, 20);
        roldqo32(34, arrayOfInt2, 0, this.subkey, 28);
        roldq(17, arrayOfInt2, 0, this.kw, 4);
        return;
        arrayOfInt1[0] = bytes2int(paramArrayOfByte, 0);
        arrayOfInt1[1] = bytes2int(paramArrayOfByte, 4);
        arrayOfInt1[2] = bytes2int(paramArrayOfByte, 8);
        arrayOfInt1[3] = bytes2int(paramArrayOfByte, 12);
        arrayOfInt1[4] = bytes2int(paramArrayOfByte, 16);
        arrayOfInt1[5] = bytes2int(paramArrayOfByte, 20);
        arrayOfInt1[6] = (0xFFFFFFFF ^ arrayOfInt1[4]);
        arrayOfInt1[7] = (0xFFFFFFFF ^ arrayOfInt1[5]);
        this._keyis128 = false;
        break;
        arrayOfInt1[0] = bytes2int(paramArrayOfByte, 0);
        arrayOfInt1[1] = bytes2int(paramArrayOfByte, 4);
        arrayOfInt1[2] = bytes2int(paramArrayOfByte, 8);
        arrayOfInt1[3] = bytes2int(paramArrayOfByte, 12);
        arrayOfInt1[4] = bytes2int(paramArrayOfByte, 16);
        arrayOfInt1[5] = bytes2int(paramArrayOfByte, 20);
        arrayOfInt1[6] = bytes2int(paramArrayOfByte, 24);
        arrayOfInt1[7] = bytes2int(paramArrayOfByte, 28);
        this._keyis128 = false;
        break;
        arrayOfInt1[i] ^= arrayOfInt1[(i + 4)];
        i++;
        break label130;
      }
      arrayOfInt2[j] ^= arrayOfInt1[j];
    }
    label664: this.kw[4] = arrayOfInt1[0];
    this.kw[5] = arrayOfInt1[1];
    this.kw[6] = arrayOfInt1[2];
    this.kw[7] = arrayOfInt1[3];
    decroldq(15, arrayOfInt1, 0, this.subkey, 28);
    decroldq(30, arrayOfInt1, 0, this.subkey, 20);
    decroldq(15, arrayOfInt1, 0, arrayOfInt4, 0);
    this.subkey[16] = arrayOfInt4[0];
    this.subkey[17] = arrayOfInt4[1];
    decroldq(17, arrayOfInt1, 0, this.ke, 0);
    decroldq(17, arrayOfInt1, 0, this.subkey, 8);
    decroldq(17, arrayOfInt1, 0, this.subkey, 0);
    this.subkey[34] = arrayOfInt2[0];
    this.subkey[35] = arrayOfInt2[1];
    this.subkey[32] = arrayOfInt2[2];
    this.subkey[33] = arrayOfInt2[3];
    decroldq(15, arrayOfInt2, 0, this.subkey, 24);
    decroldq(15, arrayOfInt2, 0, this.ke, 4);
    decroldq(15, arrayOfInt2, 0, arrayOfInt4, 0);
    this.subkey[18] = arrayOfInt4[2];
    this.subkey[19] = arrayOfInt4[3];
    decroldq(15, arrayOfInt2, 0, this.subkey, 12);
    decroldqo32(34, arrayOfInt2, 0, this.subkey, 4);
    roldq(17, arrayOfInt2, 0, this.kw, 0);
    return;
    label942: for (int k = 0; ; k++)
    {
      if (k >= 4)
      {
        camelliaF2(arrayOfInt3, SIGMA, 8);
        if (!paramBoolean)
          break;
        this.kw[0] = arrayOfInt1[0];
        this.kw[1] = arrayOfInt1[1];
        this.kw[2] = arrayOfInt1[2];
        this.kw[3] = arrayOfInt1[3];
        roldqo32(45, arrayOfInt1, 0, this.subkey, 16);
        roldq(15, arrayOfInt1, 0, this.ke, 4);
        roldq(17, arrayOfInt1, 0, this.subkey, 32);
        roldqo32(34, arrayOfInt1, 0, this.subkey, 44);
        roldq(15, arrayOfInt1, 4, this.subkey, 4);
        roldq(15, arrayOfInt1, 4, this.ke, 0);
        roldq(30, arrayOfInt1, 4, this.subkey, 24);
        roldqo32(34, arrayOfInt1, 4, this.subkey, 36);
        roldq(15, arrayOfInt2, 0, this.subkey, 8);
        roldq(30, arrayOfInt2, 0, this.subkey, 20);
        this.ke[8] = arrayOfInt2[1];
        this.ke[9] = arrayOfInt2[2];
        this.ke[10] = arrayOfInt2[3];
        this.ke[11] = arrayOfInt2[0];
        roldqo32(49, arrayOfInt2, 0, this.subkey, 40);
        this.subkey[0] = arrayOfInt3[0];
        this.subkey[1] = arrayOfInt3[1];
        this.subkey[2] = arrayOfInt3[2];
        this.subkey[3] = arrayOfInt3[3];
        roldq(30, arrayOfInt3, 0, this.subkey, 12);
        roldq(30, arrayOfInt3, 0, this.subkey, 28);
        roldqo32(51, arrayOfInt3, 0, this.kw, 4);
        return;
      }
      arrayOfInt2[k] ^= arrayOfInt1[(k + 4)];
    }
    this.kw[4] = arrayOfInt1[0];
    this.kw[5] = arrayOfInt1[1];
    this.kw[6] = arrayOfInt1[2];
    this.kw[7] = arrayOfInt1[3];
    decroldqo32(45, arrayOfInt1, 0, this.subkey, 28);
    decroldq(15, arrayOfInt1, 0, this.ke, 4);
    decroldq(17, arrayOfInt1, 0, this.subkey, 12);
    decroldqo32(34, arrayOfInt1, 0, this.subkey, 0);
    decroldq(15, arrayOfInt1, 4, this.subkey, 40);
    decroldq(15, arrayOfInt1, 4, this.ke, 8);
    decroldq(30, arrayOfInt1, 4, this.subkey, 20);
    decroldqo32(34, arrayOfInt1, 4, this.subkey, 8);
    decroldq(15, arrayOfInt2, 0, this.subkey, 36);
    decroldq(30, arrayOfInt2, 0, this.subkey, 24);
    this.ke[2] = arrayOfInt2[1];
    this.ke[3] = arrayOfInt2[2];
    this.ke[0] = arrayOfInt2[3];
    this.ke[1] = arrayOfInt2[0];
    decroldqo32(49, arrayOfInt2, 0, this.subkey, 4);
    this.subkey[46] = arrayOfInt3[0];
    this.subkey[47] = arrayOfInt3[1];
    this.subkey[44] = arrayOfInt3[2];
    this.subkey[45] = arrayOfInt3[3];
    decroldq(30, arrayOfInt3, 0, this.subkey, 32);
    decroldq(30, arrayOfInt3, 0, this.subkey, 16);
    roldqo32(51, arrayOfInt3, 0, this.kw, 0);
  }

  public String getAlgorithmName()
  {
    return "Camellia";
  }

  public int getBlockSize()
  {
    return 16;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("only simple KeyParameter expected.");
    setKey(paramBoolean, ((KeyParameter)paramCipherParameters).getKey());
    this.initialized = true;
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws IllegalStateException
  {
    if (!this.initialized)
      throw new IllegalStateException("Camellia is not initialized");
    if (paramInt1 + 16 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 16 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this._keyis128)
      return processBlock128(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return processBlock192or256(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
  }

  public void reset()
  {
  }
}