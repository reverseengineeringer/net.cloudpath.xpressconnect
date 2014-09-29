package org.bouncycastle2.asn1;

import java.io.IOException;
import java.io.InputStream;

public class BERTaggedObjectParser
  implements ASN1TaggedObjectParser
{
  private boolean _constructed;
  private ASN1StreamParser _parser;
  private int _tagNumber;

  protected BERTaggedObjectParser(int paramInt1, int paramInt2, InputStream paramInputStream)
  {
  }

  BERTaggedObjectParser(boolean paramBoolean, int paramInt, ASN1StreamParser paramASN1StreamParser)
  {
    this._constructed = paramBoolean;
    this._tagNumber = paramInt;
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
      throw new ASN1ParsingException(localIOException.getMessage());
    }
  }

  public DERObject getLoadedObject()
    throws IOException
  {
    return this._parser.readTaggedObject(this._constructed, this._tagNumber);
  }

  public DEREncodable getObjectParser(int paramInt, boolean paramBoolean)
    throws IOException
  {
    if (paramBoolean)
    {
      if (!this._constructed)
        throw new IOException("Explicit tags must be constructed (see X.690 8.14.2)");
      return this._parser.readObject();
    }
    return this._parser.readImplicit(this._constructed, paramInt);
  }

  public int getTagNo()
  {
    return this._tagNumber;
  }

  public boolean isConstructed()
  {
    return this._constructed;
  }
}