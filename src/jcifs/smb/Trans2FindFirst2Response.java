package jcifs.smb;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import jcifs.util.LogStream;

class Trans2FindFirst2Response extends SmbComTransactionResponse
{
  static final int SMB_FILE_BOTH_DIRECTORY_INFO = 260;
  static final int SMB_FILE_NAMES_INFO = 259;
  static final int SMB_FIND_FILE_DIRECTORY_INFO = 257;
  static final int SMB_FIND_FILE_FULL_DIRECTORY_INFO = 258;
  static final int SMB_INFO_QUERY_EAS_FROM_LIST = 3;
  static final int SMB_INFO_QUERY_EA_SIZE = 2;
  static final int SMB_INFO_STANDARD = 1;
  int eaErrorOffset;
  boolean isEndOfSearch;
  String lastName;
  int lastNameBufferIndex;
  int lastNameOffset;
  int resumeKey;
  int sid;

  Trans2FindFirst2Response()
  {
    this.command = 50;
    this.subCommand = 1;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.lastNameBufferIndex = (paramInt1 + this.lastNameOffset);
    this.results = new SmbFindFileBothDirectoryInfo[this.numEntries];
    for (int i = 0; i < this.numEntries; i++)
    {
      FileEntry[] arrayOfFileEntry = this.results;
      SmbFindFileBothDirectoryInfo localSmbFindFileBothDirectoryInfo = new SmbFindFileBothDirectoryInfo();
      arrayOfFileEntry[i] = localSmbFindFileBothDirectoryInfo;
      localSmbFindFileBothDirectoryInfo.nextEntryOffset = readInt4(paramArrayOfByte, paramInt1);
      localSmbFindFileBothDirectoryInfo.fileIndex = readInt4(paramArrayOfByte, paramInt1 + 4);
      localSmbFindFileBothDirectoryInfo.creationTime = readTime(paramArrayOfByte, paramInt1 + 8);
      localSmbFindFileBothDirectoryInfo.lastWriteTime = readTime(paramArrayOfByte, paramInt1 + 24);
      localSmbFindFileBothDirectoryInfo.endOfFile = readInt8(paramArrayOfByte, paramInt1 + 40);
      localSmbFindFileBothDirectoryInfo.extFileAttributes = readInt4(paramArrayOfByte, paramInt1 + 56);
      localSmbFindFileBothDirectoryInfo.fileNameLength = readInt4(paramArrayOfByte, paramInt1 + 60);
      localSmbFindFileBothDirectoryInfo.filename = readString(paramArrayOfByte, paramInt1 + 94, localSmbFindFileBothDirectoryInfo.fileNameLength);
      if ((this.lastNameBufferIndex >= paramInt1) && ((localSmbFindFileBothDirectoryInfo.nextEntryOffset == 0) || (this.lastNameBufferIndex < paramInt1 + localSmbFindFileBothDirectoryInfo.nextEntryOffset)))
      {
        this.lastName = localSmbFindFileBothDirectoryInfo.filename;
        this.resumeKey = localSmbFindFileBothDirectoryInfo.fileIndex;
      }
      paramInt1 += localSmbFindFileBothDirectoryInfo.nextEntryOffset;
    }
    return this.dataCount;
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 1;
    int j = paramInt1;
    if (this.subCommand == i)
    {
      this.sid = readInt2(paramArrayOfByte, paramInt1);
      paramInt1 += 2;
    }
    this.numEntries = readInt2(paramArrayOfByte, paramInt1);
    int k = paramInt1 + 2;
    if ((0x1 & paramArrayOfByte[k]) == i);
    while (true)
    {
      this.isEndOfSearch = i;
      int m = k + 2;
      this.eaErrorOffset = readInt2(paramArrayOfByte, m);
      int n = m + 2;
      this.lastNameOffset = readInt2(paramArrayOfByte, n);
      return n + 2 - j;
      i = 0;
    }
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  String readString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      if (this.useUnicode)
        return new String(paramArrayOfByte, paramInt1, paramInt2, "UnicodeLittleUnmarked");
      if ((paramInt2 > 0) && (paramArrayOfByte[(-1 + (paramInt1 + paramInt2))] == 0))
        paramInt2--;
      String str = new String(paramArrayOfByte, paramInt1, paramInt2, SmbConstants.OEM_ENCODING);
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      if (LogStream.level > 1)
        localUnsupportedEncodingException.printStackTrace(log);
    }
    return null;
  }

  public String toString()
  {
    if (this.subCommand == 1);
    for (String str = "Trans2FindFirst2Response["; ; str = "Trans2FindNext2Response[")
      return new String(str + super.toString() + ",sid=" + this.sid + ",searchCount=" + this.numEntries + ",isEndOfSearch=" + this.isEndOfSearch + ",eaErrorOffset=" + this.eaErrorOffset + ",lastNameOffset=" + this.lastNameOffset + ",lastName=" + this.lastName + "]");
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

  class SmbFindFileBothDirectoryInfo
    implements FileEntry
  {
    long allocationSize;
    long changeTime;
    long creationTime;
    int eaSize;
    long endOfFile;
    int extFileAttributes;
    int fileIndex;
    int fileNameLength;
    String filename;
    long lastAccessTime;
    long lastWriteTime;
    int nextEntryOffset;
    String shortName;
    int shortNameLength;

    SmbFindFileBothDirectoryInfo()
    {
    }

    public long createTime()
    {
      return this.creationTime;
    }

    public int getAttributes()
    {
      return this.extFileAttributes;
    }

    public String getName()
    {
      return this.filename;
    }

    public int getType()
    {
      return 1;
    }

    public long lastModified()
    {
      return this.lastWriteTime;
    }

    public long length()
    {
      return this.endOfFile;
    }

    public String toString()
    {
      return new String("SmbFindFileBothDirectoryInfo[nextEntryOffset=" + this.nextEntryOffset + ",fileIndex=" + this.fileIndex + ",creationTime=" + new Date(this.creationTime) + ",lastAccessTime=" + new Date(this.lastAccessTime) + ",lastWriteTime=" + new Date(this.lastWriteTime) + ",changeTime=" + new Date(this.changeTime) + ",endOfFile=" + this.endOfFile + ",allocationSize=" + this.allocationSize + ",extFileAttributes=" + this.extFileAttributes + ",fileNameLength=" + this.fileNameLength + ",eaSize=" + this.eaSize + ",shortNameLength=" + this.shortNameLength + ",shortName=" + this.shortName + ",filename=" + this.filename + "]");
    }
  }
}