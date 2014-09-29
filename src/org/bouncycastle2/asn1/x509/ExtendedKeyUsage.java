package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class ExtendedKeyUsage extends ASN1Encodable
{
  ASN1Sequence seq;
  Hashtable usageTable = new Hashtable();

  public ExtendedKeyUsage(Vector paramVector)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    Enumeration localEnumeration = paramVector.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        this.seq = new DERSequence(localASN1EncodableVector);
        return;
      }
      DERObject localDERObject = (DERObject)localEnumeration.nextElement();
      localASN1EncodableVector.add(localDERObject);
      this.usageTable.put(localDERObject, localDERObject);
    }
  }

  public ExtendedKeyUsage(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      Object localObject = localEnumeration.nextElement();
      if (!(localObject instanceof DERObjectIdentifier))
        throw new IllegalArgumentException("Only DERObjectIdentifiers allowed in ExtendedKeyUsage.");
      this.usageTable.put(localObject, localObject);
    }
  }

  public ExtendedKeyUsage(KeyPurposeId paramKeyPurposeId)
  {
    this.seq = new DERSequence(paramKeyPurposeId);
    this.usageTable.put(paramKeyPurposeId, paramKeyPurposeId);
  }

  public static ExtendedKeyUsage getInstance(Object paramObject)
  {
    if ((paramObject instanceof ExtendedKeyUsage))
      return (ExtendedKeyUsage)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ExtendedKeyUsage((ASN1Sequence)paramObject);
    if ((paramObject instanceof X509Extension))
      return getInstance(X509Extension.convertValueToObject((X509Extension)paramObject));
    throw new IllegalArgumentException("Invalid ExtendedKeyUsage: " + paramObject.getClass().getName());
  }

  public static ExtendedKeyUsage getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public Vector getUsages()
  {
    Vector localVector = new Vector();
    Enumeration localEnumeration = this.usageTable.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return localVector;
      localVector.addElement(localEnumeration.nextElement());
    }
  }

  public boolean hasKeyPurposeId(KeyPurposeId paramKeyPurposeId)
  {
    return this.usageTable.get(paramKeyPurposeId) != null;
  }

  public int size()
  {
    return this.usageTable.size();
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }
}