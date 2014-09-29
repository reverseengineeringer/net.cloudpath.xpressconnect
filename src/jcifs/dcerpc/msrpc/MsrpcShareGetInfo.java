package jcifs.dcerpc.msrpc;

import java.io.IOException;
import jcifs.smb.ACE;
import jcifs.smb.SecurityDescriptor;

public class MsrpcShareGetInfo extends srvsvc.ShareGetInfo
{
  public MsrpcShareGetInfo(String paramString1, String paramString2)
  {
    super(paramString1, paramString2, 502, new srvsvc.ShareInfo502());
    this.ptype = 0;
    this.flags = 3;
  }

  public ACE[] getSecurity()
    throws IOException
  {
    srvsvc.ShareInfo502 localShareInfo502 = (srvsvc.ShareInfo502)this.info;
    if (localShareInfo502.security_descriptor != null)
      return new SecurityDescriptor(localShareInfo502.security_descriptor, 0, localShareInfo502.sd_size).aces;
    return null;
  }
}