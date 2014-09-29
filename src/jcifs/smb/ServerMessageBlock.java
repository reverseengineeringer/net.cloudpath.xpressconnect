package jcifs.smb;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.TimeZone;
import jcifs.util.Hexdump;
import jcifs.util.LogStream;
import jcifs.util.transport.Request;
import jcifs.util.transport.Response;

abstract class ServerMessageBlock extends Response
  implements Request, SmbConstants
{
  static final byte SMB_COM_CHECK_DIRECTORY = 16;
  static final byte SMB_COM_CLOSE = 4;
  static final byte SMB_COM_CREATE_DIRECTORY = 0;
  static final byte SMB_COM_DELETE = 6;
  static final byte SMB_COM_DELETE_DIRECTORY = 1;
  static final byte SMB_COM_ECHO = 43;
  static final byte SMB_COM_FIND_CLOSE2 = 52;
  static final byte SMB_COM_LOGOFF_ANDX = 116;
  static final byte SMB_COM_MOVE = 42;
  static final byte SMB_COM_NEGOTIATE = 114;
  static final byte SMB_COM_NT_CREATE_ANDX = -94;
  static final byte SMB_COM_NT_TRANSACT = -96;
  static final byte SMB_COM_NT_TRANSACT_SECONDARY = -95;
  static final byte SMB_COM_OPEN_ANDX = 45;
  static final byte SMB_COM_QUERY_INFORMATION = 8;
  static final byte SMB_COM_READ_ANDX = 46;
  static final byte SMB_COM_RENAME = 7;
  static final byte SMB_COM_SESSION_SETUP_ANDX = 115;
  static final byte SMB_COM_TRANSACTION = 37;
  static final byte SMB_COM_TRANSACTION2 = 50;
  static final byte SMB_COM_TRANSACTION_SECONDARY = 38;
  static final byte SMB_COM_TREE_CONNECT_ANDX = 117;
  static final byte SMB_COM_TREE_DISCONNECT = 113;
  static final byte SMB_COM_WRITE = 11;
  static final byte SMB_COM_WRITE_ANDX = 47;
  static final byte[] header = { -1, 83, 77, 66, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  static LogStream log = LogStream.getInstance();
  NtlmPasswordAuthentication auth = null;
  int batchLevel = 0;
  int byteCount;
  byte command;
  SigningDigest digest = null;
  int errorCode;
  byte flags = 24;
  int flags2;
  int headerStart;
  int length;
  int mid;
  String path;
  int pid = SmbConstants.PID;
  boolean received;
  ServerMessageBlock response;
  long responseTimeout = 1L;
  int signSeq;
  int tid;
  int uid;
  boolean useUnicode;
  boolean verifyFailed;
  int wordCount;

  static int readInt2(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFF & paramArrayOfByte[paramInt]) + ((0xFF & paramArrayOfByte[(paramInt + 1)]) << 8);
  }

  static int readInt4(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFF & paramArrayOfByte[paramInt]) + ((0xFF & paramArrayOfByte[(paramInt + 1)]) << 8) + ((0xFF & paramArrayOfByte[(paramInt + 2)]) << 16) + ((0xFF & paramArrayOfByte[(paramInt + 3)]) << 24);
  }

  static long readInt8(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFFFFFFFF & readInt4(paramArrayOfByte, paramInt)) + (readInt4(paramArrayOfByte, paramInt + 4) << 32);
  }

  static long readTime(byte[] paramArrayOfByte, int paramInt)
  {
    int i = readInt4(paramArrayOfByte, paramInt);
    return (readInt4(paramArrayOfByte, paramInt + 4) << 32 | 0xFFFFFFFF & i) / 10000L - 11644473600000L;
  }

  static long readUTime(byte[] paramArrayOfByte, int paramInt)
  {
    return 1000L * readInt4(paramArrayOfByte, paramInt);
  }

  static void writeInt2(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)(int)paramLong);
    paramArrayOfByte[(paramInt + 1)] = ((byte)(int)(paramLong >> 8));
  }

  static void writeInt4(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)(int)paramLong);
    int i = paramInt + 1;
    long l1 = paramLong >> 8;
    paramArrayOfByte[i] = ((byte)(int)l1);
    int j = i + 1;
    long l2 = l1 >> 8;
    paramArrayOfByte[j] = ((byte)(int)l2);
    paramArrayOfByte[(j + 1)] = ((byte)(int)(l2 >> 8));
  }

  static void writeInt8(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)(int)paramLong);
    int i = paramInt + 1;
    long l1 = paramLong >> 8;
    paramArrayOfByte[i] = ((byte)(int)l1);
    int j = i + 1;
    long l2 = l1 >> 8;
    paramArrayOfByte[j] = ((byte)(int)l2);
    int k = j + 1;
    long l3 = l2 >> 8;
    paramArrayOfByte[k] = ((byte)(int)l3);
    int m = k + 1;
    long l4 = l3 >> 8;
    paramArrayOfByte[m] = ((byte)(int)l4);
    int n = m + 1;
    long l5 = l4 >> 8;
    paramArrayOfByte[n] = ((byte)(int)l5);
    int i1 = n + 1;
    long l6 = l5 >> 8;
    paramArrayOfByte[i1] = ((byte)(int)l6);
    paramArrayOfByte[(i1 + 1)] = ((byte)(int)(l6 >> 8));
  }

  static void writeTime(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    if (paramLong != 0L)
      paramLong = 10000L * (11644473600000L + paramLong);
    writeInt8(paramLong, paramArrayOfByte, paramInt);
  }

  static void writeUTime(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    if ((paramLong == 0L) || (paramLong == -1L))
    {
      writeInt4(-1L, paramArrayOfByte, paramInt);
      return;
    }
    synchronized (SmbConstants.TZ)
    {
      if (SmbConstants.TZ.inDaylightTime(new Date()))
        if (!SmbConstants.TZ.inDaylightTime(new Date(paramLong)));
      while (!SmbConstants.TZ.inDaylightTime(new Date(paramLong)))
        while (true)
        {
          writeInt4((int)(paramLong / 1000L), paramArrayOfByte, paramInt);
          return;
          paramLong -= 3600000L;
        }
      paramLong += 3600000L;
    }
  }

  int decode(byte[] paramArrayOfByte, int paramInt)
  {
    this.headerStart = paramInt;
    int i = paramInt + readHeaderWireFormat(paramArrayOfByte, paramInt);
    int j = i + 1;
    this.wordCount = paramArrayOfByte[i];
    if (this.wordCount != 0)
    {
      int i1 = readParameterWordsWireFormat(paramArrayOfByte, j);
      if ((i1 != 2 * this.wordCount) && (LogStream.level >= 5))
        log.println("wordCount * 2=" + 2 * this.wordCount + " but readParameterWordsWireFormat returned " + i1);
    }
    for (int k = j + 2 * this.wordCount; ; k = j)
    {
      this.byteCount = readInt2(paramArrayOfByte, k);
      int m = k + 2;
      if (this.byteCount != 0)
      {
        int n = readBytesWireFormat(paramArrayOfByte, m);
        if ((n != this.byteCount) && (LogStream.level >= 5))
          log.println("byteCount=" + this.byteCount + " but readBytesWireFormat returned " + n);
        m += this.byteCount;
      }
      this.length = (m - paramInt);
      return this.length;
    }
  }

  int encode(byte[] paramArrayOfByte, int paramInt)
  {
    this.headerStart = paramInt;
    int i = paramInt + writeHeaderWireFormat(paramArrayOfByte, paramInt);
    this.wordCount = writeParameterWordsWireFormat(paramArrayOfByte, i + 1);
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(0xFF & this.wordCount / 2));
    int k = j + this.wordCount;
    this.wordCount /= 2;
    this.byteCount = writeBytesWireFormat(paramArrayOfByte, k + 2);
    int m = k + 1;
    paramArrayOfByte[k] = ((byte)(0xFF & this.byteCount));
    int n = m + 1;
    paramArrayOfByte[m] = ((byte)(0xFF & this.byteCount >> 8));
    this.length = (n + this.byteCount - paramInt);
    if (this.digest != null)
      this.digest.sign(paramArrayOfByte, this.headerStart, this.length, this, this.response);
    return this.length;
  }

  public boolean equals(Object paramObject)
  {
    return ((paramObject instanceof ServerMessageBlock)) && (((ServerMessageBlock)paramObject).mid == this.mid);
  }

  public int hashCode()
  {
    return this.mid;
  }

  boolean isResponse()
  {
    return (0x80 & this.flags) == 128;
  }

  abstract int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt);

  int readHeaderWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.command = paramArrayOfByte[(paramInt + 4)];
    this.errorCode = readInt4(paramArrayOfByte, paramInt + 5);
    this.flags = paramArrayOfByte[(paramInt + 9)];
    this.flags2 = readInt2(paramArrayOfByte, 1 + (paramInt + 9));
    this.tid = readInt2(paramArrayOfByte, paramInt + 24);
    this.pid = readInt2(paramArrayOfByte, 2 + (paramInt + 24));
    this.uid = readInt2(paramArrayOfByte, 4 + (paramInt + 24));
    this.mid = readInt2(paramArrayOfByte, 6 + (paramInt + 24));
    return 32;
  }

  abstract int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt);

  String readString(byte[] paramArrayOfByte, int paramInt)
  {
    return readString(paramArrayOfByte, paramInt, 256, this.useUnicode);
  }

  String readString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = 128;
    int j = 0;
    if (paramBoolean)
    {
      int m;
      try
      {
        int k = (paramInt1 - this.headerStart) % 2;
        m = 0;
        if (k != 0)
          paramInt1++;
        while ((paramArrayOfByte[(paramInt1 + m)] != 0) || (paramArrayOfByte[(1 + (paramInt1 + m))] != 0))
        {
          m += 2;
          if (m > paramInt2)
          {
            if (LogStream.level > 0)
            {
              PrintStream localPrintStream2 = System.err;
              if (paramInt2 < i)
                i = paramInt2 + 8;
              Hexdump.hexdump(localPrintStream2, paramArrayOfByte, paramInt1, i);
            }
            throw new RuntimeException("zero termination not found");
          }
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        if (LogStream.level > 1)
          localUnsupportedEncodingException.printStackTrace(log);
        return null;
      }
      return new String(paramArrayOfByte, paramInt1, m, "UnicodeLittleUnmarked");
    }
    while (paramArrayOfByte[(paramInt1 + j)] != 0)
    {
      j++;
      if (j > paramInt2)
      {
        if (LogStream.level > 0)
        {
          PrintStream localPrintStream1 = System.err;
          if (paramInt2 < i)
            i = paramInt2 + 8;
          Hexdump.hexdump(localPrintStream1, paramArrayOfByte, paramInt1, i);
        }
        throw new RuntimeException("zero termination not found");
      }
    }
    String str = new String(paramArrayOfByte, paramInt1, j, SmbConstants.OEM_ENCODING);
    return str;
  }

  int readStringLength(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int j;
    for (int i = 0; ; i = j)
      if (paramArrayOfByte[(paramInt1 + i)] != 0)
      {
        j = i + 1;
        if (i > paramInt2)
          throw new RuntimeException("zero termination not found: " + this);
      }
      else
      {
        return i;
      }
  }

  void reset()
  {
    this.flags = 24;
    this.flags2 = 0;
    this.errorCode = 0;
    this.received = false;
    this.digest = null;
  }

  int stringWireLength(String paramString, int paramInt)
  {
    int i = 1 + paramString.length();
    if (this.useUnicode)
    {
      i = 2 + 2 * paramString.length();
      if (paramInt % 2 != 0)
        i++;
    }
    return i;
  }

  public String toString()
  {
    String str1;
    switch (this.command)
    {
    default:
      str1 = "UNKNOWN";
      if (this.errorCode != 0)
        break;
    case 114:
    case 115:
    case 117:
    case 8:
    case 16:
    case 37:
    case 50:
    case 38:
    case 52:
    case 113:
    case 116:
    case 43:
    case 42:
    case 7:
    case 6:
    case 1:
    case -94:
    case 45:
    case 46:
    case 4:
    case 47:
    case 0:
    case -96:
    case -95:
    }
    for (String str2 = "0"; ; str2 = SmbException.getMessageByCode(this.errorCode))
    {
      return new String("command=" + str1 + ",received=" + this.received + ",errorCode=" + str2 + ",flags=0x" + Hexdump.toHexString(0xFF & this.flags, 4) + ",flags2=0x" + Hexdump.toHexString(this.flags2, 4) + ",signSeq=" + this.signSeq + ",tid=" + this.tid + ",pid=" + this.pid + ",uid=" + this.uid + ",mid=" + this.mid + ",wordCount=" + this.wordCount + ",byteCount=" + this.byteCount);
      str1 = "SMB_COM_NEGOTIATE";
      break;
      str1 = "SMB_COM_SESSION_SETUP_ANDX";
      break;
      str1 = "SMB_COM_TREE_CONNECT_ANDX";
      break;
      str1 = "SMB_COM_QUERY_INFORMATION";
      break;
      str1 = "SMB_COM_CHECK_DIRECTORY";
      break;
      str1 = "SMB_COM_TRANSACTION";
      break;
      str1 = "SMB_COM_TRANSACTION2";
      break;
      str1 = "SMB_COM_TRANSACTION_SECONDARY";
      break;
      str1 = "SMB_COM_FIND_CLOSE2";
      break;
      str1 = "SMB_COM_TREE_DISCONNECT";
      break;
      str1 = "SMB_COM_LOGOFF_ANDX";
      break;
      str1 = "SMB_COM_ECHO";
      break;
      str1 = "SMB_COM_MOVE";
      break;
      str1 = "SMB_COM_RENAME";
      break;
      str1 = "SMB_COM_DELETE";
      break;
      str1 = "SMB_COM_DELETE_DIRECTORY";
      break;
      str1 = "SMB_COM_NT_CREATE_ANDX";
      break;
      str1 = "SMB_COM_OPEN_ANDX";
      break;
      str1 = "SMB_COM_READ_ANDX";
      break;
      str1 = "SMB_COM_CLOSE";
      break;
      str1 = "SMB_COM_WRITE_ANDX";
      break;
      str1 = "SMB_COM_CREATE_DIRECTORY";
      break;
      str1 = "SMB_COM_NT_TRANSACT";
      break;
      str1 = "SMB_COM_NT_TRANSACT_SECONDARY";
      break;
    }
  }

  abstract int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt);

  int writeHeaderWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    System.arraycopy(header, 0, paramArrayOfByte, paramInt, header.length);
    paramArrayOfByte[(paramInt + 4)] = this.command;
    paramArrayOfByte[(paramInt + 9)] = this.flags;
    writeInt2(this.flags2, paramArrayOfByte, 1 + (paramInt + 9));
    int i = paramInt + 24;
    writeInt2(this.tid, paramArrayOfByte, i);
    writeInt2(this.pid, paramArrayOfByte, i + 2);
    writeInt2(this.uid, paramArrayOfByte, i + 4);
    writeInt2(this.mid, paramArrayOfByte, i + 6);
    return 32;
  }

  abstract int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt);

  int writeString(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    return writeString(paramString, paramArrayOfByte, paramInt, this.useUnicode);
  }

  // ERROR //
  int writeString(String paramString, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    // Byte code:
    //   0: iload_3
    //   1: istore 5
    //   3: iload 4
    //   5: ifeq +87 -> 92
    //   8: iload_3
    //   9: aload_0
    //   10: getfield 176	jcifs/smb/ServerMessageBlock:headerStart	I
    //   13: isub
    //   14: iconst_2
    //   15: irem
    //   16: istore 12
    //   18: iload 12
    //   20: ifeq +15 -> 35
    //   23: iload_3
    //   24: iconst_1
    //   25: iadd
    //   26: istore 11
    //   28: aload_2
    //   29: iload_3
    //   30: iconst_0
    //   31: bastore
    //   32: iload 11
    //   34: istore_3
    //   35: aload_1
    //   36: ldc_w 293
    //   39: invokevirtual 396	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   42: iconst_0
    //   43: aload_2
    //   44: iload_3
    //   45: iconst_2
    //   46: aload_1
    //   47: invokevirtual 313	java/lang/String:length	()I
    //   50: imul
    //   51: invokestatic 385	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   54: aload_1
    //   55: invokevirtual 313	java/lang/String:length	()I
    //   58: istore 13
    //   60: iload_3
    //   61: iload 13
    //   63: iconst_2
    //   64: imul
    //   65: iadd
    //   66: istore 14
    //   68: iload 14
    //   70: iconst_1
    //   71: iadd
    //   72: istore 11
    //   74: aload_2
    //   75: iload 14
    //   77: iconst_0
    //   78: bastore
    //   79: iload 11
    //   81: iconst_1
    //   82: iadd
    //   83: istore_3
    //   84: aload_2
    //   85: iload 11
    //   87: iconst_0
    //   88: bastore
    //   89: goto +83 -> 172
    //   92: aload_1
    //   93: getstatic 299	jcifs/smb/SmbConstants:OEM_ENCODING	Ljava/lang/String;
    //   96: invokevirtual 396	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   99: astore 8
    //   101: aload 8
    //   103: iconst_0
    //   104: aload_2
    //   105: iload_3
    //   106: aload 8
    //   108: arraylength
    //   109: invokestatic 385	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   112: aload 8
    //   114: arraylength
    //   115: istore 9
    //   117: iload_3
    //   118: iload 9
    //   120: iadd
    //   121: istore 10
    //   123: iload 10
    //   125: iconst_1
    //   126: iadd
    //   127: istore 11
    //   129: aload_2
    //   130: iload 10
    //   132: iconst_0
    //   133: bastore
    //   134: iload 11
    //   136: istore_3
    //   137: goto +35 -> 172
    //   140: astore 6
    //   142: getstatic 102	jcifs/smb/ServerMessageBlock:log	Ljcifs/util/LogStream;
    //   145: pop
    //   146: getstatic 187	jcifs/util/LogStream:level	I
    //   149: iconst_1
    //   150: if_icmple +22 -> 172
    //   153: aload 6
    //   155: getstatic 102	jcifs/smb/ServerMessageBlock:log	Ljcifs/util/LogStream;
    //   158: invokevirtual 289	java/io/UnsupportedEncodingException:printStackTrace	(Ljava/io/PrintStream;)V
    //   161: goto +11 -> 172
    //   164: astore 6
    //   166: iload 11
    //   168: istore_3
    //   169: goto -27 -> 142
    //   172: iload_3
    //   173: iload 5
    //   175: isub
    //   176: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   8	18	140	java/io/UnsupportedEncodingException
    //   35	60	140	java/io/UnsupportedEncodingException
    //   84	89	140	java/io/UnsupportedEncodingException
    //   92	117	140	java/io/UnsupportedEncodingException
    //   28	32	164	java/io/UnsupportedEncodingException
    //   74	79	164	java/io/UnsupportedEncodingException
    //   129	134	164	java/io/UnsupportedEncodingException
  }
}