package org.bouncycastle2.crypto.encodings;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class PKCS1Encoding
  implements AsymmetricBlockCipher
{
  private static final int HEADER_LENGTH = 10;
  public static final String STRICT_LENGTH_ENABLED_PROPERTY = "org.bouncycastle2.pkcs1.strict";
  private AsymmetricBlockCipher engine;
  private boolean forEncryption;
  private boolean forPrivateKey;
  private SecureRandom random;
  private boolean useStrictLength;

  public PKCS1Encoding(AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.engine = paramAsymmetricBlockCipher;
    this.useStrictLength = useStrict();
  }

  private byte[] decodeBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    byte[] arrayOfByte1 = this.engine.processBlock(paramArrayOfByte, paramInt1, paramInt2);
    if (arrayOfByte1.length < getOutputBlockSize())
      throw new InvalidCipherTextException("block truncated");
    int i = arrayOfByte1[0];
    if ((i != 1) && (i != 2))
      throw new InvalidCipherTextException("unknown block type");
    if ((this.useStrictLength) && (arrayOfByte1.length != this.engine.getOutputBlockSize()))
      throw new InvalidCipherTextException("block incorrect size");
    int m;
    for (int j = 1; ; j++)
    {
      if (j == arrayOfByte1.length);
      int k;
      do
      {
        m = j + 1;
        if ((m <= arrayOfByte1.length) && (m >= 10))
          break;
        throw new InvalidCipherTextException("no data in block");
        k = arrayOfByte1[j];
      }
      while (k == 0);
      if ((i == 1) && (k != -1))
        throw new InvalidCipherTextException("block padding incorrect");
    }
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length - m];
    System.arraycopy(arrayOfByte1, m, arrayOfByte2, 0, arrayOfByte2.length);
    return arrayOfByte2;
  }

  private byte[] encodeBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (paramInt2 > getInputBlockSize())
      throw new IllegalArgumentException("input data too large");
    byte[] arrayOfByte = new byte[this.engine.getInputBlockSize()];
    if (this.forPrivateKey)
    {
      arrayOfByte[0] = 1;
      for (int j = 1; ; j++)
      {
        if (j == -1 + (arrayOfByte.length - paramInt2))
        {
          arrayOfByte[(-1 + (arrayOfByte.length - paramInt2))] = 0;
          System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, arrayOfByte.length - paramInt2, paramInt2);
          return this.engine.processBlock(arrayOfByte, 0, arrayOfByte.length);
        }
        arrayOfByte[j] = -1;
      }
    }
    this.random.nextBytes(arrayOfByte);
    arrayOfByte[0] = 2;
    int i = 1;
    label127: if (i != -1 + (arrayOfByte.length - paramInt2));
    while (true)
    {
      if (arrayOfByte[i] != 0)
      {
        i++;
        break label127;
        break;
      }
      arrayOfByte[i] = ((byte)this.random.nextInt());
    }
  }

  private boolean useStrict()
  {
    String str = (String)AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run()
      {
        return System.getProperty("org.bouncycastle2.pkcs1.strict");
      }
    });
    return (str == null) || (str.equals("true"));
  }

  public int getInputBlockSize()
  {
    int i = this.engine.getInputBlockSize();
    if (this.forEncryption)
      i -= 10;
    return i;
  }

  public int getOutputBlockSize()
  {
    int i = this.engine.getOutputBlockSize();
    if (this.forEncryption)
      return i;
    return i - 10;
  }

  public AsymmetricBlockCipher getUnderlyingCipher()
  {
    return this.engine;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    ParametersWithRandom localParametersWithRandom;
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.random = localParametersWithRandom.getRandom();
    }
    for (AsymmetricKeyParameter localAsymmetricKeyParameter = (AsymmetricKeyParameter)localParametersWithRandom.getParameters(); ; localAsymmetricKeyParameter = (AsymmetricKeyParameter)paramCipherParameters)
    {
      this.engine.init(paramBoolean, paramCipherParameters);
      this.forPrivateKey = localAsymmetricKeyParameter.isPrivate();
      this.forEncryption = paramBoolean;
      return;
      this.random = new SecureRandom();
    }
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.forEncryption)
      return encodeBlock(paramArrayOfByte, paramInt1, paramInt2);
    return decodeBlock(paramArrayOfByte, paramInt1, paramInt2);
  }
}