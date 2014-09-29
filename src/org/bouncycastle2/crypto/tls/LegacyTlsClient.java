package org.bouncycastle2.crypto.tls;

import java.io.IOException;

public class LegacyTlsClient extends DefaultTlsClient
{
  protected CertificateVerifyer verifyer;

  public LegacyTlsClient(CertificateVerifyer paramCertificateVerifyer)
  {
    this.verifyer = paramCertificateVerifyer;
  }

  public TlsAuthentication getAuthentication()
    throws IOException
  {
    return new LegacyTlsAuthentication(this.verifyer);
  }
}