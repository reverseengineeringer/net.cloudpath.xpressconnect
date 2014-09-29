package org.bouncycastle2.jce.netscape;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;

public class NetscapeCertRequest extends ASN1Encodable
{
  String challenge;
  DERBitString content;
  AlgorithmIdentifier keyAlg;
  PublicKey pubkey;
  AlgorithmIdentifier sigAlg;
  byte[] sigBits;

  public NetscapeCertRequest(String paramString, AlgorithmIdentifier paramAlgorithmIdentifier, PublicKey paramPublicKey)
    throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException
  {
    this.challenge = paramString;
    this.sigAlg = paramAlgorithmIdentifier;
    this.pubkey = paramPublicKey;
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(getKeySpec());
    localASN1EncodableVector.add(new DERIA5String(paramString));
    this.content = new DERBitString(new DERSequence(localASN1EncodableVector));
  }

  public NetscapeCertRequest(ASN1Sequence paramASN1Sequence)
  {
    try
    {
      if (paramASN1Sequence.size() != 3)
        throw new IllegalArgumentException("invalid SPKAC (size):" + paramASN1Sequence.size());
    }
    catch (Exception localException)
    {
      throw new IllegalArgumentException(localException.toString());
    }
    this.sigAlg = new AlgorithmIdentifier((ASN1Sequence)paramASN1Sequence.getObjectAt(1));
    this.sigBits = ((DERBitString)paramASN1Sequence.getObjectAt(2)).getBytes();
    ASN1Sequence localASN1Sequence = (ASN1Sequence)paramASN1Sequence.getObjectAt(0);
    if (localASN1Sequence.size() != 2)
      throw new IllegalArgumentException("invalid PKAC (len): " + localASN1Sequence.size());
    this.challenge = ((DERIA5String)localASN1Sequence.getObjectAt(1)).getString();
    this.content = new DERBitString(localASN1Sequence);
    SubjectPublicKeyInfo localSubjectPublicKeyInfo = new SubjectPublicKeyInfo((ASN1Sequence)localASN1Sequence.getObjectAt(0));
    X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(new DERBitString(localSubjectPublicKeyInfo).getBytes());
    this.keyAlg = localSubjectPublicKeyInfo.getAlgorithmId();
    this.pubkey = KeyFactory.getInstance(this.keyAlg.getObjectId().getId(), "BC").generatePublic(localX509EncodedKeySpec);
  }

  public NetscapeCertRequest(byte[] paramArrayOfByte)
    throws IOException
  {
    this(getReq(paramArrayOfByte));
  }

  private DERObject getKeySpec()
    throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      localByteArrayOutputStream.write(this.pubkey.getEncoded());
      localByteArrayOutputStream.close();
      DERObject localDERObject = new ASN1InputStream(new ByteArrayInputStream(localByteArrayOutputStream.toByteArray())).readObject();
      return localDERObject;
    }
    catch (IOException localIOException)
    {
      throw new InvalidKeySpecException(localIOException.getMessage());
    }
  }

  private static ASN1Sequence getReq(byte[] paramArrayOfByte)
    throws IOException
  {
    return ASN1Sequence.getInstance(new ASN1InputStream(new ByteArrayInputStream(paramArrayOfByte)).readObject());
  }

  public String getChallenge()
  {
    return this.challenge;
  }

  public AlgorithmIdentifier getKeyAlgorithm()
  {
    return this.keyAlg;
  }

  public PublicKey getPublicKey()
  {
    return this.pubkey;
  }

  public AlgorithmIdentifier getSigningAlgorithm()
  {
    return this.sigAlg;
  }

  public void setChallenge(String paramString)
  {
    this.challenge = paramString;
  }

  public void setKeyAlgorithm(AlgorithmIdentifier paramAlgorithmIdentifier)
  {
    this.keyAlg = paramAlgorithmIdentifier;
  }

  public void setPublicKey(PublicKey paramPublicKey)
  {
    this.pubkey = paramPublicKey;
  }

  public void setSigningAlgorithm(AlgorithmIdentifier paramAlgorithmIdentifier)
  {
    this.sigAlg = paramAlgorithmIdentifier;
  }

  public void sign(PrivateKey paramPrivateKey)
    throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidKeySpecException
  {
    sign(paramPrivateKey, null);
  }

  public void sign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidKeySpecException
  {
    Signature localSignature = Signature.getInstance(this.sigAlg.getObjectId().getId(), "BC");
    if (paramSecureRandom != null)
      localSignature.initSign(paramPrivateKey, paramSecureRandom);
    while (true)
    {
      ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
      localASN1EncodableVector.add(getKeySpec());
      localASN1EncodableVector.add(new DERIA5String(this.challenge));
      try
      {
        localSignature.update(new DERSequence(localASN1EncodableVector).getEncoded("DER"));
        this.sigBits = localSignature.sign();
        return;
        localSignature.initSign(paramPrivateKey);
      }
      catch (IOException localIOException)
      {
        throw new SignatureException(localIOException.getMessage());
      }
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
    try
    {
      localASN1EncodableVector2.add(getKeySpec());
      label24: localASN1EncodableVector2.add(new DERIA5String(this.challenge));
      localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
      localASN1EncodableVector1.add(this.sigAlg);
      localASN1EncodableVector1.add(new DERBitString(this.sigBits));
      return new DERSequence(localASN1EncodableVector1);
    }
    catch (Exception localException)
    {
      break label24;
    }
  }

  public boolean verify(String paramString)
    throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException
  {
    if (!paramString.equals(this.challenge))
      return false;
    Signature localSignature = Signature.getInstance(this.sigAlg.getObjectId().getId(), "BC");
    localSignature.initVerify(this.pubkey);
    localSignature.update(this.content.getBytes());
    return localSignature.verify(this.sigBits);
  }
}