package jcifs.smb;

import jcifs.util.LogStream;

abstract class SmbComNtTransactionResponse extends SmbComTransactionResponse
{
  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = 0;
    int j = i + 1;
    paramArrayOfByte[i] = 0;
    int k = j + 1;
    paramArrayOfByte[j] = 0;
    this.totalParameterCount = readInt4(paramArrayOfByte, k);
    if (this.bufDataStart == 0)
      this.bufDataStart = this.totalParameterCount;
    int m = k + 4;
    this.totalDataCount = readInt4(paramArrayOfByte, m);
    int n = m + 4;
    this.parameterCount = readInt4(paramArrayOfByte, n);
    int i1 = n + 4;
    this.parameterOffset = readInt4(paramArrayOfByte, i1);
    int i2 = i1 + 4;
    this.parameterDisplacement = readInt4(paramArrayOfByte, i2);
    int i3 = i2 + 4;
    this.dataCount = readInt4(paramArrayOfByte, i3);
    int i4 = i3 + 4;
    this.dataOffset = readInt4(paramArrayOfByte, i4);
    int i5 = i4 + 4;
    this.dataDisplacement = readInt4(paramArrayOfByte, i5);
    int i6 = i5 + 4;
    this.setupCount = (0xFF & paramArrayOfByte[i6]);
    int i7 = i6 + 2;
    if ((this.setupCount != 0) && (LogStream.level >= 3))
      log.println("setupCount is not zero: " + this.setupCount);
    return i7 - paramInt;
  }
}