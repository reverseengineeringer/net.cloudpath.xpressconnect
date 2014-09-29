package jcifs.netbios;

class NameQueryResponse extends NameServicePacket
{
  NameQueryResponse()
  {
    this.recordName = new Name();
  }

  int readBodyWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return readResourceRecordWireFormat(paramArrayOfByte, paramInt);
  }

  int readRDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    if ((this.resultCode != 0) || (this.opCode != 0))
      return 0;
    int i = 0x80 & paramArrayOfByte[paramInt];
    boolean bool = false;
    if (i == 128)
      bool = true;
    int j = (0x60 & paramArrayOfByte[paramInt]) >> 5;
    int k = readInt4(paramArrayOfByte, paramInt + 2);
    if (k != 0)
      this.addrEntry[this.addrIndex] = new NbtAddress(this.recordName, k, bool, j);
    while (true)
    {
      return 6;
      this.addrEntry[this.addrIndex] = null;
    }
  }

  public String toString()
  {
    return new String("NameQueryResponse[" + super.toString() + ",addrEntry=" + this.addrEntry + "]");
  }

  int writeBodyWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeRDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}