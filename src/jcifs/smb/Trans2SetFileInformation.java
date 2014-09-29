package jcifs.smb;

class Trans2SetFileInformation extends SmbComTransaction
{
  static final int SMB_FILE_BASIC_INFO = 257;
  private int attributes;
  private long createTime;
  private int fid;
  private long lastWriteTime;

  Trans2SetFileInformation(int paramInt1, int paramInt2, long paramLong1, long paramLong2)
  {
    this.fid = paramInt1;
    this.attributes = paramInt2;
    this.createTime = paramLong1;
    this.lastWriteTime = paramLong2;
    this.command = 50;
    this.subCommand = 8;
    this.maxParameterCount = 6;
    this.maxDataCount = 0;
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
    return new String("Trans2SetFileInformation[" + super.toString() + ",fid=" + this.fid + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeTime(this.createTime, paramArrayOfByte, paramInt);
    int i = paramInt + 8;
    writeInt8(0L, paramArrayOfByte, i);
    int j = i + 8;
    writeTime(this.lastWriteTime, paramArrayOfByte, j);
    int k = j + 8;
    writeInt8(0L, paramArrayOfByte, k);
    int m = k + 8;
    writeInt2(0x80 | this.attributes, paramArrayOfByte, m);
    int n = m + 2;
    writeInt8(0L, paramArrayOfByte, n);
    return n + 6 - paramInt;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.fid, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeInt2(257L, paramArrayOfByte, i);
    int j = i + 2;
    writeInt2(0L, paramArrayOfByte, j);
    return j + 2 - paramInt;
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