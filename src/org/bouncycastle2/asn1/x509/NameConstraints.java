package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class NameConstraints extends ASN1Encodable
{
  private ASN1Sequence excluded;
  private ASN1Sequence permitted;

  public NameConstraints(Vector paramVector1, Vector paramVector2)
  {
    if (paramVector1 != null)
      this.permitted = createSequence(paramVector1);
    if (paramVector2 != null)
      this.excluded = createSequence(paramVector2);
  }

  public NameConstraints(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = ASN1TaggedObject.getInstance(localEnumeration.nextElement());
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        break;
      case 0:
        this.permitted = ASN1Sequence.getInstance(localASN1TaggedObject, false);
        break;
      case 1:
        this.excluded = ASN1Sequence.getInstance(localASN1TaggedObject, false);
      }
    }
  }

  private DERSequence createSequence(Vector paramVector)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    Enumeration localEnumeration = paramVector.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add((GeneralSubtree)localEnumeration.nextElement());
    }
  }

  public ASN1Sequence getExcludedSubtrees()
  {
    return this.excluded;
  }

  public ASN1Sequence getPermittedSubtrees()
  {
    return this.permitted;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.permitted != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.permitted));
    if (this.excluded != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.excluded));
    return new DERSequence(localASN1EncodableVector);
  }
}