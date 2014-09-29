package org.bouncycastle2.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.DSA;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.digests.NullDigest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.signers.DSADigestSigner;

abstract class TlsDSASigner
  implements TlsSigner
{
  public byte[] calculateRawSignature(SecureRandom paramSecureRandom, AsymmetricKeyParameter paramAsymmetricKeyParameter, byte[] paramArrayOfByte)
    throws CryptoException
  {
    DSADigestSigner localDSADigestSigner = new DSADigestSigner(createDSAImpl(), new NullDigest());
    localDSADigestSigner.init(true, new ParametersWithRandom(paramAsymmetricKeyParameter, paramSecureRandom));
    localDSADigestSigner.update(paramArrayOfByte, 16, 20);
    return localDSADigestSigner.generateSignature();
  }

  protected abstract DSA createDSAImpl();

  public Signer createVerifyer(AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    DSADigestSigner localDSADigestSigner = new DSADigestSigner(createDSAImpl(), new SHA1Digest());
    localDSADigestSigner.init(false, paramAsymmetricKeyParameter);
    return localDSADigestSigner;
  }
}