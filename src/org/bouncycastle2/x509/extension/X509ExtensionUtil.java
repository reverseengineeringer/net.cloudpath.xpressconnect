package org.bouncycastle2.x509.extension;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.asn1.x509.X509Name;

public class X509ExtensionUtil
{
  public static ASN1Object fromExtensionValue(byte[] paramArrayOfByte)
    throws IOException
  {
    return ASN1Object.fromByteArray(((ASN1OctetString)ASN1Object.fromByteArray(paramArrayOfByte)).getOctets());
  }

  private static Collection getAlternativeNames(byte[] paramArrayOfByte)
    throws CertificateParsingException
  {
    if (paramArrayOfByte == null)
      return Collections.EMPTY_LIST;
    ArrayList localArrayList1;
    GeneralName localGeneralName;
    ArrayList localArrayList2;
    try
    {
      localArrayList1 = new ArrayList();
      Enumeration localEnumeration = DERSequence.getInstance(fromExtensionValue(paramArrayOfByte)).getObjects();
      if (!localEnumeration.hasMoreElements())
        return Collections.unmodifiableCollection(localArrayList1);
      localGeneralName = GeneralName.getInstance(localEnumeration.nextElement());
      localArrayList2 = new ArrayList();
      localArrayList2.add(new Integer(localGeneralName.getTagNo()));
      switch (localGeneralName.getTagNo())
      {
      default:
        throw new IOException("Bad tag number: " + localGeneralName.getTagNo());
      case 0:
      case 3:
      case 5:
      case 4:
      case 1:
      case 2:
      case 6:
      case 8:
      case 7:
      }
    }
    catch (Exception localException)
    {
      throw new CertificateParsingException(localException.getMessage());
    }
    localArrayList2.add(localGeneralName.getName().getDERObject());
    while (true)
    {
      localArrayList1.add(localArrayList2);
      break;
      localArrayList2.add(X509Name.getInstance(localGeneralName.getName()).toString());
      continue;
      localArrayList2.add(((ASN1String)localGeneralName.getName()).getString());
      continue;
      localArrayList2.add(DERObjectIdentifier.getInstance(localGeneralName.getName()).getId());
      continue;
      localArrayList2.add(DEROctetString.getInstance(localGeneralName.getName()).getOctets());
    }
  }

  public static Collection getIssuerAlternativeNames(X509Certificate paramX509Certificate)
    throws CertificateParsingException
  {
    return getAlternativeNames(paramX509Certificate.getExtensionValue(X509Extensions.IssuerAlternativeName.getId()));
  }

  public static Collection getSubjectAlternativeNames(X509Certificate paramX509Certificate)
    throws CertificateParsingException
  {
    return getAlternativeNames(paramX509Certificate.getExtensionValue(X509Extensions.SubjectAlternativeName.getId()));
  }
}