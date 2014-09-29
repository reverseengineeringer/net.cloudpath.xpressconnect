package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERVisibleString extends ASN1Object
  implements DERString
{
  String string;

  public DERVisibleString(String paramString)
  {
    this.string = paramString;
  }

  public DERVisibleString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
      {
        this.string = new String(arrayOfChar);
        return;
      }
      arrayOfChar[i] = ((char)(0xFF & paramArrayOfByte[i]));
    }
  }

  public static DERVisibleString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERVisibleString)))
      return (DERVisibleString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERVisibleString(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERVisibleString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERVisibleString))
      return false;
    return getString().equals(((DERVisibleString)paramDERObject).getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(26, getOctets());
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.string.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)arrayOfChar[i]);
    }
  }

  public String getString()
  {
    return this.string;
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public String toString()
  {
    return this.string;
  }
}