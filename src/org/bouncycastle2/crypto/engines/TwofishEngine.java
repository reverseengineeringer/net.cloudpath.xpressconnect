package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public final class TwofishEngine
  implements BlockCipher
{
  private static final int BLOCK_SIZE = 16;
  private static final int GF256_FDBK = 361;
  private static final int GF256_FDBK_2 = 180;
  private static final int GF256_FDBK_4 = 90;
  private static final int INPUT_WHITEN = 0;
  private static final int MAX_KEY_BITS = 256;
  private static final int MAX_ROUNDS = 16;
  private static final int OUTPUT_WHITEN = 4;
  private static final byte[][] P = arrayOfByte;
  private static final int P_00 = 1;
  private static final int P_01 = 0;
  private static final int P_02 = 0;
  private static final int P_03 = 1;
  private static final int P_04 = 1;
  private static final int P_10 = 0;
  private static final int P_11 = 0;
  private static final int P_12 = 1;
  private static final int P_13 = 1;
  private static final int P_14 = 0;
  private static final int P_20 = 1;
  private static final int P_21 = 1;
  private static final int P_22 = 0;
  private static final int P_23 = 0;
  private static final int P_24 = 0;
  private static final int P_30 = 0;
  private static final int P_31 = 1;
  private static final int P_32 = 1;
  private static final int P_33 = 0;
  private static final int P_34 = 1;
  private static final int ROUNDS = 16;
  private static final int ROUND_SUBKEYS = 8;
  private static final int RS_GF_FDBK = 333;
  private static final int SK_BUMP = 16843009;
  private static final int SK_ROTL = 9;
  private static final int SK_STEP = 33686018;
  private static final int TOTAL_SUBKEYS = 40;
  private boolean encrypting = false;
  private int[] gMDS0 = new int[256];
  private int[] gMDS1 = new int[256];
  private int[] gMDS2 = new int[256];
  private int[] gMDS3 = new int[256];
  private int[] gSBox;
  private int[] gSubKeys;
  private int k64Cnt = 0;
  private byte[] workingKey = null;

  static
  {
    byte[][] arrayOfByte = new byte[2][];
    byte[] arrayOfByte1 = new byte[256];
    arrayOfByte1[0] = -87;
    arrayOfByte1[1] = 103;
    arrayOfByte1[2] = -77;
    arrayOfByte1[3] = -24;
    arrayOfByte1[4] = 4;
    arrayOfByte1[5] = -3;
    arrayOfByte1[6] = -93;
    arrayOfByte1[7] = 118;
    arrayOfByte1[8] = -102;
    arrayOfByte1[9] = -110;
    arrayOfByte1[10] = -128;
    arrayOfByte1[11] = 120;
    arrayOfByte1[12] = -28;
    arrayOfByte1[13] = -35;
    arrayOfByte1[14] = -47;
    arrayOfByte1[15] = 56;
    arrayOfByte1[16] = 13;
    arrayOfByte1[17] = -58;
    arrayOfByte1[18] = 53;
    arrayOfByte1[19] = -104;
    arrayOfByte1[20] = 24;
    arrayOfByte1[21] = -9;
    arrayOfByte1[22] = -20;
    arrayOfByte1[23] = 108;
    arrayOfByte1[24] = 67;
    arrayOfByte1[25] = 117;
    arrayOfByte1[26] = 55;
    arrayOfByte1[27] = 38;
    arrayOfByte1[28] = -6;
    arrayOfByte1[29] = 19;
    arrayOfByte1[30] = -108;
    arrayOfByte1[31] = 72;
    arrayOfByte1[32] = -14;
    arrayOfByte1[33] = -48;
    arrayOfByte1[34] = -117;
    arrayOfByte1[35] = 48;
    arrayOfByte1[36] = -124;
    arrayOfByte1[37] = 84;
    arrayOfByte1[38] = -33;
    arrayOfByte1[39] = 35;
    arrayOfByte1[40] = 25;
    arrayOfByte1[41] = 91;
    arrayOfByte1[42] = 61;
    arrayOfByte1[43] = 89;
    arrayOfByte1[44] = -13;
    arrayOfByte1[45] = -82;
    arrayOfByte1[46] = -94;
    arrayOfByte1[47] = -126;
    arrayOfByte1[48] = 99;
    arrayOfByte1[49] = 1;
    arrayOfByte1[50] = -125;
    arrayOfByte1[51] = 46;
    arrayOfByte1[52] = -39;
    arrayOfByte1[53] = 81;
    arrayOfByte1[54] = -101;
    arrayOfByte1[55] = 124;
    arrayOfByte1[56] = -90;
    arrayOfByte1[57] = -21;
    arrayOfByte1[58] = -91;
    arrayOfByte1[59] = -66;
    arrayOfByte1[60] = 22;
    arrayOfByte1[61] = 12;
    arrayOfByte1[62] = -29;
    arrayOfByte1[63] = 97;
    arrayOfByte1[64] = -64;
    arrayOfByte1[65] = -116;
    arrayOfByte1[66] = 58;
    arrayOfByte1[67] = -11;
    arrayOfByte1[68] = 115;
    arrayOfByte1[69] = 44;
    arrayOfByte1[70] = 37;
    arrayOfByte1[71] = 11;
    arrayOfByte1[72] = -69;
    arrayOfByte1[73] = 78;
    arrayOfByte1[74] = -119;
    arrayOfByte1[75] = 107;
    arrayOfByte1[76] = 83;
    arrayOfByte1[77] = 106;
    arrayOfByte1[78] = -76;
    arrayOfByte1[79] = -15;
    arrayOfByte1[80] = -31;
    arrayOfByte1[81] = -26;
    arrayOfByte1[82] = -67;
    arrayOfByte1[83] = 69;
    arrayOfByte1[84] = -30;
    arrayOfByte1[85] = -12;
    arrayOfByte1[86] = -74;
    arrayOfByte1[87] = 102;
    arrayOfByte1[88] = -52;
    arrayOfByte1[89] = -107;
    arrayOfByte1[90] = 3;
    arrayOfByte1[91] = 86;
    arrayOfByte1[92] = -44;
    arrayOfByte1[93] = 28;
    arrayOfByte1[94] = 30;
    arrayOfByte1[95] = -41;
    arrayOfByte1[96] = -5;
    arrayOfByte1[97] = -61;
    arrayOfByte1[98] = -114;
    arrayOfByte1[99] = -75;
    arrayOfByte1[100] = -23;
    arrayOfByte1[101] = -49;
    arrayOfByte1[102] = -65;
    arrayOfByte1[103] = -70;
    arrayOfByte1[104] = -22;
    arrayOfByte1[105] = 119;
    arrayOfByte1[106] = 57;
    arrayOfByte1[107] = -81;
    arrayOfByte1[108] = 51;
    arrayOfByte1[109] = -55;
    arrayOfByte1[110] = 98;
    arrayOfByte1[111] = 113;
    arrayOfByte1[112] = -127;
    arrayOfByte1[113] = 121;
    arrayOfByte1[114] = 9;
    arrayOfByte1[115] = -83;
    arrayOfByte1[116] = 36;
    arrayOfByte1[117] = -51;
    arrayOfByte1[118] = -7;
    arrayOfByte1[119] = -40;
    arrayOfByte1[120] = -27;
    arrayOfByte1[121] = -59;
    arrayOfByte1[122] = -71;
    arrayOfByte1[123] = 77;
    arrayOfByte1[124] = 68;
    arrayOfByte1[125] = 8;
    arrayOfByte1[126] = -122;
    arrayOfByte1[127] = -25;
    arrayOfByte1[''] = -95;
    arrayOfByte1[''] = 29;
    arrayOfByte1[''] = -86;
    arrayOfByte1[''] = -19;
    arrayOfByte1[''] = 6;
    arrayOfByte1[''] = 112;
    arrayOfByte1[''] = -78;
    arrayOfByte1[''] = -46;
    arrayOfByte1[''] = 65;
    arrayOfByte1[''] = 123;
    arrayOfByte1[''] = -96;
    arrayOfByte1[''] = 17;
    arrayOfByte1[''] = 49;
    arrayOfByte1[''] = -62;
    arrayOfByte1[''] = 39;
    arrayOfByte1[''] = -112;
    arrayOfByte1[''] = 32;
    arrayOfByte1[''] = -10;
    arrayOfByte1[''] = 96;
    arrayOfByte1[''] = -1;
    arrayOfByte1[''] = -106;
    arrayOfByte1[''] = 92;
    arrayOfByte1[''] = -79;
    arrayOfByte1[''] = -85;
    arrayOfByte1[''] = -98;
    arrayOfByte1[''] = -100;
    arrayOfByte1[''] = 82;
    arrayOfByte1[''] = 27;
    arrayOfByte1[''] = 95;
    arrayOfByte1[''] = -109;
    arrayOfByte1[''] = 10;
    arrayOfByte1[''] = -17;
    arrayOfByte1[' '] = -111;
    arrayOfByte1['¡'] = -123;
    arrayOfByte1['¢'] = 73;
    arrayOfByte1['£'] = -18;
    arrayOfByte1['¤'] = 45;
    arrayOfByte1['¥'] = 79;
    arrayOfByte1['¦'] = -113;
    arrayOfByte1['§'] = 59;
    arrayOfByte1['¨'] = 71;
    arrayOfByte1['©'] = -121;
    arrayOfByte1['ª'] = 109;
    arrayOfByte1['«'] = 70;
    arrayOfByte1['¬'] = -42;
    arrayOfByte1['­'] = 62;
    arrayOfByte1['®'] = 105;
    arrayOfByte1['¯'] = 100;
    arrayOfByte1['°'] = 42;
    arrayOfByte1['±'] = -50;
    arrayOfByte1['²'] = -53;
    arrayOfByte1['³'] = 47;
    arrayOfByte1['´'] = -4;
    arrayOfByte1['µ'] = -105;
    arrayOfByte1['¶'] = 5;
    arrayOfByte1['·'] = 122;
    arrayOfByte1['¸'] = -84;
    arrayOfByte1['¹'] = 127;
    arrayOfByte1['º'] = -43;
    arrayOfByte1['»'] = 26;
    arrayOfByte1['¼'] = 75;
    arrayOfByte1['½'] = 14;
    arrayOfByte1['¾'] = -89;
    arrayOfByte1['¿'] = 90;
    arrayOfByte1['À'] = 40;
    arrayOfByte1['Á'] = 20;
    arrayOfByte1['Â'] = 63;
    arrayOfByte1['Ã'] = 41;
    arrayOfByte1['Ä'] = -120;
    arrayOfByte1['Å'] = 60;
    arrayOfByte1['Æ'] = 76;
    arrayOfByte1['Ç'] = 2;
    arrayOfByte1['È'] = -72;
    arrayOfByte1['É'] = -38;
    arrayOfByte1['Ê'] = -80;
    arrayOfByte1['Ë'] = 23;
    arrayOfByte1['Ì'] = 85;
    arrayOfByte1['Í'] = 31;
    arrayOfByte1['Î'] = -118;
    arrayOfByte1['Ï'] = 125;
    arrayOfByte1['Ð'] = 87;
    arrayOfByte1['Ñ'] = -57;
    arrayOfByte1['Ò'] = -115;
    arrayOfByte1['Ó'] = 116;
    arrayOfByte1['Ô'] = -73;
    arrayOfByte1['Õ'] = -60;
    arrayOfByte1['Ö'] = -97;
    arrayOfByte1['×'] = 114;
    arrayOfByte1['Ø'] = 126;
    arrayOfByte1['Ù'] = 21;
    arrayOfByte1['Ú'] = 34;
    arrayOfByte1['Û'] = 18;
    arrayOfByte1['Ü'] = 88;
    arrayOfByte1['Ý'] = 7;
    arrayOfByte1['Þ'] = -103;
    arrayOfByte1['ß'] = 52;
    arrayOfByte1['à'] = 110;
    arrayOfByte1['á'] = 80;
    arrayOfByte1['â'] = -34;
    arrayOfByte1['ã'] = 104;
    arrayOfByte1['ä'] = 101;
    arrayOfByte1['å'] = -68;
    arrayOfByte1['æ'] = -37;
    arrayOfByte1['ç'] = -8;
    arrayOfByte1['è'] = -56;
    arrayOfByte1['é'] = -88;
    arrayOfByte1['ê'] = 43;
    arrayOfByte1['ë'] = 64;
    arrayOfByte1['ì'] = -36;
    arrayOfByte1['í'] = -2;
    arrayOfByte1['î'] = 50;
    arrayOfByte1['ï'] = -92;
    arrayOfByte1['ð'] = -54;
    arrayOfByte1['ñ'] = 16;
    arrayOfByte1['ò'] = 33;
    arrayOfByte1['ó'] = -16;
    arrayOfByte1['ô'] = -45;
    arrayOfByte1['õ'] = 93;
    arrayOfByte1['ö'] = 15;
    arrayOfByte1['ø'] = 111;
    arrayOfByte1['ù'] = -99;
    arrayOfByte1['ú'] = 54;
    arrayOfByte1['û'] = 66;
    arrayOfByte1['ü'] = 74;
    arrayOfByte1['ý'] = 94;
    arrayOfByte1['þ'] = -63;
    arrayOfByte1['ÿ'] = -32;
    arrayOfByte[0] = arrayOfByte1;
    byte[] arrayOfByte2 = new byte[256];
    arrayOfByte2[0] = 117;
    arrayOfByte2[1] = -13;
    arrayOfByte2[2] = -58;
    arrayOfByte2[3] = -12;
    arrayOfByte2[4] = -37;
    arrayOfByte2[5] = 123;
    arrayOfByte2[6] = -5;
    arrayOfByte2[7] = -56;
    arrayOfByte2[8] = 74;
    arrayOfByte2[9] = -45;
    arrayOfByte2[10] = -26;
    arrayOfByte2[11] = 107;
    arrayOfByte2[12] = 69;
    arrayOfByte2[13] = 125;
    arrayOfByte2[14] = -24;
    arrayOfByte2[15] = 75;
    arrayOfByte2[16] = -42;
    arrayOfByte2[17] = 50;
    arrayOfByte2[18] = -40;
    arrayOfByte2[19] = -3;
    arrayOfByte2[20] = 55;
    arrayOfByte2[21] = 113;
    arrayOfByte2[22] = -15;
    arrayOfByte2[23] = -31;
    arrayOfByte2[24] = 48;
    arrayOfByte2[25] = 15;
    arrayOfByte2[26] = -8;
    arrayOfByte2[27] = 27;
    arrayOfByte2[28] = -121;
    arrayOfByte2[29] = -6;
    arrayOfByte2[30] = 6;
    arrayOfByte2[31] = 63;
    arrayOfByte2[32] = 94;
    arrayOfByte2[33] = -70;
    arrayOfByte2[34] = -82;
    arrayOfByte2[35] = 91;
    arrayOfByte2[36] = -118;
    arrayOfByte2[38] = -68;
    arrayOfByte2[39] = -99;
    arrayOfByte2[40] = 109;
    arrayOfByte2[41] = -63;
    arrayOfByte2[42] = -79;
    arrayOfByte2[43] = 14;
    arrayOfByte2[44] = -128;
    arrayOfByte2[45] = 93;
    arrayOfByte2[46] = -46;
    arrayOfByte2[47] = -43;
    arrayOfByte2[48] = -96;
    arrayOfByte2[49] = -124;
    arrayOfByte2[50] = 7;
    arrayOfByte2[51] = 20;
    arrayOfByte2[52] = -75;
    arrayOfByte2[53] = -112;
    arrayOfByte2[54] = 44;
    arrayOfByte2[55] = -93;
    arrayOfByte2[56] = -78;
    arrayOfByte2[57] = 115;
    arrayOfByte2[58] = 76;
    arrayOfByte2[59] = 84;
    arrayOfByte2[60] = -110;
    arrayOfByte2[61] = 116;
    arrayOfByte2[62] = 54;
    arrayOfByte2[63] = 81;
    arrayOfByte2[64] = 56;
    arrayOfByte2[65] = -80;
    arrayOfByte2[66] = -67;
    arrayOfByte2[67] = 90;
    arrayOfByte2[68] = -4;
    arrayOfByte2[69] = 96;
    arrayOfByte2[70] = 98;
    arrayOfByte2[71] = -106;
    arrayOfByte2[72] = 108;
    arrayOfByte2[73] = 66;
    arrayOfByte2[74] = -9;
    arrayOfByte2[75] = 16;
    arrayOfByte2[76] = 124;
    arrayOfByte2[77] = 40;
    arrayOfByte2[78] = 39;
    arrayOfByte2[79] = -116;
    arrayOfByte2[80] = 19;
    arrayOfByte2[81] = -107;
    arrayOfByte2[82] = -100;
    arrayOfByte2[83] = -57;
    arrayOfByte2[84] = 36;
    arrayOfByte2[85] = 70;
    arrayOfByte2[86] = 59;
    arrayOfByte2[87] = 112;
    arrayOfByte2[88] = -54;
    arrayOfByte2[89] = -29;
    arrayOfByte2[90] = -123;
    arrayOfByte2[91] = -53;
    arrayOfByte2[92] = 17;
    arrayOfByte2[93] = -48;
    arrayOfByte2[94] = -109;
    arrayOfByte2[95] = -72;
    arrayOfByte2[96] = -90;
    arrayOfByte2[97] = -125;
    arrayOfByte2[98] = 32;
    arrayOfByte2[99] = -1;
    arrayOfByte2[100] = -97;
    arrayOfByte2[101] = 119;
    arrayOfByte2[102] = -61;
    arrayOfByte2[103] = -52;
    arrayOfByte2[104] = 3;
    arrayOfByte2[105] = 111;
    arrayOfByte2[106] = 8;
    arrayOfByte2[107] = -65;
    arrayOfByte2[108] = 64;
    arrayOfByte2[109] = -25;
    arrayOfByte2[110] = 43;
    arrayOfByte2[111] = -30;
    arrayOfByte2[112] = 121;
    arrayOfByte2[113] = 12;
    arrayOfByte2[114] = -86;
    arrayOfByte2[115] = -126;
    arrayOfByte2[116] = 65;
    arrayOfByte2[117] = 58;
    arrayOfByte2[118] = -22;
    arrayOfByte2[119] = -71;
    arrayOfByte2[120] = -28;
    arrayOfByte2[121] = -102;
    arrayOfByte2[122] = -92;
    arrayOfByte2[123] = -105;
    arrayOfByte2[124] = 126;
    arrayOfByte2[125] = -38;
    arrayOfByte2[126] = 122;
    arrayOfByte2[127] = 23;
    arrayOfByte2[''] = 102;
    arrayOfByte2[''] = -108;
    arrayOfByte2[''] = -95;
    arrayOfByte2[''] = 29;
    arrayOfByte2[''] = 61;
    arrayOfByte2[''] = -16;
    arrayOfByte2[''] = -34;
    arrayOfByte2[''] = -77;
    arrayOfByte2[''] = 11;
    arrayOfByte2[''] = 114;
    arrayOfByte2[''] = -89;
    arrayOfByte2[''] = 28;
    arrayOfByte2[''] = -17;
    arrayOfByte2[''] = -47;
    arrayOfByte2[''] = 83;
    arrayOfByte2[''] = 62;
    arrayOfByte2[''] = -113;
    arrayOfByte2[''] = 51;
    arrayOfByte2[''] = 38;
    arrayOfByte2[''] = 95;
    arrayOfByte2[''] = -20;
    arrayOfByte2[''] = 118;
    arrayOfByte2[''] = 42;
    arrayOfByte2[''] = 73;
    arrayOfByte2[''] = -127;
    arrayOfByte2[''] = -120;
    arrayOfByte2[''] = -18;
    arrayOfByte2[''] = 33;
    arrayOfByte2[''] = -60;
    arrayOfByte2[''] = 26;
    arrayOfByte2[''] = -21;
    arrayOfByte2[''] = -39;
    arrayOfByte2[' '] = -59;
    arrayOfByte2['¡'] = 57;
    arrayOfByte2['¢'] = -103;
    arrayOfByte2['£'] = -51;
    arrayOfByte2['¤'] = -83;
    arrayOfByte2['¥'] = 49;
    arrayOfByte2['¦'] = -117;
    arrayOfByte2['§'] = 1;
    arrayOfByte2['¨'] = 24;
    arrayOfByte2['©'] = 35;
    arrayOfByte2['ª'] = -35;
    arrayOfByte2['«'] = 31;
    arrayOfByte2['¬'] = 78;
    arrayOfByte2['­'] = 45;
    arrayOfByte2['®'] = -7;
    arrayOfByte2['¯'] = 72;
    arrayOfByte2['°'] = 79;
    arrayOfByte2['±'] = -14;
    arrayOfByte2['²'] = 101;
    arrayOfByte2['³'] = -114;
    arrayOfByte2['´'] = 120;
    arrayOfByte2['µ'] = 92;
    arrayOfByte2['¶'] = 88;
    arrayOfByte2['·'] = 25;
    arrayOfByte2['¸'] = -115;
    arrayOfByte2['¹'] = -27;
    arrayOfByte2['º'] = -104;
    arrayOfByte2['»'] = 87;
    arrayOfByte2['¼'] = 103;
    arrayOfByte2['½'] = 127;
    arrayOfByte2['¾'] = 5;
    arrayOfByte2['¿'] = 100;
    arrayOfByte2['À'] = -81;
    arrayOfByte2['Á'] = 99;
    arrayOfByte2['Â'] = -74;
    arrayOfByte2['Ã'] = -2;
    arrayOfByte2['Ä'] = -11;
    arrayOfByte2['Å'] = -73;
    arrayOfByte2['Æ'] = 60;
    arrayOfByte2['Ç'] = -91;
    arrayOfByte2['È'] = -50;
    arrayOfByte2['É'] = -23;
    arrayOfByte2['Ê'] = 104;
    arrayOfByte2['Ë'] = 68;
    arrayOfByte2['Ì'] = -32;
    arrayOfByte2['Í'] = 77;
    arrayOfByte2['Î'] = 67;
    arrayOfByte2['Ï'] = 105;
    arrayOfByte2['Ð'] = 41;
    arrayOfByte2['Ñ'] = 46;
    arrayOfByte2['Ò'] = -84;
    arrayOfByte2['Ó'] = 21;
    arrayOfByte2['Ô'] = 89;
    arrayOfByte2['Õ'] = -88;
    arrayOfByte2['Ö'] = 10;
    arrayOfByte2['×'] = -98;
    arrayOfByte2['Ø'] = 110;
    arrayOfByte2['Ù'] = 71;
    arrayOfByte2['Ú'] = -33;
    arrayOfByte2['Û'] = 52;
    arrayOfByte2['Ü'] = 53;
    arrayOfByte2['Ý'] = 106;
    arrayOfByte2['Þ'] = -49;
    arrayOfByte2['ß'] = -36;
    arrayOfByte2['à'] = 34;
    arrayOfByte2['á'] = -55;
    arrayOfByte2['â'] = -64;
    arrayOfByte2['ã'] = -101;
    arrayOfByte2['ä'] = -119;
    arrayOfByte2['å'] = -44;
    arrayOfByte2['æ'] = -19;
    arrayOfByte2['ç'] = -85;
    arrayOfByte2['è'] = 18;
    arrayOfByte2['é'] = -94;
    arrayOfByte2['ê'] = 13;
    arrayOfByte2['ë'] = 82;
    arrayOfByte2['ì'] = -69;
    arrayOfByte2['í'] = 2;
    arrayOfByte2['î'] = 47;
    arrayOfByte2['ï'] = -87;
    arrayOfByte2['ð'] = -41;
    arrayOfByte2['ñ'] = 97;
    arrayOfByte2['ò'] = 30;
    arrayOfByte2['ó'] = -76;
    arrayOfByte2['ô'] = 80;
    arrayOfByte2['õ'] = 4;
    arrayOfByte2['ö'] = -10;
    arrayOfByte2['÷'] = -62;
    arrayOfByte2['ø'] = 22;
    arrayOfByte2['ù'] = 37;
    arrayOfByte2['ú'] = -122;
    arrayOfByte2['û'] = 86;
    arrayOfByte2['ü'] = 85;
    arrayOfByte2['ý'] = 9;
    arrayOfByte2['þ'] = -66;
    arrayOfByte2['ÿ'] = -111;
    arrayOfByte[1] = arrayOfByte2;
  }

  public TwofishEngine()
  {
    int[] arrayOfInt1 = new int[2];
    int[] arrayOfInt2 = new int[2];
    int[] arrayOfInt3 = new int[2];
    for (int i = 0; ; i++)
    {
      if (i >= 256)
        return;
      int j = 0xFF & P[0][i];
      arrayOfInt1[0] = j;
      arrayOfInt2[0] = (0xFF & Mx_X(j));
      arrayOfInt3[0] = (0xFF & Mx_Y(j));
      int k = 0xFF & P[1][i];
      arrayOfInt1[1] = k;
      arrayOfInt2[1] = (0xFF & Mx_X(k));
      arrayOfInt3[1] = (0xFF & Mx_Y(k));
      this.gMDS0[i] = (arrayOfInt1[1] | arrayOfInt2[1] << 8 | arrayOfInt3[1] << 16 | arrayOfInt3[1] << 24);
      this.gMDS1[i] = (arrayOfInt3[0] | arrayOfInt3[0] << 8 | arrayOfInt2[0] << 16 | arrayOfInt1[0] << 24);
      this.gMDS2[i] = (arrayOfInt2[1] | arrayOfInt3[1] << 8 | arrayOfInt1[1] << 16 | arrayOfInt3[1] << 24);
      this.gMDS3[i] = (arrayOfInt2[0] | arrayOfInt1[0] << 8 | arrayOfInt3[0] << 16 | arrayOfInt2[0] << 24);
    }
  }

  private void Bits32ToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 8));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >> 16));
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >> 24));
  }

  private int BytesTo32Bits(byte[] paramArrayOfByte, int paramInt)
  {
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
  }

  private int F32(int paramInt, int[] paramArrayOfInt)
  {
    int i = b0(paramInt);
    int j = b1(paramInt);
    int k = b2(paramInt);
    int m = b3(paramInt);
    int n = paramArrayOfInt[0];
    int i1 = paramArrayOfInt[1];
    int i2 = paramArrayOfInt[2];
    int i3 = paramArrayOfInt[3];
    switch (0x3 & this.k64Cnt)
    {
    default:
      return 0;
    case 1:
      return this.gMDS0[(0xFF & P[0][i] ^ b0(n))] ^ this.gMDS1[(0xFF & P[0][j] ^ b1(n))] ^ this.gMDS2[(0xFF & P[1][k] ^ b2(n))] ^ this.gMDS3[(0xFF & P[1][m] ^ b3(n))];
    case 0:
      i = 0xFF & P[1][i] ^ b0(i3);
      j = 0xFF & P[0][j] ^ b1(i3);
      k = 0xFF & P[0][k] ^ b2(i3);
      m = 0xFF & P[1][m] ^ b3(i3);
    case 3:
      i = 0xFF & P[1][i] ^ b0(i2);
      j = 0xFF & P[1][j] ^ b1(i2);
      k = 0xFF & P[0][k] ^ b2(i2);
      m = 0xFF & P[0][m] ^ b3(i2);
    case 2:
    }
    return this.gMDS0[(0xFF & P[0][(0xFF & P[0][i] ^ b0(i1))] ^ b0(n))] ^ this.gMDS1[(0xFF & P[0][(0xFF & P[1][j] ^ b1(i1))] ^ b1(n))] ^ this.gMDS2[(0xFF & P[1][(0xFF & P[0][k] ^ b2(i1))] ^ b2(n))] ^ this.gMDS3[(0xFF & P[1][(0xFF & P[1][m] ^ b3(i1))] ^ b3(n))];
  }

  private int Fe32_0(int paramInt)
  {
    return this.gSBox[(0 + 2 * (paramInt & 0xFF))] ^ this.gSBox[(1 + 2 * (0xFF & paramInt >>> 8))] ^ this.gSBox[(512 + 2 * (0xFF & paramInt >>> 16))] ^ this.gSBox[(513 + 2 * (0xFF & paramInt >>> 24))];
  }

  private int Fe32_3(int paramInt)
  {
    return this.gSBox[(0 + 2 * (0xFF & paramInt >>> 24))] ^ this.gSBox[(1 + 2 * (paramInt & 0xFF))] ^ this.gSBox[(512 + 2 * (0xFF & paramInt >>> 8))] ^ this.gSBox[(513 + 2 * (0xFF & paramInt >>> 16))];
  }

  private int LFSR1(int paramInt)
  {
    int i = paramInt >> 1;
    if ((paramInt & 0x1) != 0);
    for (int j = 180; ; j = 0)
      return j ^ i;
  }

  private int LFSR2(int paramInt)
  {
    int i = paramInt >> 2;
    if ((paramInt & 0x2) != 0);
    for (int j = 180; ; j = 0)
    {
      int k = j ^ i;
      int m = paramInt & 0x1;
      int n = 0;
      if (m != 0)
        n = 90;
      return n ^ k;
    }
  }

  private int Mx_X(int paramInt)
  {
    return paramInt ^ LFSR2(paramInt);
  }

  private int Mx_Y(int paramInt)
  {
    return paramInt ^ LFSR1(paramInt) ^ LFSR2(paramInt);
  }

  private int RS_MDS_Encode(int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    int j = 0;
    int k;
    if (j >= 4)
      k = i ^ paramInt1;
    for (int m = 0; ; m++)
    {
      if (m >= 4)
      {
        return k;
        i = RS_rem(i);
        j++;
        break;
      }
      k = RS_rem(k);
    }
  }

  private int RS_rem(int paramInt)
  {
    int i = 0xFF & paramInt >>> 24;
    int j = i << 1;
    if ((i & 0x80) != 0);
    for (int k = 333; ; k = 0)
    {
      int m = 0xFF & (k ^ j);
      int n = i >>> 1;
      int i1 = i & 0x1;
      int i2 = 0;
      if (i1 != 0)
        i2 = 166;
      int i3 = m ^ (i2 ^ n);
      return i ^ (paramInt << 8 ^ i3 << 24 ^ m << 16 ^ i3 << 8);
    }
  }

  private int b0(int paramInt)
  {
    return paramInt & 0xFF;
  }

  private int b1(int paramInt)
  {
    return 0xFF & paramInt >>> 8;
  }

  private int b2(int paramInt)
  {
    return 0xFF & paramInt >>> 16;
  }

  private int b3(int paramInt)
  {
    return 0xFF & paramInt >>> 24;
  }

  private void decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = BytesTo32Bits(paramArrayOfByte1, paramInt1) ^ this.gSubKeys[4];
    int j = BytesTo32Bits(paramArrayOfByte1, paramInt1 + 4) ^ this.gSubKeys[5];
    int k = BytesTo32Bits(paramArrayOfByte1, paramInt1 + 8) ^ this.gSubKeys[6];
    int m = BytesTo32Bits(paramArrayOfByte1, paramInt1 + 12) ^ this.gSubKeys[7];
    int n = 0;
    int i1 = 39;
    while (true)
    {
      if (n >= 16)
      {
        Bits32ToBytes(k ^ this.gSubKeys[0], paramArrayOfByte2, paramInt2);
        Bits32ToBytes(m ^ this.gSubKeys[1], paramArrayOfByte2, paramInt2 + 4);
        Bits32ToBytes(i ^ this.gSubKeys[2], paramArrayOfByte2, paramInt2 + 8);
        Bits32ToBytes(j ^ this.gSubKeys[3], paramArrayOfByte2, paramInt2 + 12);
        return;
      }
      int i2 = Fe32_0(i);
      int i3 = Fe32_3(j);
      int i4 = i2 + i3 * 2;
      int[] arrayOfInt1 = this.gSubKeys;
      int i5 = i1 - 1;
      int i6 = m ^ i4 + arrayOfInt1[i1];
      int i7 = k << 1 | k >>> 31;
      int i8 = i2 + i3;
      int[] arrayOfInt2 = this.gSubKeys;
      int i9 = i5 - 1;
      k = i7 ^ i8 + arrayOfInt2[i5];
      m = i6 >>> 1 | i6 << 31;
      int i10 = Fe32_0(k);
      int i11 = Fe32_3(m);
      int i12 = i10 + i11 * 2;
      int[] arrayOfInt3 = this.gSubKeys;
      int i13 = i9 - 1;
      int i14 = j ^ i12 + arrayOfInt3[i9];
      int i15 = i << 1 | i >>> 31;
      int i16 = i10 + i11;
      int[] arrayOfInt4 = this.gSubKeys;
      i1 = i13 - 1;
      i = i15 ^ i16 + arrayOfInt4[i13];
      j = i14 >>> 1 | i14 << 31;
      n += 2;
    }
  }

  private void encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = BytesTo32Bits(paramArrayOfByte1, paramInt1) ^ this.gSubKeys[0];
    int j = BytesTo32Bits(paramArrayOfByte1, paramInt1 + 4) ^ this.gSubKeys[1];
    int k = BytesTo32Bits(paramArrayOfByte1, paramInt1 + 8) ^ this.gSubKeys[2];
    int m = BytesTo32Bits(paramArrayOfByte1, paramInt1 + 12) ^ this.gSubKeys[3];
    int n = 0;
    int i1 = 8;
    while (true)
    {
      if (n >= 16)
      {
        Bits32ToBytes(k ^ this.gSubKeys[4], paramArrayOfByte2, paramInt2);
        Bits32ToBytes(m ^ this.gSubKeys[5], paramArrayOfByte2, paramInt2 + 4);
        Bits32ToBytes(i ^ this.gSubKeys[6], paramArrayOfByte2, paramInt2 + 8);
        Bits32ToBytes(j ^ this.gSubKeys[7], paramArrayOfByte2, paramInt2 + 12);
        return;
      }
      int i2 = Fe32_0(i);
      int i3 = Fe32_3(j);
      int i4 = i2 + i3;
      int[] arrayOfInt1 = this.gSubKeys;
      int i5 = i1 + 1;
      int i6 = k ^ i4 + arrayOfInt1[i1];
      k = i6 >>> 1 | i6 << 31;
      int i7 = m << 1 | m >>> 31;
      int i8 = i2 + i3 * 2;
      int[] arrayOfInt2 = this.gSubKeys;
      int i9 = i5 + 1;
      m = i7 ^ i8 + arrayOfInt2[i5];
      int i10 = Fe32_0(k);
      int i11 = Fe32_3(m);
      int i12 = i10 + i11;
      int[] arrayOfInt3 = this.gSubKeys;
      int i13 = i9 + 1;
      int i14 = i ^ i12 + arrayOfInt3[i9];
      i = i14 >>> 1 | i14 << 31;
      int i15 = j << 1 | j >>> 31;
      int i16 = i10 + i11 * 2;
      int[] arrayOfInt4 = this.gSubKeys;
      i1 = i13 + 1;
      j = i15 ^ i16 + arrayOfInt4[i13];
      n += 2;
    }
  }

  private void setKey(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt1 = new int[4];
    int[] arrayOfInt2 = new int[4];
    int[] arrayOfInt3 = new int[4];
    this.gSubKeys = new int[40];
    if (this.k64Cnt < 1)
      throw new IllegalArgumentException("Key size less than 64 bits");
    if (this.k64Cnt > 4)
      throw new IllegalArgumentException("Key size larger than 256 bits");
    int i = 0;
    if (i >= this.k64Cnt);
    int i5;
    int i6;
    int i7;
    int i8;
    int i9;
    for (int k = 0; ; k++)
    {
      if (k >= 20)
      {
        i5 = arrayOfInt3[0];
        i6 = arrayOfInt3[1];
        i7 = arrayOfInt3[2];
        i8 = arrayOfInt3[3];
        this.gSBox = new int[1024];
        i9 = 0;
        if (i9 < 256)
          break label279;
        return;
        int j = i * 8;
        arrayOfInt1[i] = BytesTo32Bits(paramArrayOfByte, j);
        arrayOfInt2[i] = BytesTo32Bits(paramArrayOfByte, j + 4);
        arrayOfInt3[(-1 + this.k64Cnt - i)] = RS_MDS_Encode(arrayOfInt1[i], arrayOfInt2[i]);
        i++;
        break;
      }
      int m = k * 33686018;
      int n = F32(m, arrayOfInt1);
      int i1 = F32(16843009 + m, arrayOfInt2);
      int i2 = i1 << 8 | i1 >>> 24;
      int i3 = n + i2;
      this.gSubKeys[(k * 2)] = i3;
      int i4 = i3 + i2;
      this.gSubKeys[(1 + k * 2)] = (i4 << 9 | i4 >>> 23);
    }
    label279: int i10 = i9;
    int i11 = i9;
    int i12 = i9;
    int i13 = i9;
    switch (0x3 & this.k64Cnt)
    {
    default:
    case 1:
    case 0:
    case 3:
    case 2:
    }
    while (true)
    {
      i9++;
      break;
      this.gSBox[(i9 * 2)] = this.gMDS0[(0xFF & P[0][i13] ^ b0(i5))];
      this.gSBox[(1 + i9 * 2)] = this.gMDS1[(0xFF & P[0][i12] ^ b1(i5))];
      this.gSBox[(512 + i9 * 2)] = this.gMDS2[(0xFF & P[1][i11] ^ b2(i5))];
      this.gSBox[(513 + i9 * 2)] = this.gMDS3[(0xFF & P[1][i10] ^ b3(i5))];
      continue;
      i13 = 0xFF & P[1][i13] ^ b0(i8);
      i12 = 0xFF & P[0][i12] ^ b1(i8);
      i11 = 0xFF & P[0][i11] ^ b2(i8);
      i10 = 0xFF & P[1][i10] ^ b3(i8);
      i13 = 0xFF & P[1][i13] ^ b0(i7);
      i12 = 0xFF & P[1][i12] ^ b1(i7);
      i11 = 0xFF & P[0][i11] ^ b2(i7);
      i10 = 0xFF & P[0][i10] ^ b3(i7);
      this.gSBox[(i9 * 2)] = this.gMDS0[(0xFF & P[0][(0xFF & P[0][i13] ^ b0(i6))] ^ b0(i5))];
      this.gSBox[(1 + i9 * 2)] = this.gMDS1[(0xFF & P[0][(0xFF & P[1][i12] ^ b1(i6))] ^ b1(i5))];
      this.gSBox[(512 + i9 * 2)] = this.gMDS2[(0xFF & P[1][(0xFF & P[0][i11] ^ b2(i6))] ^ b2(i5))];
      this.gSBox[(513 + i9 * 2)] = this.gMDS3[(0xFF & P[1][(0xFF & P[1][i10] ^ b3(i6))] ^ b3(i5))];
    }
  }

  public String getAlgorithmName()
  {
    return "Twofish";
  }

  public int getBlockSize()
  {
    return 16;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof KeyParameter))
    {
      this.encrypting = paramBoolean;
      this.workingKey = ((KeyParameter)paramCipherParameters).getKey();
      this.k64Cnt = (this.workingKey.length / 8);
      setKey(this.workingKey);
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to Twofish init - " + paramCipherParameters.getClass().getName());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.workingKey == null)
      throw new IllegalStateException("Twofish not initialised");
    if (paramInt1 + 16 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 16 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this.encrypting)
      encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    while (true)
    {
      return 16;
      decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    }
  }

  public void reset()
  {
    if (this.workingKey != null)
      setKey(this.workingKey);
  }
}