package jcifs.smb;

import java.security.MessageDigest;
import jcifs.util.Hexdump;
import jcifs.util.LogStream;

public class SigningDigest
  implements SmbConstants
{
  static LogStream log = LogStream.getInstance();
  private MessageDigest digest;
  private byte[] macSigningKey;
  private int signSequence;
  private int updates;

  // ERROR //
  public SigningDigest(SmbTransport paramSmbTransport, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws SmbException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 35	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: ldc 37
    //   7: invokestatic 42	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
    //   10: putfield 44	jcifs/smb/SigningDigest:digest	Ljava/security/MessageDigest;
    //   13: getstatic 47	jcifs/smb/SmbConstants:LM_COMPATIBILITY	I
    //   16: tableswitch	default:+40 -> 56, 0:+171->187, 1:+171->187, 2:+171->187, 3:+235->251, 4:+235->251, 5:+235->251
    //   57: bipush 40
    //   59: newarray byte
    //   61: putfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   64: aload_2
    //   65: aload_1
    //   66: getfield 55	jcifs/smb/SmbTransport:server	Ljcifs/smb/SmbTransport$ServerData;
    //   69: getfield 60	jcifs/smb/SmbTransport$ServerData:encryptionKey	[B
    //   72: aload_0
    //   73: getfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   76: iconst_0
    //   77: invokevirtual 66	jcifs/smb/NtlmPasswordAuthentication:getUserSessionKey	([B[BI)V
    //   80: aload_2
    //   81: aload_1
    //   82: getfield 55	jcifs/smb/SmbTransport:server	Ljcifs/smb/SmbTransport$ServerData;
    //   85: getfield 60	jcifs/smb/SmbTransport$ServerData:encryptionKey	[B
    //   88: invokevirtual 70	jcifs/smb/NtlmPasswordAuthentication:getUnicodeHash	([B)[B
    //   91: iconst_0
    //   92: aload_0
    //   93: getfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   96: bipush 16
    //   98: bipush 24
    //   100: invokestatic 76	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   103: getstatic 25	jcifs/smb/SigningDigest:log	Ljcifs/util/LogStream;
    //   106: pop
    //   107: getstatic 79	jcifs/util/LogStream:level	I
    //   110: iconst_5
    //   111: if_icmplt +46 -> 157
    //   114: getstatic 25	jcifs/smb/SigningDigest:log	Ljcifs/util/LogStream;
    //   117: new 81	java/lang/StringBuffer
    //   120: dup
    //   121: invokespecial 82	java/lang/StringBuffer:<init>	()V
    //   124: ldc 84
    //   126: invokevirtual 88	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   129: getstatic 47	jcifs/smb/SmbConstants:LM_COMPATIBILITY	I
    //   132: invokevirtual 91	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
    //   135: invokevirtual 95	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   138: invokevirtual 99	jcifs/util/LogStream:println	(Ljava/lang/String;)V
    //   141: getstatic 25	jcifs/smb/SigningDigest:log	Ljcifs/util/LogStream;
    //   144: aload_0
    //   145: getfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   148: iconst_0
    //   149: aload_0
    //   150: getfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   153: arraylength
    //   154: invokestatic 105	jcifs/util/Hexdump:hexdump	(Ljava/io/PrintStream;[BII)V
    //   157: return
    //   158: astore_3
    //   159: getstatic 25	jcifs/smb/SigningDigest:log	Ljcifs/util/LogStream;
    //   162: pop
    //   163: getstatic 79	jcifs/util/LogStream:level	I
    //   166: ifle +10 -> 176
    //   169: aload_3
    //   170: getstatic 25	jcifs/smb/SigningDigest:log	Ljcifs/util/LogStream;
    //   173: invokevirtual 109	java/security/NoSuchAlgorithmException:printStackTrace	(Ljava/io/PrintStream;)V
    //   176: new 29	jcifs/smb/SmbException
    //   179: dup
    //   180: ldc 37
    //   182: aload_3
    //   183: invokespecial 112	jcifs/smb/SmbException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   186: athrow
    //   187: aload_0
    //   188: bipush 40
    //   190: newarray byte
    //   192: putfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   195: aload_2
    //   196: aload_1
    //   197: getfield 55	jcifs/smb/SmbTransport:server	Ljcifs/smb/SmbTransport$ServerData;
    //   200: getfield 60	jcifs/smb/SmbTransport$ServerData:encryptionKey	[B
    //   203: aload_0
    //   204: getfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   207: iconst_0
    //   208: invokevirtual 66	jcifs/smb/NtlmPasswordAuthentication:getUserSessionKey	([B[BI)V
    //   211: aload_2
    //   212: aload_1
    //   213: getfield 55	jcifs/smb/SmbTransport:server	Ljcifs/smb/SmbTransport$ServerData;
    //   216: getfield 60	jcifs/smb/SmbTransport$ServerData:encryptionKey	[B
    //   219: invokevirtual 70	jcifs/smb/NtlmPasswordAuthentication:getUnicodeHash	([B)[B
    //   222: iconst_0
    //   223: aload_0
    //   224: getfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   227: bipush 16
    //   229: bipush 24
    //   231: invokestatic 76	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   234: goto -131 -> 103
    //   237: astore 5
    //   239: new 29	jcifs/smb/SmbException
    //   242: dup
    //   243: ldc 114
    //   245: aload 5
    //   247: invokespecial 112	jcifs/smb/SmbException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   250: athrow
    //   251: aload_0
    //   252: bipush 16
    //   254: newarray byte
    //   256: putfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   259: aload_2
    //   260: aload_1
    //   261: getfield 55	jcifs/smb/SmbTransport:server	Ljcifs/smb/SmbTransport$ServerData;
    //   264: getfield 60	jcifs/smb/SmbTransport$ServerData:encryptionKey	[B
    //   267: aload_0
    //   268: getfield 49	jcifs/smb/SigningDigest:macSigningKey	[B
    //   271: iconst_0
    //   272: invokevirtual 66	jcifs/smb/NtlmPasswordAuthentication:getUserSessionKey	([B[BI)V
    //   275: goto -172 -> 103
    //
    // Exception table:
    //   from	to	target	type
    //   4	13	158	java/security/NoSuchAlgorithmException
    //   13	56	237	java/lang/Exception
    //   56	103	237	java/lang/Exception
    //   187	234	237	java/lang/Exception
    //   251	275	237	java/lang/Exception
  }

  public byte[] digest()
  {
    byte[] arrayOfByte = this.digest.digest();
    if (LogStream.level >= 5)
    {
      log.println("digest: ");
      Hexdump.hexdump(log, arrayOfByte, 0, arrayOfByte.length);
      log.flush();
    }
    this.updates = 0;
    return arrayOfByte;
  }

  void sign(byte[] paramArrayOfByte, int paramInt1, int paramInt2, ServerMessageBlock paramServerMessageBlock1, ServerMessageBlock paramServerMessageBlock2)
  {
    paramServerMessageBlock1.signSeq = this.signSequence;
    if (paramServerMessageBlock2 != null)
    {
      paramServerMessageBlock2.signSeq = (1 + this.signSequence);
      paramServerMessageBlock2.verifyFailed = false;
    }
    try
    {
      update(this.macSigningKey, 0, this.macSigningKey.length);
      int i = paramInt1 + 14;
      for (int j = 0; j < 8; j++)
        paramArrayOfByte[(i + j)] = 0;
      ServerMessageBlock.writeInt4(this.signSequence, paramArrayOfByte, i);
      update(paramArrayOfByte, paramInt1, paramInt2);
      System.arraycopy(digest(), 0, paramArrayOfByte, i, 8);
      return;
    }
    catch (Exception localException)
    {
      if (LogStream.level > 0)
        localException.printStackTrace(log);
      return;
    }
    finally
    {
      this.signSequence = (2 + this.signSequence);
    }
  }

  public String toString()
  {
    return "LM_COMPATIBILITY=" + SmbConstants.LM_COMPATIBILITY + " MacSigningKey=" + Hexdump.toHexString(this.macSigningKey, 0, this.macSigningKey.length);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (LogStream.level >= 5)
    {
      log.println("update: " + this.updates + " " + paramInt1 + ":" + paramInt2);
      Hexdump.hexdump(log, paramArrayOfByte, paramInt1, Math.min(paramInt2, 256));
      log.flush();
    }
    if (paramInt2 == 0)
      return;
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
    this.updates = (1 + this.updates);
  }

  boolean verify(byte[] paramArrayOfByte, int paramInt, ServerMessageBlock paramServerMessageBlock)
  {
    update(this.macSigningKey, 0, this.macSigningKey.length);
    update(paramArrayOfByte, paramInt, 14);
    int i = paramInt + 14;
    byte[] arrayOfByte1 = new byte[8];
    ServerMessageBlock.writeInt4(paramServerMessageBlock.signSeq, arrayOfByte1, 0);
    update(arrayOfByte1, 0, arrayOfByte1.length);
    int j = i + 8;
    byte[] arrayOfByte2;
    if (paramServerMessageBlock.command == 46)
    {
      SmbComReadAndXResponse localSmbComReadAndXResponse = (SmbComReadAndXResponse)paramServerMessageBlock;
      update(paramArrayOfByte, j, -8 + (-14 + (paramServerMessageBlock.length - localSmbComReadAndXResponse.dataLength)));
      update(localSmbComReadAndXResponse.b, localSmbComReadAndXResponse.off, localSmbComReadAndXResponse.dataLength);
      arrayOfByte2 = digest();
    }
    for (int k = 0; ; k++)
    {
      if (k >= 8)
        break label228;
      if (arrayOfByte2[k] != paramArrayOfByte[(k + (paramInt + 14))])
      {
        if (LogStream.level >= 2)
        {
          log.println("signature verification failure");
          Hexdump.hexdump(log, arrayOfByte2, 0, 8);
          Hexdump.hexdump(log, paramArrayOfByte, paramInt + 14, 8);
        }
        paramServerMessageBlock.verifyFailed = true;
        return true;
        update(paramArrayOfByte, j, -8 + (-14 + paramServerMessageBlock.length));
        break;
      }
    }
    label228: paramServerMessageBlock.verifyFailed = false;
    return false;
  }
}