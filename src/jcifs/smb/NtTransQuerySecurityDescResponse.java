package jcifs.smb;

import java.io.IOException;

class NtTransQuerySecurityDescResponse extends SmbComNtTransactionResponse
{
  SecurityDescriptor securityDescriptor;

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (this.errorCode != 0)
      return 4;
    try
    {
      this.securityDescriptor = new SecurityDescriptor();
      int i = this.securityDescriptor.decode(paramArrayOfByte, paramInt1, paramInt2);
      return paramInt1 + i - paramInt1;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException(localIOException.getMessage());
    }
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.length = readInt4(paramArrayOfByte, paramInt1);
    return 4;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  public String toString()
  {
    return new String("NtTransQuerySecurityResponse[" + super.toString() + "]");
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