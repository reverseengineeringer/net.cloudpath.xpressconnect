package org.bouncycastle2.crypto.tls;

import java.security.SecureRandom;

public abstract interface TlsClientContext
{
  public abstract SecureRandom getSecureRandom();

  public abstract SecurityParameters getSecurityParameters();

  public abstract Object getUserObject();

  public abstract void setUserObject(Object paramObject);
}