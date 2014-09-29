package jcifs.smb;

import jcifs.util.Hexdump;

class SmbComRename extends ServerMessageBlock
{
  private String newFileName;
  private String oldFileName;
  private int searchAttributes;

  SmbComRename(String paramString1, String paramString2)
  {
    this.command = 7;
    this.oldFileName = paramString1;
    this.newFileName = paramString2;
    this.searchAttributes = 22;
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
    return new String("SmbComRename[" + super.toString() + ",searchAttributes=0x" + Hexdump.toHexString(this.searchAttributes, 4) + ",oldFileName=" + this.oldFileName + ",newFileName=" + this.newFileName + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = 4;
    int j = i + writeString(this.oldFileName, paramArrayOfByte, i);
    int k = j + 1;
    paramArrayOfByte[j] = 4;
    int m;
    if (this.useUnicode)
    {
      m = k + 1;
      paramArrayOfByte[k] = 0;
    }
    while (true)
    {
      return m + writeString(this.newFileName, paramArrayOfByte, m) - paramInt;
      m = k;
    }
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.searchAttributes, paramArrayOfByte, paramInt);
    return 2;
  }
}