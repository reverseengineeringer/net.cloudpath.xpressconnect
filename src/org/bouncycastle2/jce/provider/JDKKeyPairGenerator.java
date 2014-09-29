package org.bouncycastle2.jce.provider;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Hashtable;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle2.crypto.generators.DHParametersGenerator;
import org.bouncycastle2.crypto.generators.DSAKeyPairGenerator;
import org.bouncycastle2.crypto.generators.DSAParametersGenerator;
import org.bouncycastle2.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle2.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle2.crypto.generators.GOST3410KeyPairGenerator;
import org.bouncycastle2.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle2.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;
import org.bouncycastle2.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle2.crypto.params.GOST3410KeyGenerationParameters;
import org.bouncycastle2.crypto.params.GOST3410Parameters;
import org.bouncycastle2.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle2.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle2.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle2.jce.spec.ElGamalParameterSpec;
import org.bouncycastle2.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle2.jce.spec.GOST3410PublicKeyParameterSetSpec;

public abstract class JDKKeyPairGenerator extends KeyPairGenerator
{
  public JDKKeyPairGenerator(String paramString)
  {
    super(paramString);
  }

  public abstract KeyPair generateKeyPair();

  public abstract void initialize(int paramInt, SecureRandom paramSecureRandom);

  public static class DH extends JDKKeyPairGenerator
  {
    private static Hashtable params = new Hashtable();
    int certainty = 20;
    DHBasicKeyPairGenerator engine = new DHBasicKeyPairGenerator();
    boolean initialised = false;
    DHKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 1024;

    public DH()
    {
      super();
    }

    public KeyPair generateKeyPair()
    {
      Integer localInteger;
      if (!this.initialised)
      {
        localInteger = new Integer(this.strength);
        if (!params.containsKey(localInteger))
          break label114;
        this.param = ((DHKeyGenerationParameters)params.get(localInteger));
      }
      while (true)
      {
        this.engine.init(this.param);
        this.initialised = true;
        AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = this.engine.generateKeyPair();
        DHPublicKeyParameters localDHPublicKeyParameters = (DHPublicKeyParameters)localAsymmetricCipherKeyPair.getPublic();
        DHPrivateKeyParameters localDHPrivateKeyParameters = (DHPrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate();
        return new KeyPair(new JCEDHPublicKey(localDHPublicKeyParameters), new JCEDHPrivateKey(localDHPrivateKeyParameters));
        label114: DHParametersGenerator localDHParametersGenerator = new DHParametersGenerator();
        localDHParametersGenerator.init(this.strength, this.certainty, this.random);
        this.param = new DHKeyGenerationParameters(this.random, localDHParametersGenerator.generateParameters());
        params.put(localInteger, this.param);
      }
    }

    public void initialize(int paramInt, SecureRandom paramSecureRandom)
    {
      this.strength = paramInt;
      this.random = paramSecureRandom;
    }

    public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if (!(paramAlgorithmParameterSpec instanceof DHParameterSpec))
        throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec");
      DHParameterSpec localDHParameterSpec = (DHParameterSpec)paramAlgorithmParameterSpec;
      this.param = new DHKeyGenerationParameters(paramSecureRandom, new DHParameters(localDHParameterSpec.getP(), localDHParameterSpec.getG(), null, localDHParameterSpec.getL()));
      this.engine.init(this.param);
      this.initialised = true;
    }
  }

  public static class DSA extends JDKKeyPairGenerator
  {
    int certainty = 20;
    DSAKeyPairGenerator engine = new DSAKeyPairGenerator();
    boolean initialised = false;
    DSAKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 1024;

    public DSA()
    {
      super();
    }

    public KeyPair generateKeyPair()
    {
      if (!this.initialised)
      {
        DSAParametersGenerator localDSAParametersGenerator = new DSAParametersGenerator();
        localDSAParametersGenerator.init(this.strength, this.certainty, this.random);
        this.param = new DSAKeyGenerationParameters(this.random, localDSAParametersGenerator.generateParameters());
        this.engine.init(this.param);
        this.initialised = true;
      }
      AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = this.engine.generateKeyPair();
      DSAPublicKeyParameters localDSAPublicKeyParameters = (DSAPublicKeyParameters)localAsymmetricCipherKeyPair.getPublic();
      DSAPrivateKeyParameters localDSAPrivateKeyParameters = (DSAPrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate();
      return new KeyPair(new JDKDSAPublicKey(localDSAPublicKeyParameters), new JDKDSAPrivateKey(localDSAPrivateKeyParameters));
    }

    public void initialize(int paramInt, SecureRandom paramSecureRandom)
    {
      if ((paramInt < 512) || (paramInt > 1024) || (paramInt % 64 != 0))
        throw new InvalidParameterException("strength must be from 512 - 1024 and a multiple of 64");
      this.strength = paramInt;
      this.random = paramSecureRandom;
    }

    public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if (!(paramAlgorithmParameterSpec instanceof DSAParameterSpec))
        throw new InvalidAlgorithmParameterException("parameter object not a DSAParameterSpec");
      DSAParameterSpec localDSAParameterSpec = (DSAParameterSpec)paramAlgorithmParameterSpec;
      this.param = new DSAKeyGenerationParameters(paramSecureRandom, new DSAParameters(localDSAParameterSpec.getP(), localDSAParameterSpec.getQ(), localDSAParameterSpec.getG()));
      this.engine.init(this.param);
      this.initialised = true;
    }
  }

  public static class ElGamal extends JDKKeyPairGenerator
  {
    int certainty = 20;
    ElGamalKeyPairGenerator engine = new ElGamalKeyPairGenerator();
    boolean initialised = false;
    ElGamalKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 1024;

    public ElGamal()
    {
      super();
    }

    public KeyPair generateKeyPair()
    {
      if (!this.initialised)
      {
        ElGamalParametersGenerator localElGamalParametersGenerator = new ElGamalParametersGenerator();
        localElGamalParametersGenerator.init(this.strength, this.certainty, this.random);
        this.param = new ElGamalKeyGenerationParameters(this.random, localElGamalParametersGenerator.generateParameters());
        this.engine.init(this.param);
        this.initialised = true;
      }
      AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = this.engine.generateKeyPair();
      ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters)localAsymmetricCipherKeyPair.getPublic();
      ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate();
      return new KeyPair(new JCEElGamalPublicKey(localElGamalPublicKeyParameters), new JCEElGamalPrivateKey(localElGamalPrivateKeyParameters));
    }

    public void initialize(int paramInt, SecureRandom paramSecureRandom)
    {
      this.strength = paramInt;
      this.random = paramSecureRandom;
    }

    public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if ((!(paramAlgorithmParameterSpec instanceof ElGamalParameterSpec)) && (!(paramAlgorithmParameterSpec instanceof DHParameterSpec)))
        throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec or an ElGamalParameterSpec");
      ElGamalParameterSpec localElGamalParameterSpec;
      if ((paramAlgorithmParameterSpec instanceof ElGamalParameterSpec))
        localElGamalParameterSpec = (ElGamalParameterSpec)paramAlgorithmParameterSpec;
      DHParameterSpec localDHParameterSpec;
      for (this.param = new ElGamalKeyGenerationParameters(paramSecureRandom, new ElGamalParameters(localElGamalParameterSpec.getP(), localElGamalParameterSpec.getG())); ; this.param = new ElGamalKeyGenerationParameters(paramSecureRandom, new ElGamalParameters(localDHParameterSpec.getP(), localDHParameterSpec.getG(), localDHParameterSpec.getL())))
      {
        this.engine.init(this.param);
        this.initialised = true;
        return;
        localDHParameterSpec = (DHParameterSpec)paramAlgorithmParameterSpec;
      }
    }
  }

  public static class GOST3410 extends JDKKeyPairGenerator
  {
    GOST3410KeyPairGenerator engine = new GOST3410KeyPairGenerator();
    GOST3410ParameterSpec gost3410Params;
    boolean initialised = false;
    GOST3410KeyGenerationParameters param;
    SecureRandom random = null;
    int strength = 1024;

    public GOST3410()
    {
      super();
    }

    private void init(GOST3410ParameterSpec paramGOST3410ParameterSpec, SecureRandom paramSecureRandom)
    {
      GOST3410PublicKeyParameterSetSpec localGOST3410PublicKeyParameterSetSpec = paramGOST3410ParameterSpec.getPublicKeyParameters();
      this.param = new GOST3410KeyGenerationParameters(paramSecureRandom, new GOST3410Parameters(localGOST3410PublicKeyParameterSetSpec.getP(), localGOST3410PublicKeyParameterSetSpec.getQ(), localGOST3410PublicKeyParameterSetSpec.getA()));
      this.engine.init(this.param);
      this.initialised = true;
      this.gost3410Params = paramGOST3410ParameterSpec;
    }

    public KeyPair generateKeyPair()
    {
      if (!this.initialised)
        init(new GOST3410ParameterSpec(CryptoProObjectIdentifiers.gostR3410_94_CryptoPro_A.getId()), new SecureRandom());
      AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = this.engine.generateKeyPair();
      GOST3410PublicKeyParameters localGOST3410PublicKeyParameters = (GOST3410PublicKeyParameters)localAsymmetricCipherKeyPair.getPublic();
      GOST3410PrivateKeyParameters localGOST3410PrivateKeyParameters = (GOST3410PrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate();
      return new KeyPair(new JDKGOST3410PublicKey(localGOST3410PublicKeyParameters, this.gost3410Params), new JDKGOST3410PrivateKey(localGOST3410PrivateKeyParameters, this.gost3410Params));
    }

    public void initialize(int paramInt, SecureRandom paramSecureRandom)
    {
      this.strength = paramInt;
      this.random = paramSecureRandom;
    }

    public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if (!(paramAlgorithmParameterSpec instanceof GOST3410ParameterSpec))
        throw new InvalidAlgorithmParameterException("parameter object not a GOST3410ParameterSpec");
      init((GOST3410ParameterSpec)paramAlgorithmParameterSpec, paramSecureRandom);
    }
  }

  public static class RSA extends JDKKeyPairGenerator
  {
    static final BigInteger defaultPublicExponent = BigInteger.valueOf(65537L);
    static final int defaultTests = 12;
    RSAKeyPairGenerator engine = new RSAKeyPairGenerator();
    RSAKeyGenerationParameters param = new RSAKeyGenerationParameters(defaultPublicExponent, new SecureRandom(), 2048, 12);

    public RSA()
    {
      super();
      this.engine.init(this.param);
    }

    public KeyPair generateKeyPair()
    {
      AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = this.engine.generateKeyPair();
      RSAKeyParameters localRSAKeyParameters = (RSAKeyParameters)localAsymmetricCipherKeyPair.getPublic();
      RSAPrivateCrtKeyParameters localRSAPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)localAsymmetricCipherKeyPair.getPrivate();
      return new KeyPair(new JCERSAPublicKey(localRSAKeyParameters), new JCERSAPrivateCrtKey(localRSAPrivateCrtKeyParameters));
    }

    public void initialize(int paramInt, SecureRandom paramSecureRandom)
    {
      this.param = new RSAKeyGenerationParameters(defaultPublicExponent, paramSecureRandom, paramInt, 12);
      this.engine.init(this.param);
    }

    public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if (!(paramAlgorithmParameterSpec instanceof RSAKeyGenParameterSpec))
        throw new InvalidAlgorithmParameterException("parameter object not a RSAKeyGenParameterSpec");
      RSAKeyGenParameterSpec localRSAKeyGenParameterSpec = (RSAKeyGenParameterSpec)paramAlgorithmParameterSpec;
      this.param = new RSAKeyGenerationParameters(localRSAKeyGenParameterSpec.getPublicExponent(), paramSecureRandom, localRSAKeyGenParameterSpec.getKeysize(), 12);
      this.engine.init(this.param);
    }
  }
}