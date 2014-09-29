package org.bouncycastle2.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class ASN1OutputStream extends DEROutputStream
{
  public ASN1OutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  public void writeObject(Object paramObject)
    throws IOException
  {
    if (paramObject == null)
    {
      writeNull();
      return;
    }
    if ((paramObject instanceof DERObject))
    {
      ((DERObject)paramObject).encode(this);
      return;
    }
    if ((paramObject instanceof DEREncodable))
    {
      ((DEREncodable)paramObject).getDERObject().encode(this);
      return;
    }
    throw new IOException("object not ASN1Encodable");
  }
}