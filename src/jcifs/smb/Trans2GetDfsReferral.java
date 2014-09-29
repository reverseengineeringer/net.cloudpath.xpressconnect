package jcifs.smb;

class Trans2GetDfsReferral extends SmbComTransaction
{
  private int maxReferralLevel = 3;

  Trans2GetDfsReferral(String paramString)
  {
    this.path = paramString;
    this.command = 50;
    this.subCommand = 16;
    this.totalDataCount = 0;
    this.maxParameterCount = 0;
    this.maxDataCount = 4096;
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
    return new String("Trans2GetDfsReferral[" + super.toString() + ",maxReferralLevel=0x" + this.maxReferralLevel + ",filename=" + this.path + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.maxReferralLevel, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    return i + writeString(this.path, paramArrayOfByte, i) - paramInt;
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