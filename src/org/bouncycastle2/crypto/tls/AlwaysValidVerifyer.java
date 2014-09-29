package org.bouncycastle2.crypto.tls;

import org.bouncycastle2.asn1.x509.X509CertificateStructure;

public class AlwaysValidVerifyer
  implements CertificateVerifyer
{
  public boolean isValid(X509CertificateStructure[] paramArrayOfX509CertificateStructure)
  {
    return true;
  }
}