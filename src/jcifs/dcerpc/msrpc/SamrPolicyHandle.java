package jcifs.dcerpc.msrpc;

import java.io.IOException;
import jcifs.dcerpc.DcerpcException;
import jcifs.dcerpc.DcerpcHandle;
import jcifs.dcerpc.rpc.policy_handle;

public class SamrPolicyHandle extends rpc.policy_handle
{
  public SamrPolicyHandle(DcerpcHandle paramDcerpcHandle, String paramString, int paramInt)
    throws IOException
  {
    if (paramString == null)
      paramString = "\\\\";
    MsrpcSamrConnect4 localMsrpcSamrConnect4 = new MsrpcSamrConnect4(paramString, paramInt, this);
    try
    {
      paramDcerpcHandle.sendrecv(localMsrpcSamrConnect4);
      return;
    }
    catch (DcerpcException localDcerpcException)
    {
      if (localDcerpcException.getErrorCode() != 469827586)
        throw localDcerpcException;
      paramDcerpcHandle.sendrecv(new MsrpcSamrConnect2(paramString, paramInt, this));
    }
  }

  public void close()
    throws IOException
  {
  }
}