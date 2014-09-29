package org.bouncycastle2.crypto.signers;

import java.util.Hashtable;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DigestInfo;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.Signer;
import org.bouncycastle2.crypto.encodings.PKCS1Encoding;
import org.bouncycastle2.crypto.engines.RSABlindedEngine;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class RSADigestSigner
  implements Signer
{
  private static final Hashtable oidMap = new Hashtable();
  private final AlgorithmIdentifier algId;
  private final Digest digest;
  private boolean forSigning;
  private final AsymmetricBlockCipher rsaEngine = new PKCS1Encoding(new RSABlindedEngine());

  static
  {
    oidMap.put("RIPEMD128", TeleTrusTObjectIdentifiers.ripemd128);
    oidMap.put("RIPEMD160", TeleTrusTObjectIdentifiers.ripemd160);
    oidMap.put("RIPEMD256", TeleTrusTObjectIdentifiers.ripemd256);
    oidMap.put("SHA-1", X509ObjectIdentifiers.id_SHA1);
    oidMap.put("SHA-224", NISTObjectIdentifiers.id_sha224);
    oidMap.put("SHA-256", NISTObjectIdentifiers.id_sha256);
    oidMap.put("SHA-384", NISTObjectIdentifiers.id_sha384);
    oidMap.put("SHA-512", NISTObjectIdentifiers.id_sha512);
    oidMap.put("MD2", PKCSObjectIdentifiers.md2);
    oidMap.put("MD4", PKCSObjectIdentifiers.md4);
    oidMap.put("MD5", PKCSObjectIdentifiers.md5);
  }

  public RSADigestSigner(Digest paramDigest)
  {
    this.digest = paramDigest;
    this.algId = new AlgorithmIdentifier((DERObjectIdentifier)oidMap.get(paramDigest.getAlgorithmName()), DERNull.INSTANCE);
  }

  private byte[] derEncode(byte[] paramArrayOfByte)
  {
    return new DigestInfo(this.algId, paramArrayOfByte).getDEREncoded();
  }

  public byte[] generateSignature()
    throws CryptoException, DataLengthException
  {
    if (!this.forSigning)
      throw new IllegalStateException("RSADigestSigner not initialised for signature generation.");
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte1, 0);
    byte[] arrayOfByte2 = derEncode(arrayOfByte1);
    return this.rsaEngine.processBlock(arrayOfByte2, 0, arrayOfByte2.length);
  }

  public String getAlgorithmName()
  {
    return this.digest.getAlgorithmName() + "withRSA";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.forSigning = paramBoolean;
    if ((paramCipherParameters instanceof ParametersWithRandom));
    for (AsymmetricKeyParameter localAsymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)paramCipherParameters).getParameters(); (paramBoolean) && (!localAsymmetricKeyParameter.isPrivate()); localAsymmetricKeyParameter = (AsymmetricKeyParameter)paramCipherParameters)
      throw new IllegalArgumentException("signing requires private key");
    if ((!paramBoolean) && (localAsymmetricKeyParameter.isPrivate()))
      throw new IllegalArgumentException("verification requires public key");
    reset();
    this.rsaEngine.init(paramBoolean, paramCipherParameters);
  }

  public void reset()
  {
    this.digest.reset();
  }

  public void update(byte paramByte)
  {
    this.digest.update(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public boolean verifySignature(byte[] paramArrayOfByte)
  {
    if (this.forSigning)
      throw new IllegalStateException("RSADigestSigner not initialised for verification");
    byte[] arrayOfByte1 = new byte[this.digest.getDigestSize()];
    this.digest.doFinal(arrayOfByte1, 0);
    byte[] arrayOfByte2;
    byte[] arrayOfByte3;
    boolean bool;
    label86: int i;
    int j;
    do
    {
      while (true)
      {
        int i6;
        try
        {
          arrayOfByte2 = this.rsaEngine.processBlock(paramArrayOfByte, 0, paramArrayOfByte.length);
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
            break label240;
          int i4 = arrayOfByte2[i3];
          int i5 = arrayOfByte3[i3];
          bool = false;
          if (i4 != i5)
            break;
        }
        label240: break;
      }
      int i1 = arrayOfByte2[(k + n)];
      int i2 = arrayOfByte3[(m + n)];
      bool = false;
      if (i1 != i2)
        break label86;
    }
  }
}