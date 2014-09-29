package jcifs.smb;

import jcifs.util.Hexdump;

class Trans2QueryPathInformation extends SmbComTransaction
{
  private int informationLevel;

  Trans2QueryPathInformation(String paramString, int paramInt)
  {
    this.path = paramString;
    this.informationLevel = paramInt;
    this.command = 50;
    this.subCommand = 5;
    this.totalDataCount = 0;
    this.maxParameterCount = 2;
    this.maxDataCount = 40;
    this.maxSetupCount = 0;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  public String toString()
  {
    return new String("Trans2QueryPathInformation[" + super.toString() + ",informationLevel=0x" + Hexdump.toHexString(this.informationLevel, 3) + ",filename=" + this.path + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.informationLevel, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    int j = i + 1;
    paramArrayOfByte[i] = 0;
    int k = j + 1;
    paramArrayOfByte[j] = 0;
    int m = k + 1;
    paramArrayOfByte[k] = 0;
    int n = m + 1;
    paramArrayOfByte[m] = 0;
    return n + writeString(this.path, paramArrayOfByte, n) - paramInt;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = this.subCommand;
    (i + 1);
    paramArrayOfByte[i] = 0;
    return 2;
  }
}