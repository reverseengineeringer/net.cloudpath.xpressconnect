package jcifs.smb;

import jcifs.util.LogStream;

class NetShareEnumResponse extends SmbComTransactionResponse
{
  private int converter;
  private int totalAvailableEntries;

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    this.useUnicode = false;
    this.results = new SmbShareInfo[this.numEntries];
    for (int j = 0; j < this.numEntries; j++)
    {
      FileEntry[] arrayOfFileEntry = this.results;
      SmbShareInfo localSmbShareInfo = new SmbShareInfo();
      arrayOfFileEntry[j] = localSmbShareInfo;
      localSmbShareInfo.netName = readString(paramArrayOfByte, paramInt1, 13, false);
      int k = paramInt1 + 14;
      localSmbShareInfo.type = readInt2(paramArrayOfByte, k);
      int m = k + 2;
      int n = readInt4(paramArrayOfByte, m);
      paramInt1 = m + 4;
      localSmbShareInfo.remark = readString(paramArrayOfByte, i + ((0xFFFF & n) - this.converter), 128, false);
      if (LogStream.level >= 4)
        log.println(localSmbShareInfo);
    }
    return paramInt1 - i;
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
    return new String("NetShareEnumResponse[" + super.toString() + ",status=" + this.status + ",converter=" + this.converter + ",entriesReturned=" + this.numEntries + ",totalAvailableEntries=" + this.totalAvailableEntries + "]");
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
}