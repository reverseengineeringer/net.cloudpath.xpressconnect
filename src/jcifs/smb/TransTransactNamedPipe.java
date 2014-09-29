package jcifs.smb;

import jcifs.util.LogStream;

class TransTransactNamedPipe extends SmbComTransaction
{
  private byte[] pipeData;
  private int pipeDataLen;
  private int pipeDataOff;
  private int pipeFid;

  TransTransactNamedPipe(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    this.pipeFid = paramInt1;
    this.pipeData = paramArrayOfByte;
    this.pipeDataOff = paramInt2;
    this.pipeDataLen = paramInt3;
    this.command = 37;
    this.subCommand = 38;
    this.maxParameterCount = 0;
    this.maxDataCount = 65535;
    this.maxSetupCount = 0;
    this.setupCount = 2;
    this.name = "\\PIPE\\";
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
    return new String("TransTransactNamedPipe[" + super.toString() + ",pipeFid=" + this.pipeFid + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte.length - paramInt < this.pipeDataLen)
    {
      if (LogStream.level >= 3)
        log.println("TransTransactNamedPipe data too long for buffer");
      return 0;
    }
    System.arraycopy(this.pipeData, this.pipeDataOff, paramArrayOfByte, paramInt, this.pipeDataLen);
    return this.pipeDataLen;
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
    writeInt2(this.pipeFid, paramArrayOfByte, j);
    (j + 2);
    return 4;
  }
}