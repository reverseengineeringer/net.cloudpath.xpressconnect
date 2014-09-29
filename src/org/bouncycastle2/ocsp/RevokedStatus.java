package org.bouncycastle2.ocsp;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.ocsp.RevokedInfo;
import org.bouncycastle2.asn1.x509.CRLReason;

public class RevokedStatus
  implements CertificateStatus
{
  RevokedInfo info;

  public RevokedStatus(Date paramDate, int paramInt)
  {
    this.info = new RevokedInfo(new DERGeneralizedTime(paramDate), new CRLReason(paramInt));
  }

  public RevokedStatus(RevokedInfo paramRevokedInfo)
  {
    this.info = paramRevokedInfo;
  }

  public int getRevocationReason()
  {
    if (this.info.getRevocationReason() == null)
      throw new IllegalStateException("attempt to get a reason where none is available");
    return this.info.getRevocationReason().getValue().intValue();
  }

  public Date getRevocationTime()
  {
    try
    {
      Date localDate = this.info.getRevocationTime().getDate();
      return localDate;
    }
    catch (ParseException localParseException)
    {
      throw new IllegalStateException("ParseException:" + localParseException.getMessage());
    }
  }

  public boolean hasRevocationReason()
  {
    return this.info.getRevocationReason() != null;
  }
}