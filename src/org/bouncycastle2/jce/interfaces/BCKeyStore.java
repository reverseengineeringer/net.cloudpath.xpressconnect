package org.bouncycastle2.jce.interfaces;

import java.security.SecureRandom;

public abstract interface BCKeyStore
{
  public abstract void setRandom(SecureRandom paramSecureRandom);
}