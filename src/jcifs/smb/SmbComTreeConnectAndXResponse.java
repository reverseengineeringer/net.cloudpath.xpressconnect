package jcifs.smb;

import java.io.UnsupportedEncodingException;

class SmbComTreeConnectAndXResponse extends AndXServerMessageBlock
{
  private static final int SMB_SHARE_IS_IN_DFS = 2;
  private static final int SMB_SUPPORT_SEARCH_BITS = 1;
  String nativeFileSystem = "";
  String service;
  boolean shareIsInDfs;
  boolean supportSearchBits;

  SmbComTreeConnectAndXResponse(ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = readStringLength(paramArrayOfByte, paramInt, 32);
    try
    {
      this.service = new String(paramArrayOfByte, paramInt, i, "ASCII");
      return paramInt + (i + 1) - paramInt;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 1;
    int j;
    if ((0x1 & paramArrayOfByte[paramInt]) == i)
    {
      j = i;
      this.supportSearchBits = j;
      if ((0x2 & paramArrayOfByte[paramInt]) != 2)
        break label42;
    }
    while (true)
    {
      this.shareIsInDfs = i;
      return 2;
      j = 0;
      break;
      label42: i = 0;
    }
  }

  public String toString()
  {
    return new String("SmbComTreeConnectAndXResponse[" + super.toString() + ",supportSearchBits=" + this.supportSearchBits + ",shareIsInDfs=" + this.shareIsInDfs + ",service=" + this.service + ",nativeFileSystem=" + this.nativeFileSystem + "]");
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