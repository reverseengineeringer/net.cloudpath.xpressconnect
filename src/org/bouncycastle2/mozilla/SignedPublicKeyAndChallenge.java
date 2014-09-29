package org.bouncycastle2.mozilla;

import java.io.ByteArrayInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.mozilla.PublicKeyAndChallenge;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;

public class SignedPublicKeyAndChallenge extends ASN1Encodable
{
  private PublicKeyAndChallenge pkac;
  private DERBitString signature;
  private AlgorithmIdentifier signatureAlgorithm;
  private ASN1Sequence spkacSeq;

  public SignedPublicKeyAndChallenge(byte[] paramArrayOfByte)
  {
    this.spkacSeq = toDERSequence(paramArrayOfByte);
    this.pkac = PublicKeyAndChallenge.getInstance(this.spkacSeq.getObjectAt(0));
    this.signatureAlgorithm = AlgorithmIdentifier.getInstance(this.spkacSeq.getObjectAt(1));
    this.signature = ((DERBitString)this.spkacSeq.getObjectAt(2));
  }

  private static ASN1Sequence toDERSequence(byte[] paramArrayOfByte)
  {
    try
    {
      ASN1Sequence localASN1Sequence = (ASN1Sequence)new ASN1InputStream(new ByteArrayInputStream(paramArrayOfByte)).readObject();
      return localASN1Sequence;
    }
    catch (Exception localException)
    {
    }
    throw new IllegalArgumentException("badly encoded request");
  }

  public PublicKey getPublicKey(String paramString)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException
  {
    SubjectPublicKeyInfo localSubjectPublicKeyInfo = this.pkac.getSubjectPublicKeyInfo();
    try
    {
      X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(new DERBitString(localSubjectPublicKeyInfo).getBytes());
      PublicKey localPublicKey = KeyFactory.getInstance(localSubjectPublicKeyInfo.getAlgorithmId().getObjectId().getId(), paramString).generatePublic(localX509EncodedKeySpec);
      return localPublicKey;
    }
    catch (InvalidKeySpecException localInvalidKeySpecException)
    {
    }
    throw new InvalidKeyException("error encoding public key");
  }

  public PublicKeyAndChallenge getPublicKeyAndChallenge()
  {
    return this.pkac;
  }

  public DERObject toASN1Object()
  {
    return this.spkacSeq;
  }

  public boolean verify()
    throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException
  {
    return verify(null);
  }

  public boolean verify(String paramString)
    throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException
  {
    if (paramString == null);
    for (Signature localSignature = Signature.getInstance(this.signatureAlgorithm.getObjectId().getId()); ; localSignature = Signature.getInstance(this.signatureAlgorithm.getObjectId().getId(), paramString))
    {
      localSignature.initVerify(getPublicKey(paramString));
      localSignature.update(new DERBitString(this.pkac).getBytes());
      return localSignature.verify(this.signature.getBytes());
    }
  }
}