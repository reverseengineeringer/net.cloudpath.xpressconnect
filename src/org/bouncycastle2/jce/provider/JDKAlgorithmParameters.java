package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import javax.crypto.spec.RC2ParameterSpec;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle2.asn1.oiw.ElGamalParameter;
import org.bouncycastle2.asn1.pkcs.DHParameter;
import org.bouncycastle2.asn1.pkcs.PBKDF2Params;
import org.bouncycastle2.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.RC2CBCParameter;
import org.bouncycastle2.asn1.pkcs.RSAESOAEPparams;
import org.bouncycastle2.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DSAParameter;
import org.bouncycastle2.jce.spec.ElGamalParameterSpec;
import org.bouncycastle2.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle2.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle2.jce.spec.IESParameterSpec;
import org.bouncycastle2.util.Arrays;

public abstract class JDKAlgorithmParameters extends AlgorithmParametersSpi
{
  protected AlgorithmParameterSpec engineGetParameterSpec(Class paramClass)
    throws InvalidParameterSpecException
  {
    if (paramClass == null)
      throw new NullPointerException("argument to getParameterSpec must not be null");
    return localEngineGetParameterSpec(paramClass);
  }

  protected boolean isASN1FormatString(String paramString)
  {
    return (paramString == null) || (paramString.equals("ASN.1"));
  }

  protected abstract AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
    throws InvalidParameterSpecException;

  public static class DH extends JDKAlgorithmParameters
  {
    DHParameterSpec currentSpec;

    protected byte[] engineGetEncoded()
    {
      DHParameter localDHParameter = new DHParameter(this.currentSpec.getP(), this.currentSpec.getG(), this.currentSpec.getL());
      try
      {
        byte[] arrayOfByte = localDHParameter.getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
      }
      throw new RuntimeException("Error encoding DHParameters");
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if (isASN1FormatString(paramString))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof DHParameterSpec))
        throw new InvalidParameterSpecException("DHParameterSpec required to initialise a Diffie-Hellman algorithm parameters object");
      this.currentSpec = ((DHParameterSpec)paramAlgorithmParameterSpec);
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        DHParameter localDHParameter = new DHParameter((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte));
        if (localDHParameter.getL() != null)
        {
          this.currentSpec = new DHParameterSpec(localDHParameter.getP(), localDHParameter.getG(), localDHParameter.getL().intValue());
          return;
        }
        this.currentSpec = new DHParameterSpec(localDHParameter.getP(), localDHParameter.getG());
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IOException("Not a valid DH Parameter encoding.");
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      throw new IOException("Not a valid DH Parameter encoding.");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameter format " + paramString);
    }

    protected String engineToString()
    {
      return "Diffie-Hellman Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == DHParameterSpec.class)
        return this.currentSpec;
      throw new InvalidParameterSpecException("unknown parameter spec passed to DH parameters object.");
    }
  }

  public static class DSA extends JDKAlgorithmParameters
  {
    DSAParameterSpec currentSpec;

    protected byte[] engineGetEncoded()
    {
      DSAParameter localDSAParameter = new DSAParameter(this.currentSpec.getP(), this.currentSpec.getQ(), this.currentSpec.getG());
      try
      {
        byte[] arrayOfByte = localDSAParameter.getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
      }
      throw new RuntimeException("Error encoding DSAParameters");
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if (isASN1FormatString(paramString))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof DSAParameterSpec))
        throw new InvalidParameterSpecException("DSAParameterSpec required to initialise a DSA algorithm parameters object");
      this.currentSpec = ((DSAParameterSpec)paramAlgorithmParameterSpec);
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        DSAParameter localDSAParameter = new DSAParameter((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte));
        this.currentSpec = new DSAParameterSpec(localDSAParameter.getP(), localDSAParameter.getQ(), localDSAParameter.getG());
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IOException("Not a valid DSA Parameter encoding.");
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      throw new IOException("Not a valid DSA Parameter encoding.");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameter format " + paramString);
    }

    protected String engineToString()
    {
      return "DSA Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == DSAParameterSpec.class)
        return this.currentSpec;
      throw new InvalidParameterSpecException("unknown parameter spec passed to DSA parameters object.");
    }
  }

  public static class ElGamal extends JDKAlgorithmParameters
  {
    ElGamalParameterSpec currentSpec;

    protected byte[] engineGetEncoded()
    {
      ElGamalParameter localElGamalParameter = new ElGamalParameter(this.currentSpec.getP(), this.currentSpec.getG());
      try
      {
        byte[] arrayOfByte = localElGamalParameter.getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
      }
      throw new RuntimeException("Error encoding ElGamalParameters");
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if ((!(paramAlgorithmParameterSpec instanceof ElGamalParameterSpec)) && (!(paramAlgorithmParameterSpec instanceof DHParameterSpec)))
        throw new InvalidParameterSpecException("DHParameterSpec required to initialise a ElGamal algorithm parameters object");
      if ((paramAlgorithmParameterSpec instanceof ElGamalParameterSpec))
      {
        this.currentSpec = ((ElGamalParameterSpec)paramAlgorithmParameterSpec);
        return;
      }
      DHParameterSpec localDHParameterSpec = (DHParameterSpec)paramAlgorithmParameterSpec;
      this.currentSpec = new ElGamalParameterSpec(localDHParameterSpec.getP(), localDHParameterSpec.getG());
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        ElGamalParameter localElGamalParameter = new ElGamalParameter((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte));
        this.currentSpec = new ElGamalParameterSpec(localElGamalParameter.getP(), localElGamalParameter.getG());
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IOException("Not a valid ElGamal Parameter encoding.");
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      throw new IOException("Not a valid ElGamal Parameter encoding.");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameter format " + paramString);
    }

    protected String engineToString()
    {
      return "ElGamal Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == ElGamalParameterSpec.class)
        return this.currentSpec;
      if (paramClass == DHParameterSpec.class)
        return new DHParameterSpec(this.currentSpec.getP(), this.currentSpec.getG());
      throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
    }
  }

  public static class GOST3410 extends JDKAlgorithmParameters
  {
    GOST3410ParameterSpec currentSpec;

    protected byte[] engineGetEncoded()
    {
      GOST3410PublicKeyAlgParameters localGOST3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.currentSpec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.currentSpec.getDigestParamSetOID()), new DERObjectIdentifier(this.currentSpec.getEncryptionParamSetOID()));
      try
      {
        byte[] arrayOfByte = localGOST3410PublicKeyAlgParameters.getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
      }
      throw new RuntimeException("Error encoding GOST3410Parameters");
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof GOST3410ParameterSpec))
        throw new InvalidParameterSpecException("GOST3410ParameterSpec required to initialise a GOST3410 algorithm parameters object");
      this.currentSpec = ((GOST3410ParameterSpec)paramAlgorithmParameterSpec);
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        this.currentSpec = GOST3410ParameterSpec.fromPublicKeyAlg(new GOST3410PublicKeyAlgParameters((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte)));
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IOException("Not a valid GOST3410 Parameter encoding.");
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      throw new IOException("Not a valid GOST3410 Parameter encoding.");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameter format " + paramString);
    }

    protected String engineToString()
    {
      return "GOST3410 Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == GOST3410PublicKeyParameterSetSpec.class)
        return this.currentSpec;
      throw new InvalidParameterSpecException("unknown parameter spec passed to GOST3410 parameters object.");
    }
  }

  public static class IES extends JDKAlgorithmParameters
  {
    IESParameterSpec currentSpec;

    protected byte[] engineGetEncoded()
    {
      try
      {
        ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
        localASN1EncodableVector.add(new DEROctetString(this.currentSpec.getDerivationV()));
        localASN1EncodableVector.add(new DEROctetString(this.currentSpec.getEncodingV()));
        localASN1EncodableVector.add(new DERInteger(this.currentSpec.getMacKeySize()));
        byte[] arrayOfByte = new DERSequence(localASN1EncodableVector).getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
      }
      throw new RuntimeException("Error encoding IESParameters");
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof IESParameterSpec))
        throw new InvalidParameterSpecException("IESParameterSpec required to initialise a IES algorithm parameters object");
      this.currentSpec = ((IESParameterSpec)paramAlgorithmParameterSpec);
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        ASN1Sequence localASN1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte);
        this.currentSpec = new IESParameterSpec(((ASN1OctetString)localASN1Sequence.getObjectAt(0)).getOctets(), ((ASN1OctetString)localASN1Sequence.getObjectAt(0)).getOctets(), ((DERInteger)localASN1Sequence.getObjectAt(0)).getValue().intValue());
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IOException("Not a valid IES Parameter encoding.");
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      throw new IOException("Not a valid IES Parameter encoding.");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameter format " + paramString);
    }

    protected String engineToString()
    {
      return "IES Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == IESParameterSpec.class)
        return this.currentSpec;
      throw new InvalidParameterSpecException("unknown parameter spec passed to ElGamal parameters object.");
    }
  }

  public static class IVAlgorithmParameters extends JDKAlgorithmParameters
  {
    private byte[] iv;

    protected byte[] engineGetEncoded()
      throws IOException
    {
      return engineGetEncoded("ASN.1");
    }

    protected byte[] engineGetEncoded(String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
        return new DEROctetString(engineGetEncoded("RAW")).getEncoded();
      if (paramString.equals("RAW"))
        return Arrays.clone(this.iv);
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof IvParameterSpec))
        throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
      this.iv = ((IvParameterSpec)paramAlgorithmParameterSpec).getIV();
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      if ((paramArrayOfByte.length % 8 != 0) && (paramArrayOfByte[0] == 4) && (paramArrayOfByte[1] == -2 + paramArrayOfByte.length))
        paramArrayOfByte = ((ASN1OctetString)ASN1Object.fromByteArray(paramArrayOfByte)).getOctets();
      this.iv = Arrays.clone(paramArrayOfByte);
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
        try
        {
          engineInit(((ASN1OctetString)ASN1Object.fromByteArray(paramArrayOfByte)).getOctets());
          return;
        }
        catch (Exception localException)
        {
          throw new IOException("Exception decoding: " + localException);
        }
      if (paramString.equals("RAW"))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameters format in IV parameters object");
    }

    protected String engineToString()
    {
      return "IV Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == IvParameterSpec.class)
        return new IvParameterSpec(this.iv);
      throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
    }
  }

  public static class OAEP extends JDKAlgorithmParameters
  {
    OAEPParameterSpec currentSpec;

    protected byte[] engineGetEncoded()
    {
      AlgorithmIdentifier localAlgorithmIdentifier1 = new AlgorithmIdentifier(JCEDigestUtil.getOID(this.currentSpec.getDigestAlgorithm()), new DERNull());
      MGF1ParameterSpec localMGF1ParameterSpec = (MGF1ParameterSpec)this.currentSpec.getMGFParameters();
      AlgorithmIdentifier localAlgorithmIdentifier2 = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(JCEDigestUtil.getOID(localMGF1ParameterSpec.getDigestAlgorithm()), new DERNull()));
      PSource.PSpecified localPSpecified = (PSource.PSpecified)this.currentSpec.getPSource();
      RSAESOAEPparams localRSAESOAEPparams = new RSAESOAEPparams(localAlgorithmIdentifier1, localAlgorithmIdentifier2, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_pSpecified, new DEROctetString(localPSpecified.getValue())));
      try
      {
        byte[] arrayOfByte = localRSAESOAEPparams.getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
      }
      throw new RuntimeException("Error encoding OAEPParameters");
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof OAEPParameterSpec))
        throw new InvalidParameterSpecException("OAEPParameterSpec required to initialise an OAEP algorithm parameters object");
      this.currentSpec = ((OAEPParameterSpec)paramAlgorithmParameterSpec);
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        RSAESOAEPparams localRSAESOAEPparams = new RSAESOAEPparams((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte));
        this.currentSpec = new OAEPParameterSpec(localRSAESOAEPparams.getHashAlgorithm().getObjectId().getId(), localRSAESOAEPparams.getMaskGenAlgorithm().getObjectId().getId(), new MGF1ParameterSpec(AlgorithmIdentifier.getInstance(localRSAESOAEPparams.getMaskGenAlgorithm().getParameters()).getObjectId().getId()), new PSource.PSpecified(ASN1OctetString.getInstance(localRSAESOAEPparams.getPSourceAlgorithm().getParameters()).getOctets()));
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IOException("Not a valid OAEP Parameter encoding.");
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      throw new IOException("Not a valid OAEP Parameter encoding.");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if ((paramString.equalsIgnoreCase("X.509")) || (paramString.equalsIgnoreCase("ASN.1")))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameter format " + paramString);
    }

    protected String engineToString()
    {
      return "OAEP Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if ((paramClass == OAEPParameterSpec.class) && (this.currentSpec != null))
        return this.currentSpec;
      throw new InvalidParameterSpecException("unknown parameter spec passed to OAEP parameters object.");
    }
  }

  public static class PBKDF2 extends JDKAlgorithmParameters
  {
    PBKDF2Params params;

    protected byte[] engineGetEncoded()
    {
      try
      {
        byte[] arrayOfByte = this.params.getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("Oooops! " + localIOException.toString());
      }
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if (isASN1FormatString(paramString))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof PBEParameterSpec))
        throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PKCS12 PBE parameters algorithm parameters object");
      PBEParameterSpec localPBEParameterSpec = (PBEParameterSpec)paramAlgorithmParameterSpec;
      this.params = new PBKDF2Params(localPBEParameterSpec.getSalt(), localPBEParameterSpec.getIterationCount());
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      this.params = PBKDF2Params.getInstance(ASN1Object.fromByteArray(paramArrayOfByte));
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameters format in PWRIKEK parameters object");
    }

    protected String engineToString()
    {
      return "PBKDF2 Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == PBEParameterSpec.class)
        return new PBEParameterSpec(this.params.getSalt(), this.params.getIterationCount().intValue());
      throw new InvalidParameterSpecException("unknown parameter spec passed to PKCS12 PBE parameters object.");
    }
  }

  public static class PKCS12PBE extends JDKAlgorithmParameters
  {
    PKCS12PBEParams params;

    protected byte[] engineGetEncoded()
    {
      try
      {
        byte[] arrayOfByte = this.params.getEncoded("DER");
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("Oooops! " + localIOException.toString());
      }
    }

    protected byte[] engineGetEncoded(String paramString)
    {
      if (isASN1FormatString(paramString))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof PBEParameterSpec))
        throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PKCS12 PBE parameters algorithm parameters object");
      PBEParameterSpec localPBEParameterSpec = (PBEParameterSpec)paramAlgorithmParameterSpec;
      this.params = new PKCS12PBEParams(localPBEParameterSpec.getSalt(), localPBEParameterSpec.getIterationCount());
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      this.params = PKCS12PBEParams.getInstance(ASN1Object.fromByteArray(paramArrayOfByte));
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameters format in PKCS12 PBE parameters object");
    }

    protected String engineToString()
    {
      return "PKCS12 PBE Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if (paramClass == PBEParameterSpec.class)
        return new PBEParameterSpec(this.params.getIV(), this.params.getIterations().intValue());
      throw new InvalidParameterSpecException("unknown parameter spec passed to PKCS12 PBE parameters object.");
    }
  }

  public static class PSS extends JDKAlgorithmParameters
  {
    PSSParameterSpec currentSpec;

    protected byte[] engineGetEncoded()
      throws IOException
    {
      PSSParameterSpec localPSSParameterSpec = this.currentSpec;
      AlgorithmIdentifier localAlgorithmIdentifier = new AlgorithmIdentifier(JCEDigestUtil.getOID(localPSSParameterSpec.getDigestAlgorithm()), new DERNull());
      MGF1ParameterSpec localMGF1ParameterSpec = (MGF1ParameterSpec)localPSSParameterSpec.getMGFParameters();
      return new RSASSAPSSparams(localAlgorithmIdentifier, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(JCEDigestUtil.getOID(localMGF1ParameterSpec.getDigestAlgorithm()), new DERNull())), new DERInteger(localPSSParameterSpec.getSaltLength()), new DERInteger(localPSSParameterSpec.getTrailerField())).getEncoded("DER");
    }

    protected byte[] engineGetEncoded(String paramString)
      throws IOException
    {
      if ((paramString.equalsIgnoreCase("X.509")) || (paramString.equalsIgnoreCase("ASN.1")))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if (!(paramAlgorithmParameterSpec instanceof PSSParameterSpec))
        throw new InvalidParameterSpecException("PSSParameterSpec required to initialise an PSS algorithm parameters object");
      this.currentSpec = ((PSSParameterSpec)paramAlgorithmParameterSpec);
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        RSASSAPSSparams localRSASSAPSSparams = new RSASSAPSSparams((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte));
        this.currentSpec = new PSSParameterSpec(localRSASSAPSSparams.getHashAlgorithm().getObjectId().getId(), localRSASSAPSSparams.getMaskGenAlgorithm().getObjectId().getId(), new MGF1ParameterSpec(AlgorithmIdentifier.getInstance(localRSASSAPSSparams.getMaskGenAlgorithm().getParameters()).getObjectId().getId()), localRSASSAPSSparams.getSaltLength().getValue().intValue(), localRSASSAPSSparams.getTrailerField().getValue().intValue());
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IOException("Not a valid PSS Parameter encoding.");
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
      }
      throw new IOException("Not a valid PSS Parameter encoding.");
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if ((isASN1FormatString(paramString)) || (paramString.equalsIgnoreCase("X.509")))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameter format " + paramString);
    }

    protected String engineToString()
    {
      return "PSS Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if ((paramClass == PSSParameterSpec.class) && (this.currentSpec != null))
        return this.currentSpec;
      throw new InvalidParameterSpecException("unknown parameter spec passed to PSS parameters object.");
    }
  }

  public static class RC2AlgorithmParameters extends JDKAlgorithmParameters
  {
    private static final short[] ekb = arrayOfShort2;
    private static final short[] table;
    private byte[] iv;
    private int parameterVersion = 58;

    static
    {
      short[] arrayOfShort1 = new short[256];
      arrayOfShort1[0] = 189;
      arrayOfShort1[1] = 86;
      arrayOfShort1[2] = 234;
      arrayOfShort1[3] = 242;
      arrayOfShort1[4] = 162;
      arrayOfShort1[5] = 241;
      arrayOfShort1[6] = 172;
      arrayOfShort1[7] = 42;
      arrayOfShort1[8] = 176;
      arrayOfShort1[9] = 147;
      arrayOfShort1[10] = 209;
      arrayOfShort1[11] = 156;
      arrayOfShort1[12] = 27;
      arrayOfShort1[13] = 51;
      arrayOfShort1[14] = 253;
      arrayOfShort1[15] = 208;
      arrayOfShort1[16] = 48;
      arrayOfShort1[17] = 4;
      arrayOfShort1[18] = 182;
      arrayOfShort1[19] = 220;
      arrayOfShort1[20] = 125;
      arrayOfShort1[21] = 223;
      arrayOfShort1[22] = 50;
      arrayOfShort1[23] = 75;
      arrayOfShort1[24] = 247;
      arrayOfShort1[25] = 203;
      arrayOfShort1[26] = 69;
      arrayOfShort1[27] = 155;
      arrayOfShort1[28] = 49;
      arrayOfShort1[29] = 187;
      arrayOfShort1[30] = 33;
      arrayOfShort1[31] = 90;
      arrayOfShort1[32] = 65;
      arrayOfShort1[33] = 159;
      arrayOfShort1[34] = 225;
      arrayOfShort1[35] = 217;
      arrayOfShort1[36] = 74;
      arrayOfShort1[37] = 77;
      arrayOfShort1[38] = 158;
      arrayOfShort1[39] = 218;
      arrayOfShort1[40] = 160;
      arrayOfShort1[41] = 104;
      arrayOfShort1[42] = 44;
      arrayOfShort1[43] = 195;
      arrayOfShort1[44] = 39;
      arrayOfShort1[45] = 95;
      arrayOfShort1[46] = 128;
      arrayOfShort1[47] = 54;
      arrayOfShort1[48] = 62;
      arrayOfShort1[49] = 238;
      arrayOfShort1[50] = 251;
      arrayOfShort1[51] = 149;
      arrayOfShort1[52] = 26;
      arrayOfShort1[53] = 254;
      arrayOfShort1[54] = 206;
      arrayOfShort1[55] = 168;
      arrayOfShort1[56] = 52;
      arrayOfShort1[57] = 169;
      arrayOfShort1[58] = 19;
      arrayOfShort1[59] = 240;
      arrayOfShort1[60] = 166;
      arrayOfShort1[61] = 63;
      arrayOfShort1[62] = 216;
      arrayOfShort1[63] = 12;
      arrayOfShort1[64] = 120;
      arrayOfShort1[65] = 36;
      arrayOfShort1[66] = 175;
      arrayOfShort1[67] = 35;
      arrayOfShort1[68] = 82;
      arrayOfShort1[69] = 193;
      arrayOfShort1[70] = 103;
      arrayOfShort1[71] = 23;
      arrayOfShort1[72] = 245;
      arrayOfShort1[73] = 102;
      arrayOfShort1[74] = 144;
      arrayOfShort1[75] = 231;
      arrayOfShort1[76] = 232;
      arrayOfShort1[77] = 7;
      arrayOfShort1[78] = 184;
      arrayOfShort1[79] = 96;
      arrayOfShort1[80] = 72;
      arrayOfShort1[81] = 230;
      arrayOfShort1[82] = 30;
      arrayOfShort1[83] = 83;
      arrayOfShort1[84] = 243;
      arrayOfShort1[85] = 146;
      arrayOfShort1[86] = 164;
      arrayOfShort1[87] = 114;
      arrayOfShort1[88] = 140;
      arrayOfShort1[89] = 8;
      arrayOfShort1[90] = 21;
      arrayOfShort1[91] = 110;
      arrayOfShort1[92] = 134;
      arrayOfShort1[94] = 132;
      arrayOfShort1[95] = 250;
      arrayOfShort1[96] = 244;
      arrayOfShort1[97] = 127;
      arrayOfShort1[98] = 138;
      arrayOfShort1[99] = 66;
      arrayOfShort1[100] = 25;
      arrayOfShort1[101] = 246;
      arrayOfShort1[102] = 219;
      arrayOfShort1[103] = 205;
      arrayOfShort1[104] = 20;
      arrayOfShort1[105] = 141;
      arrayOfShort1[106] = 80;
      arrayOfShort1[107] = 18;
      arrayOfShort1[108] = 186;
      arrayOfShort1[109] = 60;
      arrayOfShort1[110] = 6;
      arrayOfShort1[111] = 78;
      arrayOfShort1[112] = 236;
      arrayOfShort1[113] = 179;
      arrayOfShort1[114] = 53;
      arrayOfShort1[115] = 17;
      arrayOfShort1[116] = 161;
      arrayOfShort1[117] = 136;
      arrayOfShort1[118] = 142;
      arrayOfShort1[119] = 43;
      arrayOfShort1[120] = 148;
      arrayOfShort1[121] = 153;
      arrayOfShort1[122] = 183;
      arrayOfShort1[123] = 113;
      arrayOfShort1[124] = 116;
      arrayOfShort1[125] = 211;
      arrayOfShort1[126] = 228;
      arrayOfShort1[127] = 191;
      arrayOfShort1[''] = 58;
      arrayOfShort1[''] = 222;
      arrayOfShort1[''] = 150;
      arrayOfShort1[''] = 14;
      arrayOfShort1[''] = 188;
      arrayOfShort1[''] = 10;
      arrayOfShort1[''] = 237;
      arrayOfShort1[''] = 119;
      arrayOfShort1[''] = 252;
      arrayOfShort1[''] = 55;
      arrayOfShort1[''] = 107;
      arrayOfShort1[''] = 3;
      arrayOfShort1[''] = 121;
      arrayOfShort1[''] = 137;
      arrayOfShort1[''] = 98;
      arrayOfShort1[''] = 198;
      arrayOfShort1[''] = 215;
      arrayOfShort1[''] = 192;
      arrayOfShort1[''] = 210;
      arrayOfShort1[''] = 124;
      arrayOfShort1[''] = 106;
      arrayOfShort1[''] = 139;
      arrayOfShort1[''] = 34;
      arrayOfShort1[''] = 163;
      arrayOfShort1[''] = 91;
      arrayOfShort1[''] = 5;
      arrayOfShort1[''] = 93;
      arrayOfShort1[''] = 2;
      arrayOfShort1[''] = 117;
      arrayOfShort1[''] = 213;
      arrayOfShort1[''] = 97;
      arrayOfShort1[''] = 227;
      arrayOfShort1[' '] = 24;
      arrayOfShort1['¡'] = 143;
      arrayOfShort1['¢'] = 85;
      arrayOfShort1['£'] = 81;
      arrayOfShort1['¤'] = 173;
      arrayOfShort1['¥'] = 31;
      arrayOfShort1['¦'] = 11;
      arrayOfShort1['§'] = 94;
      arrayOfShort1['¨'] = 133;
      arrayOfShort1['©'] = 229;
      arrayOfShort1['ª'] = 194;
      arrayOfShort1['«'] = 87;
      arrayOfShort1['¬'] = 99;
      arrayOfShort1['­'] = 202;
      arrayOfShort1['®'] = 61;
      arrayOfShort1['¯'] = 108;
      arrayOfShort1['°'] = 180;
      arrayOfShort1['±'] = 197;
      arrayOfShort1['²'] = 204;
      arrayOfShort1['³'] = 112;
      arrayOfShort1['´'] = 178;
      arrayOfShort1['µ'] = 145;
      arrayOfShort1['¶'] = 89;
      arrayOfShort1['·'] = 13;
      arrayOfShort1['¸'] = 71;
      arrayOfShort1['¹'] = 32;
      arrayOfShort1['º'] = 200;
      arrayOfShort1['»'] = 79;
      arrayOfShort1['¼'] = 88;
      arrayOfShort1['½'] = 224;
      arrayOfShort1['¾'] = 1;
      arrayOfShort1['¿'] = 226;
      arrayOfShort1['À'] = 22;
      arrayOfShort1['Á'] = 56;
      arrayOfShort1['Â'] = 196;
      arrayOfShort1['Ã'] = 111;
      arrayOfShort1['Ä'] = 59;
      arrayOfShort1['Å'] = 15;
      arrayOfShort1['Æ'] = 101;
      arrayOfShort1['Ç'] = 70;
      arrayOfShort1['È'] = 190;
      arrayOfShort1['É'] = 126;
      arrayOfShort1['Ê'] = 45;
      arrayOfShort1['Ë'] = 123;
      arrayOfShort1['Ì'] = 130;
      arrayOfShort1['Í'] = 249;
      arrayOfShort1['Î'] = 64;
      arrayOfShort1['Ï'] = 181;
      arrayOfShort1['Ð'] = 29;
      arrayOfShort1['Ñ'] = 115;
      arrayOfShort1['Ò'] = 248;
      arrayOfShort1['Ó'] = 235;
      arrayOfShort1['Ô'] = 38;
      arrayOfShort1['Õ'] = 199;
      arrayOfShort1['Ö'] = 135;
      arrayOfShort1['×'] = 151;
      arrayOfShort1['Ø'] = 37;
      arrayOfShort1['Ù'] = 84;
      arrayOfShort1['Ú'] = 177;
      arrayOfShort1['Û'] = 40;
      arrayOfShort1['Ü'] = 170;
      arrayOfShort1['Ý'] = 152;
      arrayOfShort1['Þ'] = 157;
      arrayOfShort1['ß'] = 165;
      arrayOfShort1['à'] = 100;
      arrayOfShort1['á'] = 109;
      arrayOfShort1['â'] = 122;
      arrayOfShort1['ã'] = 212;
      arrayOfShort1['ä'] = 16;
      arrayOfShort1['å'] = 129;
      arrayOfShort1['æ'] = 68;
      arrayOfShort1['ç'] = 239;
      arrayOfShort1['è'] = 73;
      arrayOfShort1['é'] = 214;
      arrayOfShort1['ê'] = 174;
      arrayOfShort1['ë'] = 46;
      arrayOfShort1['ì'] = 221;
      arrayOfShort1['í'] = 118;
      arrayOfShort1['î'] = 92;
      arrayOfShort1['ï'] = 47;
      arrayOfShort1['ð'] = 167;
      arrayOfShort1['ñ'] = 28;
      arrayOfShort1['ò'] = 201;
      arrayOfShort1['ó'] = 9;
      arrayOfShort1['ô'] = 105;
      arrayOfShort1['õ'] = 154;
      arrayOfShort1['ö'] = 131;
      arrayOfShort1['÷'] = 207;
      arrayOfShort1['ø'] = 41;
      arrayOfShort1['ù'] = 57;
      arrayOfShort1['ú'] = 185;
      arrayOfShort1['û'] = 233;
      arrayOfShort1['ü'] = 76;
      arrayOfShort1['ý'] = 255;
      arrayOfShort1['þ'] = 67;
      arrayOfShort1['ÿ'] = 171;
      table = arrayOfShort1;
      short[] arrayOfShort2 = new short[256];
      arrayOfShort2[0] = 93;
      arrayOfShort2[1] = 190;
      arrayOfShort2[2] = 155;
      arrayOfShort2[3] = 139;
      arrayOfShort2[4] = 17;
      arrayOfShort2[5] = 153;
      arrayOfShort2[6] = 110;
      arrayOfShort2[7] = 77;
      arrayOfShort2[8] = 89;
      arrayOfShort2[9] = 243;
      arrayOfShort2[10] = 133;
      arrayOfShort2[11] = 166;
      arrayOfShort2[12] = 63;
      arrayOfShort2[13] = 183;
      arrayOfShort2[14] = 131;
      arrayOfShort2[15] = 197;
      arrayOfShort2[16] = 228;
      arrayOfShort2[17] = 115;
      arrayOfShort2[18] = 107;
      arrayOfShort2[19] = 58;
      arrayOfShort2[20] = 104;
      arrayOfShort2[21] = 90;
      arrayOfShort2[22] = 192;
      arrayOfShort2[23] = 71;
      arrayOfShort2[24] = 160;
      arrayOfShort2[25] = 100;
      arrayOfShort2[26] = 52;
      arrayOfShort2[27] = 12;
      arrayOfShort2[28] = 241;
      arrayOfShort2[29] = 208;
      arrayOfShort2[30] = 82;
      arrayOfShort2[31] = 165;
      arrayOfShort2[32] = 185;
      arrayOfShort2[33] = 30;
      arrayOfShort2[34] = 150;
      arrayOfShort2[35] = 67;
      arrayOfShort2[36] = 65;
      arrayOfShort2[37] = 216;
      arrayOfShort2[38] = 212;
      arrayOfShort2[39] = 44;
      arrayOfShort2[40] = 219;
      arrayOfShort2[41] = 248;
      arrayOfShort2[42] = 7;
      arrayOfShort2[43] = 119;
      arrayOfShort2[44] = 42;
      arrayOfShort2[45] = 202;
      arrayOfShort2[46] = 235;
      arrayOfShort2[47] = 239;
      arrayOfShort2[48] = 16;
      arrayOfShort2[49] = 28;
      arrayOfShort2[50] = 22;
      arrayOfShort2[51] = 13;
      arrayOfShort2[52] = 56;
      arrayOfShort2[53] = 114;
      arrayOfShort2[54] = 47;
      arrayOfShort2[55] = 137;
      arrayOfShort2[56] = 193;
      arrayOfShort2[57] = 249;
      arrayOfShort2[58] = 128;
      arrayOfShort2[59] = 196;
      arrayOfShort2[60] = 109;
      arrayOfShort2[61] = 174;
      arrayOfShort2[62] = 48;
      arrayOfShort2[63] = 61;
      arrayOfShort2[64] = 206;
      arrayOfShort2[65] = 32;
      arrayOfShort2[66] = 99;
      arrayOfShort2[67] = 254;
      arrayOfShort2[68] = 230;
      arrayOfShort2[69] = 26;
      arrayOfShort2[70] = 199;
      arrayOfShort2[71] = 184;
      arrayOfShort2[72] = 80;
      arrayOfShort2[73] = 232;
      arrayOfShort2[74] = 36;
      arrayOfShort2[75] = 23;
      arrayOfShort2[76] = 252;
      arrayOfShort2[77] = 37;
      arrayOfShort2[78] = 111;
      arrayOfShort2[79] = 187;
      arrayOfShort2[80] = 106;
      arrayOfShort2[81] = 163;
      arrayOfShort2[82] = 68;
      arrayOfShort2[83] = 83;
      arrayOfShort2[84] = 217;
      arrayOfShort2[85] = 162;
      arrayOfShort2[86] = 1;
      arrayOfShort2[87] = 171;
      arrayOfShort2[88] = 188;
      arrayOfShort2[89] = 182;
      arrayOfShort2[90] = 31;
      arrayOfShort2[91] = 152;
      arrayOfShort2[92] = 238;
      arrayOfShort2[93] = 154;
      arrayOfShort2[94] = 167;
      arrayOfShort2[95] = 45;
      arrayOfShort2[96] = 79;
      arrayOfShort2[97] = 158;
      arrayOfShort2[98] = 142;
      arrayOfShort2[99] = 172;
      arrayOfShort2[100] = 224;
      arrayOfShort2[101] = 198;
      arrayOfShort2[102] = 73;
      arrayOfShort2[103] = 70;
      arrayOfShort2[104] = 41;
      arrayOfShort2[105] = 244;
      arrayOfShort2[106] = 148;
      arrayOfShort2[107] = 138;
      arrayOfShort2[108] = 175;
      arrayOfShort2[109] = 225;
      arrayOfShort2[110] = 91;
      arrayOfShort2[111] = 195;
      arrayOfShort2[112] = 179;
      arrayOfShort2[113] = 123;
      arrayOfShort2[114] = 87;
      arrayOfShort2[115] = 209;
      arrayOfShort2[116] = 124;
      arrayOfShort2[117] = 156;
      arrayOfShort2[118] = 237;
      arrayOfShort2[119] = 135;
      arrayOfShort2[120] = 64;
      arrayOfShort2[121] = 140;
      arrayOfShort2[122] = 226;
      arrayOfShort2[123] = 203;
      arrayOfShort2[124] = 147;
      arrayOfShort2[125] = 20;
      arrayOfShort2[126] = 201;
      arrayOfShort2[127] = 97;
      arrayOfShort2[''] = 46;
      arrayOfShort2[''] = 229;
      arrayOfShort2[''] = 204;
      arrayOfShort2[''] = 246;
      arrayOfShort2[''] = 94;
      arrayOfShort2[''] = 168;
      arrayOfShort2[''] = 92;
      arrayOfShort2[''] = 214;
      arrayOfShort2[''] = 117;
      arrayOfShort2[''] = 141;
      arrayOfShort2[''] = 98;
      arrayOfShort2[''] = 149;
      arrayOfShort2[''] = 88;
      arrayOfShort2[''] = 105;
      arrayOfShort2[''] = 118;
      arrayOfShort2[''] = 161;
      arrayOfShort2[''] = 74;
      arrayOfShort2[''] = 181;
      arrayOfShort2[''] = 85;
      arrayOfShort2[''] = 9;
      arrayOfShort2[''] = 120;
      arrayOfShort2[''] = 51;
      arrayOfShort2[''] = 130;
      arrayOfShort2[''] = 215;
      arrayOfShort2[''] = 221;
      arrayOfShort2[''] = 121;
      arrayOfShort2[''] = 245;
      arrayOfShort2[''] = 27;
      arrayOfShort2[''] = 11;
      arrayOfShort2[''] = 222;
      arrayOfShort2[''] = 38;
      arrayOfShort2[''] = 33;
      arrayOfShort2[' '] = 40;
      arrayOfShort2['¡'] = 116;
      arrayOfShort2['¢'] = 4;
      arrayOfShort2['£'] = 151;
      arrayOfShort2['¤'] = 86;
      arrayOfShort2['¥'] = 223;
      arrayOfShort2['¦'] = 60;
      arrayOfShort2['§'] = 240;
      arrayOfShort2['¨'] = 55;
      arrayOfShort2['©'] = 57;
      arrayOfShort2['ª'] = 220;
      arrayOfShort2['«'] = 255;
      arrayOfShort2['¬'] = 6;
      arrayOfShort2['­'] = 164;
      arrayOfShort2['®'] = 234;
      arrayOfShort2['¯'] = 66;
      arrayOfShort2['°'] = 8;
      arrayOfShort2['±'] = 218;
      arrayOfShort2['²'] = 180;
      arrayOfShort2['³'] = 113;
      arrayOfShort2['´'] = 176;
      arrayOfShort2['µ'] = 207;
      arrayOfShort2['¶'] = 18;
      arrayOfShort2['·'] = 122;
      arrayOfShort2['¸'] = 78;
      arrayOfShort2['¹'] = 250;
      arrayOfShort2['º'] = 108;
      arrayOfShort2['»'] = 29;
      arrayOfShort2['¼'] = 132;
      arrayOfShort2['¾'] = 200;
      arrayOfShort2['¿'] = 127;
      arrayOfShort2['À'] = 145;
      arrayOfShort2['Á'] = 69;
      arrayOfShort2['Â'] = 170;
      arrayOfShort2['Ã'] = 43;
      arrayOfShort2['Ä'] = 194;
      arrayOfShort2['Å'] = 177;
      arrayOfShort2['Æ'] = 143;
      arrayOfShort2['Ç'] = 213;
      arrayOfShort2['È'] = 186;
      arrayOfShort2['É'] = 242;
      arrayOfShort2['Ê'] = 173;
      arrayOfShort2['Ë'] = 25;
      arrayOfShort2['Ì'] = 178;
      arrayOfShort2['Í'] = 103;
      arrayOfShort2['Î'] = 54;
      arrayOfShort2['Ï'] = 247;
      arrayOfShort2['Ð'] = 15;
      arrayOfShort2['Ñ'] = 10;
      arrayOfShort2['Ò'] = 146;
      arrayOfShort2['Ó'] = 125;
      arrayOfShort2['Ô'] = 227;
      arrayOfShort2['Õ'] = 157;
      arrayOfShort2['Ö'] = 233;
      arrayOfShort2['×'] = 144;
      arrayOfShort2['Ø'] = 62;
      arrayOfShort2['Ù'] = 35;
      arrayOfShort2['Ú'] = 39;
      arrayOfShort2['Û'] = 102;
      arrayOfShort2['Ü'] = 19;
      arrayOfShort2['Ý'] = 236;
      arrayOfShort2['Þ'] = 129;
      arrayOfShort2['ß'] = 21;
      arrayOfShort2['à'] = 189;
      arrayOfShort2['á'] = 34;
      arrayOfShort2['â'] = 191;
      arrayOfShort2['ã'] = 159;
      arrayOfShort2['ä'] = 126;
      arrayOfShort2['å'] = 169;
      arrayOfShort2['æ'] = 81;
      arrayOfShort2['ç'] = 75;
      arrayOfShort2['è'] = 76;
      arrayOfShort2['é'] = 251;
      arrayOfShort2['ê'] = 2;
      arrayOfShort2['ë'] = 211;
      arrayOfShort2['ì'] = 112;
      arrayOfShort2['í'] = 134;
      arrayOfShort2['î'] = 49;
      arrayOfShort2['ï'] = 231;
      arrayOfShort2['ð'] = 59;
      arrayOfShort2['ñ'] = 5;
      arrayOfShort2['ò'] = 3;
      arrayOfShort2['ó'] = 84;
      arrayOfShort2['ô'] = 96;
      arrayOfShort2['õ'] = 72;
      arrayOfShort2['ö'] = 101;
      arrayOfShort2['÷'] = 24;
      arrayOfShort2['ø'] = 210;
      arrayOfShort2['ù'] = 205;
      arrayOfShort2['ú'] = 95;
      arrayOfShort2['û'] = 50;
      arrayOfShort2['ü'] = 136;
      arrayOfShort2['ý'] = 14;
      arrayOfShort2['þ'] = 53;
      arrayOfShort2['ÿ'] = 253;
    }

    protected byte[] engineGetEncoded()
    {
      return Arrays.clone(this.iv);
    }

    protected byte[] engineGetEncoded(String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
      {
        if (this.parameterVersion == -1)
          return new RC2CBCParameter(engineGetEncoded()).getEncoded();
        return new RC2CBCParameter(this.parameterVersion, engineGetEncoded()).getEncoded();
      }
      if (paramString.equals("RAW"))
        return engineGetEncoded();
      return null;
    }

    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidParameterSpecException
    {
      if ((paramAlgorithmParameterSpec instanceof IvParameterSpec))
      {
        this.iv = ((IvParameterSpec)paramAlgorithmParameterSpec).getIV();
        return;
      }
      if ((paramAlgorithmParameterSpec instanceof RC2ParameterSpec))
      {
        int i = ((RC2ParameterSpec)paramAlgorithmParameterSpec).getEffectiveKeyBits();
        if (i != -1)
          if (i >= 256)
            break label67;
        label67: for (this.parameterVersion = table[i]; ; this.parameterVersion = i)
        {
          this.iv = ((RC2ParameterSpec)paramAlgorithmParameterSpec).getIV();
          return;
        }
      }
      throw new InvalidParameterSpecException("IvParameterSpec or RC2ParameterSpec required to initialise a RC2 parameters algorithm parameters object");
    }

    protected void engineInit(byte[] paramArrayOfByte)
      throws IOException
    {
      this.iv = Arrays.clone(paramArrayOfByte);
    }

    protected void engineInit(byte[] paramArrayOfByte, String paramString)
      throws IOException
    {
      if (isASN1FormatString(paramString))
      {
        RC2CBCParameter localRC2CBCParameter = RC2CBCParameter.getInstance(ASN1Object.fromByteArray(paramArrayOfByte));
        if (localRC2CBCParameter.getRC2ParameterVersion() != null)
          this.parameterVersion = localRC2CBCParameter.getRC2ParameterVersion().intValue();
        this.iv = localRC2CBCParameter.getIV();
        return;
      }
      if (paramString.equals("RAW"))
      {
        engineInit(paramArrayOfByte);
        return;
      }
      throw new IOException("Unknown parameters format in IV parameters object");
    }

    protected String engineToString()
    {
      return "RC2 Parameters";
    }

    protected AlgorithmParameterSpec localEngineGetParameterSpec(Class paramClass)
      throws InvalidParameterSpecException
    {
      if ((paramClass == RC2ParameterSpec.class) && (this.parameterVersion != -1))
      {
        if (this.parameterVersion < 256)
          return new RC2ParameterSpec(ekb[this.parameterVersion], this.iv);
        return new RC2ParameterSpec(this.parameterVersion, this.iv);
      }
      if (paramClass == IvParameterSpec.class)
        return new IvParameterSpec(this.iv);
      throw new InvalidParameterSpecException("unknown parameter spec passed to RC2 parameters object.");
    }
  }
}