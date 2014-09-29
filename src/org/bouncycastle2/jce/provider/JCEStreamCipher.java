package org.bouncycastle2.jce.provider;

import java.io.PrintStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.StreamBlockCipher;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.engines.BlowfishEngine;
import org.bouncycastle2.crypto.engines.DESEngine;
import org.bouncycastle2.crypto.engines.DESedeEngine;
import org.bouncycastle2.crypto.engines.RC4Engine;
import org.bouncycastle2.crypto.engines.SkipjackEngine;
import org.bouncycastle2.crypto.engines.TwofishEngine;
import org.bouncycastle2.crypto.modes.CFBBlockCipher;
import org.bouncycastle2.crypto.modes.OFBBlockCipher;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class JCEStreamCipher extends WrapCipherSpi
  implements PBE
{
  private Class[] availableSpecs = { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
  private StreamCipher cipher;
  private int ivLength = 0;
  private ParametersWithIV ivParam;
  private String pbeAlgorithm = null;
  private PBEParameterSpec pbeSpec = null;

  protected JCEStreamCipher(BlockCipher paramBlockCipher, int paramInt)
  {
    this.ivLength = paramInt;
    this.cipher = new StreamBlockCipher(paramBlockCipher);
  }

  protected JCEStreamCipher(StreamCipher paramStreamCipher, int paramInt)
  {
    this.cipher = paramStreamCipher;
    this.ivLength = paramInt;
  }

  protected int engineDoFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (paramInt2 != 0)
      this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
    this.cipher.reset();
    return paramInt2;
  }

  protected byte[] engineDoFinal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      byte[] arrayOfByte = engineUpdate(paramArrayOfByte, paramInt1, paramInt2);
      this.cipher.reset();
      return arrayOfByte;
    }
    this.cipher.reset();
    return new byte[0];
  }

  protected int engineGetBlockSize()
  {
    return 0;
  }

  protected byte[] engineGetIV()
  {
    if (this.ivParam != null)
      return this.ivParam.getIV();
    return null;
  }

  protected int engineGetKeySize(Key paramKey)
  {
    return 8 * paramKey.getEncoded().length;
  }

  protected int engineGetOutputSize(int paramInt)
  {
    return paramInt;
  }

  protected AlgorithmParameters engineGetParameters()
  {
    if ((this.engineParams == null) && (this.pbeSpec != null))
      try
      {
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance(this.pbeAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        localAlgorithmParameters.init(this.pbeSpec);
        return localAlgorithmParameters;
      }
      catch (Exception localException)
      {
        return null;
      }
    return this.engineParams;
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
    label87: engineInit(paramInt, paramKey, localObject, paramSecureRandom);
    this.engineParams = paramAlgorithmParameters;
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
      throw new InvalidKeyException(localInvalidAlgorithmParameterException.getMessage());
    }
  }

  protected void engineInit(int paramInt, Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    this.pbeSpec = null;
    this.pbeAlgorithm = null;
    this.engineParams = null;
    if (!(paramKey instanceof SecretKey))
      throw new InvalidKeyException("Key for algorithm " + paramKey.getAlgorithm() + " not suitable for symmetric enryption.");
    JCEPBEKey localJCEPBEKey;
    label89: Object localObject;
    if ((paramKey instanceof JCEPBEKey))
    {
      localJCEPBEKey = (JCEPBEKey)paramKey;
      if (localJCEPBEKey.getOID() != null)
      {
        this.pbeAlgorithm = localJCEPBEKey.getOID().getId();
        if (localJCEPBEKey.getParam() == null)
          break label277;
        localObject = localJCEPBEKey.getParam();
        this.pbeSpec = new PBEParameterSpec(localJCEPBEKey.getSalt(), localJCEPBEKey.getIterationCount());
        label125: if (localJCEPBEKey.getIvSize() != 0)
          this.ivParam = ((ParametersWithIV)localObject);
      }
    }
    while (true)
    {
      if ((this.ivLength != 0) && (!(localObject instanceof ParametersWithIV)))
      {
        SecureRandom localSecureRandom = paramSecureRandom;
        if (localSecureRandom == null)
          localSecureRandom = new SecureRandom();
        if ((paramInt != 1) && (paramInt != 3))
          break label402;
        byte[] arrayOfByte = new byte[this.ivLength];
        localSecureRandom.nextBytes(arrayOfByte);
        ParametersWithIV localParametersWithIV = new ParametersWithIV((CipherParameters)localObject, arrayOfByte);
        this.ivParam = ((ParametersWithIV)localParametersWithIV);
        localObject = localParametersWithIV;
      }
      switch (paramInt)
      {
      default:
        System.out.println("eeek!");
        return;
        this.pbeAlgorithm = localJCEPBEKey.getAlgorithm();
        break label89;
        label277: if ((paramAlgorithmParameterSpec instanceof PBEParameterSpec))
        {
          localObject = PBE.Util.makePBEParameters(localJCEPBEKey, paramAlgorithmParameterSpec, this.cipher.getAlgorithmName());
          this.pbeSpec = ((PBEParameterSpec)paramAlgorithmParameterSpec);
          break label125;
        }
        throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
        if (paramAlgorithmParameterSpec == null)
        {
          localObject = new KeyParameter(paramKey.getEncoded());
        }
        else
        {
          if (!(paramAlgorithmParameterSpec instanceof IvParameterSpec))
            break label392;
          localObject = new ParametersWithIV(new KeyParameter(paramKey.getEncoded()), ((IvParameterSpec)paramAlgorithmParameterSpec).getIV());
          this.ivParam = ((ParametersWithIV)localObject);
        }
        break;
      case 1:
      case 3:
      case 2:
      case 4:
      }
    }
    label392: throw new IllegalArgumentException("unknown parameter type.");
    label402: throw new InvalidAlgorithmParameterException("no IV set when one expected");
    this.cipher.init(true, (CipherParameters)localObject);
    return;
    this.cipher.init(false, (CipherParameters)localObject);
  }

  protected void engineSetMode(String paramString)
  {
    if (!paramString.equalsIgnoreCase("ECB"))
      throw new IllegalArgumentException("can't support mode " + paramString);
  }

  protected void engineSetPadding(String paramString)
    throws NoSuchPaddingException
  {
    if (!paramString.equalsIgnoreCase("NoPadding"))
      throw new NoSuchPaddingException("Padding " + paramString + " unknown.");
  }

  protected int engineUpdate(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws ShortBufferException
  {
    try
    {
      this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
      return paramInt2;
    }
    catch (DataLengthException localDataLengthException)
    {
      throw new ShortBufferException(localDataLengthException.getMessage());
    }
  }

  protected byte[] engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    this.cipher.processBytes(paramArrayOfByte, paramInt1, paramInt2, arrayOfByte, 0);
    return arrayOfByte;
  }

  public static class Blowfish_CFB8 extends JCEStreamCipher
  {
    public Blowfish_CFB8()
    {
      super(64);
    }
  }

  public static class Blowfish_OFB8 extends JCEStreamCipher
  {
    public Blowfish_OFB8()
    {
      super(64);
    }
  }

  public static class DES_CFB8 extends JCEStreamCipher
  {
    public DES_CFB8()
    {
      super(64);
    }
  }

  public static class DES_OFB8 extends JCEStreamCipher
  {
    public DES_OFB8()
    {
      super(64);
    }
  }

  public static class DESede_CFB8 extends JCEStreamCipher
  {
    public DESede_CFB8()
    {
      super(64);
    }
  }

  public static class DESede_OFB8 extends JCEStreamCipher
  {
    public DESede_OFB8()
    {
      super(64);
    }
  }

  public static class PBEWithSHAAnd128BitRC4 extends JCEStreamCipher
  {
    public PBEWithSHAAnd128BitRC4()
    {
      super(0);
    }
  }

  public static class PBEWithSHAAnd40BitRC4 extends JCEStreamCipher
  {
    public PBEWithSHAAnd40BitRC4()
    {
      super(0);
    }
  }

  public static class Skipjack_CFB8 extends JCEStreamCipher
  {
    public Skipjack_CFB8()
    {
      super(64);
    }
  }

  public static class Skipjack_OFB8 extends JCEStreamCipher
  {
    public Skipjack_OFB8()
    {
      super(64);
    }
  }

  public static class Twofish_CFB8 extends JCEStreamCipher
  {
    public Twofish_CFB8()
    {
      super(128);
    }
  }

  public static class Twofish_OFB8 extends JCEStreamCipher
  {
    public Twofish_OFB8()
    {
      super(128);
    }
  }
}