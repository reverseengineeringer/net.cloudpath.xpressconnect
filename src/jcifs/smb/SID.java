package jcifs.smb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import jcifs.dcerpc.DcerpcHandle;
import jcifs.dcerpc.UnicodeString;
import jcifs.dcerpc.msrpc.LsaPolicyHandle;
import jcifs.dcerpc.msrpc.MsrpcLookupSids;
import jcifs.dcerpc.msrpc.MsrpcQueryInformationPolicy;
import jcifs.dcerpc.msrpc.lsarpc.LsarDomainInfo;
import jcifs.dcerpc.msrpc.lsarpc.LsarTranslatedName;
import jcifs.dcerpc.msrpc.lsarpc.LsarTrustInformation;
import jcifs.dcerpc.rpc.sid_t;
import jcifs.dcerpc.rpc.unicode_string;
import jcifs.util.Hexdump;

public class SID extends rpc.sid_t
{
  public static SID CREATOR_OWNER = null;
  public static SID EVERYONE;
  public static final int SID_FLAG_RESOLVE_SIDS = 1;
  public static final int SID_TYPE_ALIAS = 4;
  public static final int SID_TYPE_DELETED = 6;
  public static final int SID_TYPE_DOMAIN = 3;
  public static final int SID_TYPE_DOM_GRP = 2;
  public static final int SID_TYPE_INVALID = 7;
  static final String[] SID_TYPE_NAMES = { "0", "User", "Domain group", "Domain", "Local group", "Builtin group", "Deleted", "Invalid", "Unknown" };
  public static final int SID_TYPE_UNKNOWN = 8;
  public static final int SID_TYPE_USER = 1;
  public static final int SID_TYPE_USE_NONE = 0;
  public static final int SID_TYPE_WKN_GRP = 5;
  public static SID SYSTEM = null;
  static Map sid_cache;
  String acctName = null;
  String domainName = null;
  NtlmPasswordAuthentication origin_auth = null;
  String origin_server = null;
  int type;

  static
  {
    EVERYONE = null;
    try
    {
      EVERYONE = new SID("S-1-1-0");
      CREATOR_OWNER = new SID("S-1-3-0");
      SYSTEM = new SID("S-1-5-18");
      label104: sid_cache = Collections.synchronizedMap(new HashMap());
      return;
    }
    catch (SmbException localSmbException)
    {
      break label104;
    }
  }

  public SID(String paramString)
    throws SmbException
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "-");
    if ((localStringTokenizer.countTokens() < 3) || (!localStringTokenizer.nextToken().equals("S")))
      throw new SmbException("Bad textual SID format: " + paramString);
    this.revision = Byte.parseByte(localStringTokenizer.nextToken());
    String str = localStringTokenizer.nextToken();
    if (str.startsWith("0x"));
    for (long l = Long.parseLong(str.substring(2), 16); ; l = Long.parseLong(str))
    {
      this.identifier_authority = new byte[6];
      for (int i = 5; l > 0L; i--)
      {
        this.identifier_authority[i] = ((byte)(int)(l % 256L));
        l >>= 8;
      }
    }
    this.sub_authority_count = ((byte)localStringTokenizer.countTokens());
    if (this.sub_authority_count > 0)
    {
      this.sub_authority = new int[this.sub_authority_count];
      for (int j = 0; j < this.sub_authority_count; j++)
        this.sub_authority[j] = ((int)(0xFFFFFFFF & Long.parseLong(localStringTokenizer.nextToken())));
    }
  }

  SID(rpc.sid_t paramsid_t, int paramInt, String paramString1, String paramString2, boolean paramBoolean)
  {
    this.revision = paramsid_t.revision;
    this.sub_authority_count = paramsid_t.sub_authority_count;
    this.identifier_authority = paramsid_t.identifier_authority;
    this.sub_authority = paramsid_t.sub_authority;
    this.type = paramInt;
    this.domainName = paramString1;
    this.acctName = paramString2;
    if (paramBoolean)
    {
      this.sub_authority_count = ((byte)(-1 + this.sub_authority_count));
      this.sub_authority = new int[this.sub_authority_count];
      for (int i = 0; i < this.sub_authority_count; i++)
        this.sub_authority[i] = paramsid_t.sub_authority[i];
    }
  }

  public SID(SID paramSID, int paramInt)
  {
    this.revision = paramSID.revision;
    this.identifier_authority = paramSID.identifier_authority;
    this.sub_authority_count = ((byte)(1 + paramSID.sub_authority_count));
    this.sub_authority = new int[this.sub_authority_count];
    for (int i = 0; i < paramSID.sub_authority_count; i++)
      this.sub_authority[i] = paramSID.sub_authority[i];
    this.sub_authority[i] = paramInt;
  }

  public SID(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    this.revision = paramArrayOfByte[paramInt];
    int j = i + 1;
    this.sub_authority_count = paramArrayOfByte[i];
    this.identifier_authority = new byte[6];
    System.arraycopy(paramArrayOfByte, j, this.identifier_authority, 0, 6);
    int k = j + 6;
    if (this.sub_authority_count > 100)
      throw new RuntimeException("Invalid SID sub_authority_count");
    this.sub_authority = new int[this.sub_authority_count];
    for (int m = 0; m < this.sub_authority_count; m++)
    {
      this.sub_authority[m] = ServerMessageBlock.readInt4(paramArrayOfByte, k);
      k += 4;
    }
  }

  // ERROR //
  static SID[] getGroupMemberSids0(DcerpcHandle paramDcerpcHandle, jcifs.dcerpc.msrpc.SamrDomainHandle paramSamrDomainHandle, SID paramSID, int paramInt1, int paramInt2)
    throws IOException
  {
    // Byte code:
    //   0: new 212	jcifs/dcerpc/msrpc/lsarpc$LsarSidArray
    //   3: dup
    //   4: invokespecial 213	jcifs/dcerpc/msrpc/lsarpc$LsarSidArray:<init>	()V
    //   7: astore 5
    //   9: new 215	jcifs/dcerpc/msrpc/SamrAliasHandle
    //   12: dup
    //   13: aload_0
    //   14: aload_1
    //   15: ldc 216
    //   17: iload_3
    //   18: invokespecial 219	jcifs/dcerpc/msrpc/SamrAliasHandle:<init>	(Ljcifs/dcerpc/DcerpcHandle;Ljcifs/dcerpc/msrpc/SamrDomainHandle;II)V
    //   21: astore 6
    //   23: new 221	jcifs/dcerpc/msrpc/MsrpcGetMembersInAlias
    //   26: dup
    //   27: aload 6
    //   29: aload 5
    //   31: invokespecial 224	jcifs/dcerpc/msrpc/MsrpcGetMembersInAlias:<init>	(Ljcifs/dcerpc/msrpc/SamrAliasHandle;Ljcifs/dcerpc/msrpc/lsarpc$LsarSidArray;)V
    //   34: astore 7
    //   36: aload_0
    //   37: aload 7
    //   39: invokevirtual 230	jcifs/dcerpc/DcerpcHandle:sendrecv	(Ljcifs/dcerpc/DcerpcMessage;)V
    //   42: aload 7
    //   44: getfield 233	jcifs/dcerpc/msrpc/MsrpcGetMembersInAlias:retval	I
    //   47: ifeq +36 -> 83
    //   50: new 43	jcifs/smb/SmbException
    //   53: dup
    //   54: aload 7
    //   56: getfield 233	jcifs/dcerpc/msrpc/MsrpcGetMembersInAlias:retval	I
    //   59: iconst_0
    //   60: invokespecial 236	jcifs/smb/SmbException:<init>	(IZ)V
    //   63: athrow
    //   64: astore 8
    //   66: aload 6
    //   68: astore 9
    //   70: aload 9
    //   72: ifnull +8 -> 80
    //   75: aload 9
    //   77: invokevirtual 239	jcifs/dcerpc/msrpc/SamrAliasHandle:close	()V
    //   80: aload 8
    //   82: athrow
    //   83: aload 7
    //   85: getfield 243	jcifs/dcerpc/msrpc/MsrpcGetMembersInAlias:sids	Ljcifs/dcerpc/msrpc/lsarpc$LsarSidArray;
    //   88: getfield 246	jcifs/dcerpc/msrpc/lsarpc$LsarSidArray:num_sids	I
    //   91: anewarray 2	jcifs/smb/SID
    //   94: astore 10
    //   96: aload_0
    //   97: invokevirtual 249	jcifs/dcerpc/DcerpcHandle:getServer	()Ljava/lang/String;
    //   100: astore 11
    //   102: aload_0
    //   103: invokevirtual 253	jcifs/dcerpc/DcerpcHandle:getPrincipal	()Ljava/security/Principal;
    //   106: checkcast 255	jcifs/smb/NtlmPasswordAuthentication
    //   109: astore 12
    //   111: iconst_0
    //   112: istore 13
    //   114: iload 13
    //   116: aload 10
    //   118: arraylength
    //   119: if_icmpge +59 -> 178
    //   122: aload 10
    //   124: iload 13
    //   126: new 2	jcifs/smb/SID
    //   129: dup
    //   130: aload 7
    //   132: getfield 243	jcifs/dcerpc/msrpc/MsrpcGetMembersInAlias:sids	Ljcifs/dcerpc/msrpc/lsarpc$LsarSidArray;
    //   135: getfield 258	jcifs/dcerpc/msrpc/lsarpc$LsarSidArray:sids	[Ljcifs/dcerpc/msrpc/lsarpc$LsarSidPtr;
    //   138: iload 13
    //   140: aaload
    //   141: getfield 264	jcifs/dcerpc/msrpc/lsarpc$LsarSidPtr:sid	Ljcifs/dcerpc/rpc$sid_t;
    //   144: iconst_0
    //   145: aconst_null
    //   146: aconst_null
    //   147: iconst_0
    //   148: invokespecial 266	jcifs/smb/SID:<init>	(Ljcifs/dcerpc/rpc$sid_t;ILjava/lang/String;Ljava/lang/String;Z)V
    //   151: aastore
    //   152: aload 10
    //   154: iload 13
    //   156: aaload
    //   157: aload 11
    //   159: putfield 100	jcifs/smb/SID:origin_server	Ljava/lang/String;
    //   162: aload 10
    //   164: iload 13
    //   166: aaload
    //   167: aload 12
    //   169: putfield 102	jcifs/smb/SID:origin_auth	Ljcifs/smb/NtlmPasswordAuthentication;
    //   172: iinc 13 1
    //   175: goto -61 -> 114
    //   178: aload 10
    //   180: arraylength
    //   181: ifle +19 -> 200
    //   184: iload 4
    //   186: iconst_1
    //   187: iand
    //   188: ifeq +12 -> 200
    //   191: aload 11
    //   193: aload 12
    //   195: aload 10
    //   197: invokestatic 270	jcifs/smb/SID:resolveSids	(Ljava/lang/String;Ljcifs/smb/NtlmPasswordAuthentication;[Ljcifs/smb/SID;)V
    //   200: aload 6
    //   202: ifnull +8 -> 210
    //   205: aload 6
    //   207: invokevirtual 239	jcifs/dcerpc/msrpc/SamrAliasHandle:close	()V
    //   210: aload 10
    //   212: areturn
    //   213: astore 8
    //   215: aconst_null
    //   216: astore 9
    //   218: goto -148 -> 70
    //   221: astore 8
    //   223: aload 6
    //   225: astore 9
    //   227: goto -157 -> 70
    //
    // Exception table:
    //   from	to	target	type
    //   36	64	64	finally
    //   83	111	64	finally
    //   114	172	64	finally
    //   178	184	64	finally
    //   191	200	64	finally
    //   9	23	213	finally
    //   23	36	221	finally
  }

  // ERROR //
  static Map getLocalGroupsMap(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokestatic 276	jcifs/smb/SID:getServerSid	(Ljava/lang/String;Ljcifs/smb/NtlmPasswordAuthentication;)Ljcifs/smb/SID;
    //   5: astore_3
    //   6: aconst_null
    //   7: astore 4
    //   9: new 278	jcifs/dcerpc/msrpc/samr$SamrSamArray
    //   12: dup
    //   13: invokespecial 279	jcifs/dcerpc/msrpc/samr$SamrSamArray:<init>	()V
    //   16: astore 5
    //   18: new 125	java/lang/StringBuffer
    //   21: dup
    //   22: invokespecial 126	java/lang/StringBuffer:<init>	()V
    //   25: ldc_w 281
    //   28: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   31: aload_0
    //   32: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   35: ldc_w 283
    //   38: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   41: invokevirtual 135	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   44: aload_1
    //   45: invokestatic 287	jcifs/dcerpc/DcerpcHandle:getHandle	(Ljava/lang/String;Ljcifs/smb/NtlmPasswordAuthentication;)Ljcifs/dcerpc/DcerpcHandle;
    //   48: astore 4
    //   50: new 289	jcifs/dcerpc/msrpc/SamrPolicyHandle
    //   53: dup
    //   54: aload 4
    //   56: aload_0
    //   57: ldc_w 290
    //   60: invokespecial 293	jcifs/dcerpc/msrpc/SamrPolicyHandle:<init>	(Ljcifs/dcerpc/DcerpcHandle;Ljava/lang/String;I)V
    //   63: astore 9
    //   65: new 295	jcifs/dcerpc/msrpc/SamrDomainHandle
    //   68: dup
    //   69: aload 4
    //   71: aload 9
    //   73: ldc_w 290
    //   76: aload_3
    //   77: invokespecial 298	jcifs/dcerpc/msrpc/SamrDomainHandle:<init>	(Ljcifs/dcerpc/DcerpcHandle;Ljcifs/dcerpc/msrpc/SamrPolicyHandle;ILjcifs/dcerpc/rpc$sid_t;)V
    //   80: astore 10
    //   82: new 300	jcifs/dcerpc/msrpc/MsrpcEnumerateAliasesInDomain
    //   85: dup
    //   86: aload 10
    //   88: ldc_w 301
    //   91: aload 5
    //   93: invokespecial 304	jcifs/dcerpc/msrpc/MsrpcEnumerateAliasesInDomain:<init>	(Ljcifs/dcerpc/msrpc/SamrDomainHandle;ILjcifs/dcerpc/msrpc/samr$SamrSamArray;)V
    //   96: astore 11
    //   98: aload 4
    //   100: aload 11
    //   102: invokevirtual 230	jcifs/dcerpc/DcerpcHandle:sendrecv	(Ljcifs/dcerpc/DcerpcMessage;)V
    //   105: aload 11
    //   107: getfield 305	jcifs/dcerpc/msrpc/MsrpcEnumerateAliasesInDomain:retval	I
    //   110: ifeq +60 -> 170
    //   113: new 43	jcifs/smb/SmbException
    //   116: dup
    //   117: aload 11
    //   119: getfield 305	jcifs/dcerpc/msrpc/MsrpcEnumerateAliasesInDomain:retval	I
    //   122: iconst_0
    //   123: invokespecial 236	jcifs/smb/SmbException:<init>	(IZ)V
    //   126: athrow
    //   127: astore 6
    //   129: aload 10
    //   131: astore 7
    //   133: aload 9
    //   135: astore 8
    //   137: aload 4
    //   139: ifnull +28 -> 167
    //   142: aload 8
    //   144: ifnull +18 -> 162
    //   147: aload 7
    //   149: ifnull +8 -> 157
    //   152: aload 7
    //   154: invokevirtual 306	jcifs/dcerpc/msrpc/SamrDomainHandle:close	()V
    //   157: aload 8
    //   159: invokevirtual 307	jcifs/dcerpc/msrpc/SamrPolicyHandle:close	()V
    //   162: aload 4
    //   164: invokevirtual 308	jcifs/dcerpc/DcerpcHandle:close	()V
    //   167: aload 6
    //   169: athrow
    //   170: new 83	java/util/HashMap
    //   173: dup
    //   174: invokespecial 85	java/util/HashMap:<init>	()V
    //   177: astore 12
    //   179: iconst_0
    //   180: istore 13
    //   182: iload 13
    //   184: aload 11
    //   186: getfield 312	jcifs/dcerpc/msrpc/MsrpcEnumerateAliasesInDomain:sam	Ljcifs/dcerpc/msrpc/samr$SamrSamArray;
    //   189: getfield 315	jcifs/dcerpc/msrpc/samr$SamrSamArray:count	I
    //   192: if_icmpge +170 -> 362
    //   195: aload 11
    //   197: getfield 312	jcifs/dcerpc/msrpc/MsrpcEnumerateAliasesInDomain:sam	Ljcifs/dcerpc/msrpc/samr$SamrSamArray;
    //   200: getfield 319	jcifs/dcerpc/msrpc/samr$SamrSamArray:entries	[Ljcifs/dcerpc/msrpc/samr$SamrSamEntry;
    //   203: iload 13
    //   205: aaload
    //   206: astore 14
    //   208: aload 4
    //   210: aload 10
    //   212: aload_3
    //   213: aload 14
    //   215: getfield 324	jcifs/dcerpc/msrpc/samr$SamrSamEntry:idx	I
    //   218: iload_2
    //   219: invokestatic 326	jcifs/smb/SID:getGroupMemberSids0	(Ljcifs/dcerpc/DcerpcHandle;Ljcifs/dcerpc/msrpc/SamrDomainHandle;Ljcifs/smb/SID;II)[Ljcifs/smb/SID;
    //   222: astore 15
    //   224: new 2	jcifs/smb/SID
    //   227: dup
    //   228: aload_3
    //   229: aload 14
    //   231: getfield 324	jcifs/dcerpc/msrpc/samr$SamrSamEntry:idx	I
    //   234: invokespecial 328	jcifs/smb/SID:<init>	(Ljcifs/smb/SID;I)V
    //   237: astore 16
    //   239: aload 16
    //   241: iconst_4
    //   242: putfield 187	jcifs/smb/SID:type	I
    //   245: aload 16
    //   247: aload_3
    //   248: invokevirtual 331	jcifs/smb/SID:getDomainName	()Ljava/lang/String;
    //   251: putfield 96	jcifs/smb/SID:domainName	Ljava/lang/String;
    //   254: aload 16
    //   256: new 333	jcifs/dcerpc/UnicodeString
    //   259: dup
    //   260: aload 14
    //   262: getfield 337	jcifs/dcerpc/msrpc/samr$SamrSamEntry:name	Ljcifs/dcerpc/rpc$unicode_string;
    //   265: iconst_0
    //   266: invokespecial 340	jcifs/dcerpc/UnicodeString:<init>	(Ljcifs/dcerpc/rpc$unicode_string;Z)V
    //   269: invokevirtual 341	jcifs/dcerpc/UnicodeString:toString	()Ljava/lang/String;
    //   272: putfield 98	jcifs/smb/SID:acctName	Ljava/lang/String;
    //   275: iconst_0
    //   276: istore 17
    //   278: iload 17
    //   280: aload 15
    //   282: arraylength
    //   283: if_icmpge +73 -> 356
    //   286: aload 12
    //   288: aload 15
    //   290: iload 17
    //   292: aaload
    //   293: invokeinterface 347 2 0
    //   298: checkcast 349	java/util/ArrayList
    //   301: astore 18
    //   303: aload 18
    //   305: ifnonnull +27 -> 332
    //   308: new 349	java/util/ArrayList
    //   311: dup
    //   312: invokespecial 350	java/util/ArrayList:<init>	()V
    //   315: astore 18
    //   317: aload 12
    //   319: aload 15
    //   321: iload 17
    //   323: aaload
    //   324: aload 18
    //   326: invokeinterface 354 3 0
    //   331: pop
    //   332: aload 18
    //   334: aload 16
    //   336: invokevirtual 357	java/util/ArrayList:contains	(Ljava/lang/Object;)Z
    //   339: ifne +11 -> 350
    //   342: aload 18
    //   344: aload 16
    //   346: invokevirtual 360	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   349: pop
    //   350: iinc 17 1
    //   353: goto -75 -> 278
    //   356: iinc 13 1
    //   359: goto -177 -> 182
    //   362: aload 4
    //   364: ifnull +28 -> 392
    //   367: aload 9
    //   369: ifnull +18 -> 387
    //   372: aload 10
    //   374: ifnull +8 -> 382
    //   377: aload 10
    //   379: invokevirtual 306	jcifs/dcerpc/msrpc/SamrDomainHandle:close	()V
    //   382: aload 9
    //   384: invokevirtual 307	jcifs/dcerpc/msrpc/SamrPolicyHandle:close	()V
    //   387: aload 4
    //   389: invokevirtual 308	jcifs/dcerpc/DcerpcHandle:close	()V
    //   392: aload 12
    //   394: areturn
    //   395: astore 6
    //   397: aconst_null
    //   398: astore 7
    //   400: aconst_null
    //   401: astore 8
    //   403: goto -266 -> 137
    //   406: astore 6
    //   408: aload 9
    //   410: astore 8
    //   412: aconst_null
    //   413: astore 7
    //   415: goto -278 -> 137
    //
    // Exception table:
    //   from	to	target	type
    //   82	127	127	finally
    //   170	179	127	finally
    //   182	275	127	finally
    //   278	303	127	finally
    //   308	332	127	finally
    //   332	350	127	finally
    //   18	65	395	finally
    //   65	82	406	finally
  }

  public static SID getServerSid(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws IOException
  {
    DcerpcHandle localDcerpcHandle = null;
    lsarpc.LsarDomainInfo localLsarDomainInfo = new lsarpc.LsarDomainInfo();
    try
    {
      localDcerpcHandle = DcerpcHandle.getHandle("ncacn_np:" + paramString + "[\\PIPE\\lsarpc]", paramNtlmPasswordAuthentication);
      LsaPolicyHandle localLsaPolicyHandle2 = new LsaPolicyHandle(localDcerpcHandle, null, 1);
      try
      {
        MsrpcQueryInformationPolicy localMsrpcQueryInformationPolicy = new MsrpcQueryInformationPolicy(localLsaPolicyHandle2, (short)5, localLsarDomainInfo);
        localDcerpcHandle.sendrecv(localMsrpcQueryInformationPolicy);
        if (localMsrpcQueryInformationPolicy.retval == 0)
          break label121;
        throw new SmbException(localMsrpcQueryInformationPolicy.retval, false);
      }
      finally
      {
        localLsaPolicyHandle1 = localLsaPolicyHandle2;
      }
      if (localDcerpcHandle != null)
      {
        if (localLsaPolicyHandle1 != null)
          localLsaPolicyHandle1.close();
        localDcerpcHandle.close();
      }
      throw localObject1;
      label121: SID localSID = new SID(localLsarDomainInfo.sid, 3, new UnicodeString(localLsarDomainInfo.name, false).toString(), null, false);
      if (localDcerpcHandle != null)
      {
        if (localLsaPolicyHandle2 != null)
          localLsaPolicyHandle2.close();
        localDcerpcHandle.close();
      }
      return localSID;
    }
    finally
    {
      while (true)
        LsaPolicyHandle localLsaPolicyHandle1 = null;
    }
  }

  public static void resolveSids(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication, SID[] paramArrayOfSID)
    throws IOException
  {
    ArrayList localArrayList = new ArrayList(paramArrayOfSID.length);
    int i = 0;
    if (i < paramArrayOfSID.length)
    {
      SID localSID = (SID)sid_cache.get(paramArrayOfSID[i]);
      if (localSID != null)
      {
        paramArrayOfSID[i].type = localSID.type;
        paramArrayOfSID[i].domainName = localSID.domainName;
        paramArrayOfSID[i].acctName = localSID.acctName;
      }
      while (true)
      {
        i++;
        break;
        localArrayList.add(paramArrayOfSID[i]);
      }
    }
    if (localArrayList.size() > 0)
    {
      SID[] arrayOfSID = (SID[])localArrayList.toArray(new SID[0]);
      resolveSids0(paramString, paramNtlmPasswordAuthentication, arrayOfSID);
      for (int j = 0; j < arrayOfSID.length; j++)
        sid_cache.put(arrayOfSID[j], arrayOfSID[j]);
    }
  }

  public static void resolveSids(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication, SID[] paramArrayOfSID, int paramInt1, int paramInt2)
    throws IOException
  {
    ArrayList localArrayList = new ArrayList(paramArrayOfSID.length);
    int i = 0;
    if (i < paramInt2)
    {
      SID localSID = (SID)sid_cache.get(paramArrayOfSID[(paramInt1 + i)]);
      if (localSID != null)
      {
        paramArrayOfSID[(paramInt1 + i)].type = localSID.type;
        paramArrayOfSID[(paramInt1 + i)].domainName = localSID.domainName;
        paramArrayOfSID[(paramInt1 + i)].acctName = localSID.acctName;
      }
      while (true)
      {
        i++;
        break;
        localArrayList.add(paramArrayOfSID[(paramInt1 + i)]);
      }
    }
    if (localArrayList.size() > 0)
    {
      SID[] arrayOfSID = (SID[])localArrayList.toArray(new SID[0]);
      resolveSids0(paramString, paramNtlmPasswordAuthentication, arrayOfSID);
      for (int j = 0; j < arrayOfSID.length; j++)
        sid_cache.put(arrayOfSID[j], arrayOfSID[j]);
    }
  }

  static void resolveSids(DcerpcHandle paramDcerpcHandle, LsaPolicyHandle paramLsaPolicyHandle, SID[] paramArrayOfSID)
    throws IOException
  {
    MsrpcLookupSids localMsrpcLookupSids = new MsrpcLookupSids(paramLsaPolicyHandle, paramArrayOfSID);
    paramDcerpcHandle.sendrecv(localMsrpcLookupSids);
    switch (localMsrpcLookupSids.retval)
    {
    default:
      throw new SmbException(localMsrpcLookupSids.retval, false);
    case -1073741709:
    case 0:
    case 263:
    }
    int i = 0;
    if (i < paramArrayOfSID.length)
    {
      paramArrayOfSID[i].type = localMsrpcLookupSids.names.names[i].sid_type;
      paramArrayOfSID[i].domainName = null;
      switch (paramArrayOfSID[i].type)
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
      while (true)
      {
        paramArrayOfSID[i].acctName = new UnicodeString(localMsrpcLookupSids.names.names[i].name, false).toString();
        paramArrayOfSID[i].origin_server = null;
        paramArrayOfSID[i].origin_auth = null;
        i++;
        break;
        int j = localMsrpcLookupSids.names.names[i].sid_index;
        rpc.unicode_string localunicode_string = localMsrpcLookupSids.domains.domains[j].name;
        paramArrayOfSID[i].domainName = new UnicodeString(localunicode_string, false).toString();
      }
    }
  }

  // ERROR //
  static void resolveSids0(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication, SID[] paramArrayOfSID)
    throws IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: new 125	java/lang/StringBuffer
    //   8: dup
    //   9: invokespecial 126	java/lang/StringBuffer:<init>	()V
    //   12: ldc_w 281
    //   15: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   18: aload_0
    //   19: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   22: ldc_w 365
    //   25: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   28: invokevirtual 135	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   31: aload_1
    //   32: invokestatic 287	jcifs/dcerpc/DcerpcHandle:getHandle	(Ljava/lang/String;Ljcifs/smb/NtlmPasswordAuthentication;)Ljcifs/dcerpc/DcerpcHandle;
    //   35: astore_3
    //   36: aload_0
    //   37: astore 6
    //   39: aload 6
    //   41: bipush 46
    //   43: invokevirtual 434	java/lang/String:indexOf	(I)I
    //   46: istore 7
    //   48: iload 7
    //   50: ifle +25 -> 75
    //   53: aload 6
    //   55: iconst_0
    //   56: invokevirtual 438	java/lang/String:charAt	(I)C
    //   59: invokestatic 444	java/lang/Character:isDigit	(C)Z
    //   62: ifne +13 -> 75
    //   65: aload 6
    //   67: iconst_0
    //   68: iload 7
    //   70: invokevirtual 447	java/lang/String:substring	(II)Ljava/lang/String;
    //   73: astore 6
    //   75: new 367	jcifs/dcerpc/msrpc/LsaPolicyHandle
    //   78: dup
    //   79: aload_3
    //   80: new 125	java/lang/StringBuffer
    //   83: dup
    //   84: invokespecial 126	java/lang/StringBuffer:<init>	()V
    //   87: ldc_w 449
    //   90: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   93: aload 6
    //   95: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   98: invokevirtual 135	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   101: sipush 2048
    //   104: invokespecial 368	jcifs/dcerpc/msrpc/LsaPolicyHandle:<init>	(Ljcifs/dcerpc/DcerpcHandle;Ljava/lang/String;I)V
    //   107: astore 8
    //   109: aload_3
    //   110: aload 8
    //   112: aload_2
    //   113: invokestatic 451	jcifs/smb/SID:resolveSids	(Ljcifs/dcerpc/DcerpcHandle;Ljcifs/dcerpc/msrpc/LsaPolicyHandle;[Ljcifs/smb/SID;)V
    //   116: aload_3
    //   117: ifnull +17 -> 134
    //   120: aload 8
    //   122: ifnull +8 -> 130
    //   125: aload 8
    //   127: invokevirtual 375	jcifs/dcerpc/msrpc/LsaPolicyHandle:close	()V
    //   130: aload_3
    //   131: invokevirtual 308	jcifs/dcerpc/DcerpcHandle:close	()V
    //   134: return
    //   135: astore 5
    //   137: aload_3
    //   138: ifnull +17 -> 155
    //   141: aload 4
    //   143: ifnull +8 -> 151
    //   146: aload 4
    //   148: invokevirtual 375	jcifs/dcerpc/msrpc/LsaPolicyHandle:close	()V
    //   151: aload_3
    //   152: invokevirtual 308	jcifs/dcerpc/DcerpcHandle:close	()V
    //   155: aload 5
    //   157: athrow
    //   158: astore 5
    //   160: aload 8
    //   162: astore 4
    //   164: goto -27 -> 137
    //
    // Exception table:
    //   from	to	target	type
    //   5	36	135	finally
    //   39	48	135	finally
    //   53	75	135	finally
    //   75	109	135	finally
    //   109	116	158	finally
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1 = true;
    boolean bool2 = paramObject instanceof SID;
    boolean bool3 = false;
    SID localSID;
    if (bool2)
    {
      localSID = (SID)paramObject;
      if (localSID != this)
        break label32;
      bool3 = bool1;
    }
    label32: int i;
    int j;
    do
    {
      return bool3;
      i = localSID.sub_authority_count;
      j = this.sub_authority_count;
      bool3 = false;
    }
    while (i != j);
    int m;
    for (int k = this.sub_authority_count; ; k = m)
    {
      m = k - 1;
      if (k <= 0)
        break label108;
      int i3 = localSID.sub_authority[m];
      int i4 = this.sub_authority[m];
      bool3 = false;
      if (i3 != i4)
        break;
    }
    label108: for (int n = 0; ; n++)
    {
      if (n >= 6)
        break label153;
      int i1 = localSID.identifier_authority[n];
      int i2 = this.identifier_authority[n];
      bool3 = false;
      if (i1 != i2)
        break;
    }
    label153: if (localSID.revision == this.revision);
    while (true)
    {
      return bool1;
      bool1 = false;
    }
  }

  public String getAccountName()
  {
    if (this.origin_server != null)
      resolveWeak();
    if (this.type == 8)
      return "" + this.sub_authority[(-1 + this.sub_authority_count)];
    if (this.type == 3)
      return "";
    return this.acctName;
  }

  public String getDomainName()
  {
    if (this.origin_server != null)
      resolveWeak();
    if (this.type == 8)
    {
      String str = toString();
      return str.substring(0, -1 + (str.length() - getAccountName().length()));
    }
    return this.domainName;
  }

  public SID getDomainSid()
  {
    String str = this.domainName;
    if (getType() != 3);
    for (boolean bool = true; ; bool = false)
      return new SID(this, 3, str, null, bool);
  }

  // ERROR //
  public SID[] getGroupMemberSids(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 187	jcifs/smb/SID:type	I
    //   4: iconst_2
    //   5: if_icmpeq +20 -> 25
    //   8: aload_0
    //   9: getfield 187	jcifs/smb/SID:type	I
    //   12: iconst_4
    //   13: if_icmpeq +12 -> 25
    //   16: iconst_0
    //   17: anewarray 2	jcifs/smb/SID
    //   20: astore 12
    //   22: aload 12
    //   24: areturn
    //   25: aconst_null
    //   26: astore 4
    //   28: aconst_null
    //   29: astore 5
    //   31: aconst_null
    //   32: astore 6
    //   34: aload_0
    //   35: invokevirtual 475	jcifs/smb/SID:getDomainSid	()Ljcifs/smb/SID;
    //   38: astore 7
    //   40: new 125	java/lang/StringBuffer
    //   43: dup
    //   44: invokespecial 126	java/lang/StringBuffer:<init>	()V
    //   47: ldc_w 281
    //   50: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   53: aload_1
    //   54: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   57: ldc_w 283
    //   60: invokevirtual 132	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   63: invokevirtual 135	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   66: aload_2
    //   67: invokestatic 287	jcifs/dcerpc/DcerpcHandle:getHandle	(Ljava/lang/String;Ljcifs/smb/NtlmPasswordAuthentication;)Ljcifs/dcerpc/DcerpcHandle;
    //   70: astore 4
    //   72: new 289	jcifs/dcerpc/msrpc/SamrPolicyHandle
    //   75: dup
    //   76: aload 4
    //   78: aload_1
    //   79: bipush 48
    //   81: invokespecial 293	jcifs/dcerpc/msrpc/SamrPolicyHandle:<init>	(Ljcifs/dcerpc/DcerpcHandle;Ljava/lang/String;I)V
    //   84: astore 9
    //   86: new 295	jcifs/dcerpc/msrpc/SamrDomainHandle
    //   89: dup
    //   90: aload 4
    //   92: aload 9
    //   94: sipush 512
    //   97: aload 7
    //   99: invokespecial 298	jcifs/dcerpc/msrpc/SamrDomainHandle:<init>	(Ljcifs/dcerpc/DcerpcHandle;Ljcifs/dcerpc/msrpc/SamrPolicyHandle;ILjcifs/dcerpc/rpc$sid_t;)V
    //   102: astore 10
    //   104: aload 4
    //   106: aload 10
    //   108: aload 7
    //   110: aload_0
    //   111: invokevirtual 478	jcifs/smb/SID:getRid	()I
    //   114: iload_3
    //   115: invokestatic 326	jcifs/smb/SID:getGroupMemberSids0	(Ljcifs/dcerpc/DcerpcHandle;Ljcifs/dcerpc/msrpc/SamrDomainHandle;Ljcifs/smb/SID;II)[Ljcifs/smb/SID;
    //   118: astore 11
    //   120: aload 11
    //   122: astore 12
    //   124: aload 4
    //   126: ifnull -104 -> 22
    //   129: aload 9
    //   131: ifnull +18 -> 149
    //   134: aload 10
    //   136: ifnull +8 -> 144
    //   139: aload 10
    //   141: invokevirtual 306	jcifs/dcerpc/msrpc/SamrDomainHandle:close	()V
    //   144: aload 9
    //   146: invokevirtual 307	jcifs/dcerpc/msrpc/SamrPolicyHandle:close	()V
    //   149: aload 4
    //   151: invokevirtual 308	jcifs/dcerpc/DcerpcHandle:close	()V
    //   154: aload 12
    //   156: areturn
    //   157: astore 8
    //   159: aload 4
    //   161: ifnull +28 -> 189
    //   164: aload 5
    //   166: ifnull +18 -> 184
    //   169: aload 6
    //   171: ifnull +8 -> 179
    //   174: aload 6
    //   176: invokevirtual 306	jcifs/dcerpc/msrpc/SamrDomainHandle:close	()V
    //   179: aload 5
    //   181: invokevirtual 307	jcifs/dcerpc/msrpc/SamrPolicyHandle:close	()V
    //   184: aload 4
    //   186: invokevirtual 308	jcifs/dcerpc/DcerpcHandle:close	()V
    //   189: aload 8
    //   191: athrow
    //   192: astore 8
    //   194: aload 9
    //   196: astore 5
    //   198: aconst_null
    //   199: astore 6
    //   201: goto -42 -> 159
    //   204: astore 8
    //   206: aload 10
    //   208: astore 6
    //   210: aload 9
    //   212: astore 5
    //   214: goto -55 -> 159
    //
    // Exception table:
    //   from	to	target	type
    //   40	86	157	finally
    //   86	104	192	finally
    //   104	120	204	finally
  }

  public int getRid()
  {
    if (getType() == 3)
      throw new IllegalArgumentException("This SID is a domain sid");
    return this.sub_authority[(-1 + this.sub_authority_count)];
  }

  public int getType()
  {
    if (this.origin_server != null)
      resolveWeak();
    return this.type;
  }

  public String getTypeText()
  {
    if (this.origin_server != null)
      resolveWeak();
    return SID_TYPE_NAMES[this.type];
  }

  public int hashCode()
  {
    int i = this.identifier_authority[5];
    for (int j = 0; j < this.sub_authority_count; j++)
      i += 65599 * this.sub_authority[j];
    return i;
  }

  public void resolve(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws IOException
  {
    resolveSids(paramString, paramNtlmPasswordAuthentication, new SID[] { this });
  }

  // ERROR //
  void resolveWeak()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 100	jcifs/smb/SID:origin_server	Ljava/lang/String;
    //   4: ifnull +25 -> 29
    //   7: aload_0
    //   8: aload_0
    //   9: getfield 100	jcifs/smb/SID:origin_server	Ljava/lang/String;
    //   12: aload_0
    //   13: getfield 102	jcifs/smb/SID:origin_auth	Ljcifs/smb/NtlmPasswordAuthentication;
    //   16: invokevirtual 490	jcifs/smb/SID:resolve	(Ljava/lang/String;Ljcifs/smb/NtlmPasswordAuthentication;)V
    //   19: aload_0
    //   20: aconst_null
    //   21: putfield 100	jcifs/smb/SID:origin_server	Ljava/lang/String;
    //   24: aload_0
    //   25: aconst_null
    //   26: putfield 102	jcifs/smb/SID:origin_auth	Ljcifs/smb/NtlmPasswordAuthentication;
    //   29: return
    //   30: astore_2
    //   31: aload_0
    //   32: aconst_null
    //   33: putfield 100	jcifs/smb/SID:origin_server	Ljava/lang/String;
    //   36: aload_0
    //   37: aconst_null
    //   38: putfield 102	jcifs/smb/SID:origin_auth	Ljcifs/smb/NtlmPasswordAuthentication;
    //   41: return
    //   42: astore_1
    //   43: aload_0
    //   44: aconst_null
    //   45: putfield 100	jcifs/smb/SID:origin_server	Ljava/lang/String;
    //   48: aload_0
    //   49: aconst_null
    //   50: putfield 102	jcifs/smb/SID:origin_auth	Ljcifs/smb/NtlmPasswordAuthentication;
    //   53: aload_1
    //   54: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   7	19	30	java/io/IOException
    //   7	19	42	finally
  }

  public String toDisplayString()
  {
    if (this.origin_server != null)
      resolveWeak();
    if (this.domainName != null)
    {
      if (this.type == 3)
        return this.domainName;
      if ((this.type == 5) || (this.domainName.equals("BUILTIN")))
      {
        if (this.type == 8)
          return toString();
        return this.acctName;
      }
      return this.domainName + "\\" + this.acctName;
    }
    return toString();
  }

  public String toString()
  {
    String str1 = "S-" + (0xFF & this.revision) + "-";
    String str2;
    if ((this.identifier_authority[0] != 0) || (this.identifier_authority[1] != 0))
      str2 = str1 + "0x";
    long l2;
    for (String str3 = str2 + Hexdump.toHexString(this.identifier_authority, 0, 6); ; str3 = str1 + l2)
    {
      for (int i = 0; i < this.sub_authority_count; i++)
        str3 = str3 + "-" + (0xFFFFFFFF & this.sub_authority[i]);
      long l1 = 0L;
      l2 = 0L;
      for (int j = 5; j > 1; j--)
      {
        l2 += ((0xFF & this.identifier_authority[j]) << (int)l1);
        l1 += 8L;
      }
    }
    return str3;
  }
}