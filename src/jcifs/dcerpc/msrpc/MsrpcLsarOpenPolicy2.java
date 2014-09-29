package jcifs.dcerpc.msrpc;

public class MsrpcLsarOpenPolicy2 extends lsarpc.LsarOpenPolicy2
{
  public MsrpcLsarOpenPolicy2(String paramString, int paramInt, LsaPolicyHandle paramLsaPolicyHandle)
  {
    super(paramString, new lsarpc.LsarObjectAttributes(), paramInt, paramLsaPolicyHandle);
    this.object_attributes.length = 24;
    lsarpc.LsarQosInfo localLsarQosInfo = new lsarpc.LsarQosInfo();
    localLsarQosInfo.length = 12;
    localLsarQosInfo.impersonation_level = 2;
    localLsarQosInfo.context_mode = 1;
    localLsarQosInfo.effective_only = 0;
    this.object_attributes.security_quality_of_service = localLsarQosInfo;
    this.ptype = 0;
    this.flags = 3;
  }
}