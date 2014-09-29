package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.encodings.PKCS1Encoding;
import org.bouncycastle2.crypto.engines.RSABlindedEngine;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.crypto.util.PublicKeyFactory;

class TlsRSAKeyExchange
  implements TlsKeyExchange
{
  protected TlsClientContext context;
  protected byte[] premasterSecret;
  protected RSAKeyParameters rsaServerPublicKey = null;
  protected AsymmetricKeyParameter serverPublicKey = null;

  TlsRSAKeyExchange(TlsClientContext paramTlsClientContext)
  {
    this.context = paramTlsClientContext;
  }

  public void generateClientKeyExchange(OutputStream paramOutputStream)
    throws IOException
  {
    this.premasterSecret = new byte[48];
    this.context.getSecureRandom().nextBytes(this.premasterSecret);
    TlsUtils.writeVersion(this.premasterSecret, 0);
    PKCS1Encoding localPKCS1Encoding = new PKCS1Encoding(new RSABlindedEngine());
    localPKCS1Encoding.init(true, new ParametersWithRandom(this.rsaServerPublicKey, this.context.getSecureRandom()));
    try
    {
      byte[] arrayOfByte = localPKCS1Encoding.processBlock(this.premasterSecret, 0, this.premasterSecret.length);
      TlsUtils.writeUint24(2 + arrayOfByte.length, paramOutputStream);
      TlsUtils.writeOpaque16(arrayOfByte, paramOutputStream);
      return;
    }
    catch (InvalidCipherTextException localInvalidCipherTextException)
    {
    }
    throw new TlsFatalAlert((short)80);
  }

  public byte[] generatePremasterSecret()
    throws IOException
  {
    byte[] arrayOfByte = this.premasterSecret;
    this.premasterSecret = null;
    return arrayOfByte;
  }

  public void processClientCredentials(TlsCredentials paramTlsCredentials)
    throws IOException
  {
    if (!(paramTlsCredentials instanceof TlsSignerCredentials))
      throw new TlsFatalAlert((short)80);
  }

  public void processServerCertificate(Certificate paramCertificate)
    throws IOException
  {
    X509CertificateStructure localX509CertificateStructure = paramCertificate.certs[0];
    SubjectPublicKeyInfo localSubjectPublicKeyInfo = localX509CertificateStructure.getSubjectPublicKeyInfo();
    try
    {
      this.serverPublicKey = PublicKeyFactory.createKey(localSubjectPublicKeyInfo);
      if (this.serverPublicKey.isPrivate())
        throw new TlsFatalAlert((short)80);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw new TlsFatalAlert((short)43);
    }
    this.rsaServerPublicKey = validateRSAPublicKey((RSAKeyParameters)this.serverPublicKey);
    TlsUtils.validateKeyUsage(localX509CertificateStructure, 32);
  }

  public void processServerKeyExchange(InputStream paramInputStream)
    throws IOException
  {
    throw new TlsFatalAlert((short)10);
  }

  public void skipClientCredentials()
    throws IOException
  {
  }

  public void skipServerCertificate()
    throws IOException
  {
    throw new TlsFatalAlert((short)10);
  }

  public void skipServerKeyExchange()
    throws IOException
  {
  }

  public void validateCertificateRequest(CertificateRequest paramCertificateRequest)
    throws IOException
  {
    short[] arrayOfShort = paramCertificateRequest.getCertificateTypes();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfShort.length)
        return;
      switch (arrayOfShort[i])
      {
      default:
        throw new TlsFatalAlert((short)47);
      case 1:
      case 2:
      case 64:
      }
    }
  }

  protected RSAKeyParameters validateRSAPublicKey(RSAKeyParameters paramRSAKeyParameters)
    throws IOException
  {
    if (!paramRSAKeyParameters.getExponent().isProbablePrime(2))
      throw new TlsFatalAlert((short)47);
    return paramRSAKeyParameters;
  }
}