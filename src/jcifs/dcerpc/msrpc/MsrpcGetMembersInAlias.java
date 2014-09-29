package jcifs.dcerpc.msrpc;

public class MsrpcGetMembersInAlias extends samr.SamrGetMembersInAlias
{
  public MsrpcGetMembersInAlias(SamrAliasHandle paramSamrAliasHandle, lsarpc.LsarSidArray paramLsarSidArray)
  {
    super(paramSamrAliasHandle, paramLsarSidArray);
    this.sids = paramLsarSidArray;
    this.ptype = 0;
    this.flags = 3;
  }
}