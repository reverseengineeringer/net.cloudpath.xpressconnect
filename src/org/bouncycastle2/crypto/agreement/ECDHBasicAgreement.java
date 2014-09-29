package org.bouncycastle2.crypto.agreement;

import java.math.BigInteger;
import org.bouncycastle2.crypto.BasicAgreement;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.math.ec.ECFieldElement;
import org.bouncycastle2.math.ec.ECPoint;

public class ECDHBasicAgreement
  implements BasicAgreement
{
  private ECPrivateKeyParameters key;

  public BigInteger calculateAgreement(CipherParameters paramCipherParameters)
  {
    return ((ECPublicKeyParameters)paramCipherParameters).getQ().multiply(this.key.getD()).getX().toBigInteger();
  }

  public void init(CipherParameters paramCipherParameters)
  {
    this.key = ((ECPrivateKeyParameters)paramCipherParameters);
  }
}