package jcifs.dcerpc.msrpc;

import jcifs.dcerpc.ndr.NdrLong;
import jcifs.smb.FileEntry;
import jcifs.smb.SmbShareInfo;

public class MsrpcDfsRootEnum extends netdfs.NetrDfsEnumEx
{
  public MsrpcDfsRootEnum(String paramString)
  {
    super(paramString, 200, 65535, new netdfs.DfsEnumStruct(), new NdrLong(0));
    this.info.level = this.level;
    this.info.e = new netdfs.DfsEnumArray200();
    this.ptype = 0;
    this.flags = 3;
  }

  public FileEntry[] getEntries()
  {
    netdfs.DfsEnumArray200 localDfsEnumArray200 = (netdfs.DfsEnumArray200)this.info.e;
    SmbShareInfo[] arrayOfSmbShareInfo = new SmbShareInfo[localDfsEnumArray200.count];
    for (int i = 0; i < localDfsEnumArray200.count; i++)
      arrayOfSmbShareInfo[i] = new SmbShareInfo(localDfsEnumArray200.s[i].dfs_name, 0, null);
    return arrayOfSmbShareInfo;
  }
}