package jcifs.dcerpc.msrpc;

public class MsrpcEnumerateAliasesInDomain extends samr.SamrEnumerateAliasesInDomain
{
  public MsrpcEnumerateAliasesInDomain(SamrDomainHandle paramSamrDomainHandle, int paramInt, samr.SamrSamArray paramSamrSamArray)
  {
    super(paramSamrDomainHandle, 0, paramInt, null, 0);
    this.sam = paramSamrSamArray;
    this.ptype = 0;
    this.flags = 3;
  }
}