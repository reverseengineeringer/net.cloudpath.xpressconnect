package jcifs.smb;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.TimeZone;
import jcifs.Config;

abstract interface SmbConstants
{
  public static final int ATTR_ARCHIVE = 32;
  public static final int ATTR_COMPRESSED = 2048;
  public static final int ATTR_DIRECTORY = 16;
  public static final int ATTR_HIDDEN = 2;
  public static final int ATTR_NORMAL = 128;
  public static final int ATTR_READONLY = 1;
  public static final int ATTR_SYSTEM = 4;
  public static final int ATTR_TEMPORARY = 256;
  public static final int ATTR_VOLUME = 8;
  public static final int CAPABILITIES = 0;
  public static final int CAP_DFS = 4096;
  public static final int CAP_LARGE_FILES = 8;
  public static final int CAP_LEVEL_II_OPLOCKS = 128;
  public static final int CAP_LOCK_AND_READ = 256;
  public static final int CAP_MPX_MODE = 2;
  public static final int CAP_NONE = 0;
  public static final int CAP_NT_FIND = 512;
  public static final int CAP_NT_SMBS = 16;
  public static final int CAP_RAW_MODE = 1;
  public static final int CAP_RPC_REMOTE_APIS = 32;
  public static final int CAP_STATUS32 = 64;
  public static final int CAP_UNICODE = 4;
  public static final int CMD_OFFSET = 4;
  public static final LinkedList CONNECTIONS;
  public static final int DEFAULT_CAPABILITIES = 0;
  public static final int DEFAULT_FLAGS2 = 0;
  public static final int DEFAULT_MAX_MPX_COUNT = 10;
  public static final int DEFAULT_PORT = 445;
  public static final int DEFAULT_RCV_BUF_SIZE = 60416;
  public static final int DEFAULT_RESPONSE_TIMEOUT = 30000;
  public static final int DEFAULT_SND_BUF_SIZE = 16644;
  public static final int DEFAULT_SO_TIMEOUT = 35000;
  public static final int DEFAULT_SSN_LIMIT = 250;
  public static final int DELETE = 65536;
  public static final int ERROR_CODE_OFFSET = 5;
  public static final int FILE_APPEND_DATA = 4;
  public static final int FILE_DELETE = 64;
  public static final int FILE_EXECUTE = 32;
  public static final int FILE_READ_ATTRIBUTES = 128;
  public static final int FILE_READ_DATA = 1;
  public static final int FILE_READ_EA = 8;
  public static final int FILE_WRITE_ATTRIBUTES = 256;
  public static final int FILE_WRITE_DATA = 2;
  public static final int FILE_WRITE_EA = 16;
  public static final int FLAGS2 = 0;
  public static final int FLAGS2_EXTENDED_ATTRIBUTES = 2;
  public static final int FLAGS2_EXTENDED_SECURITY_NEGOTIATION = 2048;
  public static final int FLAGS2_LONG_FILENAMES = 1;
  public static final int FLAGS2_NONE = 0;
  public static final int FLAGS2_PERMIT_READ_IF_EXECUTE_PERM = 8192;
  public static final int FLAGS2_RESOLVE_PATHS_IN_DFS = 4096;
  public static final int FLAGS2_SECURITY_SIGNATURES = 4;
  public static final int FLAGS2_STATUS32 = 16384;
  public static final int FLAGS2_UNICODE = 32768;
  public static final int FLAGS_COPY_SOURCE_MODE_ASCII = 8;
  public static final int FLAGS_COPY_TARGET_MODE_ASCII = 4;
  public static final int FLAGS_LOCK_AND_READ_WRITE_AND_UNLOCK = 1;
  public static final int FLAGS_NONE = 0;
  public static final int FLAGS_NOTIFY_OF_MODIFY_ACTION = 64;
  public static final int FLAGS_OFFSET = 9;
  public static final int FLAGS_OPLOCK_REQUESTED_OR_GRANTED = 32;
  public static final int FLAGS_PATH_NAMES_CANONICALIZED = 16;
  public static final int FLAGS_PATH_NAMES_CASELESS = 8;
  public static final int FLAGS_RECEIVE_BUFFER_POSTED = 2;
  public static final int FLAGS_RESPONSE = 128;
  public static final int FLAGS_TARGET_MUST_BE_DIRECTORY = 2;
  public static final int FLAGS_TARGET_MUST_BE_FILE = 1;
  public static final int FLAGS_TREE_COPY = 32;
  public static final int FLAGS_VERIFY_ALL_WRITES = 16;
  public static final boolean FORCE_UNICODE = false;
  public static final int GENERIC_ALL = 268435456;
  public static final int GENERIC_EXECUTE = 536870912;
  public static final int GENERIC_READ = -2147483648;
  public static final int GENERIC_WRITE = 1073741824;
  public static final int HEADER_LENGTH = 32;
  public static final InetAddress LADDR;
  public static final int LM_COMPATIBILITY = 0;
  public static final int LPORT = 0;
  public static final int MAX_MPX_COUNT = 0;
  public static final long MILLISECONDS_BETWEEN_1970_AND_1601 = 11644473600000L;
  public static final String NATIVE_LANMAN;
  public static final String NATIVE_OS;
  public static final String NETBIOS_HOSTNAME;
  public static final SmbTransport NULL_TRANSPORT;
  public static final String OEM_ENCODING;
  public static final int OPEN_FUNCTION_FAIL_IF_EXISTS = 0;
  public static final int OPEN_FUNCTION_OVERWRITE_IF_EXISTS = 32;
  public static final int PID = 0;
  public static final int RCV_BUF_SIZE = 0;
  public static final int READ_CONTROL = 131072;
  public static final int RESPONSE_TIMEOUT = 0;
  public static final int SECURITY_SHARE = 0;
  public static final int SECURITY_USER = 1;
  public static final int SIGNATURE_OFFSET = 14;
  public static final boolean SIGNPREF = false;
  public static final int SND_BUF_SIZE = 0;
  public static final int SO_TIMEOUT = 0;
  public static final int SSN_LIMIT = 0;
  public static final int SYNCHRONIZE = 1048576;
  public static final boolean TCP_NODELAY = false;
  public static final int TID_OFFSET = 24;
  public static final TimeZone TZ;
  public static final boolean USE_BATCHING = false;
  public static final boolean USE_EXTSEC = false;
  public static final boolean USE_NTSMBS = false;
  public static final boolean USE_NTSTATUS = false;
  public static final boolean USE_UNICODE = false;
  public static final int VC_NUMBER = 1;
  public static final int WRITE_DAC = 262144;
  public static final int WRITE_OWNER = 524288;

  static
  {
    int i = 4;
    LADDR = Config.getLocalHost();
    LPORT = Config.getInt("jcifs.smb.client.lport", 0);
    MAX_MPX_COUNT = Config.getInt("jcifs.smb.client.maxMpxCount", 10);
    SND_BUF_SIZE = Config.getInt("jcifs.smb.client.snd_buf_size", 16644);
    RCV_BUF_SIZE = Config.getInt("jcifs.smb.client.rcv_buf_size", 60416);
    USE_UNICODE = Config.getBoolean("jcifs.smb.client.useUnicode", true);
    FORCE_UNICODE = Config.getBoolean("jcifs.smb.client.useUnicode", false);
    USE_NTSTATUS = Config.getBoolean("jcifs.smb.client.useNtStatus", true);
    SIGNPREF = Config.getBoolean("jcifs.smb.client.signingPreferred", false);
    USE_NTSMBS = Config.getBoolean("jcifs.smb.client.useNTSmbs", true);
    USE_EXTSEC = Config.getBoolean("jcifs.smb.client.useExtendedSecurity", false);
    NETBIOS_HOSTNAME = Config.getProperty("jcifs.netbios.hostname", null);
    LM_COMPATIBILITY = Config.getInt("jcifs.smb.lmCompatibility", 0);
    PID = (int)(65536.0D * Math.random());
    TZ = TimeZone.getDefault();
    USE_BATCHING = Config.getBoolean("jcifs.smb.client.useBatching", true);
    OEM_ENCODING = Config.getProperty("jcifs.encoding", Config.DEFAULT_OEM_ENCODING);
    int j;
    int m;
    label179: int i1;
    label195: int i3;
    label212: int i4;
    label230: int i5;
    label240: int i6;
    if (USE_EXTSEC)
    {
      j = 2048;
      int k = j | 0x3;
      if (!SIGNPREF)
        break label390;
      m = i;
      int n = k | m;
      if (!USE_NTSTATUS)
        break label395;
      i1 = 16384;
      int i2 = n | i1;
      if (!USE_UNICODE)
        break label401;
      i3 = 32768;
      DEFAULT_FLAGS2 = i3 | i2;
      if (!USE_NTSMBS)
        break label407;
      i4 = 16;
      if (!USE_NTSTATUS)
        break label413;
      i5 = 64;
      i6 = i4 | i5;
      if (!USE_UNICODE)
        break label419;
    }
    while (true)
    {
      DEFAULT_CAPABILITIES = 0x1000 | (i6 | i);
      FLAGS2 = Config.getInt("jcifs.smb.client.flags2", DEFAULT_FLAGS2);
      CAPABILITIES = Config.getInt("jcifs.smb.client.capabilities", DEFAULT_CAPABILITIES);
      TCP_NODELAY = Config.getBoolean("jcifs.smb.client.tcpNoDelay", false);
      RESPONSE_TIMEOUT = Config.getInt("jcifs.smb.client.responseTimeout", 30000);
      CONNECTIONS = new LinkedList();
      SSN_LIMIT = Config.getInt("jcifs.smb.client.ssnLimit", 250);
      SO_TIMEOUT = Config.getInt("jcifs.smb.client.soTimeout", 35000);
      NATIVE_OS = Config.getProperty("jcifs.smb.client.nativeOs", System.getProperty("os.name"));
      NATIVE_LANMAN = Config.getProperty("jcifs.smb.client.nativeLanMan", "jCIFS");
      NULL_TRANSPORT = new SmbTransport(null, 0, null, 0);
      return;
      j = 0;
      break;
      label390: m = 0;
      break label179;
      label395: i1 = 0;
      break label195;
      label401: i3 = 0;
      break label212;
      label407: i4 = 0;
      break label230;
      label413: i5 = 0;
      break label240;
      label419: i = 0;
    }
  }
}