package org.bouncycastle2.crypto.engines;

import java.lang.reflect.Array;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class AESLightEngine
  implements BlockCipher
{
  private static final int BLOCK_SIZE = 16;
  private static final byte[] S;
  private static final byte[] Si = arrayOfByte2;
  private static final int m1 = -2139062144;
  private static final int m2 = 2139062143;
  private static final int m3 = 27;
  private static final int[] rcon = { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145 };
  private int C0;
  private int C1;
  private int C2;
  private int C3;
  private int ROUNDS;
  private int[][] WorkingKey = null;
  private boolean forEncryption;

  static
  {
    byte[] arrayOfByte1 = new byte[256];
    arrayOfByte1[0] = 99;
    arrayOfByte1[1] = 124;
    arrayOfByte1[2] = 119;
    arrayOfByte1[3] = 123;
    arrayOfByte1[4] = -14;
    arrayOfByte1[5] = 107;
    arrayOfByte1[6] = 111;
    arrayOfByte1[7] = -59;
    arrayOfByte1[8] = 48;
    arrayOfByte1[9] = 1;
    arrayOfByte1[10] = 103;
    arrayOfByte1[11] = 43;
    arrayOfByte1[12] = -2;
    arrayOfByte1[13] = -41;
    arrayOfByte1[14] = -85;
    arrayOfByte1[15] = 118;
    arrayOfByte1[16] = -54;
    arrayOfByte1[17] = -126;
    arrayOfByte1[18] = -55;
    arrayOfByte1[19] = 125;
    arrayOfByte1[20] = -6;
    arrayOfByte1[21] = 89;
    arrayOfByte1[22] = 71;
    arrayOfByte1[23] = -16;
    arrayOfByte1[24] = -83;
    arrayOfByte1[25] = -44;
    arrayOfByte1[26] = -94;
    arrayOfByte1[27] = -81;
    arrayOfByte1[28] = -100;
    arrayOfByte1[29] = -92;
    arrayOfByte1[30] = 114;
    arrayOfByte1[31] = -64;
    arrayOfByte1[32] = -73;
    arrayOfByte1[33] = -3;
    arrayOfByte1[34] = -109;
    arrayOfByte1[35] = 38;
    arrayOfByte1[36] = 54;
    arrayOfByte1[37] = 63;
    arrayOfByte1[38] = -9;
    arrayOfByte1[39] = -52;
    arrayOfByte1[40] = 52;
    arrayOfByte1[41] = -91;
    arrayOfByte1[42] = -27;
    arrayOfByte1[43] = -15;
    arrayOfByte1[44] = 113;
    arrayOfByte1[45] = -40;
    arrayOfByte1[46] = 49;
    arrayOfByte1[47] = 21;
    arrayOfByte1[48] = 4;
    arrayOfByte1[49] = -57;
    arrayOfByte1[50] = 35;
    arrayOfByte1[51] = -61;
    arrayOfByte1[52] = 24;
    arrayOfByte1[53] = -106;
    arrayOfByte1[54] = 5;
    arrayOfByte1[55] = -102;
    arrayOfByte1[56] = 7;
    arrayOfByte1[57] = 18;
    arrayOfByte1[58] = -128;
    arrayOfByte1[59] = -30;
    arrayOfByte1[60] = -21;
    arrayOfByte1[61] = 39;
    arrayOfByte1[62] = -78;
    arrayOfByte1[63] = 117;
    arrayOfByte1[64] = 9;
    arrayOfByte1[65] = -125;
    arrayOfByte1[66] = 44;
    arrayOfByte1[67] = 26;
    arrayOfByte1[68] = 27;
    arrayOfByte1[69] = 110;
    arrayOfByte1[70] = 90;
    arrayOfByte1[71] = -96;
    arrayOfByte1[72] = 82;
    arrayOfByte1[73] = 59;
    arrayOfByte1[74] = -42;
    arrayOfByte1[75] = -77;
    arrayOfByte1[76] = 41;
    arrayOfByte1[77] = -29;
    arrayOfByte1[78] = 47;
    arrayOfByte1[79] = -124;
    arrayOfByte1[80] = 83;
    arrayOfByte1[81] = -47;
    arrayOfByte1[83] = -19;
    arrayOfByte1[84] = 32;
    arrayOfByte1[85] = -4;
    arrayOfByte1[86] = -79;
    arrayOfByte1[87] = 91;
    arrayOfByte1[88] = 106;
    arrayOfByte1[89] = -53;
    arrayOfByte1[90] = -66;
    arrayOfByte1[91] = 57;
    arrayOfByte1[92] = 74;
    arrayOfByte1[93] = 76;
    arrayOfByte1[94] = 88;
    arrayOfByte1[95] = -49;
    arrayOfByte1[96] = -48;
    arrayOfByte1[97] = -17;
    arrayOfByte1[98] = -86;
    arrayOfByte1[99] = -5;
    arrayOfByte1[100] = 67;
    arrayOfByte1[101] = 77;
    arrayOfByte1[102] = 51;
    arrayOfByte1[103] = -123;
    arrayOfByte1[104] = 69;
    arrayOfByte1[105] = -7;
    arrayOfByte1[106] = 2;
    arrayOfByte1[107] = 127;
    arrayOfByte1[108] = 80;
    arrayOfByte1[109] = 60;
    arrayOfByte1[110] = -97;
    arrayOfByte1[111] = -88;
    arrayOfByte1[112] = 81;
    arrayOfByte1[113] = -93;
    arrayOfByte1[114] = 64;
    arrayOfByte1[115] = -113;
    arrayOfByte1[116] = -110;
    arrayOfByte1[117] = -99;
    arrayOfByte1[118] = 56;
    arrayOfByte1[119] = -11;
    arrayOfByte1[120] = -68;
    arrayOfByte1[121] = -74;
    arrayOfByte1[122] = -38;
    arrayOfByte1[123] = 33;
    arrayOfByte1[124] = 16;
    arrayOfByte1[125] = -1;
    arrayOfByte1[126] = -13;
    arrayOfByte1[127] = -46;
    arrayOfByte1[''] = -51;
    arrayOfByte1[''] = 12;
    arrayOfByte1[''] = 19;
    arrayOfByte1[''] = -20;
    arrayOfByte1[''] = 95;
    arrayOfByte1[''] = -105;
    arrayOfByte1[''] = 68;
    arrayOfByte1[''] = 23;
    arrayOfByte1[''] = -60;
    arrayOfByte1[''] = -89;
    arrayOfByte1[''] = 126;
    arrayOfByte1[''] = 61;
    arrayOfByte1[''] = 100;
    arrayOfByte1[''] = 93;
    arrayOfByte1[''] = 25;
    arrayOfByte1[''] = 115;
    arrayOfByte1[''] = 96;
    arrayOfByte1[''] = -127;
    arrayOfByte1[''] = 79;
    arrayOfByte1[''] = -36;
    arrayOfByte1[''] = 34;
    arrayOfByte1[''] = 42;
    arrayOfByte1[''] = -112;
    arrayOfByte1[''] = -120;
    arrayOfByte1[''] = 70;
    arrayOfByte1[''] = -18;
    arrayOfByte1[''] = -72;
    arrayOfByte1[''] = 20;
    arrayOfByte1[''] = -34;
    arrayOfByte1[''] = 94;
    arrayOfByte1[''] = 11;
    arrayOfByte1[''] = -37;
    arrayOfByte1[' '] = -32;
    arrayOfByte1['¡'] = 50;
    arrayOfByte1['¢'] = 58;
    arrayOfByte1['£'] = 10;
    arrayOfByte1['¤'] = 73;
    arrayOfByte1['¥'] = 6;
    arrayOfByte1['¦'] = 36;
    arrayOfByte1['§'] = 92;
    arrayOfByte1['¨'] = -62;
    arrayOfByte1['©'] = -45;
    arrayOfByte1['ª'] = -84;
    arrayOfByte1['«'] = 98;
    arrayOfByte1['¬'] = -111;
    arrayOfByte1['­'] = -107;
    arrayOfByte1['®'] = -28;
    arrayOfByte1['¯'] = 121;
    arrayOfByte1['°'] = -25;
    arrayOfByte1['±'] = -56;
    arrayOfByte1['²'] = 55;
    arrayOfByte1['³'] = 109;
    arrayOfByte1['´'] = -115;
    arrayOfByte1['µ'] = -43;
    arrayOfByte1['¶'] = 78;
    arrayOfByte1['·'] = -87;
    arrayOfByte1['¸'] = 108;
    arrayOfByte1['¹'] = 86;
    arrayOfByte1['º'] = -12;
    arrayOfByte1['»'] = -22;
    arrayOfByte1['¼'] = 101;
    arrayOfByte1['½'] = 122;
    arrayOfByte1['¾'] = -82;
    arrayOfByte1['¿'] = 8;
    arrayOfByte1['À'] = -70;
    arrayOfByte1['Á'] = 120;
    arrayOfByte1['Â'] = 37;
    arrayOfByte1['Ã'] = 46;
    arrayOfByte1['Ä'] = 28;
    arrayOfByte1['Å'] = -90;
    arrayOfByte1['Æ'] = -76;
    arrayOfByte1['Ç'] = -58;
    arrayOfByte1['È'] = -24;
    arrayOfByte1['É'] = -35;
    arrayOfByte1['Ê'] = 116;
    arrayOfByte1['Ë'] = 31;
    arrayOfByte1['Ì'] = 75;
    arrayOfByte1['Í'] = -67;
    arrayOfByte1['Î'] = -117;
    arrayOfByte1['Ï'] = -118;
    arrayOfByte1['Ð'] = 112;
    arrayOfByte1['Ñ'] = 62;
    arrayOfByte1['Ò'] = -75;
    arrayOfByte1['Ó'] = 102;
    arrayOfByte1['Ô'] = 72;
    arrayOfByte1['Õ'] = 3;
    arrayOfByte1['Ö'] = -10;
    arrayOfByte1['×'] = 14;
    arrayOfByte1['Ø'] = 97;
    arrayOfByte1['Ù'] = 53;
    arrayOfByte1['Ú'] = 87;
    arrayOfByte1['Û'] = -71;
    arrayOfByte1['Ü'] = -122;
    arrayOfByte1['Ý'] = -63;
    arrayOfByte1['Þ'] = 29;
    arrayOfByte1['ß'] = -98;
    arrayOfByte1['à'] = -31;
    arrayOfByte1['á'] = -8;
    arrayOfByte1['â'] = -104;
    arrayOfByte1['ã'] = 17;
    arrayOfByte1['ä'] = 105;
    arrayOfByte1['å'] = -39;
    arrayOfByte1['æ'] = -114;
    arrayOfByte1['ç'] = -108;
    arrayOfByte1['è'] = -101;
    arrayOfByte1['é'] = 30;
    arrayOfByte1['ê'] = -121;
    arrayOfByte1['ë'] = -23;
    arrayOfByte1['ì'] = -50;
    arrayOfByte1['í'] = 85;
    arrayOfByte1['î'] = 40;
    arrayOfByte1['ï'] = -33;
    arrayOfByte1['ð'] = -116;
    arrayOfByte1['ñ'] = -95;
    arrayOfByte1['ò'] = -119;
    arrayOfByte1['ó'] = 13;
    arrayOfByte1['ô'] = -65;
    arrayOfByte1['õ'] = -26;
    arrayOfByte1['ö'] = 66;
    arrayOfByte1['÷'] = 104;
    arrayOfByte1['ø'] = 65;
    arrayOfByte1['ù'] = -103;
    arrayOfByte1['ú'] = 45;
    arrayOfByte1['û'] = 15;
    arrayOfByte1['ü'] = -80;
    arrayOfByte1['ý'] = 84;
    arrayOfByte1['þ'] = -69;
    arrayOfByte1['ÿ'] = 22;
    S = arrayOfByte1;
    byte[] arrayOfByte2 = new byte[256];
    arrayOfByte2[0] = 82;
    arrayOfByte2[1] = 9;
    arrayOfByte2[2] = 106;
    arrayOfByte2[3] = -43;
    arrayOfByte2[4] = 48;
    arrayOfByte2[5] = 54;
    arrayOfByte2[6] = -91;
    arrayOfByte2[7] = 56;
    arrayOfByte2[8] = -65;
    arrayOfByte2[9] = 64;
    arrayOfByte2[10] = -93;
    arrayOfByte2[11] = -98;
    arrayOfByte2[12] = -127;
    arrayOfByte2[13] = -13;
    arrayOfByte2[14] = -41;
    arrayOfByte2[15] = -5;
    arrayOfByte2[16] = 124;
    arrayOfByte2[17] = -29;
    arrayOfByte2[18] = 57;
    arrayOfByte2[19] = -126;
    arrayOfByte2[20] = -101;
    arrayOfByte2[21] = 47;
    arrayOfByte2[22] = -1;
    arrayOfByte2[23] = -121;
    arrayOfByte2[24] = 52;
    arrayOfByte2[25] = -114;
    arrayOfByte2[26] = 67;
    arrayOfByte2[27] = 68;
    arrayOfByte2[28] = -60;
    arrayOfByte2[29] = -34;
    arrayOfByte2[30] = -23;
    arrayOfByte2[31] = -53;
    arrayOfByte2[32] = 84;
    arrayOfByte2[33] = 123;
    arrayOfByte2[34] = -108;
    arrayOfByte2[35] = 50;
    arrayOfByte2[36] = -90;
    arrayOfByte2[37] = -62;
    arrayOfByte2[38] = 35;
    arrayOfByte2[39] = 61;
    arrayOfByte2[40] = -18;
    arrayOfByte2[41] = 76;
    arrayOfByte2[42] = -107;
    arrayOfByte2[43] = 11;
    arrayOfByte2[44] = 66;
    arrayOfByte2[45] = -6;
    arrayOfByte2[46] = -61;
    arrayOfByte2[47] = 78;
    arrayOfByte2[48] = 8;
    arrayOfByte2[49] = 46;
    arrayOfByte2[50] = -95;
    arrayOfByte2[51] = 102;
    arrayOfByte2[52] = 40;
    arrayOfByte2[53] = -39;
    arrayOfByte2[54] = 36;
    arrayOfByte2[55] = -78;
    arrayOfByte2[56] = 118;
    arrayOfByte2[57] = 91;
    arrayOfByte2[58] = -94;
    arrayOfByte2[59] = 73;
    arrayOfByte2[60] = 109;
    arrayOfByte2[61] = -117;
    arrayOfByte2[62] = -47;
    arrayOfByte2[63] = 37;
    arrayOfByte2[64] = 114;
    arrayOfByte2[65] = -8;
    arrayOfByte2[66] = -10;
    arrayOfByte2[67] = 100;
    arrayOfByte2[68] = -122;
    arrayOfByte2[69] = 104;
    arrayOfByte2[70] = -104;
    arrayOfByte2[71] = 22;
    arrayOfByte2[72] = -44;
    arrayOfByte2[73] = -92;
    arrayOfByte2[74] = 92;
    arrayOfByte2[75] = -52;
    arrayOfByte2[76] = 93;
    arrayOfByte2[77] = 101;
    arrayOfByte2[78] = -74;
    arrayOfByte2[79] = -110;
    arrayOfByte2[80] = 108;
    arrayOfByte2[81] = 112;
    arrayOfByte2[82] = 72;
    arrayOfByte2[83] = 80;
    arrayOfByte2[84] = -3;
    arrayOfByte2[85] = -19;
    arrayOfByte2[86] = -71;
    arrayOfByte2[87] = -38;
    arrayOfByte2[88] = 94;
    arrayOfByte2[89] = 21;
    arrayOfByte2[90] = 70;
    arrayOfByte2[91] = 87;
    arrayOfByte2[92] = -89;
    arrayOfByte2[93] = -115;
    arrayOfByte2[94] = -99;
    arrayOfByte2[95] = -124;
    arrayOfByte2[96] = -112;
    arrayOfByte2[97] = -40;
    arrayOfByte2[98] = -85;
    arrayOfByte2[100] = -116;
    arrayOfByte2[101] = -68;
    arrayOfByte2[102] = -45;
    arrayOfByte2[103] = 10;
    arrayOfByte2[104] = -9;
    arrayOfByte2[105] = -28;
    arrayOfByte2[106] = 88;
    arrayOfByte2[107] = 5;
    arrayOfByte2[108] = -72;
    arrayOfByte2[109] = -77;
    arrayOfByte2[110] = 69;
    arrayOfByte2[111] = 6;
    arrayOfByte2[112] = -48;
    arrayOfByte2[113] = 44;
    arrayOfByte2[114] = 30;
    arrayOfByte2[115] = -113;
    arrayOfByte2[116] = -54;
    arrayOfByte2[117] = 63;
    arrayOfByte2[118] = 15;
    arrayOfByte2[119] = 2;
    arrayOfByte2[120] = -63;
    arrayOfByte2[121] = -81;
    arrayOfByte2[122] = -67;
    arrayOfByte2[123] = 3;
    arrayOfByte2[124] = 1;
    arrayOfByte2[125] = 19;
    arrayOfByte2[126] = -118;
    arrayOfByte2[127] = 107;
    arrayOfByte2[''] = 58;
    arrayOfByte2[''] = -111;
    arrayOfByte2[''] = 17;
    arrayOfByte2[''] = 65;
    arrayOfByte2[''] = 79;
    arrayOfByte2[''] = 103;
    arrayOfByte2[''] = -36;
    arrayOfByte2[''] = -22;
    arrayOfByte2[''] = -105;
    arrayOfByte2[''] = -14;
    arrayOfByte2[''] = -49;
    arrayOfByte2[''] = -50;
    arrayOfByte2[''] = -16;
    arrayOfByte2[''] = -76;
    arrayOfByte2[''] = -26;
    arrayOfByte2[''] = 115;
    arrayOfByte2[''] = -106;
    arrayOfByte2[''] = -84;
    arrayOfByte2[''] = 116;
    arrayOfByte2[''] = 34;
    arrayOfByte2[''] = -25;
    arrayOfByte2[''] = -83;
    arrayOfByte2[''] = 53;
    arrayOfByte2[''] = -123;
    arrayOfByte2[''] = -30;
    arrayOfByte2[''] = -7;
    arrayOfByte2[''] = 55;
    arrayOfByte2[''] = -24;
    arrayOfByte2[''] = 28;
    arrayOfByte2[''] = 117;
    arrayOfByte2[''] = -33;
    arrayOfByte2[''] = 110;
    arrayOfByte2[' '] = 71;
    arrayOfByte2['¡'] = -15;
    arrayOfByte2['¢'] = 26;
    arrayOfByte2['£'] = 113;
    arrayOfByte2['¤'] = 29;
    arrayOfByte2['¥'] = 41;
    arrayOfByte2['¦'] = -59;
    arrayOfByte2['§'] = -119;
    arrayOfByte2['¨'] = 111;
    arrayOfByte2['©'] = -73;
    arrayOfByte2['ª'] = 98;
    arrayOfByte2['«'] = 14;
    arrayOfByte2['¬'] = -86;
    arrayOfByte2['­'] = 24;
    arrayOfByte2['®'] = -66;
    arrayOfByte2['¯'] = 27;
    arrayOfByte2['°'] = -4;
    arrayOfByte2['±'] = 86;
    arrayOfByte2['²'] = 62;
    arrayOfByte2['³'] = 75;
    arrayOfByte2['´'] = -58;
    arrayOfByte2['µ'] = -46;
    arrayOfByte2['¶'] = 121;
    arrayOfByte2['·'] = 32;
    arrayOfByte2['¸'] = -102;
    arrayOfByte2['¹'] = -37;
    arrayOfByte2['º'] = -64;
    arrayOfByte2['»'] = -2;
    arrayOfByte2['¼'] = 120;
    arrayOfByte2['½'] = -51;
    arrayOfByte2['¾'] = 90;
    arrayOfByte2['¿'] = -12;
    arrayOfByte2['À'] = 31;
    arrayOfByte2['Á'] = -35;
    arrayOfByte2['Â'] = -88;
    arrayOfByte2['Ã'] = 51;
    arrayOfByte2['Ä'] = -120;
    arrayOfByte2['Å'] = 7;
    arrayOfByte2['Æ'] = -57;
    arrayOfByte2['Ç'] = 49;
    arrayOfByte2['È'] = -79;
    arrayOfByte2['É'] = 18;
    arrayOfByte2['Ê'] = 16;
    arrayOfByte2['Ë'] = 89;
    arrayOfByte2['Ì'] = 39;
    arrayOfByte2['Í'] = -128;
    arrayOfByte2['Î'] = -20;
    arrayOfByte2['Ï'] = 95;
    arrayOfByte2['Ð'] = 96;
    arrayOfByte2['Ñ'] = 81;
    arrayOfByte2['Ò'] = 127;
    arrayOfByte2['Ó'] = -87;
    arrayOfByte2['Ô'] = 25;
    arrayOfByte2['Õ'] = -75;
    arrayOfByte2['Ö'] = 74;
    arrayOfByte2['×'] = 13;
    arrayOfByte2['Ø'] = 45;
    arrayOfByte2['Ù'] = -27;
    arrayOfByte2['Ú'] = 122;
    arrayOfByte2['Û'] = -97;
    arrayOfByte2['Ü'] = -109;
    arrayOfByte2['Ý'] = -55;
    arrayOfByte2['Þ'] = -100;
    arrayOfByte2['ß'] = -17;
    arrayOfByte2['à'] = -96;
    arrayOfByte2['á'] = -32;
    arrayOfByte2['â'] = 59;
    arrayOfByte2['ã'] = 77;
    arrayOfByte2['ä'] = -82;
    arrayOfByte2['å'] = 42;
    arrayOfByte2['æ'] = -11;
    arrayOfByte2['ç'] = -80;
    arrayOfByte2['è'] = -56;
    arrayOfByte2['é'] = -21;
    arrayOfByte2['ê'] = -69;
    arrayOfByte2['ë'] = 60;
    arrayOfByte2['ì'] = -125;
    arrayOfByte2['í'] = 83;
    arrayOfByte2['î'] = -103;
    arrayOfByte2['ï'] = 97;
    arrayOfByte2['ð'] = 23;
    arrayOfByte2['ñ'] = 43;
    arrayOfByte2['ò'] = 4;
    arrayOfByte2['ó'] = 126;
    arrayOfByte2['ô'] = -70;
    arrayOfByte2['õ'] = 119;
    arrayOfByte2['ö'] = -42;
    arrayOfByte2['÷'] = 38;
    arrayOfByte2['ø'] = -31;
    arrayOfByte2['ù'] = 105;
    arrayOfByte2['ú'] = 20;
    arrayOfByte2['û'] = 99;
    arrayOfByte2['ü'] = 85;
    arrayOfByte2['ý'] = 33;
    arrayOfByte2['þ'] = 12;
    arrayOfByte2['ÿ'] = 125;
  }

  private int FFmulX(int paramInt)
  {
    return (0x7F7F7F7F & paramInt) << 1 ^ 27 * ((0x80808080 & paramInt) >>> 7);
  }

  private void decryptBlock(int[][] paramArrayOfInt)
  {
    this.C0 ^= paramArrayOfInt[this.ROUNDS][0];
    this.C1 ^= paramArrayOfInt[this.ROUNDS][1];
    this.C2 ^= paramArrayOfInt[this.ROUNDS][2];
    this.C3 ^= paramArrayOfInt[this.ROUNDS][3];
    int i = -1 + this.ROUNDS;
    while (true)
    {
      if (i <= 1)
      {
        int i4 = inv_mcol(0xFF & Si[(0xFF & this.C0)] ^ (0xFF & Si[(0xFF & this.C3 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C2 >> 16)]) << 16 ^ Si[(0xFF & this.C1 >> 24)] << 24) ^ paramArrayOfInt[i][0];
        int i5 = inv_mcol(0xFF & Si[(0xFF & this.C1)] ^ (0xFF & Si[(0xFF & this.C0 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C3 >> 16)]) << 16 ^ Si[(0xFF & this.C2 >> 24)] << 24) ^ paramArrayOfInt[i][1];
        int i6 = inv_mcol(0xFF & Si[(0xFF & this.C2)] ^ (0xFF & Si[(0xFF & this.C1 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C0 >> 16)]) << 16 ^ Si[(0xFF & this.C3 >> 24)] << 24) ^ paramArrayOfInt[i][2];
        int i7 = inv_mcol(0xFF & Si[(0xFF & this.C3)] ^ (0xFF & Si[(0xFF & this.C2 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C1 >> 16)]) << 16 ^ Si[(0xFF & this.C0 >> 24)] << 24) ^ paramArrayOfInt[i][3];
        this.C0 = (0xFF & Si[(i4 & 0xFF)] ^ (0xFF & Si[(0xFF & i7 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & i6 >> 16)]) << 16 ^ Si[(0xFF & i5 >> 24)] << 24 ^ paramArrayOfInt[0][0]);
        this.C1 = (0xFF & Si[(i5 & 0xFF)] ^ (0xFF & Si[(0xFF & i4 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & i7 >> 16)]) << 16 ^ Si[(0xFF & i6 >> 24)] << 24 ^ paramArrayOfInt[0][1]);
        this.C2 = (0xFF & Si[(i6 & 0xFF)] ^ (0xFF & Si[(0xFF & i5 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & i4 >> 16)]) << 16 ^ Si[(0xFF & i7 >> 24)] << 24 ^ paramArrayOfInt[0][2]);
        this.C3 = (0xFF & Si[(i7 & 0xFF)] ^ (0xFF & Si[(0xFF & i6 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & i5 >> 16)]) << 16 ^ Si[(0xFF & i4 >> 24)] << 24 ^ paramArrayOfInt[0][3]);
        return;
      }
      int j = inv_mcol(0xFF & Si[(0xFF & this.C0)] ^ (0xFF & Si[(0xFF & this.C3 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C2 >> 16)]) << 16 ^ Si[(0xFF & this.C1 >> 24)] << 24) ^ paramArrayOfInt[i][0];
      int k = inv_mcol(0xFF & Si[(0xFF & this.C1)] ^ (0xFF & Si[(0xFF & this.C0 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C3 >> 16)]) << 16 ^ Si[(0xFF & this.C2 >> 24)] << 24) ^ paramArrayOfInt[i][1];
      int m = inv_mcol(0xFF & Si[(0xFF & this.C2)] ^ (0xFF & Si[(0xFF & this.C1 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C0 >> 16)]) << 16 ^ Si[(0xFF & this.C3 >> 24)] << 24) ^ paramArrayOfInt[i][2];
      int n = inv_mcol(0xFF & Si[(0xFF & this.C3)] ^ (0xFF & Si[(0xFF & this.C2 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & this.C1 >> 16)]) << 16 ^ Si[(0xFF & this.C0 >> 24)] << 24);
      int i1 = i - 1;
      int i2 = n ^ paramArrayOfInt[i][3];
      this.C0 = (inv_mcol(0xFF & Si[(j & 0xFF)] ^ (0xFF & Si[(0xFF & i2 >> 8)]) << 8 ^ (0xFF & Si[(0xFF & m >> 16)]) << 16 ^ Si[(0xFF & k >> 24)] << 24) ^ paramArrayOfInt[i1][0]);
      this.C1 = (inv_mcol(0xFF & Si[(k & 0xFF)] ^ (0xFF & Si[(0xFF & j >> 8)]) << 8 ^ (0xFF & Si[(0xFF & i2 >> 16)]) << 16 ^ Si[(0xFF & m >> 24)] << 24) ^ paramArrayOfInt[i1][1]);
      this.C2 = (inv_mcol(0xFF & Si[(m & 0xFF)] ^ (0xFF & Si[(0xFF & k >> 8)]) << 8 ^ (0xFF & Si[(0xFF & j >> 16)]) << 16 ^ Si[(0xFF & i2 >> 24)] << 24) ^ paramArrayOfInt[i1][2]);
      int i3 = inv_mcol(0xFF & Si[(i2 & 0xFF)] ^ (0xFF & Si[(0xFF & m >> 8)]) << 8 ^ (0xFF & Si[(0xFF & k >> 16)]) << 16 ^ Si[(0xFF & j >> 24)] << 24);
      i = i1 - 1;
      this.C3 = (i3 ^ paramArrayOfInt[i1][3]);
    }
  }

  private void encryptBlock(int[][] paramArrayOfInt)
  {
    this.C0 ^= paramArrayOfInt[0][0];
    this.C1 ^= paramArrayOfInt[0][1];
    this.C2 ^= paramArrayOfInt[0][2];
    this.C3 ^= paramArrayOfInt[0][3];
    int i = 1;
    while (true)
    {
      if (i >= -1 + this.ROUNDS)
      {
        int i4 = mcol(0xFF & S[(0xFF & this.C0)] ^ (0xFF & S[(0xFF & this.C1 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C2 >> 16)]) << 16 ^ S[(0xFF & this.C3 >> 24)] << 24) ^ paramArrayOfInt[i][0];
        int i5 = mcol(0xFF & S[(0xFF & this.C1)] ^ (0xFF & S[(0xFF & this.C2 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C3 >> 16)]) << 16 ^ S[(0xFF & this.C0 >> 24)] << 24) ^ paramArrayOfInt[i][1];
        int i6 = mcol(0xFF & S[(0xFF & this.C2)] ^ (0xFF & S[(0xFF & this.C3 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C0 >> 16)]) << 16 ^ S[(0xFF & this.C1 >> 24)] << 24) ^ paramArrayOfInt[i][2];
        int i7 = mcol(0xFF & S[(0xFF & this.C3)] ^ (0xFF & S[(0xFF & this.C0 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C1 >> 16)]) << 16 ^ S[(0xFF & this.C2 >> 24)] << 24);
        int i8 = i + 1;
        int i9 = i7 ^ paramArrayOfInt[i][3];
        this.C0 = (0xFF & S[(i4 & 0xFF)] ^ (0xFF & S[(0xFF & i5 >> 8)]) << 8 ^ (0xFF & S[(0xFF & i6 >> 16)]) << 16 ^ S[(0xFF & i9 >> 24)] << 24 ^ paramArrayOfInt[i8][0]);
        this.C1 = (0xFF & S[(i5 & 0xFF)] ^ (0xFF & S[(0xFF & i6 >> 8)]) << 8 ^ (0xFF & S[(0xFF & i9 >> 16)]) << 16 ^ S[(0xFF & i4 >> 24)] << 24 ^ paramArrayOfInt[i8][1]);
        this.C2 = (0xFF & S[(i6 & 0xFF)] ^ (0xFF & S[(0xFF & i9 >> 8)]) << 8 ^ (0xFF & S[(0xFF & i4 >> 16)]) << 16 ^ S[(0xFF & i5 >> 24)] << 24 ^ paramArrayOfInt[i8][2]);
        this.C3 = (0xFF & S[(i9 & 0xFF)] ^ (0xFF & S[(0xFF & i4 >> 8)]) << 8 ^ (0xFF & S[(0xFF & i5 >> 16)]) << 16 ^ S[(0xFF & i6 >> 24)] << 24 ^ paramArrayOfInt[i8][3]);
        return;
      }
      int j = mcol(0xFF & S[(0xFF & this.C0)] ^ (0xFF & S[(0xFF & this.C1 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C2 >> 16)]) << 16 ^ S[(0xFF & this.C3 >> 24)] << 24) ^ paramArrayOfInt[i][0];
      int k = mcol(0xFF & S[(0xFF & this.C1)] ^ (0xFF & S[(0xFF & this.C2 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C3 >> 16)]) << 16 ^ S[(0xFF & this.C0 >> 24)] << 24) ^ paramArrayOfInt[i][1];
      int m = mcol(0xFF & S[(0xFF & this.C2)] ^ (0xFF & S[(0xFF & this.C3 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C0 >> 16)]) << 16 ^ S[(0xFF & this.C1 >> 24)] << 24) ^ paramArrayOfInt[i][2];
      int n = mcol(0xFF & S[(0xFF & this.C3)] ^ (0xFF & S[(0xFF & this.C0 >> 8)]) << 8 ^ (0xFF & S[(0xFF & this.C1 >> 16)]) << 16 ^ S[(0xFF & this.C2 >> 24)] << 24);
      int i1 = i + 1;
      int i2 = n ^ paramArrayOfInt[i][3];
      this.C0 = (mcol(0xFF & S[(j & 0xFF)] ^ (0xFF & S[(0xFF & k >> 8)]) << 8 ^ (0xFF & S[(0xFF & m >> 16)]) << 16 ^ S[(0xFF & i2 >> 24)] << 24) ^ paramArrayOfInt[i1][0]);
      this.C1 = (mcol(0xFF & S[(k & 0xFF)] ^ (0xFF & S[(0xFF & m >> 8)]) << 8 ^ (0xFF & S[(0xFF & i2 >> 16)]) << 16 ^ S[(0xFF & j >> 24)] << 24) ^ paramArrayOfInt[i1][1]);
      this.C2 = (mcol(0xFF & S[(m & 0xFF)] ^ (0xFF & S[(0xFF & i2 >> 8)]) << 8 ^ (0xFF & S[(0xFF & j >> 16)]) << 16 ^ S[(0xFF & k >> 24)] << 24) ^ paramArrayOfInt[i1][2]);
      int i3 = mcol(0xFF & S[(i2 & 0xFF)] ^ (0xFF & S[(0xFF & j >> 8)]) << 8 ^ (0xFF & S[(0xFF & k >> 16)]) << 16 ^ S[(0xFF & m >> 24)] << 24);
      i = i1 + 1;
      this.C3 = (i3 ^ paramArrayOfInt[i1][3]);
    }
  }

  private int[][] generateWorkingKey(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    int i = paramArrayOfByte.length / 4;
    if (((i != 4) && (i != 6) && (i != 8)) || (i * 4 != paramArrayOfByte.length))
      throw new IllegalArgumentException("Key length not 128/192/256 bits.");
    this.ROUNDS = (i + 6);
    int[] arrayOfInt = { 1 + this.ROUNDS, 4 };
    int[][] arrayOfInt1 = (int[][])Array.newInstance(Integer.TYPE, arrayOfInt);
    int j = 0;
    int k = 0;
    int n;
    int i2;
    while (true)
    {
      if (k >= paramArrayOfByte.length)
      {
        int m = 1 + this.ROUNDS << 2;
        n = i;
        if (n < m)
          break;
        if (!paramBoolean)
        {
          i2 = 1;
          if (i2 < this.ROUNDS)
            break label310;
        }
        return arrayOfInt1;
      }
      arrayOfInt1[(j >> 2)][(j & 0x3)] = (0xFF & paramArrayOfByte[k] | (0xFF & paramArrayOfByte[(k + 1)]) << 8 | (0xFF & paramArrayOfByte[(k + 2)]) << 16 | paramArrayOfByte[(k + 3)] << 24);
      k += 4;
      j++;
    }
    int i1 = arrayOfInt1[(n - 1 >> 2)][(0x3 & n - 1)];
    if (n % i == 0);
    for (i1 = subWord(shift(i1, 8)) ^ rcon[(-1 + n / i)]; ; i1 = subWord(i1))
      do
      {
        arrayOfInt1[(n >> 2)][(n & 0x3)] = (i1 ^ arrayOfInt1[(n - i >> 2)][(0x3 & n - i)]);
        n++;
        break;
      }
      while ((i <= 6) || (n % i != 4));
    label310: for (int i3 = 0; ; i3++)
    {
      if (i3 >= 4)
      {
        i2++;
        break;
      }
      arrayOfInt1[i2][i3] = inv_mcol(arrayOfInt1[i2][i3]);
    }
  }

  private int inv_mcol(int paramInt)
  {
    int i = FFmulX(paramInt);
    int j = FFmulX(i);
    int k = FFmulX(j);
    int m = paramInt ^ k;
    return k ^ (i ^ j) ^ shift(i ^ m, 8) ^ shift(j ^ m, 16) ^ shift(m, 24);
  }

  private int mcol(int paramInt)
  {
    int i = FFmulX(paramInt);
    return i ^ shift(paramInt ^ i, 8) ^ shift(paramInt, 16) ^ shift(paramInt, 24);
  }

  private void packBlock(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = ((byte)this.C0);
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(this.C0 >> 8));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(this.C0 >> 16));
    int m = k + 1;
    paramArrayOfByte[k] = ((byte)(this.C0 >> 24));
    int n = m + 1;
    paramArrayOfByte[m] = ((byte)this.C1);
    int i1 = n + 1;
    paramArrayOfByte[n] = ((byte)(this.C1 >> 8));
    int i2 = i1 + 1;
    paramArrayOfByte[i1] = ((byte)(this.C1 >> 16));
    int i3 = i2 + 1;
    paramArrayOfByte[i2] = ((byte)(this.C1 >> 24));
    int i4 = i3 + 1;
    paramArrayOfByte[i3] = ((byte)this.C2);
    int i5 = i4 + 1;
    paramArrayOfByte[i4] = ((byte)(this.C2 >> 8));
    int i6 = i5 + 1;
    paramArrayOfByte[i5] = ((byte)(this.C2 >> 16));
    int i7 = i6 + 1;
    paramArrayOfByte[i6] = ((byte)(this.C2 >> 24));
    int i8 = i7 + 1;
    paramArrayOfByte[i7] = ((byte)this.C3);
    int i9 = i8 + 1;
    paramArrayOfByte[i8] = ((byte)(this.C3 >> 8));
    int i10 = i9 + 1;
    paramArrayOfByte[i9] = ((byte)(this.C3 >> 16));
    (i10 + 1);
    paramArrayOfByte[i10] = ((byte)(this.C3 >> 24));
  }

  private int shift(int paramInt1, int paramInt2)
  {
    return paramInt1 >>> paramInt2 | paramInt1 << -paramInt2;
  }

  private int subWord(int paramInt)
  {
    return 0xFF & S[(paramInt & 0xFF)] | (0xFF & S[(0xFF & paramInt >> 8)]) << 8 | (0xFF & S[(0xFF & paramInt >> 16)]) << 16 | S[(0xFF & paramInt >> 24)] << 24;
  }

  private void unpackBlock(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    this.C0 = (0xFF & paramArrayOfByte[paramInt]);
    int j = this.C0;
    int k = i + 1;
    this.C0 = (j | (0xFF & paramArrayOfByte[i]) << 8);
    int m = this.C0;
    int n = k + 1;
    this.C0 = (m | (0xFF & paramArrayOfByte[k]) << 16);
    int i1 = this.C0;
    int i2 = n + 1;
    this.C0 = (i1 | paramArrayOfByte[n] << 24);
    int i3 = i2 + 1;
    this.C1 = (0xFF & paramArrayOfByte[i2]);
    int i4 = this.C1;
    int i5 = i3 + 1;
    this.C1 = (i4 | (0xFF & paramArrayOfByte[i3]) << 8);
    int i6 = this.C1;
    int i7 = i5 + 1;
    this.C1 = (i6 | (0xFF & paramArrayOfByte[i5]) << 16);
    int i8 = this.C1;
    int i9 = i7 + 1;
    this.C1 = (i8 | paramArrayOfByte[i7] << 24);
    int i10 = i9 + 1;
    this.C2 = (0xFF & paramArrayOfByte[i9]);
    int i11 = this.C2;
    int i12 = i10 + 1;
    this.C2 = (i11 | (0xFF & paramArrayOfByte[i10]) << 8);
    int i13 = this.C2;
    int i14 = i12 + 1;
    this.C2 = (i13 | (0xFF & paramArrayOfByte[i12]) << 16);
    int i15 = this.C2;
    int i16 = i14 + 1;
    this.C2 = (i15 | paramArrayOfByte[i14] << 24);
    int i17 = i16 + 1;
    this.C3 = (0xFF & paramArrayOfByte[i16]);
    int i18 = this.C3;
    int i19 = i17 + 1;
    this.C3 = (i18 | (0xFF & paramArrayOfByte[i17]) << 8);
    int i20 = this.C3;
    int i21 = i19 + 1;
    this.C3 = (i20 | (0xFF & paramArrayOfByte[i19]) << 16);
    int i22 = this.C3;
    (i21 + 1);
    this.C3 = (i22 | paramArrayOfByte[i21] << 24);
  }

  public String getAlgorithmName()
  {
    return "AES";
  }

  public int getBlockSize()
  {
    return 16;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof KeyParameter))
    {
      this.WorkingKey = generateWorkingKey(((KeyParameter)paramCipherParameters).getKey(), paramBoolean);
      this.forEncryption = paramBoolean;
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to AES init - " + paramCipherParameters.getClass().getName());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.WorkingKey == null)
      throw new IllegalStateException("AES engine not initialised");
    if (paramInt1 + 16 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 16 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this.forEncryption)
    {
      unpackBlock(paramArrayOfByte1, paramInt1);
      encryptBlock(this.WorkingKey);
      packBlock(paramArrayOfByte2, paramInt2);
    }
    while (true)
    {
      return 16;
      unpackBlock(paramArrayOfByte1, paramInt1);
      decryptBlock(this.WorkingKey);
      packBlock(paramArrayOfByte2, paramInt2);
    }
  }

  public void reset()
  {
  }
}