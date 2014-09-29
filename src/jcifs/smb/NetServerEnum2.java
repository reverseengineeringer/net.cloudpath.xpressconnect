package jcifs.smb;

import java.io.UnsupportedEncodingException;

class NetServerEnum2 extends SmbComTransaction
{
  static final String[] DESCR = { "", "" };
  static final int SV_TYPE_ALL = -1;
  static final int SV_TYPE_DOMAIN_ENUM = -2147483648;
  String domain;
  String lastName = null;
  int serverTypes;

  NetServerEnum2(String paramString, int paramInt)
  {
    this.domain = paramString;
    this.serverTypes = paramInt;
    this.command = 37;
    this.subCommand = 104;
    this.name = "\\PIPE\\LANMAN";
    this.maxParameterCount = 8;
    this.maxDataCount = 16384;
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

  void reset(int paramInt, String paramString)
  {
    super.reset();
    this.lastName = paramString;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer().append("NetServerEnum2[").append(super.toString()).append(",name=").append(this.name).append(",serverTypes=");
    if (this.serverTypes == -1);
    for (String str = "SV_TYPE_ALL"; ; str = "SV_TYPE_DOMAIN_ENUM")
      return new String(str + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i;
    if (this.subCommand == 104)
      i = 0;
    try
    {
      while (true)
      {
        byte[] arrayOfByte = DESCR[i].getBytes("ASCII");
        writeInt2(0xFF & this.subCommand, paramArrayOfByte, paramInt);
        int j = paramInt + 2;
        System.arraycopy(arrayOfByte, 0, paramArrayOfByte, j, arrayOfByte.length);
        int k = j + arrayOfByte.length;
        writeInt2(1L, paramArrayOfByte, k);
        int m = k + 2;
        writeInt2(this.maxDataCount, paramArrayOfByte, m);
        int n = m + 2;
        writeInt4(this.serverTypes, paramArrayOfByte, n);
        int i1 = n + 4;
        int i2 = i1 + writeString(this.domain.toUpperCase(), paramArrayOfByte, i1, false);
        if (i == 1)
          i2 += writeString(this.lastName.toUpperCase(), paramArrayOfByte, i2, false);
        return i2 - paramInt;
        i = 1;
      }
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