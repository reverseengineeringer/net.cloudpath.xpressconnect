package org.bouncycastle2.asn1;

import java.io.IOException;

public class BERApplicationSpecificParser
  implements ASN1ApplicationSpecificParser
{
  private final ASN1StreamParser parser;
  private final int tag;

  BERApplicationSpecificParser(int paramInt, ASN1StreamParser paramASN1StreamParser)
  {
    this.tag = paramInt;
    this.parser = paramASN1StreamParser;
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
      throw new ASN1ParsingException(localIOException.getMessage(), localIOException);
    }
  }

  public DERObject getLoadedObject()
    throws IOException
  {
    return new BERApplicationSpecific(this.tag, this.parser.readVector());
  }

  public DEREncodable readObject()
    throws IOException
  {
    return this.parser.readObject();
  }
}