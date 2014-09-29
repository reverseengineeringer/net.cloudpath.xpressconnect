package org.bouncycastle2.jce.provider.asymmetric.ec;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x9.X9IntegerConverter;
import org.bouncycastle2.crypto.BasicAgreement;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DerivationFunction;
import org.bouncycastle2.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle2.crypto.agreement.ECDHCBasicAgreement;
import org.bouncycastle2.crypto.agreement.ECMQVBasicAgreement;
import org.bouncycastle2.crypto.agreement.kdf.DHKDFParameters;
import org.bouncycastle2.crypto.agreement.kdf.ECDHKEKGenerator;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.crypto.params.MQVPrivateParameters;
import org.bouncycastle2.crypto.params.MQVPublicParameters;
import org.bouncycastle2.jce.interfaces.ECPrivateKey;
import org.bouncycastle2.jce.interfaces.ECPublicKey;
import org.bouncycastle2.jce.interfaces.MQVPrivateKey;
import org.bouncycastle2.jce.interfaces.MQVPublicKey;
import org.bouncycastle2.math.ec.ECPoint;

public class KeyAgreement extends KeyAgreementSpi
{
  private static final Hashtable algorithms;
  private static final X9IntegerConverter converter = new X9IntegerConverter();
  private BasicAgreement agreement;
  private String kaAlgorithm;
  private DerivationFunction kdf;
  private ECDomainParameters parameters;
  private BigInteger result;

  static
  {
    algorithms = new Hashtable();
    Integer localInteger1 = new Integer(128);
    Integer localInteger2 = new Integer(192);
    Integer localInteger3 = new Integer(256);
    algorithms.put(NISTObjectIdentifiers.id_aes128_CBC.getId(), localInteger1);
    algorithms.put(NISTObjectIdentifiers.id_aes192_CBC.getId(), localInteger2);
    algorithms.put(NISTObjectIdentifiers.id_aes256_CBC.getId(), localInteger3);
    algorithms.put(NISTObjectIdentifiers.id_aes128_wrap.getId(), localInteger1);
    algorithms.put(NISTObjectIdentifiers.id_aes192_wrap.getId(), localInteger2);
    algorithms.put(NISTObjectIdentifiers.id_aes256_wrap.getId(), localInteger3);
    algorithms.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), localInteger2);
  }

  protected KeyAgreement(String paramString, BasicAgreement paramBasicAgreement, DerivationFunction paramDerivationFunction)
  {
    this.kaAlgorithm = paramString;
    this.agreement = paramBasicAgreement;
    this.kdf = paramDerivationFunction;
  }

  private byte[] bigIntToBytes(BigInteger paramBigInteger)
  {
    return converter.integerToBytes(paramBigInteger, converter.getByteLength(this.parameters.getG().getX()));
  }

  private static String getSimpleName(Class paramClass)
  {
    String str = paramClass.getName();
    return str.substring(1 + str.lastIndexOf('.'));
  }

  private void initFromKey(Key paramKey)
    throws InvalidKeyException
  {
    if ((this.agreement instanceof ECMQVBasicAgreement))
    {
      if (!(paramKey instanceof MQVPrivateKey))
        throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPrivateKey.class) + " for initialisation");
      MQVPrivateKey localMQVPrivateKey = (MQVPrivateKey)paramKey;
      ECPrivateKeyParameters localECPrivateKeyParameters2 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter(localMQVPrivateKey.getStaticPrivateKey());
      ECPrivateKeyParameters localECPrivateKeyParameters3 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter(localMQVPrivateKey.getEphemeralPrivateKey());
      PublicKey localPublicKey = localMQVPrivateKey.getEphemeralPublicKey();
      ECPublicKeyParameters localECPublicKeyParameters = null;
      if (localPublicKey != null)
        localECPublicKeyParameters = (ECPublicKeyParameters)ECUtil.generatePublicKeyParameter(localMQVPrivateKey.getEphemeralPublicKey());
      MQVPrivateParameters localMQVPrivateParameters = new MQVPrivateParameters(localECPrivateKeyParameters2, localECPrivateKeyParameters3, localECPublicKeyParameters);
      this.parameters = localECPrivateKeyParameters2.getParameters();
      this.agreement.init(localMQVPrivateParameters);
      return;
    }
    if (!(paramKey instanceof ECPrivateKey))
      throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPrivateKey.class) + " for initialisation");
    ECPrivateKeyParameters localECPrivateKeyParameters1 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)paramKey);
    this.parameters = localECPrivateKeyParameters1.getParameters();
    this.agreement.init(localECPrivateKeyParameters1);
  }

  protected Key engineDoPhase(Key paramKey, boolean paramBoolean)
    throws InvalidKeyException, IllegalStateException
  {
    if (this.parameters == null)
      throw new IllegalStateException(this.kaAlgorithm + " not initialised.");
    if (!paramBoolean)
      throw new IllegalStateException(this.kaAlgorithm + " can only be between two parties.");
    MQVPublicKey localMQVPublicKey;
    if ((this.agreement instanceof ECMQVBasicAgreement))
    {
      if (!(paramKey instanceof MQVPublicKey))
        throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPublicKey.class) + " for doPhase");
      localMQVPublicKey = (MQVPublicKey)paramKey;
    }
    for (Object localObject = new MQVPublicParameters((ECPublicKeyParameters)ECUtil.generatePublicKeyParameter(localMQVPublicKey.getStaticKey()), (ECPublicKeyParameters)ECUtil.generatePublicKeyParameter(localMQVPublicKey.getEphemeralKey())); ; localObject = ECUtil.generatePublicKeyParameter((PublicKey)paramKey))
    {
      this.result = this.agreement.calculateAgreement((CipherParameters)localObject);
      return null;
      if (!(paramKey instanceof ECPublicKey))
        throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPublicKey.class) + " for doPhase");
    }
  }

  protected int engineGenerateSecret(byte[] paramArrayOfByte, int paramInt)
    throws IllegalStateException, ShortBufferException
  {
    byte[] arrayOfByte = engineGenerateSecret();
    if (paramArrayOfByte.length - paramInt < arrayOfByte.length)
      throw new ShortBufferException(this.kaAlgorithm + " key agreement: need " + arrayOfByte.length + " bytes");
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
    return arrayOfByte.length;
  }

  protected SecretKey engineGenerateSecret(String paramString)
    throws NoSuchAlgorithmException
  {
    Object localObject = bigIntToBytes(this.result);
    if (this.kdf != null)
    {
      if (!algorithms.containsKey(paramString))
        throw new NoSuchAlgorithmException("unknown algorithm encountered: " + paramString);
      int i = ((Integer)algorithms.get(paramString)).intValue();
      DHKDFParameters localDHKDFParameters = new DHKDFParameters(new DERObjectIdentifier(paramString), i, (byte[])localObject);
      byte[] arrayOfByte = new byte[i / 8];
      this.kdf.init(localDHKDFParameters);
      this.kdf.generateBytes(arrayOfByte, 0, arrayOfByte.length);
      localObject = arrayOfByte;
    }
    return new SecretKeySpec((byte[])localObject, paramString);
  }

  protected byte[] engineGenerateSecret()
    throws IllegalStateException
  {
    if (this.kdf != null)
      throw new UnsupportedOperationException("KDF can only be used when algorithm is known");
    return bigIntToBytes(this.result);
  }

  protected void engineInit(Key paramKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    initFromKey(paramKey);
  }

  protected void engineInit(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    initFromKey(paramKey);
  }

  public static class DH extends KeyAgreement
  {
    public DH()
    {
      super(new ECDHBasicAgreement(), null);
    }
  }

  public static class DHC extends KeyAgreement
  {
    public DHC()
    {
      super(new ECDHCBasicAgreement(), null);
    }
  }

  public static class DHwithSHA1KDF extends KeyAgreement
  {
    public DHwithSHA1KDF()
    {
      super(new ECDHBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
    }
  }

  public static class MQV extends KeyAgreement
  {
    public MQV()
    {
      super(new ECMQVBasicAgreement(), null);
    }
  }

  public static class MQVwithSHA1KDF extends KeyAgreement
  {
    public MQVwithSHA1KDF()
    {
      super(new ECMQVBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
    }
  }
}