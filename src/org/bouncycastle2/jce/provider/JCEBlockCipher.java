package org.bouncycastle2.jce.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.BufferedBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.engines.AESFastEngine;
import org.bouncycastle2.crypto.engines.DESEngine;
import org.bouncycastle2.crypto.engines.DESedeEngine;
import org.bouncycastle2.crypto.engines.GOST28147Engine;
import org.bouncycastle2.crypto.engines.RC2Engine;
import org.bouncycastle2.crypto.engines.TwofishEngine;
import org.bouncycastle2.crypto.modes.AEADBlockCipher;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.modes.CCMBlockCipher;
import org.bouncycastle2.crypto.modes.CFBBlockCipher;
import org.bouncycastle2.crypto.modes.CTSBlockCipher;
import org.bouncycastle2.crypto.modes.EAXBlockCipher;
import org.bouncycastle2.crypto.modes.GCMBlockCipher;
import org.bouncycastle2.crypto.modes.GOFBBlockCipher;
import org.bouncycastle2.crypto.modes.OFBBlockCipher;
import org.bouncycastle2.crypto.modes.OpenPGPCFBBlockCipher;
import org.bouncycastle2.crypto.modes.PGPCFBBlockCipher;
import org.bouncycastle2.crypto.modes.SICBlockCipher;
import org.bouncycastle2.crypto.paddings.BlockCipherPadding;
import org.bouncycastle2.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle2.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle2.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle2.crypto.paddings.TBCPadding;
import org.bouncycastle2.crypto.paddings.X923Padding;
import org.bouncycastle2.crypto.paddings.ZeroBytePadding;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.ParametersWithSBox;
import org.bouncycastle2.crypto.params.RC2Parameters;
import org.bouncycastle2.crypto.params.RC5Parameters;
import org.bouncycastle2.jce.spec.GOST28147ParameterSpec;
import org.bouncycastle2.util.Strings;

public class JCEBlockCipher extends WrapCipherSpi
  implements PBE
{
  private Class[] availableSpecs = { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class };
  private BlockCipher baseEngine;
  private GenericBlockCipher cipher;
  private int ivLength = 0;
  private ParametersWithIV ivParam;
  private String modeName = null;
  private boolean padded;
  private String pbeAlgorithm = null;
  private PBEParameterSpec pbeSpec = null;

  protected JCEBlockCipher(BlockCipher paramBlockCipher)
  {
    this.baseEngine = paramBlockCipher;
    this.cipher = new BufferedGenericBlockCipher(paramBlockCipher);
  }

  protected JCEBlockCipher(BlockCipher paramBlockCipher, int paramInt)
  {
    this.baseEngine = paramBlockCipher;
    this.cipher = new BufferedGenericBlockCipher(paramBlockCipher);
    this.ivLength = (paramInt / 8);
  }

  protected JCEBlockCipher(BufferedBlockCipher paramBufferedBlockCipher, int paramInt)
  {
    this.baseEngine = paramBufferedBlockCipher.getUnderlyingCipher();
    this.cipher = new BufferedGenericBlockCipher(paramBufferedBlockCipher);
    this.ivLength = (paramInt / 8);
  }

  private boolean isAEADModeName(String paramString)
  {
    return ("CCM".equals(paramString)) || ("EAX".equals(paramString)) || ("GCM".equals(paramString));
  }

  protected int engineDoFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws IllegalBlockSizeException, BadPaddingException
  {
    int i = 0;
    if (paramInt2 != 0)
      i = this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
    try
    {
      int j = this.cipher.doFinal(paramArrayOfByte2, paramInt3 + i);
      return j + i;
    }
    catch (DataLengthException localDataLengthException)
    {
      throw new IllegalBlockSizeException(localDataLengthException.getMessage());
    }
    catch (InvalidCipherTextException localInvalidCipherTextException)
    {
      throw new BadPaddingException(localInvalidCipherTextException.getMessage());
    }
  }

  protected byte[] engineDoFinal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IllegalBlockSizeException, BadPaddingException
  {
    byte[] arrayOfByte1 = new byte[engineGetOutputSize(paramInt2)];
    int i = 0;
    if (paramInt2 != 0)
      i = this.cipher.processBytes(paramArrayOfByte, paramInt1, paramInt2, arrayOfByte1, 0);
    int k;
    try
    {
      int j = this.cipher.doFinal(arrayOfByte1, i);
      k = i + j;
      if (k == arrayOfByte1.length)
        return arrayOfByte1;
    }
    catch (DataLengthException localDataLengthException)
    {
      throw new IllegalBlockSizeException(localDataLengthException.getMessage());
    }
    catch (InvalidCipherTextException localInvalidCipherTextException)
    {
      throw new BadPaddingException(localInvalidCipherTextException.getMessage());
    }
    byte[] arrayOfByte2 = new byte[k];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, k);
    return arrayOfByte2;
  }

  protected int engineGetBlockSize()
  {
    return this.baseEngine.getBlockSize();
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
    return this.cipher.getOutputSize(paramInt);
  }

  protected AlgorithmParameters engineGetParameters()
  {
    if ((this.engineParams != null) || (this.pbeSpec != null));
    while (true)
    {
      try
      {
        this.engineParams = AlgorithmParameters.getInstance(this.pbeAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        this.engineParams.init(this.pbeSpec);
        return this.engineParams;
      }
      catch (Exception localException2)
      {
        return null;
      }
      if (this.ivParam == null)
        continue;
      String str = this.cipher.getUnderlyingCipher().getAlgorithmName();
      if (str.indexOf('/') >= 0)
        str = str.substring(0, str.indexOf('/'));
      try
      {
        this.engineParams = AlgorithmParameters.getInstance(str, BouncyCastleProvider.PROVIDER_NAME);
        this.engineParams.init(this.ivParam.getIV());
      }
      catch (Exception localException1)
      {
        throw new RuntimeException(localException1.toString());
      }
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
    if ((paramAlgorithmParameterSpec == null) && (this.baseEngine.getAlgorithmName().startsWith("RC5-64")))
      throw new InvalidAlgorithmParameterException("RC5 requires an RC5ParametersSpec to be passed in.");
    JCEPBEKey localJCEPBEKey;
    Object localObject1;
    label156: label173: Object localObject2;
    if ((paramKey instanceof JCEPBEKey))
    {
      localJCEPBEKey = (JCEPBEKey)paramKey;
      if (localJCEPBEKey.getOID() != null)
      {
        this.pbeAlgorithm = localJCEPBEKey.getOID().getId();
        if (localJCEPBEKey.getParam() == null)
          break label366;
        localObject1 = localJCEPBEKey.getParam();
        this.pbeSpec = new PBEParameterSpec(localJCEPBEKey.getSalt(), localJCEPBEKey.getIterationCount());
        if ((localObject1 instanceof ParametersWithIV))
          this.ivParam = ((ParametersWithIV)localObject1);
        if ((this.ivLength == 0) || ((localObject1 instanceof ParametersWithIV)))
          break label1090;
        SecureRandom localSecureRandom = paramSecureRandom;
        if (localSecureRandom == null)
          localSecureRandom = new SecureRandom();
        if ((paramInt != 1) && (paramInt != 3))
          break label1023;
        byte[] arrayOfByte = new byte[this.ivLength];
        localSecureRandom.nextBytes(arrayOfByte);
        localObject2 = new ParametersWithIV((CipherParameters)localObject1, arrayOfByte);
        this.ivParam = ((ParametersWithIV)localObject2);
      }
    }
    while (true)
    {
      if ((paramSecureRandom != null) && (this.padded));
      for (Object localObject3 = new ParametersWithRandom((CipherParameters)localObject2, paramSecureRandom); ; localObject3 = localObject2)
        switch (paramInt)
        {
        default:
          try
          {
            throw new InvalidParameterException("unknown opmode " + paramInt + " passed");
          }
          catch (Exception localException)
          {
            throw new InvalidKeyException(localException.getMessage());
          }
          this.pbeAlgorithm = localJCEPBEKey.getAlgorithm();
          break;
          if ((paramAlgorithmParameterSpec instanceof PBEParameterSpec))
          {
            this.pbeSpec = ((PBEParameterSpec)paramAlgorithmParameterSpec);
            localObject1 = PBE.Util.makePBEParameters(localJCEPBEKey, paramAlgorithmParameterSpec, this.cipher.getUnderlyingCipher().getAlgorithmName());
            break label156;
          }
          throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
          if (paramAlgorithmParameterSpec == null)
          {
            localObject1 = new KeyParameter(paramKey.getEncoded());
            break label173;
          }
          if ((paramAlgorithmParameterSpec instanceof IvParameterSpec))
          {
            if (this.ivLength != 0)
            {
              IvParameterSpec localIvParameterSpec = (IvParameterSpec)paramAlgorithmParameterSpec;
              if ((localIvParameterSpec.getIV().length != this.ivLength) && (!isAEADModeName(this.modeName)))
                throw new InvalidAlgorithmParameterException("IV must be " + this.ivLength + " bytes long.");
              localObject1 = new ParametersWithIV(new KeyParameter(paramKey.getEncoded()), localIvParameterSpec.getIV());
              this.ivParam = ((ParametersWithIV)localObject1);
              break label173;
            }
            if ((this.modeName != null) && (this.modeName.equals("ECB")))
              throw new InvalidAlgorithmParameterException("ECB mode does not use an IV");
            localObject1 = new KeyParameter(paramKey.getEncoded());
            break label173;
          }
          if ((paramAlgorithmParameterSpec instanceof GOST28147ParameterSpec))
          {
            GOST28147ParameterSpec localGOST28147ParameterSpec = (GOST28147ParameterSpec)paramAlgorithmParameterSpec;
            localObject1 = new ParametersWithSBox(new KeyParameter(paramKey.getEncoded()), ((GOST28147ParameterSpec)paramAlgorithmParameterSpec).getSbox());
            if ((localGOST28147ParameterSpec.getIV() == null) || (this.ivLength == 0))
              break label173;
            ParametersWithIV localParametersWithIV3 = new ParametersWithIV((CipherParameters)localObject1, localGOST28147ParameterSpec.getIV());
            this.ivParam = ((ParametersWithIV)localParametersWithIV3);
            localObject1 = localParametersWithIV3;
            break label173;
          }
          if ((paramAlgorithmParameterSpec instanceof RC2ParameterSpec))
          {
            RC2ParameterSpec localRC2ParameterSpec = (RC2ParameterSpec)paramAlgorithmParameterSpec;
            localObject1 = new RC2Parameters(paramKey.getEncoded(), ((RC2ParameterSpec)paramAlgorithmParameterSpec).getEffectiveKeyBits());
            if ((localRC2ParameterSpec.getIV() == null) || (this.ivLength == 0))
              break label173;
            ParametersWithIV localParametersWithIV2 = new ParametersWithIV((CipherParameters)localObject1, localRC2ParameterSpec.getIV());
            this.ivParam = ((ParametersWithIV)localParametersWithIV2);
            localObject1 = localParametersWithIV2;
            break label173;
          }
          if ((paramAlgorithmParameterSpec instanceof RC5ParameterSpec))
          {
            RC5ParameterSpec localRC5ParameterSpec = (RC5ParameterSpec)paramAlgorithmParameterSpec;
            localObject1 = new RC5Parameters(paramKey.getEncoded(), ((RC5ParameterSpec)paramAlgorithmParameterSpec).getRounds());
            if (this.baseEngine.getAlgorithmName().startsWith("RC5"))
            {
              if (this.baseEngine.getAlgorithmName().equals("RC5-32"))
              {
                if (localRC5ParameterSpec.getWordSize() != 32)
                  throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 32 not " + localRC5ParameterSpec.getWordSize() + ".");
              }
              else if ((this.baseEngine.getAlgorithmName().equals("RC5-64")) && (localRC5ParameterSpec.getWordSize() != 64))
                throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 64 not " + localRC5ParameterSpec.getWordSize() + ".");
            }
            else
              throw new InvalidAlgorithmParameterException("RC5 parameters passed to a cipher that is not RC5.");
            if ((localRC5ParameterSpec.getIV() == null) || (this.ivLength == 0))
              break label173;
            ParametersWithIV localParametersWithIV1 = new ParametersWithIV((CipherParameters)localObject1, localRC5ParameterSpec.getIV());
            this.ivParam = ((ParametersWithIV)localParametersWithIV1);
            localObject1 = localParametersWithIV1;
            break label173;
          }
          throw new InvalidAlgorithmParameterException("unknown parameter type.");
          if (this.cipher.getUnderlyingCipher().getAlgorithmName().indexOf("PGPCFB") >= 0)
            break label1090;
          throw new InvalidAlgorithmParameterException("no IV set when one expected");
        case 1:
        case 3:
          this.cipher.init(true, (CipherParameters)localObject3);
          return;
        case 2:
        case 4:
          label366: label1023: this.cipher.init(false, (CipherParameters)localObject3);
          return;
        }
      label1090: localObject2 = localObject1;
    }
  }

  protected void engineSetMode(String paramString)
    throws NoSuchAlgorithmException
  {
    this.modeName = Strings.toUpperCase(paramString);
    if (this.modeName.equals("ECB"))
    {
      this.ivLength = 0;
      this.cipher = new BufferedGenericBlockCipher(this.baseEngine);
      return;
    }
    if (this.modeName.equals("CBC"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new BufferedGenericBlockCipher(new CBCBlockCipher(this.baseEngine));
      return;
    }
    if (this.modeName.startsWith("OFB"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      if (this.modeName.length() != 3)
      {
        int j = Integer.parseInt(this.modeName.substring(3));
        this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, j));
        return;
      }
      this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
      return;
    }
    if (this.modeName.startsWith("CFB"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      if (this.modeName.length() != 3)
      {
        int i = Integer.parseInt(this.modeName.substring(3));
        this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, i));
        return;
      }
      this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
      return;
    }
    if (this.modeName.startsWith("PGP"))
    {
      boolean bool = this.modeName.equalsIgnoreCase("PGPCFBwithIV");
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new BufferedGenericBlockCipher(new PGPCFBBlockCipher(this.baseEngine, bool));
      return;
    }
    if (this.modeName.equalsIgnoreCase("OpenPGPCFB"))
    {
      this.ivLength = 0;
      this.cipher = new BufferedGenericBlockCipher(new OpenPGPCFBBlockCipher(this.baseEngine));
      return;
    }
    if (this.modeName.startsWith("SIC"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      if (this.ivLength < 16)
        throw new IllegalArgumentException("Warning: SIC-Mode can become a twotime-pad if the blocksize of the cipher is too small. Use a cipher with a block size of at least 128 bits (e.g. AES)");
      this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
      return;
    }
    if (this.modeName.startsWith("CTR"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
      return;
    }
    if (this.modeName.startsWith("GOFB"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new GOFBBlockCipher(this.baseEngine)));
      return;
    }
    if (this.modeName.startsWith("CTS"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(new CBCBlockCipher(this.baseEngine)));
      return;
    }
    if (this.modeName.startsWith("CCM"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new AEADGenericBlockCipher(new CCMBlockCipher(this.baseEngine));
      return;
    }
    if (this.modeName.startsWith("EAX"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new AEADGenericBlockCipher(new EAXBlockCipher(this.baseEngine));
      return;
    }
    if (this.modeName.startsWith("GCM"))
    {
      this.ivLength = this.baseEngine.getBlockSize();
      this.cipher = new AEADGenericBlockCipher(new GCMBlockCipher(this.baseEngine));
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
      if (this.cipher.wrapOnNoPadding())
        this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(this.cipher.getUnderlyingCipher()));
      return;
    }
    if (str.equals("WITHCTS"))
    {
      this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(this.cipher.getUnderlyingCipher()));
      return;
    }
    this.padded = true;
    if (isAEADModeName(this.modeName))
      throw new NoSuchPaddingException("Only NoPadding can be used with AEAD modes.");
    if ((str.equals("PKCS5PADDING")) || (str.equals("PKCS7PADDING")))
    {
      this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher());
      return;
    }
    if (str.equals("ZEROBYTEPADDING"))
    {
      this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ZeroBytePadding());
      return;
    }
    if ((str.equals("ISO10126PADDING")) || (str.equals("ISO10126-2PADDING")))
    {
      this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO10126d2Padding());
      return;
    }
    if ((str.equals("X9.23PADDING")) || (str.equals("X923PADDING")))
    {
      this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new X923Padding());
      return;
    }
    if ((str.equals("ISO7816-4PADDING")) || (str.equals("ISO9797-1PADDING")))
    {
      this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO7816d4Padding());
      return;
    }
    if (str.equals("TBCPADDING"))
    {
      this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new TBCPadding());
      return;
    }
    throw new NoSuchPaddingException("Padding " + paramString + " unknown.");
  }

  protected int engineUpdate(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws ShortBufferException
  {
    try
    {
      int i = this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
      return i;
    }
    catch (DataLengthException localDataLengthException)
    {
      throw new ShortBufferException(localDataLengthException.getMessage());
    }
  }

  protected byte[] engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = this.cipher.getUpdateOutputSize(paramInt2);
    if (i > 0)
    {
      byte[] arrayOfByte1 = new byte[i];
      int j = this.cipher.processBytes(paramArrayOfByte, paramInt1, paramInt2, arrayOfByte1, 0);
      if (j == 0)
        return null;
      if (j != arrayOfByte1.length)
      {
        byte[] arrayOfByte2 = new byte[j];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, j);
        return arrayOfByte2;
      }
      return arrayOfByte1;
    }
    this.cipher.processBytes(paramArrayOfByte, paramInt1, paramInt2, null, 0);
    return null;
  }

  private static class AEADGenericBlockCipher
    implements JCEBlockCipher.GenericBlockCipher
  {
    private AEADBlockCipher cipher;

    AEADGenericBlockCipher(AEADBlockCipher paramAEADBlockCipher)
    {
      this.cipher = paramAEADBlockCipher;
    }

    public int doFinal(byte[] paramArrayOfByte, int paramInt)
      throws IllegalStateException, InvalidCipherTextException
    {
      return this.cipher.doFinal(paramArrayOfByte, paramInt);
    }

    public String getAlgorithmName()
    {
      return this.cipher.getUnderlyingCipher().getAlgorithmName();
    }

    public int getOutputSize(int paramInt)
    {
      return this.cipher.getOutputSize(paramInt);
    }

    public BlockCipher getUnderlyingCipher()
    {
      return this.cipher.getUnderlyingCipher();
    }

    public int getUpdateOutputSize(int paramInt)
    {
      return this.cipher.getUpdateOutputSize(paramInt);
    }

    public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
      throws IllegalArgumentException
    {
      this.cipher.init(paramBoolean, paramCipherParameters);
    }

    public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
      throws DataLengthException
    {
      return this.cipher.processByte(paramByte, paramArrayOfByte, paramInt);
    }

    public int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
      throws DataLengthException
    {
      return this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
    }

    public boolean wrapOnNoPadding()
    {
      return false;
    }
  }

  private static class BufferedGenericBlockCipher
    implements JCEBlockCipher.GenericBlockCipher
  {
    private BufferedBlockCipher cipher;

    BufferedGenericBlockCipher(BlockCipher paramBlockCipher)
    {
      this.cipher = new PaddedBufferedBlockCipher(paramBlockCipher);
    }

    BufferedGenericBlockCipher(BlockCipher paramBlockCipher, BlockCipherPadding paramBlockCipherPadding)
    {
      this.cipher = new PaddedBufferedBlockCipher(paramBlockCipher, paramBlockCipherPadding);
    }

    BufferedGenericBlockCipher(BufferedBlockCipher paramBufferedBlockCipher)
    {
      this.cipher = paramBufferedBlockCipher;
    }

    public int doFinal(byte[] paramArrayOfByte, int paramInt)
      throws IllegalStateException, InvalidCipherTextException
    {
      return this.cipher.doFinal(paramArrayOfByte, paramInt);
    }

    public String getAlgorithmName()
    {
      return this.cipher.getUnderlyingCipher().getAlgorithmName();
    }

    public int getOutputSize(int paramInt)
    {
      return this.cipher.getOutputSize(paramInt);
    }

    public BlockCipher getUnderlyingCipher()
    {
      return this.cipher.getUnderlyingCipher();
    }

    public int getUpdateOutputSize(int paramInt)
    {
      return this.cipher.getUpdateOutputSize(paramInt);
    }

    public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
      throws IllegalArgumentException
    {
      this.cipher.init(paramBoolean, paramCipherParameters);
    }

    public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
      throws DataLengthException
    {
      return this.cipher.processByte(paramByte, paramArrayOfByte, paramInt);
    }

    public int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
      throws DataLengthException
    {
      return this.cipher.processBytes(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
    }

    public boolean wrapOnNoPadding()
    {
      return !(this.cipher instanceof CTSBlockCipher);
    }
  }

  public static class DES extends JCEBlockCipher
  {
    public DES()
    {
      super();
    }
  }

  public static class DESCBC extends JCEBlockCipher
  {
    public DESCBC()
    {
      super(64);
    }
  }

  public static class GOST28147 extends JCEBlockCipher
  {
    public GOST28147()
    {
      super();
    }
  }

  public static class GOST28147cbc extends JCEBlockCipher
  {
    public GOST28147cbc()
    {
      super(64);
    }
  }

  private static abstract interface GenericBlockCipher
  {
    public abstract int doFinal(byte[] paramArrayOfByte, int paramInt)
      throws IllegalStateException, InvalidCipherTextException;

    public abstract String getAlgorithmName();

    public abstract int getOutputSize(int paramInt);

    public abstract BlockCipher getUnderlyingCipher();

    public abstract int getUpdateOutputSize(int paramInt);

    public abstract void init(boolean paramBoolean, CipherParameters paramCipherParameters)
      throws IllegalArgumentException;

    public abstract int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
      throws DataLengthException;

    public abstract int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
      throws DataLengthException;

    public abstract boolean wrapOnNoPadding();
  }

  public static class PBEWithAESCBC extends JCEBlockCipher
  {
    public PBEWithAESCBC()
    {
      super();
    }
  }

  public static class PBEWithMD5AndDES extends JCEBlockCipher
  {
    public PBEWithMD5AndDES()
    {
      super();
    }
  }

  public static class PBEWithMD5AndRC2 extends JCEBlockCipher
  {
    public PBEWithMD5AndRC2()
    {
      super();
    }
  }

  public static class PBEWithSHA1AndDES extends JCEBlockCipher
  {
    public PBEWithSHA1AndDES()
    {
      super();
    }
  }

  public static class PBEWithSHA1AndRC2 extends JCEBlockCipher
  {
    public PBEWithSHA1AndRC2()
    {
      super();
    }
  }

  public static class PBEWithSHAAnd128BitRC2 extends JCEBlockCipher
  {
    public PBEWithSHAAnd128BitRC2()
    {
      super();
    }
  }

  public static class PBEWithSHAAnd40BitRC2 extends JCEBlockCipher
  {
    public PBEWithSHAAnd40BitRC2()
    {
      super();
    }
  }

  public static class PBEWithSHAAndDES2Key extends JCEBlockCipher
  {
    public PBEWithSHAAndDES2Key()
    {
      super();
    }
  }

  public static class PBEWithSHAAndDES3Key extends JCEBlockCipher
  {
    public PBEWithSHAAndDES3Key()
    {
      super();
    }
  }

  public static class PBEWithSHAAndTwofish extends JCEBlockCipher
  {
    public PBEWithSHAAndTwofish()
    {
      super();
    }
  }

  public static class RC2 extends JCEBlockCipher
  {
    public RC2()
    {
      super();
    }
  }

  public static class RC2CBC extends JCEBlockCipher
  {
    public RC2CBC()
    {
      super(64);
    }
  }
}