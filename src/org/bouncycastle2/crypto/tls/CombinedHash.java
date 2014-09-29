package org.bouncycastle2.crypto.tls;

import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;

class CombinedHash
  implements Digest
{
  private MD5Digest md5;
  private SHA1Digest sha1;

  CombinedHash()
  {
    this.md5 = new MD5Digest();
    this.sha1 = new SHA1Digest();
  }

  CombinedHash(CombinedHash paramCombinedHash)
  {
    this.md5 = new MD5Digest(paramCombinedHash.md5);
    this.sha1 = new SHA1Digest(paramCombinedHash.sha1);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    return this.md5.doFinal(paramArrayOfByte, paramInt) + this.sha1.doFinal(paramArrayOfByte, paramInt + 16);
  }

  public String getAlgorithmName()
  {
    return this.md5.getAlgorithmName() + " and " + this.sha1.getAlgorithmName() + " for TLS 1.0";
  }

  public int getDigestSize()
  {
    return 36;
  }

  public void reset()
  {
    this.md5.reset();
    this.sha1.reset();
  }

  public void update(byte paramByte)
  {
    this.md5.update(paramByte);
    this.sha1.update(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.md5.update(paramArrayOfByte, paramInt1, paramInt2);
    this.sha1.update(paramArrayOfByte, paramInt1, paramInt2);
  }
}