package org.bouncycastle2.asn1;

import java.util.Date;

public class ASN1GeneralizedTime extends DERGeneralizedTime
{
  public ASN1GeneralizedTime(String paramString)
  {
    super(paramString);
  }

  public ASN1GeneralizedTime(Date paramDate)
  {
    super(paramDate);
  }

  ASN1GeneralizedTime(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }
}