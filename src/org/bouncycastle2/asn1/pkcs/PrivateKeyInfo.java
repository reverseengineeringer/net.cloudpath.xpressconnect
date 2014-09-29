package org.bouncycastle2.asn1.pkcs;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
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

public class PrivateKeyInfo extends ASN1Encodable
{
  private AlgorithmIdentifier algId;
  private ASN1Set attributes;
  private DERObject privKey;

  public PrivateKeyInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    if (((DERInteger)localEnumeration.nextElement()).getValue().intValue() != 0)
      throw new IllegalArgumentException("wrong version for private key info");
    this.algId = new AlgorithmIdentifier((ASN1Sequence)localEnumeration.nextElement());
    try
    {
      this.privKey = new ASN1InputStream(((ASN1OctetString)localEnumeration.nextElement()).getOctets()).readObject();
      if (localEnumeration.hasMoreElements())
        this.attributes = ASN1Set.getInstance((ASN1TaggedObject)localEnumeration.nextElement(), false);
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("Error recoverying private key from sequence");
  }

  public PrivateKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, DERObject paramDERObject)
  {
    this(paramAlgorithmIdentifier, paramDERObject, null);
  }

  public PrivateKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, DERObject paramDERObject, ASN1Set paramASN1Set)
  {
    this.privKey = paramDERObject;
    this.algId = paramAlgorithmIdentifier;
    this.attributes = paramASN1Set;
  }

  public static PrivateKeyInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof PrivateKeyInfo))
      return (PrivateKeyInfo)paramObject;
    if (paramObject != null)
      return new PrivateKeyInfo(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static PrivateKeyInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getAlgorithmId()
  {
    return this.algId;
  }

  public ASN1Set getAttributes()
  {
    return this.attributes;
  }

  public DERObject getPrivateKey()
  {
    return this.privKey;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new DERInteger(0));
    localASN1EncodableVector.add(this.algId);
    localASN1EncodableVector.add(new DEROctetString(this.privKey));
    if (this.attributes != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.attributes));
    return new DERSequence(localASN1EncodableVector);
  }
}