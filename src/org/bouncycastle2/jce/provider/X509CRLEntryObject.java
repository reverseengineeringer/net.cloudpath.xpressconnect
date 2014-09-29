package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CRLException;
import java.security.cert.X509CRLEntry;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DEREnumerated;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.util.ASN1Dump;
import org.bouncycastle2.asn1.x509.CRLReason;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.GeneralNames;
import org.bouncycastle2.asn1.x509.TBSCertList.CRLEntry;
import org.bouncycastle2.asn1.x509.Time;
import org.bouncycastle2.asn1.x509.X509Extension;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.x509.extension.X509ExtensionUtil;

public class X509CRLEntryObject extends X509CRLEntry
{
  private TBSCertList.CRLEntry c;
  private X500Principal certificateIssuer;
  private int hashValue;
  private boolean isHashValueSet;
  private boolean isIndirect;
  private X500Principal previousCertificateIssuer;

  public X509CRLEntryObject(TBSCertList.CRLEntry paramCRLEntry)
  {
    this.c = paramCRLEntry;
    this.certificateIssuer = loadCertificateIssuer();
  }

  public X509CRLEntryObject(TBSCertList.CRLEntry paramCRLEntry, boolean paramBoolean, X500Principal paramX500Principal)
  {
    this.c = paramCRLEntry;
    this.isIndirect = paramBoolean;
    this.previousCertificateIssuer = paramX500Principal;
    this.certificateIssuer = loadCertificateIssuer();
  }

  private Set getExtensionOIDs(boolean paramBoolean)
  {
    X509Extensions localX509Extensions = this.c.getExtensions();
    if (localX509Extensions != null)
    {
      HashSet localHashSet = new HashSet();
      Enumeration localEnumeration = localX509Extensions.oids();
      while (true)
      {
        if (!localEnumeration.hasMoreElements())
          return localHashSet;
        DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
        if (paramBoolean == localX509Extensions.getExtension(localDERObjectIdentifier).isCritical())
          localHashSet.add(localDERObjectIdentifier.getId());
      }
    }
    return null;
  }

  private X500Principal loadCertificateIssuer()
  {
    if (!this.isIndirect);
    while (true)
    {
      return null;
      byte[] arrayOfByte = getExtensionValue(X509Extensions.CertificateIssuer.getId());
      if (arrayOfByte == null)
        return this.previousCertificateIssuer;
      try
      {
        GeneralName[] arrayOfGeneralName = GeneralNames.getInstance(X509ExtensionUtil.fromExtensionValue(arrayOfByte)).getNames();
        for (int i = 0; i < arrayOfGeneralName.length; i++)
          if (arrayOfGeneralName[i].getTagNo() == 4)
          {
            X500Principal localX500Principal = new X500Principal(arrayOfGeneralName[i].getName().getDERObject().getDEREncoded());
            return localX500Principal;
          }
      }
      catch (IOException localIOException)
      {
      }
    }
    return null;
  }

  public X500Principal getCertificateIssuer()
  {
    return this.certificateIssuer;
  }

  public Set getCriticalExtensionOIDs()
  {
    return getExtensionOIDs(true);
  }

  public byte[] getEncoded()
    throws CRLException
  {
    try
    {
      byte[] arrayOfByte = this.c.getEncoded("DER");
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new CRLException(localIOException.toString());
    }
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = this.c.getExtensions();
    if (localX509Extensions != null)
    {
      X509Extension localX509Extension = localX509Extensions.getExtension(new DERObjectIdentifier(paramString));
      if (localX509Extension != null)
        try
        {
          byte[] arrayOfByte = localX509Extension.getValue().getEncoded();
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

  public Date getRevocationDate()
  {
    return this.c.getRevocationDate().getDate();
  }

  public BigInteger getSerialNumber()
  {
    return this.c.getUserCertificate().getValue();
  }

  public boolean hasExtensions()
  {
    return this.c.getExtensions() != null;
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    return (localSet != null) && (!localSet.isEmpty());
  }

  public int hashCode()
  {
    if (!this.isHashValueSet)
    {
      this.hashValue = super.hashCode();
      this.isHashValueSet = true;
    }
    return this.hashValue;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("      userCertificate: ").append(getSerialNumber()).append(str);
    localStringBuffer.append("       revocationDate: ").append(getRevocationDate()).append(str);
    localStringBuffer.append("       certificateIssuer: ").append(getCertificateIssuer()).append(str);
    X509Extensions localX509Extensions = this.c.getExtensions();
    Enumeration localEnumeration;
    if (localX509Extensions != null)
    {
      localEnumeration = localX509Extensions.oids();
      if (localEnumeration.hasMoreElements())
        localStringBuffer.append("   crlEntryExtensions:").append(str);
    }
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return localStringBuffer.toString();
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
      X509Extension localX509Extension = localX509Extensions.getExtension(localDERObjectIdentifier);
      if (localX509Extension.getValue() != null)
      {
        ASN1InputStream localASN1InputStream = new ASN1InputStream(localX509Extension.getValue().getOctets());
        localStringBuffer.append("                       critical(").append(localX509Extension.isCritical()).append(") ");
        try
        {
          if (!localDERObjectIdentifier.equals(X509Extensions.ReasonCode))
            break label263;
          localStringBuffer.append(new CRLReason(DEREnumerated.getInstance(localASN1InputStream.readObject()))).append(str);
        }
        catch (Exception localException)
        {
          localStringBuffer.append(localDERObjectIdentifier.getId());
          localStringBuffer.append(" value = ").append("*****").append(str);
        }
        continue;
        label263: if (localDERObjectIdentifier.equals(X509Extensions.CertificateIssuer))
        {
          localStringBuffer.append("Certificate issuer: ").append(new GeneralNames((ASN1Sequence)localASN1InputStream.readObject())).append(str);
        }
        else
        {
          localStringBuffer.append(localDERObjectIdentifier.getId());
          localStringBuffer.append(" value = ").append(ASN1Dump.dumpAsString(localASN1InputStream.readObject())).append(str);
        }
      }
      else
      {
        localStringBuffer.append(str);
      }
    }
  }
}