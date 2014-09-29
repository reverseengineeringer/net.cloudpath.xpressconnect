package org.bouncycastle2.asn1.cms;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSet;

public class AttributeTable
{
  private Hashtable attributes = new Hashtable();

  public AttributeTable(Hashtable paramHashtable)
  {
    this.attributes = copyTable(paramHashtable);
  }

  public AttributeTable(ASN1EncodableVector paramASN1EncodableVector)
  {
    for (int i = 0; ; i++)
    {
      if (i == paramASN1EncodableVector.size())
        return;
      Attribute localAttribute = Attribute.getInstance(paramASN1EncodableVector.get(i));
      addAttribute(localAttribute.getAttrType(), localAttribute);
    }
  }

  public AttributeTable(ASN1Set paramASN1Set)
  {
    for (int i = 0; ; i++)
    {
      if (i == paramASN1Set.size())
        return;
      Attribute localAttribute = Attribute.getInstance(paramASN1Set.getObjectAt(i));
      addAttribute(localAttribute.getAttrType(), localAttribute);
    }
  }

  public AttributeTable(Attributes paramAttributes)
  {
    this(ASN1Set.getInstance(paramAttributes.getDERObject()));
  }

  private void addAttribute(DERObjectIdentifier paramDERObjectIdentifier, Attribute paramAttribute)
  {
    Object localObject = this.attributes.get(paramDERObjectIdentifier);
    if (localObject == null)
    {
      this.attributes.put(paramDERObjectIdentifier, paramAttribute);
      return;
    }
    Vector localVector;
    if ((localObject instanceof Attribute))
    {
      localVector = new Vector();
      localVector.addElement(localObject);
      localVector.addElement(paramAttribute);
    }
    while (true)
    {
      this.attributes.put(paramDERObjectIdentifier, localVector);
      return;
      localVector = (Vector)localObject;
      localVector.addElement(paramAttribute);
    }
  }

  private Hashtable copyTable(Hashtable paramHashtable)
  {
    Hashtable localHashtable = new Hashtable();
    Enumeration localEnumeration = paramHashtable.keys();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return localHashtable;
      Object localObject = localEnumeration.nextElement();
      localHashtable.put(localObject, paramHashtable.get(localObject));
    }
  }

  public AttributeTable add(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    AttributeTable localAttributeTable = new AttributeTable(this.attributes);
    localAttributeTable.addAttribute(paramASN1ObjectIdentifier, new Attribute(paramASN1ObjectIdentifier, new DERSet(paramASN1Encodable)));
    return localAttributeTable;
  }

  public Attribute get(DERObjectIdentifier paramDERObjectIdentifier)
  {
    Object localObject = this.attributes.get(paramDERObjectIdentifier);
    if ((localObject instanceof Vector))
      return (Attribute)((Vector)localObject).elementAt(0);
    return (Attribute)localObject;
  }

  public ASN1EncodableVector getAll(DERObjectIdentifier paramDERObjectIdentifier)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    Object localObject = this.attributes.get(paramDERObjectIdentifier);
    if ((localObject instanceof Vector))
    {
      localEnumeration = ((Vector)localObject).elements();
      if (localEnumeration.hasMoreElements());
    }
    while (localObject == null)
      while (true)
      {
        Enumeration localEnumeration;
        return localASN1EncodableVector;
        localASN1EncodableVector.add((Attribute)localEnumeration.nextElement());
      }
    localASN1EncodableVector.add((Attribute)localObject);
    return localASN1EncodableVector;
  }

  public AttributeTable remove(ASN1ObjectIdentifier paramASN1ObjectIdentifier)
  {
    AttributeTable localAttributeTable = new AttributeTable(this.attributes);
    localAttributeTable.attributes.remove(paramASN1ObjectIdentifier);
    return localAttributeTable;
  }

  public int size()
  {
    int i = 0;
    Enumeration localEnumeration = this.attributes.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return i;
      Object localObject = localEnumeration.nextElement();
      if ((localObject instanceof Vector))
        i += ((Vector)localObject).size();
      else
        i++;
    }
  }

  public ASN1EncodableVector toASN1EncodableVector()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    Enumeration localEnumeration1 = this.attributes.elements();
    while (true)
    {
      if (!localEnumeration1.hasMoreElements())
        return localASN1EncodableVector;
      Object localObject = localEnumeration1.nextElement();
      if ((localObject instanceof Vector))
      {
        Enumeration localEnumeration2 = ((Vector)localObject).elements();
        while (localEnumeration2.hasMoreElements())
          localASN1EncodableVector.add(Attribute.getInstance(localEnumeration2.nextElement()));
      }
      else
      {
        localASN1EncodableVector.add(Attribute.getInstance(localObject));
      }
    }
  }

  public Attributes toAttributes()
  {
    return new Attributes(toASN1EncodableVector());
  }

  public Hashtable toHashtable()
  {
    return copyTable(this.attributes);
  }
}