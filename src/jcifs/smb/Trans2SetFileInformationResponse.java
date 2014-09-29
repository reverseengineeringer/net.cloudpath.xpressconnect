package jcifs.smb;

class Trans2SetFileInformationResponse extends SmbComTransactionResponse
{
  Trans2SetFileInformationResponse()
  {
    this.subCommand = 8;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  public String toString()
  {
    return new String("Trans2SetFileInformationResponse[" + super.toString() + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}