package jcifs.smb;

import java.io.UnsupportedEncodingException;

class NetShareEnum extends SmbComTransaction
{
  private static final String DESCR = "";

  NetShareEnum()
  {
    this.command = 37;
    this.subCommand = 0;
    this.name = new String("\\PIPE\\LANMAN");
    this.maxParameterCount = 8;
    this.maxSetupCount = 0;
    this.setupCount = 0;
    this.timeout = 5000;
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
    return new String("NetShareEnum[" + super.toString() + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    try
    {
      byte[] arrayOfByte = "".getBytes("ASCII");
      writeInt2(0L, paramArrayOfByte, paramInt);
      int i = paramInt + 2;
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, i, arrayOfByte.length);
      int j = i + arrayOfByte.length;
      writeInt2(1L, paramArrayOfByte, j);
      int k = j + 2;
      writeInt2(this.maxDataCount, paramArrayOfByte, k);
      return k + 2 - paramInt;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return 0;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}