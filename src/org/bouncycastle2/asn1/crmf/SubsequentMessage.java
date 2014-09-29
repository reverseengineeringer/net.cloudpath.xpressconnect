package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.DERInteger;

public class SubsequentMessage extends DERInteger
{
  public static final SubsequentMessage challengeResp = new SubsequentMessage(1);
  public static final SubsequentMessage encrCert = new SubsequentMessage(0);

  private SubsequentMessage(int paramInt)
  {
    super(paramInt);
  }

  public static SubsequentMessage valueOf(int paramInt)
  {
    if (paramInt == 0)
      return encrCert;
    if (paramInt == 1)
      return challengeResp;
    throw new IllegalArgumentException("unknown value: " + paramInt);
  }
}