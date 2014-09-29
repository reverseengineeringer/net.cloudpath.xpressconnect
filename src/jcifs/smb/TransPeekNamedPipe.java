package jcifs.smb;

class TransPeekNamedPipe extends SmbComTransaction
{
  private int fid;

  TransPeekNamedPipe(String paramString, int paramInt)
  {
    this.name = paramString;
    this.fid = paramInt;
    this.command = 37;
    this.subCommand = 35;
    this.timeout = -1;
    this.maxParameterCount = 6;
    this.maxDataCount = 1;
    this.maxSetupCount = 0;
    this.setupCount = 2;
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
    return new String("TransPeekNamedPipe[" + super.toString() + ",pipeName=" + this.name + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = this.subCommand;
    int j = i + 1;
    paramArrayOfByte[i] = 0;
    writeInt2(this.fid, paramArrayOfByte, j);
    return 4;
  }
}