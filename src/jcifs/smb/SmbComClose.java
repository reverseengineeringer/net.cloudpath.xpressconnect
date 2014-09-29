package jcifs.smb;

class SmbComClose extends ServerMessageBlock
{
  private int fid;
  private long lastWriteTime;

  SmbComClose(int paramInt, long paramLong)
  {
    this.fid = paramInt;
    this.lastWriteTime = paramLong;
    this.command = 4;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  public String toString()
  {
    return new String("SmbComClose[" + super.toString() + ",fid=" + this.fid + ",lastWriteTime=" + this.lastWriteTime + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.fid, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeUTime(this.lastWriteTime, paramArrayOfByte, i);
    return 6;
  }
}