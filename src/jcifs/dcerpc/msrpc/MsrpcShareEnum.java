package jcifs.dcerpc.msrpc;

import jcifs.smb.FileEntry;
import jcifs.smb.SmbShareInfo;

public class MsrpcShareEnum extends srvsvc.ShareEnumAll
{
  public MsrpcShareEnum(String paramString)
  {
    super("\\\\" + paramString, 1, new srvsvc.ShareInfoCtr1(), -1, 0, 0);
    this.ptype = 0;
    this.flags = 3;
  }

  public FileEntry[] getEntries()
  {
    srvsvc.ShareInfoCtr1 localShareInfoCtr1 = (srvsvc.ShareInfoCtr1)this.info;
    MsrpcShareInfo1[] arrayOfMsrpcShareInfo1 = new MsrpcShareInfo1[localShareInfoCtr1.count];
    for (int i = 0; i < localShareInfoCtr1.count; i++)
      arrayOfMsrpcShareInfo1[i] = new MsrpcShareInfo1(localShareInfoCtr1.array[i]);
    return arrayOfMsrpcShareInfo1;
  }

  class MsrpcShareInfo1 extends SmbShareInfo
  {
    MsrpcShareInfo1(srvsvc.ShareInfo1 arg2)
    {
      Object localObject;
      this.netName = localObject.netname;
      this.type = localObject.type;
      this.remark = localObject.remark;
    }
  }
}