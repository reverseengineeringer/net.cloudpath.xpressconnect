package jcifs.smb;

class SmbComFindClose2 extends ServerMessageBlock
{
  private int sid;

  SmbComFindClose2(int paramInt)
  {
    this.sid = paramInt;
    this.command = 52;
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
    return new String("SmbComFindClose2[" + super.toString() + ",sid=" + this.sid + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.sid, paramArrayOfByte, paramInt);
    return 2;
  }
}