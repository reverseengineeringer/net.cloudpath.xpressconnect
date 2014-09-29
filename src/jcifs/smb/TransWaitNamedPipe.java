package jcifs.smb;

class TransWaitNamedPipe extends SmbComTransaction
{
  TransWaitNamedPipe(String paramString)
  {
    this.name = paramString;
    this.command = 37;
    this.subCommand = 83;
    this.timeout = -1;
    this.maxParameterCount = 0;
    this.maxDataCount = 0;
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
    return new String("TransWaitNamedPipe[" + super.toString() + ",pipeName=" + this.name + "]");
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
    int k = j + 1;
    paramArrayOfByte[j] = 0;
    (k + 1);
    paramArrayOfByte[k] = 0;
    return 4;
  }
}