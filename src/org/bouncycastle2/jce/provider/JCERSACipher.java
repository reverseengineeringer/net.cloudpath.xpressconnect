package org.bouncycastle2.jce.provider;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.encodings.ISO9796d1Encoding;
import org.bouncycastle2.crypto.encodings.OAEPEncoding;
import org.bouncycastle2.crypto.encodings.PKCS1Encoding;
import org.bouncycastle2.crypto.engines.RSABlindedEngine;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.util.Strings;

public class JCERSACipher extends WrapCipherSpi
{
  private ByteArrayOutputStream bOut = new ByteArrayOutputStream();
  private AsymmetricBlockCipher cipher;
  private AlgorithmParameters engineParams;
  private AlgorithmParameterSpec paramSpec;
  private boolean privateKeyOnly = false;
  private boolean publicKeyOnly = false;

  public JCERSACipher(OAEPParameterSpec paramOAEPParameterSpec)
  {
    try
    {
      initFromSpec(paramOAEPParameterSpec);
      return;
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new IllegalArgumentException(localNoSuchPaddingException.getMessage());
    }
  }

  public JCERSACipher(AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.cipher = paramAsymmetricBlockCipher;
  }

  public JCERSACipher(boolean paramBoolean1, boolean paramBoolean2, AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.publicKeyOnly = paramBoolean1;
    this.privateKeyOnly = paramBoolean2;
    this.cipher = paramAsymmetricBlockCipher;
  }

  private void initFromSpec(OAEPParameterSpec paramOAEPParameterSpec)
    throws NoSuchPaddingException
  {
    MGF1ParameterSpec localMGF1ParameterSpec = (MGF1ParameterSpec)paramOAEPParameterSpec.getMGFParameters();
    Digest localDigest = JCEDigestUtil.getDigest(localMGF1ParameterSpec.getDigestAlgorithm());
    if (localDigest == null)
      throw new NoSuchPaddingException("no match on OAEP constructor for digest algorithm: " + localMGF1ParameterSpec.getDigestAlgorithm());
    this.cipher = new OAEPEncoding(new RSABlindedEngine(), localDigest, ((PSource.PSpecified)paramOAEPParameterSpec.getPSource()).getValue());
    this.paramSpec = paramOAEPParameterSpec;
  }

  protected int engineDoFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws IllegalBlockSizeException, BadPaddingException
  {
    if (paramArrayOfByte1 != null)
      this.bOut.write(paramArrayOfByte1, paramInt1, paramInt2);
    if ((this.cipher instanceof RSABlindedEngine))
    {
      if (this.bOut.size() > 1 + this.cipher.getInputBlockSize())
        throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    }
    else if (this.bOut.size() > this.cipher.getInputBlockSize())
      throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    while (true)
    {
      byte[] arrayOfByte2;
      int i;
      try
      {
        byte[] arrayOfByte1 = this.bOut.toByteArray();
        this.bOut.reset();
        arrayOfByte2 = this.cipher.processBlock(arrayOfByte1, 0, arrayOfByte1.length);
        i = 0;
        if (i == arrayOfByte2.length)
          return arrayOfByte2.length;
      }
      catch (InvalidCipherTextException localInvalidCipherTextException)
      {
        throw new BadPaddingException(localInvalidCipherTextException.getMessage());
      }
      paramArrayOfByte2[(paramInt3 + i)] = arrayOfByte2[i];
      i++;
    }
  }

  protected byte[] engineDoFinal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IllegalBlockSizeException, BadPaddingException
  {
    if (paramArrayOfByte != null)
      this.bOut.write(paramArrayOfByte, paramInt1, paramInt2);
    if ((this.cipher instanceof RSABlindedEngine))
    {
      if (this.bOut.size() > 1 + this.cipher.getInputBlockSize())
        throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    }
    else if (this.bOut.size() > this.cipher.getInputBlockSize())
      throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    try
    {
      byte[] arrayOfByte1 = this.bOut.toByteArray();
      this.bOut.reset();
      byte[] arrayOfByte2 = this.cipher.processBlock(arrayOfByte1, 0, arrayOfByte1.length);
      return arrayOfByte2;
    }
    catch (InvalidCipherTextException localInvalidCipherTextException)
    {
      throw new BadPaddingException(localInvalidCipherTextException.getMessage());
    }
  }

  protected int engineGetBlockSize()
  {
    try
    {
      int i = this.cipher.getInputBlockSize();
      return i;
    }
    catch (NullPointerException localNullPointerException)
    {
    }
    throw new IllegalStateException("RSA Cipher not initialised");
  }

  protected byte[] engineGetIV()
  {
    return null;
  }

  protected int engineGetKeySize(Key paramKey)
  {
    if ((paramKey instanceof RSAPrivateKey))
      return ((RSAPrivateKey)paramKey).getModulus().bitLength();
    if ((paramKey instanceof RSAPublicKey))
      return ((RSAPublicKey)paramKey).getModulus().bitLength();
    throw new IllegalArgumentException("not an RSA key!");
  }

  protected int engineGetOutputSize(int paramInt)
  {
    try
    {
      int i = this.cipher.getOutputBlockSize();
      return i;
    }
    catch (NullPointerException localNullPointerException)
    {
    }
    throw new IllegalStateException("RSA Cipher not initialised");
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
    Object localObject = null;
    if (paramAlgorithmParameters != null);
    try
    {
      AlgorithmParameterSpec localAlgorithmParameterSpec = paramAlgorithmParameters.getParameterSpec(OAEPParameterSpec.class);
      localObject = localAlgorithmParameterSpec;
      this.engineParams = paramAlgorithmParameters;
      engineInit(paramInt, paramKey, localObject, paramSecureRandom);
      return;
    }
    catch (InvalidParameterSpecException localInvalidParameterSpecException)
    {
      throw new InvalidAlgorithmParameterException("cannot recognise parameters: " + localInvalidParameterSpecException.toString(), localInvalidParameterSpecException);
    }
  }

  protected void engineInit(int paramInt, Key paramKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    try
    {
      engineInit(paramInt, paramKey, null, paramSecureRandom);
      return;
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new RuntimeException("Eeeek! " + localInvalidAlgorithmParameterException.toString(), localInvalidAlgorithmParameterException);
    }
  }

  protected void engineInit(int paramInt, Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    RSAKeyParameters localRSAKeyParameters;
    Object localObject;
    if ((paramAlgorithmParameterSpec == null) || ((paramAlgorithmParameterSpec instanceof OAEPParameterSpec)))
    {
      if ((paramKey instanceof RSAPublicKey))
      {
        if (this.privateKeyOnly)
          throw new InvalidKeyException("mode 1 requires RSAPrivateKey");
        localRSAKeyParameters = RSAUtil.generatePublicKeyParameter((RSAPublicKey)paramKey);
      }
      while (paramAlgorithmParameterSpec != null)
      {
        OAEPParameterSpec localOAEPParameterSpec = (OAEPParameterSpec)paramAlgorithmParameterSpec;
        this.paramSpec = paramAlgorithmParameterSpec;
        if ((!localOAEPParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1")) && (!localOAEPParameterSpec.getMGFAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1.getId())))
        {
          throw new InvalidAlgorithmParameterException("unknown mask generation function specified");
          if ((paramKey instanceof RSAPrivateKey))
          {
            if (this.publicKeyOnly)
              throw new InvalidKeyException("mode 2 requires RSAPublicKey");
            localRSAKeyParameters = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)paramKey);
          }
          else
          {
            throw new InvalidKeyException("unknown key type passed to RSA");
          }
        }
        else
        {
          if (!(localOAEPParameterSpec.getMGFParameters() instanceof MGF1ParameterSpec))
            throw new InvalidAlgorithmParameterException("unkown MGF parameters");
          Digest localDigest1 = JCEDigestUtil.getDigest(localOAEPParameterSpec.getDigestAlgorithm());
          if (localDigest1 == null)
            throw new InvalidAlgorithmParameterException("no match on digest algorithm: " + localOAEPParameterSpec.getDigestAlgorithm());
          MGF1ParameterSpec localMGF1ParameterSpec = (MGF1ParameterSpec)localOAEPParameterSpec.getMGFParameters();
          Digest localDigest2 = JCEDigestUtil.getDigest(localMGF1ParameterSpec.getDigestAlgorithm());
          if (localDigest2 == null)
            throw new InvalidAlgorithmParameterException("no match on MGF digest algorithm: " + localMGF1ParameterSpec.getDigestAlgorithm());
          this.cipher = new OAEPEncoding(new RSABlindedEngine(), localDigest1, localDigest2, ((PSource.PSpecified)localOAEPParameterSpec.getPSource()).getValue());
        }
      }
      if ((this.cipher instanceof RSABlindedEngine))
        break label449;
      if (paramSecureRandom == null)
        break label402;
      localObject = new ParametersWithRandom(localRSAKeyParameters, paramSecureRandom);
    }
    while (true)
      switch (paramInt)
      {
      default:
        throw new InvalidParameterException("unknown opmode " + paramInt + " passed to RSA");
        throw new IllegalArgumentException("unknown parameter type.");
        localObject = new ParametersWithRandom(localRSAKeyParameters, new SecureRandom());
        break;
      case 1:
      case 3:
        this.cipher.init(true, (CipherParameters)localObject);
        return;
      case 2:
      case 4:
        label402: this.cipher.init(false, (CipherParameters)localObject);
        return;
        label449: localObject = localRSAKeyParameters;
      }
  }

  protected void engineSetMode(String paramString)
    throws NoSuchAlgorithmException
  {
    String str = Strings.toUpperCase(paramString);
    if ((str.equals("NONE")) || (str.equals("ECB")))
      return;
    if (str.equals("1"))
    {
      this.privateKeyOnly = true;
      this.publicKeyOnly = false;
      return;
    }
    if (str.equals("2"))
    {
      this.privateKeyOnly = false;
      this.publicKeyOnly = true;
      return;
    }
    throw new NoSuchAlgorithmException("can't support mode " + paramString);
  }

  protected void engineSetPadding(String paramString)
    throws NoSuchPaddingException
  {
    String str = Strings.toUpperCase(paramString);
    if (str.equals("NOPADDING"))
    {
      this.cipher = new RSABlindedEngine();
      return;
    }
    if (str.equals("PKCS1PADDING"))
    {
      this.cipher = new PKCS1Encoding(new RSABlindedEngine());
      return;
    }
    if (str.equals("ISO9796-1PADDING"))
    {
      this.cipher = new ISO9796d1Encoding(new RSABlindedEngine());
      return;
    }
    if (str.equals("OAEPWITHMD5ANDMGF1PADDING"))
    {
      initFromSpec(new OAEPParameterSpec("MD5", "MGF1", new MGF1ParameterSpec("MD5"), PSource.PSpecified.DEFAULT));
      return;
    }
    if (str.equals("OAEPPADDING"))
    {
      initFromSpec(OAEPParameterSpec.DEFAULT);
      return;
    }
    if ((str.equals("OAEPWITHSHA1ANDMGF1PADDING")) || (str.equals("OAEPWITHSHA-1ANDMGF1PADDING")))
    {
      initFromSpec(OAEPParameterSpec.DEFAULT);
      return;
    }
    if ((str.equals("OAEPWITHSHA224ANDMGF1PADDING")) || (str.equals("OAEPWITHSHA-224ANDMGF1PADDING")))
    {
      initFromSpec(new OAEPParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), PSource.PSpecified.DEFAULT));
      return;
    }
    if ((str.equals("OAEPWITHSHA256ANDMGF1PADDING")) || (str.equals("OAEPWITHSHA-256ANDMGF1PADDING")))
    {
      initFromSpec(new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
      return;
    }
    if ((str.equals("OAEPWITHSHA384ANDMGF1PADDING")) || (str.equals("OAEPWITHSHA-384ANDMGF1PADDING")))
    {
      initFromSpec(new OAEPParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
      return;
    }
    if ((str.equals("OAEPWITHSHA512ANDMGF1PADDING")) || (str.equals("OAEPWITHSHA-512ANDMGF1PADDING")))
    {
      initFromSpec(new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
      return;
    }
    throw new NoSuchPaddingException(paramString + " unavailable with RSA.");
  }

  protected int engineUpdate(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    this.bOut.write(paramArrayOfByte1, paramInt1, paramInt2);
    if ((this.cipher instanceof RSABlindedEngine))
    {
      if (this.bOut.size() > 1 + this.cipher.getInputBlockSize())
        throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    }
    else if (this.bOut.size() > this.cipher.getInputBlockSize())
      throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    return 0;
  }

  protected byte[] engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.bOut.write(paramArrayOfByte, paramInt1, paramInt2);
    if ((this.cipher instanceof RSABlindedEngine))
    {
      if (this.bOut.size() > 1 + this.cipher.getInputBlockSize())
        throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    }
    else if (this.bOut.size() > this.cipher.getInputBlockSize())
      throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
    return null;
  }

  public static class ISO9796d1Padding extends JCERSACipher
  {
    public ISO9796d1Padding()
    {
      super();
    }
  }

  public static class NoPadding extends JCERSACipher
  {
    public NoPadding()
    {
      super();
    }
  }

  public static class OAEPPadding extends JCERSACipher
  {
    public OAEPPadding()
    {
      super();
    }
  }

  public static class PKCS1v1_5Padding extends JCERSACipher
  {
    public PKCS1v1_5Padding()
    {
      super();
    }
  }

  public static class PKCS1v1_5Padding_PrivateOnly extends JCERSACipher
  {
    public PKCS1v1_5Padding_PrivateOnly()
    {
      super(true, new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class PKCS1v1_5Padding_PublicOnly extends JCERSACipher
  {
    public PKCS1v1_5Padding_PublicOnly()
    {
      super(false, new PKCS1Encoding(new RSABlindedEngine()));
    }
  }
}