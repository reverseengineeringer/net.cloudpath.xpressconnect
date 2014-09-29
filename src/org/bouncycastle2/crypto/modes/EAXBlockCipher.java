package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.macs.CMac;
import org.bouncycastle2.crypto.params.AEADParameters;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.util.Arrays;

public class EAXBlockCipher
  implements AEADBlockCipher
{
  private static final byte cTAG = 2;
  private static final byte hTAG = 1;
  private static final byte nTAG;
  private byte[] associatedTextMac;
  private int blockSize;
  private byte[] bufBlock;
  private int bufOff;
  private SICBlockCipher cipher;
  private boolean forEncryption;
  private Mac mac;
  private byte[] macBlock;
  private int macSize;
  private byte[] nonceMac;

  public EAXBlockCipher(BlockCipher paramBlockCipher)
  {
    this.blockSize = paramBlockCipher.getBlockSize();
    this.mac = new CMac(paramBlockCipher);
    this.macBlock = new byte[this.blockSize];
    this.bufBlock = new byte[2 * this.blockSize];
    this.associatedTextMac = new byte[this.mac.getMacSize()];
    this.nonceMac = new byte[this.mac.getMacSize()];
    this.cipher = new SICBlockCipher(paramBlockCipher);
  }

  private void calculateMac()
  {
    byte[] arrayOfByte = new byte[this.blockSize];
    this.mac.doFinal(arrayOfByte, 0);
    for (int i = 0; ; i++)
    {
      if (i >= this.macBlock.length)
        return;
      this.macBlock[i] = ((byte)(this.nonceMac[i] ^ this.associatedTextMac[i] ^ arrayOfByte[i]));
    }
  }

  private int process(byte paramByte, byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = this.bufBlock;
    int i = this.bufOff;
    this.bufOff = (i + 1);
    arrayOfByte[i] = paramByte;
    if (this.bufOff == this.bufBlock.length)
    {
      int j;
      if (this.forEncryption)
      {
        j = this.cipher.processBlock(this.bufBlock, 0, paramArrayOfByte, paramInt);
        this.mac.update(paramArrayOfByte, paramInt, this.blockSize);
      }
      while (true)
      {
        this.bufOff = this.blockSize;
        System.arraycopy(this.bufBlock, this.blockSize, this.bufBlock, 0, this.blockSize);
        return j;
        this.mac.update(this.bufBlock, 0, this.blockSize);
        j = this.cipher.processBlock(this.bufBlock, 0, paramArrayOfByte, paramInt);
      }
    }
    return 0;
  }

  private void reset(boolean paramBoolean)
  {
    this.cipher.reset();
    this.mac.reset();
    this.bufOff = 0;
    Arrays.fill(this.bufBlock, (byte)0);
    if (paramBoolean)
      Arrays.fill(this.macBlock, (byte)0);
    byte[] arrayOfByte = new byte[this.blockSize];
    arrayOfByte[(-1 + this.blockSize)] = 2;
    this.mac.update(arrayOfByte, 0, this.blockSize);
  }

  private boolean verifyMac(byte[] paramArrayOfByte, int paramInt)
  {
    for (int i = 0; ; i++)
    {
      if (i >= this.macSize)
        return true;
      if (this.macBlock[i] != paramArrayOfByte[(paramInt + i)])
        return false;
    }
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws IllegalStateException, InvalidCipherTextException
  {
    int i = this.bufOff;
    byte[] arrayOfByte = new byte[this.bufBlock.length];
    this.bufOff = 0;
    if (this.forEncryption)
    {
      this.cipher.processBlock(this.bufBlock, 0, arrayOfByte, 0);
      this.cipher.processBlock(this.bufBlock, this.blockSize, arrayOfByte, this.blockSize);
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, i);
      this.mac.update(arrayOfByte, 0, i);
      calculateMac();
      System.arraycopy(this.macBlock, 0, paramArrayOfByte, paramInt + i, this.macSize);
      reset(false);
      return i + this.macSize;
    }
    if (i > this.macSize)
    {
      this.mac.update(this.bufBlock, 0, i - this.macSize);
      this.cipher.processBlock(this.bufBlock, 0, arrayOfByte, 0);
      this.cipher.processBlock(this.bufBlock, this.blockSize, arrayOfByte, this.blockSize);
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, i - this.macSize);
    }
    calculateMac();
    if (!verifyMac(this.bufBlock, i - this.macSize))
      throw new InvalidCipherTextException("mac check in EAX failed");
    reset(false);
    return i - this.macSize;
  }

  public String getAlgorithmName()
  {
    return this.cipher.getUnderlyingCipher().getAlgorithmName() + "/EAX";
  }

  public int getBlockSize()
  {
    return this.cipher.getBlockSize();
  }

  public byte[] getMac()
  {
    byte[] arrayOfByte = new byte[this.macSize];
    System.arraycopy(this.macBlock, 0, arrayOfByte, 0, this.macSize);
    return arrayOfByte;
  }

  public int getOutputSize(int paramInt)
  {
    if (this.forEncryption)
      return paramInt + this.bufOff + this.macSize;
    return paramInt + this.bufOff - this.macSize;
  }

  public BlockCipher getUnderlyingCipher()
  {
    return this.cipher.getUnderlyingCipher();
  }

  public int getUpdateOutputSize(int paramInt)
  {
    return (paramInt + this.bufOff) / this.blockSize * this.blockSize;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.forEncryption = paramBoolean;
    AEADParameters localAEADParameters;
    byte[] arrayOfByte1;
    byte[] arrayOfByte2;
    if ((paramCipherParameters instanceof AEADParameters))
    {
      localAEADParameters = (AEADParameters)paramCipherParameters;
      arrayOfByte1 = localAEADParameters.getNonce();
      arrayOfByte2 = localAEADParameters.getAssociatedText();
      this.macSize = (localAEADParameters.getMacSize() / 8);
    }
    ParametersWithIV localParametersWithIV;
    for (Object localObject = localAEADParameters.getKey(); ; localObject = localParametersWithIV.getParameters())
    {
      byte[] arrayOfByte3 = new byte[this.blockSize];
      this.mac.init((CipherParameters)localObject);
      arrayOfByte3[(-1 + this.blockSize)] = 1;
      this.mac.update(arrayOfByte3, 0, this.blockSize);
      this.mac.update(arrayOfByte2, 0, arrayOfByte2.length);
      this.mac.doFinal(this.associatedTextMac, 0);
      arrayOfByte3[(-1 + this.blockSize)] = 0;
      this.mac.update(arrayOfByte3, 0, this.blockSize);
      this.mac.update(arrayOfByte1, 0, arrayOfByte1.length);
      this.mac.doFinal(this.nonceMac, 0);
      arrayOfByte3[(-1 + this.blockSize)] = 2;
      this.mac.update(arrayOfByte3, 0, this.blockSize);
      this.cipher.init(true, new ParametersWithIV((CipherParameters)localObject, this.nonceMac));
      return;
      if (!(paramCipherParameters instanceof ParametersWithIV))
        break;
      localParametersWithIV = (ParametersWithIV)paramCipherParameters;
      arrayOfByte1 = localParametersWithIV.getIV();
      arrayOfByte2 = new byte[0];
      this.macSize = (this.mac.getMacSize() / 2);
    }
    throw new IllegalArgumentException("invalid parameters passed to EAX");
  }

  public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException
  {
    return process(paramByte, paramArrayOfByte, paramInt);
  }

  public int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws DataLengthException
  {
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j == paramInt2)
        return i;
      i += process(paramArrayOfByte1[(paramInt1 + j)], paramArrayOfByte2, paramInt3 + i);
    }
  }

  public void reset()
  {
    reset(true);
  }
}