package jcifs.dcerpc.msrpc;

import jcifs.smb.SID;

class LsarSidArrayX extends lsarpc.LsarSidArray
{
  LsarSidArrayX(SID[] paramArrayOfSID)
  {
    this.num_sids = paramArrayOfSID.length;
    this.sids = new lsarpc.LsarSidPtr[paramArrayOfSID.length];
    for (int i = 0; i < paramArrayOfSID.length; i++)
    {
      this.sids[i] = new lsarpc.LsarSidPtr();
      this.sids[i].sid = paramArrayOfSID[i];
    }
  }
}