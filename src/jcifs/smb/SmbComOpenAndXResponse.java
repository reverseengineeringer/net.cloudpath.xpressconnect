package jcifs.smb;

class SmbComOpenAndXResponse extends AndXServerMessageBlock
{
  int action;
  int dataSize;
  int deviceState;
  int fid;
  int fileAttributes;
  int fileType;
  int grantedAccess;
  long lastWriteTime;
  int serverFid;

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.fid = readInt2(paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    this.fileAttributes = readInt2(paramArrayOfByte, i);
    int j = i + 2;
    this.lastWriteTime = readUTime(paramArrayOfByte, j);
    int k = j + 4;
    this.dataSize = readInt4(paramArrayOfByte, k);
    int m = k + 4;
    this.grantedAccess = readInt2(paramArrayOfByte, m);
    int n = m + 2;
    this.fileType = readInt2(paramArrayOfByte, n);
    int i1 = n + 2;
    this.deviceState = readInt2(paramArrayOfByte, i1);
    int i2 = i1 + 2;
    this.action = readInt2(paramArrayOfByte, i2);
    int i3 = i2 + 2;
    this.serverFid = readInt4(paramArrayOfByte, i3);
    return i3 + 6 - paramInt;
  }

  public String toString()
  {
    return new String("SmbComOpenAndXResponse[" + super.toString() + ",fid=" + this.fid + ",fileAttributes=" + this.fileAttributes + ",lastWriteTime=" + this.lastWriteTime + ",dataSize=" + this.dataSize + ",grantedAccess=" + this.grantedAccess + ",fileType=" + this.fileType + ",deviceState=" + this.deviceState + ",action=" + this.action + ",serverFid=" + this.serverFid + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}