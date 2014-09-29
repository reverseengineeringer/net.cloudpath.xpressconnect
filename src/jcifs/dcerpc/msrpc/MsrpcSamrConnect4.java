package jcifs.dcerpc.msrpc;

public class MsrpcSamrConnect4 extends samr.SamrConnect4
{
  public MsrpcSamrConnect4(String paramString, int paramInt, SamrPolicyHandle paramSamrPolicyHandle)
  {
    super(paramString, 2, paramInt, paramSamrPolicyHandle);
    this.ptype = 0;
    this.flags = 3;
  }
}