package org.bouncycastle2.crypto.tls;

import java.io.IOException;

public class LegacyTlsAuthentication
  implements TlsAuthentication
{
  protected CertificateVerifyer verifyer;

  public LegacyTlsAuthentication(CertificateVerifyer paramCertificateVerifyer)
  {
    this.verifyer = paramCertificateVerifyer;
  }

  public TlsCredentials getClientCredentials(CertificateRequest paramCertificateRequest)
    throws IOException
  {
    return null;
  }

  public void notifyServerCertificate(Certificate paramCertificate)
    throws IOException
  {
    if (!this.verifyer.isValid(paramCertificate.getCerts()))
      throw new TlsFatalAlert((short)90);
  }
}