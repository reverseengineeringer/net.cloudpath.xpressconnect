package org.bouncycastle2.jce;

import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.x509.TBSCertList;
import org.bouncycastle2.asn1.x509.TBSCertificateStructure;

public class PrincipalUtil
{
  public static X509Principal getIssuerX509Principal(X509CRL paramX509CRL)
    throws CRLException
  {
    try
    {
      X509Principal localX509Principal = new X509Principal(TBSCertList.getInstance(ASN1Object.fromByteArray(paramX509CRL.getTBSCertList())).getIssuer());
      return localX509Principal;
    }
    catch (IOException localIOException)
    {
      throw new CRLException(localIOException.toString());
    }
  }

  public static X509Principal getIssuerX509Principal(X509Certificate paramX509Certificate)
    throws CertificateEncodingException
  {
    try
    {
      X509Principal localX509Principal = new X509Principal(TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(paramX509Certificate.getTBSCertificate())).getIssuer());
      return localX509Principal;
    }
    catch (IOException localIOException)
    {
      throw new CertificateEncodingException(localIOException.toString());
    }
  }

  public static X509Principal getSubjectX509Principal(X509Certificate paramX509Certificate)
    throws CertificateEncodingException
  {
    try
    {
      X509Principal localX509Principal = new X509Principal(TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(paramX509Certificate.getTBSCertificate())).getSubject());
      return localX509Principal;
    }
    catch (IOException localIOException)
    {
      throw new CertificateEncodingException(localIOException.toString());
    }
  }
}