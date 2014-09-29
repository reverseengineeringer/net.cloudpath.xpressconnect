package jcifs.smb;

import jcifs.Config;

class SmbComReadAndX extends AndXServerMessageBlock
{
  private static final int BATCH_LIMIT = Config.getInt("jcifs.smb.client.ReadAndX.Close", 1);
  private int fid;
  int maxCount;
  int minCount;
  private long offset;
  private int openTimeout;
  int remaining;

  SmbComReadAndX()
  {
    super(null);
    this.command = 46;
    this.openTimeout = -1;
  }

  SmbComReadAndX(int paramInt1, long paramLong, int paramInt2, ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
    this.fid = paramInt1;
    this.offset = paramLong;
    this.minCount = paramInt2;
    this.maxCount = paramInt2;
    this.command = 46;
    this.openTimeout = -1;
  }

  int getBatchLimit(byte paramByte)
  {
    if (paramByte == 4)
      return BATCH_LIMIT;
    return 0;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  void setParam(int paramInt1, long paramLong, int paramInt2)
  {
    this.fid = paramInt1;
    this.offset = paramLong;
    this.minCount = paramInt2;
    this.maxCount = paramInt2;
  }

  public String toString()
  {
    return new String("SmbComReadAndX[" + super.toString() + ",fid=" + this.fid + ",offset=" + this.offset + ",maxCount=" + this.maxCount + ",minCount=" + this.minCount + ",openTimeout=" + this.openTimeout + ",remaining=" + this.remaining + ",offset=" + this.offset + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.fid, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeInt4(this.offset, paramArrayOfByte, i);
    int j = i + 4;
    writeInt2(this.maxCount, paramArrayOfByte, j);
    int k = j + 2;
    writeInt2(this.minCount, paramArrayOfByte, k);
    int m = k + 2;
    writeInt4(this.openTimeout, paramArrayOfByte, m);
    int n = m + 4;
    writeInt2(this.remaining, paramArrayOfByte, n);
    int i1 = n + 2;
    writeInt4(this.offset >> 32, paramArrayOfByte, i1);
    return i1 + 4 - paramInt;
  }
}