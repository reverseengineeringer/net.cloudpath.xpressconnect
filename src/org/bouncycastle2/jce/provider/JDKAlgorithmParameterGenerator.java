package org.bouncycastle2.jce.provider;

import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import javax.crypto.spec.DHGenParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import org.bouncycastle2.crypto.generators.DHParametersGenerator;
import org.bouncycastle2.crypto.generators.DSAParametersGenerator;
import org.bouncycastle2.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle2.crypto.generators.GOST3410ParametersGenerator;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.GOST3410Parameters;
import org.bouncycastle2.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle2.jce.spec.GOST3410PublicKeyParameterSetSpec;

public abstract class JDKAlgorithmParameterGenerator extends AlgorithmParameterGeneratorSpi
{
  protected SecureRandom random;
  protected int strength = 1024;

  protected void engineInit(int paramInt, SecureRandom paramSecureRandom)
  {
    this.strength = paramInt;
    this.random = paramSecureRandom;
  }

  public static class DES extends JDKAlgorithmParameterGenerator
  {
    protected AlgorithmParameters engineGenerateParameters()
    {
      byte[] arrayOfByte = new byte[8];
      if (this.random == null)
        this.random = new SecureRandom();
      this.random.nextBytes(arrayOfByte);
      try
      {
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("DES", BouncyCastleProvider.PROVIDER_NAME);
        localAlgorithmParameters.init(new IvParameterSpec(arrayOfByte));
        return localAlgorithmParameters;
      }
      catch (Exception localException)
      {
        throw new RuntimeException(localException.getMessage());
      }
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DES parameter generation.");
    }
  }

  public static class DH extends JDKAlgorithmParameterGenerator
  {
    private int l = 0;

    protected AlgorithmParameters engineGenerateParameters()
    {
      DHParametersGenerator localDHParametersGenerator = new DHParametersGenerator();
      if (this.random != null)
        localDHParametersGenerator.init(this.strength, 20, this.random);
      while (true)
      {
        DHParameters localDHParameters = localDHParametersGenerator.generateParameters();
        try
        {
          AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("DH", BouncyCastleProvider.PROVIDER_NAME);
          localAlgorithmParameters.init(new DHParameterSpec(localDHParameters.getP(), localDHParameters.getG(), this.l));
          return localAlgorithmParameters;
          localDHParametersGenerator.init(this.strength, 20, new SecureRandom());
        }
        catch (Exception localException)
        {
          throw new RuntimeException(localException.getMessage());
        }
      }
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if (!(paramAlgorithmParameterSpec instanceof DHGenParameterSpec))
        throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
      DHGenParameterSpec localDHGenParameterSpec = (DHGenParameterSpec)paramAlgorithmParameterSpec;
      this.strength = localDHGenParameterSpec.getPrimeSize();
      this.l = localDHGenParameterSpec.getExponentSize();
      this.random = paramSecureRandom;
    }
  }

  public static class DSA extends JDKAlgorithmParameterGenerator
  {
    protected AlgorithmParameters engineGenerateParameters()
    {
      DSAParametersGenerator localDSAParametersGenerator = new DSAParametersGenerator();
      if (this.random != null)
        localDSAParametersGenerator.init(this.strength, 20, this.random);
      while (true)
      {
        DSAParameters localDSAParameters = localDSAParametersGenerator.generateParameters();
        try
        {
          AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("DSA", BouncyCastleProvider.PROVIDER_NAME);
          localAlgorithmParameters.init(new DSAParameterSpec(localDSAParameters.getP(), localDSAParameters.getQ(), localDSAParameters.getG()));
          return localAlgorithmParameters;
          localDSAParametersGenerator.init(this.strength, 20, new SecureRandom());
        }
        catch (Exception localException)
        {
          throw new RuntimeException(localException.getMessage());
        }
      }
    }

    protected void engineInit(int paramInt, SecureRandom paramSecureRandom)
    {
      if ((paramInt < 512) || (paramInt > 1024) || (paramInt % 64 != 0))
        throw new InvalidParameterException("strength must be from 512 - 1024 and a multiple of 64");
      this.strength = paramInt;
      this.random = paramSecureRandom;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DSA parameter generation.");
    }
  }

  public static class ElGamal extends JDKAlgorithmParameterGenerator
  {
    private int l = 0;

    protected AlgorithmParameters engineGenerateParameters()
    {
      ElGamalParametersGenerator localElGamalParametersGenerator = new ElGamalParametersGenerator();
      if (this.random != null)
        localElGamalParametersGenerator.init(this.strength, 20, this.random);
      while (true)
      {
        ElGamalParameters localElGamalParameters = localElGamalParametersGenerator.generateParameters();
        try
        {
          AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("ElGamal", BouncyCastleProvider.PROVIDER_NAME);
          localAlgorithmParameters.init(new DHParameterSpec(localElGamalParameters.getP(), localElGamalParameters.getG(), this.l));
          return localAlgorithmParameters;
          localElGamalParametersGenerator.init(this.strength, 20, new SecureRandom());
        }
        catch (Exception localException)
        {
          throw new RuntimeException(localException.getMessage());
        }
      }
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if (!(paramAlgorithmParameterSpec instanceof DHGenParameterSpec))
        throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
      DHGenParameterSpec localDHGenParameterSpec = (DHGenParameterSpec)paramAlgorithmParameterSpec;
      this.strength = localDHGenParameterSpec.getPrimeSize();
      this.l = localDHGenParameterSpec.getExponentSize();
      this.random = paramSecureRandom;
    }
  }

  public static class GOST3410 extends JDKAlgorithmParameterGenerator
  {
    protected AlgorithmParameters engineGenerateParameters()
    {
      GOST3410ParametersGenerator localGOST3410ParametersGenerator = new GOST3410ParametersGenerator();
      if (this.random != null)
        localGOST3410ParametersGenerator.init(this.strength, 2, this.random);
      while (true)
      {
        GOST3410Parameters localGOST3410Parameters = localGOST3410ParametersGenerator.generateParameters();
        try
        {
          AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("GOST3410", BouncyCastleProvider.PROVIDER_NAME);
          localAlgorithmParameters.init(new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(localGOST3410Parameters.getP(), localGOST3410Parameters.getQ(), localGOST3410Parameters.getA())));
          return localAlgorithmParameters;
          localGOST3410ParametersGenerator.init(this.strength, 2, new SecureRandom());
        }
        catch (Exception localException)
        {
          throw new RuntimeException(localException.getMessage());
        }
      }
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for GOST3410 parameter generation.");
    }
  }

  public static class RC2 extends JDKAlgorithmParameterGenerator
  {
    RC2ParameterSpec spec = null;

    protected AlgorithmParameters engineGenerateParameters()
    {
      if (this.spec == null)
      {
        byte[] arrayOfByte = new byte[8];
        if (this.random == null)
          this.random = new SecureRandom();
        this.random.nextBytes(arrayOfByte);
        try
        {
          AlgorithmParameters localAlgorithmParameters2 = AlgorithmParameters.getInstance("RC2", BouncyCastleProvider.PROVIDER_NAME);
          localAlgorithmParameters2.init(new IvParameterSpec(arrayOfByte));
          return localAlgorithmParameters2;
        }
        catch (Exception localException2)
        {
          throw new RuntimeException(localException2.getMessage());
        }
      }
      try
      {
        AlgorithmParameters localAlgorithmParameters1 = AlgorithmParameters.getInstance("RC2", BouncyCastleProvider.PROVIDER_NAME);
        localAlgorithmParameters1.init(this.spec);
        return localAlgorithmParameters1;
      }
      catch (Exception localException1)
      {
        throw new RuntimeException(localException1.getMessage());
      }
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      if ((paramAlgorithmParameterSpec instanceof RC2ParameterSpec))
      {
        this.spec = ((RC2ParameterSpec)paramAlgorithmParameterSpec);
        return;
      }
      throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC2 parameter generation.");
    }
  }
}