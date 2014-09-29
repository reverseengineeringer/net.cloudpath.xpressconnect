package jcifs.smb;

import java.util.Date;
import jcifs.util.Hexdump;

class SmbComNTCreateAndXResponse extends AndXServerMessageBlock
{
  static final int BATCH_OPLOCK_GRANTED = 2;
  static final int EXCLUSIVE_OPLOCK_GRANTED = 1;
  static final int LEVEL_II_OPLOCK_GRANTED = 3;
  long allocationSize;
  long changeTime;
  int createAction;
  long creationTime;
  int deviceState;
  boolean directory;
  long endOfFile;
  int extFileAttributes;
  int fid;
  int fileType;
  boolean isExtended;
  long lastAccessTime;
  long lastWriteTime;
  byte oplockLevel;

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    this.oplockLevel = paramArrayOfByte[paramInt];
    this.fid = readInt2(paramArrayOfByte, i);
    int j = i + 2;
    this.createAction = readInt4(paramArrayOfByte, j);
    int k = j + 4;
    this.creationTime = readTime(paramArrayOfByte, k);
    int m = k + 8;
    this.lastAccessTime = readTime(paramArrayOfByte, m);
    int n = m + 8;
    this.lastWriteTime = readTime(paramArrayOfByte, n);
    int i1 = n + 8;
    this.changeTime = readTime(paramArrayOfByte, i1);
    int i2 = i1 + 8;
    this.extFileAttributes = readInt4(paramArrayOfByte, i2);
    int i3 = i2 + 4;
    this.allocationSize = readInt8(paramArrayOfByte, i3);
    int i4 = i3 + 8;
    this.endOfFile = readInt8(paramArrayOfByte, i4);
    int i5 = i4 + 8;
    this.fileType = readInt2(paramArrayOfByte, i5);
    int i6 = i5 + 2;
    this.deviceState = readInt2(paramArrayOfByte, i6);
    int i7 = i6 + 2;
    int i8 = i7 + 1;
    if ((0xFF & paramArrayOfByte[i7]) > 0);
    for (boolean bool = true; ; bool = false)
    {
      this.directory = bool;
      return i8 - paramInt;
    }
  }

  public String toString()
  {
    return new String("SmbComNTCreateAndXResponse[" + super.toString() + ",oplockLevel=" + this.oplockLevel + ",fid=" + this.fid + ",createAction=0x" + Hexdump.toHexString(this.createAction, 4) + ",creationTime=" + new Date(this.creationTime) + ",lastAccessTime=" + new Date(this.lastAccessTime) + ",lastWriteTime=" + new Date(this.lastWriteTime) + ",changeTime=" + new Date(this.changeTime) + ",extFileAttributes=0x" + Hexdump.toHexString(this.extFileAttributes, 4) + ",allocationSize=" + this.allocationSize + ",endOfFile=" + this.endOfFile + ",fileType=" + this.fileType + ",deviceState=" + this.deviceState + ",directory=" + this.directory + "]");
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