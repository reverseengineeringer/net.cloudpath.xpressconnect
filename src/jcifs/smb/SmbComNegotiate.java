package jcifs.smb;

import java.io.UnsupportedEncodingException;

class SmbComNegotiate extends ServerMessageBlock
{
  private static final String DIALECTS = "";

  SmbComNegotiate()
  {
    this.command = 114;
    this.flags2 = SmbConstants.DEFAULT_FLAGS2;
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
    return new String("SmbComNegotiate[" + super.toString() + ",wordCount=" + this.wordCount + ",dialects=NT LM 0.12]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    try
    {
      byte[] arrayOfByte = "".getBytes("ASCII");
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
      return arrayOfByte.length;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return 0;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}