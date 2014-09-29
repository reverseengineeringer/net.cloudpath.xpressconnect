package jcifs.smb;

import java.util.Date;
import jcifs.util.Hexdump;

class SmbComQueryInformationResponse extends ServerMessageBlock
  implements Info
{
  private int fileAttributes = 0;
  private int fileSize = 0;
  private long lastWriteTime = 0L;
  private long serverTimeZoneOffset;

  SmbComQueryInformationResponse(long paramLong)
  {
    this.serverTimeZoneOffset = paramLong;
    this.command = 8;
  }

  public int getAttributes()
  {
    return this.fileAttributes;
  }

  public long getCreateTime()
  {
    return this.lastWriteTime + this.serverTimeZoneOffset;
  }

  public long getLastWriteTime()
  {
    return this.lastWriteTime + this.serverTimeZoneOffset;
  }

  public long getSize()
  {
    return this.fileSize;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.wordCount == 0)
      return 0;
    this.fileAttributes = readInt2(paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    this.lastWriteTime = readUTime(paramArrayOfByte, i);
    this.fileSize = readInt4(paramArrayOfByte, i + 4);
    return 20;
  }

  public String toString()
  {
    return new String("SmbComQueryInformationResponse[" + super.toString() + ",fileAttributes=0x" + Hexdump.toHexString(this.fileAttributes, 4) + ",lastWriteTime=" + new Date(this.lastWriteTime) + ",fileSize=" + this.fileSize + "]");
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