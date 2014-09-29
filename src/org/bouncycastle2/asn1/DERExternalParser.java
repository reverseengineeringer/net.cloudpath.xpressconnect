package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERExternalParser
  implements DEREncodable, InMemoryRepresentable
{
  private ASN1StreamParser _parser;

  public DERExternalParser(ASN1StreamParser paramASN1StreamParser)
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
      throw new ASN1ParsingException("unable to get DER object", localIOException);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new ASN1ParsingException("unable to get DER object", localIllegalArgumentException);
    }
  }

  public DERObject getLoadedObject()
    throws IOException
  {
    try
    {
      DERExternal localDERExternal = new DERExternal(this._parser.readVector());
      return localDERExternal;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new ASN1Exception(localIllegalArgumentException.getMessage(), localIllegalArgumentException);
    }
  }

  public DEREncodable readObject()
    throws IOException
  {
    return this._parser.readObject();
  }
}