package org.bouncycastle2.jce.provider;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.interfaces.DHPrivateKey;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.agreement.DHBasicAgreement;
import org.bouncycastle2.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.engines.IESEngine;
import org.bouncycastle2.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle2.crypto.macs.HMac;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.IESParameters;
import org.bouncycastle2.jce.interfaces.ECPrivateKey;
import org.bouncycastle2.jce.interfaces.ECPublicKey;
import org.bouncycastle2.jce.interfaces.IESKey;
import org.bouncycastle2.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle2.jce.spec.IESParameterSpec;

public class JCEIESCipher extends WrapCipherSpi
{
  private Class[] availableSpecs = { IESParameterSpec.class };
  private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private IESEngine cipher;
  private AlgorithmParameters engineParam = null;
  private IESParameterSpec engineParams = null;
  private int state = -1;

  public JCEIESCipher(IESEngine paramIESEngine)
  {
    this.cipher = paramIESEngine;
  }

  protected int engineDoFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws IllegalBlockSizeException, BadPaddingException
  {
    if (paramInt2 != 0)
      this.buffer.write(paramArrayOfByte1, paramInt1, paramInt2);
    try
    {
      byte[] arrayOfByte1 = this.buffer.toByteArray();
      this.buffer.reset();
      byte[] arrayOfByte2 = this.cipher.processBlock(arrayOfByte1, 0, arrayOfByte1.length);
      System.arraycopy(arrayOfByte2, 0, paramArrayOfByte2, paramInt3, arrayOfByte2.length);
      int i = arrayOfByte2.length;
      return i;
    }
    catch (InvalidCipherTextException localInvalidCipherTextException)
    {
      throw new BadPaddingException(localInvalidCipherTextException.getMessage());
    }
  }

  protected byte[] engineDoFinal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IllegalBlockSizeException, BadPaddingException
  {
    if (paramInt2 != 0)
      this.buffer.write(paramArrayOfByte, paramInt1, paramInt2);
    try
    {
      byte[] arrayOfByte1 = this.buffer.toByteArray();
      this.buffer.reset();
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
    return 0;
  }

  protected byte[] engineGetIV()
  {
    return null;
  }

  protected int engineGetKeySize(Key paramKey)
  {
    if (!(paramKey instanceof IESKey))
      throw new IllegalArgumentException("must be passed IE key");
    IESKey localIESKey = (IESKey)paramKey;
    if ((localIESKey.getPrivate() instanceof DHPrivateKey))
      return ((DHPrivateKey)localIESKey.getPrivate()).getX().bitLength();
    if ((localIESKey.getPrivate() instanceof ECPrivateKey))
      return ((ECPrivateKey)localIESKey.getPrivate()).getD().bitLength();
    throw new IllegalArgumentException("not an IE key!");
  }

  protected int engineGetOutputSize(int paramInt)
  {
    if ((this.state == 1) || (this.state == 3))
      return 20 + (paramInt + this.buffer.size());
    if ((this.state == 2) || (this.state == 4))
      return -20 + (paramInt + this.buffer.size());
    throw new IllegalStateException("cipher not initialised");
  }

  protected AlgorithmParameters engineGetParameters()
  {
    if ((this.engineParam == null) && (this.engineParams != null));
    try
    {
      this.engineParam = AlgorithmParameters.getInstance("IES", BouncyCastleProvider.PROVIDER_NAME);
      this.engineParam.init(this.engineParams);
      return this.engineParam;
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
    if (paramAlgorithmParameters != null)
    {
      int i = 0;
      while (true)
      {
        int j = this.availableSpecs.length;
        localObject = null;
        if (i == j);
        while (true)
        {
          if (localObject != null)
            break label87;
          throw new InvalidAlgorithmParameterException("can't handle parameter " + paramAlgorithmParameters.toString());
          try
          {
            AlgorithmParameterSpec localAlgorithmParameterSpec = paramAlgorithmParameters.getParameterSpec(this.availableSpecs[i]);
            localObject = localAlgorithmParameterSpec;
          }
          catch (Exception localException)
          {
            i++;
          }
        }
      }
    }
    label87: this.engineParam = paramAlgorithmParameters;
    engineInit(paramInt, paramKey, localObject, paramSecureRandom);
  }

  protected void engineInit(int paramInt, Key paramKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    if ((paramInt == 1) || (paramInt == 3))
      try
      {
        engineInit(paramInt, paramKey, null, paramSecureRandom);
        return;
      }
      catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
      {
      }
    throw new IllegalArgumentException("can't handle null parameter spec in IES");
  }

  protected void engineInit(int paramInt, Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    if (!(paramKey instanceof IESKey))
      throw new InvalidKeyException("must be passed IES key");
    label86: IESKey localIESKey;
    AsymmetricKeyParameter localAsymmetricKeyParameter1;
    if ((paramAlgorithmParameterSpec == null) && ((paramInt == 1) || (paramInt == 3)))
    {
      byte[] arrayOfByte1 = new byte[16];
      byte[] arrayOfByte2 = new byte[16];
      if (paramSecureRandom == null)
        paramSecureRandom = new SecureRandom();
      paramSecureRandom.nextBytes(arrayOfByte1);
      paramSecureRandom.nextBytes(arrayOfByte2);
      paramAlgorithmParameterSpec = new IESParameterSpec(arrayOfByte1, arrayOfByte2, 128);
      localIESKey = (IESKey)paramKey;
      if (!(localIESKey.getPublic() instanceof ECPublicKey))
        break label238;
      localAsymmetricKeyParameter1 = ECUtil.generatePublicKeyParameter(localIESKey.getPublic());
    }
    IESParameters localIESParameters;
    for (AsymmetricKeyParameter localAsymmetricKeyParameter2 = ECUtil.generatePrivateKeyParameter(localIESKey.getPrivate()); ; localAsymmetricKeyParameter2 = DHUtil.generatePrivateKeyParameter(localIESKey.getPrivate()))
    {
      this.engineParams = ((IESParameterSpec)paramAlgorithmParameterSpec);
      localIESParameters = new IESParameters(this.engineParams.getDerivationV(), this.engineParams.getEncodingV(), this.engineParams.getMacKeySize());
      this.state = paramInt;
      this.buffer.reset();
      switch (paramInt)
      {
      default:
        System.out.println("eeek!");
        return;
        if ((paramAlgorithmParameterSpec instanceof IESParameterSpec))
          break label86;
        throw new InvalidAlgorithmParameterException("must be passed IES parameters");
        label238: localAsymmetricKeyParameter1 = DHUtil.generatePublicKeyParameter(localIESKey.getPublic());
      case 1:
      case 3:
      case 2:
      case 4:
      }
    }
    this.cipher.init(true, localAsymmetricKeyParameter2, localAsymmetricKeyParameter1, localIESParameters);
    return;
    this.cipher.init(false, localAsymmetricKeyParameter2, localAsymmetricKeyParameter1, localIESParameters);
  }

  protected void engineSetMode(String paramString)
  {
    throw new IllegalArgumentException("can't support mode " + paramString);
  }

  protected void engineSetPadding(String paramString)
    throws NoSuchPaddingException
  {
    throw new NoSuchPaddingException(paramString + " unavailable with RSA.");
  }

  protected int engineUpdate(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    this.buffer.write(paramArrayOfByte1, paramInt1, paramInt2);
    return 0;
  }

  protected byte[] engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.buffer.write(paramArrayOfByte, paramInt1, paramInt2);
    return null;
  }

  public static class BrokenECIES extends JCEIESCipher
  {
    public BrokenECIES()
    {
      super();
    }
  }

  public static class BrokenIES extends JCEIESCipher
  {
    public BrokenIES()
    {
      super();
    }
  }

  public static class ECIES extends JCEIESCipher
  {
    public ECIES()
    {
      super();
    }
  }

  public static class IES extends JCEIESCipher
  {
    public IES()
    {
      super();
    }
  }
}