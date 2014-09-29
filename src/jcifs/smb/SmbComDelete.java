package jcifs.smb;

import jcifs.util.Hexdump;

class SmbComDelete extends ServerMessageBlock
{
  private int searchAttributes;

  SmbComDelete(String paramString)
  {
    this.path = paramString;
    this.command = 6;
    this.searchAttributes = 6;
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
    return new String("SmbComDelete[" + super.toString() + ",searchAttributes=0x" + Hexdump.toHexString(this.searchAttributes, 4) + ",fileName=" + this.path + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = 4;
    return i + writeString(this.path, paramArrayOfByte, i) - paramInt;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.searchAttributes, paramArrayOfByte, paramInt);
    return 2;
  }
}