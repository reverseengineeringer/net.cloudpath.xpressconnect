package org.bouncycastle2.x509;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.x509.CertificatePair;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.jce.provider.X509CertificateObject;

public class X509CertificatePair
{
  private X509Certificate forward;
  private X509Certificate reverse;

  public X509CertificatePair(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2)
  {
    this.forward = paramX509Certificate1;
    this.reverse = paramX509Certificate2;
  }

  public X509CertificatePair(CertificatePair paramCertificatePair)
    throws CertificateParsingException
  {
    if (paramCertificatePair.getForward() != null)
      this.forward = new X509CertificateObject(paramCertificatePair.getForward());
    if (paramCertificatePair.getReverse() != null)
      this.reverse = new X509CertificateObject(paramCertificatePair.getReverse());
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == null);
    label86: label96: 
    while (true)
    {
      return false;
      if ((paramObject instanceof X509CertificatePair))
      {
        X509CertificatePair localX509CertificatePair = (X509CertificatePair)paramObject;
        boolean bool1 = true;
        boolean bool2 = true;
        if (this.forward != null)
        {
          bool2 = this.forward.equals(localX509CertificatePair.forward);
          if (this.reverse == null)
            break label86;
          bool1 = this.reverse.equals(localX509CertificatePair.reverse);
        }
        while (true)
        {
          if ((!bool2) || (!bool1))
            break label96;
          return true;
          if (localX509CertificatePair.forward == null)
            break;
          bool2 = false;
          break;
          if (localX509CertificatePair.reverse != null)
            bool1 = false;
        }
      }
    }
  }

  public byte[] getEncoded()
    throws CertificateEncodingException
  {
    X509CertificateStructure localX509CertificateStructure1;
    X509CertificateStructure localX509CertificateStructure2;
    try
    {
      X509Certificate localX509Certificate1 = this.forward;
      localX509CertificateStructure1 = null;
      if (localX509Certificate1 != null)
      {
        localX509CertificateStructure1 = X509CertificateStructure.getInstance(new ASN1InputStream(this.forward.getEncoded()).readObject());
        if (localX509CertificateStructure1 == null)
          throw new CertificateEncodingException("unable to get encoding for forward");
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new ExtCertificateEncodingException(localIllegalArgumentException.toString(), localIllegalArgumentException);
      X509Certificate localX509Certificate2 = this.reverse;
      localX509CertificateStructure2 = null;
      if (localX509Certificate2 != null)
      {
        localX509CertificateStructure2 = X509CertificateStructure.getInstance(new ASN1InputStream(this.reverse.getEncoded()).readObject());
        if (localX509CertificateStructure2 == null)
          throw new CertificateEncodingException("unable to get encoding for reverse");
      }
    }
    catch (IOException localIOException)
    {
      throw new ExtCertificateEncodingException(localIOException.toString(), localIOException);
    }
    byte[] arrayOfByte = new CertificatePair(localX509CertificateStructure1, localX509CertificateStructure2).getDEREncoded();
    return arrayOfByte;
  }

  public X509Certificate getForward()
  {
    return this.forward;
  }

  public X509Certificate getReverse()
  {
    return this.reverse;
  }

  public int hashCode()
  {
    int i = -1;
    if (this.forward != null)
      i ^= this.forward.hashCode();
    if (this.reverse != null)
      i = i * 17 ^ this.reverse.hashCode();
    return i;
  }
}