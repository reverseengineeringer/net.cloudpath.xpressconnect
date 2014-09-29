package org.bouncycastle2.crypto.tls;

import java.security.SecureRandom;

class TlsClientContextImpl
  implements TlsClientContext
{
  private SecureRandom secureRandom;
  private SecurityParameters securityParameters;
  private Object userObject = null;

  TlsClientContextImpl(SecureRandom paramSecureRandom, SecurityParameters paramSecurityParameters)
  {
    this.secureRandom = paramSecureRandom;
    this.securityParameters = paramSecurityParameters;
  }

  public SecureRandom getSecureRandom()
  {
    return this.secureRandom;
  }

  public SecurityParameters getSecurityParameters()
  {
    return this.securityParameters;
  }

  public Object getUserObject()
  {
    return this.userObject;
  }

  public void setUserObject(Object paramObject)
  {
    this.userObject = paramObject;
  }
}