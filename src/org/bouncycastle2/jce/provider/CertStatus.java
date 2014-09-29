package org.bouncycastle2.jce.provider;

import java.util.Date;

class CertStatus
{
  public static final int UNDETERMINED = 12;
  public static final int UNREVOKED = 11;
  int certStatus = 11;
  Date revocationDate = null;

  public int getCertStatus()
  {
    return this.certStatus;
  }

  public Date getRevocationDate()
  {
    return this.revocationDate;
  }

  public void setCertStatus(int paramInt)
  {
    this.certStatus = paramInt;
  }

  public void setRevocationDate(Date paramDate)
  {
    this.revocationDate = paramDate;
  }
}