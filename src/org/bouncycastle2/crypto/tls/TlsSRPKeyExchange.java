package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.agreement.srp.SRP6Client;
import org.bouncycastle2.crypto.agreement.srp.SRP6Util;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.io.SignerInputStream;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.util.PublicKeyFactory;
import org.bouncycastle2.util.BigIntegers;

class TlsSRPKeyExchange
  implements TlsKeyExchange
{
  protected BigInteger B = null;
  protected TlsClientContext context;
  protected byte[] identity;
  protected int keyExchange;
  protected byte[] password;
  protected byte[] s = null;
  protected AsymmetricKeyParameter serverPublicKey = null;
  protected SRP6Client srpClient = new SRP6Client();
  protected TlsSigner tlsSigner;

  TlsSRPKeyExchange(TlsClientContext paramTlsClientContext, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("unsupported key exchange algorithm");
    case 21:
      this.tlsSigner = null;
    case 23:
    case 22:
    }
    while (true)
    {
      this.context = paramTlsClientContext;
      this.keyExchange = paramInt;
      this.identity = paramArrayOfByte1;
      this.password = paramArrayOfByte2;
      return;
      this.tlsSigner = new TlsRSASigner();
      continue;
      this.tlsSigner = new TlsDSSSigner();
    }
  }

  public void generateClientKeyExchange(OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = BigIntegers.asUnsignedByteArray(this.srpClient.generateClientCredentials(this.s, this.identity, this.password));
    TlsUtils.writeUint24(2 + arrayOfByte.length, paramOutputStream);
    TlsUtils.writeOpaque16(arrayOfByte, paramOutputStream);
  }

  public byte[] generatePremasterSecret()
    throws IOException
  {
    try
    {
      byte[] arrayOfByte = BigIntegers.asUnsignedByteArray(this.srpClient.calculateSecret(this.B));
      return arrayOfByte;
    }
    catch (CryptoException localCryptoException)
    {
    }
    throw new TlsFatalAlert((short)47);
  }

  protected Signer initSigner(TlsSigner paramTlsSigner, SecurityParameters paramSecurityParameters)
  {
    Signer localSigner = paramTlsSigner.createVerifyer(this.serverPublicKey);
    localSigner.update(paramSecurityParameters.clientRandom, 0, paramSecurityParameters.clientRandom.length);
    localSigner.update(paramSecurityParameters.serverRandom, 0, paramSecurityParameters.serverRandom.length);
    return localSigner;
  }

  public void processClientCredentials(TlsCredentials paramTlsCredentials)
    throws IOException
  {
    throw new TlsFatalAlert((short)80);
  }

  public void processServerCertificate(Certificate paramCertificate)
    throws IOException
  {
    if (this.tlsSigner == null)
      throw new TlsFatalAlert((short)10);
    X509CertificateStructure localX509CertificateStructure = paramCertificate.certs[0];
    SubjectPublicKeyInfo localSubjectPublicKeyInfo = localX509CertificateStructure.getSubjectPublicKeyInfo();
    try
    {
      this.serverPublicKey = PublicKeyFactory.createKey(localSubjectPublicKeyInfo);
      if (!this.tlsSigner.isValidPublicKey(this.serverPublicKey))
        throw new TlsFatalAlert((short)46);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw new TlsFatalAlert((short)43);
    }
    TlsUtils.validateKeyUsage(localX509CertificateStructure, 128);
  }

  public void processServerKeyExchange(InputStream paramInputStream)
    throws IOException
  {
    SecurityParameters localSecurityParameters = this.context.getSecurityParameters();
    Object localObject = paramInputStream;
    TlsSigner localTlsSigner = this.tlsSigner;
    Signer localSigner = null;
    if (localTlsSigner != null)
    {
      localSigner = initSigner(this.tlsSigner, localSecurityParameters);
      localObject = new SignerInputStream(paramInputStream, localSigner);
    }
    byte[] arrayOfByte1 = TlsUtils.readOpaque16((InputStream)localObject);
    byte[] arrayOfByte2 = TlsUtils.readOpaque16((InputStream)localObject);
    byte[] arrayOfByte3 = TlsUtils.readOpaque8((InputStream)localObject);
    byte[] arrayOfByte4 = TlsUtils.readOpaque16((InputStream)localObject);
    if ((localSigner != null) && (!localSigner.verifySignature(TlsUtils.readOpaque16(paramInputStream))))
      throw new TlsFatalAlert((short)42);
    BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte1);
    BigInteger localBigInteger2 = new BigInteger(1, arrayOfByte2);
    this.s = arrayOfByte3;
    try
    {
      this.B = SRP6Util.validatePublicValue(localBigInteger1, new BigInteger(1, arrayOfByte4));
      this.srpClient.init(localBigInteger1, localBigInteger2, new SHA1Digest(), this.context.getSecureRandom());
      return;
    }
    catch (CryptoException localCryptoException)
    {
    }
    throw new TlsFatalAlert((short)47);
  }

  public void skipClientCredentials()
    throws IOException
  {
  }

  public void skipServerCertificate()
    throws IOException
  {
    if (this.tlsSigner != null)
      throw new TlsFatalAlert((short)10);
  }

  public void skipServerKeyExchange()
    throws IOException
  {
    throw new TlsFatalAlert((short)10);
  }

  public void validateCertificateRequest(CertificateRequest paramCertificateRequest)
    throws IOException
  {
    throw new TlsFatalAlert((short)10);
  }
}