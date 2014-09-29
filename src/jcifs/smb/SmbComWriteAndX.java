package jcifs.smb;

import jcifs.Config;

class SmbComWriteAndX extends AndXServerMessageBlock
{
  private static final int CLOSE_BATCH_LIMIT = Config.getInt("jcifs.smb.client.WriteAndX.Close", 1);
  private static final int READ_ANDX_BATCH_LIMIT = Config.getInt("jcifs.smb.client.WriteAndX.ReadAndX", 1);
  private byte[] b;
  private int dataLength;
  private int dataOffset;
  private int fid;
  private int off;
  private long offset;
  private int pad;
  private int remaining;
  int writeMode;

  SmbComWriteAndX()
  {
    super(null);
    this.command = 47;
  }

  SmbComWriteAndX(int paramInt1, long paramLong, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
    this.fid = paramInt1;
    this.offset = paramLong;
    this.remaining = paramInt2;
    this.b = paramArrayOfByte;
    this.off = paramInt3;
    this.dataLength = paramInt4;
    this.command = 47;
  }

  int getBatchLimit(byte paramByte)
  {
    if (paramByte == 46)
      return READ_ANDX_BATCH_LIMIT;
    if (paramByte == 4)
      return CLOSE_BATCH_LIMIT;
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

  void setParam(int paramInt1, long paramLong, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
  {
    this.fid = paramInt1;
    this.offset = paramLong;
    this.remaining = paramInt2;
    this.b = paramArrayOfByte;
    this.off = paramInt3;
    this.dataLength = paramInt4;
    this.digest = null;
  }

  public String toString()
  {
    return new String("SmbComWriteAndX[" + super.toString() + ",fid=" + this.fid + ",offset=" + this.offset + ",writeMode=" + this.writeMode + ",remaining=" + this.remaining + ",dataLength=" + this.dataLength + ",dataOffset=" + this.dataOffset + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt;
    while (true)
    {
      int j = this.pad;
      this.pad = (j - 1);
      if (j <= 0)
        break;
      int k = paramInt + 1;
      paramArrayOfByte[paramInt] = -18;
      paramInt = k;
    }
    System.arraycopy(this.b, this.off, paramArrayOfByte, paramInt, this.dataLength);
    return paramInt + this.dataLength - i;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.dataOffset = (26 + (paramInt - this.headerStart));
    this.pad = ((this.dataOffset - this.headerStart) % 4);
    if (this.pad == 0);
    int n;
    for (int i = 0; ; i = 4 - this.pad)
    {
      this.pad = i;
      this.dataOffset += this.pad;
      writeInt2(this.fid, paramArrayOfByte, paramInt);
      int j = paramInt + 2;
      writeInt4(this.offset, paramArrayOfByte, j);
      int k = j + 4;
      int m = 0;
      int i7;
      for (n = k; m < 4; n = i7)
      {
        i7 = n + 1;
        paramArrayOfByte[n] = -1;
        m++;
      }
    }
    writeInt2(this.writeMode, paramArrayOfByte, n);
    int i1 = n + 2;
    writeInt2(this.remaining, paramArrayOfByte, i1);
    int i2 = i1 + 2;
    int i3 = i2 + 1;
    paramArrayOfByte[i2] = 0;
    int i4 = i3 + 1;
    paramArrayOfByte[i3] = 0;
    writeInt2(this.dataLength, paramArrayOfByte, i4);
    int i5 = i4 + 2;
    writeInt2(this.dataOffset, paramArrayOfByte, i5);
    int i6 = i5 + 2;
    writeInt4(this.offset >> 32, paramArrayOfByte, i6);
    return i6 + 4 - paramInt;
  }
}