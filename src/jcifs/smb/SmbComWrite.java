package jcifs.smb;

class SmbComWrite extends ServerMessageBlock
{
  private byte[] b;
  private int count;
  private int fid;
  private int off;
  private int offset;
  private int remaining;

  SmbComWrite()
  {
    this.command = 11;
  }

  SmbComWrite(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4, int paramInt5)
  {
    this.fid = paramInt1;
    this.count = paramInt5;
    this.offset = paramInt2;
    this.remaining = paramInt3;
    this.b = paramArrayOfByte;
    this.off = paramInt4;
    this.command = 11;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  void setParam(int paramInt1, long paramLong, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
  {
    this.fid = paramInt1;
    this.offset = ((int)(0xFFFFFFFF & paramLong));
    this.remaining = paramInt2;
    this.b = paramArrayOfByte;
    this.off = paramInt3;
    this.count = paramInt4;
    this.digest = null;
  }

  public String toString()
  {
    return new String("SmbComWrite[" + super.toString() + ",fid=" + this.fid + ",count=" + this.count + ",offset=" + this.offset + ",remaining=" + this.remaining + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = 1;
    writeInt2(this.count, paramArrayOfByte, i);
    int j = i + 2;
    System.arraycopy(this.b, this.off, paramArrayOfByte, j, this.count);
    return j + this.count - paramInt;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.fid, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeInt2(this.count, paramArrayOfByte, i);
    int j = i + 2;
    writeInt4(this.offset, paramArrayOfByte, j);
    int k = j + 4;
    writeInt2(this.remaining, paramArrayOfByte, k);
    return k + 2 - paramInt;
  }
}