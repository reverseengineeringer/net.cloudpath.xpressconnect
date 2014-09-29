package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class DERObjectIdentifier extends ASN1Object
{
  String identifier;

  public DERObjectIdentifier(String paramString)
  {
    if (!isValidIdentifier(paramString))
      throw new IllegalArgumentException("string " + paramString + " not an OID");
    this.identifier = paramString;
  }

  DERObjectIdentifier(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    long l = 0L;
    BigInteger localBigInteger = null;
    int i = 1;
    int j = 0;
    if (j == paramArrayOfByte.length)
    {
      this.identifier = localStringBuffer.toString();
      return;
    }
    int k = 0xFF & paramArrayOfByte[j];
    if (l < 36028797018963968L)
    {
      l = 128L * l + (k & 0x7F);
      if ((k & 0x80) == 0)
      {
        if (i != 0);
        switch ((int)l / 40)
        {
        default:
          localStringBuffer.append('2');
          l -= 80L;
          label125: i = 0;
          localStringBuffer.append('.');
          localStringBuffer.append(l);
          l = 0L;
        case 0:
        case 1:
        }
      }
    }
    while (true)
    {
      j++;
      break;
      localStringBuffer.append('0');
      break label125;
      localStringBuffer.append('1');
      l -= 40L;
      break label125;
      if (localBigInteger == null)
        localBigInteger = BigInteger.valueOf(l);
      localBigInteger = localBigInteger.shiftLeft(7).or(BigInteger.valueOf(k & 0x7F));
      if ((k & 0x80) == 0)
      {
        localStringBuffer.append('.');
        localStringBuffer.append(localBigInteger);
        l = 0L;
        localBigInteger = null;
      }
    }
  }

  public static DERObjectIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERObjectIdentifier)))
      return (DERObjectIdentifier)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERObjectIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERObjectIdentifier)))
      return getInstance(localDERObject);
    return new ASN1ObjectIdentifier(ASN1OctetString.getInstance(paramASN1TaggedObject.getObject()).getOctets());
  }

  private static boolean isValidIdentifier(String paramString)
  {
    if ((paramString.length() < 3) || (paramString.charAt(1) != '.'))
    {
      bool = false;
      return bool;
    }
    int i = paramString.charAt(0);
    if ((i < 48) || (i > 50))
      return false;
    boolean bool = false;
    int j = -1 + paramString.length();
    label51: int k;
    if (j >= 2)
    {
      k = paramString.charAt(j);
      if ((48 > k) || (k > 57))
        break label85;
    }
    for (bool = true; ; bool = false)
    {
      j--;
      break label51;
      break;
      label85: if (k != 46)
        break label103;
      if (!bool)
        return false;
    }
    label103: return false;
  }

  private void writeField(OutputStream paramOutputStream, long paramLong)
    throws IOException
  {
    byte[] arrayOfByte = new byte[9];
    int i = 8;
    arrayOfByte[i] = ((byte)(0x7F & (int)paramLong));
    while (true)
    {
      if (paramLong < 128L)
      {
        paramOutputStream.write(arrayOfByte, i, 9 - i);
        return;
      }
      paramLong >>= 7;
      i--;
      arrayOfByte[i] = ((byte)(0x80 | 0x7F & (int)paramLong));
    }
  }

  private void writeField(OutputStream paramOutputStream, BigInteger paramBigInteger)
    throws IOException
  {
    int i = (6 + paramBigInteger.bitLength()) / 7;
    if (i == 0)
    {
      paramOutputStream.write(0);
      return;
    }
    BigInteger localBigInteger = paramBigInteger;
    byte[] arrayOfByte = new byte[i];
    for (int j = i - 1; ; j--)
    {
      if (j < 0)
      {
        int k = i - 1;
        arrayOfByte[k] = ((byte)(0x7F & arrayOfByte[k]));
        paramOutputStream.write(arrayOfByte);
        return;
      }
      arrayOfByte[j] = ((byte)(0x80 | 0x7F & localBigInteger.intValue()));
      localBigInteger = localBigInteger.shiftRight(7);
    }
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERObjectIdentifier))
      return false;
    return this.identifier.equals(((DERObjectIdentifier)paramDERObject).identifier);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    OIDTokenizer localOIDTokenizer = new OIDTokenizer(this.identifier);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    writeField(localByteArrayOutputStream, 40 * Integer.parseInt(localOIDTokenizer.nextToken()) + Integer.parseInt(localOIDTokenizer.nextToken()));
    while (true)
    {
      if (!localOIDTokenizer.hasMoreTokens())
      {
        localDEROutputStream.close();
        paramDEROutputStream.writeEncoded(6, localByteArrayOutputStream.toByteArray());
        return;
      }
      String str = localOIDTokenizer.nextToken();
      if (str.length() < 18)
        writeField(localByteArrayOutputStream, Long.parseLong(str));
      else
        writeField(localByteArrayOutputStream, new BigInteger(str));
    }
  }

  public String getId()
  {
    return this.identifier;
  }

  public int hashCode()
  {
    return this.identifier.hashCode();
  }

  public String toString()
  {
    return getId();
  }
}