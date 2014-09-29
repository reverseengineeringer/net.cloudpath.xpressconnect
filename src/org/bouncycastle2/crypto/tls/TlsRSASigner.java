package org.bouncycastle2.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.digests.NullDigest;
import org.bouncycastle2.crypto.encodings.PKCS1Encoding;
import org.bouncycastle2.crypto.engines.RSABlindedEngine;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.crypto.signers.GenericSigner;

class TlsRSASigner
  implements TlsSigner
{
  public byte[] calculateRawSignature(SecureRandom paramSecureRandom, AsymmetricKeyParameter paramAsymmetricKeyParameter, byte[] paramArrayOfByte)
    throws CryptoException
  {
    GenericSigner localGenericSigner = new GenericSigner(new PKCS1Encoding(new RSABlindedEngine()), new NullDigest());
    localGenericSigner.init(true, new ParametersWithRandom(paramAsymmetricKeyParameter, paramSecureRandom));
    localGenericSigner.update(paramArrayOfByte, 0, paramArrayOfByte.length);
    return localGenericSigner.generateSignature();
  }

  public Signer createVerifyer(AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    GenericSigner localGenericSigner = new GenericSigner(new PKCS1Encoding(new RSABlindedEngine()), new CombinedHash());
    localGenericSigner.init(false, paramAsymmetricKeyParameter);
    return localGenericSigner;
  }

  public boolean isValidPublicKey(AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    return ((paramAsymmetricKeyParameter instanceof RSAKeyParameters)) && (!paramAsymmetricKeyParameter.isPrivate());
  }
}