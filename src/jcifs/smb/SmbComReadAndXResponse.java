package jcifs.smb;

class SmbComReadAndXResponse extends AndXServerMessageBlock
{
  byte[] b;
  int dataCompactionMode;
  int dataLength;
  int dataOffset;
  int off;

  SmbComReadAndXResponse()
  {
  }

  SmbComReadAndXResponse(byte[] paramArrayOfByte, int paramInt)
  {
    this.b = paramArrayOfByte;
    this.off = paramInt;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 2;
    this.dataCompactionMode = readInt2(paramArrayOfByte, i);
    int j = i + 4;
    this.dataLength = readInt2(paramArrayOfByte, j);
    int k = j + 2;
    this.dataOffset = readInt2(paramArrayOfByte, k);
    return k + 12 - paramInt;
  }

  void setParam(byte[] paramArrayOfByte, int paramInt)
  {
    this.b = paramArrayOfByte;
    this.off = paramInt;
  }

  public String toString()
  {
    return new String("SmbComReadAndXResponse[" + super.toString() + ",dataCompactionMode=" + this.dataCompactionMode + ",dataLength=" + this.dataLength + ",dataOffset=" + this.dataOffset + "]");
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