package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.RSAKeyParameters;

public class DefaultTlsSignerCredentials
  implements TlsSignerCredentials
{
  protected Certificate clientCert;
  protected AsymmetricKeyParameter clientPrivateKey;
  protected TlsSigner clientSigner;
  protected TlsClientContext context;

  public DefaultTlsSignerCredentials(TlsClientContext paramTlsClientContext, Certificate paramCertificate, AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    if (paramCertificate == null)
      throw new IllegalArgumentException("'clientCertificate' cannot be null");
    if (paramCertificate.certs.length == 0)
      throw new IllegalArgumentException("'clientCertificate' cannot be empty");
    if (paramAsymmetricKeyParameter == null)
      throw new IllegalArgumentException("'clientPrivateKey' cannot be null");
    if (!paramAsymmetricKeyParameter.isPrivate())
      throw new IllegalArgumentException("'clientPrivateKey' must be private");
    if ((paramAsymmetricKeyParameter instanceof RSAKeyParameters))
      this.clientSigner = new TlsRSASigner();
    while (true)
    {
      this.context = paramTlsClientContext;
      this.clientCert = paramCertificate;
      this.clientPrivateKey = paramAsymmetricKeyParameter;
      return;
      if ((paramAsymmetricKeyParameter instanceof DSAPrivateKeyParameters))
      {
        this.clientSigner = new TlsDSSSigner();
      }
      else
      {
        if (!(paramAsymmetricKeyParameter instanceof ECPrivateKeyParameters))
          break;
        this.clientSigner = new TlsECDSASigner();
      }
    }
    throw new IllegalArgumentException("'clientPrivateKey' type not supported: " + paramAsymmetricKeyParameter.getClass().getName());
  }

  public byte[] generateCertificateSignature(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      byte[] arrayOfByte = this.clientSigner.calculateRawSignature(this.context.getSecureRandom(), this.clientPrivateKey, paramArrayOfByte);
      return arrayOfByte;
    }
    catch (CryptoException localCryptoException)
    {
    }
    throw new TlsFatalAlert((short)80);
  }

  public Certificate getCertificate()
  {
    return this.clientCert;
  }
}