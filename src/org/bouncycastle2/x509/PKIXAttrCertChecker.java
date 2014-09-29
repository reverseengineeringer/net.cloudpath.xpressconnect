package org.bouncycastle2.x509;

import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.util.Collection;
import java.util.Set;

public abstract class PKIXAttrCertChecker
  implements Cloneable
{
  public abstract void check(X509AttributeCertificate paramX509AttributeCertificate, CertPath paramCertPath1, CertPath paramCertPath2, Collection paramCollection)
    throws CertPathValidatorException;

  public abstract Object clone();

  public abstract Set getSupportedExtensions();
}