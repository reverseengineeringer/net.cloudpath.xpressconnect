package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Set extends ASN1Object
{
  protected Vector set = new Vector();

  private byte[] getEncoded(DEREncodable paramDEREncodable)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ASN1OutputStream localASN1OutputStream = new ASN1OutputStream(localByteArrayOutputStream);
    try
    {
      localASN1OutputStream.writeObject(paramDEREncodable);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("cannot encode object added to SET");
  }

  public static ASN1Set getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ASN1Set)))
      return (ASN1Set)paramObject;
    throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
  }

  public static ASN1Set getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (!paramASN1TaggedObject.isExplicit())
        throw new IllegalArgumentException("object implicit - explicit expected.");
      return (ASN1Set)paramASN1TaggedObject.getObject();
    }
    if (paramASN1TaggedObject.isExplicit())
      return new DERSet(paramASN1TaggedObject.getObject());
    if ((paramASN1TaggedObject.getObject() instanceof ASN1Set))
      return (ASN1Set)paramASN1TaggedObject.getObject();
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if ((paramASN1TaggedObject.getObject() instanceof ASN1Sequence))
    {
      Enumeration localEnumeration = ((ASN1Sequence)paramASN1TaggedObject.getObject()).getObjects();
      while (true)
      {
        if (!localEnumeration.hasMoreElements())
          return new DERSet(localASN1EncodableVector, false);
        localASN1EncodableVector.add((DEREncodable)localEnumeration.nextElement());
      }
    }
    throw new IllegalArgumentException("unknown object in getInstance: " + paramASN1TaggedObject.getClass().getName());
  }

  private DEREncodable getNext(Enumeration paramEnumeration)
  {
    Object localObject = (DEREncodable)paramEnumeration.nextElement();
    if (localObject == null)
      localObject = DERNull.INSTANCE;
    return localObject;
  }

  private boolean lessThanOrEqual(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = Math.min(paramArrayOfByte1.length, paramArrayOfByte2.length);
    for (int j = 0; ; j++)
    {
      if (j == i)
        if (i != paramArrayOfByte1.length)
          break;
      do
      {
        return true;
        if (paramArrayOfByte1[j] == paramArrayOfByte2[j])
          break;
      }
      while ((0xFF & paramArrayOfByte1[j]) < (0xFF & paramArrayOfByte2[j]));
      return false;
    }
    return false;
  }

  protected void addObject(DEREncodable paramDEREncodable)
  {
    this.set.addElement(paramDEREncodable);
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof ASN1Set));
    ASN1Set localASN1Set;
    do
    {
      return false;
      localASN1Set = (ASN1Set)paramDERObject;
    }
    while (size() != localASN1Set.size());
    Enumeration localEnumeration1 = getObjects();
    Enumeration localEnumeration2 = localASN1Set.getObjects();
    while (true)
    {
      if (!localEnumeration1.hasMoreElements())
        return true;
      DEREncodable localDEREncodable1 = getNext(localEnumeration1);
      DEREncodable localDEREncodable2 = getNext(localEnumeration2);
      DERObject localDERObject1 = localDEREncodable1.getDERObject();
      DERObject localDERObject2 = localDEREncodable2.getDERObject();
      if (localDERObject1 != localDERObject2)
        if (!localDERObject1.equals(localDERObject2))
          break;
    }
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;

  public DEREncodable getObjectAt(int paramInt)
  {
    return (DEREncodable)this.set.elementAt(paramInt);
  }

  public Enumeration getObjects()
  {
    return this.set.elements();
  }

  public int hashCode()
  {
    Enumeration localEnumeration = getObjects();
    DEREncodable localDEREncodable;
    for (int i = size(); ; i = i * 17 ^ localDEREncodable.hashCode())
    {
      if (!localEnumeration.hasMoreElements())
        return i;
      localDEREncodable = getNext(localEnumeration);
    }
  }

  public ASN1SetParser parser()
  {
    return new ASN1SetParser()
    {
      private int index;
      private final int max = ASN1Set.this.size();

      public DERObject getDERObject()
      {
        return jdField_this;
      }

      public DERObject getLoadedObject()
      {
        return jdField_this;
      }

      public DEREncodable readObject()
        throws IOException
      {
        DEREncodable localDEREncodable;
        if (this.index == this.max)
          localDEREncodable = null;
        do
        {
          return localDEREncodable;
          ASN1Set localASN1Set = ASN1Set.this;
          int i = this.index;
          this.index = (i + 1);
          localDEREncodable = localASN1Set.getObjectAt(i);
          if ((localDEREncodable instanceof ASN1Sequence))
            return ((ASN1Sequence)localDEREncodable).parser();
        }
        while (!(localDEREncodable instanceof ASN1Set));
        return ((ASN1Set)localDEREncodable).parser();
      }
    };
  }

  public int size()
  {
    return this.set.size();
  }

  protected void sort()
  {
    int i;
    if (this.set.size() > 1)
      i = 1;
    int k;
    int m;
    Object localObject1;
    for (int j = -1 + this.set.size(); ; j = m)
    {
      if (i == 0)
        return;
      k = 0;
      m = 0;
      localObject1 = getEncoded((DEREncodable)this.set.elementAt(0));
      i = 0;
      if (k != j)
        break;
    }
    byte[] arrayOfByte = getEncoded((DEREncodable)this.set.elementAt(k + 1));
    if (lessThanOrEqual((byte[])localObject1, arrayOfByte))
      localObject1 = arrayOfByte;
    while (true)
    {
      k++;
      break;
      Object localObject2 = this.set.elementAt(k);
      this.set.setElementAt(this.set.elementAt(k + 1), k);
      this.set.setElementAt(localObject2, k + 1);
      i = 1;
      m = k;
    }
  }

  public ASN1Encodable[] toArray()
  {
    ASN1Encodable[] arrayOfASN1Encodable = new ASN1Encodable[size()];
    for (int i = 0; ; i++)
    {
      if (i == size())
        return arrayOfASN1Encodable;
      arrayOfASN1Encodable[i] = ((ASN1Encodable)getObjectAt(i));
    }
  }

  public String toString()
  {
    return this.set.toString();
  }
}