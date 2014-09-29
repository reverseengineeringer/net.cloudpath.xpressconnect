package org.bouncycastle2.crypto.digests;

import org.bouncycastle2.crypto.ExtendedDigest;

public class MD2Digest
  implements ExtendedDigest
{
  private static final int DIGEST_LENGTH = 16;
  private static final byte[] S = arrayOfByte;
  private byte[] C = new byte[16];
  private int COff;
  private byte[] M = new byte[16];
  private byte[] X = new byte[48];
  private int mOff;
  private int xOff;

  static
  {
    byte[] arrayOfByte = new byte[256];
    arrayOfByte[0] = 41;
    arrayOfByte[1] = 46;
    arrayOfByte[2] = 67;
    arrayOfByte[3] = -55;
    arrayOfByte[4] = -94;
    arrayOfByte[5] = -40;
    arrayOfByte[6] = 124;
    arrayOfByte[7] = 1;
    arrayOfByte[8] = 61;
    arrayOfByte[9] = 54;
    arrayOfByte[10] = 84;
    arrayOfByte[11] = -95;
    arrayOfByte[12] = -20;
    arrayOfByte[13] = -16;
    arrayOfByte[14] = 6;
    arrayOfByte[15] = 19;
    arrayOfByte[16] = 98;
    arrayOfByte[17] = -89;
    arrayOfByte[18] = 5;
    arrayOfByte[19] = -13;
    arrayOfByte[20] = -64;
    arrayOfByte[21] = -57;
    arrayOfByte[22] = 115;
    arrayOfByte[23] = -116;
    arrayOfByte[24] = -104;
    arrayOfByte[25] = -109;
    arrayOfByte[26] = 43;
    arrayOfByte[27] = -39;
    arrayOfByte[28] = -68;
    arrayOfByte[29] = 76;
    arrayOfByte[30] = -126;
    arrayOfByte[31] = -54;
    arrayOfByte[32] = 30;
    arrayOfByte[33] = -101;
    arrayOfByte[34] = 87;
    arrayOfByte[35] = 60;
    arrayOfByte[36] = -3;
    arrayOfByte[37] = -44;
    arrayOfByte[38] = -32;
    arrayOfByte[39] = 22;
    arrayOfByte[40] = 103;
    arrayOfByte[41] = 66;
    arrayOfByte[42] = 111;
    arrayOfByte[43] = 24;
    arrayOfByte[44] = -118;
    arrayOfByte[45] = 23;
    arrayOfByte[46] = -27;
    arrayOfByte[47] = 18;
    arrayOfByte[48] = -66;
    arrayOfByte[49] = 78;
    arrayOfByte[50] = -60;
    arrayOfByte[51] = -42;
    arrayOfByte[52] = -38;
    arrayOfByte[53] = -98;
    arrayOfByte[54] = -34;
    arrayOfByte[55] = 73;
    arrayOfByte[56] = -96;
    arrayOfByte[57] = -5;
    arrayOfByte[58] = -11;
    arrayOfByte[59] = -114;
    arrayOfByte[60] = -69;
    arrayOfByte[61] = 47;
    arrayOfByte[62] = -18;
    arrayOfByte[63] = 122;
    arrayOfByte[64] = -87;
    arrayOfByte[65] = 104;
    arrayOfByte[66] = 121;
    arrayOfByte[67] = -111;
    arrayOfByte[68] = 21;
    arrayOfByte[69] = -78;
    arrayOfByte[70] = 7;
    arrayOfByte[71] = 63;
    arrayOfByte[72] = -108;
    arrayOfByte[73] = -62;
    arrayOfByte[74] = 16;
    arrayOfByte[75] = -119;
    arrayOfByte[76] = 11;
    arrayOfByte[77] = 34;
    arrayOfByte[78] = 95;
    arrayOfByte[79] = 33;
    arrayOfByte[80] = -128;
    arrayOfByte[81] = 127;
    arrayOfByte[82] = 93;
    arrayOfByte[83] = -102;
    arrayOfByte[84] = 90;
    arrayOfByte[85] = -112;
    arrayOfByte[86] = 50;
    arrayOfByte[87] = 39;
    arrayOfByte[88] = 53;
    arrayOfByte[89] = 62;
    arrayOfByte[90] = -52;
    arrayOfByte[91] = -25;
    arrayOfByte[92] = -65;
    arrayOfByte[93] = -9;
    arrayOfByte[94] = -105;
    arrayOfByte[95] = 3;
    arrayOfByte[96] = -1;
    arrayOfByte[97] = 25;
    arrayOfByte[98] = 48;
    arrayOfByte[99] = -77;
    arrayOfByte[100] = 72;
    arrayOfByte[101] = -91;
    arrayOfByte[102] = -75;
    arrayOfByte[103] = -47;
    arrayOfByte[104] = -41;
    arrayOfByte[105] = 94;
    arrayOfByte[106] = -110;
    arrayOfByte[107] = 42;
    arrayOfByte[108] = -84;
    arrayOfByte[109] = 86;
    arrayOfByte[110] = -86;
    arrayOfByte[111] = -58;
    arrayOfByte[112] = 79;
    arrayOfByte[113] = -72;
    arrayOfByte[114] = 56;
    arrayOfByte[115] = -46;
    arrayOfByte[116] = -106;
    arrayOfByte[117] = -92;
    arrayOfByte[118] = 125;
    arrayOfByte[119] = -74;
    arrayOfByte[120] = 118;
    arrayOfByte[121] = -4;
    arrayOfByte[122] = 107;
    arrayOfByte[123] = -30;
    arrayOfByte[124] = -100;
    arrayOfByte[125] = 116;
    arrayOfByte[126] = 4;
    arrayOfByte[127] = -15;
    arrayOfByte[''] = 69;
    arrayOfByte[''] = -99;
    arrayOfByte[''] = 112;
    arrayOfByte[''] = 89;
    arrayOfByte[''] = 100;
    arrayOfByte[''] = 113;
    arrayOfByte[''] = -121;
    arrayOfByte[''] = 32;
    arrayOfByte[''] = -122;
    arrayOfByte[''] = 91;
    arrayOfByte[''] = -49;
    arrayOfByte[''] = 101;
    arrayOfByte[''] = -26;
    arrayOfByte[''] = 45;
    arrayOfByte[''] = -88;
    arrayOfByte[''] = 2;
    arrayOfByte[''] = 27;
    arrayOfByte[''] = 96;
    arrayOfByte[''] = 37;
    arrayOfByte[''] = -83;
    arrayOfByte[''] = -82;
    arrayOfByte[''] = -80;
    arrayOfByte[''] = -71;
    arrayOfByte[''] = -10;
    arrayOfByte[''] = 28;
    arrayOfByte[''] = 70;
    arrayOfByte[''] = 97;
    arrayOfByte[''] = 105;
    arrayOfByte[''] = 52;
    arrayOfByte[''] = 64;
    arrayOfByte[''] = 126;
    arrayOfByte[''] = 15;
    arrayOfByte[' '] = 85;
    arrayOfByte['¡'] = 71;
    arrayOfByte['¢'] = -93;
    arrayOfByte['£'] = 35;
    arrayOfByte['¤'] = -35;
    arrayOfByte['¥'] = 81;
    arrayOfByte['¦'] = -81;
    arrayOfByte['§'] = 58;
    arrayOfByte['¨'] = -61;
    arrayOfByte['©'] = 92;
    arrayOfByte['ª'] = -7;
    arrayOfByte['«'] = -50;
    arrayOfByte['¬'] = -70;
    arrayOfByte['­'] = -59;
    arrayOfByte['®'] = -22;
    arrayOfByte['¯'] = 38;
    arrayOfByte['°'] = 44;
    arrayOfByte['±'] = 83;
    arrayOfByte['²'] = 13;
    arrayOfByte['³'] = 110;
    arrayOfByte['´'] = -123;
    arrayOfByte['µ'] = 40;
    arrayOfByte['¶'] = -124;
    arrayOfByte['·'] = 9;
    arrayOfByte['¸'] = -45;
    arrayOfByte['¹'] = -33;
    arrayOfByte['º'] = -51;
    arrayOfByte['»'] = -12;
    arrayOfByte['¼'] = 65;
    arrayOfByte['½'] = -127;
    arrayOfByte['¾'] = 77;
    arrayOfByte['¿'] = 82;
    arrayOfByte['À'] = 106;
    arrayOfByte['Á'] = -36;
    arrayOfByte['Â'] = 55;
    arrayOfByte['Ã'] = -56;
    arrayOfByte['Ä'] = 108;
    arrayOfByte['Å'] = -63;
    arrayOfByte['Æ'] = -85;
    arrayOfByte['Ç'] = -6;
    arrayOfByte['È'] = 36;
    arrayOfByte['É'] = -31;
    arrayOfByte['Ê'] = 123;
    arrayOfByte['Ë'] = 8;
    arrayOfByte['Ì'] = 12;
    arrayOfByte['Í'] = -67;
    arrayOfByte['Î'] = -79;
    arrayOfByte['Ï'] = 74;
    arrayOfByte['Ð'] = 120;
    arrayOfByte['Ñ'] = -120;
    arrayOfByte['Ò'] = -107;
    arrayOfByte['Ó'] = -117;
    arrayOfByte['Ô'] = -29;
    arrayOfByte['Õ'] = 99;
    arrayOfByte['Ö'] = -24;
    arrayOfByte['×'] = 109;
    arrayOfByte['Ø'] = -23;
    arrayOfByte['Ù'] = -53;
    arrayOfByte['Ú'] = -43;
    arrayOfByte['Û'] = -2;
    arrayOfByte['Ü'] = 59;
    arrayOfByte['Þ'] = 29;
    arrayOfByte['ß'] = 57;
    arrayOfByte['à'] = -14;
    arrayOfByte['á'] = -17;
    arrayOfByte['â'] = -73;
    arrayOfByte['ã'] = 14;
    arrayOfByte['ä'] = 102;
    arrayOfByte['å'] = 88;
    arrayOfByte['æ'] = -48;
    arrayOfByte['ç'] = -28;
    arrayOfByte['è'] = -90;
    arrayOfByte['é'] = 119;
    arrayOfByte['ê'] = 114;
    arrayOfByte['ë'] = -8;
    arrayOfByte['ì'] = -21;
    arrayOfByte['í'] = 117;
    arrayOfByte['î'] = 75;
    arrayOfByte['ï'] = 10;
    arrayOfByte['ð'] = 49;
    arrayOfByte['ñ'] = 68;
    arrayOfByte['ò'] = 80;
    arrayOfByte['ó'] = -76;
    arrayOfByte['ô'] = -113;
    arrayOfByte['õ'] = -19;
    arrayOfByte['ö'] = 31;
    arrayOfByte['÷'] = 26;
    arrayOfByte['ø'] = -37;
    arrayOfByte['ù'] = -103;
    arrayOfByte['ú'] = -115;
    arrayOfByte['û'] = 51;
    arrayOfByte['ü'] = -97;
    arrayOfByte['ý'] = 17;
    arrayOfByte['þ'] = -125;
    arrayOfByte['ÿ'] = 20;
  }

  public MD2Digest()
  {
    reset();
  }

  public MD2Digest(MD2Digest paramMD2Digest)
  {
    System.arraycopy(paramMD2Digest.X, 0, this.X, 0, paramMD2Digest.X.length);
    this.xOff = paramMD2Digest.xOff;
    System.arraycopy(paramMD2Digest.M, 0, this.M, 0, paramMD2Digest.M.length);
    this.mOff = paramMD2Digest.mOff;
    System.arraycopy(paramMD2Digest.C, 0, this.C, 0, paramMD2Digest.C.length);
    this.COff = paramMD2Digest.COff;
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (byte)(this.M.length - this.mOff);
    for (int j = this.mOff; ; j++)
    {
      if (j >= this.M.length)
      {
        processCheckSum(this.M);
        processBlock(this.M);
        processBlock(this.C);
        System.arraycopy(this.X, this.xOff, paramArrayOfByte, paramInt, 16);
        reset();
        return 16;
      }
      this.M[j] = i;
    }
  }

  public String getAlgorithmName()
  {
    return "MD2";
  }

  public int getByteLength()
  {
    return 16;
  }

  public int getDigestSize()
  {
    return 16;
  }

  protected void processBlock(byte[] paramArrayOfByte)
  {
    int j;
    int k;
    for (int i = 0; ; i++)
    {
      if (i >= 16)
      {
        j = 0;
        k = 0;
        if (k < 18)
          break;
        return;
      }
      this.X[(i + 16)] = paramArrayOfByte[i];
      this.X[(i + 32)] = ((byte)(paramArrayOfByte[i] ^ this.X[i]));
    }
    for (int m = 0; ; m++)
    {
      if (m >= 48)
      {
        j = (j + k) % 256;
        k++;
        break;
      }
      byte[] arrayOfByte = this.X;
      int n = (byte)(arrayOfByte[m] ^ S[j]);
      arrayOfByte[m] = n;
      j = n & 0xFF;
    }
  }

  protected void processCheckSum(byte[] paramArrayOfByte)
  {
    int i = this.C[15];
    for (int j = 0; ; j++)
    {
      if (j >= 16)
        return;
      byte[] arrayOfByte = this.C;
      arrayOfByte[j] = ((byte)(arrayOfByte[j] ^ S[(0xFF & (i ^ paramArrayOfByte[j]))]));
      i = this.C[j];
    }
  }

  public void reset()
  {
    this.xOff = 0;
    int i = 0;
    int j;
    if (i == this.X.length)
    {
      this.mOff = 0;
      j = 0;
      label23: if (j != this.M.length)
        break label62;
      this.COff = 0;
    }
    for (int k = 0; ; k++)
    {
      if (k == this.C.length)
      {
        return;
        this.X[i] = 0;
        i++;
        break;
        label62: this.M[j] = 0;
        j++;
        break label23;
      }
      this.C[k] = 0;
    }
  }

  public void update(byte paramByte)
  {
    byte[] arrayOfByte = this.M;
    int i = this.mOff;
    this.mOff = (i + 1);
    arrayOfByte[i] = paramByte;
    if (this.mOff == 16)
    {
      processCheckSum(this.M);
      processBlock(this.M);
      this.mOff = 0;
    }
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((this.mOff == 0) || (paramInt2 <= 0))
      label11: if (paramInt2 > 16)
        break label38;
    while (true)
    {
      if (paramInt2 <= 0)
      {
        return;
        update(paramArrayOfByte[paramInt1]);
        paramInt1++;
        paramInt2--;
        break;
        label38: System.arraycopy(paramArrayOfByte, paramInt1, this.M, 0, 16);
        processCheckSum(this.M);
        processBlock(this.M);
        paramInt2 -= 16;
        paramInt1 += 16;
        break label11;
      }
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
  }
}