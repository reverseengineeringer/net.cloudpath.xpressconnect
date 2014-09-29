package org.bouncycastle2.crypto;

public class AsymmetricCipherKeyPair
{
  private CipherParameters privateParam;
  private CipherParameters publicParam;

  public AsymmetricCipherKeyPair(CipherParameters paramCipherParameters1, CipherParameters paramCipherParameters2)
  {
    this.publicParam = paramCipherParameters1;
    this.privateParam = paramCipherParameters2;
  }

  public CipherParameters getPrivate()
  {
    return this.privateParam;
  }

  public CipherParameters getPublic()
  {
    return this.publicParam;
  }
}