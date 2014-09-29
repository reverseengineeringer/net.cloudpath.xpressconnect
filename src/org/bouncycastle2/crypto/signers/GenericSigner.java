package org.bouncycastle2.crypto.signers;

import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.util.Arrays;

public class GenericSigner
  implements Signer
{
  private final Digest digest;
  private final AsymmetricBlockCipher engine;
  private boolean forSigning;

  public GenericSigner(AsymmetricBlockCipher paramAsymmetricBlockCipher, Digest paramDigest)
  {
    this.engine = paramAsymmetricBlockCipher;
    this.digest = paramDigest;
  }

  public byte[] generateSignature()
    throws CryptoException, DataLengthException
  {
    if (!this.forSigning)
      throw new IllegalStateException("GenericSigner not initialised for signature generation.");
    byte[] arrayOfByte = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte, 0);
    return this.engine.processBlock(arrayOfByte, 0, arrayOfByte.length);
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.forSigning = paramBoolean;
    if ((paramCipherParameters instanceof ParametersWithRandom));
    for (AsymmetricKeyParameter localAsymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)paramCipherParameters).getParameters(); (paramBoolean) && (!localAsymmetricKeyParameter.isPrivate()); localAsymmetricKeyParameter = (AsymmetricKeyParameter)paramCipherParameters)
      throw new IllegalArgumentException("signing requires private key");
    if ((!paramBoolean) && (localAsymmetricKeyParameter.isPrivate()))
      throw new IllegalArgumentException("verification requires public key");
    reset();
    this.engine.init(paramBoolean, paramCipherParameters);
  }

  public void reset()
  {
    this.digest.reset();
  }

  public void update(byte paramByte)
  {
    this.digest.update(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public boolean verifySignature(byte[] paramArrayOfByte)
  {
    if (this.forSigning)
      throw new IllegalStateException("GenericSigner not initialised for verification");
    byte[] arrayOfByte = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte, 0);
    try
    {
      boolean bool = Arrays.constantTimeAreEqual(this.engine.processBlock(paramArrayOfByte, 0, paramArrayOfByte.length), arrayOfByte);
      return bool;
    }
    catch (Exception localException)
    {
    }
    return false;
  }
}