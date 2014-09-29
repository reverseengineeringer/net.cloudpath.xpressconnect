package org.bouncycastle2.jce.interfaces;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public abstract interface IESKey extends Key
{
  public abstract PrivateKey getPrivate();

  public abstract PublicKey getPublic();
}