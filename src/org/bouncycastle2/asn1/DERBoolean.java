package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERBoolean extends ASN1Object
{
  public static final DERBoolean FALSE = new DERBoolean(false);
  public static final DERBoolean TRUE = new DERBoolean(true);
  byte value;

  public DERBoolean(boolean paramBoolean)
  {
    if (paramBoolean);
    for (byte b = -1; ; b = 0)
    {
      this.value = b;
      return;
    }
  }

  public DERBoolean(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length != 1)
      throw new IllegalArgumentException("byte value should have 1 byte in it");
    this.value = paramArrayOfByte[0];
  }

  public static DERBoolean getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERBoolean)))
      return (DERBoolean)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERBoolean getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERBoolean)))
      return getInstance(localDERObject);
    return new DERBoolean(((ASN1OctetString)localDERObject).getOctets());
  }

  public static DERBoolean getInstance(boolean paramBoolean)
  {
    if (paramBoolean)
      return TRUE;
    return FALSE;
  }

  protected boolean asn1Equals(DERObject paramDERObject)
  {
    if ((paramDERObject == null) || (!(paramDERObject instanceof DERBoolean)));
    while (this.value != ((DERBoolean)paramDERObject).value)
      return false;
    return true;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    arrayOfByte[0] = this.value;
    paramDEROutputStream.writeEncoded(1, arrayOfByte);
  }

  public int hashCode()
  {
    return this.value;
  }

  public boolean isTrue()
  {
    return this.value != 0;
  }

  public String toString()
  {
    if (this.value != 0)
      return "TRUE";
    return "FALSE";
  }
}