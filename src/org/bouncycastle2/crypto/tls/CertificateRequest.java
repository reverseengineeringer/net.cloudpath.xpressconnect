package org.bouncycastle2.crypto.tls;

import java.util.Vector;

public class CertificateRequest
{
  private Vector certificateAuthorities;
  private short[] certificateTypes;

  public CertificateRequest(short[] paramArrayOfShort, Vector paramVector)
  {
    this.certificateTypes = paramArrayOfShort;
    this.certificateAuthorities = paramVector;
  }

  public Vector getCertificateAuthorities()
  {
    return this.certificateAuthorities;
  }

  public short[] getCertificateTypes()
  {
    return this.certificateTypes;
  }
}