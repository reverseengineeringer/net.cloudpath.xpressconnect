package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;

public class SubjectKeyIdentifier extends ASN1Encodable
{
  private byte[] keyidentifier;

  public SubjectKeyIdentifier(ASN1OctetString paramASN1OctetString)
  {
    this.keyidentifier = paramASN1OctetString.getOctets();
  }

  public SubjectKeyIdentifier(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    this.keyidentifier = getDigest(paramSubjectPublicKeyInfo);
  }

  public SubjectKeyIdentifier(byte[] paramArrayOfByte)
  {
    this.keyidentifier = paramArrayOfByte;
  }

  public static SubjectKeyIdentifier createSHA1KeyIdentifier(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    return new SubjectKeyIdentifier(paramSubjectPublicKeyInfo);
  }

  public static SubjectKeyIdentifier createTruncatedSHA1KeyIdentifier(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    byte[] arrayOfByte1 = getDigest(paramSubjectPublicKeyInfo);
    byte[] arrayOfByte2 = new byte[8];
    System.arraycopy(arrayOfByte1, -8 + arrayOfByte1.length, arrayOfByte2, 0, arrayOfByte2.length);
    arrayOfByte2[0] = ((byte)(0xF & arrayOfByte2[0]));
    arrayOfByte2[0] = ((byte)(0x40 | arrayOfByte2[0]));
    return new SubjectKeyIdentifier(arrayOfByte2);
  }

  private static byte[] getDigest(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    SHA1Digest localSHA1Digest = new SHA1Digest();
    byte[] arrayOfByte1 = new byte[localSHA1Digest.getDigestSize()];
    byte[] arrayOfByte2 = paramSubjectPublicKeyInfo.getPublicKeyData().getBytes();
    localSHA1Digest.update(arrayOfByte2, 0, arrayOfByte2.length);
    localSHA1Digest.doFinal(arrayOfByte1, 0);
    return arrayOfByte1;
  }

  public static SubjectKeyIdentifier getInstance(Object paramObject)
  {
    if ((paramObject instanceof SubjectKeyIdentifier))
      return (SubjectKeyIdentifier)paramObject;
    if ((paramObject instanceof SubjectPublicKeyInfo))
      return new SubjectKeyIdentifier((SubjectPublicKeyInfo)paramObject);
    if ((paramObject instanceof ASN1OctetString))
      return new SubjectKeyIdentifier((ASN1OctetString)paramObject);
    if ((paramObject instanceof X509Extension))
      return getInstance(X509Extension.convertValueToObject((X509Extension)paramObject));
    throw new IllegalArgumentException("Invalid SubjectKeyIdentifier: " + paramObject.getClass().getName());
  }

  public static SubjectKeyIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1OctetString.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public byte[] getKeyIdentifier()
  {
    return this.keyidentifier;
  }

  public DERObject toASN1Object()
  {
    return new DEROctetString(this.keyidentifier);
  }
}