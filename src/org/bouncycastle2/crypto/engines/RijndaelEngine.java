package org.bouncycastle2.crypto.engines;

import java.lang.reflect.Array;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class RijndaelEngine
  implements BlockCipher
{
  private static final int MAXKC = 64;
  private static final int MAXROUNDS = 14;
  private static final byte[] S;
  private static final byte[] Si;
  private static final byte[] aLogtable;
  private static final byte[] logtable;
  private static final int[] rcon;
  static byte[][] shifts0;
  static byte[][] shifts1 = arrayOfByte11;
  private long A0;
  private long A1;
  private long A2;
  private long A3;
  private int BC;
  private long BC_MASK;
  private int ROUNDS;
  private int blockBits;
  private boolean forEncryption;
  private byte[] shifts0SC;
  private byte[] shifts1SC;
  private long[][] workingKey;

  static
  {
    byte[] arrayOfByte1 = new byte[256];
    arrayOfByte1[2] = 25;
    arrayOfByte1[3] = 1;
    arrayOfByte1[4] = 50;
    arrayOfByte1[5] = 2;
    arrayOfByte1[6] = 26;
    arrayOfByte1[7] = -58;
    arrayOfByte1[8] = 75;
    arrayOfByte1[9] = -57;
    arrayOfByte1[10] = 27;
    arrayOfByte1[11] = 104;
    arrayOfByte1[12] = 51;
    arrayOfByte1[13] = -18;
    arrayOfByte1[14] = -33;
    arrayOfByte1[15] = 3;
    arrayOfByte1[16] = 100;
    arrayOfByte1[17] = 4;
    arrayOfByte1[18] = -32;
    arrayOfByte1[19] = 14;
    arrayOfByte1[20] = 52;
    arrayOfByte1[21] = -115;
    arrayOfByte1[22] = -127;
    arrayOfByte1[23] = -17;
    arrayOfByte1[24] = 76;
    arrayOfByte1[25] = 113;
    arrayOfByte1[26] = 8;
    arrayOfByte1[27] = -56;
    arrayOfByte1[28] = -8;
    arrayOfByte1[29] = 105;
    arrayOfByte1[30] = 28;
    arrayOfByte1[31] = -63;
    arrayOfByte1[32] = 125;
    arrayOfByte1[33] = -62;
    arrayOfByte1[34] = 29;
    arrayOfByte1[35] = -75;
    arrayOfByte1[36] = -7;
    arrayOfByte1[37] = -71;
    arrayOfByte1[38] = 39;
    arrayOfByte1[39] = 106;
    arrayOfByte1[40] = 77;
    arrayOfByte1[41] = -28;
    arrayOfByte1[42] = -90;
    arrayOfByte1[43] = 114;
    arrayOfByte1[44] = -102;
    arrayOfByte1[45] = -55;
    arrayOfByte1[46] = 9;
    arrayOfByte1[47] = 120;
    arrayOfByte1[48] = 101;
    arrayOfByte1[49] = 47;
    arrayOfByte1[50] = -118;
    arrayOfByte1[51] = 5;
    arrayOfByte1[52] = 33;
    arrayOfByte1[53] = 15;
    arrayOfByte1[54] = -31;
    arrayOfByte1[55] = 36;
    arrayOfByte1[56] = 18;
    arrayOfByte1[57] = -16;
    arrayOfByte1[58] = -126;
    arrayOfByte1[59] = 69;
    arrayOfByte1[60] = 53;
    arrayOfByte1[61] = -109;
    arrayOfByte1[62] = -38;
    arrayOfByte1[63] = -114;
    arrayOfByte1[64] = -106;
    arrayOfByte1[65] = -113;
    arrayOfByte1[66] = -37;
    arrayOfByte1[67] = -67;
    arrayOfByte1[68] = 54;
    arrayOfByte1[69] = -48;
    arrayOfByte1[70] = -50;
    arrayOfByte1[71] = -108;
    arrayOfByte1[72] = 19;
    arrayOfByte1[73] = 92;
    arrayOfByte1[74] = -46;
    arrayOfByte1[75] = -15;
    arrayOfByte1[76] = 64;
    arrayOfByte1[77] = 70;
    arrayOfByte1[78] = -125;
    arrayOfByte1[79] = 56;
    arrayOfByte1[80] = 102;
    arrayOfByte1[81] = -35;
    arrayOfByte1[82] = -3;
    arrayOfByte1[83] = 48;
    arrayOfByte1[84] = -65;
    arrayOfByte1[85] = 6;
    arrayOfByte1[86] = -117;
    arrayOfByte1[87] = 98;
    arrayOfByte1[88] = -77;
    arrayOfByte1[89] = 37;
    arrayOfByte1[90] = -30;
    arrayOfByte1[91] = -104;
    arrayOfByte1[92] = 34;
    arrayOfByte1[93] = -120;
    arrayOfByte1[94] = -111;
    arrayOfByte1[95] = 16;
    arrayOfByte1[96] = 126;
    arrayOfByte1[97] = 110;
    arrayOfByte1[98] = 72;
    arrayOfByte1[99] = -61;
    arrayOfByte1[100] = -93;
    arrayOfByte1[101] = -74;
    arrayOfByte1[102] = 30;
    arrayOfByte1[103] = 66;
    arrayOfByte1[104] = 58;
    arrayOfByte1[105] = 107;
    arrayOfByte1[106] = 40;
    arrayOfByte1[107] = 84;
    arrayOfByte1[108] = -6;
    arrayOfByte1[109] = -123;
    arrayOfByte1[110] = 61;
    arrayOfByte1[111] = -70;
    arrayOfByte1[112] = 43;
    arrayOfByte1[113] = 121;
    arrayOfByte1[114] = 10;
    arrayOfByte1[115] = 21;
    arrayOfByte1[116] = -101;
    arrayOfByte1[117] = -97;
    arrayOfByte1[118] = 94;
    arrayOfByte1[119] = -54;
    arrayOfByte1[120] = 78;
    arrayOfByte1[121] = -44;
    arrayOfByte1[122] = -84;
    arrayOfByte1[123] = -27;
    arrayOfByte1[124] = -13;
    arrayOfByte1[125] = 115;
    arrayOfByte1[126] = -89;
    arrayOfByte1[127] = 87;
    arrayOfByte1[''] = -81;
    arrayOfByte1[''] = 88;
    arrayOfByte1[''] = -88;
    arrayOfByte1[''] = 80;
    arrayOfByte1[''] = -12;
    arrayOfByte1[''] = -22;
    arrayOfByte1[''] = -42;
    arrayOfByte1[''] = 116;
    arrayOfByte1[''] = 79;
    arrayOfByte1[''] = -82;
    arrayOfByte1[''] = -23;
    arrayOfByte1[''] = -43;
    arrayOfByte1[''] = -25;
    arrayOfByte1[''] = -26;
    arrayOfByte1[''] = -83;
    arrayOfByte1[''] = -24;
    arrayOfByte1[''] = 44;
    arrayOfByte1[''] = -41;
    arrayOfByte1[''] = 117;
    arrayOfByte1[''] = 122;
    arrayOfByte1[''] = -21;
    arrayOfByte1[''] = 22;
    arrayOfByte1[''] = 11;
    arrayOfByte1[''] = -11;
    arrayOfByte1[''] = 89;
    arrayOfByte1[''] = -53;
    arrayOfByte1[''] = 95;
    arrayOfByte1[''] = -80;
    arrayOfByte1[''] = -100;
    arrayOfByte1[''] = -87;
    arrayOfByte1[''] = 81;
    arrayOfByte1[''] = -96;
    arrayOfByte1[' '] = 127;
    arrayOfByte1['¡'] = 12;
    arrayOfByte1['¢'] = -10;
    arrayOfByte1['£'] = 111;
    arrayOfByte1['¤'] = 23;
    arrayOfByte1['¥'] = -60;
    arrayOfByte1['¦'] = 73;
    arrayOfByte1['§'] = -20;
    arrayOfByte1['¨'] = -40;
    arrayOfByte1['©'] = 67;
    arrayOfByte1['ª'] = 31;
    arrayOfByte1['«'] = 45;
    arrayOfByte1['¬'] = -92;
    arrayOfByte1['­'] = 118;
    arrayOfByte1['®'] = 123;
    arrayOfByte1['¯'] = -73;
    arrayOfByte1['°'] = -52;
    arrayOfByte1['±'] = -69;
    arrayOfByte1['²'] = 62;
    arrayOfByte1['³'] = 90;
    arrayOfByte1['´'] = -5;
    arrayOfByte1['µ'] = 96;
    arrayOfByte1['¶'] = -79;
    arrayOfByte1['·'] = -122;
    arrayOfByte1['¸'] = 59;
    arrayOfByte1['¹'] = 82;
    arrayOfByte1['º'] = -95;
    arrayOfByte1['»'] = 108;
    arrayOfByte1['¼'] = -86;
    arrayOfByte1['½'] = 85;
    arrayOfByte1['¾'] = 41;
    arrayOfByte1['¿'] = -99;
    arrayOfByte1['À'] = -105;
    arrayOfByte1['Á'] = -78;
    arrayOfByte1['Â'] = -121;
    arrayOfByte1['Ã'] = -112;
    arrayOfByte1['Ä'] = 97;
    arrayOfByte1['Å'] = -66;
    arrayOfByte1['Æ'] = -36;
    arrayOfByte1['Ç'] = -4;
    arrayOfByte1['È'] = -68;
    arrayOfByte1['É'] = -107;
    arrayOfByte1['Ê'] = -49;
    arrayOfByte1['Ë'] = -51;
    arrayOfByte1['Ì'] = 55;
    arrayOfByte1['Í'] = 63;
    arrayOfByte1['Î'] = 91;
    arrayOfByte1['Ï'] = -47;
    arrayOfByte1['Ð'] = 83;
    arrayOfByte1['Ñ'] = 57;
    arrayOfByte1['Ò'] = -124;
    arrayOfByte1['Ó'] = 60;
    arrayOfByte1['Ô'] = 65;
    arrayOfByte1['Õ'] = -94;
    arrayOfByte1['Ö'] = 109;
    arrayOfByte1['×'] = 71;
    arrayOfByte1['Ø'] = 20;
    arrayOfByte1['Ù'] = 42;
    arrayOfByte1['Ú'] = -98;
    arrayOfByte1['Û'] = 93;
    arrayOfByte1['Ü'] = 86;
    arrayOfByte1['Ý'] = -14;
    arrayOfByte1['Þ'] = -45;
    arrayOfByte1['ß'] = -85;
    arrayOfByte1['à'] = 68;
    arrayOfByte1['á'] = 17;
    arrayOfByte1['â'] = -110;
    arrayOfByte1['ã'] = -39;
    arrayOfByte1['ä'] = 35;
    arrayOfByte1['å'] = 32;
    arrayOfByte1['æ'] = 46;
    arrayOfByte1['ç'] = -119;
    arrayOfByte1['è'] = -76;
    arrayOfByte1['é'] = 124;
    arrayOfByte1['ê'] = -72;
    arrayOfByte1['ë'] = 38;
    arrayOfByte1['ì'] = 119;
    arrayOfByte1['í'] = -103;
    arrayOfByte1['î'] = -29;
    arrayOfByte1['ï'] = -91;
    arrayOfByte1['ð'] = 103;
    arrayOfByte1['ñ'] = 74;
    arrayOfByte1['ò'] = -19;
    arrayOfByte1['ó'] = -34;
    arrayOfByte1['ô'] = -59;
    arrayOfByte1['õ'] = 49;
    arrayOfByte1['ö'] = -2;
    arrayOfByte1['÷'] = 24;
    arrayOfByte1['ø'] = 13;
    arrayOfByte1['ù'] = 99;
    arrayOfByte1['ú'] = -116;
    arrayOfByte1['û'] = -128;
    arrayOfByte1['ü'] = -64;
    arrayOfByte1['ý'] = -9;
    arrayOfByte1['þ'] = 112;
    arrayOfByte1['ÿ'] = 7;
    logtable = arrayOfByte1;
    byte[] arrayOfByte2 = new byte[511];
    arrayOfByte2[1] = 3;
    arrayOfByte2[2] = 5;
    arrayOfByte2[3] = 15;
    arrayOfByte2[4] = 17;
    arrayOfByte2[5] = 51;
    arrayOfByte2[6] = 85;
    arrayOfByte2[7] = -1;
    arrayOfByte2[8] = 26;
    arrayOfByte2[9] = 46;
    arrayOfByte2[10] = 114;
    arrayOfByte2[11] = -106;
    arrayOfByte2[12] = -95;
    arrayOfByte2[13] = -8;
    arrayOfByte2[14] = 19;
    arrayOfByte2[15] = 53;
    arrayOfByte2[16] = 95;
    arrayOfByte2[17] = -31;
    arrayOfByte2[18] = 56;
    arrayOfByte2[19] = 72;
    arrayOfByte2[20] = -40;
    arrayOfByte2[21] = 115;
    arrayOfByte2[22] = -107;
    arrayOfByte2[23] = -92;
    arrayOfByte2[24] = -9;
    arrayOfByte2[25] = 2;
    arrayOfByte2[26] = 6;
    arrayOfByte2[27] = 10;
    arrayOfByte2[28] = 30;
    arrayOfByte2[29] = 34;
    arrayOfByte2[30] = 102;
    arrayOfByte2[31] = -86;
    arrayOfByte2[32] = -27;
    arrayOfByte2[33] = 52;
    arrayOfByte2[34] = 92;
    arrayOfByte2[35] = -28;
    arrayOfByte2[36] = 55;
    arrayOfByte2[37] = 89;
    arrayOfByte2[38] = -21;
    arrayOfByte2[39] = 38;
    arrayOfByte2[40] = 106;
    arrayOfByte2[41] = -66;
    arrayOfByte2[42] = -39;
    arrayOfByte2[43] = 112;
    arrayOfByte2[44] = -112;
    arrayOfByte2[45] = -85;
    arrayOfByte2[46] = -26;
    arrayOfByte2[47] = 49;
    arrayOfByte2[48] = 83;
    arrayOfByte2[49] = -11;
    arrayOfByte2[50] = 4;
    arrayOfByte2[51] = 12;
    arrayOfByte2[52] = 20;
    arrayOfByte2[53] = 60;
    arrayOfByte2[54] = 68;
    arrayOfByte2[55] = -52;
    arrayOfByte2[56] = 79;
    arrayOfByte2[57] = -47;
    arrayOfByte2[58] = 104;
    arrayOfByte2[59] = -72;
    arrayOfByte2[60] = -45;
    arrayOfByte2[61] = 110;
    arrayOfByte2[62] = -78;
    arrayOfByte2[63] = -51;
    arrayOfByte2[64] = 76;
    arrayOfByte2[65] = -44;
    arrayOfByte2[66] = 103;
    arrayOfByte2[67] = -87;
    arrayOfByte2[68] = -32;
    arrayOfByte2[69] = 59;
    arrayOfByte2[70] = 77;
    arrayOfByte2[71] = -41;
    arrayOfByte2[72] = 98;
    arrayOfByte2[73] = -90;
    arrayOfByte2[74] = -15;
    arrayOfByte2[75] = 8;
    arrayOfByte2[76] = 24;
    arrayOfByte2[77] = 40;
    arrayOfByte2[78] = 120;
    arrayOfByte2[79] = -120;
    arrayOfByte2[80] = -125;
    arrayOfByte2[81] = -98;
    arrayOfByte2[82] = -71;
    arrayOfByte2[83] = -48;
    arrayOfByte2[84] = 107;
    arrayOfByte2[85] = -67;
    arrayOfByte2[86] = -36;
    arrayOfByte2[87] = 127;
    arrayOfByte2[88] = -127;
    arrayOfByte2[89] = -104;
    arrayOfByte2[90] = -77;
    arrayOfByte2[91] = -50;
    arrayOfByte2[92] = 73;
    arrayOfByte2[93] = -37;
    arrayOfByte2[94] = 118;
    arrayOfByte2[95] = -102;
    arrayOfByte2[96] = -75;
    arrayOfByte2[97] = -60;
    arrayOfByte2[98] = 87;
    arrayOfByte2[99] = -7;
    arrayOfByte2[100] = 16;
    arrayOfByte2[101] = 48;
    arrayOfByte2[102] = 80;
    arrayOfByte2[103] = -16;
    arrayOfByte2[104] = 11;
    arrayOfByte2[105] = 29;
    arrayOfByte2[106] = 39;
    arrayOfByte2[107] = 105;
    arrayOfByte2[108] = -69;
    arrayOfByte2[109] = -42;
    arrayOfByte2[110] = 97;
    arrayOfByte2[111] = -93;
    arrayOfByte2[112] = -2;
    arrayOfByte2[113] = 25;
    arrayOfByte2[114] = 43;
    arrayOfByte2[115] = 125;
    arrayOfByte2[116] = -121;
    arrayOfByte2[117] = -110;
    arrayOfByte2[118] = -83;
    arrayOfByte2[119] = -20;
    arrayOfByte2[120] = 47;
    arrayOfByte2[121] = 113;
    arrayOfByte2[122] = -109;
    arrayOfByte2[123] = -82;
    arrayOfByte2[124] = -23;
    arrayOfByte2[125] = 32;
    arrayOfByte2[126] = 96;
    arrayOfByte2[127] = -96;
    arrayOfByte2[''] = -5;
    arrayOfByte2[''] = 22;
    arrayOfByte2[''] = 58;
    arrayOfByte2[''] = 78;
    arrayOfByte2[''] = -46;
    arrayOfByte2[''] = 109;
    arrayOfByte2[''] = -73;
    arrayOfByte2[''] = -62;
    arrayOfByte2[''] = 93;
    arrayOfByte2[''] = -25;
    arrayOfByte2[''] = 50;
    arrayOfByte2[''] = 86;
    arrayOfByte2[''] = -6;
    arrayOfByte2[''] = 21;
    arrayOfByte2[''] = 63;
    arrayOfByte2[''] = 65;
    arrayOfByte2[''] = -61;
    arrayOfByte2[''] = 94;
    arrayOfByte2[''] = -30;
    arrayOfByte2[''] = 61;
    arrayOfByte2[''] = 71;
    arrayOfByte2[''] = -55;
    arrayOfByte2[''] = 64;
    arrayOfByte2[''] = -64;
    arrayOfByte2[''] = 91;
    arrayOfByte2[''] = -19;
    arrayOfByte2[''] = 44;
    arrayOfByte2[''] = 116;
    arrayOfByte2[''] = -100;
    arrayOfByte2[''] = -65;
    arrayOfByte2[''] = -38;
    arrayOfByte2[''] = 117;
    arrayOfByte2[' '] = -97;
    arrayOfByte2['¡'] = -70;
    arrayOfByte2['¢'] = -43;
    arrayOfByte2['£'] = 100;
    arrayOfByte2['¤'] = -84;
    arrayOfByte2['¥'] = -17;
    arrayOfByte2['¦'] = 42;
    arrayOfByte2['§'] = 126;
    arrayOfByte2['¨'] = -126;
    arrayOfByte2['©'] = -99;
    arrayOfByte2['ª'] = -68;
    arrayOfByte2['«'] = -33;
    arrayOfByte2['¬'] = 122;
    arrayOfByte2['­'] = -114;
    arrayOfByte2['®'] = -119;
    arrayOfByte2['¯'] = -128;
    arrayOfByte2['°'] = -101;
    arrayOfByte2['±'] = -74;
    arrayOfByte2['²'] = -63;
    arrayOfByte2['³'] = 88;
    arrayOfByte2['´'] = -24;
    arrayOfByte2['µ'] = 35;
    arrayOfByte2['¶'] = 101;
    arrayOfByte2['·'] = -81;
    arrayOfByte2['¸'] = -22;
    arrayOfByte2['¹'] = 37;
    arrayOfByte2['º'] = 111;
    arrayOfByte2['»'] = -79;
    arrayOfByte2['¼'] = -56;
    arrayOfByte2['½'] = 67;
    arrayOfByte2['¾'] = -59;
    arrayOfByte2['¿'] = 84;
    arrayOfByte2['À'] = -4;
    arrayOfByte2['Á'] = 31;
    arrayOfByte2['Â'] = 33;
    arrayOfByte2['Ã'] = 99;
    arrayOfByte2['Ä'] = -91;
    arrayOfByte2['Å'] = -12;
    arrayOfByte2['Æ'] = 7;
    arrayOfByte2['Ç'] = 9;
    arrayOfByte2['È'] = 27;
    arrayOfByte2['É'] = 45;
    arrayOfByte2['Ê'] = 119;
    arrayOfByte2['Ë'] = -103;
    arrayOfByte2['Ì'] = -80;
    arrayOfByte2['Í'] = -53;
    arrayOfByte2['Î'] = 70;
    arrayOfByte2['Ï'] = -54;
    arrayOfByte2['Ð'] = 69;
    arrayOfByte2['Ñ'] = -49;
    arrayOfByte2['Ò'] = 74;
    arrayOfByte2['Ó'] = -34;
    arrayOfByte2['Ô'] = 121;
    arrayOfByte2['Õ'] = -117;
    arrayOfByte2['Ö'] = -122;
    arrayOfByte2['×'] = -111;
    arrayOfByte2['Ø'] = -88;
    arrayOfByte2['Ù'] = -29;
    arrayOfByte2['Ú'] = 62;
    arrayOfByte2['Û'] = 66;
    arrayOfByte2['Ü'] = -58;
    arrayOfByte2['Ý'] = 81;
    arrayOfByte2['Þ'] = -13;
    arrayOfByte2['ß'] = 14;
    arrayOfByte2['à'] = 18;
    arrayOfByte2['á'] = 54;
    arrayOfByte2['â'] = 90;
    arrayOfByte2['ã'] = -18;
    arrayOfByte2['ä'] = 41;
    arrayOfByte2['å'] = 123;
    arrayOfByte2['æ'] = -115;
    arrayOfByte2['ç'] = -116;
    arrayOfByte2['è'] = -113;
    arrayOfByte2['é'] = -118;
    arrayOfByte2['ê'] = -123;
    arrayOfByte2['ë'] = -108;
    arrayOfByte2['ì'] = -89;
    arrayOfByte2['í'] = -14;
    arrayOfByte2['î'] = 13;
    arrayOfByte2['ï'] = 23;
    arrayOfByte2['ð'] = 57;
    arrayOfByte2['ñ'] = 75;
    arrayOfByte2['ò'] = -35;
    arrayOfByte2['ó'] = 124;
    arrayOfByte2['ô'] = -124;
    arrayOfByte2['õ'] = -105;
    arrayOfByte2['ö'] = -94;
    arrayOfByte2['÷'] = -3;
    arrayOfByte2['ø'] = 28;
    arrayOfByte2['ù'] = 36;
    arrayOfByte2['ú'] = 108;
    arrayOfByte2['û'] = -76;
    arrayOfByte2['ü'] = -57;
    arrayOfByte2['ý'] = 82;
    arrayOfByte2['þ'] = -10;
    arrayOfByte2['ÿ'] = 1;
    arrayOfByte2[256] = 3;
    arrayOfByte2[257] = 5;
    arrayOfByte2[258] = 15;
    arrayOfByte2[259] = 17;
    arrayOfByte2[260] = 51;
    arrayOfByte2[261] = 85;
    arrayOfByte2[262] = -1;
    arrayOfByte2[263] = 26;
    arrayOfByte2[264] = 46;
    arrayOfByte2[265] = 114;
    arrayOfByte2[266] = -106;
    arrayOfByte2[267] = -95;
    arrayOfByte2[268] = -8;
    arrayOfByte2[269] = 19;
    arrayOfByte2[270] = 53;
    arrayOfByte2[271] = 95;
    arrayOfByte2[272] = -31;
    arrayOfByte2[273] = 56;
    arrayOfByte2[274] = 72;
    arrayOfByte2[275] = -40;
    arrayOfByte2[276] = 115;
    arrayOfByte2[277] = -107;
    arrayOfByte2[278] = -92;
    arrayOfByte2[279] = -9;
    arrayOfByte2[280] = 2;
    arrayOfByte2[281] = 6;
    arrayOfByte2[282] = 10;
    arrayOfByte2[283] = 30;
    arrayOfByte2[284] = 34;
    arrayOfByte2[285] = 102;
    arrayOfByte2[286] = -86;
    arrayOfByte2[287] = -27;
    arrayOfByte2[288] = 52;
    arrayOfByte2[289] = 92;
    arrayOfByte2[290] = -28;
    arrayOfByte2[291] = 55;
    arrayOfByte2[292] = 89;
    arrayOfByte2[293] = -21;
    arrayOfByte2[294] = 38;
    arrayOfByte2[295] = 106;
    arrayOfByte2[296] = -66;
    arrayOfByte2[297] = -39;
    arrayOfByte2[298] = 112;
    arrayOfByte2[299] = -112;
    arrayOfByte2[300] = -85;
    arrayOfByte2[301] = -26;
    arrayOfByte2[302] = 49;
    arrayOfByte2[303] = 83;
    arrayOfByte2[304] = -11;
    arrayOfByte2[305] = 4;
    arrayOfByte2[306] = 12;
    arrayOfByte2[307] = 20;
    arrayOfByte2[308] = 60;
    arrayOfByte2[309] = 68;
    arrayOfByte2[310] = -52;
    arrayOfByte2[311] = 79;
    arrayOfByte2[312] = -47;
    arrayOfByte2[313] = 104;
    arrayOfByte2[314] = -72;
    arrayOfByte2[315] = -45;
    arrayOfByte2[316] = 110;
    arrayOfByte2[317] = -78;
    arrayOfByte2[318] = -51;
    arrayOfByte2[319] = 76;
    arrayOfByte2[320] = -44;
    arrayOfByte2[321] = 103;
    arrayOfByte2[322] = -87;
    arrayOfByte2[323] = -32;
    arrayOfByte2[324] = 59;
    arrayOfByte2[325] = 77;
    arrayOfByte2[326] = -41;
    arrayOfByte2[327] = 98;
    arrayOfByte2[328] = -90;
    arrayOfByte2[329] = -15;
    arrayOfByte2[330] = 8;
    arrayOfByte2[331] = 24;
    arrayOfByte2[332] = 40;
    arrayOfByte2[333] = 120;
    arrayOfByte2[334] = -120;
    arrayOfByte2[335] = -125;
    arrayOfByte2[336] = -98;
    arrayOfByte2[337] = -71;
    arrayOfByte2[338] = -48;
    arrayOfByte2[339] = 107;
    arrayOfByte2[340] = -67;
    arrayOfByte2[341] = -36;
    arrayOfByte2[342] = 127;
    arrayOfByte2[343] = -127;
    arrayOfByte2[344] = -104;
    arrayOfByte2[345] = -77;
    arrayOfByte2[346] = -50;
    arrayOfByte2[347] = 73;
    arrayOfByte2[348] = -37;
    arrayOfByte2[349] = 118;
    arrayOfByte2[350] = -102;
    arrayOfByte2[351] = -75;
    arrayOfByte2[352] = -60;
    arrayOfByte2[353] = 87;
    arrayOfByte2[354] = -7;
    arrayOfByte2[355] = 16;
    arrayOfByte2[356] = 48;
    arrayOfByte2[357] = 80;
    arrayOfByte2[358] = -16;
    arrayOfByte2[359] = 11;
    arrayOfByte2[360] = 29;
    arrayOfByte2[361] = 39;
    arrayOfByte2[362] = 105;
    arrayOfByte2[363] = -69;
    arrayOfByte2[364] = -42;
    arrayOfByte2[365] = 97;
    arrayOfByte2[366] = -93;
    arrayOfByte2[367] = -2;
    arrayOfByte2[368] = 25;
    arrayOfByte2[369] = 43;
    arrayOfByte2[370] = 125;
    arrayOfByte2[371] = -121;
    arrayOfByte2[372] = -110;
    arrayOfByte2[373] = -83;
    arrayOfByte2[374] = -20;
    arrayOfByte2[375] = 47;
    arrayOfByte2[376] = 113;
    arrayOfByte2[377] = -109;
    arrayOfByte2[378] = -82;
    arrayOfByte2[379] = -23;
    arrayOfByte2[380] = 32;
    arrayOfByte2[381] = 96;
    arrayOfByte2[382] = -96;
    arrayOfByte2[383] = -5;
    arrayOfByte2[384] = 22;
    arrayOfByte2[385] = 58;
    arrayOfByte2[386] = 78;
    arrayOfByte2[387] = -46;
    arrayOfByte2[388] = 109;
    arrayOfByte2[389] = -73;
    arrayOfByte2[390] = -62;
    arrayOfByte2[391] = 93;
    arrayOfByte2[392] = -25;
    arrayOfByte2[393] = 50;
    arrayOfByte2[394] = 86;
    arrayOfByte2[395] = -6;
    arrayOfByte2[396] = 21;
    arrayOfByte2[397] = 63;
    arrayOfByte2[398] = 65;
    arrayOfByte2[399] = -61;
    arrayOfByte2[400] = 94;
    arrayOfByte2[401] = -30;
    arrayOfByte2[402] = 61;
    arrayOfByte2[403] = 71;
    arrayOfByte2[404] = -55;
    arrayOfByte2[405] = 64;
    arrayOfByte2[406] = -64;
    arrayOfByte2[407] = 91;
    arrayOfByte2[408] = -19;
    arrayOfByte2[409] = 44;
    arrayOfByte2[410] = 116;
    arrayOfByte2[411] = -100;
    arrayOfByte2[412] = -65;
    arrayOfByte2[413] = -38;
    arrayOfByte2[414] = 117;
    arrayOfByte2[415] = -97;
    arrayOfByte2[416] = -70;
    arrayOfByte2[417] = -43;
    arrayOfByte2[418] = 100;
    arrayOfByte2[419] = -84;
    arrayOfByte2[420] = -17;
    arrayOfByte2[421] = 42;
    arrayOfByte2[422] = 126;
    arrayOfByte2[423] = -126;
    arrayOfByte2[424] = -99;
    arrayOfByte2[425] = -68;
    arrayOfByte2[426] = -33;
    arrayOfByte2[427] = 122;
    arrayOfByte2[428] = -114;
    arrayOfByte2[429] = -119;
    arrayOfByte2[430] = -128;
    arrayOfByte2[431] = -101;
    arrayOfByte2[432] = -74;
    arrayOfByte2[433] = -63;
    arrayOfByte2[434] = 88;
    arrayOfByte2[435] = -24;
    arrayOfByte2[436] = 35;
    arrayOfByte2[437] = 101;
    arrayOfByte2[438] = -81;
    arrayOfByte2[439] = -22;
    arrayOfByte2[440] = 37;
    arrayOfByte2[441] = 111;
    arrayOfByte2[442] = -79;
    arrayOfByte2[443] = -56;
    arrayOfByte2[444] = 67;
    arrayOfByte2[445] = -59;
    arrayOfByte2[446] = 84;
    arrayOfByte2[447] = -4;
    arrayOfByte2[448] = 31;
    arrayOfByte2[449] = 33;
    arrayOfByte2[450] = 99;
    arrayOfByte2[451] = -91;
    arrayOfByte2[452] = -12;
    arrayOfByte2[453] = 7;
    arrayOfByte2[454] = 9;
    arrayOfByte2[455] = 27;
    arrayOfByte2[456] = 45;
    arrayOfByte2[457] = 119;
    arrayOfByte2[458] = -103;
    arrayOfByte2[459] = -80;
    arrayOfByte2[460] = -53;
    arrayOfByte2[461] = 70;
    arrayOfByte2[462] = -54;
    arrayOfByte2[463] = 69;
    arrayOfByte2[464] = -49;
    arrayOfByte2[465] = 74;
    arrayOfByte2[466] = -34;
    arrayOfByte2[467] = 121;
    arrayOfByte2[468] = -117;
    arrayOfByte2[469] = -122;
    arrayOfByte2[470] = -111;
    arrayOfByte2[471] = -88;
    arrayOfByte2[472] = -29;
    arrayOfByte2[473] = 62;
    arrayOfByte2[474] = 66;
    arrayOfByte2[475] = -58;
    arrayOfByte2[476] = 81;
    arrayOfByte2[477] = -13;
    arrayOfByte2[478] = 14;
    arrayOfByte2[479] = 18;
    arrayOfByte2[480] = 54;
    arrayOfByte2[481] = 90;
    arrayOfByte2[482] = -18;
    arrayOfByte2[483] = 41;
    arrayOfByte2[484] = 123;
    arrayOfByte2[485] = -115;
    arrayOfByte2[486] = -116;
    arrayOfByte2[487] = -113;
    arrayOfByte2[488] = -118;
    arrayOfByte2[489] = -123;
    arrayOfByte2[490] = -108;
    arrayOfByte2[491] = -89;
    arrayOfByte2[492] = -14;
    arrayOfByte2[493] = 13;
    arrayOfByte2[494] = 23;
    arrayOfByte2[495] = 57;
    arrayOfByte2[496] = 75;
    arrayOfByte2[497] = -35;
    arrayOfByte2[498] = 124;
    arrayOfByte2[499] = -124;
    arrayOfByte2[500] = -105;
    arrayOfByte2[501] = -94;
    arrayOfByte2[502] = -3;
    arrayOfByte2[503] = 28;
    arrayOfByte2[504] = 36;
    arrayOfByte2[505] = 108;
    arrayOfByte2[506] = -76;
    arrayOfByte2[507] = -57;
    arrayOfByte2[508] = 82;
    arrayOfByte2[509] = -10;
    arrayOfByte2[510] = 1;
    aLogtable = arrayOfByte2;
    byte[] arrayOfByte3 = new byte[256];
    arrayOfByte3[0] = 99;
    arrayOfByte3[1] = 124;
    arrayOfByte3[2] = 119;
    arrayOfByte3[3] = 123;
    arrayOfByte3[4] = -14;
    arrayOfByte3[5] = 107;
    arrayOfByte3[6] = 111;
    arrayOfByte3[7] = -59;
    arrayOfByte3[8] = 48;
    arrayOfByte3[9] = 1;
    arrayOfByte3[10] = 103;
    arrayOfByte3[11] = 43;
    arrayOfByte3[12] = -2;
    arrayOfByte3[13] = -41;
    arrayOfByte3[14] = -85;
    arrayOfByte3[15] = 118;
    arrayOfByte3[16] = -54;
    arrayOfByte3[17] = -126;
    arrayOfByte3[18] = -55;
    arrayOfByte3[19] = 125;
    arrayOfByte3[20] = -6;
    arrayOfByte3[21] = 89;
    arrayOfByte3[22] = 71;
    arrayOfByte3[23] = -16;
    arrayOfByte3[24] = -83;
    arrayOfByte3[25] = -44;
    arrayOfByte3[26] = -94;
    arrayOfByte3[27] = -81;
    arrayOfByte3[28] = -100;
    arrayOfByte3[29] = -92;
    arrayOfByte3[30] = 114;
    arrayOfByte3[31] = -64;
    arrayOfByte3[32] = -73;
    arrayOfByte3[33] = -3;
    arrayOfByte3[34] = -109;
    arrayOfByte3[35] = 38;
    arrayOfByte3[36] = 54;
    arrayOfByte3[37] = 63;
    arrayOfByte3[38] = -9;
    arrayOfByte3[39] = -52;
    arrayOfByte3[40] = 52;
    arrayOfByte3[41] = -91;
    arrayOfByte3[42] = -27;
    arrayOfByte3[43] = -15;
    arrayOfByte3[44] = 113;
    arrayOfByte3[45] = -40;
    arrayOfByte3[46] = 49;
    arrayOfByte3[47] = 21;
    arrayOfByte3[48] = 4;
    arrayOfByte3[49] = -57;
    arrayOfByte3[50] = 35;
    arrayOfByte3[51] = -61;
    arrayOfByte3[52] = 24;
    arrayOfByte3[53] = -106;
    arrayOfByte3[54] = 5;
    arrayOfByte3[55] = -102;
    arrayOfByte3[56] = 7;
    arrayOfByte3[57] = 18;
    arrayOfByte3[58] = -128;
    arrayOfByte3[59] = -30;
    arrayOfByte3[60] = -21;
    arrayOfByte3[61] = 39;
    arrayOfByte3[62] = -78;
    arrayOfByte3[63] = 117;
    arrayOfByte3[64] = 9;
    arrayOfByte3[65] = -125;
    arrayOfByte3[66] = 44;
    arrayOfByte3[67] = 26;
    arrayOfByte3[68] = 27;
    arrayOfByte3[69] = 110;
    arrayOfByte3[70] = 90;
    arrayOfByte3[71] = -96;
    arrayOfByte3[72] = 82;
    arrayOfByte3[73] = 59;
    arrayOfByte3[74] = -42;
    arrayOfByte3[75] = -77;
    arrayOfByte3[76] = 41;
    arrayOfByte3[77] = -29;
    arrayOfByte3[78] = 47;
    arrayOfByte3[79] = -124;
    arrayOfByte3[80] = 83;
    arrayOfByte3[81] = -47;
    arrayOfByte3[83] = -19;
    arrayOfByte3[84] = 32;
    arrayOfByte3[85] = -4;
    arrayOfByte3[86] = -79;
    arrayOfByte3[87] = 91;
    arrayOfByte3[88] = 106;
    arrayOfByte3[89] = -53;
    arrayOfByte3[90] = -66;
    arrayOfByte3[91] = 57;
    arrayOfByte3[92] = 74;
    arrayOfByte3[93] = 76;
    arrayOfByte3[94] = 88;
    arrayOfByte3[95] = -49;
    arrayOfByte3[96] = -48;
    arrayOfByte3[97] = -17;
    arrayOfByte3[98] = -86;
    arrayOfByte3[99] = -5;
    arrayOfByte3[100] = 67;
    arrayOfByte3[101] = 77;
    arrayOfByte3[102] = 51;
    arrayOfByte3[103] = -123;
    arrayOfByte3[104] = 69;
    arrayOfByte3[105] = -7;
    arrayOfByte3[106] = 2;
    arrayOfByte3[107] = 127;
    arrayOfByte3[108] = 80;
    arrayOfByte3[109] = 60;
    arrayOfByte3[110] = -97;
    arrayOfByte3[111] = -88;
    arrayOfByte3[112] = 81;
    arrayOfByte3[113] = -93;
    arrayOfByte3[114] = 64;
    arrayOfByte3[115] = -113;
    arrayOfByte3[116] = -110;
    arrayOfByte3[117] = -99;
    arrayOfByte3[118] = 56;
    arrayOfByte3[119] = -11;
    arrayOfByte3[120] = -68;
    arrayOfByte3[121] = -74;
    arrayOfByte3[122] = -38;
    arrayOfByte3[123] = 33;
    arrayOfByte3[124] = 16;
    arrayOfByte3[125] = -1;
    arrayOfByte3[126] = -13;
    arrayOfByte3[127] = -46;
    arrayOfByte3[''] = -51;
    arrayOfByte3[''] = 12;
    arrayOfByte3[''] = 19;
    arrayOfByte3[''] = -20;
    arrayOfByte3[''] = 95;
    arrayOfByte3[''] = -105;
    arrayOfByte3[''] = 68;
    arrayOfByte3[''] = 23;
    arrayOfByte3[''] = -60;
    arrayOfByte3[''] = -89;
    arrayOfByte3[''] = 126;
    arrayOfByte3[''] = 61;
    arrayOfByte3[''] = 100;
    arrayOfByte3[''] = 93;
    arrayOfByte3[''] = 25;
    arrayOfByte3[''] = 115;
    arrayOfByte3[''] = 96;
    arrayOfByte3[''] = -127;
    arrayOfByte3[''] = 79;
    arrayOfByte3[''] = -36;
    arrayOfByte3[''] = 34;
    arrayOfByte3[''] = 42;
    arrayOfByte3[''] = -112;
    arrayOfByte3[''] = -120;
    arrayOfByte3[''] = 70;
    arrayOfByte3[''] = -18;
    arrayOfByte3[''] = -72;
    arrayOfByte3[''] = 20;
    arrayOfByte3[''] = -34;
    arrayOfByte3[''] = 94;
    arrayOfByte3[''] = 11;
    arrayOfByte3[''] = -37;
    arrayOfByte3[' '] = -32;
    arrayOfByte3['¡'] = 50;
    arrayOfByte3['¢'] = 58;
    arrayOfByte3['£'] = 10;
    arrayOfByte3['¤'] = 73;
    arrayOfByte3['¥'] = 6;
    arrayOfByte3['¦'] = 36;
    arrayOfByte3['§'] = 92;
    arrayOfByte3['¨'] = -62;
    arrayOfByte3['©'] = -45;
    arrayOfByte3['ª'] = -84;
    arrayOfByte3['«'] = 98;
    arrayOfByte3['¬'] = -111;
    arrayOfByte3['­'] = -107;
    arrayOfByte3['®'] = -28;
    arrayOfByte3['¯'] = 121;
    arrayOfByte3['°'] = -25;
    arrayOfByte3['±'] = -56;
    arrayOfByte3['²'] = 55;
    arrayOfByte3['³'] = 109;
    arrayOfByte3['´'] = -115;
    arrayOfByte3['µ'] = -43;
    arrayOfByte3['¶'] = 78;
    arrayOfByte3['·'] = -87;
    arrayOfByte3['¸'] = 108;
    arrayOfByte3['¹'] = 86;
    arrayOfByte3['º'] = -12;
    arrayOfByte3['»'] = -22;
    arrayOfByte3['¼'] = 101;
    arrayOfByte3['½'] = 122;
    arrayOfByte3['¾'] = -82;
    arrayOfByte3['¿'] = 8;
    arrayOfByte3['À'] = -70;
    arrayOfByte3['Á'] = 120;
    arrayOfByte3['Â'] = 37;
    arrayOfByte3['Ã'] = 46;
    arrayOfByte3['Ä'] = 28;
    arrayOfByte3['Å'] = -90;
    arrayOfByte3['Æ'] = -76;
    arrayOfByte3['Ç'] = -58;
    arrayOfByte3['È'] = -24;
    arrayOfByte3['É'] = -35;
    arrayOfByte3['Ê'] = 116;
    arrayOfByte3['Ë'] = 31;
    arrayOfByte3['Ì'] = 75;
    arrayOfByte3['Í'] = -67;
    arrayOfByte3['Î'] = -117;
    arrayOfByte3['Ï'] = -118;
    arrayOfByte3['Ð'] = 112;
    arrayOfByte3['Ñ'] = 62;
    arrayOfByte3['Ò'] = -75;
    arrayOfByte3['Ó'] = 102;
    arrayOfByte3['Ô'] = 72;
    arrayOfByte3['Õ'] = 3;
    arrayOfByte3['Ö'] = -10;
    arrayOfByte3['×'] = 14;
    arrayOfByte3['Ø'] = 97;
    arrayOfByte3['Ù'] = 53;
    arrayOfByte3['Ú'] = 87;
    arrayOfByte3['Û'] = -71;
    arrayOfByte3['Ü'] = -122;
    arrayOfByte3['Ý'] = -63;
    arrayOfByte3['Þ'] = 29;
    arrayOfByte3['ß'] = -98;
    arrayOfByte3['à'] = -31;
    arrayOfByte3['á'] = -8;
    arrayOfByte3['â'] = -104;
    arrayOfByte3['ã'] = 17;
    arrayOfByte3['ä'] = 105;
    arrayOfByte3['å'] = -39;
    arrayOfByte3['æ'] = -114;
    arrayOfByte3['ç'] = -108;
    arrayOfByte3['è'] = -101;
    arrayOfByte3['é'] = 30;
    arrayOfByte3['ê'] = -121;
    arrayOfByte3['ë'] = -23;
    arrayOfByte3['ì'] = -50;
    arrayOfByte3['í'] = 85;
    arrayOfByte3['î'] = 40;
    arrayOfByte3['ï'] = -33;
    arrayOfByte3['ð'] = -116;
    arrayOfByte3['ñ'] = -95;
    arrayOfByte3['ò'] = -119;
    arrayOfByte3['ó'] = 13;
    arrayOfByte3['ô'] = -65;
    arrayOfByte3['õ'] = -26;
    arrayOfByte3['ö'] = 66;
    arrayOfByte3['÷'] = 104;
    arrayOfByte3['ø'] = 65;
    arrayOfByte3['ù'] = -103;
    arrayOfByte3['ú'] = 45;
    arrayOfByte3['û'] = 15;
    arrayOfByte3['ü'] = -80;
    arrayOfByte3['ý'] = 84;
    arrayOfByte3['þ'] = -69;
    arrayOfByte3['ÿ'] = 22;
    S = arrayOfByte3;
    byte[] arrayOfByte4 = new byte[256];
    arrayOfByte4[0] = 82;
    arrayOfByte4[1] = 9;
    arrayOfByte4[2] = 106;
    arrayOfByte4[3] = -43;
    arrayOfByte4[4] = 48;
    arrayOfByte4[5] = 54;
    arrayOfByte4[6] = -91;
    arrayOfByte4[7] = 56;
    arrayOfByte4[8] = -65;
    arrayOfByte4[9] = 64;
    arrayOfByte4[10] = -93;
    arrayOfByte4[11] = -98;
    arrayOfByte4[12] = -127;
    arrayOfByte4[13] = -13;
    arrayOfByte4[14] = -41;
    arrayOfByte4[15] = -5;
    arrayOfByte4[16] = 124;
    arrayOfByte4[17] = -29;
    arrayOfByte4[18] = 57;
    arrayOfByte4[19] = -126;
    arrayOfByte4[20] = -101;
    arrayOfByte4[21] = 47;
    arrayOfByte4[22] = -1;
    arrayOfByte4[23] = -121;
    arrayOfByte4[24] = 52;
    arrayOfByte4[25] = -114;
    arrayOfByte4[26] = 67;
    arrayOfByte4[27] = 68;
    arrayOfByte4[28] = -60;
    arrayOfByte4[29] = -34;
    arrayOfByte4[30] = -23;
    arrayOfByte4[31] = -53;
    arrayOfByte4[32] = 84;
    arrayOfByte4[33] = 123;
    arrayOfByte4[34] = -108;
    arrayOfByte4[35] = 50;
    arrayOfByte4[36] = -90;
    arrayOfByte4[37] = -62;
    arrayOfByte4[38] = 35;
    arrayOfByte4[39] = 61;
    arrayOfByte4[40] = -18;
    arrayOfByte4[41] = 76;
    arrayOfByte4[42] = -107;
    arrayOfByte4[43] = 11;
    arrayOfByte4[44] = 66;
    arrayOfByte4[45] = -6;
    arrayOfByte4[46] = -61;
    arrayOfByte4[47] = 78;
    arrayOfByte4[48] = 8;
    arrayOfByte4[49] = 46;
    arrayOfByte4[50] = -95;
    arrayOfByte4[51] = 102;
    arrayOfByte4[52] = 40;
    arrayOfByte4[53] = -39;
    arrayOfByte4[54] = 36;
    arrayOfByte4[55] = -78;
    arrayOfByte4[56] = 118;
    arrayOfByte4[57] = 91;
    arrayOfByte4[58] = -94;
    arrayOfByte4[59] = 73;
    arrayOfByte4[60] = 109;
    arrayOfByte4[61] = -117;
    arrayOfByte4[62] = -47;
    arrayOfByte4[63] = 37;
    arrayOfByte4[64] = 114;
    arrayOfByte4[65] = -8;
    arrayOfByte4[66] = -10;
    arrayOfByte4[67] = 100;
    arrayOfByte4[68] = -122;
    arrayOfByte4[69] = 104;
    arrayOfByte4[70] = -104;
    arrayOfByte4[71] = 22;
    arrayOfByte4[72] = -44;
    arrayOfByte4[73] = -92;
    arrayOfByte4[74] = 92;
    arrayOfByte4[75] = -52;
    arrayOfByte4[76] = 93;
    arrayOfByte4[77] = 101;
    arrayOfByte4[78] = -74;
    arrayOfByte4[79] = -110;
    arrayOfByte4[80] = 108;
    arrayOfByte4[81] = 112;
    arrayOfByte4[82] = 72;
    arrayOfByte4[83] = 80;
    arrayOfByte4[84] = -3;
    arrayOfByte4[85] = -19;
    arrayOfByte4[86] = -71;
    arrayOfByte4[87] = -38;
    arrayOfByte4[88] = 94;
    arrayOfByte4[89] = 21;
    arrayOfByte4[90] = 70;
    arrayOfByte4[91] = 87;
    arrayOfByte4[92] = -89;
    arrayOfByte4[93] = -115;
    arrayOfByte4[94] = -99;
    arrayOfByte4[95] = -124;
    arrayOfByte4[96] = -112;
    arrayOfByte4[97] = -40;
    arrayOfByte4[98] = -85;
    arrayOfByte4[100] = -116;
    arrayOfByte4[101] = -68;
    arrayOfByte4[102] = -45;
    arrayOfByte4[103] = 10;
    arrayOfByte4[104] = -9;
    arrayOfByte4[105] = -28;
    arrayOfByte4[106] = 88;
    arrayOfByte4[107] = 5;
    arrayOfByte4[108] = -72;
    arrayOfByte4[109] = -77;
    arrayOfByte4[110] = 69;
    arrayOfByte4[111] = 6;
    arrayOfByte4[112] = -48;
    arrayOfByte4[113] = 44;
    arrayOfByte4[114] = 30;
    arrayOfByte4[115] = -113;
    arrayOfByte4[116] = -54;
    arrayOfByte4[117] = 63;
    arrayOfByte4[118] = 15;
    arrayOfByte4[119] = 2;
    arrayOfByte4[120] = -63;
    arrayOfByte4[121] = -81;
    arrayOfByte4[122] = -67;
    arrayOfByte4[123] = 3;
    arrayOfByte4[124] = 1;
    arrayOfByte4[125] = 19;
    arrayOfByte4[126] = -118;
    arrayOfByte4[127] = 107;
    arrayOfByte4[''] = 58;
    arrayOfByte4[''] = -111;
    arrayOfByte4[''] = 17;
    arrayOfByte4[''] = 65;
    arrayOfByte4[''] = 79;
    arrayOfByte4[''] = 103;
    arrayOfByte4[''] = -36;
    arrayOfByte4[''] = -22;
    arrayOfByte4[''] = -105;
    arrayOfByte4[''] = -14;
    arrayOfByte4[''] = -49;
    arrayOfByte4[''] = -50;
    arrayOfByte4[''] = -16;
    arrayOfByte4[''] = -76;
    arrayOfByte4[''] = -26;
    arrayOfByte4[''] = 115;
    arrayOfByte4[''] = -106;
    arrayOfByte4[''] = -84;
    arrayOfByte4[''] = 116;
    arrayOfByte4[''] = 34;
    arrayOfByte4[''] = -25;
    arrayOfByte4[''] = -83;
    arrayOfByte4[''] = 53;
    arrayOfByte4[''] = -123;
    arrayOfByte4[''] = -30;
    arrayOfByte4[''] = -7;
    arrayOfByte4[''] = 55;
    arrayOfByte4[''] = -24;
    arrayOfByte4[''] = 28;
    arrayOfByte4[''] = 117;
    arrayOfByte4[''] = -33;
    arrayOfByte4[''] = 110;
    arrayOfByte4[' '] = 71;
    arrayOfByte4['¡'] = -15;
    arrayOfByte4['¢'] = 26;
    arrayOfByte4['£'] = 113;
    arrayOfByte4['¤'] = 29;
    arrayOfByte4['¥'] = 41;
    arrayOfByte4['¦'] = -59;
    arrayOfByte4['§'] = -119;
    arrayOfByte4['¨'] = 111;
    arrayOfByte4['©'] = -73;
    arrayOfByte4['ª'] = 98;
    arrayOfByte4['«'] = 14;
    arrayOfByte4['¬'] = -86;
    arrayOfByte4['­'] = 24;
    arrayOfByte4['®'] = -66;
    arrayOfByte4['¯'] = 27;
    arrayOfByte4['°'] = -4;
    arrayOfByte4['±'] = 86;
    arrayOfByte4['²'] = 62;
    arrayOfByte4['³'] = 75;
    arrayOfByte4['´'] = -58;
    arrayOfByte4['µ'] = -46;
    arrayOfByte4['¶'] = 121;
    arrayOfByte4['·'] = 32;
    arrayOfByte4['¸'] = -102;
    arrayOfByte4['¹'] = -37;
    arrayOfByte4['º'] = -64;
    arrayOfByte4['»'] = -2;
    arrayOfByte4['¼'] = 120;
    arrayOfByte4['½'] = -51;
    arrayOfByte4['¾'] = 90;
    arrayOfByte4['¿'] = -12;
    arrayOfByte4['À'] = 31;
    arrayOfByte4['Á'] = -35;
    arrayOfByte4['Â'] = -88;
    arrayOfByte4['Ã'] = 51;
    arrayOfByte4['Ä'] = -120;
    arrayOfByte4['Å'] = 7;
    arrayOfByte4['Æ'] = -57;
    arrayOfByte4['Ç'] = 49;
    arrayOfByte4['È'] = -79;
    arrayOfByte4['É'] = 18;
    arrayOfByte4['Ê'] = 16;
    arrayOfByte4['Ë'] = 89;
    arrayOfByte4['Ì'] = 39;
    arrayOfByte4['Í'] = -128;
    arrayOfByte4['Î'] = -20;
    arrayOfByte4['Ï'] = 95;
    arrayOfByte4['Ð'] = 96;
    arrayOfByte4['Ñ'] = 81;
    arrayOfByte4['Ò'] = 127;
    arrayOfByte4['Ó'] = -87;
    arrayOfByte4['Ô'] = 25;
    arrayOfByte4['Õ'] = -75;
    arrayOfByte4['Ö'] = 74;
    arrayOfByte4['×'] = 13;
    arrayOfByte4['Ø'] = 45;
    arrayOfByte4['Ù'] = -27;
    arrayOfByte4['Ú'] = 122;
    arrayOfByte4['Û'] = -97;
    arrayOfByte4['Ü'] = -109;
    arrayOfByte4['Ý'] = -55;
    arrayOfByte4['Þ'] = -100;
    arrayOfByte4['ß'] = -17;
    arrayOfByte4['à'] = -96;
    arrayOfByte4['á'] = -32;
    arrayOfByte4['â'] = 59;
    arrayOfByte4['ã'] = 77;
    arrayOfByte4['ä'] = -82;
    arrayOfByte4['å'] = 42;
    arrayOfByte4['æ'] = -11;
    arrayOfByte4['ç'] = -80;
    arrayOfByte4['è'] = -56;
    arrayOfByte4['é'] = -21;
    arrayOfByte4['ê'] = -69;
    arrayOfByte4['ë'] = 60;
    arrayOfByte4['ì'] = -125;
    arrayOfByte4['í'] = 83;
    arrayOfByte4['î'] = -103;
    arrayOfByte4['ï'] = 97;
    arrayOfByte4['ð'] = 23;
    arrayOfByte4['ñ'] = 43;
    arrayOfByte4['ò'] = 4;
    arrayOfByte4['ó'] = 126;
    arrayOfByte4['ô'] = -70;
    arrayOfByte4['õ'] = 119;
    arrayOfByte4['ö'] = -42;
    arrayOfByte4['÷'] = 38;
    arrayOfByte4['ø'] = -31;
    arrayOfByte4['ù'] = 105;
    arrayOfByte4['ú'] = 20;
    arrayOfByte4['û'] = 99;
    arrayOfByte4['ü'] = 85;
    arrayOfByte4['ý'] = 33;
    arrayOfByte4['þ'] = 12;
    arrayOfByte4['ÿ'] = 125;
    Si = arrayOfByte4;
    rcon = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145 };
    byte[][] arrayOfByte5 = new byte[5][];
    byte[] arrayOfByte6 = new byte[4];
    arrayOfByte6[1] = 8;
    arrayOfByte6[2] = 16;
    arrayOfByte6[3] = 24;
    arrayOfByte5[0] = arrayOfByte6;
    byte[] arrayOfByte7 = new byte[4];
    arrayOfByte7[1] = 8;
    arrayOfByte7[2] = 16;
    arrayOfByte7[3] = 24;
    arrayOfByte5[1] = arrayOfByte7;
    byte[] arrayOfByte8 = new byte[4];
    arrayOfByte8[1] = 8;
    arrayOfByte8[2] = 16;
    arrayOfByte8[3] = 24;
    arrayOfByte5[2] = arrayOfByte8;
    byte[] arrayOfByte9 = new byte[4];
    arrayOfByte9[1] = 8;
    arrayOfByte9[2] = 16;
    arrayOfByte9[3] = 32;
    arrayOfByte5[3] = arrayOfByte9;
    byte[] arrayOfByte10 = new byte[4];
    arrayOfByte10[1] = 8;
    arrayOfByte10[2] = 24;
    arrayOfByte10[3] = 32;
    arrayOfByte5[4] = arrayOfByte10;
    shifts0 = arrayOfByte5;
    byte[][] arrayOfByte11 = new byte[5][];
    byte[] arrayOfByte12 = new byte[4];
    arrayOfByte12[1] = 24;
    arrayOfByte12[2] = 16;
    arrayOfByte12[3] = 8;
    arrayOfByte11[0] = arrayOfByte12;
    byte[] arrayOfByte13 = new byte[4];
    arrayOfByte13[1] = 32;
    arrayOfByte13[2] = 24;
    arrayOfByte13[3] = 16;
    arrayOfByte11[1] = arrayOfByte13;
    byte[] arrayOfByte14 = new byte[4];
    arrayOfByte14[1] = 40;
    arrayOfByte14[2] = 32;
    arrayOfByte14[3] = 24;
    arrayOfByte11[2] = arrayOfByte14;
    byte[] arrayOfByte15 = new byte[4];
    arrayOfByte15[1] = 48;
    arrayOfByte15[2] = 40;
    arrayOfByte15[3] = 24;
    arrayOfByte11[3] = arrayOfByte15;
    byte[] arrayOfByte16 = new byte[4];
    arrayOfByte16[1] = 56;
    arrayOfByte16[2] = 40;
    arrayOfByte16[3] = 32;
    arrayOfByte11[4] = arrayOfByte16;
  }

  public RijndaelEngine()
  {
    this(128);
  }

  public RijndaelEngine(int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("unknown blocksize to Rijndael");
    case 128:
      this.BC = 32;
      this.BC_MASK = 4294967295L;
      this.shifts0SC = shifts0[0];
      this.shifts1SC = shifts1[0];
    case 160:
    case 192:
    case 224:
    case 256:
    }
    while (true)
    {
      this.blockBits = paramInt;
      return;
      this.BC = 40;
      this.BC_MASK = 1099511627775L;
      this.shifts0SC = shifts0[1];
      this.shifts1SC = shifts1[1];
      continue;
      this.BC = 48;
      this.BC_MASK = 281474976710655L;
      this.shifts0SC = shifts0[2];
      this.shifts1SC = shifts1[2];
      continue;
      this.BC = 56;
      this.BC_MASK = 72057594037927935L;
      this.shifts0SC = shifts0[3];
      this.shifts1SC = shifts1[3];
      continue;
      this.BC = 64;
      this.BC_MASK = -1L;
      this.shifts0SC = shifts0[4];
      this.shifts1SC = shifts1[4];
    }
  }

  private void InvMixColumn()
  {
    long l1 = 0L;
    long l2 = l1;
    long l3 = l1;
    long l4 = l1;
    int i = 0;
    if (i >= this.BC)
    {
      this.A0 = l4;
      this.A1 = l3;
      this.A2 = l2;
      this.A3 = l1;
      return;
    }
    int j = (int)(0xFF & this.A0 >> i);
    int k = (int)(0xFF & this.A1 >> i);
    int m = (int)(0xFF & this.A2 >> i);
    int n = (int)(0xFF & this.A3 >> i);
    int i1;
    label122: int i2;
    label143: int i3;
    if (j != 0)
    {
      i1 = 0xFF & logtable[(j & 0xFF)];
      if (k == 0)
        break label353;
      i2 = 0xFF & logtable[(k & 0xFF)];
      if (m == 0)
        break label359;
      i3 = 0xFF & logtable[(m & 0xFF)];
      label164: if (n == 0)
        break label365;
    }
    label353: label359: label365: for (int i4 = 0xFF & logtable[(n & 0xFF)]; ; i4 = -1)
    {
      l4 |= (0xFF & (mul0xe(i1) ^ mul0xb(i2) ^ mul0xd(i3) ^ mul0x9(i4))) << i;
      l3 |= (0xFF & (mul0xe(i2) ^ mul0xb(i3) ^ mul0xd(i4) ^ mul0x9(i1))) << i;
      l2 |= (0xFF & (mul0xe(i3) ^ mul0xb(i4) ^ mul0xd(i1) ^ mul0x9(i2))) << i;
      l1 |= (0xFF & (mul0xe(i4) ^ mul0xb(i1) ^ mul0xd(i2) ^ mul0x9(i3))) << i;
      i += 8;
      break;
      i1 = -1;
      break label122;
      i2 = -1;
      break label143;
      i3 = -1;
      break label164;
    }
  }

  private void KeyAddition(long[] paramArrayOfLong)
  {
    this.A0 ^= paramArrayOfLong[0];
    this.A1 ^= paramArrayOfLong[1];
    this.A2 ^= paramArrayOfLong[2];
    this.A3 ^= paramArrayOfLong[3];
  }

  private void MixColumn()
  {
    long l1 = 0L;
    long l2 = l1;
    long l3 = l1;
    long l4 = l1;
    for (int i = 0; ; i += 8)
    {
      if (i >= this.BC)
      {
        this.A0 = l4;
        this.A1 = l3;
        this.A2 = l2;
        this.A3 = l1;
        return;
      }
      int j = (int)(0xFF & this.A0 >> i);
      int k = (int)(0xFF & this.A1 >> i);
      int m = (int)(0xFF & this.A2 >> i);
      int n = (int)(0xFF & this.A3 >> i);
      l4 |= (0xFF & (n ^ (m ^ (mul0x2(j) ^ mul0x3(k))))) << i;
      l3 |= (0xFF & (j ^ (n ^ (mul0x2(k) ^ mul0x3(m))))) << i;
      l2 |= (0xFF & (k ^ (j ^ (mul0x2(m) ^ mul0x3(n))))) << i;
      l1 |= (0xFF & (m ^ (k ^ (mul0x2(n) ^ mul0x3(j))))) << i;
    }
  }

  private void ShiftRow(byte[] paramArrayOfByte)
  {
    this.A1 = shift(this.A1, paramArrayOfByte[1]);
    this.A2 = shift(this.A2, paramArrayOfByte[2]);
    this.A3 = shift(this.A3, paramArrayOfByte[3]);
  }

  private void Substitution(byte[] paramArrayOfByte)
  {
    this.A0 = applyS(this.A0, paramArrayOfByte);
    this.A1 = applyS(this.A1, paramArrayOfByte);
    this.A2 = applyS(this.A2, paramArrayOfByte);
    this.A3 = applyS(this.A3, paramArrayOfByte);
  }

  private long applyS(long paramLong, byte[] paramArrayOfByte)
  {
    long l = 0L;
    for (int i = 0; ; i += 8)
    {
      if (i >= this.BC)
        return l;
      l |= (0xFF & paramArrayOfByte[((int)(0xFF & paramLong >> i))]) << i;
    }
  }

  private void decryptBlock(long[][] paramArrayOfLong)
  {
    KeyAddition(paramArrayOfLong[this.ROUNDS]);
    Substitution(Si);
    ShiftRow(this.shifts1SC);
    for (int i = -1 + this.ROUNDS; ; i--)
    {
      if (i <= 0)
      {
        KeyAddition(paramArrayOfLong[0]);
        return;
      }
      KeyAddition(paramArrayOfLong[i]);
      InvMixColumn();
      Substitution(Si);
      ShiftRow(this.shifts1SC);
    }
  }

  private void encryptBlock(long[][] paramArrayOfLong)
  {
    KeyAddition(paramArrayOfLong[0]);
    for (int i = 1; ; i++)
    {
      if (i >= this.ROUNDS)
      {
        Substitution(S);
        ShiftRow(this.shifts0SC);
        KeyAddition(paramArrayOfLong[this.ROUNDS]);
        return;
      }
      Substitution(S);
      ShiftRow(this.shifts0SC);
      MixColumn();
      KeyAddition(paramArrayOfLong[i]);
    }
  }

  private long[][] generateWorkingKey(byte[] paramArrayOfByte)
  {
    int i = 8 * paramArrayOfByte.length;
    int[] arrayOfInt1 = { 4, 64 };
    byte[][] arrayOfByte = (byte[][])Array.newInstance(Byte.TYPE, arrayOfInt1);
    int[] arrayOfInt2 = { 15, 4 };
    long[][] arrayOfLong = (long[][])Array.newInstance(Long.TYPE, arrayOfInt2);
    int j;
    label138: int k;
    int m;
    switch (i)
    {
    default:
      throw new IllegalArgumentException("Key length not 128/160/192/224/256 bits.");
    case 128:
      j = 4;
      if (i >= this.blockBits)
      {
        this.ROUNDS = (j + 6);
        k = 0;
        m = 0;
      }
      break;
    case 160:
    case 192:
    case 224:
    case 256:
    }
    int i2;
    int i3;
    int i4;
    while (true)
    {
      if (m >= paramArrayOfByte.length)
      {
        i2 = 0;
        i3 = 0;
        i4 = 0;
        if (i3 < j)
        {
          int i17 = (1 + this.ROUNDS) * (this.BC / 8);
          i4 = 0;
          if (i2 < i17)
            break label299;
        }
        if (i2 < (1 + this.ROUNDS) * (this.BC / 8))
          break label373;
        return arrayOfLong;
        j = 5;
        break;
        j = 6;
        break;
        j = 7;
        break;
        j = 8;
        break;
        this.ROUNDS = (6 + this.BC / 8);
        break label138;
      }
      byte[] arrayOfByte1 = arrayOfByte[(m % 4)];
      int n = m / 4;
      int i1 = k + 1;
      arrayOfByte1[n] = paramArrayOfByte[k];
      m++;
      k = i1;
    }
    label299: for (int i18 = 0; ; i18++)
    {
      if (i18 >= 4)
      {
        i3++;
        i2++;
        break;
      }
      long[] arrayOfLong2 = arrayOfLong[(i2 / (this.BC / 8))];
      arrayOfLong2[i18] |= (0xFF & arrayOfByte[i18][i3]) << i2 * 8 % this.BC;
    }
    label373: int i15;
    int i13;
    for (int i5 = 0; ; i5++)
    {
      if (i5 >= 4)
      {
        byte[] arrayOfByte3 = arrayOfByte[0];
        int i6 = arrayOfByte3[0];
        int[] arrayOfInt3 = rcon;
        int i7 = i4 + 1;
        arrayOfByte3[0] = ((byte)(i6 ^ arrayOfInt3[i4]));
        if (j > 6)
          break label566;
        i15 = 1;
        if (i15 < j)
          break label516;
        i13 = 0;
        if ((i13 < j) && (i2 < (1 + this.ROUNDS) * (this.BC / 8)))
          break label732;
        i4 = i7;
        break;
      }
      byte[] arrayOfByte2 = arrayOfByte[i5];
      arrayOfByte2[0] = ((byte)(arrayOfByte2[0] ^ S[(0xFF & arrayOfByte[((i5 + 1) % 4)][(j - 1)])]));
    }
    label516: for (int i16 = 0; ; i16++)
    {
      if (i16 >= 4)
      {
        i15++;
        break;
      }
      byte[] arrayOfByte7 = arrayOfByte[i16];
      arrayOfByte7[i15] = ((byte)(arrayOfByte7[i15] ^ arrayOfByte[i16][(i15 - 1)]));
    }
    label566: int i8 = 1;
    int i10;
    label578: int i11;
    if (i8 >= 4)
    {
      i10 = 0;
      if (i10 < 4)
        break label659;
      i11 = 5;
      label587: if (i11 >= j);
    }
    for (int i12 = 0; ; i12++)
    {
      if (i12 >= 4)
      {
        i11++;
        break label587;
        break;
        for (int i9 = 0; ; i9++)
        {
          if (i9 >= 4)
          {
            i8++;
            break;
          }
          byte[] arrayOfByte4 = arrayOfByte[i9];
          arrayOfByte4[i8] = ((byte)(arrayOfByte4[i8] ^ arrayOfByte[i9][(i8 - 1)]));
        }
        label659: byte[] arrayOfByte5 = arrayOfByte[i10];
        arrayOfByte5[4] = ((byte)(arrayOfByte5[4] ^ S[(0xFF & arrayOfByte[i10][3])]));
        i10++;
        break label578;
      }
      byte[] arrayOfByte6 = arrayOfByte[i12];
      arrayOfByte6[i11] = ((byte)(arrayOfByte6[i11] ^ arrayOfByte[i12][(i11 - 1)]));
    }
    label732: for (int i14 = 0; ; i14++)
    {
      if (i14 >= 4)
      {
        i13++;
        i2++;
        break;
      }
      long[] arrayOfLong1 = arrayOfLong[(i2 / (this.BC / 8))];
      arrayOfLong1[i14] |= (0xFF & arrayOfByte[i14][i13]) << i2 * 8 % this.BC;
    }
  }

  private byte mul0x2(int paramInt)
  {
    if (paramInt != 0)
      return aLogtable[(25 + (0xFF & logtable[paramInt]))];
    return 0;
  }

  private byte mul0x3(int paramInt)
  {
    if (paramInt != 0)
      return aLogtable[(1 + (0xFF & logtable[paramInt]))];
    return 0;
  }

  private byte mul0x9(int paramInt)
  {
    if (paramInt >= 0)
      return aLogtable[(paramInt + 199)];
    return 0;
  }

  private byte mul0xb(int paramInt)
  {
    if (paramInt >= 0)
      return aLogtable[(paramInt + 104)];
    return 0;
  }

  private byte mul0xd(int paramInt)
  {
    if (paramInt >= 0)
      return aLogtable[(paramInt + 238)];
    return 0;
  }

  private byte mul0xe(int paramInt)
  {
    if (paramInt >= 0)
      return aLogtable[(paramInt + 223)];
    return 0;
  }

  private void packBlock(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt;
    for (int j = 0; ; j += 8)
    {
      if (j == this.BC)
        return;
      int k = i + 1;
      paramArrayOfByte[i] = ((byte)(int)(this.A0 >> j));
      int m = k + 1;
      paramArrayOfByte[k] = ((byte)(int)(this.A1 >> j));
      int n = m + 1;
      paramArrayOfByte[m] = ((byte)(int)(this.A2 >> j));
      i = n + 1;
      paramArrayOfByte[n] = ((byte)(int)(this.A3 >> j));
    }
  }

  private long shift(long paramLong, int paramInt)
  {
    return (paramLong >>> paramInt | paramLong << this.BC - paramInt) & this.BC_MASK;
  }

  private void unpackBlock(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    this.A0 = (0xFF & paramArrayOfByte[paramInt]);
    int j = i + 1;
    this.A1 = (0xFF & paramArrayOfByte[i]);
    int k = j + 1;
    this.A2 = (0xFF & paramArrayOfByte[j]);
    int m = k + 1;
    this.A3 = (0xFF & paramArrayOfByte[k]);
    for (int n = 8; ; n += 8)
    {
      if (n == this.BC)
        return;
      long l1 = this.A0;
      int i1 = m + 1;
      this.A0 = (l1 | (0xFF & paramArrayOfByte[m]) << n);
      long l2 = this.A1;
      int i2 = i1 + 1;
      this.A1 = (l2 | (0xFF & paramArrayOfByte[i1]) << n);
      long l3 = this.A2;
      int i3 = i2 + 1;
      this.A2 = (l3 | (0xFF & paramArrayOfByte[i2]) << n);
      long l4 = this.A3;
      m = i3 + 1;
      this.A3 = (l4 | (0xFF & paramArrayOfByte[i3]) << n);
    }
  }

  public String getAlgorithmName()
  {
    return "Rijndael";
  }

  public int getBlockSize()
  {
    return this.BC / 2;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof KeyParameter))
    {
      this.workingKey = generateWorkingKey(((KeyParameter)paramCipherParameters).getKey());
      this.forEncryption = paramBoolean;
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to Rijndael init - " + paramCipherParameters.getClass().getName());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.workingKey == null)
      throw new IllegalStateException("Rijndael engine not initialised");
    if (paramInt1 + this.BC / 2 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + this.BC / 2 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    if (this.forEncryption)
    {
      unpackBlock(paramArrayOfByte1, paramInt1);
      encryptBlock(this.workingKey);
      packBlock(paramArrayOfByte2, paramInt2);
    }
    while (true)
    {
      return this.BC / 2;
      unpackBlock(paramArrayOfByte1, paramInt1);
      decryptBlock(this.workingKey);
      packBlock(paramArrayOfByte2, paramInt2);
    }
  }

  public void reset()
  {
  }
}