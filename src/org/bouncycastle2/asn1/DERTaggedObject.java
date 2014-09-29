package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERTaggedObject extends ASN1TaggedObject
{
  private static final byte[] ZERO_BYTES = new byte[0];

  public DERTaggedObject(int paramInt)
  {
    super(false, paramInt, new DERSequence());
  }

  public DERTaggedObject(int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramInt, paramDEREncodable);
  }

  public DERTaggedObject(boolean paramBoolean, int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramBoolean, paramInt, paramDEREncodable);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (!this.empty)
    {
      byte[] arrayOfByte = this.obj.getDERObject().getEncoded("DER");
      if (this.explicit)
      {
        paramDEROutputStream.writeEncoded(160, this.tagNo, arrayOfByte);
        return;
      }
      if ((0x20 & arrayOfByte[0]) != 0);
      for (int i = 160; ; i = 128)
      {
        paramDEROutputStream.writeTag(i, this.tagNo);
        paramDEROutputStream.write(arrayOfByte, 1, -1 + arrayOfByte.length);
        return;
      }
    }
    paramDEROutputStream.writeEncoded(160, this.tagNo, ZERO_BYTES);
  }
}