package jcifs.dcerpc.msrpc;

import java.io.IOException;
import jcifs.dcerpc.DcerpcHandle;
import jcifs.dcerpc.rpc.policy_handle;
import jcifs.dcerpc.rpc.sid_t;
import jcifs.smb.SmbException;

public class SamrDomainHandle extends rpc.policy_handle
{
  public SamrDomainHandle(DcerpcHandle paramDcerpcHandle, SamrPolicyHandle paramSamrPolicyHandle, int paramInt, rpc.sid_t paramsid_t)
    throws IOException
  {
    MsrpcSamrOpenDomain localMsrpcSamrOpenDomain = new MsrpcSamrOpenDomain(paramSamrPolicyHandle, paramInt, paramsid_t, this);
    paramDcerpcHandle.sendrecv(localMsrpcSamrOpenDomain);
    if (localMsrpcSamrOpenDomain.retval != 0)
      throw new SmbException(localMsrpcSamrOpenDomain.retval, false);
  }

  public void close()
    throws IOException
  {
  }
}