package org.bouncycastle2.crypto.agreement;

import java.math.BigInteger;
import org.bouncycastle2.crypto.BasicAgreement;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.math.ec.ECFieldElement;
import org.bouncycastle2.math.ec.ECPoint;

public class ECDHCBasicAgreement
  implements BasicAgreement
{
  ECPrivateKeyParameters key;

  public BigInteger calculateAgreement(CipherParameters paramCipherParameters)
  {
    ECPublicKeyParameters localECPublicKeyParameters = (ECPublicKeyParameters)paramCipherParameters;
    ECDomainParameters localECDomainParameters = localECPublicKeyParameters.getParameters();
    return localECPublicKeyParameters.getQ().multiply(localECDomainParameters.getH().multiply(this.key.getD())).getX().toBigInteger();
  }

  public void init(CipherParameters paramCipherParameters)
  {
    this.key = ((ECPrivateKeyParameters)paramCipherParameters);
  }
}