package jcifs.smb;

class Trans2QueryFSInformationResponse extends SmbComTransactionResponse
{
  static final int SMB_FS_FULL_SIZE_INFORMATION = 1007;
  static final int SMB_INFO_ALLOCATION = 1;
  static final int SMB_QUERY_FS_SIZE_INFO = 259;
  AllocInfo info;
  private int informationLevel;

  Trans2QueryFSInformationResponse(int paramInt)
  {
    this.informationLevel = paramInt;
    this.command = 50;
    this.subCommand = 3;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    switch (this.informationLevel)
    {
    default:
      return 0;
    case 1:
      return readSmbInfoAllocationWireFormat(paramArrayOfByte, paramInt1);
    case 259:
      return readSmbQueryFSSizeInfoWireFormat(paramArrayOfByte, paramInt1);
    case 1007:
    }
    return readFsFullSizeInformationWireFormat(paramArrayOfByte, paramInt1);
  }

  int readFsFullSizeInformationWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    SmbInfoAllocation localSmbInfoAllocation = new SmbInfoAllocation();
    localSmbInfoAllocation.alloc = readInt8(paramArrayOfByte, paramInt);
    int i = paramInt + 8;
    localSmbInfoAllocation.free = readInt8(paramArrayOfByte, i);
    int j = 8 + (i + 8);
    localSmbInfoAllocation.sectPerAlloc = readInt4(paramArrayOfByte, j);
    int k = j + 4;
    localSmbInfoAllocation.bytesPerSect = readInt4(paramArrayOfByte, k);
    int m = k + 4;
    this.info = localSmbInfoAllocation;
    return m - paramInt;
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSmbInfoAllocationWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    SmbInfoAllocation localSmbInfoAllocation = new SmbInfoAllocation();
    int i = paramInt + 4;
    localSmbInfoAllocation.sectPerAlloc = readInt4(paramArrayOfByte, i);
    int j = i + 4;
    localSmbInfoAllocation.alloc = readInt4(paramArrayOfByte, j);
    int k = j + 4;
    localSmbInfoAllocation.free = readInt4(paramArrayOfByte, k);
    int m = k + 4;
    localSmbInfoAllocation.bytesPerSect = readInt2(paramArrayOfByte, m);
    int n = m + 4;
    this.info = localSmbInfoAllocation;
    return n - paramInt;
  }

  int readSmbQueryFSSizeInfoWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    SmbInfoAllocation localSmbInfoAllocation = new SmbInfoAllocation();
    localSmbInfoAllocation.alloc = readInt8(paramArrayOfByte, paramInt);
    int i = paramInt + 8;
    localSmbInfoAllocation.free = readInt8(paramArrayOfByte, i);
    int j = i + 8;
    localSmbInfoAllocation.sectPerAlloc = readInt4(paramArrayOfByte, j);
    int k = j + 4;
    localSmbInfoAllocation.bytesPerSect = readInt4(paramArrayOfByte, k);
    int m = k + 4;
    this.info = localSmbInfoAllocation;
    return m - paramInt;
  }

  public String toString()
  {
    return new String("Trans2QueryFSInformationResponse[" + super.toString() + "]");
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

  class SmbInfoAllocation
    implements AllocInfo
  {
    long alloc;
    int bytesPerSect;
    long free;
    int sectPerAlloc;

    SmbInfoAllocation()
    {
    }

    public long getCapacity()
    {
      return this.alloc * this.sectPerAlloc * this.bytesPerSect;
    }

    public long getFree()
    {
      return this.free * this.sectPerAlloc * this.bytesPerSect;
    }

    public String toString()
    {
      return new String("SmbInfoAllocation[alloc=" + this.alloc + ",free=" + this.free + ",sectPerAlloc=" + this.sectPerAlloc + ",bytesPerSect=" + this.bytesPerSect + "]");
    }
  }
}