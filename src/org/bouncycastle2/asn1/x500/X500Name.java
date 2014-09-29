package org.bouncycastle2.asn1.x500;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x500.style.BCStyle;
import org.bouncycastle2.asn1.x509.X509Name;

public class X500Name extends ASN1Encodable
  implements ASN1Choice
{
  private static X500NameStyle defaultStyle = BCStyle.INSTANCE;
  private int hashCodeValue;
  private boolean isHashCodeCalculated;
  private RDN[] rdns;
  private X500NameStyle style;

  public X500Name(String paramString)
  {
    this(defaultStyle, paramString);
  }

  private X500Name(ASN1Sequence paramASN1Sequence)
  {
    this(defaultStyle, paramASN1Sequence);
  }

  public X500Name(X500NameStyle paramX500NameStyle, String paramString)
  {
    this(paramX500NameStyle.fromString(paramString));
    this.style = paramX500NameStyle;
  }

  private X500Name(X500NameStyle paramX500NameStyle, ASN1Sequence paramASN1Sequence)
  {
    this.style = paramX500NameStyle;
    this.rdns = new RDN[paramASN1Sequence.size()];
    int i = 0;
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      RDN[] arrayOfRDN = this.rdns;
      int j = i + 1;
      arrayOfRDN[i] = RDN.getInstance(localEnumeration.nextElement());
      i = j;
    }
  }

  public X500Name(X500NameStyle paramX500NameStyle, X500Name paramX500Name)
  {
    this.rdns = paramX500Name.rdns;
    this.style = paramX500NameStyle;
  }

  public X500Name(X500NameStyle paramX500NameStyle, RDN[] paramArrayOfRDN)
  {
    this.rdns = paramArrayOfRDN;
    this.style = paramX500NameStyle;
  }

  public X500Name(RDN[] paramArrayOfRDN)
  {
    this(defaultStyle, paramArrayOfRDN);
  }

  public static X500NameStyle getDefaultStyle()
  {
    return defaultStyle;
  }

  public static X500Name getInstance(Object paramObject)
  {
    if ((paramObject instanceof X500Name))
      return (X500Name)paramObject;
    if ((paramObject instanceof X509Name))
      return new X500Name(ASN1Sequence.getInstance(((X509Name)paramObject).getDERObject()));
    if (paramObject != null)
      return new X500Name(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static X500Name getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, true));
  }

  public static void setDefaultStyle(X500NameStyle paramX500NameStyle)
  {
    if (paramX500NameStyle == null)
      throw new NullPointerException("cannot set style to null");
    defaultStyle = paramX500NameStyle;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((!(paramObject instanceof X500Name)) && (!(paramObject instanceof ASN1Sequence)))
      return false;
    DERObject localDERObject = ((DEREncodable)paramObject).getDERObject();
    if (getDERObject().equals(localDERObject))
      return true;
    try
    {
      boolean bool = this.style.areEqual(this, new X500Name(ASN1Sequence.getInstance(((DEREncodable)paramObject).getDERObject())));
      return bool;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public RDN[] getRDNs()
  {
    RDN[] arrayOfRDN = new RDN[this.rdns.length];
    System.arraycopy(this.rdns, 0, arrayOfRDN, 0, arrayOfRDN.length);
    return arrayOfRDN;
  }

  public RDN[] getRDNs(ASN1ObjectIdentifier paramASN1ObjectIdentifier)
  {
    RDN[] arrayOfRDN1 = new RDN[this.rdns.length];
    int i = 0;
    int j = 0;
    if (j == this.rdns.length)
    {
      RDN[] arrayOfRDN2 = new RDN[i];
      System.arraycopy(arrayOfRDN1, 0, arrayOfRDN2, 0, arrayOfRDN2.length);
      return arrayOfRDN2;
    }
    RDN localRDN = this.rdns[j];
    AttributeTypeAndValue[] arrayOfAttributeTypeAndValue;
    int m;
    if (localRDN.isMultiValued())
    {
      arrayOfAttributeTypeAndValue = localRDN.getTypesAndValues();
      m = 0;
      label71: if (m != arrayOfAttributeTypeAndValue.length);
    }
    while (true)
    {
      j++;
      break;
      if (arrayOfAttributeTypeAndValue[m].getType().equals(paramASN1ObjectIdentifier))
      {
        int n = i + 1;
        arrayOfRDN1[i] = localRDN;
        i = n;
      }
      else
      {
        m++;
        break label71;
        if (localRDN.getFirst().getType().equals(paramASN1ObjectIdentifier))
        {
          int k = i + 1;
          arrayOfRDN1[i] = localRDN;
          i = k;
        }
      }
    }
  }

  public int hashCode()
  {
    if (this.isHashCodeCalculated)
      return this.hashCodeValue;
    this.isHashCodeCalculated = true;
    this.hashCodeValue = this.style.calculateHashCode(this);
    return this.hashCodeValue;
  }

  public DERObject toASN1Object()
  {
    return new DERSequence(this.rdns);
  }

  public String toString()
  {
    return this.style.toString(this);
  }
}