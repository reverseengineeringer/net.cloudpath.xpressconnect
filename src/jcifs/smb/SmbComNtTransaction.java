package jcifs.smb;

abstract class SmbComNtTransaction extends SmbComTransaction
{
  private static final int NTT_PRIMARY_SETUP_OFFSET = 69;
  private static final int NTT_SECONDARY_PARAMETER_OFFSET = 51;
  static final int NT_TRANSACT_QUERY_SECURITY_DESC = 6;
  int function;

  SmbComNtTransaction()
  {
    this.primarySetupOffset = 69;
    this.secondaryParameterOffset = 51;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int j;
    int i3;
    label151: int i6;
    label216: int i7;
    int i12;
    if (this.command != -95)
    {
      int i14 = paramInt + 1;
      paramArrayOfByte[paramInt] = this.maxSetupCount;
      j = i14;
      int k = j + 1;
      paramArrayOfByte[j] = 0;
      int m = k + 1;
      paramArrayOfByte[k] = 0;
      writeInt4(this.totalParameterCount, paramArrayOfByte, m);
      int n = m + 4;
      writeInt4(this.totalDataCount, paramArrayOfByte, n);
      int i1 = n + 4;
      if (this.command != -95)
      {
        writeInt4(this.maxParameterCount, paramArrayOfByte, i1);
        int i13 = i1 + 4;
        writeInt4(this.maxDataCount, paramArrayOfByte, i13);
        i1 = i13 + 4;
      }
      writeInt4(this.parameterCount, paramArrayOfByte, i1);
      int i2 = i1 + 4;
      if (this.parameterCount != 0)
        break label291;
      i3 = 0;
      writeInt4(i3, paramArrayOfByte, i2);
      int i4 = i2 + 4;
      if (this.command == -95)
      {
        writeInt4(this.parameterDisplacement, paramArrayOfByte, i4);
        i4 += 4;
      }
      writeInt4(this.dataCount, paramArrayOfByte, i4);
      int i5 = i4 + 4;
      if (this.dataCount != 0)
        break label300;
      i6 = 0;
      writeInt4(i6, paramArrayOfByte, i5);
      i7 = i5 + 4;
      if (this.command != -95)
        break label309;
      writeInt4(this.dataDisplacement, paramArrayOfByte, i7);
      int i11 = i7 + 4;
      i12 = i11 + 1;
      paramArrayOfByte[i11] = 0;
    }
    label291: label300: label309: int i9;
    for (int i10 = i12; ; i10 = i9 + writeSetupWireFormat(paramArrayOfByte, i9))
    {
      return i10 - paramInt;
      int i = paramInt + 1;
      paramArrayOfByte[paramInt] = 0;
      j = i;
      break;
      i3 = this.parameterOffset;
      break label151;
      i6 = this.dataOffset;
      break label216;
      int i8 = i7 + 1;
      paramArrayOfByte[i7] = ((byte)this.setupCount);
      writeInt2(this.function, paramArrayOfByte, i8);
      i9 = i8 + 2;
    }
  }
}