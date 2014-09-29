package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle2.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.math.ec.ECPoint;
import org.bouncycastle2.util.BigIntegers;

class TlsECDHKeyExchange
  implements TlsKeyExchange
{
  protected TlsAgreementCredentials agreementCredentials;
  protected TlsClientContext context;
  protected ECPrivateKeyParameters ecAgreeClientPrivateKey = null;
  protected ECPublicKeyParameters ecAgreeServerPublicKey;
  protected int keyExchange;
  protected AsymmetricKeyParameter serverPublicKey;
  protected TlsSigner tlsSigner;

  TlsECDHKeyExchange(TlsClientContext paramTlsClientContext, int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("unsupported key exchange algorithm");
    case 19:
      this.tlsSigner = new TlsRSASigner();
    case 17:
    case 16:
    case 18:
    }
    while (true)
    {
      this.context = paramTlsClientContext;
      this.keyExchange = paramInt;
      return;
      this.tlsSigner = new TlsECDSASigner();
      continue;
      this.tlsSigner = null;
    }
  }

  protected boolean areOnSameCurve(ECDomainParameters paramECDomainParameters1, ECDomainParameters paramECDomainParameters2)
  {
    return (paramECDomainParameters1.getCurve().equals(paramECDomainParameters2.getCurve())) && (paramECDomainParameters1.getG().equals(paramECDomainParameters2.getG())) && (paramECDomainParameters1.getN().equals(paramECDomainParameters2.getN())) && (paramECDomainParameters1.getH().equals(paramECDomainParameters2.getH()));
  }

  protected byte[] calculateECDHBasicAgreement(ECPublicKeyParameters paramECPublicKeyParameters, ECPrivateKeyParameters paramECPrivateKeyParameters)
  {
    ECDHBasicAgreement localECDHBasicAgreement = new ECDHBasicAgreement();
    localECDHBasicAgreement.init(paramECPrivateKeyParameters);
    return BigIntegers.asUnsignedByteArray(localECDHBasicAgreement.calculateAgreement(paramECPublicKeyParameters));
  }

  protected byte[] externalizeKey(ECPublicKeyParameters paramECPublicKeyParameters)
    throws IOException
  {
    return paramECPublicKeyParameters.getQ().getEncoded();
  }

  public void generateClientKeyExchange(OutputStream paramOutputStream)
    throws IOException
  {
    if (this.agreementCredentials != null)
    {
      TlsUtils.writeUint24(0, paramOutputStream);
      return;
    }
    generateEphemeralClientKeyExchange(this.ecAgreeServerPublicKey.getParameters(), paramOutputStream);
  }

  protected AsymmetricCipherKeyPair generateECKeyPair(ECDomainParameters paramECDomainParameters)
  {
    ECKeyPairGenerator localECKeyPairGenerator = new ECKeyPairGenerator();
    localECKeyPairGenerator.init(new ECKeyGenerationParameters(paramECDomainParameters, this.context.getSecureRandom()));
    return localECKeyPairGenerator.generateKeyPair();
  }

  protected void generateEphemeralClientKeyExchange(ECDomainParameters paramECDomainParameters, OutputStream paramOutputStream)
    throws IOException
  {
    AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = generateECKeyPair(paramECDomainParameters);
    this.ecAgreeClientPrivateKey = ((ECPrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate());
    byte[] arrayOfByte = externalizeKey((ECPublicKeyParameters)localAsymmetricCipherKeyPair.getPublic());
    TlsUtils.writeUint24(1 + arrayOfByte.length, paramOutputStream);
    TlsUtils.writeOpaque8(arrayOfByte, paramOutputStream);
  }

  public byte[] generatePremasterSecret()
    throws IOException
  {
    if (this.agreementCredentials != null)
      return this.agreementCredentials.generateAgreement(this.ecAgreeServerPublicKey);
    return calculateECDHBasicAgreement(this.ecAgreeServerPublicKey, this.ecAgreeClientPrivateKey);
  }

  public void processClientCredentials(TlsCredentials paramTlsCredentials)
    throws IOException
  {
    if ((paramTlsCredentials instanceof TlsAgreementCredentials))
      this.agreementCredentials = ((TlsAgreementCredentials)paramTlsCredentials);
    while ((paramTlsCredentials instanceof TlsSignerCredentials))
      return;
    throw new TlsFatalAlert((short)80);
  }

  // ERROR //
  public void processServerCertificate(Certificate paramCertificate)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 199	org/bouncycastle2/crypto/tls/Certificate:certs	[Lorg/bouncycastle2/asn1/x509/X509CertificateStructure;
    //   4: iconst_0
    //   5: aaload
    //   6: astore_2
    //   7: aload_2
    //   8: invokevirtual 205	org/bouncycastle2/asn1/x509/X509CertificateStructure:getSubjectPublicKeyInfo	()Lorg/bouncycastle2/asn1/x509/SubjectPublicKeyInfo;
    //   11: astore_3
    //   12: aload_0
    //   13: aload_3
    //   14: invokestatic 211	org/bouncycastle2/crypto/util/PublicKeyFactory:createKey	(Lorg/bouncycastle2/asn1/x509/SubjectPublicKeyInfo;)Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   17: putfield 213	org/bouncycastle2/crypto/tls/TlsECDHKeyExchange:serverPublicKey	Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   20: aload_0
    //   21: getfield 39	org/bouncycastle2/crypto/tls/TlsECDHKeyExchange:tlsSigner	Lorg/bouncycastle2/crypto/tls/TlsSigner;
    //   24: ifnonnull +49 -> 73
    //   27: aload_0
    //   28: aload_0
    //   29: aload_0
    //   30: getfield 213	org/bouncycastle2/crypto/tls/TlsECDHKeyExchange:serverPublicKey	Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   33: checkcast 100	org/bouncycastle2/crypto/params/ECPublicKeyParameters
    //   36: invokevirtual 217	org/bouncycastle2/crypto/tls/TlsECDHKeyExchange:validateECPublicKey	(Lorg/bouncycastle2/crypto/params/ECPublicKeyParameters;)Lorg/bouncycastle2/crypto/params/ECPublicKeyParameters;
    //   39: putfield 119	org/bouncycastle2/crypto/tls/TlsECDHKeyExchange:ecAgreeServerPublicKey	Lorg/bouncycastle2/crypto/params/ECPublicKeyParameters;
    //   42: aload_2
    //   43: bipush 8
    //   45: invokestatic 221	org/bouncycastle2/crypto/tls/TlsUtils:validateKeyUsage	(Lorg/bouncycastle2/asn1/x509/X509CertificateStructure;I)V
    //   48: return
    //   49: astore 4
    //   51: new 184	org/bouncycastle2/crypto/tls/TlsFatalAlert
    //   54: dup
    //   55: bipush 43
    //   57: invokespecial 187	org/bouncycastle2/crypto/tls/TlsFatalAlert:<init>	(S)V
    //   60: athrow
    //   61: astore 5
    //   63: new 184	org/bouncycastle2/crypto/tls/TlsFatalAlert
    //   66: dup
    //   67: bipush 46
    //   69: invokespecial 187	org/bouncycastle2/crypto/tls/TlsFatalAlert:<init>	(S)V
    //   72: athrow
    //   73: aload_0
    //   74: getfield 39	org/bouncycastle2/crypto/tls/TlsECDHKeyExchange:tlsSigner	Lorg/bouncycastle2/crypto/tls/TlsSigner;
    //   77: aload_0
    //   78: getfield 213	org/bouncycastle2/crypto/tls/TlsECDHKeyExchange:serverPublicKey	Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   81: invokeinterface 227 2 0
    //   86: ifne +13 -> 99
    //   89: new 184	org/bouncycastle2/crypto/tls/TlsFatalAlert
    //   92: dup
    //   93: bipush 46
    //   95: invokespecial 187	org/bouncycastle2/crypto/tls/TlsFatalAlert:<init>	(S)V
    //   98: athrow
    //   99: aload_2
    //   100: sipush 128
    //   103: invokestatic 221	org/bouncycastle2/crypto/tls/TlsUtils:validateKeyUsage	(Lorg/bouncycastle2/asn1/x509/X509CertificateStructure;I)V
    //   106: return
    //
    // Exception table:
    //   from	to	target	type
    //   12	20	49	java/lang/RuntimeException
    //   27	42	61	java/lang/ClassCastException
  }

  public void processServerKeyExchange(InputStream paramInputStream)
    throws IOException
  {
    throw new TlsFatalAlert((short)10);
  }

  public void skipClientCredentials()
    throws IOException
  {
    this.agreementCredentials = null;
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
      case 65:
      case 66:
      }
    }
  }

  protected ECPublicKeyParameters validateECPublicKey(ECPublicKeyParameters paramECPublicKeyParameters)
    throws IOException
  {
    return paramECPublicKeyParameters;
  }
}