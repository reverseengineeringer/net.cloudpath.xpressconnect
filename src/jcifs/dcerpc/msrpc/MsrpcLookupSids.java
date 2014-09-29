package jcifs.dcerpc.msrpc;

import jcifs.smb.SID;

public class MsrpcLookupSids extends lsarpc.LsarLookupSids
{
  SID[] sids;

  public MsrpcLookupSids(LsaPolicyHandle paramLsaPolicyHandle, SID[] paramArrayOfSID)
  {
    super(paramLsaPolicyHandle, new LsarSidArrayX(paramArrayOfSID), new lsarpc.LsarRefDomainList(), new lsarpc.LsarTransNameArray(), (short)1, paramArrayOfSID.length);
    this.sids = paramArrayOfSID;
    this.ptype = 0;
    this.flags = 3;
  }
}