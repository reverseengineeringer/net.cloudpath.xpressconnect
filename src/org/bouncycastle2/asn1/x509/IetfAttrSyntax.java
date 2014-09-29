package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.DERUTF8String;

public class IetfAttrSyntax extends ASN1Encodable
{
  public static final int VALUE_OCTETS = 1;
  public static final int VALUE_OID = 2;
  public static final int VALUE_UTF8 = 3;
  GeneralNames policyAuthority = null;
  int valueChoice = -1;
  Vector values = new Vector();

  public IetfAttrSyntax(ASN1Sequence paramASN1Sequence)
  {
    int j;
    if ((paramASN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject))
    {
      this.policyAuthority = GeneralNames.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0), false);
      j = 0 + 1;
    }
    while (!(paramASN1Sequence.getObjectAt(j) instanceof ASN1Sequence))
    {
      throw new IllegalArgumentException("Non-IetfAttrSyntax encoding");
      int i = paramASN1Sequence.size();
      j = 0;
      if (i == 2)
      {
        this.policyAuthority = GeneralNames.getInstance(paramASN1Sequence.getObjectAt(0));
        j = 0 + 1;
      }
    }
    Enumeration localEnumeration = ((ASN1Sequence)paramASN1Sequence.getObjectAt(j)).getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      DERObject localDERObject = (DERObject)localEnumeration.nextElement();
      int k;
      if ((localDERObject instanceof DERObjectIdentifier))
        k = 2;
      while (true)
      {
        if (this.valueChoice < 0)
          this.valueChoice = k;
        if (k == this.valueChoice)
          break label225;
        throw new IllegalArgumentException("Mix of value types in IetfAttrSyntax");
        if ((localDERObject instanceof DERUTF8String))
        {
          k = 3;
        }
        else
        {
          if (!(localDERObject instanceof DEROctetString))
            break;
          k = 1;
        }
      }
      throw new IllegalArgumentException("Bad value type encoding IetfAttrSyntax");
      label225: this.values.addElement(localDERObject);
    }
  }

  public GeneralNames getPolicyAuthority()
  {
    return this.policyAuthority;
  }

  public int getValueType()
  {
    return this.valueChoice;
  }

  public Object[] getValues()
  {
    Object localObject;
    int k;
    if (getValueType() == 1)
    {
      localObject = new ASN1OctetString[this.values.size()];
      k = 0;
      if (k != localObject.length);
    }
    while (true)
    {
      return localObject;
      localObject[k] = ((ASN1OctetString)this.values.elementAt(k));
      k++;
      break;
      if (getValueType() == 2)
      {
        localObject = new DERObjectIdentifier[this.values.size()];
        for (int j = 0; j != localObject.length; j++)
          localObject[j] = ((DERObjectIdentifier)this.values.elementAt(j));
      }
      else
      {
        localObject = new DERUTF8String[this.values.size()];
        for (int i = 0; i != localObject.length; i++)
          localObject[i] = ((DERUTF8String)this.values.elementAt(i));
      }
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    if (this.policyAuthority != null)
      localASN1EncodableVector1.add(new DERTaggedObject(0, this.policyAuthority));
    ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
    Enumeration localEnumeration = this.values.elements();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
        return new DERSequence(localASN1EncodableVector1);
      }
      localASN1EncodableVector2.add((ASN1Encodable)localEnumeration.nextElement());
    }
  }
}