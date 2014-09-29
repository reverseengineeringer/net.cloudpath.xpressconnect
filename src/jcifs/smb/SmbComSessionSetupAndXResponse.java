package jcifs.smb;

import java.io.UnsupportedEncodingException;
import jcifs.util.LogStream;

class SmbComSessionSetupAndXResponse extends AndXServerMessageBlock
{
  boolean isLoggedInAsGuest;
  private String nativeLanMan = "";
  private String nativeOs = "";
  private String primaryDomain = "";

  SmbComSessionSetupAndXResponse(ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.nativeOs = readString(paramArrayOfByte, paramInt);
    int i = paramInt + stringWireLength(this.nativeOs, paramInt);
    this.nativeLanMan = readString(paramArrayOfByte, i);
    int j = i + stringWireLength(this.nativeLanMan, i);
    int m;
    if (this.useUnicode)
    {
      if ((j - this.headerStart) % 2 != 0)
        j++;
      m = 0;
      while (paramArrayOfByte[(j + m)] != 0)
      {
        m += 2;
        if (m > 256)
          throw new RuntimeException("zero termination not found");
      }
    }
    while (true)
    {
      try
      {
        this.primaryDomain = new String(paramArrayOfByte, j, m, "UnicodeLittle");
        k = j + m;
        return k - paramInt;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        if (LogStream.level <= 1)
          continue;
        localUnsupportedEncodingException.printStackTrace(log);
        continue;
      }
      this.primaryDomain = readString(paramArrayOfByte, j);
      int k = j + stringWireLength(this.primaryDomain, j);
    }
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 1;
    if ((0x1 & paramArrayOfByte[paramInt]) == i);
    while (true)
    {
      this.isLoggedInAsGuest = i;
      return 2;
      i = 0;
    }
  }

  public String toString()
  {
    return new String("SmbComSessionSetupAndXResponse[" + super.toString() + ",isLoggedInAsGuest=" + this.isLoggedInAsGuest + ",nativeOs=" + this.nativeOs + ",nativeLanMan=" + this.nativeLanMan + ",primaryDomain=" + this.primaryDomain + "]");
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