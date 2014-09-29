package org.bouncycastle2.jce.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1OutputStream;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;

class PKCS12BagAttributeCarrierImpl
  implements PKCS12BagAttributeCarrier
{
  private Hashtable pkcs12Attributes;
  private Vector pkcs12Ordering;

  public PKCS12BagAttributeCarrierImpl()
  {
    this(new Hashtable(), new Vector());
  }

  PKCS12BagAttributeCarrierImpl(Hashtable paramHashtable, Vector paramVector)
  {
    this.pkcs12Attributes = paramHashtable;
    this.pkcs12Ordering = paramVector;
  }

  Hashtable getAttributes()
  {
    return this.pkcs12Attributes;
  }

  public DEREncodable getBagAttribute(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return (DEREncodable)this.pkcs12Attributes.get(paramDERObjectIdentifier);
  }

  public Enumeration getBagAttributeKeys()
  {
    return this.pkcs12Ordering.elements();
  }

  Vector getOrdering()
  {
    return this.pkcs12Ordering;
  }

  public void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    Object localObject = paramObjectInputStream.readObject();
    if ((localObject instanceof Hashtable))
    {
      this.pkcs12Attributes = ((Hashtable)localObject);
      this.pkcs12Ordering = ((Vector)paramObjectInputStream.readObject());
      return;
    }
    ASN1InputStream localASN1InputStream = new ASN1InputStream((byte[])localObject);
    while (true)
    {
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localASN1InputStream.readObject();
      if (localDERObjectIdentifier == null)
        break;
      setBagAttribute(localDERObjectIdentifier, localASN1InputStream.readObject());
    }
  }

  public void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    if (this.pkcs12Attributes.containsKey(paramDERObjectIdentifier))
    {
      this.pkcs12Attributes.put(paramDERObjectIdentifier, paramDEREncodable);
      return;
    }
    this.pkcs12Attributes.put(paramDERObjectIdentifier, paramDEREncodable);
    this.pkcs12Ordering.addElement(paramDERObjectIdentifier);
  }

  int size()
  {
    return this.pkcs12Ordering.size();
  }

  public void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    if (this.pkcs12Ordering.size() == 0)
    {
      paramObjectOutputStream.writeObject(new Hashtable());
      paramObjectOutputStream.writeObject(new Vector());
      return;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ASN1OutputStream localASN1OutputStream = new ASN1OutputStream(localByteArrayOutputStream);
    Enumeration localEnumeration = getBagAttributeKeys();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
      {
        paramObjectOutputStream.writeObject(localByteArrayOutputStream.toByteArray());
        return;
      }
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
      localASN1OutputStream.writeObject(localDERObjectIdentifier);
      localASN1OutputStream.writeObject(this.pkcs12Attributes.get(localDERObjectIdentifier));
    }
  }
}