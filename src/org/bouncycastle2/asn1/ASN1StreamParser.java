package org.bouncycastle2.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ASN1StreamParser
{
  private final InputStream _in;
  private final int _limit;

  public ASN1StreamParser(InputStream paramInputStream)
  {
    this(paramInputStream, ASN1InputStream.findLimit(paramInputStream));
  }

  public ASN1StreamParser(InputStream paramInputStream, int paramInt)
  {
    this._in = paramInputStream;
    this._limit = paramInt;
  }

  public ASN1StreamParser(byte[] paramArrayOfByte)
  {
    this(new ByteArrayInputStream(paramArrayOfByte), paramArrayOfByte.length);
  }

  private void set00Check(boolean paramBoolean)
  {
    if ((this._in instanceof IndefiniteLengthInputStream))
      ((IndefiniteLengthInputStream)this._in).setEofOn00(paramBoolean);
  }

  DEREncodable readImplicit(boolean paramBoolean, int paramInt)
    throws IOException
  {
    if ((this._in instanceof IndefiniteLengthInputStream))
    {
      if (!paramBoolean)
        throw new IOException("indefinite length primitive encoding encountered");
      return readIndef(paramInt);
    }
    if (paramBoolean)
      switch (paramInt)
      {
      default:
      case 17:
      case 16:
      case 4:
      }
    while (true)
    {
      throw new RuntimeException("implicit tagging not implemented");
      return new DERSetParser(this);
      return new DERSequenceParser(this);
      return new BEROctetStringParser(this);
      switch (paramInt)
      {
      default:
      case 4:
      case 17:
      case 16:
      }
    }
    return new DEROctetStringParser((DefiniteLengthInputStream)this._in);
    throw new ASN1Exception("sequences must use constructed encoding (see X.690 8.9.1/8.10.1)");
    throw new ASN1Exception("sets must use constructed encoding (see X.690 8.11.1/8.12.1)");
  }

  DEREncodable readIndef(int paramInt)
    throws IOException
  {
    switch (paramInt)
    {
    default:
      throw new ASN1Exception("unknown BER object encountered: 0x" + Integer.toHexString(paramInt));
    case 8:
      return new DERExternalParser(this);
    case 4:
      return new BEROctetStringParser(this);
    case 16:
      return new BERSequenceParser(this);
    case 17:
    }
    return new BERSetParser(this);
  }

  public DEREncodable readObject()
    throws IOException
  {
    int i = this._in.read();
    if (i == -1)
      return null;
    set00Check(false);
    int j = ASN1InputStream.readTagNumber(this._in, i);
    int k = i & 0x20;
    boolean bool = false;
    if (k != 0)
      bool = true;
    int m = ASN1InputStream.readLength(this._in, this._limit);
    if (m < 0)
    {
      if (!bool)
        throw new IOException("indefinite length primitive encoding encountered");
      ASN1StreamParser localASN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this._in, this._limit), this._limit);
      if ((i & 0x40) != 0)
        return new BERApplicationSpecificParser(j, localASN1StreamParser);
      if ((i & 0x80) != 0)
        return new BERTaggedObjectParser(true, j, localASN1StreamParser);
      return localASN1StreamParser.readIndef(j);
    }
    DefiniteLengthInputStream localDefiniteLengthInputStream = new DefiniteLengthInputStream(this._in, m);
    if ((i & 0x40) != 0)
      return new DERApplicationSpecific(bool, j, localDefiniteLengthInputStream.toByteArray());
    if ((i & 0x80) != 0)
      return new BERTaggedObjectParser(bool, j, new ASN1StreamParser(localDefiniteLengthInputStream));
    if (bool)
    {
      switch (j)
      {
      default:
        return new DERUnknownTag(true, j, localDefiniteLengthInputStream.toByteArray());
      case 4:
        return new BEROctetStringParser(new ASN1StreamParser(localDefiniteLengthInputStream));
      case 16:
        return new DERSequenceParser(new ASN1StreamParser(localDefiniteLengthInputStream));
      case 17:
        return new DERSetParser(new ASN1StreamParser(localDefiniteLengthInputStream));
      case 8:
      }
      return new DERExternalParser(new ASN1StreamParser(localDefiniteLengthInputStream));
    }
    switch (j)
    {
    default:
    case 4:
    }
    try
    {
      DERObject localDERObject = ASN1InputStream.createPrimitiveDERObject(j, localDefiniteLengthInputStream.toByteArray());
      return localDERObject;
      return new DEROctetStringParser(localDefiniteLengthInputStream);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new ASN1Exception("corrupted stream detected", localIllegalArgumentException);
    }
  }

  DERObject readTaggedObject(boolean paramBoolean, int paramInt)
    throws IOException
  {
    if (!paramBoolean)
      return new DERTaggedObject(false, paramInt, new DEROctetString(((DefiniteLengthInputStream)this._in).toByteArray()));
    ASN1EncodableVector localASN1EncodableVector = readVector();
    if ((this._in instanceof IndefiniteLengthInputStream))
    {
      if (localASN1EncodableVector.size() == 1)
        return new BERTaggedObject(true, paramInt, localASN1EncodableVector.get(0));
      return new BERTaggedObject(false, paramInt, BERFactory.createSequence(localASN1EncodableVector));
    }
    if (localASN1EncodableVector.size() == 1)
      return new DERTaggedObject(true, paramInt, localASN1EncodableVector.get(0));
    return new DERTaggedObject(false, paramInt, DERFactory.createSequence(localASN1EncodableVector));
  }

  ASN1EncodableVector readVector()
    throws IOException
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    while (true)
    {
      DEREncodable localDEREncodable = readObject();
      if (localDEREncodable == null)
        return localASN1EncodableVector;
      if ((localDEREncodable instanceof InMemoryRepresentable))
        localASN1EncodableVector.add(((InMemoryRepresentable)localDEREncodable).getLoadedObject());
      else
        localASN1EncodableVector.add(localDEREncodable.getDERObject());
    }
  }
}