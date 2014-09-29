package org.bouncycastle2.crypto.macs;

import java.util.Hashtable;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.ExtendedDigest;
import org.bouncycastle2.crypto.Mac;
import org.bouncycastle2.crypto.params.KeyParameter;

public class HMac
  implements Mac
{
  private static final byte IPAD = 54;
  private static final byte OPAD = 92;
  private static Hashtable blockLengths = new Hashtable();
  private int blockLength;
  private Digest digest;
  private int digestSize;
  private byte[] inputPad;
  private byte[] outputPad;

  static
  {
    blockLengths.put("GOST3411", new Integer(32));
    blockLengths.put("MD2", new Integer(16));
    blockLengths.put("MD4", new Integer(64));
    blockLengths.put("MD5", new Integer(64));
    blockLengths.put("RIPEMD128", new Integer(64));
    blockLengths.put("RIPEMD160", new Integer(64));
    blockLengths.put("SHA-1", new Integer(64));
    blockLengths.put("SHA-224", new Integer(64));
    blockLengths.put("SHA-256", new Integer(64));
    blockLengths.put("SHA-384", new Integer(128));
    blockLengths.put("SHA-512", new Integer(128));
    blockLengths.put("Tiger", new Integer(64));
    blockLengths.put("Whirlpool", new Integer(64));
  }

  public HMac(Digest paramDigest)
  {
    this(paramDigest, getByteLength(paramDigest));
  }

  private HMac(Digest paramDigest, int paramInt)
  {
    this.digest = paramDigest;
    this.digestSize = paramDigest.getDigestSize();
    this.blockLength = paramInt;
    this.inputPad = new byte[this.blockLength];
    this.outputPad = new byte[this.blockLength];
  }

  private static int getByteLength(Digest paramDigest)
  {
    if ((paramDigest instanceof ExtendedDigest))
      return ((ExtendedDigest)paramDigest).getByteLength();
    Integer localInteger = (Integer)blockLengths.get(paramDigest.getAlgorithmName());
    if (localInteger == null)
      throw new IllegalArgumentException("unknown digest passed: " + paramDigest.getAlgorithmName());
    return localInteger.intValue();
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
    label70: int j;
    if (arrayOfByte1.length > this.blockLength)
    {
      this.digest.update(arrayOfByte1, 0, arrayOfByte1.length);
      this.digest.doFinal(this.inputPad, 0);
      m = this.digestSize;
      if (m >= this.inputPad.length)
      {
        this.outputPad = new byte[this.inputPad.length];
        System.arraycopy(this.inputPad, 0, this.outputPad, 0, this.inputPad.length);
        j = 0;
        label102: if (j < this.inputPad.length)
          break label196;
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
        break label70;
        label196: byte[] arrayOfByte2 = this.inputPad;
        arrayOfByte2[j] = ((byte)(0x36 ^ arrayOfByte2[j]));
        j++;
        break label102;
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