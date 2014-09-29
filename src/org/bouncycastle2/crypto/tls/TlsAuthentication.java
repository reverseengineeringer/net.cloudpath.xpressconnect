package org.bouncycastle2.crypto.tls;

import java.io.IOException;

public abstract interface TlsAuthentication
{
  public abstract TlsCredentials getClientCredentials(CertificateRequest paramCertificateRequest)
    throws IOException;

  public abstract void notifyServerCertificate(Certificate paramCertificate)
    throws IOException;
}