package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class CertificatePolicies extends ASN1Encodable
{
  static final DERObjectIdentifier anyPolicy = new DERObjectIdentifier("2.5.29.32.0");
  Vector policies = new Vector();

  public CertificatePolicies(String paramString)
  {
    this(new DERObjectIdentifier(paramString));
  }

  public CertificatePolicies(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1Sequence localASN1Sequence = ASN1Sequence.getInstance(localEnumeration.nextElement());
      this.policies.addElement(localASN1Sequence.getObjectAt(0));
    }
  }

  public CertificatePolicies(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.policies.addElement(paramDERObjectIdentifier);
  }

  public static CertificatePolicies getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertificatePolicies))
      return (CertificatePolicies)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertificatePolicies((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static CertificatePolicies getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public void addPolicy(String paramString)
  {
    this.policies.addElement(new DERObjectIdentifier(paramString));
  }

  public String getPolicy(int paramInt)
  {
    if (this.policies.size() > paramInt)
      return ((DERObjectIdentifier)this.policies.elementAt(paramInt)).getId();
    return null;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= this.policies.size())
        return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(new DERSequence((DERObjectIdentifier)this.policies.elementAt(i)));
    }
  }

  public String toString()
  {
    String str = null;
    for (int i = 0; ; i++)
    {
      if (i >= this.policies.size())
        return "CertificatePolicies: " + str;
      if (str != null)
        str = str + ", ";
      str = str + ((DERObjectIdentifier)this.policies.elementAt(i)).getId();
    }
  }
}