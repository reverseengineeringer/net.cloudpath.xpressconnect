package org.bouncycastle2.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class LazyDERSequence extends DERSequence
{
  private byte[] encoded;
  private boolean parsed = false;
  private int size = -1;

  LazyDERSequence(byte[] paramArrayOfByte)
    throws IOException
  {
    this.encoded = paramArrayOfByte;
  }

  private void parse()
  {
    LazyDERConstructionEnumeration localLazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
    while (true)
    {
      if (!localLazyDERConstructionEnumeration.hasMoreElements())
      {
        this.parsed = true;
        return;
      }
      addObject((DEREncodable)localLazyDERConstructionEnumeration.nextElement());
    }
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(48, this.encoded);
  }

  public DEREncodable getObjectAt(int paramInt)
  {
    try
    {
      if (!this.parsed)
        parse();
      DEREncodable localDEREncodable = super.getObjectAt(paramInt);
      return localDEREncodable;
    }
    finally
    {
    }
  }

  public Enumeration getObjects()
  {
    try
    {
      Enumeration localEnumeration;
      if (this.parsed)
        localEnumeration = super.getObjects();
      for (Object localObject2 = localEnumeration; ; localObject2 = new LazyDERConstructionEnumeration(this.encoded))
        return localObject2;
    }
    finally
    {
    }
  }

  public int size()
  {
    LazyDERConstructionEnumeration localLazyDERConstructionEnumeration;
    if (this.size < 0)
      localLazyDERConstructionEnumeration = new LazyDERConstructionEnumeration(this.encoded);
    for (this.size = 0; ; this.size = (1 + this.size))
    {
      if (!localLazyDERConstructionEnumeration.hasMoreElements())
        return this.size;
      localLazyDERConstructionEnumeration.nextElement();
    }
  }
}