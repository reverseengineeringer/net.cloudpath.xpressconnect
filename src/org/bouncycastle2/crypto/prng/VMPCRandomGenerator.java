package org.bouncycastle2.crypto.prng;

public class VMPCRandomGenerator
  implements RandomGenerator
{
  private byte[] P;
  private byte n = 0;
  private byte s;

  public VMPCRandomGenerator()
  {
    byte[] arrayOfByte = new byte[256];
    arrayOfByte[0] = -69;
    arrayOfByte[1] = 44;
    arrayOfByte[2] = 98;
    arrayOfByte[3] = 127;
    arrayOfByte[4] = -75;
    arrayOfByte[5] = -86;
    arrayOfByte[6] = -44;
    arrayOfByte[7] = 13;
    arrayOfByte[8] = -127;
    arrayOfByte[9] = -2;
    arrayOfByte[10] = -78;
    arrayOfByte[11] = -126;
    arrayOfByte[12] = -53;
    arrayOfByte[13] = -96;
    arrayOfByte[14] = -95;
    arrayOfByte[15] = 8;
    arrayOfByte[16] = 24;
    arrayOfByte[17] = 113;
    arrayOfByte[18] = 86;
    arrayOfByte[19] = -24;
    arrayOfByte[20] = 73;
    arrayOfByte[21] = 2;
    arrayOfByte[22] = 16;
    arrayOfByte[23] = -60;
    arrayOfByte[24] = -34;
    arrayOfByte[25] = 53;
    arrayOfByte[26] = -91;
    arrayOfByte[27] = -20;
    arrayOfByte[28] = -128;
    arrayOfByte[29] = 18;
    arrayOfByte[30] = -72;
    arrayOfByte[31] = 105;
    arrayOfByte[32] = -38;
    arrayOfByte[33] = 47;
    arrayOfByte[34] = 117;
    arrayOfByte[35] = -52;
    arrayOfByte[36] = -94;
    arrayOfByte[37] = 9;
    arrayOfByte[38] = 54;
    arrayOfByte[39] = 3;
    arrayOfByte[40] = 97;
    arrayOfByte[41] = 45;
    arrayOfByte[42] = -3;
    arrayOfByte[43] = -32;
    arrayOfByte[44] = -35;
    arrayOfByte[45] = 5;
    arrayOfByte[46] = 67;
    arrayOfByte[47] = -112;
    arrayOfByte[48] = -83;
    arrayOfByte[49] = -56;
    arrayOfByte[50] = -31;
    arrayOfByte[51] = -81;
    arrayOfByte[52] = 87;
    arrayOfByte[53] = -101;
    arrayOfByte[54] = 76;
    arrayOfByte[55] = -40;
    arrayOfByte[56] = 81;
    arrayOfByte[57] = -82;
    arrayOfByte[58] = 80;
    arrayOfByte[59] = -123;
    arrayOfByte[60] = 60;
    arrayOfByte[61] = 10;
    arrayOfByte[62] = -28;
    arrayOfByte[63] = -13;
    arrayOfByte[64] = -100;
    arrayOfByte[65] = 38;
    arrayOfByte[66] = 35;
    arrayOfByte[67] = 83;
    arrayOfByte[68] = -55;
    arrayOfByte[69] = -125;
    arrayOfByte[70] = -105;
    arrayOfByte[71] = 70;
    arrayOfByte[72] = -79;
    arrayOfByte[73] = -103;
    arrayOfByte[74] = 100;
    arrayOfByte[75] = 49;
    arrayOfByte[76] = 119;
    arrayOfByte[77] = -43;
    arrayOfByte[78] = 29;
    arrayOfByte[79] = -42;
    arrayOfByte[80] = 120;
    arrayOfByte[81] = -67;
    arrayOfByte[82] = 94;
    arrayOfByte[83] = -80;
    arrayOfByte[84] = -118;
    arrayOfByte[85] = 34;
    arrayOfByte[86] = 56;
    arrayOfByte[87] = -8;
    arrayOfByte[88] = 104;
    arrayOfByte[89] = 43;
    arrayOfByte[90] = 42;
    arrayOfByte[91] = -59;
    arrayOfByte[92] = -45;
    arrayOfByte[93] = -9;
    arrayOfByte[94] = -68;
    arrayOfByte[95] = 111;
    arrayOfByte[96] = -33;
    arrayOfByte[97] = 4;
    arrayOfByte[98] = -27;
    arrayOfByte[99] = -107;
    arrayOfByte[100] = 62;
    arrayOfByte[101] = 37;
    arrayOfByte[102] = -122;
    arrayOfByte[103] = -90;
    arrayOfByte[104] = 11;
    arrayOfByte[105] = -113;
    arrayOfByte[106] = -15;
    arrayOfByte[107] = 36;
    arrayOfByte[108] = 14;
    arrayOfByte[109] = -41;
    arrayOfByte[110] = 64;
    arrayOfByte[111] = -77;
    arrayOfByte[112] = -49;
    arrayOfByte[113] = 126;
    arrayOfByte[114] = 6;
    arrayOfByte[115] = 21;
    arrayOfByte[116] = -102;
    arrayOfByte[117] = 77;
    arrayOfByte[118] = 28;
    arrayOfByte[119] = -93;
    arrayOfByte[120] = -37;
    arrayOfByte[121] = 50;
    arrayOfByte[122] = -110;
    arrayOfByte[123] = 88;
    arrayOfByte[124] = 17;
    arrayOfByte[125] = 39;
    arrayOfByte[126] = -12;
    arrayOfByte[127] = 89;
    arrayOfByte[''] = -48;
    arrayOfByte[''] = 78;
    arrayOfByte[''] = 106;
    arrayOfByte[''] = 23;
    arrayOfByte[''] = 91;
    arrayOfByte[''] = -84;
    arrayOfByte[''] = -1;
    arrayOfByte[''] = 7;
    arrayOfByte[''] = -64;
    arrayOfByte[''] = 101;
    arrayOfByte[''] = 121;
    arrayOfByte[''] = -4;
    arrayOfByte[''] = -57;
    arrayOfByte[''] = -51;
    arrayOfByte[''] = 118;
    arrayOfByte[''] = 66;
    arrayOfByte[''] = 93;
    arrayOfByte[''] = -25;
    arrayOfByte[''] = 58;
    arrayOfByte[''] = 52;
    arrayOfByte[''] = 122;
    arrayOfByte[''] = 48;
    arrayOfByte[''] = 40;
    arrayOfByte[''] = 15;
    arrayOfByte[''] = 115;
    arrayOfByte[''] = 1;
    arrayOfByte[''] = -7;
    arrayOfByte[''] = -47;
    arrayOfByte[''] = -46;
    arrayOfByte[''] = 25;
    arrayOfByte[''] = -23;
    arrayOfByte[''] = -111;
    arrayOfByte[' '] = -71;
    arrayOfByte['¡'] = 90;
    arrayOfByte['¢'] = -19;
    arrayOfByte['£'] = 65;
    arrayOfByte['¤'] = 109;
    arrayOfByte['¥'] = -76;
    arrayOfByte['¦'] = -61;
    arrayOfByte['§'] = -98;
    arrayOfByte['¨'] = -65;
    arrayOfByte['©'] = 99;
    arrayOfByte['ª'] = -6;
    arrayOfByte['«'] = 31;
    arrayOfByte['¬'] = 51;
    arrayOfByte['­'] = 96;
    arrayOfByte['®'] = 71;
    arrayOfByte['¯'] = -119;
    arrayOfByte['°'] = -16;
    arrayOfByte['±'] = -106;
    arrayOfByte['²'] = 26;
    arrayOfByte['³'] = 95;
    arrayOfByte['´'] = -109;
    arrayOfByte['µ'] = 61;
    arrayOfByte['¶'] = 55;
    arrayOfByte['·'] = 75;
    arrayOfByte['¸'] = -39;
    arrayOfByte['¹'] = -88;
    arrayOfByte['º'] = -63;
    arrayOfByte['»'] = 27;
    arrayOfByte['¼'] = -10;
    arrayOfByte['½'] = 57;
    arrayOfByte['¾'] = -117;
    arrayOfByte['¿'] = -73;
    arrayOfByte['À'] = 12;
    arrayOfByte['Á'] = 32;
    arrayOfByte['Â'] = -50;
    arrayOfByte['Ã'] = -120;
    arrayOfByte['Ä'] = 110;
    arrayOfByte['Å'] = -74;
    arrayOfByte['Æ'] = 116;
    arrayOfByte['Ç'] = -114;
    arrayOfByte['È'] = -115;
    arrayOfByte['É'] = 22;
    arrayOfByte['Ê'] = 41;
    arrayOfByte['Ë'] = -14;
    arrayOfByte['Ì'] = -121;
    arrayOfByte['Í'] = -11;
    arrayOfByte['Î'] = -21;
    arrayOfByte['Ï'] = 112;
    arrayOfByte['Ð'] = -29;
    arrayOfByte['Ñ'] = -5;
    arrayOfByte['Ò'] = 85;
    arrayOfByte['Ó'] = -97;
    arrayOfByte['Ô'] = -58;
    arrayOfByte['Õ'] = 68;
    arrayOfByte['Ö'] = 74;
    arrayOfByte['×'] = 69;
    arrayOfByte['Ø'] = 125;
    arrayOfByte['Ù'] = -30;
    arrayOfByte['Ú'] = 107;
    arrayOfByte['Û'] = 92;
    arrayOfByte['Ü'] = 108;
    arrayOfByte['Ý'] = 102;
    arrayOfByte['Þ'] = -87;
    arrayOfByte['ß'] = -116;
    arrayOfByte['à'] = -18;
    arrayOfByte['á'] = -124;
    arrayOfByte['â'] = 19;
    arrayOfByte['ã'] = -89;
    arrayOfByte['ä'] = 30;
    arrayOfByte['å'] = -99;
    arrayOfByte['æ'] = -36;
    arrayOfByte['ç'] = 103;
    arrayOfByte['è'] = 72;
    arrayOfByte['é'] = -70;
    arrayOfByte['ê'] = 46;
    arrayOfByte['ë'] = -26;
    arrayOfByte['ì'] = -92;
    arrayOfByte['í'] = -85;
    arrayOfByte['î'] = 124;
    arrayOfByte['ï'] = -108;
    arrayOfByte['ñ'] = 33;
    arrayOfByte['ò'] = -17;
    arrayOfByte['ó'] = -22;
    arrayOfByte['ô'] = -66;
    arrayOfByte['õ'] = -54;
    arrayOfByte['ö'] = 114;
    arrayOfByte['÷'] = 79;
    arrayOfByte['ø'] = 82;
    arrayOfByte['ù'] = -104;
    arrayOfByte['ú'] = 63;
    arrayOfByte['û'] = -62;
    arrayOfByte['ü'] = 20;
    arrayOfByte['ý'] = 123;
    arrayOfByte['þ'] = 59;
    arrayOfByte['ÿ'] = 84;
    this.P = arrayOfByte;
    this.s = -66;
  }

  public void addSeedMaterial(long paramLong)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[3] = ((byte)(int)(0xFF & paramLong));
    arrayOfByte[2] = ((byte)(int)((0xFF00 & paramLong) >> 8));
    arrayOfByte[1] = ((byte)(int)((0xFF0000 & paramLong) >> 16));
    arrayOfByte[0] = ((byte)(int)((0xFF000000 & paramLong) >> 24));
    addSeedMaterial(arrayOfByte);
  }

  public void addSeedMaterial(byte[] paramArrayOfByte)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte.length)
        return;
      this.s = this.P[(0xFF & this.s + this.P[(0xFF & this.n)] + paramArrayOfByte[i])];
      int j = this.P[(0xFF & this.n)];
      this.P[(0xFF & this.n)] = this.P[(0xFF & this.s)];
      this.P[(0xFF & this.s)] = j;
      this.n = ((byte)(0xFF & 1 + this.n));
    }
  }

  public void nextBytes(byte[] paramArrayOfByte)
  {
    nextBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  // ERROR //
  public void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   4: astore 4
    //   6: aload 4
    //   8: monitorenter
    //   9: iload_2
    //   10: iload_3
    //   11: iadd
    //   12: istore 5
    //   14: iload_2
    //   15: istore 6
    //   17: iload 6
    //   19: iload 5
    //   21: if_icmpne +7 -> 28
    //   24: aload 4
    //   26: monitorexit
    //   27: return
    //   28: aload_0
    //   29: aload_0
    //   30: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   33: sipush 255
    //   36: aload_0
    //   37: getfield 21	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:s	B
    //   40: aload_0
    //   41: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   44: sipush 255
    //   47: aload_0
    //   48: getfield 17	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:n	B
    //   51: iand
    //   52: baload
    //   53: iadd
    //   54: iand
    //   55: baload
    //   56: putfield 21	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:s	B
    //   59: aload_1
    //   60: iload 6
    //   62: aload_0
    //   63: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   66: sipush 255
    //   69: iconst_1
    //   70: aload_0
    //   71: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   74: sipush 255
    //   77: aload_0
    //   78: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   81: sipush 255
    //   84: aload_0
    //   85: getfield 21	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:s	B
    //   88: iand
    //   89: baload
    //   90: iand
    //   91: baload
    //   92: iadd
    //   93: iand
    //   94: baload
    //   95: bastore
    //   96: aload_0
    //   97: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   100: sipush 255
    //   103: aload_0
    //   104: getfield 17	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:n	B
    //   107: iand
    //   108: baload
    //   109: istore 8
    //   111: aload_0
    //   112: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   115: sipush 255
    //   118: aload_0
    //   119: getfield 17	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:n	B
    //   122: iand
    //   123: aload_0
    //   124: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   127: sipush 255
    //   130: aload_0
    //   131: getfield 21	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:s	B
    //   134: iand
    //   135: baload
    //   136: bastore
    //   137: aload_0
    //   138: getfield 19	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:P	[B
    //   141: sipush 255
    //   144: aload_0
    //   145: getfield 21	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:s	B
    //   148: iand
    //   149: iload 8
    //   151: bastore
    //   152: aload_0
    //   153: sipush 255
    //   156: iconst_1
    //   157: aload_0
    //   158: getfield 17	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:n	B
    //   161: iadd
    //   162: iand
    //   163: i2b
    //   164: putfield 17	org/bouncycastle2/crypto/prng/VMPCRandomGenerator:n	B
    //   167: iinc 6 1
    //   170: goto -153 -> 17
    //   173: astore 7
    //   175: aload 4
    //   177: monitorexit
    //   178: aload 7
    //   180: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   24	27	173	finally
    //   28	167	173	finally
    //   175	178	173	finally
  }
}