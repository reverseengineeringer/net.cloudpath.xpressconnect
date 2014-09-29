package org.bouncycastle2.jce.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.x509.CertificatePair;
import org.bouncycastle2.x509.X509CertificatePair;
import org.bouncycastle2.x509.X509StreamParserSpi;
import org.bouncycastle2.x509.util.StreamParsingException;

public class X509CertPairParser extends X509StreamParserSpi
{
  private InputStream currentStream = null;

  private X509CertificatePair readDERCrossCertificatePair(InputStream paramInputStream)
    throws IOException, CertificateParsingException
  {
    return new X509CertificatePair(CertificatePair.getInstance((ASN1Sequence)new ASN1InputStream(paramInputStream, ProviderUtil.getReadLimit(paramInputStream)).readObject()));
  }

  public void engineInit(InputStream paramInputStream)
  {
    this.currentStream = paramInputStream;
    if (!this.currentStream.markSupported())
      this.currentStream = new BufferedInputStream(this.currentStream);
  }

  public Object engineRead()
    throws StreamParsingException
  {
    try
    {
      this.currentStream.mark(10);
      if (this.currentStream.read() == -1)
        return null;
      this.currentStream.reset();
      X509CertificatePair localX509CertificatePair = readDERCrossCertificatePair(this.currentStream);
      return localX509CertificatePair;
    }
    catch (Exception localException)
    {
      throw new StreamParsingException(localException.toString(), localException);
    }
  }

  public Collection engineReadAll()
    throws StreamParsingException
  {
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      X509CertificatePair localX509CertificatePair = (X509CertificatePair)engineRead();
      if (localX509CertificatePair == null)
        return localArrayList;
      localArrayList.add(localX509CertificatePair);
    }
  }
}