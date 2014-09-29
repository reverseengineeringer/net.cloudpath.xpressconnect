package org.bouncycastle2.crypto.agreement;

import java.math.BigInteger;
import org.bouncycastle2.crypto.BasicAgreement;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class DHBasicAgreement
  implements BasicAgreement
{
  private DHParameters dhParams;
  private DHPrivateKeyParameters key;

  public BigInteger calculateAgreement(CipherParameters paramCipherParameters)
  {
    DHPublicKeyParameters localDHPublicKeyParameters = (DHPublicKeyParameters)paramCipherParameters;
    if (!localDHPublicKeyParameters.getParameters().equals(this.dhParams))
      throw new IllegalArgumentException("Diffie-Hellman public key has wrong parameters.");
    return localDHPublicKeyParameters.getY().modPow(this.key.getX(), this.dhParams.getP());
  }

  public void init(CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof ParametersWithRandom));
    for (AsymmetricKeyParameter localAsymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)paramCipherParameters).getParameters(); !(localAsymmetricKeyParameter instanceof DHPrivateKeyParameters); localAsymmetricKeyParameter = (AsymmetricKeyParameter)paramCipherParameters)
      throw new IllegalArgumentException("DHEngine expects DHPrivateKeyParameters");
    this.key = ((DHPrivateKeyParameters)localAsymmetricKeyParameter);
    this.dhParams = this.key.getParameters();
  }
}