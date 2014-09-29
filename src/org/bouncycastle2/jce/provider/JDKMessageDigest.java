package org.bouncycastle2.jce.provider;

import java.security.MessageDigest;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.GOST3411Digest;
import org.bouncycastle2.crypto.digests.MD2Digest;
import org.bouncycastle2.crypto.digests.MD4Digest;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.RIPEMD128Digest;
import org.bouncycastle2.crypto.digests.RIPEMD160Digest;
import org.bouncycastle2.crypto.digests.RIPEMD256Digest;
import org.bouncycastle2.crypto.digests.RIPEMD320Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.digests.SHA224Digest;
import org.bouncycastle2.crypto.digests.SHA256Digest;
import org.bouncycastle2.crypto.digests.SHA384Digest;
import org.bouncycastle2.crypto.digests.SHA512Digest;
import org.bouncycastle2.crypto.digests.TigerDigest;
import org.bouncycastle2.crypto.digests.WhirlpoolDigest;

public class JDKMessageDigest extends MessageDigest
{
  Digest digest;

  protected JDKMessageDigest(Digest paramDigest)
  {
    super(paramDigest.getAlgorithmName());
    this.digest = paramDigest;
  }

  public byte[] engineDigest()
  {
    byte[] arrayOfByte = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte, 0);
    return arrayOfByte;
  }

  public void engineReset()
  {
    this.digest.reset();
  }

  public void engineUpdate(byte paramByte)
  {
    this.digest.update(paramByte);
  }

  public void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public static class GOST3411 extends JDKMessageDigest
    implements Cloneable
  {
    public GOST3411()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      GOST3411 localGOST3411 = (GOST3411)super.clone();
      localGOST3411.digest = new GOST3411Digest((GOST3411Digest)this.digest);
      return localGOST3411;
    }
  }

  public static class MD2 extends JDKMessageDigest
    implements Cloneable
  {
    public MD2()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      MD2 localMD2 = (MD2)super.clone();
      localMD2.digest = new MD2Digest((MD2Digest)this.digest);
      return localMD2;
    }
  }

  public static class MD4 extends JDKMessageDigest
    implements Cloneable
  {
    public MD4()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      MD4 localMD4 = (MD4)super.clone();
      localMD4.digest = new MD4Digest((MD4Digest)this.digest);
      return localMD4;
    }
  }

  public static class MD5 extends JDKMessageDigest
    implements Cloneable
  {
    public MD5()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      MD5 localMD5 = (MD5)super.clone();
      localMD5.digest = new MD5Digest((MD5Digest)this.digest);
      return localMD5;
    }
  }

  public static class RIPEMD128 extends JDKMessageDigest
    implements Cloneable
  {
    public RIPEMD128()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      RIPEMD128 localRIPEMD128 = (RIPEMD128)super.clone();
      localRIPEMD128.digest = new RIPEMD128Digest((RIPEMD128Digest)this.digest);
      return localRIPEMD128;
    }
  }

  public static class RIPEMD160 extends JDKMessageDigest
    implements Cloneable
  {
    public RIPEMD160()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      RIPEMD160 localRIPEMD160 = (RIPEMD160)super.clone();
      localRIPEMD160.digest = new RIPEMD160Digest((RIPEMD160Digest)this.digest);
      return localRIPEMD160;
    }
  }

  public static class RIPEMD256 extends JDKMessageDigest
    implements Cloneable
  {
    public RIPEMD256()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      RIPEMD256 localRIPEMD256 = (RIPEMD256)super.clone();
      localRIPEMD256.digest = new RIPEMD256Digest((RIPEMD256Digest)this.digest);
      return localRIPEMD256;
    }
  }

  public static class RIPEMD320 extends JDKMessageDigest
    implements Cloneable
  {
    public RIPEMD320()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      RIPEMD320 localRIPEMD320 = (RIPEMD320)super.clone();
      localRIPEMD320.digest = new RIPEMD320Digest((RIPEMD320Digest)this.digest);
      return localRIPEMD320;
    }
  }

  public static class SHA1 extends JDKMessageDigest
    implements Cloneable
  {
    public SHA1()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      SHA1 localSHA1 = (SHA1)super.clone();
      localSHA1.digest = new SHA1Digest((SHA1Digest)this.digest);
      return localSHA1;
    }
  }

  public static class SHA224 extends JDKMessageDigest
    implements Cloneable
  {
    public SHA224()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      SHA224 localSHA224 = (SHA224)super.clone();
      localSHA224.digest = new SHA224Digest((SHA224Digest)this.digest);
      return localSHA224;
    }
  }

  public static class SHA256 extends JDKMessageDigest
    implements Cloneable
  {
    public SHA256()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      SHA256 localSHA256 = (SHA256)super.clone();
      localSHA256.digest = new SHA256Digest((SHA256Digest)this.digest);
      return localSHA256;
    }
  }

  public static class SHA384 extends JDKMessageDigest
    implements Cloneable
  {
    public SHA384()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      SHA384 localSHA384 = (SHA384)super.clone();
      localSHA384.digest = new SHA384Digest((SHA384Digest)this.digest);
      return localSHA384;
    }
  }

  public static class SHA512 extends JDKMessageDigest
    implements Cloneable
  {
    public SHA512()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      SHA512 localSHA512 = (SHA512)super.clone();
      localSHA512.digest = new SHA512Digest((SHA512Digest)this.digest);
      return localSHA512;
    }
  }

  public static class Tiger extends JDKMessageDigest
    implements Cloneable
  {
    public Tiger()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      Tiger localTiger = (Tiger)super.clone();
      localTiger.digest = new TigerDigest((TigerDigest)this.digest);
      return localTiger;
    }
  }

  public static class Whirlpool extends JDKMessageDigest
    implements Cloneable
  {
    public Whirlpool()
    {
      super();
    }

    public Object clone()
      throws CloneNotSupportedException
    {
      Whirlpool localWhirlpool = (Whirlpool)super.clone();
      localWhirlpool.digest = new WhirlpoolDigest((WhirlpoolDigest)this.digest);
      return localWhirlpool;
    }
  }
}