package jcifs.dcerpc.msrpc;

import jcifs.dcerpc.rpc.sid_t;

public class MsrpcSamrOpenDomain extends samr.SamrOpenDomain
{
  public MsrpcSamrOpenDomain(SamrPolicyHandle paramSamrPolicyHandle, int paramInt, rpc.sid_t paramsid_t, SamrDomainHandle paramSamrDomainHandle)
  {
    super(paramSamrPolicyHandle, paramInt, paramsid_t, paramSamrDomainHandle);
    this.ptype = 0;
    this.flags = 3;
  }
}