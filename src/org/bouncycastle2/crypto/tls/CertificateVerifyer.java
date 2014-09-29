package org.bouncycastle2.crypto.tls;

import org.bouncycastle2.asn1.x509.X509CertificateStructure;

public abstract interface CertificateVerifyer
{
  public abstract boolean isValid(X509CertificateStructure[] paramArrayOfX509CertificateStructure);
}