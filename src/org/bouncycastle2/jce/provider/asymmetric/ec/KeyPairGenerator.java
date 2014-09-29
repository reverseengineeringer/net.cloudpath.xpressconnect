package org.bouncycastle2.jce.provider.asymmetric.ec;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.util.Hashtable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle2.asn1.nist.NISTNamedCurves;
import org.bouncycastle2.asn1.sec.SECNamedCurves;
import org.bouncycastle2.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle2.asn1.x9.X962NamedCurves;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.jce.provider.JCEECPrivateKey;
import org.bouncycastle2.jce.provider.JCEECPublicKey;
import org.bouncycastle2.jce.provider.JDKKeyPairGenerator;
import org.bouncycastle2.jce.provider.ProviderUtil;
import org.bouncycastle2.jce.spec.ECNamedCurveSpec;
import org.bouncycastle2.math.ec.ECCurve;

public abstract class KeyPairGenerator extends JDKKeyPairGenerator
{
  public KeyPairGenerator(String paramString)
  {
    super(paramString);
  }

  public static class EC extends KeyPairGenerator
  {
    private static Hashtable ecParameters = new Hashtable();
    String algorithm;
    int certainty = 50;
    Object ecParams = null;
    ECKeyPairGenerator engine = new ECKeyPairGenerator();
    boolean initialised = false;
    ECKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 239;

    static
    {
      ecParameters.put(new Integer(192), new ECGenParameterSpec("prime192v1"));
      ecParameters.put(new Integer(239), new ECGenParameterSpec("prime239v1"));
      ecParameters.put(new Integer(256), new ECGenParameterSpec("prime256v1"));
      ecParameters.put(new Integer(224), new ECGenParameterSpec("P-224"));
      ecParameters.put(new Integer(384), new ECGenParameterSpec("P-384"));
      ecParameters.put(new Integer(521), new ECGenParameterSpec("P-521"));
    }

    public EC()
    {
      super();
      this.algorithm = "EC";
    }

    public EC(String paramString)
    {
      super();
      this.algorithm = paramString;
    }

    public KeyPair generateKeyPair()
    {
      if (!this.initialised)
        throw new IllegalStateException("EC Key Pair Generator not initialised");
      AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = this.engine.generateKeyPair();
      ECPublicKeyParameters localECPublicKeyParameters = (ECPublicKeyParameters)localAsymmetricCipherKeyPair.getPublic();
      ECPrivateKeyParameters localECPrivateKeyParameters = (ECPrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate();
      if ((this.ecParams instanceof org.bouncycastle2.jce.spec.ECParameterSpec))
      {
        org.bouncycastle2.jce.spec.ECParameterSpec localECParameterSpec1 = (org.bouncycastle2.jce.spec.ECParameterSpec)this.ecParams;
        JCEECPublicKey localJCEECPublicKey2 = new JCEECPublicKey(this.algorithm, localECPublicKeyParameters, localECParameterSpec1);
        return new KeyPair(localJCEECPublicKey2, new JCEECPrivateKey(this.algorithm, localECPrivateKeyParameters, localJCEECPublicKey2, localECParameterSpec1));
      }
      if (this.ecParams == null)
        return new KeyPair(new JCEECPublicKey(this.algorithm, localECPublicKeyParameters), new JCEECPrivateKey(this.algorithm, localECPrivateKeyParameters));
      java.security.spec.ECParameterSpec localECParameterSpec = (java.security.spec.ECParameterSpec)this.ecParams;
      JCEECPublicKey localJCEECPublicKey1 = new JCEECPublicKey(this.algorithm, localECPublicKeyParameters, localECParameterSpec);
      return new KeyPair(localJCEECPublicKey1, new JCEECPrivateKey(this.algorithm, localECPrivateKeyParameters, localJCEECPublicKey1, localECParameterSpec));
    }

    public void initialize(int paramInt, SecureRandom paramSecureRandom)
    {
      this.strength = paramInt;
      this.random = paramSecureRandom;
      this.ecParams = ecParameters.get(new Integer(paramInt));
      if (this.ecParams != null)
        try
        {
          initialize((ECGenParameterSpec)this.ecParams, paramSecureRandom);
          return;
        }
        catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
        {
          throw new InvalidParameterException("key size not configurable.");
        }
      throw new InvalidParameterException("unknown key size.");
    }

    public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if ((paramAlgorithmParameterSpec instanceof org.bouncycastle2.jce.spec.ECParameterSpec))
      {
        org.bouncycastle2.jce.spec.ECParameterSpec localECParameterSpec4 = (org.bouncycastle2.jce.spec.ECParameterSpec)paramAlgorithmParameterSpec;
        this.ecParams = paramAlgorithmParameterSpec;
        this.param = new ECKeyGenerationParameters(new ECDomainParameters(localECParameterSpec4.getCurve(), localECParameterSpec4.getG(), localECParameterSpec4.getN()), paramSecureRandom);
        this.engine.init(this.param);
        this.initialised = true;
        return;
      }
      if ((paramAlgorithmParameterSpec instanceof java.security.spec.ECParameterSpec))
      {
        java.security.spec.ECParameterSpec localECParameterSpec3 = (java.security.spec.ECParameterSpec)paramAlgorithmParameterSpec;
        this.ecParams = paramAlgorithmParameterSpec;
        ECCurve localECCurve2 = EC5Util.convertCurve(localECParameterSpec3.getCurve());
        this.param = new ECKeyGenerationParameters(new ECDomainParameters(localECCurve2, EC5Util.convertPoint(localECCurve2, localECParameterSpec3.getGenerator(), false), localECParameterSpec3.getOrder(), BigInteger.valueOf(localECParameterSpec3.getCofactor())), paramSecureRandom);
        this.engine.init(this.param);
        this.initialised = true;
        return;
      }
      if ((paramAlgorithmParameterSpec instanceof ECGenParameterSpec))
      {
        String str = ((ECGenParameterSpec)paramAlgorithmParameterSpec).getName();
        ECDomainParameters localECDomainParameters;
        if (this.algorithm.equals("ECGOST3410"))
        {
          localECDomainParameters = ECGOST3410NamedCurves.getByName(str);
          if (localECDomainParameters == null)
            throw new InvalidAlgorithmParameterException("unknown curve name: " + str);
        }
        X9ECParameters localX9ECParameters;
        for (this.ecParams = new ECNamedCurveSpec(str, localECDomainParameters.getCurve(), localECDomainParameters.getG(), localECDomainParameters.getN(), localECDomainParameters.getH(), localECDomainParameters.getSeed()); ; this.ecParams = new ECNamedCurveSpec(str, localX9ECParameters.getCurve(), localX9ECParameters.getG(), localX9ECParameters.getN(), localX9ECParameters.getH(), null))
        {
          java.security.spec.ECParameterSpec localECParameterSpec2 = (java.security.spec.ECParameterSpec)this.ecParams;
          ECCurve localECCurve1 = EC5Util.convertCurve(localECParameterSpec2.getCurve());
          this.param = new ECKeyGenerationParameters(new ECDomainParameters(localECCurve1, EC5Util.convertPoint(localECCurve1, localECParameterSpec2.getGenerator(), false), localECParameterSpec2.getOrder(), BigInteger.valueOf(localECParameterSpec2.getCofactor())), paramSecureRandom);
          this.engine.init(this.param);
          this.initialised = true;
          return;
          localX9ECParameters = X962NamedCurves.getByName(str);
          if (localX9ECParameters == null)
          {
            localX9ECParameters = SECNamedCurves.getByName(str);
            if (localX9ECParameters == null)
              localX9ECParameters = NISTNamedCurves.getByName(str);
            if (localX9ECParameters == null)
              localX9ECParameters = TeleTrusTNamedCurves.getByName(str);
            if (localX9ECParameters == null)
              try
              {
                DERObjectIdentifier localDERObjectIdentifier = new DERObjectIdentifier(str);
                localX9ECParameters = X962NamedCurves.getByOID(localDERObjectIdentifier);
                if (localX9ECParameters == null)
                  localX9ECParameters = SECNamedCurves.getByOID(localDERObjectIdentifier);
                if (localX9ECParameters == null)
                  localX9ECParameters = NISTNamedCurves.getByOID(localDERObjectIdentifier);
                if (localX9ECParameters == null)
                  localX9ECParameters = TeleTrusTNamedCurves.getByOID(localDERObjectIdentifier);
                if (localX9ECParameters == null)
                  throw new InvalidAlgorithmParameterException("unknown curve OID: " + str);
              }
              catch (IllegalArgumentException localIllegalArgumentException)
              {
                throw new InvalidAlgorithmParameterException("unknown curve name: " + str);
              }
          }
        }
      }
      if ((paramAlgorithmParameterSpec == null) && (ProviderUtil.getEcImplicitlyCa() != null))
      {
        org.bouncycastle2.jce.spec.ECParameterSpec localECParameterSpec1 = ProviderUtil.getEcImplicitlyCa();
        this.ecParams = paramAlgorithmParameterSpec;
        this.param = new ECKeyGenerationParameters(new ECDomainParameters(localECParameterSpec1.getCurve(), localECParameterSpec1.getG(), localECParameterSpec1.getN()), paramSecureRandom);
        this.engine.init(this.param);
        this.initialised = true;
        return;
      }
      if ((paramAlgorithmParameterSpec == null) && (ProviderUtil.getEcImplicitlyCa() == null))
        throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
      throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
    }
  }

  public static class ECDH extends KeyPairGenerator.EC
  {
    public ECDH()
    {
      super();
    }
  }

  public static class ECDHC extends KeyPairGenerator.EC
  {
    public ECDHC()
    {
      super();
    }
  }

  public static class ECDSA extends KeyPairGenerator.EC
  {
    public ECDSA()
    {
      super();
    }
  }

  public static class ECGOST3410 extends KeyPairGenerator.EC
  {
    public ECGOST3410()
    {
      super();
    }
  }

  public static class ECMQV extends KeyPairGenerator.EC
  {
    public ECMQV()
    {
      super();
    }
  }
}