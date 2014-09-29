package org.bouncycastle2.asn1.x500.style;

import org.bouncycastle2.asn1.x500.RDN;
import org.bouncycastle2.asn1.x500.X500Name;

public class BCStrictStyle extends BCStyle
{
  public boolean areEqual(X500Name paramX500Name1, X500Name paramX500Name2)
  {
    RDN[] arrayOfRDN1 = paramX500Name1.getRDNs();
    RDN[] arrayOfRDN2 = paramX500Name2.getRDNs();
    if (arrayOfRDN1.length != arrayOfRDN2.length)
      return false;
    for (int i = 0; ; i++)
    {
      if (i == arrayOfRDN1.length)
        return true;
      if (rdnAreEqual(arrayOfRDN1[i], arrayOfRDN2[i]))
        break;
    }
  }
}