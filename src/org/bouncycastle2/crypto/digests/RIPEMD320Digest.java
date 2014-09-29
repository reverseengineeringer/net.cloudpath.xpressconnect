package org.bouncycastle2.crypto.digests;

public class RIPEMD320Digest extends GeneralDigest
{
  private static final int DIGEST_LENGTH = 40;
  private int H0;
  private int H1;
  private int H2;
  private int H3;
  private int H4;
  private int H5;
  private int H6;
  private int H7;
  private int H8;
  private int H9;
  private int[] X = new int[16];
  private int xOff;

  public RIPEMD320Digest()
  {
    reset();
  }

  public RIPEMD320Digest(RIPEMD320Digest paramRIPEMD320Digest)
  {
    super(paramRIPEMD320Digest);
    this.H0 = paramRIPEMD320Digest.H0;
    this.H1 = paramRIPEMD320Digest.H1;
    this.H2 = paramRIPEMD320Digest.H2;
    this.H3 = paramRIPEMD320Digest.H3;
    this.H4 = paramRIPEMD320Digest.H4;
    this.H5 = paramRIPEMD320Digest.H5;
    this.H6 = paramRIPEMD320Digest.H6;
    this.H7 = paramRIPEMD320Digest.H7;
    this.H8 = paramRIPEMD320Digest.H8;
    this.H9 = paramRIPEMD320Digest.H9;
    System.arraycopy(paramRIPEMD320Digest.X, 0, this.X, 0, paramRIPEMD320Digest.X.length);
    this.xOff = paramRIPEMD320Digest.xOff;
  }

  private int RL(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }

  private int f1(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ (paramInt1 ^ paramInt2);
  }

  private int f2(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt3 & (paramInt1 ^ 0xFFFFFFFF);
  }

  private int f3(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ (paramInt1 | paramInt2 ^ 0xFFFFFFFF);
  }

  private int f4(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt3 | paramInt2 & (paramInt3 ^ 0xFFFFFFFF);
  }

  private int f5(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 ^ (paramInt2 | paramInt3 ^ 0xFFFFFFFF);
  }

  private void unpackWord(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >>> 16));
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >>> 24));
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    unpackWord(this.H0, paramArrayOfByte, paramInt);
    unpackWord(this.H1, paramArrayOfByte, paramInt + 4);
    unpackWord(this.H2, paramArrayOfByte, paramInt + 8);
    unpackWord(this.H3, paramArrayOfByte, paramInt + 12);
    unpackWord(this.H4, paramArrayOfByte, paramInt + 16);
    unpackWord(this.H5, paramArrayOfByte, paramInt + 20);
    unpackWord(this.H6, paramArrayOfByte, paramInt + 24);
    unpackWord(this.H7, paramArrayOfByte, paramInt + 28);
    unpackWord(this.H8, paramArrayOfByte, paramInt + 32);
    unpackWord(this.H9, paramArrayOfByte, paramInt + 36);
    reset();
    return 40;
  }

  public String getAlgorithmName()
  {
    return "RIPEMD320";
  }

  public int getDigestSize()
  {
    return 40;
  }

  protected void processBlock()
  {
    i = this.H0;
    int j = this.H1;
    int k = this.H2;
    int m = this.H3;
    int n = this.H4;
    int i1 = this.H5;
    int i2 = this.H6;
    int i3 = this.H7;
    int i4 = this.H8;
    int i5 = this.H9;
    int i6 = n + RL(i + f1(j, k, m) + this.X[0], 11);
    int i7 = RL(k, 10);
    int i8 = m + RL(n + f1(i6, j, i7) + this.X[1], 14);
    int i9 = RL(j, 10);
    int i10 = i7 + RL(m + f1(i8, i6, i9) + this.X[2], 15);
    int i11 = RL(i6, 10);
    int i12 = i9 + RL(i7 + f1(i10, i8, i11) + this.X[3], 12);
    int i13 = RL(i8, 10);
    int i14 = i11 + RL(i9 + f1(i12, i10, i13) + this.X[4], 5);
    int i15 = RL(i10, 10);
    int i16 = i13 + RL(i11 + f1(i14, i12, i15) + this.X[5], 8);
    int i17 = RL(i12, 10);
    int i18 = i15 + RL(i13 + f1(i16, i14, i17) + this.X[6], 7);
    int i19 = RL(i14, 10);
    int i20 = i17 + RL(i15 + f1(i18, i16, i19) + this.X[7], 9);
    int i21 = RL(i16, 10);
    int i22 = i19 + RL(i17 + f1(i20, i18, i21) + this.X[8], 11);
    int i23 = RL(i18, 10);
    int i24 = i21 + RL(i19 + f1(i22, i20, i23) + this.X[9], 13);
    int i25 = RL(i20, 10);
    int i26 = i23 + RL(i21 + f1(i24, i22, i25) + this.X[10], 14);
    int i27 = RL(i22, 10);
    int i28 = i25 + RL(i23 + f1(i26, i24, i27) + this.X[11], 15);
    int i29 = RL(i24, 10);
    int i30 = i27 + RL(i25 + f1(i28, i26, i29) + this.X[12], 6);
    int i31 = RL(i26, 10);
    int i32 = i29 + RL(i27 + f1(i30, i28, i31) + this.X[13], 7);
    int i33 = RL(i28, 10);
    int i34 = i31 + RL(i29 + f1(i32, i30, i33) + this.X[14], 9);
    int i35 = RL(i30, 10);
    int i36 = i33 + RL(i31 + f1(i34, i32, i35) + this.X[15], 8);
    int i37 = RL(i32, 10);
    int i38 = i5 + RL(1352829926 + (i1 + f5(i2, i3, i4) + this.X[5]), 8);
    int i39 = RL(i3, 10);
    int i40 = i4 + RL(1352829926 + (i5 + f5(i38, i2, i39) + this.X[14]), 9);
    int i41 = RL(i2, 10);
    int i42 = i39 + RL(1352829926 + (i4 + f5(i40, i38, i41) + this.X[7]), 9);
    int i43 = RL(i38, 10);
    int i44 = i41 + RL(1352829926 + (i39 + f5(i42, i40, i43) + this.X[0]), 11);
    int i45 = RL(i40, 10);
    int i46 = i43 + RL(1352829926 + (i41 + f5(i44, i42, i45) + this.X[9]), 13);
    int i47 = RL(i42, 10);
    int i48 = i45 + RL(1352829926 + (i43 + f5(i46, i44, i47) + this.X[2]), 15);
    int i49 = RL(i44, 10);
    int i50 = i47 + RL(1352829926 + (i45 + f5(i48, i46, i49) + this.X[11]), 15);
    int i51 = RL(i46, 10);
    int i52 = i49 + RL(1352829926 + (i47 + f5(i50, i48, i51) + this.X[4]), 5);
    int i53 = RL(i48, 10);
    int i54 = i51 + RL(1352829926 + (i49 + f5(i52, i50, i53) + this.X[13]), 7);
    int i55 = RL(i50, 10);
    int i56 = i53 + RL(1352829926 + (i51 + f5(i54, i52, i55) + this.X[6]), 7);
    int i57 = RL(i52, 10);
    int i58 = i55 + RL(1352829926 + (i53 + f5(i56, i54, i57) + this.X[15]), 8);
    int i59 = RL(i54, 10);
    int i60 = i57 + RL(1352829926 + (i55 + f5(i58, i56, i59) + this.X[8]), 11);
    int i61 = RL(i56, 10);
    int i62 = i59 + RL(1352829926 + (i57 + f5(i60, i58, i61) + this.X[1]), 14);
    int i63 = RL(i58, 10);
    int i64 = i61 + RL(1352829926 + (i59 + f5(i62, i60, i63) + this.X[10]), 14);
    int i65 = RL(i60, 10);
    int i66 = i63 + RL(1352829926 + (i61 + f5(i64, i62, i65) + this.X[3]), 12);
    int i67 = RL(i62, 10);
    int i68 = i65 + RL(1352829926 + (i63 + f5(i66, i64, i67) + this.X[12]), 6);
    int i69 = RL(i64, 10);
    int i70 = i35 + RL(1518500249 + (i33 + f2(i68, i34, i37) + this.X[7]), 7);
    int i71 = RL(i34, 10);
    int i72 = i37 + RL(1518500249 + (i35 + f2(i70, i68, i71) + this.X[4]), 6);
    int i73 = RL(i68, 10);
    int i74 = i71 + RL(1518500249 + (i37 + f2(i72, i70, i73) + this.X[13]), 8);
    int i75 = RL(i70, 10);
    int i76 = i73 + RL(1518500249 + (i71 + f2(i74, i72, i75) + this.X[1]), 13);
    int i77 = RL(i72, 10);
    int i78 = i75 + RL(1518500249 + (i73 + f2(i76, i74, i77) + this.X[10]), 11);
    int i79 = RL(i74, 10);
    int i80 = i77 + RL(1518500249 + (i75 + f2(i78, i76, i79) + this.X[6]), 9);
    int i81 = RL(i76, 10);
    int i82 = i79 + RL(1518500249 + (i77 + f2(i80, i78, i81) + this.X[15]), 7);
    int i83 = RL(i78, 10);
    int i84 = i81 + RL(1518500249 + (i79 + f2(i82, i80, i83) + this.X[3]), 15);
    int i85 = RL(i80, 10);
    int i86 = i83 + RL(1518500249 + (i81 + f2(i84, i82, i85) + this.X[12]), 7);
    int i87 = RL(i82, 10);
    int i88 = i85 + RL(1518500249 + (i83 + f2(i86, i84, i87) + this.X[0]), 12);
    int i89 = RL(i84, 10);
    int i90 = i87 + RL(1518500249 + (i85 + f2(i88, i86, i89) + this.X[9]), 15);
    int i91 = RL(i86, 10);
    int i92 = i89 + RL(1518500249 + (i87 + f2(i90, i88, i91) + this.X[5]), 9);
    int i93 = RL(i88, 10);
    int i94 = i91 + RL(1518500249 + (i89 + f2(i92, i90, i93) + this.X[2]), 11);
    int i95 = RL(i90, 10);
    int i96 = i93 + RL(1518500249 + (i91 + f2(i94, i92, i95) + this.X[14]), 7);
    int i97 = RL(i92, 10);
    int i98 = i95 + RL(1518500249 + (i93 + f2(i96, i94, i97) + this.X[11]), 13);
    int i99 = RL(i94, 10);
    int i100 = i97 + RL(1518500249 + (i95 + f2(i98, i96, i99) + this.X[8]), 12);
    int i101 = RL(i96, 10);
    int i102 = i67 + RL(1548603684 + (i65 + f4(i36, i66, i69) + this.X[6]), 9);
    int i103 = RL(i66, 10);
    int i104 = i69 + RL(1548603684 + (i67 + f4(i102, i36, i103) + this.X[11]), 13);
    int i105 = RL(i36, 10);
    int i106 = i103 + RL(1548603684 + (i69 + f4(i104, i102, i105) + this.X[3]), 15);
    int i107 = RL(i102, 10);
    int i108 = i105 + RL(1548603684 + (i103 + f4(i106, i104, i107) + this.X[7]), 7);
    int i109 = RL(i104, 10);
    int i110 = i107 + RL(1548603684 + (i105 + f4(i108, i106, i109) + this.X[0]), 12);
    int i111 = RL(i106, 10);
    int i112 = i109 + RL(1548603684 + (i107 + f4(i110, i108, i111) + this.X[13]), 8);
    int i113 = RL(i108, 10);
    int i114 = i111 + RL(1548603684 + (i109 + f4(i112, i110, i113) + this.X[5]), 9);
    int i115 = RL(i110, 10);
    int i116 = i113 + RL(1548603684 + (i111 + f4(i114, i112, i115) + this.X[10]), 11);
    int i117 = RL(i112, 10);
    int i118 = i115 + RL(1548603684 + (i113 + f4(i116, i114, i117) + this.X[14]), 7);
    int i119 = RL(i114, 10);
    int i120 = i117 + RL(1548603684 + (i115 + f4(i118, i116, i119) + this.X[15]), 7);
    int i121 = RL(i116, 10);
    int i122 = i119 + RL(1548603684 + (i117 + f4(i120, i118, i121) + this.X[8]), 12);
    int i123 = RL(i118, 10);
    int i124 = i121 + RL(1548603684 + (i119 + f4(i122, i120, i123) + this.X[12]), 7);
    int i125 = RL(i120, 10);
    int i126 = i123 + RL(1548603684 + (i121 + f4(i124, i122, i125) + this.X[4]), 6);
    int i127 = RL(i122, 10);
    int i128 = i125 + RL(1548603684 + (i123 + f4(i126, i124, i127) + this.X[9]), 15);
    int i129 = RL(i124, 10);
    int i130 = i127 + RL(1548603684 + (i125 + f4(i128, i126, i129) + this.X[1]), 13);
    int i131 = RL(i126, 10);
    int i132 = i129 + RL(1548603684 + (i127 + f4(i130, i128, i131) + this.X[2]), 11);
    int i133 = RL(i128, 10);
    int i134 = i99 + RL(1859775393 + (i97 + f3(i100, i98, i133) + this.X[3]), 11);
    int i135 = RL(i98, 10);
    int i136 = i133 + RL(1859775393 + (i99 + f3(i134, i100, i135) + this.X[10]), 13);
    int i137 = RL(i100, 10);
    int i138 = i135 + RL(1859775393 + (i133 + f3(i136, i134, i137) + this.X[14]), 6);
    int i139 = RL(i134, 10);
    int i140 = i137 + RL(1859775393 + (i135 + f3(i138, i136, i139) + this.X[4]), 7);
    int i141 = RL(i136, 10);
    int i142 = i139 + RL(1859775393 + (i137 + f3(i140, i138, i141) + this.X[9]), 14);
    int i143 = RL(i138, 10);
    int i144 = i141 + RL(1859775393 + (i139 + f3(i142, i140, i143) + this.X[15]), 9);
    int i145 = RL(i140, 10);
    int i146 = i143 + RL(1859775393 + (i141 + f3(i144, i142, i145) + this.X[8]), 13);
    int i147 = RL(i142, 10);
    int i148 = i145 + RL(1859775393 + (i143 + f3(i146, i144, i147) + this.X[1]), 15);
    int i149 = RL(i144, 10);
    int i150 = i147 + RL(1859775393 + (i145 + f3(i148, i146, i149) + this.X[2]), 14);
    int i151 = RL(i146, 10);
    int i152 = i149 + RL(1859775393 + (i147 + f3(i150, i148, i151) + this.X[7]), 8);
    int i153 = RL(i148, 10);
    int i154 = i151 + RL(1859775393 + (i149 + f3(i152, i150, i153) + this.X[0]), 13);
    int i155 = RL(i150, 10);
    int i156 = i153 + RL(1859775393 + (i151 + f3(i154, i152, i155) + this.X[6]), 6);
    int i157 = RL(i152, 10);
    int i158 = i155 + RL(1859775393 + (i153 + f3(i156, i154, i157) + this.X[13]), 5);
    int i159 = RL(i154, 10);
    int i160 = i157 + RL(1859775393 + (i155 + f3(i158, i156, i159) + this.X[11]), 12);
    int i161 = RL(i156, 10);
    int i162 = i159 + RL(1859775393 + (i157 + f3(i160, i158, i161) + this.X[5]), 7);
    int i163 = RL(i158, 10);
    int i164 = i161 + RL(1859775393 + (i159 + f3(i162, i160, i163) + this.X[12]), 5);
    int i165 = RL(i160, 10);
    int i166 = i131 + RL(1836072691 + (i129 + f3(i132, i130, i101) + this.X[15]), 9);
    int i167 = RL(i130, 10);
    int i168 = i101 + RL(1836072691 + (i131 + f3(i166, i132, i167) + this.X[5]), 7);
    int i169 = RL(i132, 10);
    int i170 = i167 + RL(1836072691 + (i101 + f3(i168, i166, i169) + this.X[1]), 15);
    int i171 = RL(i166, 10);
    int i172 = i169 + RL(1836072691 + (i167 + f3(i170, i168, i171) + this.X[3]), 11);
    int i173 = RL(i168, 10);
    int i174 = i171 + RL(1836072691 + (i169 + f3(i172, i170, i173) + this.X[7]), 8);
    int i175 = RL(i170, 10);
    int i176 = i173 + RL(1836072691 + (i171 + f3(i174, i172, i175) + this.X[14]), 6);
    int i177 = RL(i172, 10);
    int i178 = i175 + RL(1836072691 + (i173 + f3(i176, i174, i177) + this.X[6]), 6);
    int i179 = RL(i174, 10);
    int i180 = i177 + RL(1836072691 + (i175 + f3(i178, i176, i179) + this.X[9]), 14);
    int i181 = RL(i176, 10);
    int i182 = i179 + RL(1836072691 + (i177 + f3(i180, i178, i181) + this.X[11]), 12);
    int i183 = RL(i178, 10);
    int i184 = i181 + RL(1836072691 + (i179 + f3(i182, i180, i183) + this.X[8]), 13);
    int i185 = RL(i180, 10);
    int i186 = i183 + RL(1836072691 + (i181 + f3(i184, i182, i185) + this.X[12]), 5);
    int i187 = RL(i182, 10);
    int i188 = i185 + RL(1836072691 + (i183 + f3(i186, i184, i187) + this.X[2]), 14);
    int i189 = RL(i184, 10);
    int i190 = i187 + RL(1836072691 + (i185 + f3(i188, i186, i189) + this.X[10]), 13);
    int i191 = RL(i186, 10);
    int i192 = i189 + RL(1836072691 + (i187 + f3(i190, i188, i191) + this.X[0]), 13);
    int i193 = RL(i188, 10);
    int i194 = i191 + RL(1836072691 + (i189 + f3(i192, i190, i193) + this.X[4]), 7);
    int i195 = RL(i190, 10);
    int i196 = i193 + RL(1836072691 + (i191 + f3(i194, i192, i195) + this.X[13]), 5);
    int i197 = RL(i192, 10);
    int i198 = i163 + RL(-1894007588 + (i193 + f4(i164, i162, i165) + this.X[1]), 11);
    int i199 = RL(i162, 10);
    int i200 = i165 + RL(-1894007588 + (i163 + f4(i198, i164, i199) + this.X[9]), 12);
    int i201 = RL(i164, 10);
    int i202 = i199 + RL(-1894007588 + (i165 + f4(i200, i198, i201) + this.X[11]), 14);
    int i203 = RL(i198, 10);
    int i204 = i201 + RL(-1894007588 + (i199 + f4(i202, i200, i203) + this.X[10]), 15);
    int i205 = RL(i200, 10);
    int i206 = i203 + RL(-1894007588 + (i201 + f4(i204, i202, i205) + this.X[0]), 14);
    int i207 = RL(i202, 10);
    int i208 = i205 + RL(-1894007588 + (i203 + f4(i206, i204, i207) + this.X[8]), 15);
    int i209 = RL(i204, 10);
    int i210 = i207 + RL(-1894007588 + (i205 + f4(i208, i206, i209) + this.X[12]), 9);
    int i211 = RL(i206, 10);
    int i212 = i209 + RL(-1894007588 + (i207 + f4(i210, i208, i211) + this.X[4]), 8);
    int i213 = RL(i208, 10);
    int i214 = i211 + RL(-1894007588 + (i209 + f4(i212, i210, i213) + this.X[13]), 9);
    int i215 = RL(i210, 10);
    int i216 = i213 + RL(-1894007588 + (i211 + f4(i214, i212, i215) + this.X[3]), 14);
    int i217 = RL(i212, 10);
    int i218 = i215 + RL(-1894007588 + (i213 + f4(i216, i214, i217) + this.X[7]), 5);
    int i219 = RL(i214, 10);
    int i220 = i217 + RL(-1894007588 + (i215 + f4(i218, i216, i219) + this.X[15]), 6);
    int i221 = RL(i216, 10);
    int i222 = i219 + RL(-1894007588 + (i217 + f4(i220, i218, i221) + this.X[14]), 8);
    int i223 = RL(i218, 10);
    int i224 = i221 + RL(-1894007588 + (i219 + f4(i222, i220, i223) + this.X[5]), 6);
    int i225 = RL(i220, 10);
    int i226 = i223 + RL(-1894007588 + (i221 + f4(i224, i222, i225) + this.X[6]), 5);
    int i227 = RL(i222, 10);
    int i228 = i225 + RL(-1894007588 + (i223 + f4(i226, i224, i227) + this.X[2]), 12);
    int i229 = RL(i224, 10);
    int i230 = i195 + RL(2053994217 + (i161 + f2(i196, i194, i197) + this.X[8]), 15);
    int i231 = RL(i194, 10);
    int i232 = i197 + RL(2053994217 + (i195 + f2(i230, i196, i231) + this.X[6]), 5);
    int i233 = RL(i196, 10);
    int i234 = i231 + RL(2053994217 + (i197 + f2(i232, i230, i233) + this.X[4]), 8);
    int i235 = RL(i230, 10);
    int i236 = i233 + RL(2053994217 + (i231 + f2(i234, i232, i235) + this.X[1]), 11);
    int i237 = RL(i232, 10);
    int i238 = i235 + RL(2053994217 + (i233 + f2(i236, i234, i237) + this.X[3]), 14);
    int i239 = RL(i234, 10);
    int i240 = i237 + RL(2053994217 + (i235 + f2(i238, i236, i239) + this.X[11]), 14);
    int i241 = RL(i236, 10);
    int i242 = i239 + RL(2053994217 + (i237 + f2(i240, i238, i241) + this.X[15]), 6);
    int i243 = RL(i238, 10);
    int i244 = i241 + RL(2053994217 + (i239 + f2(i242, i240, i243) + this.X[0]), 14);
    int i245 = RL(i240, 10);
    int i246 = i243 + RL(2053994217 + (i241 + f2(i244, i242, i245) + this.X[5]), 6);
    int i247 = RL(i242, 10);
    int i248 = i245 + RL(2053994217 + (i243 + f2(i246, i244, i247) + this.X[12]), 9);
    int i249 = RL(i244, 10);
    int i250 = i247 + RL(2053994217 + (i245 + f2(i248, i246, i249) + this.X[2]), 12);
    int i251 = RL(i246, 10);
    int i252 = i249 + RL(2053994217 + (i247 + f2(i250, i248, i251) + this.X[13]), 9);
    int i253 = RL(i248, 10);
    int i254 = 2053994217 + i249.RL(this + (i252 + null.f2(i250, i253, -1) + this.X[9]), 12);
    int i255 = RL(i250, 10);
    int i256 = i251 + RL(i254 + (0 + i252.f2(null, i255, 1) + this.X[7]), 5);
    int i257 = i252.RL(null, 10);
    int i258 = -1 + RL(i256 + (2 + i254.f2(0, i257, 3) + this.X[10]), 15);
    int i259 = i254.RL(0, 10);
    int i260 = 1 + RL(i258 + (4 + i256.f2(2, i259, 5) + this.X[14]), 8);
    int i261 = i256.RL(2, 10);
    int i262 = this + -1454113458.RL(i225 + (this + i228.f5(i258, 4, i229) + this.X[4]), 9);
    int i263 = i258.RL(4, 10);
    int i264 = -1454113458 + i227.RL(this + (i262 + 0.0F.f5(i228, i263, 1.0F) + this.X[0]), 15);
    i265 = RL(i228, 10);
    int i266 = i229 + RL(i264 + (2.0F + i262.f5(0.0F, i265, 0.0D) + this.X[5]), 5);
    int i267 = i262.RL(0.0F, 10);
    int tmp5958_5952 = i267;
    i268 = 2.0F + tmp5958_5952.RL(-73 + (tmp5958_5952 + -73 + this.X[9]), 11);
    i269 = i.RL(2.0F, 10);
  }

  protected void processLength(long paramLong)
  {
    if (this.xOff > 14)
      processBlock();
    this.X[14] = ((int)(0xFFFFFFFF & paramLong));
    this.X[15] = ((int)(paramLong >>> 32));
  }

  protected void processWord(byte[] paramArrayOfByte, int paramInt)
  {
    int[] arrayOfInt = this.X;
    int i = this.xOff;
    this.xOff = (i + 1);
    arrayOfInt[i] = (0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24);
    if (this.xOff == 16)
      processBlock();
  }

  public void reset()
  {
    super.reset();
    this.H0 = 1732584193;
    this.H1 = -271733879;
    this.H2 = -1732584194;
    this.H3 = 271733878;
    this.H4 = -1009589776;
    this.H5 = 1985229328;
    this.H6 = -19088744;
    this.H7 = -1985229329;
    this.H8 = 19088743;
    this.H9 = 1009589775;
    this.xOff = 0;
    for (int i = 0; ; i++)
    {
      if (i == this.X.length)
        return;
      this.X[i] = 0;
    }
  }
}