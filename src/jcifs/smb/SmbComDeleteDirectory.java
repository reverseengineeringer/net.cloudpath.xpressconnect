package jcifs.smb;

class SmbComDeleteDirectory extends ServerMessageBlock
{
  SmbComDeleteDirectory(String paramString)
  {
    this.path = paramString;
    this.command = 1;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  public String toString()
  {
    return new String("SmbComDeleteDirectory[" + super.toString() + ",directoryName=" + this.path + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = 4;
    return i + writeString(this.path, paramArrayOfByte, i) - paramInt;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}