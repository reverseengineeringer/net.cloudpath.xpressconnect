package jcifs.netbios;

class NodeStatusRequest extends NameServicePacket
{
  NodeStatusRequest(Name paramName)
  {
    this.questionName = paramName;
    this.questionType = 33;
    this.isRecurDesired = false;
    this.isBroadcast = false;
  }

  int readBodyWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readRDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  public String toString()
  {
    return new String("NodeStatusRequest[" + super.toString() + "]");
  }

  int writeBodyWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = this.questionName.hexCode;
    this.questionName.hexCode = 0;
    int j = writeQuestionSectionWireFormat(paramArrayOfByte, paramInt);
    this.questionName.hexCode = i;
    return j;
  }

  int writeRDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}