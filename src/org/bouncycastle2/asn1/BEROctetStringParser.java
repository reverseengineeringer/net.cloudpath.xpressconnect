package org.bouncycastle2.asn1;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle2.util.io.Streams;

public class BEROctetStringParser
  implements ASN1OctetStringParser
{
  private ASN1StreamParser _parser;

  BEROctetStringParser(ASN1StreamParser paramASN1StreamParser)
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
      throw new ASN1ParsingException("IOException converting stream to byte array: " + localIOException.getMessage(), localIOException);
    }
  }

  public DERObject getLoadedObject()
    throws IOException
  {
    return new BERConstructedOctetString(Streams.readAll(getOctetStream()));
  }

  public InputStream getOctetStream()
  {
    return new ConstructedOctetStream(this._parser);
  }
}