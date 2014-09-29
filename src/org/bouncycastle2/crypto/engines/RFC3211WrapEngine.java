package org.bouncycastle2.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.Wrapper;
import org.bouncycastle2.crypto.modes.CBCBlockCipher;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class RFC3211WrapEngine
  implements Wrapper
{
  private CBCBlockCipher engine;
  private boolean forWrapping;
  private ParametersWithIV param;
  private SecureRandom rand;

  public RFC3211WrapEngine(BlockCipher paramBlockCipher)
  {
    this.engine = new CBCBlockCipher(paramBlockCipher);
  }

  public String getAlgorithmName()
  {
    return this.engine.getUnderlyingCipher().getAlgorithmName() + "/RFC3211Wrap";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.forWrapping = paramBoolean;
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      ParametersWithRandom localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.rand = localParametersWithRandom.getRandom();
      this.param = ((ParametersWithIV)localParametersWithRandom.getParameters());
      return;
    }
    if (paramBoolean)
      this.rand = new SecureRandom();
    this.param = ((ParametersWithIV)paramCipherParameters);
  }

  public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.forWrapping)
      throw new IllegalStateException("not set for unwrapping");
    int i = this.engine.getBlockSize();
    if (paramInt2 < i * 2)
      throw new InvalidCipherTextException("input too short");
    byte[] arrayOfByte1 = new byte[paramInt2];
    byte[] arrayOfByte2 = new byte[i];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, paramInt2);
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte2, 0, arrayOfByte2.length);
    this.engine.init(false, new ParametersWithIV(this.param.getParameters(), arrayOfByte2));
    int j = i;
    int k;
    if (j >= arrayOfByte1.length)
    {
      System.arraycopy(arrayOfByte1, arrayOfByte1.length - arrayOfByte2.length, arrayOfByte2, 0, arrayOfByte2.length);
      this.engine.init(false, new ParametersWithIV(this.param.getParameters(), arrayOfByte2));
      this.engine.processBlock(arrayOfByte1, 0, arrayOfByte1, 0);
      this.engine.init(false, this.param);
      k = 0;
    }
    while (true)
    {
      if (k >= arrayOfByte1.length)
      {
        if ((0xFF & arrayOfByte1[0]) <= -4 + arrayOfByte1.length)
          break label269;
        throw new InvalidCipherTextException("wrapped key corrupted");
        this.engine.processBlock(arrayOfByte1, j, arrayOfByte1, j);
        j += i;
        break;
      }
      this.engine.processBlock(arrayOfByte1, k, arrayOfByte1, k);
      k += i;
    }
    label269: byte[] arrayOfByte3 = new byte[0xFF & arrayOfByte1[0]];
    System.arraycopy(arrayOfByte1, 4, arrayOfByte3, 0, arrayOfByte1[0]);
    int m = 0;
    for (int n = 0; ; n++)
    {
      if (n == 3)
      {
        if (m == 0)
          break;
        throw new InvalidCipherTextException("wrapped key fails checksum");
      }
      m |= (byte)(0xFFFFFFFF ^ arrayOfByte1[(n + 1)]) ^ arrayOfByte3[n];
    }
    return arrayOfByte3;
  }

  public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (!this.forWrapping)
      throw new IllegalStateException("not set for wrapping");
    this.engine.init(true, this.param);
    int i = this.engine.getBlockSize();
    byte[] arrayOfByte;
    int k;
    label110: int m;
    label121: int n;
    if (paramInt2 + 4 < i * 2)
    {
      arrayOfByte = new byte[i * 2];
      arrayOfByte[0] = ((byte)paramInt2);
      arrayOfByte[1] = ((byte)(0xFFFFFFFF ^ paramArrayOfByte[paramInt1]));
      arrayOfByte[2] = ((byte)(0xFFFFFFFF ^ paramArrayOfByte[(paramInt1 + 1)]));
      arrayOfByte[3] = ((byte)(0xFFFFFFFF ^ paramArrayOfByte[(paramInt1 + 2)]));
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 4, paramInt2);
      k = paramInt2 + 4;
      if (k < arrayOfByte.length)
        break label182;
      m = 0;
      if (m < arrayOfByte.length)
        break label201;
      n = 0;
    }
    while (true)
    {
      if (n >= arrayOfByte.length)
      {
        return arrayOfByte;
        if ((paramInt2 + 4) % i == 0);
        for (int j = paramInt2 + 4; ; j = i * (1 + (paramInt2 + 4) / i))
        {
          arrayOfByte = new byte[j];
          break;
        }
        label182: arrayOfByte[k] = ((byte)this.rand.nextInt());
        k++;
        break label110;
        label201: this.engine.processBlock(arrayOfByte, m, arrayOfByte, m);
        m += i;
        break label121;
      }
      this.engine.processBlock(arrayOfByte, n, arrayOfByte, n);
      n += i;
    }
  }
}