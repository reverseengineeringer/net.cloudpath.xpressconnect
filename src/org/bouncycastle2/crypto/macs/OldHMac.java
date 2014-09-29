package org.bouncycastle2.crypto.macs;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.params.KeyParameter;

public class OldHMac
  implements Mac
{
  private static final int BLOCK_LENGTH = 64;
  private static final byte IPAD = 54;
  private static final byte OPAD = 92;
  private Digest digest;
  private int digestSize;
  private byte[] inputPad = new byte[64];
  private byte[] outputPad = new byte[64];

  public OldHMac(Digest paramDigest)
  {
    this.digest = paramDigest;
    this.digestSize = paramDigest.getDigestSize();
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[this.digestSize];
    this.digest.doFinal(arrayOfByte, 0);
    this.digest.update(this.outputPad, 0, this.outputPad.length);
    this.digest.update(arrayOfByte, 0, arrayOfByte.length);
    int i = this.digest.doFinal(paramArrayOfByte, paramInt);
    reset();
    return i;
  }

  public String getAlgorithmName()
  {
    return this.digest.getAlgorithmName() + "/HMAC";
  }

  public int getMacSize()
  {
    return this.digestSize;
  }

  public Digest getUnderlyingDigest()
  {
    return this.digest;
  }

  public void init(CipherParameters paramCipherParameters)
  {
    this.digest.reset();
    byte[] arrayOfByte1 = ((KeyParameter)paramCipherParameters).getKey();
    int m;
    label68: int j;
    if (arrayOfByte1.length > 64)
    {
      this.digest.update(arrayOfByte1, 0, arrayOfByte1.length);
      this.digest.doFinal(this.inputPad, 0);
      m = this.digestSize;
      if (m >= this.inputPad.length)
      {
        this.outputPad = new byte[this.inputPad.length];
        System.arraycopy(this.inputPad, 0, this.outputPad, 0, this.inputPad.length);
        j = 0;
        label100: if (j < this.inputPad.length)
          break label194;
      }
    }
    for (int k = 0; ; k++)
    {
      if (k >= this.outputPad.length)
      {
        this.digest.update(this.inputPad, 0, this.inputPad.length);
        return;
        this.inputPad[m] = 0;
        m++;
        break;
        System.arraycopy(arrayOfByte1, 0, this.inputPad, 0, arrayOfByte1.length);
        for (int i = arrayOfByte1.length; i < this.inputPad.length; i++)
          this.inputPad[i] = 0;
        break label68;
        label194: byte[] arrayOfByte2 = this.inputPad;
        arrayOfByte2[j] = ((byte)(0x36 ^ arrayOfByte2[j]));
        j++;
        break label100;
      }
      byte[] arrayOfByte3 = this.outputPad;
      arrayOfByte3[k] = ((byte)(0x5C ^ arrayOfByte3[k]));
    }
  }

  public void reset()
  {
    this.digest.reset();
    this.digest.update(this.inputPad, 0, this.inputPad.length);
  }

  public void update(byte paramByte)
  {
    this.digest.update(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
  }
}