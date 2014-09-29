package org.bouncycastle2.asn1.x509;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;

public class X509ExtensionsGenerator
{
  private Vector extOrdering = new Vector();
  private Hashtable extensions = new Hashtable();

  public void addExtension(DERObjectIdentifier paramDERObjectIdentifier, boolean paramBoolean, DEREncodable paramDEREncodable)
  {
    try
    {
      addExtension(paramDERObjectIdentifier, paramBoolean, paramDEREncodable.getDERObject().getEncoded("DER"));
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("error encoding value: " + localIOException);
    }
  }

  public void addExtension(DERObjectIdentifier paramDERObjectIdentifier, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    if (this.extensions.containsKey(paramDERObjectIdentifier))
      throw new IllegalArgumentException("extension " + paramDERObjectIdentifier + " already added");
    this.extOrdering.addElement(paramDERObjectIdentifier);
    this.extensions.put(paramDERObjectIdentifier, new X509Extension(paramBoolean, new DEROctetString(paramArrayOfByte)));
  }

  public X509Extensions generate()
  {
    return new X509Extensions(this.extOrdering, this.extensions);
  }

  public boolean isEmpty()
  {
    return this.extOrdering.isEmpty();
  }

  public void reset()
  {
    this.extensions = new Hashtable();
    this.extOrdering = new Vector();
  }
}