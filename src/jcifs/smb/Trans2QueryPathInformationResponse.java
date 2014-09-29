package jcifs.smb;

import java.util.Date;
import jcifs.util.Hexdump;

class Trans2QueryPathInformationResponse extends SmbComTransactionResponse
{
  static final int SMB_QUERY_FILE_BASIC_INFO = 257;
  static final int SMB_QUERY_FILE_STANDARD_INFO = 258;
  Info info;
  private int informationLevel;

  Trans2QueryPathInformationResponse(int paramInt)
  {
    this.informationLevel = paramInt;
    this.subCommand = 5;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    switch (this.informationLevel)
    {
    default:
      return 0;
    case 257:
      return readSmbQueryFileBasicInfoWireFormat(paramArrayOfByte, paramInt1);
    case 258:
    }
    return readSmbQueryFileStandardInfoWireFormat(paramArrayOfByte, paramInt1);
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 2;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSmbQueryFileBasicInfoWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    SmbQueryFileBasicInfo localSmbQueryFileBasicInfo = new SmbQueryFileBasicInfo();
    localSmbQueryFileBasicInfo.createTime = readTime(paramArrayOfByte, paramInt);
    int i = paramInt + 8;
    localSmbQueryFileBasicInfo.lastAccessTime = readTime(paramArrayOfByte, i);
    int j = i + 8;
    localSmbQueryFileBasicInfo.lastWriteTime = readTime(paramArrayOfByte, j);
    int k = j + 8;
    localSmbQueryFileBasicInfo.changeTime = readTime(paramArrayOfByte, k);
    int m = k + 8;
    localSmbQueryFileBasicInfo.attributes = readInt2(paramArrayOfByte, m);
    int n = m + 2;
    this.info = localSmbQueryFileBasicInfo;
    return n - paramInt;
  }

  int readSmbQueryFileStandardInfoWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    boolean bool1 = true;
    SmbQueryFileStandardInfo localSmbQueryFileStandardInfo = new SmbQueryFileStandardInfo();
    localSmbQueryFileStandardInfo.allocationSize = readInt8(paramArrayOfByte, paramInt);
    int i = paramInt + 8;
    localSmbQueryFileStandardInfo.endOfFile = readInt8(paramArrayOfByte, i);
    int j = i + 8;
    localSmbQueryFileStandardInfo.numberOfLinks = readInt4(paramArrayOfByte, j);
    int k = j + 4;
    int m = k + 1;
    boolean bool2;
    int n;
    if ((0xFF & paramArrayOfByte[k]) > 0)
    {
      bool2 = bool1;
      localSmbQueryFileStandardInfo.deletePending = bool2;
      n = m + 1;
      if ((0xFF & paramArrayOfByte[m]) <= 0)
        break label130;
    }
    while (true)
    {
      localSmbQueryFileStandardInfo.directory = bool1;
      this.info = localSmbQueryFileStandardInfo;
      return n - paramInt;
      bool2 = false;
      break;
      label130: bool1 = false;
    }
  }

  public String toString()
  {
    return new String("Trans2QueryPathInformationResponse[" + super.toString() + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  class SmbQueryFileBasicInfo
    implements Info
  {
    int attributes;
    long changeTime;
    long createTime;
    long lastAccessTime;
    long lastWriteTime;

    SmbQueryFileBasicInfo()
    {
    }

    public int getAttributes()
    {
      return this.attributes;
    }

    public long getCreateTime()
    {
      return this.createTime;
    }

    public long getLastWriteTime()
    {
      return this.lastWriteTime;
    }

    public long getSize()
    {
      return 0L;
    }

    public String toString()
    {
      return new String("SmbQueryFileBasicInfo[createTime=" + new Date(this.createTime) + ",lastAccessTime=" + new Date(this.lastAccessTime) + ",lastWriteTime=" + new Date(this.lastWriteTime) + ",changeTime=" + new Date(this.changeTime) + ",attributes=0x" + Hexdump.toHexString(this.attributes, 4) + "]");
    }
  }

  class SmbQueryFileStandardInfo
    implements Info
  {
    long allocationSize;
    boolean deletePending;
    boolean directory;
    long endOfFile;
    int numberOfLinks;

    SmbQueryFileStandardInfo()
    {
    }

    public int getAttributes()
    {
      return 0;
    }

    public long getCreateTime()
    {
      return 0L;
    }

    public long getLastWriteTime()
    {
      return 0L;
    }

    public long getSize()
    {
      return this.endOfFile;
    }

    public String toString()
    {
      return new String("SmbQueryInfoStandard[allocationSize=" + this.allocationSize + ",endOfFile=" + this.endOfFile + ",numberOfLinks=" + this.numberOfLinks + ",deletePending=" + this.deletePending + ",directory=" + this.directory + "]");
    }
  }
}