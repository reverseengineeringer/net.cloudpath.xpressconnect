package org.bouncycastle2.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERTaggedObject extends DERTaggedObject
{
  public BERTaggedObject(int paramInt)
  {
    super(false, paramInt, new BERSequence());
  }

  public BERTaggedObject(int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramInt, paramDEREncodable);
  }

  public BERTaggedObject(boolean paramBoolean, int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramBoolean, paramInt, paramDEREncodable);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.writeTag(160, this.tagNo);
      paramDEROutputStream.write(128);
      Enumeration localEnumeration;
      if (!this.empty)
      {
        if (this.explicit)
          break label215;
        if (!(this.obj instanceof ASN1OctetString))
          break label121;
        if (!(this.obj instanceof BERConstructedOctetString))
          break label97;
        localEnumeration = ((BERConstructedOctetString)this.obj).getObjects();
        if (localEnumeration.hasMoreElements())
          break label202;
      }
      while (true)
      {
        paramDEROutputStream.write(0);
        paramDEROutputStream.write(0);
        return;
        label97: localEnumeration = new BERConstructedOctetString(((ASN1OctetString)this.obj).getOctets()).getObjects();
        break;
        label121: if ((this.obj instanceof ASN1Sequence))
        {
          localEnumeration = ((ASN1Sequence)this.obj).getObjects();
          break;
        }
        if ((this.obj instanceof ASN1Set))
        {
          localEnumeration = ((ASN1Set)this.obj).getObjects();
          break;
        }
        throw new RuntimeException("not implemented: " + this.obj.getClass().getName());
        label202: paramDEROutputStream.writeObject(localEnumeration.nextElement());
        break;
        label215: paramDEROutputStream.writeObject(this.obj);
      }
    }
    super.encode(paramDEROutputStream);
  }
}