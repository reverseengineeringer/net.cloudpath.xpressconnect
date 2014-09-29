package jcifs.dcerpc.msrpc;

import java.io.IOException;
import jcifs.dcerpc.DcerpcHandle;
import jcifs.dcerpc.rpc.policy_handle;
import jcifs.smb.SmbException;

public class LsaPolicyHandle extends rpc.policy_handle
{
  public LsaPolicyHandle(DcerpcHandle paramDcerpcHandle, String paramString, int paramInt)
    throws IOException
  {
    if (paramString == null)
      paramString = "\\\\";
    MsrpcLsarOpenPolicy2 localMsrpcLsarOpenPolicy2 = new MsrpcLsarOpenPolicy2(paramString, paramInt, this);
    paramDcerpcHandle.sendrecv(localMsrpcLsarOpenPolicy2);
    if (localMsrpcLsarOpenPolicy2.retval != 0)
      throw new SmbException(localMsrpcLsarOpenPolicy2.retval, false);
  }

  public void close()
    throws IOException
  {
  }
}