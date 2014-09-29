package org.bouncycastle2.crypto.tls;

public abstract interface TlsCredentials
{
  public abstract Certificate getCertificate();
}