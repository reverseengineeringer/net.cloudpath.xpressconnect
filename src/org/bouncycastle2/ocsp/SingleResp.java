package org.bouncycastle2.ocsp;

import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.ocsp.CertStatus;
import org.bouncycastle2.asn1.ocsp.RevokedInfo;
import org.bouncycastle2.asn1.ocsp.SingleResponse;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class SingleResp
  implements java.security.cert.X509Extension
{
  SingleResponse resp;

  public SingleResp(SingleResponse paramSingleResponse)
  {
    this.resp = paramSingleResponse;
  }

  private Set getExtensionOIDs(boolean paramBoolean)
  {
    HashSet localHashSet = new HashSet();
    X509Extensions localX509Extensions = getSingleExtensions();
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
    return new CertificateID(this.resp.getCertID());
  }

  public Object getCertStatus()
  {
    CertStatus localCertStatus = this.resp.getCertStatus();
    if (localCertStatus.getTagNo() == 0)
      return null;
    if (localCertStatus.getTagNo() == 1)
      return new RevokedStatus(RevokedInfo.getInstance(localCertStatus.getStatus()));
    return new UnknownStatus();
  }

  public Set getCriticalExtensionOIDs()
  {
    return getExtensionOIDs(true);
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = getSingleExtensions();
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

  public Date getNextUpdate()
  {
    if (this.resp.getNextUpdate() == null)
      return null;
    try
    {
      Date localDate = this.resp.getNextUpdate().getDate();
      return localDate;
    }
    catch (ParseException localParseException)
    {
      throw new IllegalStateException("ParseException: " + localParseException.getMessage());
    }
  }

  public Set getNonCriticalExtensionOIDs()
  {
    return getExtensionOIDs(false);
  }

  public X509Extensions getSingleExtensions()
  {
    return this.resp.getSingleExtensions();
  }

  public Date getThisUpdate()
  {
    try
    {
      Date localDate = this.resp.getThisUpdate().getDate();
      return localDate;
    }
    catch (ParseException localParseException)
    {
      throw new IllegalStateException("ParseException: " + localParseException.getMessage());
    }
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    return (localSet != null) && (!localSet.isEmpty());
  }
}