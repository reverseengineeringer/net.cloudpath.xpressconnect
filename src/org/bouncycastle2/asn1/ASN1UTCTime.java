package org.bouncycastle2.asn1;

import java.util.Date;

public class ASN1UTCTime extends DERUTCTime
{
  public ASN1UTCTime(String paramString)
  {
    super(paramString);
  }

  public ASN1UTCTime(Date paramDate)
  {
    super(paramDate);
  }

  ASN1UTCTime(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }
}