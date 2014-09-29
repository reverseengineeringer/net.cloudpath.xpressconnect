package jcifs.dcerpc.msrpc;

public class MsrpcSamrOpenAlias extends samr.SamrOpenAlias
{
  public MsrpcSamrOpenAlias(SamrDomainHandle paramSamrDomainHandle, int paramInt1, int paramInt2, SamrAliasHandle paramSamrAliasHandle)
  {
    super(paramSamrDomainHandle, paramInt1, paramInt2, paramSamrAliasHandle);
    this.ptype = 0;
    this.flags = 3;
  }
}