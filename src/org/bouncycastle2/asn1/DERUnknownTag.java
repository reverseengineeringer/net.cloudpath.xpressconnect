package org.bouncycastle2.asn1;

import java.io.IOException;
import org.bouncycastle2.util.Arrays;

public class DERUnknownTag extends DERObject
{
  private byte[] data;
  private boolean isConstructed;
  private int tag;

  public DERUnknownTag(int paramInt, byte[] paramArrayOfByte)
  {
    this(false, paramInt, paramArrayOfByte);
  }

  public DERUnknownTag(boolean paramBoolean, int paramInt, byte[] paramArrayOfByte)
  {
    this.isConstructed = paramBoolean;
    this.tag = paramInt;
    this.data = paramArrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (this.isConstructed);
    for (int i = 32; ; i = 0)
    {
      paramDEROutputStream.writeEncoded(i, this.tag, this.data);
      return;
    }
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DERUnknownTag));
    DERUnknownTag localDERUnknownTag;
    do
    {
      return false;
      localDERUnknownTag = (DERUnknownTag)paramObject;
    }
    while ((this.isConstructed != localDERUnknownTag.isConstructed) || (this.tag != localDERUnknownTag.tag) || (!Arrays.areEqual(this.data, localDERUnknownTag.data)));
    return true;
  }

  public byte[] getData()
  {
    return this.data;
  }

  public int getTag()
  {
    return this.tag;
  }

  public int hashCode()
  {
    if (this.isConstructed);
    for (int i = -1; ; i = 0)
      return i ^ this.tag ^ Arrays.hashCode(this.data);
  }

  public boolean isConstructed()
  {
    return this.isConstructed;
  }
}