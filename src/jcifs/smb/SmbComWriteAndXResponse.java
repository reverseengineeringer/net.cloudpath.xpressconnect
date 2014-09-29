package jcifs.smb;

class SmbComWriteAndXResponse extends AndXServerMessageBlock
{
  long count;

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.count = (0xFFFF & readInt2(paramArrayOfByte, paramInt));
    return 8;
  }

  public String toString()
  {
    return new String("SmbComWriteAndXResponse[" + super.toString() + ",count=" + this.count + "]");
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