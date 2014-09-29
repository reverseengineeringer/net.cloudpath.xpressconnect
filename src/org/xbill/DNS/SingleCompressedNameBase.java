package org.xbill.DNS;

abstract class SingleCompressedNameBase extends SingleNameBase
{
  private static final long serialVersionUID = -236435396815460677L;

  protected SingleCompressedNameBase()
  {
  }

  protected SingleCompressedNameBase(Name paramName1, int paramInt1, int paramInt2, long paramLong, Name paramName2, String paramString)
  {
    super(paramName1, paramInt1, paramInt2, paramLong, paramName2, paramString);
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    this.singleName.toWire(paramDNSOutput, paramCompression, paramBoolean);
  }
}