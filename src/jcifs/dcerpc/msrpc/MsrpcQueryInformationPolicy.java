package jcifs.dcerpc.msrpc;

import jcifs.dcerpc.ndr.NdrObject;

public class MsrpcQueryInformationPolicy extends lsarpc.LsarQueryInformationPolicy
{
  public MsrpcQueryInformationPolicy(LsaPolicyHandle paramLsaPolicyHandle, short paramShort, NdrObject paramNdrObject)
  {
    super(paramLsaPolicyHandle, paramShort, paramNdrObject);
    this.ptype = 0;
    this.flags = 3;
  }
}