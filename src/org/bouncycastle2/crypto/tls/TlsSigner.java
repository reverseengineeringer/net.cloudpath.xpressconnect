package org.bouncycastle2.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;

abstract interface TlsSigner
{
  public abstract byte[] calculateRawSignature(SecureRandom paramSecureRandom, AsymmetricKeyParameter paramAsymmetricKeyParameter, byte[] paramArrayOfByte)
    throws CryptoException;

  public abstract Signer createVerifyer(AsymmetricKeyParameter paramAsymmetricKeyParameter);

  public abstract boolean isValidPublicKey(AsymmetricKeyParameter paramAsymmetricKeyParameter);
}