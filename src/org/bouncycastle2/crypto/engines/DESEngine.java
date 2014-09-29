package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class DESEngine
  implements BlockCipher
{
  protected static final int BLOCK_SIZE = 8;
  private static final int[] SP1;
  private static final int[] SP2;
  private static final int[] SP3;
  private static final int[] SP4;
  private static final int[] SP5;
  private static final int[] SP6;
  private static final int[] SP7;
  private static final int[] SP8 = arrayOfInt8;
  private static final int[] bigbyte;
  private static final short[] bytebit = { 128, 64, 32, 16, 8, 4, 2, 1 };
  private static final byte[] pc1;
  private static final byte[] pc2;
  private static final byte[] totrot;
  private int[] workingKey = null;

  static
  {
    bigbyte = new int[] { 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1 };
    byte[] arrayOfByte1 = new byte[56];
    arrayOfByte1[0] = 56;
    arrayOfByte1[1] = 48;
    arrayOfByte1[2] = 40;
    arrayOfByte1[3] = 32;
    arrayOfByte1[4] = 24;
    arrayOfByte1[5] = 16;
    arrayOfByte1[6] = 8;
    arrayOfByte1[8] = 57;
    arrayOfByte1[9] = 49;
    arrayOfByte1[10] = 41;
    arrayOfByte1[11] = 33;
    arrayOfByte1[12] = 25;
    arrayOfByte1[13] = 17;
    arrayOfByte1[14] = 9;
    arrayOfByte1[15] = 1;
    arrayOfByte1[16] = 58;
    arrayOfByte1[17] = 50;
    arrayOfByte1[18] = 42;
    arrayOfByte1[19] = 34;
    arrayOfByte1[20] = 26;
    arrayOfByte1[21] = 18;
    arrayOfByte1[22] = 10;
    arrayOfByte1[23] = 2;
    arrayOfByte1[24] = 59;
    arrayOfByte1[25] = 51;
    arrayOfByte1[26] = 43;
    arrayOfByte1[27] = 35;
    arrayOfByte1[28] = 62;
    arrayOfByte1[29] = 54;
    arrayOfByte1[30] = 46;
    arrayOfByte1[31] = 38;
    arrayOfByte1[32] = 30;
    arrayOfByte1[33] = 22;
    arrayOfByte1[34] = 14;
    arrayOfByte1[35] = 6;
    arrayOfByte1[36] = 61;
    arrayOfByte1[37] = 53;
    arrayOfByte1[38] = 45;
    arrayOfByte1[39] = 37;
    arrayOfByte1[40] = 29;
    arrayOfByte1[41] = 21;
    arrayOfByte1[42] = 13;
    arrayOfByte1[43] = 5;
    arrayOfByte1[44] = 60;
    arrayOfByte1[45] = 52;
    arrayOfByte1[46] = 44;
    arrayOfByte1[47] = 36;
    arrayOfByte1[48] = 28;
    arrayOfByte1[49] = 20;
    arrayOfByte1[50] = 12;
    arrayOfByte1[51] = 4;
    arrayOfByte1[52] = 27;
    arrayOfByte1[53] = 19;
    arrayOfByte1[54] = 11;
    arrayOfByte1[55] = 3;
    pc1 = arrayOfByte1;
    totrot = new byte[] { 1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 19, 21, 23, 25, 27, 28 };
    byte[] arrayOfByte2 = new byte[48];
    arrayOfByte2[0] = 13;
    arrayOfByte2[1] = 16;
    arrayOfByte2[2] = 10;
    arrayOfByte2[3] = 23;
    arrayOfByte2[5] = 4;
    arrayOfByte2[6] = 2;
    arrayOfByte2[7] = 27;
    arrayOfByte2[8] = 14;
    arrayOfByte2[9] = 5;
    arrayOfByte2[10] = 20;
    arrayOfByte2[11] = 9;
    arrayOfByte2[12] = 22;
    arrayOfByte2[13] = 18;
    arrayOfByte2[14] = 11;
    arrayOfByte2[15] = 3;
    arrayOfByte2[16] = 25;
    arrayOfByte2[17] = 7;
    arrayOfByte2[18] = 15;
    arrayOfByte2[19] = 6;
    arrayOfByte2[20] = 26;
    arrayOfByte2[21] = 19;
    arrayOfByte2[22] = 12;
    arrayOfByte2[23] = 1;
    arrayOfByte2[24] = 40;
    arrayOfByte2[25] = 51;
    arrayOfByte2[26] = 30;
    arrayOfByte2[27] = 36;
    arrayOfByte2[28] = 46;
    arrayOfByte2[29] = 54;
    arrayOfByte2[30] = 29;
    arrayOfByte2[31] = 39;
    arrayOfByte2[32] = 50;
    arrayOfByte2[33] = 44;
    arrayOfByte2[34] = 32;
    arrayOfByte2[35] = 47;
    arrayOfByte2[36] = 43;
    arrayOfByte2[37] = 48;
    arrayOfByte2[38] = 38;
    arrayOfByte2[39] = 55;
    arrayOfByte2[40] = 33;
    arrayOfByte2[41] = 52;
    arrayOfByte2[42] = 45;
    arrayOfByte2[43] = 41;
    arrayOfByte2[44] = 49;
    arrayOfByte2[45] = 35;
    arrayOfByte2[46] = 28;
    arrayOfByte2[47] = 31;
    pc2 = arrayOfByte2;
    int[] arrayOfInt1 = new int[64];
    arrayOfInt1[0] = 16843776;
    arrayOfInt1[2] = 65536;
    arrayOfInt1[3] = 16843780;
    arrayOfInt1[4] = 16842756;
    arrayOfInt1[5] = 66564;
    arrayOfInt1[6] = 4;
    arrayOfInt1[7] = 65536;
    arrayOfInt1[8] = 1024;
    arrayOfInt1[9] = 16843776;
    arrayOfInt1[10] = 16843780;
    arrayOfInt1[11] = 1024;
    arrayOfInt1[12] = 16778244;
    arrayOfInt1[13] = 16842756;
    arrayOfInt1[14] = 16777216;
    arrayOfInt1[15] = 4;
    arrayOfInt1[16] = 1028;
    arrayOfInt1[17] = 16778240;
    arrayOfInt1[18] = 16778240;
    arrayOfInt1[19] = 66560;
    arrayOfInt1[20] = 66560;
    arrayOfInt1[21] = 16842752;
    arrayOfInt1[22] = 16842752;
    arrayOfInt1[23] = 16778244;
    arrayOfInt1[24] = 65540;
    arrayOfInt1[25] = 16777220;
    arrayOfInt1[26] = 16777220;
    arrayOfInt1[27] = 65540;
    arrayOfInt1[29] = 1028;
    arrayOfInt1[30] = 66564;
    arrayOfInt1[31] = 16777216;
    arrayOfInt1[32] = 65536;
    arrayOfInt1[33] = 16843780;
    arrayOfInt1[34] = 4;
    arrayOfInt1[35] = 16842752;
    arrayOfInt1[36] = 16843776;
    arrayOfInt1[37] = 16777216;
    arrayOfInt1[38] = 16777216;
    arrayOfInt1[39] = 1024;
    arrayOfInt1[40] = 16842756;
    arrayOfInt1[41] = 65536;
    arrayOfInt1[42] = 66560;
    arrayOfInt1[43] = 16777220;
    arrayOfInt1[44] = 1024;
    arrayOfInt1[45] = 4;
    arrayOfInt1[46] = 16778244;
    arrayOfInt1[47] = 66564;
    arrayOfInt1[48] = 16843780;
    arrayOfInt1[49] = 65540;
    arrayOfInt1[50] = 16842752;
    arrayOfInt1[51] = 16778244;
    arrayOfInt1[52] = 16777220;
    arrayOfInt1[53] = 1028;
    arrayOfInt1[54] = 66564;
    arrayOfInt1[55] = 16843776;
    arrayOfInt1[56] = 1028;
    arrayOfInt1[57] = 16778240;
    arrayOfInt1[58] = 16778240;
    arrayOfInt1[60] = 65540;
    arrayOfInt1[61] = 66560;
    arrayOfInt1[63] = 16842756;
    SP1 = arrayOfInt1;
    int[] arrayOfInt2 = new int[64];
    arrayOfInt2[0] = -2146402272;
    arrayOfInt2[1] = -2147450880;
    arrayOfInt2[2] = 32768;
    arrayOfInt2[3] = 1081376;
    arrayOfInt2[4] = 1048576;
    arrayOfInt2[5] = 32;
    arrayOfInt2[6] = -2146435040;
    arrayOfInt2[7] = -2147450848;
    arrayOfInt2[8] = -2147483616;
    arrayOfInt2[9] = -2146402272;
    arrayOfInt2[10] = -2146402304;
    arrayOfInt2[11] = -2147483648;
    arrayOfInt2[12] = -2147450880;
    arrayOfInt2[13] = 1048576;
    arrayOfInt2[14] = 32;
    arrayOfInt2[15] = -2146435040;
    arrayOfInt2[16] = 1081344;
    arrayOfInt2[17] = 1048608;
    arrayOfInt2[18] = -2147450848;
    arrayOfInt2[20] = -2147483648;
    arrayOfInt2[21] = 32768;
    arrayOfInt2[22] = 1081376;
    arrayOfInt2[23] = -2146435072;
    arrayOfInt2[24] = 1048608;
    arrayOfInt2[25] = -2147483616;
    arrayOfInt2[27] = 1081344;
    arrayOfInt2[28] = 32800;
    arrayOfInt2[29] = -2146402304;
    arrayOfInt2[30] = -2146435072;
    arrayOfInt2[31] = 32800;
    arrayOfInt2[33] = 1081376;
    arrayOfInt2[34] = -2146435040;
    arrayOfInt2[35] = 1048576;
    arrayOfInt2[36] = -2147450848;
    arrayOfInt2[37] = -2146435072;
    arrayOfInt2[38] = -2146402304;
    arrayOfInt2[39] = 32768;
    arrayOfInt2[40] = -2146435072;
    arrayOfInt2[41] = -2147450880;
    arrayOfInt2[42] = 32;
    arrayOfInt2[43] = -2146402272;
    arrayOfInt2[44] = 1081376;
    arrayOfInt2[45] = 32;
    arrayOfInt2[46] = 32768;
    arrayOfInt2[47] = -2147483648;
    arrayOfInt2[48] = 32800;
    arrayOfInt2[49] = -2146402304;
    arrayOfInt2[50] = 1048576;
    arrayOfInt2[51] = -2147483616;
    arrayOfInt2[52] = 1048608;
    arrayOfInt2[53] = -2147450848;
    arrayOfInt2[54] = -2147483616;
    arrayOfInt2[55] = 1048608;
    arrayOfInt2[56] = 1081344;
    arrayOfInt2[58] = -2147450880;
    arrayOfInt2[59] = 32800;
    arrayOfInt2[60] = -2147483648;
    arrayOfInt2[61] = -2146435040;
    arrayOfInt2[62] = -2146402272;
    arrayOfInt2[63] = 1081344;
    SP2 = arrayOfInt2;
    int[] arrayOfInt3 = new int[64];
    arrayOfInt3[0] = 520;
    arrayOfInt3[1] = 134349312;
    arrayOfInt3[3] = 134348808;
    arrayOfInt3[4] = 134218240;
    arrayOfInt3[6] = 131592;
    arrayOfInt3[7] = 134218240;
    arrayOfInt3[8] = 131080;
    arrayOfInt3[9] = 134217736;
    arrayOfInt3[10] = 134217736;
    arrayOfInt3[11] = 131072;
    arrayOfInt3[12] = 134349320;
    arrayOfInt3[13] = 131080;
    arrayOfInt3[14] = 134348800;
    arrayOfInt3[15] = 520;
    arrayOfInt3[16] = 134217728;
    arrayOfInt3[17] = 8;
    arrayOfInt3[18] = 134349312;
    arrayOfInt3[19] = 512;
    arrayOfInt3[20] = 131584;
    arrayOfInt3[21] = 134348800;
    arrayOfInt3[22] = 134348808;
    arrayOfInt3[23] = 131592;
    arrayOfInt3[24] = 134218248;
    arrayOfInt3[25] = 131584;
    arrayOfInt3[26] = 131072;
    arrayOfInt3[27] = 134218248;
    arrayOfInt3[28] = 8;
    arrayOfInt3[29] = 134349320;
    arrayOfInt3[30] = 512;
    arrayOfInt3[31] = 134217728;
    arrayOfInt3[32] = 134349312;
    arrayOfInt3[33] = 134217728;
    arrayOfInt3[34] = 131080;
    arrayOfInt3[35] = 520;
    arrayOfInt3[36] = 131072;
    arrayOfInt3[37] = 134349312;
    arrayOfInt3[38] = 134218240;
    arrayOfInt3[40] = 512;
    arrayOfInt3[41] = 131080;
    arrayOfInt3[42] = 134349320;
    arrayOfInt3[43] = 134218240;
    arrayOfInt3[44] = 134217736;
    arrayOfInt3[45] = 512;
    arrayOfInt3[47] = 134348808;
    arrayOfInt3[48] = 134218248;
    arrayOfInt3[49] = 131072;
    arrayOfInt3[50] = 134217728;
    arrayOfInt3[51] = 134349320;
    arrayOfInt3[52] = 8;
    arrayOfInt3[53] = 131592;
    arrayOfInt3[54] = 131584;
    arrayOfInt3[55] = 134217736;
    arrayOfInt3[56] = 134348800;
    arrayOfInt3[57] = 134218248;
    arrayOfInt3[58] = 520;
    arrayOfInt3[59] = 134348800;
    arrayOfInt3[60] = 131592;
    arrayOfInt3[61] = 8;
    arrayOfInt3[62] = 134348808;
    arrayOfInt3[63] = 131584;
    SP3 = arrayOfInt3;
    int[] arrayOfInt4 = new int[64];
    arrayOfInt4[0] = 8396801;
    arrayOfInt4[1] = 8321;
    arrayOfInt4[2] = 8321;
    arrayOfInt4[3] = 128;
    arrayOfInt4[4] = 8396928;
    arrayOfInt4[5] = 8388737;
    arrayOfInt4[6] = 8388609;
    arrayOfInt4[7] = 8193;
    arrayOfInt4[9] = 8396800;
    arrayOfInt4[10] = 8396800;
    arrayOfInt4[11] = 8396929;
    arrayOfInt4[12] = 129;
    arrayOfInt4[14] = 8388736;
    arrayOfInt4[15] = 8388609;
    arrayOfInt4[16] = 1;
    arrayOfInt4[17] = 8192;
    arrayOfInt4[18] = 8388608;
    arrayOfInt4[19] = 8396801;
    arrayOfInt4[20] = 128;
    arrayOfInt4[21] = 8388608;
    arrayOfInt4[22] = 8193;
    arrayOfInt4[23] = 8320;
    arrayOfInt4[24] = 8388737;
    arrayOfInt4[25] = 1;
    arrayOfInt4[26] = 8320;
    arrayOfInt4[27] = 8388736;
    arrayOfInt4[28] = 8192;
    arrayOfInt4[29] = 8396928;
    arrayOfInt4[30] = 8396929;
    arrayOfInt4[31] = 129;
    arrayOfInt4[32] = 8388736;
    arrayOfInt4[33] = 8388609;
    arrayOfInt4[34] = 8396800;
    arrayOfInt4[35] = 8396929;
    arrayOfInt4[36] = 129;
    arrayOfInt4[39] = 8396800;
    arrayOfInt4[40] = 8320;
    arrayOfInt4[41] = 8388736;
    arrayOfInt4[42] = 8388737;
    arrayOfInt4[43] = 1;
    arrayOfInt4[44] = 8396801;
    arrayOfInt4[45] = 8321;
    arrayOfInt4[46] = 8321;
    arrayOfInt4[47] = 128;
    arrayOfInt4[48] = 8396929;
    arrayOfInt4[49] = 129;
    arrayOfInt4[50] = 1;
    arrayOfInt4[51] = 8192;
    arrayOfInt4[52] = 8388609;
    arrayOfInt4[53] = 8193;
    arrayOfInt4[54] = 8396928;
    arrayOfInt4[55] = 8388737;
    arrayOfInt4[56] = 8193;
    arrayOfInt4[57] = 8320;
    arrayOfInt4[58] = 8388608;
    arrayOfInt4[59] = 8396801;
    arrayOfInt4[60] = 128;
    arrayOfInt4[61] = 8388608;
    arrayOfInt4[62] = 8192;
    arrayOfInt4[63] = 8396928;
    SP4 = arrayOfInt4;
    int[] arrayOfInt5 = new int[64];
    arrayOfInt5[0] = 256;
    arrayOfInt5[1] = 34078976;
    arrayOfInt5[2] = 34078720;
    arrayOfInt5[3] = 1107296512;
    arrayOfInt5[4] = 524288;
    arrayOfInt5[5] = 256;
    arrayOfInt5[6] = 1073741824;
    arrayOfInt5[7] = 34078720;
    arrayOfInt5[8] = 1074266368;
    arrayOfInt5[9] = 524288;
    arrayOfInt5[10] = 33554688;
    arrayOfInt5[11] = 1074266368;
    arrayOfInt5[12] = 1107296512;
    arrayOfInt5[13] = 1107820544;
    arrayOfInt5[14] = 524544;
    arrayOfInt5[15] = 1073741824;
    arrayOfInt5[16] = 33554432;
    arrayOfInt5[17] = 1074266112;
    arrayOfInt5[18] = 1074266112;
    arrayOfInt5[20] = 1073742080;
    arrayOfInt5[21] = 1107820800;
    arrayOfInt5[22] = 1107820800;
    arrayOfInt5[23] = 33554688;
    arrayOfInt5[24] = 1107820544;
    arrayOfInt5[25] = 1073742080;
    arrayOfInt5[27] = 1107296256;
    arrayOfInt5[28] = 34078976;
    arrayOfInt5[29] = 33554432;
    arrayOfInt5[30] = 1107296256;
    arrayOfInt5[31] = 524544;
    arrayOfInt5[32] = 524288;
    arrayOfInt5[33] = 1107296512;
    arrayOfInt5[34] = 256;
    arrayOfInt5[35] = 33554432;
    arrayOfInt5[36] = 1073741824;
    arrayOfInt5[37] = 34078720;
    arrayOfInt5[38] = 1107296512;
    arrayOfInt5[39] = 1074266368;
    arrayOfInt5[40] = 33554688;
    arrayOfInt5[41] = 1073741824;
    arrayOfInt5[42] = 1107820544;
    arrayOfInt5[43] = 34078976;
    arrayOfInt5[44] = 1074266368;
    arrayOfInt5[45] = 256;
    arrayOfInt5[46] = 33554432;
    arrayOfInt5[47] = 1107820544;
    arrayOfInt5[48] = 1107820800;
    arrayOfInt5[49] = 524544;
    arrayOfInt5[50] = 1107296256;
    arrayOfInt5[51] = 1107820800;
    arrayOfInt5[52] = 34078720;
    arrayOfInt5[54] = 1074266112;
    arrayOfInt5[55] = 1107296256;
    arrayOfInt5[56] = 524544;
    arrayOfInt5[57] = 33554688;
    arrayOfInt5[58] = 1073742080;
    arrayOfInt5[59] = 524288;
    arrayOfInt5[61] = 1074266112;
    arrayOfInt5[62] = 34078976;
    arrayOfInt5[63] = 1073742080;
    SP5 = arrayOfInt5;
    int[] arrayOfInt6 = new int[64];
    arrayOfInt6[0] = 536870928;
    arrayOfInt6[1] = 541065216;
    arrayOfInt6[2] = 16384;
    arrayOfInt6[3] = 541081616;
    arrayOfInt6[4] = 541065216;
    arrayOfInt6[5] = 16;
    arrayOfInt6[6] = 541081616;
    arrayOfInt6[7] = 4194304;
    arrayOfInt6[8] = 536887296;
    arrayOfInt6[9] = 4210704;
    arrayOfInt6[10] = 4194304;
    arrayOfInt6[11] = 536870928;
    arrayOfInt6[12] = 4194320;
    arrayOfInt6[13] = 536887296;
    arrayOfInt6[14] = 536870912;
    arrayOfInt6[15] = 16400;
    arrayOfInt6[17] = 4194320;
    arrayOfInt6[18] = 536887312;
    arrayOfInt6[19] = 16384;
    arrayOfInt6[20] = 4210688;
    arrayOfInt6[21] = 536887312;
    arrayOfInt6[22] = 16;
    arrayOfInt6[23] = 541065232;
    arrayOfInt6[24] = 541065232;
    arrayOfInt6[26] = 4210704;
    arrayOfInt6[27] = 541081600;
    arrayOfInt6[28] = 16400;
    arrayOfInt6[29] = 4210688;
    arrayOfInt6[30] = 541081600;
    arrayOfInt6[31] = 536870912;
    arrayOfInt6[32] = 536887296;
    arrayOfInt6[33] = 16;
    arrayOfInt6[34] = 541065232;
    arrayOfInt6[35] = 4210688;
    arrayOfInt6[36] = 541081616;
    arrayOfInt6[37] = 4194304;
    arrayOfInt6[38] = 16400;
    arrayOfInt6[39] = 536870928;
    arrayOfInt6[40] = 4194304;
    arrayOfInt6[41] = 536887296;
    arrayOfInt6[42] = 536870912;
    arrayOfInt6[43] = 16400;
    arrayOfInt6[44] = 536870928;
    arrayOfInt6[45] = 541081616;
    arrayOfInt6[46] = 4210688;
    arrayOfInt6[47] = 541065216;
    arrayOfInt6[48] = 4210704;
    arrayOfInt6[49] = 541081600;
    arrayOfInt6[51] = 541065232;
    arrayOfInt6[52] = 16;
    arrayOfInt6[53] = 16384;
    arrayOfInt6[54] = 541065216;
    arrayOfInt6[55] = 4210704;
    arrayOfInt6[56] = 16384;
    arrayOfInt6[57] = 4194320;
    arrayOfInt6[58] = 536887312;
    arrayOfInt6[60] = 541081600;
    arrayOfInt6[61] = 536870912;
    arrayOfInt6[62] = 4194320;
    arrayOfInt6[63] = 536887312;
    SP6 = arrayOfInt6;
    int[] arrayOfInt7 = new int[64];
    arrayOfInt7[0] = 2097152;
    arrayOfInt7[1] = 69206018;
    arrayOfInt7[2] = 67110914;
    arrayOfInt7[4] = 2048;
    arrayOfInt7[5] = 67110914;
    arrayOfInt7[6] = 2099202;
    arrayOfInt7[7] = 69208064;
    arrayOfInt7[8] = 69208066;
    arrayOfInt7[9] = 2097152;
    arrayOfInt7[11] = 67108866;
    arrayOfInt7[12] = 2;
    arrayOfInt7[13] = 67108864;
    arrayOfInt7[14] = 69206018;
    arrayOfInt7[15] = 2050;
    arrayOfInt7[16] = 67110912;
    arrayOfInt7[17] = 2099202;
    arrayOfInt7[18] = 2097154;
    arrayOfInt7[19] = 67110912;
    arrayOfInt7[20] = 67108866;
    arrayOfInt7[21] = 69206016;
    arrayOfInt7[22] = 69208064;
    arrayOfInt7[23] = 2097154;
    arrayOfInt7[24] = 69206016;
    arrayOfInt7[25] = 2048;
    arrayOfInt7[26] = 2050;
    arrayOfInt7[27] = 69208066;
    arrayOfInt7[28] = 2099200;
    arrayOfInt7[29] = 2;
    arrayOfInt7[30] = 67108864;
    arrayOfInt7[31] = 2099200;
    arrayOfInt7[32] = 67108864;
    arrayOfInt7[33] = 2099200;
    arrayOfInt7[34] = 2097152;
    arrayOfInt7[35] = 67110914;
    arrayOfInt7[36] = 67110914;
    arrayOfInt7[37] = 69206018;
    arrayOfInt7[38] = 69206018;
    arrayOfInt7[39] = 2;
    arrayOfInt7[40] = 2097154;
    arrayOfInt7[41] = 67108864;
    arrayOfInt7[42] = 67110912;
    arrayOfInt7[43] = 2097152;
    arrayOfInt7[44] = 69208064;
    arrayOfInt7[45] = 2050;
    arrayOfInt7[46] = 2099202;
    arrayOfInt7[47] = 69208064;
    arrayOfInt7[48] = 2050;
    arrayOfInt7[49] = 67108866;
    arrayOfInt7[50] = 69208066;
    arrayOfInt7[51] = 69206016;
    arrayOfInt7[52] = 2099200;
    arrayOfInt7[54] = 2;
    arrayOfInt7[55] = 69208066;
    arrayOfInt7[57] = 2099202;
    arrayOfInt7[58] = 69206016;
    arrayOfInt7[59] = 2048;
    arrayOfInt7[60] = 67108866;
    arrayOfInt7[61] = 67110912;
    arrayOfInt7[62] = 2048;
    arrayOfInt7[63] = 2097154;
    SP7 = arrayOfInt7;
    int[] arrayOfInt8 = new int[64];
    arrayOfInt8[0] = 268439616;
    arrayOfInt8[1] = 4096;
    arrayOfInt8[2] = 262144;
    arrayOfInt8[3] = 268701760;
    arrayOfInt8[4] = 268435456;
    arrayOfInt8[5] = 268439616;
    arrayOfInt8[6] = 64;
    arrayOfInt8[7] = 268435456;
    arrayOfInt8[8] = 262208;
    arrayOfInt8[9] = 268697600;
    arrayOfInt8[10] = 268701760;
    arrayOfInt8[11] = 266240;
    arrayOfInt8[12] = 268701696;
    arrayOfInt8[13] = 266304;
    arrayOfInt8[14] = 4096;
    arrayOfInt8[15] = 64;
    arrayOfInt8[16] = 268697600;
    arrayOfInt8[17] = 268435520;
    arrayOfInt8[18] = 268439552;
    arrayOfInt8[19] = 4160;
    arrayOfInt8[20] = 266240;
    arrayOfInt8[21] = 262208;
    arrayOfInt8[22] = 268697664;
    arrayOfInt8[23] = 268701696;
    arrayOfInt8[24] = 4160;
    arrayOfInt8[27] = 268697664;
    arrayOfInt8[28] = 268435520;
    arrayOfInt8[29] = 268439552;
    arrayOfInt8[30] = 266304;
    arrayOfInt8[31] = 262144;
    arrayOfInt8[32] = 266304;
    arrayOfInt8[33] = 262144;
    arrayOfInt8[34] = 268701696;
    arrayOfInt8[35] = 4096;
    arrayOfInt8[36] = 64;
    arrayOfInt8[37] = 268697664;
    arrayOfInt8[38] = 4096;
    arrayOfInt8[39] = 266304;
    arrayOfInt8[40] = 268439552;
    arrayOfInt8[41] = 64;
    arrayOfInt8[42] = 268435520;
    arrayOfInt8[43] = 268697600;
    arrayOfInt8[44] = 268697664;
    arrayOfInt8[45] = 268435456;
    arrayOfInt8[46] = 262144;
    arrayOfInt8[47] = 268439616;
    arrayOfInt8[49] = 268701760;
    arrayOfInt8[50] = 262208;
    arrayOfInt8[51] = 268435520;
    arrayOfInt8[52] = 268697600;
    arrayOfInt8[53] = 268439552;
    arrayOfInt8[54] = 268439616;
    arrayOfInt8[56] = 268701760;
    arrayOfInt8[57] = 266240;
    arrayOfInt8[58] = 266240;
    arrayOfInt8[59] = 4160;
    arrayOfInt8[60] = 4160;
    arrayOfInt8[61] = 262208;
    arrayOfInt8[62] = 268435456;
    arrayOfInt8[63] = 268701696;
  }

  protected void desFunc(int[] paramArrayOfInt, byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = (0xFF & paramArrayOfByte1[(paramInt1 + 0)]) << 24 | (0xFF & paramArrayOfByte1[(paramInt1 + 1)]) << 16 | (0xFF & paramArrayOfByte1[(paramInt1 + 2)]) << 8 | 0xFF & paramArrayOfByte1[(paramInt1 + 3)];
    int j = (0xFF & paramArrayOfByte1[(paramInt1 + 4)]) << 24 | (0xFF & paramArrayOfByte1[(paramInt1 + 5)]) << 16 | (0xFF & paramArrayOfByte1[(paramInt1 + 6)]) << 8 | 0xFF & paramArrayOfByte1[(paramInt1 + 7)];
    int k = 0xF0F0F0F & (j ^ i >>> 4);
    int m = j ^ k;
    int n = i ^ k << 4;
    int i1 = 0xFFFF & (m ^ n >>> 16);
    int i2 = m ^ i1;
    int i3 = n ^ i1 << 16;
    int i4 = 0x33333333 & (i3 ^ i2 >>> 2);
    int i5 = i3 ^ i4;
    int i6 = i2 ^ i4 << 2;
    int i7 = 0xFF00FF & (i5 ^ i6 >>> 8);
    int i8 = i5 ^ i7;
    int i9 = i6 ^ i7 << 8;
    int i10 = 0xFFFFFFFF & (i9 << 1 | 0x1 & i9 >>> 31);
    int i11 = 0xAAAAAAAA & (i8 ^ i10);
    int i12 = i8 ^ i11;
    int i13 = i10 ^ i11;
    int i14 = 0xFFFFFFFF & (i12 << 1 | 0x1 & i12 >>> 31);
    for (int i15 = 0; ; i15++)
    {
      if (i15 >= 8)
      {
        int i22 = i13 << 31 | i13 >>> 1;
        int i23 = 0xAAAAAAAA & (i14 ^ i22);
        int i24 = i14 ^ i23;
        int i25 = i22 ^ i23;
        int i26 = i24 << 31 | i24 >>> 1;
        int i27 = 0xFF00FF & (i25 ^ i26 >>> 8);
        int i28 = i25 ^ i27;
        int i29 = i26 ^ i27 << 8;
        int i30 = 0x33333333 & (i28 ^ i29 >>> 2);
        int i31 = i28 ^ i30;
        int i32 = i29 ^ i30 << 2;
        int i33 = 0xFFFF & (i32 ^ i31 >>> 16);
        int i34 = i32 ^ i33;
        int i35 = i31 ^ i33 << 16;
        int i36 = 0xF0F0F0F & (i34 ^ i35 >>> 4);
        int i37 = i34 ^ i36;
        int i38 = i35 ^ i36 << 4;
        paramArrayOfByte2[(paramInt2 + 0)] = ((byte)(0xFF & i38 >>> 24));
        paramArrayOfByte2[(paramInt2 + 1)] = ((byte)(0xFF & i38 >>> 16));
        paramArrayOfByte2[(paramInt2 + 2)] = ((byte)(0xFF & i38 >>> 8));
        paramArrayOfByte2[(paramInt2 + 3)] = ((byte)(i38 & 0xFF));
        paramArrayOfByte2[(paramInt2 + 4)] = ((byte)(0xFF & i37 >>> 24));
        paramArrayOfByte2[(paramInt2 + 5)] = ((byte)(0xFF & i37 >>> 16));
        paramArrayOfByte2[(paramInt2 + 6)] = ((byte)(0xFF & i37 >>> 8));
        paramArrayOfByte2[(paramInt2 + 7)] = ((byte)(i37 & 0xFF));
        return;
      }
      int i16 = (i13 << 28 | i13 >>> 4) ^ paramArrayOfInt[(0 + i15 * 4)];
      int i17 = SP7[(i16 & 0x3F)] | SP5[(0x3F & i16 >>> 8)] | SP3[(0x3F & i16 >>> 16)] | SP1[(0x3F & i16 >>> 24)];
      int i18 = i13 ^ paramArrayOfInt[(1 + i15 * 4)];
      i14 ^= (i17 | SP8[(i18 & 0x3F)] | SP6[(0x3F & i18 >>> 8)] | SP4[(0x3F & i18 >>> 16)] | SP2[(0x3F & i18 >>> 24)]);
      int i19 = (i14 << 28 | i14 >>> 4) ^ paramArrayOfInt[(2 + i15 * 4)];
      int i20 = SP7[(i19 & 0x3F)] | SP5[(0x3F & i19 >>> 8)] | SP3[(0x3F & i19 >>> 16)] | SP1[(0x3F & i19 >>> 24)];
      int i21 = i14 ^ paramArrayOfInt[(3 + i15 * 4)];
      i13 ^= (i20 | SP8[(i21 & 0x3F)] | SP6[(0x3F & i21 >>> 8)] | SP4[(0x3F & i21 >>> 16)] | SP2[(0x3F & i21 >>> 24)]);
    }
  }

  protected int[] generateWorkingKey(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = new int[32];
    boolean[] arrayOfBoolean1 = new boolean[56];
    boolean[] arrayOfBoolean2 = new boolean[56];
    int i = 0;
    int m;
    if (i >= 56)
    {
      m = 0;
      if (m < 16)
        break label98;
    }
    for (int i7 = 0; ; i7 += 2)
    {
      if (i7 == 32)
      {
        return arrayOfInt;
        int j = pc1[i];
        if ((paramArrayOfByte[(j >>> 3)] & bytebit[(j & 0x7)]) != 0);
        for (int k = 1; ; k = 0)
        {
          arrayOfBoolean1[i] = k;
          i++;
          break;
        }
        label98: int n;
        label108: int i1;
        int i2;
        int i4;
        if (paramBoolean)
        {
          n = m << 1;
          i1 = n + 1;
          arrayOfInt[i1] = 0;
          arrayOfInt[n] = 0;
          i2 = 0;
          if (i2 < 28)
            break label173;
          i4 = 28;
          if (i4 < 56)
            break label223;
        }
        for (int i6 = 0; ; i6++)
        {
          if (i6 >= 24)
          {
            m++;
            break;
            n = 15 - m << 1;
            break label108;
            label173: int i3 = i2 + totrot[m];
            if (i3 < 28)
              arrayOfBoolean2[i2] = arrayOfBoolean1[i3];
            while (true)
            {
              i2++;
              break;
              arrayOfBoolean2[i2] = arrayOfBoolean1[(i3 - 28)];
            }
            label223: int i5 = i4 + totrot[m];
            if (i5 < 56)
              arrayOfBoolean2[i4] = arrayOfBoolean1[i5];
            while (true)
            {
              i4++;
              break;
              arrayOfBoolean2[i4] = arrayOfBoolean1[(i5 - 28)];
            }
          }
          if (arrayOfBoolean2[pc2[i6]] != 0)
            arrayOfInt[n] |= bigbyte[i6];
          if (arrayOfBoolean2[pc2[(i6 + 24)]] != 0)
            arrayOfInt[i1] |= bigbyte[i6];
        }
      }
      int i8 = arrayOfInt[i7];
      int i9 = arrayOfInt[(i7 + 1)];
      arrayOfInt[i7] = ((0xFC0000 & i8) << 6 | (i8 & 0xFC0) << 10 | (0xFC0000 & i9) >>> 10 | (i9 & 0xFC0) >>> 6);
      arrayOfInt[(i7 + 1)] = ((0x3F000 & i8) << 12 | (i8 & 0x3F) << 16 | (0x3F000 & i9) >>> 4 | i9 & 0x3F);
    }
  }

  public String getAlgorithmName()
  {
    return "DES";
  }

  public int getBlockSize()
  {
    return 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof KeyParameter))
    {
      if (((KeyParameter)paramCipherParameters).getKey().length > 8)
        throw new IllegalArgumentException("DES key too long - should be 8 bytes");
      this.workingKey = generateWorkingKey(paramBoolean, ((KeyParameter)paramCipherParameters).getKey());
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to DES init - " + paramCipherParameters.getClass().getName());
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.workingKey == null)
      throw new IllegalStateException("DES engine not initialised");
    if (paramInt1 + 8 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 8 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    desFunc(this.workingKey, paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return 8;
  }

  public void reset()
  {
  }
}