package org.bouncycastle2.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.RSABlindingParameters;
import org.bouncycastle2.crypto.params.RSAKeyParameters;

public class RSABlindingEngine
  implements AsymmetricBlockCipher
{
  private BigInteger blindingFactor;
  private RSACoreEngine core = new RSACoreEngine();
  private boolean forEncryption;
  private RSAKeyParameters key;

  private BigInteger blindMessage(BigInteger paramBigInteger)
  {
    return paramBigInteger.multiply(this.blindingFactor.modPow(this.key.getExponent(), this.key.getModulus())).mod(this.key.getModulus());
  }

  private BigInteger unblindMessage(BigInteger paramBigInteger)
  {
    BigInteger localBigInteger = this.key.getModulus();
    return paramBigInteger.multiply(this.blindingFactor.modInverse(localBigInteger)).mod(localBigInteger);
  }

  public int getInputBlockSize()
  {
    return this.core.getInputBlockSize();
  }

  public int getOutputBlockSize()
  {
    return this.core.getOutputBlockSize();
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof ParametersWithRandom));
    for (RSABlindingParameters localRSABlindingParameters = (RSABlindingParameters)((ParametersWithRandom)paramCipherParameters).getParameters(); ; localRSABlindingParameters = (RSABlindingParameters)paramCipherParameters)
    {
      this.core.init(paramBoolean, localRSABlindingParameters.getPublicKey());
      this.forEncryption = paramBoolean;
      this.key = localRSABlindingParameters.getPublicKey();
      this.blindingFactor = localRSABlindingParameters.getBlindingFactor();
      return;
    }
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    BigInteger localBigInteger1 = this.core.convertInput(paramArrayOfByte, paramInt1, paramInt2);
    if (this.forEncryption);
    for (BigInteger localBigInteger2 = blindMessage(localBigInteger1); ; localBigInteger2 = unblindMessage(localBigInteger1))
      return this.core.convertOutput(localBigInteger2);
  }
}