package org.bouncycastle2.x509;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import org.bouncycastle2.util.Selector;

public class X509CertStoreSelector extends X509CertSelector
  implements Selector
{
  public static X509CertStoreSelector getInstance(X509CertSelector paramX509CertSelector)
  {
    if (paramX509CertSelector == null)
      throw new IllegalArgumentException("cannot create from null selector");
    X509CertStoreSelector localX509CertStoreSelector = new X509CertStoreSelector();
    localX509CertStoreSelector.setAuthorityKeyIdentifier(paramX509CertSelector.getAuthorityKeyIdentifier());
    localX509CertStoreSelector.setBasicConstraints(paramX509CertSelector.getBasicConstraints());
    localX509CertStoreSelector.setCertificate(paramX509CertSelector.getCertificate());
    localX509CertStoreSelector.setCertificateValid(paramX509CertSelector.getCertificateValid());
    localX509CertStoreSelector.setMatchAllSubjectAltNames(paramX509CertSelector.getMatchAllSubjectAltNames());
    try
    {
      localX509CertStoreSelector.setPathToNames(paramX509CertSelector.getPathToNames());
      localX509CertStoreSelector.setExtendedKeyUsage(paramX509CertSelector.getExtendedKeyUsage());
      localX509CertStoreSelector.setNameConstraints(paramX509CertSelector.getNameConstraints());
      localX509CertStoreSelector.setPolicy(paramX509CertSelector.getPolicy());
      localX509CertStoreSelector.setSubjectPublicKeyAlgID(paramX509CertSelector.getSubjectPublicKeyAlgID());
      localX509CertStoreSelector.setSubjectAlternativeNames(paramX509CertSelector.getSubjectAlternativeNames());
      localX509CertStoreSelector.setIssuer(paramX509CertSelector.getIssuer());
      localX509CertStoreSelector.setKeyUsage(paramX509CertSelector.getKeyUsage());
      localX509CertStoreSelector.setPrivateKeyValid(paramX509CertSelector.getPrivateKeyValid());
      localX509CertStoreSelector.setSerialNumber(paramX509CertSelector.getSerialNumber());
      localX509CertStoreSelector.setSubject(paramX509CertSelector.getSubject());
      localX509CertStoreSelector.setSubjectKeyIdentifier(paramX509CertSelector.getSubjectKeyIdentifier());
      localX509CertStoreSelector.setSubjectPublicKey(paramX509CertSelector.getSubjectPublicKey());
      return localX509CertStoreSelector;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("error in passed in selector: " + localIOException);
    }
  }

  public Object clone()
  {
    return (X509CertStoreSelector)super.clone();
  }

  public boolean match(Object paramObject)
  {
    if (!(paramObject instanceof X509Certificate))
      return false;
    return super.match((X509Certificate)paramObject);
  }

  public boolean match(Certificate paramCertificate)
  {
    return match(paramCertificate);
  }
}