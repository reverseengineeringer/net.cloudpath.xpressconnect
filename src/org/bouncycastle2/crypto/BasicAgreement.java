package org.bouncycastle2.crypto;

import java.math.BigInteger;

public abstract interface BasicAgreement
{
  public abstract BigInteger calculateAgreement(CipherParameters paramCipherParameters);

  public abstract void init(CipherParameters paramCipherParameters);
}