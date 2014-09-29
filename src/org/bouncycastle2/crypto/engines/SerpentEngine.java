package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class SerpentEngine
  implements BlockCipher
{
  private static final int BLOCK_SIZE = 16;
  static final int PHI = -1640531527;
  static final int ROUNDS = 32;
  private int X0;
  private int X1;
  private int X2;
  private int X3;
  private boolean encrypting;
  private int[] wKey;

  private void LT()
  {
    int i = rotateLeft(this.X0, 13);
    int j = rotateLeft(this.X2, 3);
    int k = j ^ (i ^ this.X1);
    int m = j ^ this.X3 ^ i << 3;
    this.X1 = rotateLeft(k, 1);
    this.X3 = rotateLeft(m, 7);
    this.X0 = rotateLeft(i ^ this.X1 ^ this.X3, 5);
    this.X2 = rotateLeft(j ^ this.X3 ^ this.X1 << 7, 22);
  }

  private int bytesToWord(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFF & paramArrayOfByte[paramInt]) << 24 | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 8 | 0xFF & paramArrayOfByte[(paramInt + 3)];
  }

  private void decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    this.X3 = (this.wKey[''] ^ bytesToWord(paramArrayOfByte1, paramInt1));
    this.X2 = (this.wKey[''] ^ bytesToWord(paramArrayOfByte1, paramInt1 + 4));
    this.X1 = (this.wKey[''] ^ bytesToWord(paramArrayOfByte1, paramInt1 + 8));
    this.X0 = (this.wKey[''] ^ bytesToWord(paramArrayOfByte1, paramInt1 + 12));
    ib7(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[124];
    this.X1 ^= this.wKey[125];
    this.X2 ^= this.wKey[126];
    this.X3 ^= this.wKey[127];
    inverseLT();
    ib6(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[120];
    this.X1 ^= this.wKey[121];
    this.X2 ^= this.wKey[122];
    this.X3 ^= this.wKey[123];
    inverseLT();
    ib5(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[116];
    this.X1 ^= this.wKey[117];
    this.X2 ^= this.wKey[118];
    this.X3 ^= this.wKey[119];
    inverseLT();
    ib4(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[112];
    this.X1 ^= this.wKey[113];
    this.X2 ^= this.wKey[114];
    this.X3 ^= this.wKey[115];
    inverseLT();
    ib3(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[108];
    this.X1 ^= this.wKey[109];
    this.X2 ^= this.wKey[110];
    this.X3 ^= this.wKey[111];
    inverseLT();
    ib2(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[104];
    this.X1 ^= this.wKey[105];
    this.X2 ^= this.wKey[106];
    this.X3 ^= this.wKey[107];
    inverseLT();
    ib1(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[100];
    this.X1 ^= this.wKey[101];
    this.X2 ^= this.wKey[102];
    this.X3 ^= this.wKey[103];
    inverseLT();
    ib0(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[96];
    this.X1 ^= this.wKey[97];
    this.X2 ^= this.wKey[98];
    this.X3 ^= this.wKey[99];
    inverseLT();
    ib7(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[92];
    this.X1 ^= this.wKey[93];
    this.X2 ^= this.wKey[94];
    this.X3 ^= this.wKey[95];
    inverseLT();
    ib6(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[88];
    this.X1 ^= this.wKey[89];
    this.X2 ^= this.wKey[90];
    this.X3 ^= this.wKey[91];
    inverseLT();
    ib5(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[84];
    this.X1 ^= this.wKey[85];
    this.X2 ^= this.wKey[86];
    this.X3 ^= this.wKey[87];
    inverseLT();
    ib4(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[80];
    this.X1 ^= this.wKey[81];
    this.X2 ^= this.wKey[82];
    this.X3 ^= this.wKey[83];
    inverseLT();
    ib3(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[76];
    this.X1 ^= this.wKey[77];
    this.X2 ^= this.wKey[78];
    this.X3 ^= this.wKey[79];
    inverseLT();
    ib2(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[72];
    this.X1 ^= this.wKey[73];
    this.X2 ^= this.wKey[74];
    this.X3 ^= this.wKey[75];
    inverseLT();
    ib1(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[68];
    this.X1 ^= this.wKey[69];
    this.X2 ^= this.wKey[70];
    this.X3 ^= this.wKey[71];
    inverseLT();
    ib0(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[64];
    this.X1 ^= this.wKey[65];
    this.X2 ^= this.wKey[66];
    this.X3 ^= this.wKey[67];
    inverseLT();
    ib7(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[60];
    this.X1 ^= this.wKey[61];
    this.X2 ^= this.wKey[62];
    this.X3 ^= this.wKey[63];
    inverseLT();
    ib6(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[56];
    this.X1 ^= this.wKey[57];
    this.X2 ^= this.wKey[58];
    this.X3 ^= this.wKey[59];
    inverseLT();
    ib5(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[52];
    this.X1 ^= this.wKey[53];
    this.X2 ^= this.wKey[54];
    this.X3 ^= this.wKey[55];
    inverseLT();
    ib4(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[48];
    this.X1 ^= this.wKey[49];
    this.X2 ^= this.wKey[50];
    this.X3 ^= this.wKey[51];
    inverseLT();
    ib3(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[44];
    this.X1 ^= this.wKey[45];
    this.X2 ^= this.wKey[46];
    this.X3 ^= this.wKey[47];
    inverseLT();
    ib2(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[40];
    this.X1 ^= this.wKey[41];
    this.X2 ^= this.wKey[42];
    this.X3 ^= this.wKey[43];
    inverseLT();
    ib1(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[36];
    this.X1 ^= this.wKey[37];
    this.X2 ^= this.wKey[38];
    this.X3 ^= this.wKey[39];
    inverseLT();
    ib0(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[32];
    this.X1 ^= this.wKey[33];
    this.X2 ^= this.wKey[34];
    this.X3 ^= this.wKey[35];
    inverseLT();
    ib7(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[28];
    this.X1 ^= this.wKey[29];
    this.X2 ^= this.wKey[30];
    this.X3 ^= this.wKey[31];
    inverseLT();
    ib6(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[24];
    this.X1 ^= this.wKey[25];
    this.X2 ^= this.wKey[26];
    this.X3 ^= this.wKey[27];
    inverseLT();
    ib5(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[20];
    this.X1 ^= this.wKey[21];
    this.X2 ^= this.wKey[22];
    this.X3 ^= this.wKey[23];
    inverseLT();
    ib4(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[16];
    this.X1 ^= this.wKey[17];
    this.X2 ^= this.wKey[18];
    this.X3 ^= this.wKey[19];
    inverseLT();
    ib3(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[12];
    this.X1 ^= this.wKey[13];
    this.X2 ^= this.wKey[14];
    this.X3 ^= this.wKey[15];
    inverseLT();
    ib2(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[8];
    this.X1 ^= this.wKey[9];
    this.X2 ^= this.wKey[10];
    this.X3 ^= this.wKey[11];
    inverseLT();
    ib1(this.X0, this.X1, this.X2, this.X3);
    this.X0 ^= this.wKey[4];
    this.X1 ^= this.wKey[5];
    this.X2 ^= this.wKey[6];
    this.X3 ^= this.wKey[7];
    inverseLT();
    ib0(this.X0, this.X1, this.X2, this.X3);
    wordToBytes(this.X3 ^ this.wKey[3], paramArrayOfByte2, paramInt2);
    wordToBytes(this.X2 ^ this.wKey[2], paramArrayOfByte2, paramInt2 + 4);
    wordToBytes(this.X1 ^ this.wKey[1], paramArrayOfByte2, paramInt2 + 8);
    wordToBytes(this.X0 ^ this.wKey[0], paramArrayOfByte2, paramInt2 + 12);
  }

  private void encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    this.X3 = bytesToWord(paramArrayOfByte1, paramInt1);
    this.X2 = bytesToWord(paramArrayOfByte1, paramInt1 + 4);
    this.X1 = bytesToWord(paramArrayOfByte1, paramInt1 + 8);
    this.X0 = bytesToWord(paramArrayOfByte1, paramInt1 + 12);
    sb0(this.wKey[0] ^ this.X0, this.wKey[1] ^ this.X1, this.wKey[2] ^ this.X2, this.wKey[3] ^ this.X3);
    LT();
    sb1(this.wKey[4] ^ this.X0, this.wKey[5] ^ this.X1, this.wKey[6] ^ this.X2, this.wKey[7] ^ this.X3);
    LT();
    sb2(this.wKey[8] ^ this.X0, this.wKey[9] ^ this.X1, this.wKey[10] ^ this.X2, this.wKey[11] ^ this.X3);
    LT();
    sb3(this.wKey[12] ^ this.X0, this.wKey[13] ^ this.X1, this.wKey[14] ^ this.X2, this.wKey[15] ^ this.X3);
    LT();
    sb4(this.wKey[16] ^ this.X0, this.wKey[17] ^ this.X1, this.wKey[18] ^ this.X2, this.wKey[19] ^ this.X3);
    LT();
    sb5(this.wKey[20] ^ this.X0, this.wKey[21] ^ this.X1, this.wKey[22] ^ this.X2, this.wKey[23] ^ this.X3);
    LT();
    sb6(this.wKey[24] ^ this.X0, this.wKey[25] ^ this.X1, this.wKey[26] ^ this.X2, this.wKey[27] ^ this.X3);
    LT();
    sb7(this.wKey[28] ^ this.X0, this.wKey[29] ^ this.X1, this.wKey[30] ^ this.X2, this.wKey[31] ^ this.X3);
    LT();
    sb0(this.wKey[32] ^ this.X0, this.wKey[33] ^ this.X1, this.wKey[34] ^ this.X2, this.wKey[35] ^ this.X3);
    LT();
    sb1(this.wKey[36] ^ this.X0, this.wKey[37] ^ this.X1, this.wKey[38] ^ this.X2, this.wKey[39] ^ this.X3);
    LT();
    sb2(this.wKey[40] ^ this.X0, this.wKey[41] ^ this.X1, this.wKey[42] ^ this.X2, this.wKey[43] ^ this.X3);
    LT();
    sb3(this.wKey[44] ^ this.X0, this.wKey[45] ^ this.X1, this.wKey[46] ^ this.X2, this.wKey[47] ^ this.X3);
    LT();
    sb4(this.wKey[48] ^ this.X0, this.wKey[49] ^ this.X1, this.wKey[50] ^ this.X2, this.wKey[51] ^ this.X3);
    LT();
    sb5(this.wKey[52] ^ this.X0, this.wKey[53] ^ this.X1, this.wKey[54] ^ this.X2, this.wKey[55] ^ this.X3);
    LT();
    sb6(this.wKey[56] ^ this.X0, this.wKey[57] ^ this.X1, this.wKey[58] ^ this.X2, this.wKey[59] ^ this.X3);
    LT();
    sb7(this.wKey[60] ^ this.X0, this.wKey[61] ^ this.X1, this.wKey[62] ^ this.X2, this.wKey[63] ^ this.X3);
    LT();
    sb0(this.wKey[64] ^ this.X0, this.wKey[65] ^ this.X1, this.wKey[66] ^ this.X2, this.wKey[67] ^ this.X3);
    LT();
    sb1(this.wKey[68] ^ this.X0, this.wKey[69] ^ this.X1, this.wKey[70] ^ this.X2, this.wKey[71] ^ this.X3);
    LT();
    sb2(this.wKey[72] ^ this.X0, this.wKey[73] ^ this.X1, this.wKey[74] ^ this.X2, this.wKey[75] ^ this.X3);
    LT();
    sb3(this.wKey[76] ^ this.X0, this.wKey[77] ^ this.X1, this.wKey[78] ^ this.X2, this.wKey[79] ^ this.X3);
    LT();
    sb4(this.wKey[80] ^ this.X0, this.wKey[81] ^ this.X1, this.wKey[82] ^ this.X2, this.wKey[83] ^ this.X3);
    LT();
    sb5(this.wKey[84] ^ this.X0, this.wKey[85] ^ this.X1, this.wKey[86] ^ this.X2, this.wKey[87] ^ this.X3);
    LT();
    sb6(this.wKey[88] ^ this.X0, this.wKey[89] ^ this.X1, this.wKey[90] ^ this.X2, this.wKey[91] ^ this.X3);
    LT();
    sb7(this.wKey[92] ^ this.X0, this.wKey[93] ^ this.X1, this.wKey[94] ^ this.X2, this.wKey[95] ^ this.X3);
    LT();
    sb0(this.wKey[96] ^ this.X0, this.wKey[97] ^ this.X1, this.wKey[98] ^ this.X2, this.wKey[99] ^ this.X3);
    LT();
    sb1(this.wKey[100] ^ this.X0, this.wKey[101] ^ this.X1, this.wKey[102] ^ this.X2, this.wKey[103] ^ this.X3);
    LT();
    sb2(this.wKey[104] ^ this.X0, this.wKey[105] ^ this.X1, this.wKey[106] ^ this.X2, this.wKey[107] ^ this.X3);
    LT();
    sb3(this.wKey[108] ^ this.X0, this.wKey[109] ^ this.X1, this.wKey[110] ^ this.X2, this.wKey[111] ^ this.X3);
    LT();
    sb4(this.wKey[112] ^ this.X0, this.wKey[113] ^ this.X1, this.wKey[114] ^ this.X2, this.wKey[115] ^ this.X3);
    LT();
    sb5(this.wKey[116] ^ this.X0, this.wKey[117] ^ this.X1, this.wKey[118] ^ this.X2, this.wKey[119] ^ this.X3);
    LT();
    sb6(this.wKey[120] ^ this.X0, this.wKey[121] ^ this.X1, this.wKey[122] ^ this.X2, this.wKey[123] ^ this.X3);
    LT();
    sb7(this.wKey[124] ^ this.X0, this.wKey[125] ^ this.X1, this.wKey[126] ^ this.X2, this.wKey[127] ^ this.X3);
    wordToBytes(this.wKey[''] ^ this.X3, paramArrayOfByte2, paramInt2);
    wordToBytes(this.wKey[''] ^ this.X2, paramArrayOfByte2, paramInt2 + 4);
    wordToBytes(this.wKey[''] ^ this.X1, paramArrayOfByte2, paramInt2 + 8);
    wordToBytes(this.wKey[''] ^ this.X0, paramArrayOfByte2, paramInt2 + 12);
  }

  private void ib0(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ 0xFFFFFFFF;
    int j = paramInt1 ^ paramInt2;
    int k = paramInt4 ^ (i | j);
    int m = paramInt3 ^ k;
    this.X2 = (j ^ m);
    int n = i ^ paramInt4 & j;
    this.X1 = (k ^ n & this.X2);
    this.X3 = (paramInt1 & k ^ (m | this.X1));
    this.X0 = (this.X3 ^ (m ^ n));
  }

  private void ib1(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt2 ^ paramInt4;
    int j = paramInt1 ^ paramInt2 & i;
    int k = i ^ j;
    this.X3 = (paramInt3 ^ k);
    int m = paramInt2 ^ i & j;
    this.X1 = (j ^ (m | this.X3));
    int n = 0xFFFFFFFF ^ this.X1;
    int i1 = m ^ this.X3;
    this.X0 = (n ^ i1);
    this.X2 = (k ^ (n | i1));
  }

  private void ib2(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt2 ^ paramInt4;
    int j = i ^ 0xFFFFFFFF;
    int k = paramInt1 ^ paramInt3;
    int m = paramInt3 ^ i;
    this.X0 = (k ^ paramInt2 & m);
    this.X3 = (i ^ (k | paramInt4 ^ (paramInt1 | j)));
    int n = m ^ 0xFFFFFFFF;
    int i1 = this.X0 | this.X3;
    this.X1 = (n ^ i1);
    this.X2 = (paramInt4 & n ^ (k ^ i1));
  }

  private void ib3(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 | paramInt2;
    int j = paramInt2 ^ paramInt3;
    int k = paramInt1 ^ paramInt2 & j;
    int m = paramInt3 ^ k;
    int n = paramInt4 | k;
    this.X0 = (j ^ n);
    int i1 = paramInt4 ^ (j | n);
    this.X2 = (m ^ i1);
    int i2 = i ^ i1;
    this.X3 = (k ^ i2 & this.X0);
    this.X1 = (this.X3 ^ (i2 ^ this.X0));
  }

  private void ib4(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt2 ^ paramInt1 & (paramInt3 | paramInt4);
    int j = paramInt3 ^ paramInt1 & i;
    this.X1 = (paramInt4 ^ j);
    int k = paramInt1 ^ 0xFFFFFFFF;
    this.X3 = (i ^ j & this.X1);
    int m = paramInt4 ^ (k | this.X1);
    this.X0 = (m ^ this.X3);
    this.X2 = (i & m ^ (k ^ this.X1));
  }

  private void ib5(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt3 ^ 0xFFFFFFFF;
    int j = paramInt4 ^ paramInt2 & i;
    int k = paramInt1 & j;
    this.X3 = (k ^ (paramInt2 ^ i));
    int m = paramInt2 | this.X3;
    this.X1 = (j ^ paramInt1 & m);
    int n = paramInt1 | paramInt4;
    this.X0 = (n ^ (i ^ m));
    this.X2 = (paramInt2 & n ^ (k | paramInt1 ^ paramInt3));
  }

  private void ib6(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ 0xFFFFFFFF;
    int j = paramInt1 ^ paramInt2;
    int k = paramInt3 ^ j;
    int m = paramInt4 ^ (paramInt3 | i);
    this.X1 = (k ^ m);
    int n = j ^ k & m;
    this.X3 = (m ^ (paramInt2 | n));
    int i1 = paramInt2 | this.X3;
    this.X0 = (n ^ i1);
    this.X2 = (paramInt4 & i ^ (k ^ i1));
  }

  private void ib7(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt3 | paramInt1 & paramInt2;
    int j = paramInt4 & (paramInt1 | paramInt2);
    this.X3 = (i ^ j);
    int k = paramInt4 ^ 0xFFFFFFFF;
    int m = paramInt2 ^ j;
    this.X1 = (paramInt1 ^ (m | k ^ this.X3));
    this.X0 = (paramInt3 ^ m ^ (paramInt4 | this.X1));
    this.X2 = (i ^ this.X1 ^ (this.X0 ^ paramInt1 & this.X3));
  }

  private void inverseLT()
  {
    int i = rotateRight(this.X2, 22) ^ this.X3 ^ this.X1 << 7;
    int j = rotateRight(this.X0, 5) ^ this.X1 ^ this.X3;
    int k = rotateRight(this.X3, 7);
    int m = rotateRight(this.X1, 1);
    this.X3 = (k ^ i ^ j << 3);
    this.X1 = (i ^ (m ^ j));
    this.X2 = rotateRight(i, 3);
    this.X0 = rotateRight(j, 13);
  }

  private int[] makeWorkingKey(byte[] paramArrayOfByte)
    throws IllegalArgumentException
  {
    int[] arrayOfInt1 = new int[16];
    int i = -4 + paramArrayOfByte.length;
    int j = 0;
    int[] arrayOfInt2;
    int n;
    if (i <= 0)
    {
      if (i != 0)
        break label2096;
      int m = j + 1;
      arrayOfInt1[j] = bytesToWord(paramArrayOfByte, 0);
      if (m < 8)
        arrayOfInt1[m] = 1;
      arrayOfInt2 = new int[''];
      n = 8;
      label61: if (n < 16)
        break label2106;
      System.arraycopy(arrayOfInt1, 8, arrayOfInt2, 0, 8);
    }
    for (int i1 = 8; ; i1++)
    {
      if (i1 >= 132)
      {
        sb3(arrayOfInt2[0], arrayOfInt2[1], arrayOfInt2[2], arrayOfInt2[3]);
        arrayOfInt2[0] = this.X0;
        arrayOfInt2[1] = this.X1;
        arrayOfInt2[2] = this.X2;
        arrayOfInt2[3] = this.X3;
        sb2(arrayOfInt2[4], arrayOfInt2[5], arrayOfInt2[6], arrayOfInt2[7]);
        arrayOfInt2[4] = this.X0;
        arrayOfInt2[5] = this.X1;
        arrayOfInt2[6] = this.X2;
        arrayOfInt2[7] = this.X3;
        sb1(arrayOfInt2[8], arrayOfInt2[9], arrayOfInt2[10], arrayOfInt2[11]);
        arrayOfInt2[8] = this.X0;
        arrayOfInt2[9] = this.X1;
        arrayOfInt2[10] = this.X2;
        arrayOfInt2[11] = this.X3;
        sb0(arrayOfInt2[12], arrayOfInt2[13], arrayOfInt2[14], arrayOfInt2[15]);
        arrayOfInt2[12] = this.X0;
        arrayOfInt2[13] = this.X1;
        arrayOfInt2[14] = this.X2;
        arrayOfInt2[15] = this.X3;
        sb7(arrayOfInt2[16], arrayOfInt2[17], arrayOfInt2[18], arrayOfInt2[19]);
        arrayOfInt2[16] = this.X0;
        arrayOfInt2[17] = this.X1;
        arrayOfInt2[18] = this.X2;
        arrayOfInt2[19] = this.X3;
        sb6(arrayOfInt2[20], arrayOfInt2[21], arrayOfInt2[22], arrayOfInt2[23]);
        arrayOfInt2[20] = this.X0;
        arrayOfInt2[21] = this.X1;
        arrayOfInt2[22] = this.X2;
        arrayOfInt2[23] = this.X3;
        sb5(arrayOfInt2[24], arrayOfInt2[25], arrayOfInt2[26], arrayOfInt2[27]);
        arrayOfInt2[24] = this.X0;
        arrayOfInt2[25] = this.X1;
        arrayOfInt2[26] = this.X2;
        arrayOfInt2[27] = this.X3;
        sb4(arrayOfInt2[28], arrayOfInt2[29], arrayOfInt2[30], arrayOfInt2[31]);
        arrayOfInt2[28] = this.X0;
        arrayOfInt2[29] = this.X1;
        arrayOfInt2[30] = this.X2;
        arrayOfInt2[31] = this.X3;
        sb3(arrayOfInt2[32], arrayOfInt2[33], arrayOfInt2[34], arrayOfInt2[35]);
        arrayOfInt2[32] = this.X0;
        arrayOfInt2[33] = this.X1;
        arrayOfInt2[34] = this.X2;
        arrayOfInt2[35] = this.X3;
        sb2(arrayOfInt2[36], arrayOfInt2[37], arrayOfInt2[38], arrayOfInt2[39]);
        arrayOfInt2[36] = this.X0;
        arrayOfInt2[37] = this.X1;
        arrayOfInt2[38] = this.X2;
        arrayOfInt2[39] = this.X3;
        sb1(arrayOfInt2[40], arrayOfInt2[41], arrayOfInt2[42], arrayOfInt2[43]);
        arrayOfInt2[40] = this.X0;
        arrayOfInt2[41] = this.X1;
        arrayOfInt2[42] = this.X2;
        arrayOfInt2[43] = this.X3;
        sb0(arrayOfInt2[44], arrayOfInt2[45], arrayOfInt2[46], arrayOfInt2[47]);
        arrayOfInt2[44] = this.X0;
        arrayOfInt2[45] = this.X1;
        arrayOfInt2[46] = this.X2;
        arrayOfInt2[47] = this.X3;
        sb7(arrayOfInt2[48], arrayOfInt2[49], arrayOfInt2[50], arrayOfInt2[51]);
        arrayOfInt2[48] = this.X0;
        arrayOfInt2[49] = this.X1;
        arrayOfInt2[50] = this.X2;
        arrayOfInt2[51] = this.X3;
        sb6(arrayOfInt2[52], arrayOfInt2[53], arrayOfInt2[54], arrayOfInt2[55]);
        arrayOfInt2[52] = this.X0;
        arrayOfInt2[53] = this.X1;
        arrayOfInt2[54] = this.X2;
        arrayOfInt2[55] = this.X3;
        sb5(arrayOfInt2[56], arrayOfInt2[57], arrayOfInt2[58], arrayOfInt2[59]);
        arrayOfInt2[56] = this.X0;
        arrayOfInt2[57] = this.X1;
        arrayOfInt2[58] = this.X2;
        arrayOfInt2[59] = this.X3;
        sb4(arrayOfInt2[60], arrayOfInt2[61], arrayOfInt2[62], arrayOfInt2[63]);
        arrayOfInt2[60] = this.X0;
        arrayOfInt2[61] = this.X1;
        arrayOfInt2[62] = this.X2;
        arrayOfInt2[63] = this.X3;
        sb3(arrayOfInt2[64], arrayOfInt2[65], arrayOfInt2[66], arrayOfInt2[67]);
        arrayOfInt2[64] = this.X0;
        arrayOfInt2[65] = this.X1;
        arrayOfInt2[66] = this.X2;
        arrayOfInt2[67] = this.X3;
        sb2(arrayOfInt2[68], arrayOfInt2[69], arrayOfInt2[70], arrayOfInt2[71]);
        arrayOfInt2[68] = this.X0;
        arrayOfInt2[69] = this.X1;
        arrayOfInt2[70] = this.X2;
        arrayOfInt2[71] = this.X3;
        sb1(arrayOfInt2[72], arrayOfInt2[73], arrayOfInt2[74], arrayOfInt2[75]);
        arrayOfInt2[72] = this.X0;
        arrayOfInt2[73] = this.X1;
        arrayOfInt2[74] = this.X2;
        arrayOfInt2[75] = this.X3;
        sb0(arrayOfInt2[76], arrayOfInt2[77], arrayOfInt2[78], arrayOfInt2[79]);
        arrayOfInt2[76] = this.X0;
        arrayOfInt2[77] = this.X1;
        arrayOfInt2[78] = this.X2;
        arrayOfInt2[79] = this.X3;
        sb7(arrayOfInt2[80], arrayOfInt2[81], arrayOfInt2[82], arrayOfInt2[83]);
        arrayOfInt2[80] = this.X0;
        arrayOfInt2[81] = this.X1;
        arrayOfInt2[82] = this.X2;
        arrayOfInt2[83] = this.X3;
        sb6(arrayOfInt2[84], arrayOfInt2[85], arrayOfInt2[86], arrayOfInt2[87]);
        arrayOfInt2[84] = this.X0;
        arrayOfInt2[85] = this.X1;
        arrayOfInt2[86] = this.X2;
        arrayOfInt2[87] = this.X3;
        sb5(arrayOfInt2[88], arrayOfInt2[89], arrayOfInt2[90], arrayOfInt2[91]);
        arrayOfInt2[88] = this.X0;
        arrayOfInt2[89] = this.X1;
        arrayOfInt2[90] = this.X2;
        arrayOfInt2[91] = this.X3;
        sb4(arrayOfInt2[92], arrayOfInt2[93], arrayOfInt2[94], arrayOfInt2[95]);
        arrayOfInt2[92] = this.X0;
        arrayOfInt2[93] = this.X1;
        arrayOfInt2[94] = this.X2;
        arrayOfInt2[95] = this.X3;
        sb3(arrayOfInt2[96], arrayOfInt2[97], arrayOfInt2[98], arrayOfInt2[99]);
        arrayOfInt2[96] = this.X0;
        arrayOfInt2[97] = this.X1;
        arrayOfInt2[98] = this.X2;
        arrayOfInt2[99] = this.X3;
        sb2(arrayOfInt2[100], arrayOfInt2[101], arrayOfInt2[102], arrayOfInt2[103]);
        arrayOfInt2[100] = this.X0;
        arrayOfInt2[101] = this.X1;
        arrayOfInt2[102] = this.X2;
        arrayOfInt2[103] = this.X3;
        sb1(arrayOfInt2[104], arrayOfInt2[105], arrayOfInt2[106], arrayOfInt2[107]);
        arrayOfInt2[104] = this.X0;
        arrayOfInt2[105] = this.X1;
        arrayOfInt2[106] = this.X2;
        arrayOfInt2[107] = this.X3;
        sb0(arrayOfInt2[108], arrayOfInt2[109], arrayOfInt2[110], arrayOfInt2[111]);
        arrayOfInt2[108] = this.X0;
        arrayOfInt2[109] = this.X1;
        arrayOfInt2[110] = this.X2;
        arrayOfInt2[111] = this.X3;
        sb7(arrayOfInt2[112], arrayOfInt2[113], arrayOfInt2[114], arrayOfInt2[115]);
        arrayOfInt2[112] = this.X0;
        arrayOfInt2[113] = this.X1;
        arrayOfInt2[114] = this.X2;
        arrayOfInt2[115] = this.X3;
        sb6(arrayOfInt2[116], arrayOfInt2[117], arrayOfInt2[118], arrayOfInt2[119]);
        arrayOfInt2[116] = this.X0;
        arrayOfInt2[117] = this.X1;
        arrayOfInt2[118] = this.X2;
        arrayOfInt2[119] = this.X3;
        sb5(arrayOfInt2[120], arrayOfInt2[121], arrayOfInt2[122], arrayOfInt2[123]);
        arrayOfInt2[120] = this.X0;
        arrayOfInt2[121] = this.X1;
        arrayOfInt2[122] = this.X2;
        arrayOfInt2[123] = this.X3;
        sb4(arrayOfInt2[124], arrayOfInt2[125], arrayOfInt2[126], arrayOfInt2[127]);
        arrayOfInt2[124] = this.X0;
        arrayOfInt2[125] = this.X1;
        arrayOfInt2[126] = this.X2;
        arrayOfInt2[127] = this.X3;
        sb3(arrayOfInt2[''], arrayOfInt2[''], arrayOfInt2[''], arrayOfInt2['']);
        arrayOfInt2[''] = this.X0;
        arrayOfInt2[''] = this.X1;
        arrayOfInt2[''] = this.X2;
        arrayOfInt2[''] = this.X3;
        return arrayOfInt2;
        int k = j + 1;
        arrayOfInt1[j] = bytesToWord(paramArrayOfByte, i);
        i -= 4;
        j = k;
        break;
        label2096: throw new IllegalArgumentException("key must be a multiple of 4 bytes");
        label2106: arrayOfInt1[n] = rotateLeft(0x9E3779B9 ^ (arrayOfInt1[(n - 8)] ^ arrayOfInt1[(n - 5)] ^ arrayOfInt1[(n - 3)] ^ arrayOfInt1[(n - 1)]) ^ n - 8, 11);
        n++;
        break label61;
      }
      arrayOfInt2[i1] = rotateLeft(i1 ^ (0x9E3779B9 ^ (arrayOfInt2[(i1 - 8)] ^ arrayOfInt2[(i1 - 5)] ^ arrayOfInt2[(i1 - 3)] ^ arrayOfInt2[(i1 - 1)])), 11);
    }
  }

  private int rotateLeft(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> -paramInt2;
  }

  private int rotateRight(int paramInt1, int paramInt2)
  {
    return paramInt1 >>> paramInt2 | paramInt1 << -paramInt2;
  }

  private void sb0(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ paramInt4;
    int j = paramInt3 ^ i;
    int k = paramInt2 ^ j;
    this.X3 = (k ^ paramInt1 & paramInt4);
    int m = paramInt1 ^ paramInt2 & i;
    this.X2 = (k ^ (paramInt3 | m));
    int n = this.X3 & (j ^ m);
    this.X1 = (n ^ (j ^ 0xFFFFFFFF));
    this.X0 = (n ^ (m ^ 0xFFFFFFFF));
  }

  private void sb1(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt2 ^ (paramInt1 ^ 0xFFFFFFFF);
    int j = paramInt3 ^ (paramInt1 | i);
    this.X2 = (paramInt4 ^ j);
    int k = paramInt2 ^ (paramInt4 | i);
    int m = i ^ this.X2;
    this.X3 = (m ^ j & k);
    int n = j ^ k;
    this.X1 = (n ^ this.X3);
    this.X0 = (j ^ m & n);
  }

  private void sb2(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ 0xFFFFFFFF;
    int j = paramInt2 ^ paramInt4;
    this.X0 = (j ^ paramInt3 & i);
    int k = paramInt3 ^ i;
    int m = paramInt2 & (paramInt3 ^ this.X0);
    this.X3 = (k ^ m);
    this.X2 = (paramInt1 ^ (paramInt4 | m) & (k | this.X0));
    this.X1 = (j ^ this.X3 ^ (this.X2 ^ (paramInt4 | i)));
  }

  private void sb3(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ paramInt2;
    int j = paramInt1 & paramInt3;
    int k = paramInt1 | paramInt4;
    int m = paramInt3 ^ paramInt4;
    int n = j | i & k;
    this.X2 = (m ^ n);
    int i1 = n ^ (paramInt2 ^ k);
    this.X0 = (i ^ m & i1);
    int i2 = this.X2 & this.X0;
    this.X1 = (i1 ^ i2);
    this.X3 = ((paramInt2 | paramInt4) ^ (m ^ i2));
  }

  private void sb4(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ paramInt4;
    int j = paramInt3 ^ paramInt4 & i;
    int k = paramInt2 | j;
    this.X3 = (i ^ k);
    int m = paramInt2 ^ 0xFFFFFFFF;
    this.X0 = (j ^ (i | m));
    int n = paramInt1 & this.X0;
    int i1 = i ^ m;
    this.X2 = (n ^ k & i1);
    this.X1 = (paramInt1 ^ j ^ i1 & this.X2);
  }

  private void sb5(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ 0xFFFFFFFF;
    int j = paramInt1 ^ paramInt2;
    int k = paramInt1 ^ paramInt4;
    this.X0 = (paramInt3 ^ i ^ (j | k));
    int m = paramInt4 & this.X0;
    this.X1 = (m ^ (j ^ this.X0));
    int n = i | this.X0;
    int i1 = j | m;
    int i2 = k ^ n;
    this.X2 = (i1 ^ i2);
    this.X3 = (paramInt2 ^ m ^ i2 & this.X1);
  }

  private void sb6(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1 ^ 0xFFFFFFFF;
    int j = paramInt1 ^ paramInt4;
    int k = paramInt2 ^ j;
    int m = paramInt3 ^ (i | j);
    this.X1 = (paramInt2 ^ m);
    int n = paramInt4 ^ (j | this.X1);
    this.X2 = (k ^ m & n);
    int i1 = m ^ n;
    this.X0 = (i1 ^ this.X2);
    this.X3 = (m ^ 0xFFFFFFFF ^ k & i1);
  }

  private void sb7(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt2 ^ paramInt3;
    int j = paramInt4 ^ paramInt3 & i;
    int k = paramInt1 ^ j;
    this.X1 = (paramInt2 ^ k & (paramInt4 | i));
    int m = j | this.X1;
    this.X3 = (i ^ paramInt1 & k);
    int n = k ^ m;
    this.X2 = (j ^ n & this.X3);
    this.X0 = (n ^ 0xFFFFFFFF ^ this.X3 & this.X2);
  }

  private void wordToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)paramInt1);
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >>> 16));
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >>> 24));
  }

  public String getAlgorithmName()
  {
    return "Serpent";
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
      this.wKey = makeWorkingKey(((KeyParameter)paramCipherParameters).getKey());
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to Serpent init - " + paramCipherParameters.getClass().getName());
  }

  public final int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.wKey == null)
      throw new IllegalStateException("Serpent not initialised");
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
  }
}