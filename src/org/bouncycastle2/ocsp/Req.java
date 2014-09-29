package org.bouncycastle2.ocsp;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.ocsp.Request;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class Req
  implements java.security.cert.X509Extension
{
  private Request req;

  public Req(Request paramRequest)
  {
    this.req = paramRequest;
  }

  private Set getExtensionOIDs(boolean paramBoolean)
  {
    HashSet localHashSet = new HashSet();
    X509Extensions localX509Extensions = getSingleRequestExtensions();
    Enumeration localEnumeration;
    if (localX509Extensions != null)
      localEnumeration = localX509Extensions.oids();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return localHashSet;
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
      if (paramBoolean == localX509Extensions.getExtension(localDERObjectIdentifier).isCritical())
        localHashSet.add(localDERObjectIdentifier.getId());
    }
  }

  public CertificateID getCertID()
  {
    return new CertificateID(this.req.getReqCert());
  }

  public Set getCriticalExtensionOIDs()
  {
    return getExtensionOIDs(true);
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = getSingleRequestExtensions();
    if (localX509Extensions != null)
    {
      org.bouncycastle2.asn1.x509.X509Extension localX509Extension = localX509Extensions.getExtension(new DERObjectIdentifier(paramString));
      if (localX509Extension != null)
        try
        {
          byte[] arrayOfByte = localX509Extension.getValue().getEncoded("DER");
          return arrayOfByte;
        }
        catch (Exception localException)
        {
          throw new RuntimeException("error encoding " + localException.toString());
        }
    }
    return null;
  }

  public Set getNonCriticalExtensionOIDs()
  {
    return getExtensionOIDs(false);
  }

  public X509Extensions getSingleRequestExtensions()
  {
    return this.req.getSingleRequestExtensions();
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    return (localSet != null) && (!localSet.isEmpty());
  }
}