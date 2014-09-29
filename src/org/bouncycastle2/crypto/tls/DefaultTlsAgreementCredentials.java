package org.bouncycastle2.crypto.tls;

import org.bouncycastle2.crypto.BasicAgreement;
import org.bouncycastle2.crypto.agreement.DHBasicAgreement;
import org.bouncycastle2.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.util.BigIntegers;

public class DefaultTlsAgreementCredentials
  implements TlsAgreementCredentials
{
  protected BasicAgreement basicAgreement;
  protected Certificate clientCert;
  protected AsymmetricKeyParameter clientPrivateKey;

  public DefaultTlsAgreementCredentials(Certificate paramCertificate, AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    if (paramCertificate == null)
      throw new IllegalArgumentException("'clientCertificate' cannot be null");
    if (paramCertificate.certs.length == 0)
      throw new IllegalArgumentException("'clientCertificate' cannot be empty");
    if (paramAsymmetricKeyParameter == null)
      throw new IllegalArgumentException("'clientPrivateKey' cannot be null");
    if (!paramAsymmetricKeyParameter.isPrivate())
      throw new IllegalArgumentException("'clientPrivateKey' must be private");
    if ((paramAsymmetricKeyParameter instanceof DHPrivateKeyParameters));
    for (this.basicAgreement = new DHBasicAgreement(); ; this.basicAgreement = new ECDHBasicAgreement())
    {
      this.clientCert = paramCertificate;
      this.clientPrivateKey = paramAsymmetricKeyParameter;
      return;
      if (!(paramAsymmetricKeyParameter instanceof ECPrivateKeyParameters))
        break;
    }
    throw new IllegalArgumentException("'clientPrivateKey' type not supported: " + paramAsymmetricKeyParameter.getClass().getName());
  }

  public byte[] generateAgreement(AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    this.basicAgreement.init(this.clientPrivateKey);
    return BigIntegers.asUnsignedByteArray(this.basicAgreement.calculateAgreement(paramAsymmetricKeyParameter));
  }

  public Certificate getCertificate()
  {
    return this.clientCert;
  }
}