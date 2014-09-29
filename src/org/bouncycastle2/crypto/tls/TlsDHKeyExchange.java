package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.agreement.DHBasicAgreement;
import org.bouncycastle2.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;
import org.bouncycastle2.util.BigIntegers;

class TlsDHKeyExchange
  implements TlsKeyExchange
{
  protected static final BigInteger ONE = BigInteger.valueOf(1L);
  protected static final BigInteger TWO = BigInteger.valueOf(2L);
  protected TlsAgreementCredentials agreementCredentials;
  protected TlsClientContext context;
  protected DHPrivateKeyParameters dhAgreeClientPrivateKey = null;
  protected DHPublicKeyParameters dhAgreeServerPublicKey = null;
  protected int keyExchange;
  protected AsymmetricKeyParameter serverPublicKey = null;
  protected TlsSigner tlsSigner;

  TlsDHKeyExchange(TlsClientContext paramTlsClientContext, int paramInt)
  {
    switch (paramInt)
    {
    case 4:
    case 6:
    case 8:
    default:
      throw new IllegalArgumentException("unsupported key exchange algorithm");
    case 7:
    case 9:
      this.tlsSigner = null;
    case 5:
    case 3:
    }
    while (true)
    {
      this.context = paramTlsClientContext;
      this.keyExchange = paramInt;
      return;
      this.tlsSigner = new TlsRSASigner();
      continue;
      this.tlsSigner = new TlsDSSSigner();
    }
  }

  protected boolean areCompatibleParameters(DHParameters paramDHParameters1, DHParameters paramDHParameters2)
  {
    return (paramDHParameters1.getP().equals(paramDHParameters2.getP())) && (paramDHParameters1.getG().equals(paramDHParameters2.getG()));
  }

  protected byte[] calculateDHBasicAgreement(DHPublicKeyParameters paramDHPublicKeyParameters, DHPrivateKeyParameters paramDHPrivateKeyParameters)
  {
    DHBasicAgreement localDHBasicAgreement = new DHBasicAgreement();
    localDHBasicAgreement.init(this.dhAgreeClientPrivateKey);
    return BigIntegers.asUnsignedByteArray(localDHBasicAgreement.calculateAgreement(this.dhAgreeServerPublicKey));
  }

  public void generateClientKeyExchange(OutputStream paramOutputStream)
    throws IOException
  {
    if (this.agreementCredentials != null)
    {
      TlsUtils.writeUint24(0, paramOutputStream);
      return;
    }
    generateEphemeralClientKeyExchange(this.dhAgreeServerPublicKey.getParameters(), paramOutputStream);
  }

  protected AsymmetricCipherKeyPair generateDHKeyPair(DHParameters paramDHParameters)
  {
    DHBasicKeyPairGenerator localDHBasicKeyPairGenerator = new DHBasicKeyPairGenerator();
    localDHBasicKeyPairGenerator.init(new DHKeyGenerationParameters(this.context.getSecureRandom(), paramDHParameters));
    return localDHBasicKeyPairGenerator.generateKeyPair();
  }

  protected void generateEphemeralClientKeyExchange(DHParameters paramDHParameters, OutputStream paramOutputStream)
    throws IOException
  {
    AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = generateDHKeyPair(paramDHParameters);
    this.dhAgreeClientPrivateKey = ((DHPrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate());
    byte[] arrayOfByte = BigIntegers.asUnsignedByteArray(((DHPublicKeyParameters)localAsymmetricCipherKeyPair.getPublic()).getY());
    TlsUtils.writeUint24(2 + arrayOfByte.length, paramOutputStream);
    TlsUtils.writeOpaque16(arrayOfByte, paramOutputStream);
  }

  public byte[] generatePremasterSecret()
    throws IOException
  {
    if (this.agreementCredentials != null)
      return this.agreementCredentials.generateAgreement(this.dhAgreeServerPublicKey);
    return calculateDHBasicAgreement(this.dhAgreeServerPublicKey, this.dhAgreeClientPrivateKey);
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
    //   1: getfield 196	org/bouncycastle2/crypto/tls/Certificate:certs	[Lorg/bouncycastle2/asn1/x509/X509CertificateStructure;
    //   4: iconst_0
    //   5: aaload
    //   6: astore_2
    //   7: aload_2
    //   8: invokevirtual 202	org/bouncycastle2/asn1/x509/X509CertificateStructure:getSubjectPublicKeyInfo	()Lorg/bouncycastle2/asn1/x509/SubjectPublicKeyInfo;
    //   11: astore_3
    //   12: aload_0
    //   13: aload_3
    //   14: invokestatic 208	org/bouncycastle2/crypto/util/PublicKeyFactory:createKey	(Lorg/bouncycastle2/asn1/x509/SubjectPublicKeyInfo;)Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   17: putfield 43	org/bouncycastle2/crypto/tls/TlsDHKeyExchange:serverPublicKey	Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   20: aload_0
    //   21: getfield 56	org/bouncycastle2/crypto/tls/TlsDHKeyExchange:tlsSigner	Lorg/bouncycastle2/crypto/tls/TlsSigner;
    //   24: ifnonnull +49 -> 73
    //   27: aload_0
    //   28: aload_0
    //   29: aload_0
    //   30: getfield 43	org/bouncycastle2/crypto/tls/TlsDHKeyExchange:serverPublicKey	Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   33: checkcast 114	org/bouncycastle2/crypto/params/DHPublicKeyParameters
    //   36: invokevirtual 212	org/bouncycastle2/crypto/tls/TlsDHKeyExchange:validateDHPublicKey	(Lorg/bouncycastle2/crypto/params/DHPublicKeyParameters;)Lorg/bouncycastle2/crypto/params/DHPublicKeyParameters;
    //   39: putfield 45	org/bouncycastle2/crypto/tls/TlsDHKeyExchange:dhAgreeServerPublicKey	Lorg/bouncycastle2/crypto/params/DHPublicKeyParameters;
    //   42: aload_2
    //   43: bipush 8
    //   45: invokestatic 216	org/bouncycastle2/crypto/tls/TlsUtils:validateKeyUsage	(Lorg/bouncycastle2/asn1/x509/X509CertificateStructure;I)V
    //   48: return
    //   49: astore 4
    //   51: new 181	org/bouncycastle2/crypto/tls/TlsFatalAlert
    //   54: dup
    //   55: bipush 43
    //   57: invokespecial 184	org/bouncycastle2/crypto/tls/TlsFatalAlert:<init>	(S)V
    //   60: athrow
    //   61: astore 5
    //   63: new 181	org/bouncycastle2/crypto/tls/TlsFatalAlert
    //   66: dup
    //   67: bipush 46
    //   69: invokespecial 184	org/bouncycastle2/crypto/tls/TlsFatalAlert:<init>	(S)V
    //   72: athrow
    //   73: aload_0
    //   74: getfield 56	org/bouncycastle2/crypto/tls/TlsDHKeyExchange:tlsSigner	Lorg/bouncycastle2/crypto/tls/TlsSigner;
    //   77: aload_0
    //   78: getfield 43	org/bouncycastle2/crypto/tls/TlsDHKeyExchange:serverPublicKey	Lorg/bouncycastle2/crypto/params/AsymmetricKeyParameter;
    //   81: invokeinterface 222 2 0
    //   86: ifne +13 -> 99
    //   89: new 181	org/bouncycastle2/crypto/tls/TlsFatalAlert
    //   92: dup
    //   93: bipush 46
    //   95: invokespecial 184	org/bouncycastle2/crypto/tls/TlsFatalAlert:<init>	(S)V
    //   98: athrow
    //   99: aload_2
    //   100: sipush 128
    //   103: invokestatic 216	org/bouncycastle2/crypto/tls/TlsUtils:validateKeyUsage	(Lorg/bouncycastle2/asn1/x509/X509CertificateStructure;I)V
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
      case 3:
      case 4:
      case 64:
      }
    }
  }

  protected DHPublicKeyParameters validateDHPublicKey(DHPublicKeyParameters paramDHPublicKeyParameters)
    throws IOException
  {
    BigInteger localBigInteger1 = paramDHPublicKeyParameters.getY();
    DHParameters localDHParameters = paramDHPublicKeyParameters.getParameters();
    BigInteger localBigInteger2 = localDHParameters.getP();
    BigInteger localBigInteger3 = localDHParameters.getG();
    if (!localBigInteger2.isProbablePrime(2))
      throw new TlsFatalAlert((short)47);
    if ((localBigInteger3.compareTo(TWO) < 0) || (localBigInteger3.compareTo(localBigInteger2.subtract(TWO)) > 0))
      throw new TlsFatalAlert((short)47);
    if ((localBigInteger1.compareTo(TWO) < 0) || (localBigInteger1.compareTo(localBigInteger2.subtract(ONE)) > 0))
      throw new TlsFatalAlert((short)47);
    return paramDHPublicKeyParameters;
  }
}