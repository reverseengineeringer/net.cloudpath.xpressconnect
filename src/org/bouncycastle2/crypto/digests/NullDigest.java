package org.bouncycastle2.crypto.digests;

import java.io.ByteArrayOutputStream;
import org.bouncycastle2.crypto.Digest;

public class NullDigest
  implements Digest
{
  private ByteArrayOutputStream bOut = new ByteArrayOutputStream();

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = this.bOut.toByteArray();
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
    reset();
    return arrayOfByte.length;
  }

  public String getAlgorithmName()
  {
    return "NULL";
  }

  public int getDigestSize()
  {
    return this.bOut.size();
  }

  public void reset()
  {
    this.bOut.reset();
  }

  public void update(byte paramByte)
  {
    this.bOut.write(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.bOut.write(paramArrayOfByte, paramInt1, paramInt2);
  }
}