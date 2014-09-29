package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class SubjectDirectoryAttributes extends ASN1Encodable
{
  private Vector attributes = new Vector();

  public SubjectDirectoryAttributes(Vector paramVector)
  {
    Enumeration localEnumeration = paramVector.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      this.attributes.addElement(localEnumeration.nextElement());
    }
  }

  public SubjectDirectoryAttributes(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1Sequence localASN1Sequence = ASN1Sequence.getInstance(localEnumeration.nextElement());
      this.attributes.addElement(new Attribute(localASN1Sequence));
    }
  }

  public static SubjectDirectoryAttributes getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SubjectDirectoryAttributes)))
      return (SubjectDirectoryAttributes)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SubjectDirectoryAttributes((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public Vector getAttributes()
  {
    return this.attributes;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    Enumeration localEnumeration = this.attributes.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add((Attribute)localEnumeration.nextElement());
    }
  }
}