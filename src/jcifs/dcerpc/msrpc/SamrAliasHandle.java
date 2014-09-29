package jcifs.dcerpc.msrpc;

import java.io.IOException;
import jcifs.dcerpc.DcerpcHandle;
import jcifs.dcerpc.rpc.policy_handle;
import jcifs.smb.SmbException;

public class SamrAliasHandle extends rpc.policy_handle
{
  public SamrAliasHandle(DcerpcHandle paramDcerpcHandle, SamrDomainHandle paramSamrDomainHandle, int paramInt1, int paramInt2)
    throws IOException
  {
    MsrpcSamrOpenAlias localMsrpcSamrOpenAlias = new MsrpcSamrOpenAlias(paramSamrDomainHandle, paramInt1, paramInt2, this);
    paramDcerpcHandle.sendrecv(localMsrpcSamrOpenAlias);
    if (localMsrpcSamrOpenAlias.retval != 0)
      throw new SmbException(localMsrpcSamrOpenAlias.retval, false);
  }

  public void close()
    throws IOException
  {
  }
}