package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DigestInfo;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.MD2Digest;
import org.bouncycastle2.crypto.digests.MD4Digest;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.NullDigest;
import org.bouncycastle2.crypto.digests.RIPEMD128Digest;
import org.bouncycastle2.crypto.digests.RIPEMD160Digest;
import org.bouncycastle2.crypto.digests.RIPEMD256Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.digests.SHA224Digest;
import org.bouncycastle2.crypto.digests.SHA256Digest;
import org.bouncycastle2.crypto.digests.SHA384Digest;
import org.bouncycastle2.crypto.digests.SHA512Digest;
import org.bouncycastle2.crypto.encodings.PKCS1Encoding;
import org.bouncycastle2.crypto.engines.RSABlindedEngine;
import org.bouncycastle2.crypto.params.RSAKeyParameters;

public class JDKDigestSignature extends SignatureSpi
{
  private AlgorithmIdentifier algId;
  private AsymmetricBlockCipher cipher;
  private Digest digest;

  protected JDKDigestSignature(DERObjectIdentifier paramDERObjectIdentifier, Digest paramDigest, AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.digest = paramDigest;
    this.cipher = paramAsymmetricBlockCipher;
    this.algId = new AlgorithmIdentifier(paramDERObjectIdentifier, DERNull.INSTANCE);
  }

  protected JDKDigestSignature(Digest paramDigest, AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.digest = paramDigest;
    this.cipher = paramAsymmetricBlockCipher;
    this.algId = null;
  }

  private byte[] derEncode(byte[] paramArrayOfByte)
    throws IOException
  {
    if (this.algId == null)
      return paramArrayOfByte;
    return new DigestInfo(this.algId, paramArrayOfByte).getEncoded("DER");
  }

  private String getType(Object paramObject)
  {
    if (paramObject == null)
      return null;
    return paramObject.getClass().getName();
  }

  protected Object engineGetParameter(String paramString)
  {
    return null;
  }

  protected AlgorithmParameters engineGetParameters()
  {
    return null;
  }

  protected void engineInitSign(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if (!(paramPrivateKey instanceof RSAPrivateKey))
      throw new InvalidKeyException("Supplied key (" + getType(paramPrivateKey) + ") is not a RSAPrivateKey instance");
    RSAKeyParameters localRSAKeyParameters = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)paramPrivateKey);
    this.digest.reset();
    this.cipher.init(true, localRSAKeyParameters);
  }

  protected void engineInitVerify(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    if (!(paramPublicKey instanceof RSAPublicKey))
      throw new InvalidKeyException("Supplied key (" + getType(paramPublicKey) + ") is not a RSAPublicKey instance");
    RSAKeyParameters localRSAKeyParameters = RSAUtil.generatePublicKeyParameter((RSAPublicKey)paramPublicKey);
    this.digest.reset();
    this.cipher.init(false, localRSAKeyParameters);
  }

  protected void engineSetParameter(String paramString, Object paramObject)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
  {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }

  protected byte[] engineSign()
    throws SignatureException
  {
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte1, 0);
    try
    {
      byte[] arrayOfByte2 = derEncode(arrayOfByte1);
      byte[] arrayOfByte3 = this.cipher.processBlock(arrayOfByte2, 0, arrayOfByte2.length);
      return arrayOfByte3;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      throw new SignatureException("key too small for signature type");
    }
    catch (Exception localException)
    {
      throw new SignatureException(localException.toString());
    }
  }

  protected void engineUpdate(byte paramByte)
    throws SignatureException
  {
    this.digest.update(paramByte);
  }

  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SignatureException
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  protected boolean engineVerify(byte[] paramArrayOfByte)
    throws SignatureException
  {
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte1, 0);
    byte[] arrayOfByte2;
    byte[] arrayOfByte3;
    boolean bool;
    label69: int i;
    int j;
    do
    {
      while (true)
      {
        int i6;
        try
        {
          arrayOfByte2 = this.cipher.processBlock(paramArrayOfByte, 0, paramArrayOfByte.length);
          arrayOfByte3 = derEncode(arrayOfByte1);
          if (arrayOfByte2.length != arrayOfByte3.length)
            break;
          i6 = 0;
          if (i6 >= arrayOfByte2.length)
          {
            bool = true;
            return bool;
          }
        }
        catch (Exception localException)
        {
          return false;
        }
        int i7 = arrayOfByte2[i6];
        int i8 = arrayOfByte3[i6];
        bool = false;
        if (i7 == i8)
          i6++;
      }
      i = arrayOfByte2.length;
      j = -2 + arrayOfByte3.length;
      bool = false;
    }
    while (i != j);
    int k = -2 + (arrayOfByte2.length - arrayOfByte1.length);
    int m = -2 + (arrayOfByte3.length - arrayOfByte1.length);
    arrayOfByte3[1] = ((byte)(-2 + arrayOfByte3[1]));
    arrayOfByte3[3] = ((byte)(-2 + arrayOfByte3[3]));
    for (int n = 0; ; n++)
    {
      if (n >= arrayOfByte1.length)
      {
        for (int i3 = 0; ; i3++)
        {
          if (i3 >= k)
            break label223;
          int i4 = arrayOfByte2[i3];
          int i5 = arrayOfByte3[i3];
          bool = false;
          if (i4 != i5)
            break;
        }
        label223: break;
      }
      int i1 = arrayOfByte2[(k + n)];
      int i2 = arrayOfByte3[(m + n)];
      bool = false;
      if (i1 != i2)
        break label69;
    }
  }

  public static class MD2WithRSAEncryption extends JDKDigestSignature
  {
    public MD2WithRSAEncryption()
    {
      super(new MD2Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class MD4WithRSAEncryption extends JDKDigestSignature
  {
    public MD4WithRSAEncryption()
    {
      super(new MD4Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class MD5WithRSAEncryption extends JDKDigestSignature
  {
    public MD5WithRSAEncryption()
    {
      super(new MD5Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class RIPEMD128WithRSAEncryption extends JDKDigestSignature
  {
    public RIPEMD128WithRSAEncryption()
    {
      super(new RIPEMD128Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class RIPEMD160WithRSAEncryption extends JDKDigestSignature
  {
    public RIPEMD160WithRSAEncryption()
    {
      super(new RIPEMD160Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class RIPEMD256WithRSAEncryption extends JDKDigestSignature
  {
    public RIPEMD256WithRSAEncryption()
    {
      super(new RIPEMD256Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class SHA1WithRSAEncryption extends JDKDigestSignature
  {
    public SHA1WithRSAEncryption()
    {
      super(new SHA1Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class SHA224WithRSAEncryption extends JDKDigestSignature
  {
    public SHA224WithRSAEncryption()
    {
      super(new SHA224Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class SHA256WithRSAEncryption extends JDKDigestSignature
  {
    public SHA256WithRSAEncryption()
    {
      super(new SHA256Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class SHA384WithRSAEncryption extends JDKDigestSignature
  {
    public SHA384WithRSAEncryption()
    {
      super(new SHA384Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class SHA512WithRSAEncryption extends JDKDigestSignature
  {
    public SHA512WithRSAEncryption()
    {
      super(new SHA512Digest(), new PKCS1Encoding(new RSABlindedEngine()));
    }
  }

  public static class noneRSA extends JDKDigestSignature
  {
    public noneRSA()
    {
      super(new PKCS1Encoding(new RSABlindedEngine()));
    }
  }
}