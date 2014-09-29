package jcifs.smb;

import jcifs.util.LogStream;

class TransCallNamedPipe extends SmbComTransaction
{
  private byte[] pipeData;
  private int pipeDataLen;
  private int pipeDataOff;

  TransCallNamedPipe(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.name = paramString;
    this.pipeData = paramArrayOfByte;
    this.pipeDataOff = paramInt1;
    this.pipeDataLen = paramInt2;
    this.command = 37;
    this.subCommand = 84;
    this.timeout = -1;
    this.maxParameterCount = 0;
    this.maxDataCount = 65535;
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
    return new String("TransCallNamedPipe[" + super.toString() + ",pipeName=" + this.name + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte.length - paramInt < this.pipeDataLen)
    {
      if (LogStream.level >= 3)
        log.println("TransCallNamedPipe data too long for buffer");
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
    int k = j + 1;
    paramArrayOfByte[j] = 0;
    (k + 1);
    paramArrayOfByte[k] = 0;
    return 4;
  }
}