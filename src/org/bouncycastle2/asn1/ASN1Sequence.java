package org.bouncycastle2.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Sequence extends ASN1Object
{
  private Vector seq = new Vector();

  public static ASN1Sequence getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ASN1Sequence)))
      return (ASN1Sequence)paramObject;
    if ((paramObject instanceof byte[]))
      try
      {
        ASN1Sequence localASN1Sequence = getInstance(ASN1Object.fromByteArray((byte[])paramObject));
        return localASN1Sequence;
      }
      catch (IOException localIOException)
      {
        throw new IllegalArgumentException("failed to construct sequence from byte[]: " + localIOException.getMessage());
      }
    throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
  }

  public static ASN1Sequence getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (!paramASN1TaggedObject.isExplicit())
        throw new IllegalArgumentException("object implicit - explicit expected.");
      return (ASN1Sequence)paramASN1TaggedObject.getObject();
    }
    if (paramASN1TaggedObject.isExplicit())
    {
      if ((paramASN1TaggedObject instanceof BERTaggedObject))
        return new BERSequence(paramASN1TaggedObject.getObject());
      return new DERSequence(paramASN1TaggedObject.getObject());
    }
    if ((paramASN1TaggedObject.getObject() instanceof ASN1Sequence))
      return (ASN1Sequence)paramASN1TaggedObject.getObject();
    throw new IllegalArgumentException("unknown object in getInstance: " + paramASN1TaggedObject.getClass().getName());
  }

  private DEREncodable getNext(Enumeration paramEnumeration)
  {
    Object localObject = (DEREncodable)paramEnumeration.nextElement();
    if (localObject == null)
      localObject = DERNull.INSTANCE;
    return localObject;
  }

  protected void addObject(DEREncodable paramDEREncodable)
  {
    this.seq.addElement(paramDEREncodable);
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof ASN1Sequence));
    ASN1Sequence localASN1Sequence;
    do
    {
      return false;
      localASN1Sequence = (ASN1Sequence)paramDERObject;
    }
    while (size() != localASN1Sequence.size());
    Enumeration localEnumeration1 = getObjects();
    Enumeration localEnumeration2 = localASN1Sequence.getObjects();
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
    return (DEREncodable)this.seq.elementAt(paramInt);
  }

  public Enumeration getObjects()
  {
    return this.seq.elements();
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

  public ASN1SequenceParser parser()
  {
    return new ASN1SequenceParser()
    {
      private int index;
      private final int max = ASN1Sequence.this.size();

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
          ASN1Sequence localASN1Sequence = ASN1Sequence.this;
          int i = this.index;
          this.index = (i + 1);
          localDEREncodable = localASN1Sequence.getObjectAt(i);
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
    return this.seq.size();
  }

  public String toString()
  {
    return this.seq.toString();
  }
}