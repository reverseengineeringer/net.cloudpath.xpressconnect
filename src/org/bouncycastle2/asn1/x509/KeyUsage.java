package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.DERBitString;

public class KeyUsage extends DERBitString
{
  public static final int cRLSign = 2;
  public static final int dataEncipherment = 16;
  public static final int decipherOnly = 32768;
  public static final int digitalSignature = 128;
  public static final int encipherOnly = 1;
  public static final int keyAgreement = 8;
  public static final int keyCertSign = 4;
  public static final int keyEncipherment = 32;
  public static final int nonRepudiation = 64;

  public KeyUsage(int paramInt)
  {
    super(getBytes(paramInt), getPadBits(paramInt));
  }

  public KeyUsage(DERBitString paramDERBitString)
  {
    super(paramDERBitString.getBytes(), paramDERBitString.getPadBits());
  }

  public static DERBitString getInstance(Object paramObject)
  {
    if ((paramObject instanceof KeyUsage))
      return (KeyUsage)paramObject;
    if ((paramObject instanceof X509Extension))
      return new KeyUsage(DERBitString.getInstance(X509Extension.convertValueToObject((X509Extension)paramObject)));
    return new KeyUsage(DERBitString.getInstance(paramObject));
  }

  public String toString()
  {
    if (this.data.length == 1)
      return "KeyUsage: 0x" + Integer.toHexString(0xFF & this.data[0]);
    return "KeyUsage: 0x" + Integer.toHexString((0xFF & this.data[1]) << 8 | 0xFF & this.data[0]);
  }
}