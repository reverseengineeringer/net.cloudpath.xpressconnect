package org.bouncycastle2.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.encoders.Hex;

public abstract class ASN1OctetString extends ASN1Object
  implements ASN1OctetStringParser
{
  byte[] string;

  public ASN1OctetString(DEREncodable paramDEREncodable)
  {
    try
    {
      this.string = paramDEREncodable.getDERObject().getEncoded("DER");
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("Error processing object : " + localIOException.toString());
    }
  }

  public ASN1OctetString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      throw new NullPointerException("string cannot be null");
    this.string = paramArrayOfByte;
  }

  public static ASN1OctetString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ASN1OctetString)))
      return (ASN1OctetString)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static ASN1OctetString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof ASN1OctetString)))
      return getInstance(localDERObject);
    return BERConstructedOctetString.fromSequence(ASN1Sequence.getInstance(localDERObject));
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof ASN1OctetString))
      return false;
    ASN1OctetString localASN1OctetString = (ASN1OctetString)paramDERObject;
    return Arrays.areEqual(this.string, localASN1OctetString.string);
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;

  public DERObject getLoadedObject()
  {
    return getDERObject();
  }

  public InputStream getOctetStream()
  {
    return new ByteArrayInputStream(this.string);
  }

  public byte[] getOctets()
  {
    return this.string;
  }

  public int hashCode()
  {
    return Arrays.hashCode(getOctets());
  }

  public ASN1OctetStringParser parser()
  {
    return this;
  }

  public String toString()
  {
    return "#" + new String(Hex.encode(this.string));
  }
}