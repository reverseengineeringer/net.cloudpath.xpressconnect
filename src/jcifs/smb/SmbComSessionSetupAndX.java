package jcifs.smb;

import java.util.Arrays;
import jcifs.Config;

class SmbComSessionSetupAndX extends AndXServerMessageBlock
{
  private static final int BATCH_LIMIT = Config.getInt("jcifs.smb.client.SessionSetupAndX.TreeConnectAndX", 1);
  private static final boolean DISABLE_PLAIN_TEXT_PASSWORDS = Config.getBoolean("jcifs.smb.client.disablePlainTextPasswords", true);
  private String accountName;
  private byte[] accountPassword;
  NtlmPasswordAuthentication auth;
  private int passwordLength;
  private String primaryDomain;
  SmbSession session;
  private int sessionKey;
  private byte[] unicodePassword;
  private int unicodePasswordLength;

  SmbComSessionSetupAndX(SmbSession paramSmbSession, ServerMessageBlock paramServerMessageBlock)
    throws SmbException
  {
    super(paramServerMessageBlock);
    this.command = 115;
    this.session = paramSmbSession;
    this.auth = paramSmbSession.auth;
    if ((this.auth.hashesExternal) && (!Arrays.equals(this.auth.challenge, paramSmbSession.transport.server.encryptionKey)))
      throw new SmbAuthException(-1073741819);
  }

  int getBatchLimit(byte paramByte)
  {
    if (paramByte == 117)
      return BATCH_LIMIT;
    return 0;
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
    StringBuffer localStringBuffer1 = new StringBuffer().append("SmbComSessionSetupAndX[").append(super.toString()).append(",snd_buf_size=").append(this.session.transport.snd_buf_size).append(",maxMpxCount=").append(this.session.transport.maxMpxCount).append(",VC_NUMBER=");
    StringBuffer localStringBuffer2 = localStringBuffer1.append(1).append(",sessionKey=").append(this.sessionKey).append(",passwordLength=").append(this.passwordLength).append(",unicodePasswordLength=").append(this.unicodePasswordLength).append(",capabilities=").append(this.session.transport.capabilities).append(",accountName=").append(this.accountName).append(",primaryDomain=").append(this.primaryDomain).append(",NATIVE_OS=");
    StringBuffer localStringBuffer3 = localStringBuffer2.append(SmbConstants.NATIVE_OS).append(",NATIVE_LANMAN=");
    return new String(SmbConstants.NATIVE_LANMAN + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt;
    if (this.useUnicode);
    for (String str = this.auth.username; ; str = this.auth.username.toUpperCase())
    {
      this.accountName = str;
      this.primaryDomain = this.auth.domain.toUpperCase();
      if ((this.session.transport.server.security == 1) && ((this.auth.hashesExternal) || (this.auth.password.length() > 0)))
      {
        System.arraycopy(this.accountPassword, 0, paramArrayOfByte, paramInt, this.passwordLength);
        int n = paramInt + this.passwordLength;
        if ((!this.session.transport.server.encryptedPasswords) && (this.useUnicode) && ((n - this.headerStart) % 2 != 0))
        {
          int i1 = n + 1;
          paramArrayOfByte[n] = 0;
          n = i1;
        }
        System.arraycopy(this.unicodePassword, 0, paramArrayOfByte, n, this.unicodePasswordLength);
        paramInt = n + this.unicodePasswordLength;
      }
      int j = paramInt + writeString(this.accountName, paramArrayOfByte, paramInt);
      int k = j + writeString(this.primaryDomain, paramArrayOfByte, j);
      int m = k + writeString(SmbConstants.NATIVE_OS, paramArrayOfByte, k);
      return m + writeString(SmbConstants.NATIVE_LANMAN, paramArrayOfByte, m) - i;
    }
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    if ((this.session.transport.server.security == 1) && ((this.auth.hashesExternal) || (this.auth.password.length() > 0)))
      if (this.session.transport.server.encryptedPasswords)
      {
        this.accountPassword = this.auth.getAnsiHash(this.session.transport.server.encryptionKey);
        this.passwordLength = this.accountPassword.length;
        this.unicodePassword = this.auth.getUnicodeHash(this.session.transport.server.encryptionKey);
        this.unicodePasswordLength = this.unicodePassword.length;
        if ((this.unicodePasswordLength == 0) && (this.passwordLength == 0))
          throw new RuntimeException("Null setup prohibited.");
      }
      else
      {
        if (DISABLE_PLAIN_TEXT_PASSWORDS)
          throw new RuntimeException("Plain text passwords are disabled");
        if (!this.useUnicode)
          break label415;
        String str2 = this.auth.getPassword();
        this.accountPassword = new byte[0];
        this.passwordLength = 0;
        this.unicodePassword = new byte[2 * (1 + str2.length())];
        this.unicodePasswordLength = writeString(str2, this.unicodePassword, 0);
      }
    while (true)
    {
      this.sessionKey = this.session.transport.sessionKey;
      writeInt2(this.session.transport.snd_buf_size, paramArrayOfByte, paramInt);
      int i = paramInt + 2;
      writeInt2(this.session.transport.maxMpxCount, paramArrayOfByte, i);
      int j = i + 2;
      writeInt2(1L, paramArrayOfByte, j);
      int k = j + 2;
      writeInt4(this.sessionKey, paramArrayOfByte, k);
      int m = k + 4;
      writeInt2(this.passwordLength, paramArrayOfByte, m);
      int n = m + 2;
      writeInt2(this.unicodePasswordLength, paramArrayOfByte, n);
      int i1 = n + 2;
      int i2 = i1 + 1;
      paramArrayOfByte[i1] = 0;
      int i3 = i2 + 1;
      paramArrayOfByte[i2] = 0;
      int i4 = i3 + 1;
      paramArrayOfByte[i3] = 0;
      int i5 = i4 + 1;
      paramArrayOfByte[i4] = 0;
      writeInt4(this.session.transport.capabilities, paramArrayOfByte, i5);
      return i5 + 4 - paramInt;
      label415: String str1 = this.auth.getPassword();
      this.accountPassword = new byte[2 * (1 + str1.length())];
      this.passwordLength = writeString(str1, this.accountPassword, 0);
      this.unicodePassword = new byte[0];
      this.unicodePasswordLength = 0;
      continue;
      this.unicodePasswordLength = 0;
      this.passwordLength = 0;
    }
  }
}