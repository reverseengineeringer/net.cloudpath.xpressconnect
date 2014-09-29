package jcifs.smb;

import java.io.UnsupportedEncodingException;
import jcifs.Config;
import jcifs.util.Hexdump;

class SmbComTreeConnectAndX extends AndXServerMessageBlock
{
  private static final boolean DISABLE_PLAIN_TEXT_PASSWORDS = Config.getBoolean("jcifs.smb.client.disablePlainTextPasswords", true);
  private static byte[] batchLimits = { 1, 1, 1, 1, 1, 1, 1, 1, 0 };
  private boolean disconnectTid = false;
  private byte[] password;
  private int passwordLength;
  private String path;
  private String service;
  private SmbSession session;

  static
  {
    String str1 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.CheckDirectory");
    if (str1 != null)
      batchLimits[0] = Byte.parseByte(str1);
    String str2 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.CreateDirectory");
    if (str2 != null)
      batchLimits[2] = Byte.parseByte(str2);
    String str3 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.Delete");
    if (str3 != null)
      batchLimits[3] = Byte.parseByte(str3);
    String str4 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.DeleteDirectory");
    if (str4 != null)
      batchLimits[4] = Byte.parseByte(str4);
    String str5 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.OpenAndX");
    if (str5 != null)
      batchLimits[5] = Byte.parseByte(str5);
    String str6 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.Rename");
    if (str6 != null)
      batchLimits[6] = Byte.parseByte(str6);
    String str7 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.Transaction");
    if (str7 != null)
      batchLimits[7] = Byte.parseByte(str7);
    String str8 = Config.getProperty("jcifs.smb.client.TreeConnectAndX.QueryInformation");
    if (str8 != null)
      batchLimits[8] = Byte.parseByte(str8);
  }

  SmbComTreeConnectAndX(SmbSession paramSmbSession, String paramString1, String paramString2, ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
    this.session = paramSmbSession;
    this.path = paramString1;
    this.service = paramString2;
    this.command = 117;
  }

  int getBatchLimit(byte paramByte)
  {
    switch (paramByte & 0xFF)
    {
    default:
      return 0;
    case 16:
      return batchLimits[0];
    case 0:
      return batchLimits[2];
    case 6:
      return batchLimits[3];
    case 1:
      return batchLimits[4];
    case 45:
      return batchLimits[5];
    case 7:
      return batchLimits[6];
    case 37:
      return batchLimits[7];
    case 8:
    }
    return batchLimits[8];
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
    return new String("SmbComTreeConnectAndX[" + super.toString() + ",disconnectTid=" + this.disconnectTid + ",passwordLength=" + this.passwordLength + ",password=" + Hexdump.toHexString(this.password, this.passwordLength, 0) + ",path=" + this.path + ",service=" + this.service + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int j;
    if ((this.session.transport.server.security == 0) && ((this.session.auth.hashesExternal) || (this.session.auth.password.length() > 0)))
    {
      System.arraycopy(this.password, 0, paramArrayOfByte, paramInt, this.passwordLength);
      j = paramInt + this.passwordLength;
    }
    while (true)
    {
      int k = j + writeString(this.path, paramArrayOfByte, j);
      try
      {
        System.arraycopy(this.service.getBytes("ASCII"), 0, paramArrayOfByte, k, this.service.length());
        int m = k + this.service.length();
        int n = m + 1;
        paramArrayOfByte[m] = 0;
        return n - paramInt;
        int i = paramInt + 1;
        paramArrayOfByte[paramInt] = 0;
        j = i;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
      }
    }
    return 0;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 1;
    int j;
    if ((this.session.transport.server.security == 0) && ((this.session.auth.hashesExternal) || (this.session.auth.password.length() > 0)))
      if (this.session.transport.server.encryptedPasswords)
      {
        this.password = this.session.auth.getAnsiHash(this.session.transport.server.encryptionKey);
        this.passwordLength = this.password.length;
        j = paramInt + 1;
        if (!this.disconnectTid)
          break label212;
      }
    while (true)
    {
      paramArrayOfByte[paramInt] = i;
      int k = j + 1;
      paramArrayOfByte[j] = 0;
      writeInt2(this.passwordLength, paramArrayOfByte, k);
      return 4;
      if (DISABLE_PLAIN_TEXT_PASSWORDS)
        throw new RuntimeException("Plain text passwords are disabled");
      this.password = new byte[2 * (1 + this.session.auth.password.length())];
      this.passwordLength = writeString(this.session.auth.password, this.password, 0);
      break;
      this.passwordLength = i;
      break;
      label212: i = 0;
    }
  }
}