package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERSequenceParser
  implements ASN1SequenceParser
{
  private ASN1StreamParser _parser;

  DERSequenceParser(ASN1StreamParser paramASN1StreamParser)
  {
    this._parser = paramASN1StreamParser;
  }

  public DERObject getDERObject()
  {
    try
    {
      DERObject localDERObject = getLoadedObject();
      return localDERObject;
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException(localIOException.getMessage());
    }
  }

  public DERObject getLoadedObject()
    throws IOException
  {
    return new DERSequence(this._parser.readVector());
  }

  public DEREncodable readObject()
    throws IOException
  {
    return this._parser.readObject();
  }
}