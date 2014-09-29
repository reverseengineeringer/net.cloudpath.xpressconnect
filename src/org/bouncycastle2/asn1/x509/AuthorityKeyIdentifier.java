package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;

public class AuthorityKeyIdentifier extends ASN1Encodable
{
  GeneralNames certissuer = null;
  DERInteger certserno = null;
  ASN1OctetString keyidentifier = null;

  public AuthorityKeyIdentifier(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = DERTaggedObject.getInstance(localEnumeration.nextElement());
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("illegal tag");
      case 0:
        this.keyidentifier = ASN1OctetString.getInstance(localASN1TaggedObject, false);
        break;
      case 1:
        this.certissuer = GeneralNames.getInstance(localASN1TaggedObject, false);
        break;
      case 2:
        this.certserno = DERInteger.getInstance(localASN1TaggedObject, false);
      }
    }
  }

  public AuthorityKeyIdentifier(GeneralNames paramGeneralNames, BigInteger paramBigInteger)
  {
    this.keyidentifier = null;
    this.certissuer = GeneralNames.getInstance(paramGeneralNames.toASN1Object());
    this.certserno = new DERInteger(paramBigInteger);
  }

  public AuthorityKeyIdentifier(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    SHA1Digest localSHA1Digest = new SHA1Digest();
    byte[] arrayOfByte1 = new byte[localSHA1Digest.getDigestSize()];
    byte[] arrayOfByte2 = paramSubjectPublicKeyInfo.getPublicKeyData().getBytes();
    localSHA1Digest.update(arrayOfByte2, 0, arrayOfByte2.length);
    localSHA1Digest.doFinal(arrayOfByte1, 0);
    this.keyidentifier = new DEROctetString(arrayOfByte1);
  }

  public AuthorityKeyIdentifier(SubjectPublicKeyInfo paramSubjectPublicKeyInfo, GeneralNames paramGeneralNames, BigInteger paramBigInteger)
  {
    SHA1Digest localSHA1Digest = new SHA1Digest();
    byte[] arrayOfByte1 = new byte[localSHA1Digest.getDigestSize()];
    byte[] arrayOfByte2 = paramSubjectPublicKeyInfo.getPublicKeyData().getBytes();
    localSHA1Digest.update(arrayOfByte2, 0, arrayOfByte2.length);
    localSHA1Digest.doFinal(arrayOfByte1, 0);
    this.keyidentifier = new DEROctetString(arrayOfByte1);
    this.certissuer = GeneralNames.getInstance(paramGeneralNames.toASN1Object());
    this.certserno = new DERInteger(paramBigInteger);
  }

  public AuthorityKeyIdentifier(byte[] paramArrayOfByte)
  {
    this.keyidentifier = new DEROctetString(paramArrayOfByte);
    this.certissuer = null;
    this.certserno = null;
  }

  public AuthorityKeyIdentifier(byte[] paramArrayOfByte, GeneralNames paramGeneralNames, BigInteger paramBigInteger)
  {
    this.keyidentifier = new DEROctetString(paramArrayOfByte);
    this.certissuer = GeneralNames.getInstance(paramGeneralNames.toASN1Object());
    this.certserno = new DERInteger(paramBigInteger);
  }

  public static AuthorityKeyIdentifier getInstance(Object paramObject)
  {
    if ((paramObject instanceof AuthorityKeyIdentifier))
      return (AuthorityKeyIdentifier)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AuthorityKeyIdentifier((ASN1Sequence)paramObject);
    if ((paramObject instanceof X509Extension))
      return getInstance(X509Extension.convertValueToObject((X509Extension)paramObject));
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static AuthorityKeyIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public GeneralNames getAuthorityCertIssuer()
  {
    return this.certissuer;
  }

  public BigInteger getAuthorityCertSerialNumber()
  {
    if (this.certserno != null)
      return this.certserno.getValue();
    return null;
  }

  public byte[] getKeyIdentifier()
  {
    if (this.keyidentifier != null)
      return this.keyidentifier.getOctets();
    return null;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.keyidentifier != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.keyidentifier));
    if (this.certissuer != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.certissuer));
    if (this.certserno != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 2, this.certserno));
    return new DERSequence(localASN1EncodableVector);
  }

  public String toString()
  {
    return "AuthorityKeyIdentifier: KeyID(" + this.keyidentifier.getOctets() + ")";
  }
}