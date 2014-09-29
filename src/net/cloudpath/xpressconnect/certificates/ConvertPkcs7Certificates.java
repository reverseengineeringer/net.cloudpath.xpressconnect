package net.cloudpath.xpressconnect.certificates;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

public class ConvertPkcs7Certificates
{
  private Collection certCollection = null;
  private String incertdata;

  public ConvertPkcs7Certificates(String paramString)
    throws Exception
  {
    if (paramString == null)
      throw new Exception("Input data is null.");
    this.incertdata = paramString;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.incertdata.getBytes());
    this.certCollection = CertificateFactory.getInstance("X.509").generateCertificates(localByteArrayInputStream);
  }

  public X509Certificate findCertificateByCn(String paramString)
  {
    Object localObject = null;
    BigInteger localBigInteger = BigInteger.ZERO;
    X509Certificate localX509Certificate = null;
    if (paramString == null);
    Collection localCollection;
    do
    {
      return localX509Certificate;
      localCollection = this.certCollection;
      localX509Certificate = null;
    }
    while (localCollection == null);
    String str = "CN=" + paramString;
    Iterator localIterator = this.certCollection.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        break label127;
      localX509Certificate = (X509Certificate)localIterator.next();
      if (localX509Certificate.getSubjectDN().getName().contains(str))
        break;
      if (localX509Certificate.getSerialNumber().compareTo(localBigInteger) == 1)
      {
        localObject = localX509Certificate;
        localBigInteger = localX509Certificate.getSerialNumber();
      }
    }
    label127: return localObject;
  }

  public String toString()
  {
    String str = "";
    Iterator localIterator = this.certCollection.iterator();
    while (localIterator.hasNext())
    {
      X509Certificate localX509Certificate = (X509Certificate)localIterator.next();
      str = str + localX509Certificate.toString();
    }
    return str;
  }
}