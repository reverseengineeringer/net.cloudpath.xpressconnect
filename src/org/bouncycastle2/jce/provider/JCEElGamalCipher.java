package org.bouncycastle2.jce.provider;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.interfaces.DHKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.BufferedAsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.encodings.ISO9796d1Encoding;
import org.bouncycastle2.crypto.encodings.OAEPEncoding;
import org.bouncycastle2.crypto.encodings.PKCS1Encoding;
import org.bouncycastle2.crypto.engines.ElGamalEngine;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.jce.interfaces.ElGamalKey;
import org.bouncycastle2.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle2.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle2.jce.spec.ElGamalParameterSpec;
import org.bouncycastle2.util.Strings;

public class JCEElGamalCipher extends WrapCipherSpi
{
  private BufferedAsymmetricBlockCipher cipher;
  private AlgorithmParameters engineParams;
  private AlgorithmParameterSpec paramSpec;

  public JCEElGamalCipher(AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.cipher = new BufferedAsymmetricBlockCipher(paramAsymmetricBlockCipher);
  }

  private void initFromSpec(OAEPParameterSpec paramOAEPParameterSpec)
    throws NoSuchPaddingException
  {
    MGF1ParameterSpec localMGF1ParameterSpec = (MGF1ParameterSpec)paramOAEPParameterSpec.getMGFParameters();
    Digest localDigest = JCEDigestUtil.getDigest(localMGF1ParameterSpec.getDigestAlgorithm());
    if (localDigest == null)
      throw new NoSuchPaddingException("no match on OAEP constructor for digest algorithm: " + localMGF1ParameterSpec.getDigestAlgorithm());
    this.cipher = new BufferedAsymmetricBlockCipher(new OAEPEncoding(new ElGamalEngine(), localDigest, ((PSource.PSpecified)paramOAEPParameterSpec.getPSource()).getValue()));
    this.paramSpec = paramOAEPParameterSpec;
  }

  protected int engineDoFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws IllegalBlockSizeException, BadPaddingException
  {
    this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2);
    while (true)
    {
      byte[] arrayOfByte;
      int i;
      try
      {
        arrayOfByte = this.cipher.doFinal();
        i = 0;
        if (i == arrayOfByte.length)
          return arrayOfByte.length;
      }
      catch (InvalidCipherTextException localInvalidCipherTextException)
      {
        throw new BadPaddingException(localInvalidCipherTextException.getMessage());
      }
      paramArrayOfByte2[(paramInt3 + i)] = arrayOfByte[i];
      i++;
    }
  }

  protected byte[] engineDoFinal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IllegalBlockSizeException, BadPaddingException
  {
    this.cipher.processBytes(paramArrayOfByte, paramInt1, paramInt2);
    try
    {
      byte[] arrayOfByte = this.cipher.doFinal();
      return arrayOfByte;
    }
    catch (InvalidCipherTextException localInvalidCipherTextException)
    {
      throw new BadPaddingException(localInvalidCipherTextException.getMessage());
    }
  }

  protected int engineGetBlockSize()
  {
    return this.cipher.getInputBlockSize();
  }

  protected byte[] engineGetIV()
  {
    return null;
  }

  protected int engineGetKeySize(Key paramKey)
  {
    if ((paramKey instanceof ElGamalKey))
      return ((ElGamalKey)paramKey).getParameters().getP().bitLength();
    if ((paramKey instanceof DHKey))
      return ((DHKey)paramKey).getParams().getP().bitLength();
    throw new IllegalArgumentException("not an ElGamal key!");
  }

  protected int engineGetOutputSize(int paramInt)
  {
    return this.cipher.getOutputBlockSize();
  }

  protected AlgorithmParameters engineGetParameters()
  {
    if ((this.engineParams == null) && (this.paramSpec != null));
    try
    {
      this.engineParams = AlgorithmParameters.getInstance("OAEP", BouncyCastleProvider.PROVIDER_NAME);
      this.engineParams.init(this.paramSpec);
      return this.engineParams;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException.toString());
    }
  }

  protected void engineInit(int paramInt, Key paramKey, AlgorithmParameters paramAlgorithmParameters, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    throw new InvalidAlgorithmParameterException("can't handle parameters in ElGamal");
  }

  protected void engineInit(int paramInt, Key paramKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    engineInit(paramInt, paramKey, null, paramSecureRandom);
  }

  protected void engineInit(int paramInt, Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    AsymmetricKeyParameter localAsymmetricKeyParameter;
    if (paramAlgorithmParameterSpec == null)
      if ((paramKey instanceof ElGamalPublicKey))
      {
        localAsymmetricKeyParameter = ElGamalUtil.generatePublicKeyParameter((PublicKey)paramKey);
        if (paramSecureRandom == null)
          break label158;
      }
    label158: for (Object localObject = new ParametersWithRandom(localAsymmetricKeyParameter, paramSecureRandom); ; localObject = localAsymmetricKeyParameter)
      switch (paramInt)
      {
      default:
        throw new InvalidParameterException("unknown opmode " + paramInt + " passed to ElGamal");
        if ((paramKey instanceof ElGamalPrivateKey))
        {
          localAsymmetricKeyParameter = ElGamalUtil.generatePrivateKeyParameter((PrivateKey)paramKey);
          break;
        }
        throw new InvalidKeyException("unknown key type passed to ElGamal");
        throw new IllegalArgumentException("unknown parameter type.");
      case 1:
      case 3:
        this.cipher.init(true, (CipherParameters)localObject);
        return;
      case 2:
      case 4:
        this.cipher.init(false, (CipherParameters)localObject);
        return;
      }
  }

  protected void engineSetMode(String paramString)
    throws NoSuchAlgorithmException
  {
    String str = Strings.toUpperCase(paramString);
    if ((str.equals("NONE")) || (str.equals("ECB")))
      return;
    throw new NoSuchAlgorithmException("can't support mode " + paramString);
  }

  protected void engineSetPadding(String paramString)
    throws NoSuchPaddingException
  {
    String str = Strings.toUpperCase(paramString);
    if (str.equals("NOPADDING"))
    {
      this.cipher = new BufferedAsymmetricBlockCipher(new ElGamalEngine());
      return;
    }
    if (str.equals("PKCS1PADDING"))
    {
      this.cipher = new BufferedAsymmetricBlockCipher(new PKCS1Encoding(new ElGamalEngine()));
      return;
    }
    if (str.equals("ISO9796-1PADDING"))
    {
      this.cipher = new BufferedAsymmetricBlockCipher(new ISO9796d1Encoding(new ElGamalEngine()));
      return;
    }
    if (str.equals("OAEPPADDING"))
    {
      initFromSpec(OAEPParameterSpec.DEFAULT);
      return;
    }
    if (str.equals("OAEPWITHMD5ANDMGF1PADDING"))
    {
      initFromSpec(new OAEPParameterSpec("MD5", "MGF1", new MGF1ParameterSpec("MD5"), PSource.PSpecified.DEFAULT));
      return;
    }
    if (str.equals("OAEPWITHSHA1ANDMGF1PADDING"))
    {
      initFromSpec(OAEPParameterSpec.DEFAULT);
      return;
    }
    if (str.equals("OAEPWITHSHA224ANDMGF1PADDING"))
    {
      initFromSpec(new OAEPParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), PSource.PSpecified.DEFAULT));
      return;
    }
    if (str.equals("OAEPWITHSHA256ANDMGF1PADDING"))
    {
      initFromSpec(new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
      return;
    }
    if (str.equals("OAEPWITHSHA384ANDMGF1PADDING"))
    {
      initFromSpec(new OAEPParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
      return;
    }
    if (str.equals("OAEPWITHSHA512ANDMGF1PADDING"))
    {
      initFromSpec(new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
      return;
    }
    throw new NoSuchPaddingException(paramString + " unavailable with ElGamal.");
  }

  protected int engineUpdate(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2);
    return 0;
  }

  protected byte[] engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.cipher.processBytes(paramArrayOfByte, paramInt1, paramInt2);
    return null;
  }

  public static class NoPadding extends JCEElGamalCipher
  {
    public NoPadding()
    {
      super();
    }
  }

  public static class PKCS1v1_5Padding extends JCEElGamalCipher
  {
    public PKCS1v1_5Padding()
    {
      super();
    }
  }
}