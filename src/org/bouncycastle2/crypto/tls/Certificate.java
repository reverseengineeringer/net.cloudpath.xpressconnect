package org.bouncycastle2.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;

public class Certificate
{
  public static final Certificate EMPTY_CHAIN = new Certificate(new X509CertificateStructure[0]);
  protected X509CertificateStructure[] certs;

  public Certificate(X509CertificateStructure[] paramArrayOfX509CertificateStructure)
  {
    if (paramArrayOfX509CertificateStructure == null)
      throw new IllegalArgumentException("'certs' cannot be null");
    this.certs = paramArrayOfX509CertificateStructure;
  }

  protected static Certificate parse(InputStream paramInputStream)
    throws IOException
  {
    int i = TlsUtils.readUint24(paramInputStream);
    if (i == 0)
      return EMPTY_CHAIN;
    Vector localVector = new Vector();
    X509CertificateStructure[] arrayOfX509CertificateStructure;
    if (i <= 0)
      arrayOfX509CertificateStructure = new X509CertificateStructure[localVector.size()];
    for (int k = 0; ; k++)
    {
      if (k >= localVector.size())
      {
        return new Certificate(arrayOfX509CertificateStructure);
        int j = TlsUtils.readUint24(paramInputStream);
        i -= j + 3;
        byte[] arrayOfByte = new byte[j];
        TlsUtils.readFully(arrayOfByte, paramInputStream);
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
        localVector.addElement(X509CertificateStructure.getInstance(new ASN1InputStream(localByteArrayInputStream).readObject()));
        if (localByteArrayInputStream.available() <= 0)
          break;
        throw new IllegalArgumentException("Sorry, there is garbage data left after the certificate");
      }
      arrayOfX509CertificateStructure[k] = ((X509CertificateStructure)localVector.elementAt(k));
    }
  }

  protected void encode(OutputStream paramOutputStream)
    throws IOException
  {
    Vector localVector = new Vector();
    int i = 0;
    int j = 0;
    if (j >= this.certs.length)
    {
      TlsUtils.writeUint24(i + 3, paramOutputStream);
      TlsUtils.writeUint24(i, paramOutputStream);
    }
    for (int k = 0; ; k++)
    {
      if (k >= localVector.size())
      {
        return;
        byte[] arrayOfByte = this.certs[j].getEncoded("DER");
        localVector.addElement(arrayOfByte);
        i += 3 + arrayOfByte.length;
        j++;
        break;
      }
      TlsUtils.writeOpaque24((byte[])localVector.elementAt(k), paramOutputStream);
    }
  }

  public X509CertificateStructure[] getCerts()
  {
    X509CertificateStructure[] arrayOfX509CertificateStructure = new X509CertificateStructure[this.certs.length];
    System.arraycopy(this.certs, 0, arrayOfX509CertificateStructure, 0, this.certs.length);
    return arrayOfX509CertificateStructure;
  }

  public boolean isEmpty()
  {
    return this.certs.length == 0;
  }
}