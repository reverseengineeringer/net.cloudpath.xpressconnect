package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DERSequenceGenerator extends DERGenerator
{
  private final ByteArrayOutputStream _bOut = new ByteArrayOutputStream();

  public DERSequenceGenerator(OutputStream paramOutputStream)
    throws IOException
  {
    super(paramOutputStream);
  }

  public DERSequenceGenerator(OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
    throws IOException
  {
    super(paramOutputStream, paramInt, paramBoolean);
  }

  public void addObject(DEREncodable paramDEREncodable)
    throws IOException
  {
    paramDEREncodable.getDERObject().encode(new DEROutputStream(this._bOut));
  }

  public void close()
    throws IOException
  {
    writeDEREncoded(48, this._bOut.toByteArray());
  }

  public OutputStream getRawOutputStream()
  {
    return this._bOut;
  }
}