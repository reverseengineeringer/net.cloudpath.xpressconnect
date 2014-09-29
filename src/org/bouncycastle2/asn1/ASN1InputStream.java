package org.bouncycastle2.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle2.util.io.Streams;

public class ASN1InputStream extends FilterInputStream
  implements DERTags
{
  private final boolean lazyEvaluate;
  private final int limit;

  public ASN1InputStream(InputStream paramInputStream)
  {
    this(paramInputStream, findLimit(paramInputStream));
  }

  public ASN1InputStream(InputStream paramInputStream, int paramInt)
  {
    this(paramInputStream, paramInt, false);
  }

  public ASN1InputStream(InputStream paramInputStream, int paramInt, boolean paramBoolean)
  {
    super(paramInputStream);
    this.limit = paramInt;
    this.lazyEvaluate = paramBoolean;
  }

  public ASN1InputStream(byte[] paramArrayOfByte)
  {
    this(new ByteArrayInputStream(paramArrayOfByte), paramArrayOfByte.length);
  }

  public ASN1InputStream(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    this(new ByteArrayInputStream(paramArrayOfByte), paramArrayOfByte.length, paramBoolean);
  }

  static DERObject createPrimitiveDERObject(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 7:
    case 8:
    case 9:
    case 11:
    case 13:
    case 14:
    case 15:
    case 16:
    case 17:
    case 21:
    case 25:
    case 29:
    default:
      return new DERUnknownTag(false, paramInt, paramArrayOfByte);
    case 3:
      return DERBitString.fromOctetString(paramArrayOfByte);
    case 30:
      return new DERBMPString(paramArrayOfByte);
    case 1:
      return new ASN1Boolean(paramArrayOfByte);
    case 10:
      return new ASN1Enumerated(paramArrayOfByte);
    case 24:
      return new ASN1GeneralizedTime(paramArrayOfByte);
    case 27:
      return new DERGeneralString(paramArrayOfByte);
    case 22:
      return new DERIA5String(paramArrayOfByte);
    case 2:
      return new ASN1Integer(paramArrayOfByte);
    case 5:
      return DERNull.INSTANCE;
    case 18:
      return new DERNumericString(paramArrayOfByte);
    case 6:
      return new ASN1ObjectIdentifier(paramArrayOfByte);
    case 4:
      return new DEROctetString(paramArrayOfByte);
    case 19:
      return new DERPrintableString(paramArrayOfByte);
    case 20:
      return new DERT61String(paramArrayOfByte);
    case 28:
      return new DERUniversalString(paramArrayOfByte);
    case 23:
      return new ASN1UTCTime(paramArrayOfByte);
    case 12:
      return new DERUTF8String(paramArrayOfByte);
    case 26:
    }
    return new DERVisibleString(paramArrayOfByte);
  }

  static int findLimit(InputStream paramInputStream)
  {
    if ((paramInputStream instanceof LimitedInputStream))
      return ((LimitedInputStream)paramInputStream).getRemaining();
    if ((paramInputStream instanceof ByteArrayInputStream))
      return ((ByteArrayInputStream)paramInputStream).available();
    return 2147483647;
  }

  static int readLength(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    int i = paramInputStream.read();
    if (i < 0)
      throw new EOFException("EOF found when length expected");
    if (i == 128)
      return -1;
    if (i > 127)
    {
      int j = i & 0x7F;
      if (j > 4)
        throw new IOException("DER length more than 4 bytes: " + j);
      i = 0;
      for (int k = 0; ; k++)
      {
        if (k >= j)
        {
          if (i >= 0)
            break;
          throw new IOException("corrupted stream - negative length found");
        }
        int m = paramInputStream.read();
        if (m < 0)
          throw new EOFException("EOF found reading length");
        i = m + (i << 8);
      }
      if (i >= paramInt)
        throw new IOException("corrupted stream - out of bounds length found");
    }
    return i;
  }

  static int readTagNumber(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    int i = paramInt & 0x1F;
    if (i == 31)
    {
      int j = paramInputStream.read();
      int k = j & 0x7F;
      int m = 0;
      if (k == 0)
        throw new IOException("corrupted stream - invalid high tag number found");
      while ((j >= 0) && ((j & 0x80) != 0))
      {
        m = (m | j & 0x7F) << 7;
        j = paramInputStream.read();
      }
      if (j < 0)
        throw new EOFException("EOF found inside tag value.");
      i = m | j & 0x7F;
    }
    return i;
  }

  ASN1EncodableVector buildDEREncodableVector(DefiniteLengthInputStream paramDefiniteLengthInputStream)
    throws IOException
  {
    return new ASN1InputStream(paramDefiniteLengthInputStream).buildEncodableVector();
  }

  ASN1EncodableVector buildEncodableVector()
    throws IOException
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    while (true)
    {
      DERObject localDERObject = readObject();
      if (localDERObject == null)
        return localASN1EncodableVector;
      localASN1EncodableVector.add(localDERObject);
    }
  }

  protected DERObject buildObject(int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    if ((paramInt1 & 0x20) != 0);
    DefiniteLengthInputStream localDefiniteLengthInputStream;
    for (boolean bool = true; ; bool = false)
    {
      localDefiniteLengthInputStream = new DefiniteLengthInputStream(this, paramInt3);
      if ((paramInt1 & 0x40) == 0)
        break;
      return new DERApplicationSpecific(bool, paramInt2, localDefiniteLengthInputStream.toByteArray());
    }
    if ((paramInt1 & 0x80) != 0)
      return new ASN1StreamParser(localDefiniteLengthInputStream).readTaggedObject(bool, paramInt2);
    if (bool)
    {
      switch (paramInt2)
      {
      default:
        return new DERUnknownTag(true, paramInt2, localDefiniteLengthInputStream.toByteArray());
      case 4:
        return new BERConstructedOctetString(buildDEREncodableVector(localDefiniteLengthInputStream).v);
      case 16:
        if (this.lazyEvaluate)
          return new LazyDERSequence(localDefiniteLengthInputStream.toByteArray());
        return DERFactory.createSequence(buildDEREncodableVector(localDefiniteLengthInputStream));
      case 17:
        return DERFactory.createSet(buildDEREncodableVector(localDefiniteLengthInputStream), false);
      case 8:
      }
      return new DERExternal(buildDEREncodableVector(localDefiniteLengthInputStream));
    }
    return createPrimitiveDERObject(paramInt2, localDefiniteLengthInputStream.toByteArray());
  }

  protected void readFully(byte[] paramArrayOfByte)
    throws IOException
  {
    if (Streams.readFully(this, paramArrayOfByte) != paramArrayOfByte.length)
      throw new EOFException("EOF encountered in middle of object");
  }

  protected int readLength()
    throws IOException
  {
    return readLength(this, this.limit);
  }

  public DERObject readObject()
    throws IOException
  {
    int i = read();
    if (i <= 0)
    {
      if (i == 0)
        throw new IOException("unexpected end-of-contents marker");
      return null;
    }
    int j = readTagNumber(this, i);
    if ((i & 0x20) != 0);
    int m;
    for (int k = 1; ; k = 0)
    {
      m = readLength();
      if (m >= 0)
        break label247;
      if (k != 0)
        break;
      throw new IOException("indefinite length primitive encoding encountered");
    }
    ASN1StreamParser localASN1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this, this.limit), this.limit);
    if ((i & 0x40) != 0)
      return new BERApplicationSpecificParser(j, localASN1StreamParser).getLoadedObject();
    if ((i & 0x80) != 0)
      return new BERTaggedObjectParser(true, j, localASN1StreamParser).getLoadedObject();
    switch (j)
    {
    default:
      throw new IOException("unknown BER object encountered");
    case 4:
      return new BEROctetStringParser(localASN1StreamParser).getLoadedObject();
    case 16:
      return new BERSequenceParser(localASN1StreamParser).getLoadedObject();
    case 17:
      return new BERSetParser(localASN1StreamParser).getLoadedObject();
    case 8:
    }
    return new DERExternalParser(localASN1StreamParser).getLoadedObject();
    try
    {
      label247: DERObject localDERObject = buildObject(i, j, m);
      return localDERObject;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new ASN1Exception("corrupted stream detected", localIllegalArgumentException);
    }
  }
}