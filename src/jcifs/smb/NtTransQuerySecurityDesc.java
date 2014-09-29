package jcifs.smb;

import jcifs.util.Hexdump;

class NtTransQuerySecurityDesc extends SmbComNtTransaction
{
  int fid;
  int securityInformation;

  NtTransQuerySecurityDesc(int paramInt1, int paramInt2)
  {
    this.fid = paramInt1;
    this.securityInformation = paramInt2;
    this.command = -96;
    this.function = 6;
    this.setupCount = 0;
    this.totalDataCount = 0;
    this.maxParameterCount = 4;
    this.maxDataCount = 32768;
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
    return new String("NtTransQuerySecurityDesc[" + super.toString() + ",fid=0x" + Hexdump.toHexString(this.fid, 4) + ",securityInformation=0x" + Hexdump.toHexString(this.securityInformation, 8) + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.fid, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    int j = i + 1;
    paramArrayOfByte[i] = 0;
    int k = j + 1;
    paramArrayOfByte[j] = 0;
    writeInt4(this.securityInformation, paramArrayOfByte, k);
    return k + 4 - paramInt;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}