package jcifs.dcerpc.msrpc;

public class MsrpcSamrConnect2 extends samr.SamrConnect2
{
  public MsrpcSamrConnect2(String paramString, int paramInt, SamrPolicyHandle paramSamrPolicyHandle)
  {
    super(paramString, paramInt, paramSamrPolicyHandle);
    this.ptype = 0;
    this.flags = 3;
  }
}