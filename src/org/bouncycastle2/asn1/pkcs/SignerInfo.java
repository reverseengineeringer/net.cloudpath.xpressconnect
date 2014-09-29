package org.bouncycastle2.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class SignerInfo extends ASN1Encodable
{
  private ASN1Set authenticatedAttributes;
  private AlgorithmIdentifier digAlgorithm;
  private AlgorithmIdentifier digEncryptionAlgorithm;
  private ASN1OctetString encryptedDigest;
  private IssuerAndSerialNumber issuerAndSerialNumber;
  private ASN1Set unauthenticatedAttributes;
  private DERInteger version;

  public SignerInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.version = ((DERInteger)localEnumeration.nextElement());
    this.issuerAndSerialNumber = IssuerAndSerialNumber.getInstance(localEnumeration.nextElement());
    this.digAlgorithm = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    Object localObject = localEnumeration.nextElement();
    if ((localObject instanceof ASN1TaggedObject))
      this.authenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)localObject, false);
    for (this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(localEnumeration.nextElement()); ; this.digEncryptionAlgorithm = AlgorithmIdentifier.getInstance(localObject))
    {
      this.encryptedDigest = DEROctetString.getInstance(localEnumeration.nextElement());
      if (!localEnumeration.hasMoreElements())
        break;
      this.unauthenticatedAttributes = ASN1Set.getInstance((ASN1TaggedObject)localEnumeration.nextElement(), false);
      return;
      this.authenticatedAttributes = null;
    }
    this.unauthenticatedAttributes = null;
  }

  public SignerInfo(DERInteger paramDERInteger, IssuerAndSerialNumber paramIssuerAndSerialNumber, AlgorithmIdentifier paramAlgorithmIdentifier1, ASN1Set paramASN1Set1, AlgorithmIdentifier paramAlgorithmIdentifier2, ASN1OctetString paramASN1OctetString, ASN1Set paramASN1Set2)
  {
    this.version = paramDERInteger;
    this.issuerAndSerialNumber = paramIssuerAndSerialNumber;
    this.digAlgorithm = paramAlgorithmIdentifier1;
    this.authenticatedAttributes = paramASN1Set1;
    this.digEncryptionAlgorithm = paramAlgorithmIdentifier2;
    this.encryptedDigest = paramASN1OctetString;
    this.unauthenticatedAttributes = paramASN1Set2;
  }

  public static SignerInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof SignerInfo))
      return (SignerInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SignerInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public ASN1Set getAuthenticatedAttributes()
  {
    return this.authenticatedAttributes;
  }

  public AlgorithmIdentifier getDigestAlgorithm()
  {
    return this.digAlgorithm;
  }

  public AlgorithmIdentifier getDigestEncryptionAlgorithm()
  {
    return this.digEncryptionAlgorithm;
  }

  public ASN1OctetString getEncryptedDigest()
  {
    return this.encryptedDigest;
  }

  public IssuerAndSerialNumber getIssuerAndSerialNumber()
  {
    return this.issuerAndSerialNumber;
  }

  public ASN1Set getUnauthenticatedAttributes()
  {
    return this.unauthenticatedAttributes;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.issuerAndSerialNumber);
    localASN1EncodableVector.add(this.digAlgorithm);
    if (this.authenticatedAttributes != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.authenticatedAttributes));
    localASN1EncodableVector.add(this.digEncryptionAlgorithm);
    localASN1EncodableVector.add(this.encryptedDigest);
    if (this.unauthenticatedAttributes != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.unauthenticatedAttributes));
    return new DERSequence(localASN1EncodableVector);
  }
}