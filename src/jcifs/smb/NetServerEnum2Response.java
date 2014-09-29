package jcifs.smb;

import jcifs.util.Hexdump;
import jcifs.util.LogStream;

class NetServerEnum2Response extends SmbComTransactionResponse
{
  private int converter;
  String lastName;
  private int totalAvailableEntries;

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    ServerInfo1 localServerInfo1 = null;
    this.results = new ServerInfo1[this.numEntries];
    for (int j = 0; j < this.numEntries; j++)
    {
      FileEntry[] arrayOfFileEntry = this.results;
      localServerInfo1 = new ServerInfo1();
      arrayOfFileEntry[j] = localServerInfo1;
      localServerInfo1.name = readString(paramArrayOfByte, paramInt1, 16, false);
      int k = paramInt1 + 16;
      int m = k + 1;
      localServerInfo1.versionMajor = (0xFF & paramArrayOfByte[k]);
      int n = m + 1;
      localServerInfo1.versionMinor = (0xFF & paramArrayOfByte[m]);
      localServerInfo1.type = readInt4(paramArrayOfByte, n);
      int i1 = n + 4;
      int i2 = readInt4(paramArrayOfByte, i1);
      paramInt1 = i1 + 4;
      localServerInfo1.commentOrMasterBrowser = readString(paramArrayOfByte, i + ((0xFFFF & i2) - this.converter), 48, false);
      if (LogStream.level >= 4)
        log.println(localServerInfo1);
    }
    if (this.numEntries == 0);
    for (String str = null; ; str = localServerInfo1.name)
    {
      this.lastName = str;
      return paramInt1 - i;
    }
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.status = readInt2(paramArrayOfByte, paramInt1);
    int i = paramInt1 + 2;
    this.converter = readInt2(paramArrayOfByte, i);
    int j = i + 2;
    this.numEntries = readInt2(paramArrayOfByte, j);
    int k = j + 2;
    this.totalAvailableEntries = readInt2(paramArrayOfByte, k);
    return k + 2 - paramInt1;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  public String toString()
  {
    return new String("NetServerEnum2Response[" + super.toString() + ",status=" + this.status + ",converter=" + this.converter + ",entriesReturned=" + this.numEntries + ",totalAvailableEntries=" + this.totalAvailableEntries + ",lastName=" + this.lastName + "]");
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

  class ServerInfo1
    implements FileEntry
  {
    String commentOrMasterBrowser;
    String name;
    int type;
    int versionMajor;
    int versionMinor;

    ServerInfo1()
    {
    }

    public long createTime()
    {
      return 0L;
    }

    public int getAttributes()
    {
      return 17;
    }

    public String getName()
    {
      return this.name;
    }

    public int getType()
    {
      if ((0x80000000 & this.type) != 0)
        return 2;
      return 4;
    }

    public long lastModified()
    {
      return 0L;
    }

    public long length()
    {
      return 0L;
    }

    public String toString()
    {
      return new String("ServerInfo1[name=" + this.name + ",versionMajor=" + this.versionMajor + ",versionMinor=" + this.versionMinor + ",type=0x" + Hexdump.toHexString(this.type, 8) + ",commentOrMasterBrowser=" + this.commentOrMasterBrowser + "]");
    }
  }
}