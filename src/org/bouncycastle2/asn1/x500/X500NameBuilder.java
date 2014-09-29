package org.bouncycastle2.asn1.x500;

import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;

public class X500NameBuilder
{
  private Vector rdns = new Vector();
  private X500NameStyle template;

  public X500NameBuilder(X500NameStyle paramX500NameStyle)
  {
    this.template = paramX500NameStyle;
  }

  public X500NameBuilder addMultiValuedRDN(ASN1ObjectIdentifier[] paramArrayOfASN1ObjectIdentifier, String[] paramArrayOfString)
  {
    ASN1Encodable[] arrayOfASN1Encodable = new ASN1Encodable[paramArrayOfString.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfASN1Encodable.length)
        return addMultiValuedRDN(paramArrayOfASN1ObjectIdentifier, arrayOfASN1Encodable);
      arrayOfASN1Encodable[i] = this.template.stringToValue(paramArrayOfASN1ObjectIdentifier[i], paramArrayOfString[i]);
    }
  }

  public X500NameBuilder addMultiValuedRDN(ASN1ObjectIdentifier[] paramArrayOfASN1ObjectIdentifier, ASN1Encodable[] paramArrayOfASN1Encodable)
  {
    AttributeTypeAndValue[] arrayOfAttributeTypeAndValue = new AttributeTypeAndValue[paramArrayOfASN1ObjectIdentifier.length];
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfASN1ObjectIdentifier.length)
        return addMultiValuedRDN(arrayOfAttributeTypeAndValue);
      arrayOfAttributeTypeAndValue[i] = new AttributeTypeAndValue(paramArrayOfASN1ObjectIdentifier[i], paramArrayOfASN1Encodable[i]);
    }
  }

  public X500NameBuilder addMultiValuedRDN(AttributeTypeAndValue[] paramArrayOfAttributeTypeAndValue)
  {
    this.rdns.addElement(new RDN(paramArrayOfAttributeTypeAndValue));
    return this;
  }

  public X500NameBuilder addRDN(ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString)
  {
    addRDN(paramASN1ObjectIdentifier, this.template.stringToValue(paramASN1ObjectIdentifier, paramString));
    return this;
  }

  public X500NameBuilder addRDN(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    this.rdns.addElement(new RDN(paramASN1ObjectIdentifier, paramASN1Encodable));
    return this;
  }

  public X500NameBuilder addRDN(AttributeTypeAndValue paramAttributeTypeAndValue)
  {
    this.rdns.addElement(new RDN(paramAttributeTypeAndValue));
    return this;
  }

  public X500Name build()
  {
    RDN[] arrayOfRDN = new RDN[this.rdns.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfRDN.length)
        return new X500Name(this.template, arrayOfRDN);
      arrayOfRDN[i] = ((RDN)this.rdns.elementAt(i));
    }
  }
}