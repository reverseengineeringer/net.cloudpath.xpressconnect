package jcifs.smb;

import jcifs.util.Hexdump;

class Trans2QueryFSInformation extends SmbComTransaction
{
  private int informationLevel;

  Trans2QueryFSInformation(int paramInt)
  {
    this.command = 50;
    this.subCommand = 3;
    this.informationLevel = paramInt;
    this.totalParameterCount = 2;
    this.totalDataCount = 0;
    this.maxParameterCount = 0;
    this.maxDataCount = 800;
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
    return new String("Trans2QueryFSInformation[" + super.toString() + ",informationLevel=0x" + Hexdump.toHexString(this.informationLevel, 3) + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.informationLevel, paramArrayOfByte, paramInt);
    return paramInt + 2 - paramInt;
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