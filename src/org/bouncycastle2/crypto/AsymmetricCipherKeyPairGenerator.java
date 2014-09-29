package org.bouncycastle2.crypto;

public abstract interface AsymmetricCipherKeyPairGenerator
{
  public abstract AsymmetricCipherKeyPair generateKeyPair();

  public abstract void init(KeyGenerationParameters paramKeyGenerationParameters);
}