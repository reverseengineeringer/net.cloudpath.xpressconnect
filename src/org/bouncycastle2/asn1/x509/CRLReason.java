package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle2.asn1.DEREnumerated;

public class CRLReason extends DEREnumerated
{
  public static final int AA_COMPROMISE = 10;
  public static final int AFFILIATION_CHANGED = 3;
  public static final int CA_COMPROMISE = 2;
  public static final int CERTIFICATE_HOLD = 6;
  public static final int CESSATION_OF_OPERATION = 5;
  public static final int KEY_COMPROMISE = 1;
  public static final int PRIVILEGE_WITHDRAWN = 9;
  public static final int REMOVE_FROM_CRL = 8;
  public static final int SUPERSEDED = 4;
  public static final int UNSPECIFIED = 0;
  public static final int aACompromise = 10;
  public static final int affiliationChanged = 3;
  public static final int cACompromise = 2;
  public static final int certificateHold = 6;
  public static final int cessationOfOperation = 5;
  public static final int keyCompromise = 1;
  public static final int privilegeWithdrawn = 9;
  private static final String[] reasonString = { "unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise" };
  public static final int removeFromCRL = 8;
  public static final int superseded = 4;
  public static final int unspecified;

  public CRLReason(int paramInt)
  {
    super(paramInt);
  }

  public CRLReason(DEREnumerated paramDEREnumerated)
  {
    super(paramDEREnumerated.getValue().intValue());
  }

  public String toString()
  {
    int i = getValue().intValue();
    if ((i < 0) || (i > 10));
    for (String str = "invalid"; ; str = reasonString[i])
      return "CRLReason: " + str;
  }
}