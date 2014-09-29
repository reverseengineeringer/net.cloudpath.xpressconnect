package jcifs.smb;

class SmbComLogoffAndX extends AndXServerMessageBlock
{
  SmbComLogoffAndX(ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
    this.command = 116;
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
    return new String("SmbComLogoffAndX[" + super.toString() + "]");
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