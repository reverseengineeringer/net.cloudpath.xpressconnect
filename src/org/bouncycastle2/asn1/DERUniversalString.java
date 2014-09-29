package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERUniversalString extends ASN1Object
  implements DERString
{
  private static final char[] table = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  private byte[] string;

  public DERUniversalString(byte[] paramArrayOfByte)
  {
    this.string = paramArrayOfByte;
  }

  public static DERUniversalString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERUniversalString)))
      return (DERUniversalString)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERUniversalString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERUniversalString)))
      return getInstance(localDERObject);
    return new DERUniversalString(((ASN1OctetString)localDERObject).getOctets());
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERUniversalString))
      return false;
    return getString().equals(((DERUniversalString)paramDERObject).getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(28, getOctets());
  }

  public byte[] getOctets()
  {
    return this.string;
  }

  public String getString()
  {
    StringBuffer localStringBuffer = new StringBuffer("#");
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ASN1OutputStream localASN1OutputStream = new ASN1OutputStream(localByteArrayOutputStream);
    while (true)
    {
      byte[] arrayOfByte;
      int i;
      try
      {
        localASN1OutputStream.writeObject(this);
        arrayOfByte = localByteArrayOutputStream.toByteArray();
        i = 0;
        if (i == arrayOfByte.length)
          return localStringBuffer.toString();
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("internal error encoding BitString");
      }
      localStringBuffer.append(table[(0xF & arrayOfByte[i] >>> 4)]);
      localStringBuffer.append(table[(0xF & arrayOfByte[i])]);
      i++;
    }
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public String toString()
  {
    return getString();
  }
}