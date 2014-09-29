package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class BERConstructedOctetString extends DEROctetString
{
  private static final int MAX_LENGTH = 1000;
  private Vector octs;

  public BERConstructedOctetString(Vector paramVector)
  {
    super(toBytes(paramVector));
    this.octs = paramVector;
  }

  public BERConstructedOctetString(DEREncodable paramDEREncodable)
  {
    super(paramDEREncodable.getDERObject());
  }

  public BERConstructedOctetString(DERObject paramDERObject)
  {
    super(paramDERObject);
  }

  public BERConstructedOctetString(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }

  public static BERConstructedOctetString fromSequence(ASN1Sequence paramASN1Sequence)
  {
    Vector localVector = new Vector();
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return new BERConstructedOctetString(localVector);
      localVector.addElement(localEnumeration.nextElement());
    }
  }

  private Vector generateOcts()
  {
    Vector localVector = new Vector();
    int i = 0;
    if (i >= this.string.length)
      return localVector;
    if (i + 1000 > this.string.length);
    for (int j = this.string.length; ; j = i + 1000)
    {
      byte[] arrayOfByte = new byte[j - i];
      System.arraycopy(this.string, i, arrayOfByte, 0, arrayOfByte.length);
      localVector.addElement(new DEROctetString(arrayOfByte));
      i += 1000;
      break;
    }
  }

  private static byte[] toBytes(Vector paramVector)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 0;
    while (true)
    {
      if (i == paramVector.size())
        return localByteArrayOutputStream.toByteArray();
      try
      {
        localByteArrayOutputStream.write(((DEROctetString)paramVector.elementAt(i)).getOctets());
        i++;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new IllegalArgumentException(paramVector.elementAt(i).getClass().getName() + " found in input should only contain DEROctetString");
      }
      catch (IOException localIOException)
      {
        throw new IllegalArgumentException("exception converting octets " + localIOException.toString());
      }
    }
  }

  public void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(36);
      paramDEROutputStream.write(128);
      Enumeration localEnumeration = getObjects();
      while (true)
      {
        if (!localEnumeration.hasMoreElements())
        {
          paramDEROutputStream.write(0);
          paramDEROutputStream.write(0);
          return;
        }
        paramDEROutputStream.writeObject(localEnumeration.nextElement());
      }
    }
    super.encode(paramDEROutputStream);
  }

  public Enumeration getObjects()
  {
    if (this.octs == null)
      return generateOcts().elements();
    return this.octs.elements();
  }

  public byte[] getOctets()
  {
    return this.string;
  }
}