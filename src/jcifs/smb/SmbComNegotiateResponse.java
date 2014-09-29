package jcifs.smb;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import jcifs.util.Hexdump;
import jcifs.util.LogStream;

class SmbComNegotiateResponse extends ServerMessageBlock
{
  int dialectIndex;
  SmbTransport.ServerData server;

  SmbComNegotiateResponse(SmbTransport.ServerData paramServerData)
  {
    this.server = paramServerData;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.server.encryptionKey = new byte[this.server.encryptionKeyLength];
    System.arraycopy(paramArrayOfByte, paramInt, this.server.encryptionKey, 0, this.server.encryptionKeyLength);
    int i = paramInt + this.server.encryptionKeyLength;
    int j;
    if (this.byteCount > this.server.encryptionKeyLength)
    {
      j = 0;
      try
      {
        int k = 0x8000 & this.flags2;
        j = 0;
        if (k != 32768)
          break label176;
        do
        {
          if ((paramArrayOfByte[(i + j)] == 0) && (paramArrayOfByte[(1 + (i + j))] == 0))
            break;
          j += 2;
        }
        while (j <= 256);
        throw new RuntimeException("zero termination not found");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        if (LogStream.level > 1)
          localUnsupportedEncodingException.printStackTrace(log);
      }
      i += j;
    }
    while (true)
    {
      return i - paramInt;
      this.server.oemDomainName = new String(paramArrayOfByte, i, j, "UnicodeLittleUnmarked");
      break;
      label176: 
      while (paramArrayOfByte[(i + j)] != 0)
      {
        j++;
        if (j > 256)
          throw new RuntimeException("zero termination not found");
      }
      this.server.oemDomainName = new String(paramArrayOfByte, i, j, SmbConstants.OEM_ENCODING);
      break;
      this.server.oemDomainName = new String();
    }
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    boolean bool1 = true;
    this.dialectIndex = readInt2(paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    if (this.dialectIndex > 10)
      return i - paramInt;
    SmbTransport.ServerData localServerData1 = this.server;
    int j = i + 1;
    localServerData1.securityMode = (0xFF & paramArrayOfByte[i]);
    this.server.security = (0x1 & this.server.securityMode);
    SmbTransport.ServerData localServerData2 = this.server;
    boolean bool2;
    boolean bool3;
    label122: SmbTransport.ServerData localServerData4;
    if ((0x2 & this.server.securityMode) == 2)
    {
      bool2 = bool1;
      localServerData2.encryptedPasswords = bool2;
      SmbTransport.ServerData localServerData3 = this.server;
      if ((0x4 & this.server.securityMode) != 4)
        break label345;
      bool3 = bool1;
      localServerData3.signaturesEnabled = bool3;
      localServerData4 = this.server;
      if ((0x8 & this.server.securityMode) != 8)
        break label351;
    }
    while (true)
    {
      localServerData4.signaturesRequired = bool1;
      this.server.maxMpxCount = readInt2(paramArrayOfByte, j);
      int k = j + 2;
      this.server.maxNumberVcs = readInt2(paramArrayOfByte, k);
      int m = k + 2;
      this.server.maxBufferSize = readInt4(paramArrayOfByte, m);
      int n = m + 4;
      this.server.maxRawSize = readInt4(paramArrayOfByte, n);
      int i1 = n + 4;
      this.server.sessionKey = readInt4(paramArrayOfByte, i1);
      int i2 = i1 + 4;
      this.server.capabilities = readInt4(paramArrayOfByte, i2);
      int i3 = i2 + 4;
      this.server.serverTime = readTime(paramArrayOfByte, i3);
      int i4 = i3 + 8;
      this.server.serverTimeZone = readInt2(paramArrayOfByte, i4);
      int i5 = i4 + 2;
      SmbTransport.ServerData localServerData5 = this.server;
      int i6 = i5 + 1;
      localServerData5.encryptionKeyLength = (0xFF & paramArrayOfByte[i5]);
      return i6 - paramInt;
      bool2 = false;
      break;
      label345: bool3 = false;
      break label122;
      label351: bool1 = false;
    }
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer().append("SmbComNegotiateResponse[").append(super.toString()).append(",wordCount=").append(this.wordCount).append(",dialectIndex=").append(this.dialectIndex).append(",securityMode=0x").append(Hexdump.toHexString(this.server.securityMode, 1)).append(",security=");
    if (this.server.security == 0);
    for (String str = "share"; ; str = "user")
      return new String(str + ",encryptedPasswords=" + this.server.encryptedPasswords + ",maxMpxCount=" + this.server.maxMpxCount + ",maxNumberVcs=" + this.server.maxNumberVcs + ",maxBufferSize=" + this.server.maxBufferSize + ",maxRawSize=" + this.server.maxRawSize + ",sessionKey=0x" + Hexdump.toHexString(this.server.sessionKey, 8) + ",capabilities=0x" + Hexdump.toHexString(this.server.capabilities, 8) + ",serverTime=" + new Date(this.server.serverTime) + ",serverTimeZone=" + this.server.serverTimeZone + ",encryptionKeyLength=" + this.server.encryptionKeyLength + ",byteCount=" + this.byteCount + ",encryptionKey=0x" + Hexdump.toHexString(this.server.encryptionKey, 0, 2 * this.server.encryptionKeyLength) + ",oemDomainName=" + this.server.oemDomainName + "]");
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