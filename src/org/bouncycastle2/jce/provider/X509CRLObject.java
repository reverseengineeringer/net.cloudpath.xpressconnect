package org.bouncycastle2.jce.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1OutputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.util.ASN1Dump;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.CRLDistPoint;
import org.bouncycastle2.asn1.x509.CRLNumber;
import org.bouncycastle2.asn1.x509.CertificateList;
import org.bouncycastle2.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle2.asn1.x509.TBSCertList;
import org.bouncycastle2.asn1.x509.TBSCertList.CRLEntry;
import org.bouncycastle2.asn1.x509.Time;
import org.bouncycastle2.asn1.x509.X509Extension;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.jce.X509Principal;
import org.bouncycastle2.util.encoders.Hex;
import org.bouncycastle2.x509.extension.X509ExtensionUtil;

public class X509CRLObject extends X509CRL
{
  private CertificateList c;
  private boolean isIndirect;
  private String sigAlgName;
  private byte[] sigAlgParams;

  public X509CRLObject(CertificateList paramCertificateList)
    throws CRLException
  {
    this.c = paramCertificateList;
    try
    {
      this.sigAlgName = X509SignatureUtil.getSignatureName(paramCertificateList.getSignatureAlgorithm());
      if (paramCertificateList.getSignatureAlgorithm().getParameters() != null);
      for (this.sigAlgParams = ((ASN1Encodable)paramCertificateList.getSignatureAlgorithm().getParameters()).getDEREncoded(); ; this.sigAlgParams = null)
      {
        this.isIndirect = isIndirectCRL();
        return;
      }
    }
    catch (Exception localException)
    {
      throw new CRLException("CRL contents invalid: " + localException);
    }
  }

  private Set getExtensionOIDs(boolean paramBoolean)
  {
    if (getVersion() == 2)
    {
      X509Extensions localX509Extensions = this.c.getTBSCertList().getExtensions();
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
    }
    return null;
  }

  private boolean isIndirectCRL()
    throws CRLException
  {
    byte[] arrayOfByte = getExtensionValue(X509Extensions.IssuingDistributionPoint.getId());
    boolean bool1 = false;
    if (arrayOfByte != null);
    try
    {
      boolean bool2 = IssuingDistributionPoint.getInstance(X509ExtensionUtil.fromExtensionValue(arrayOfByte)).isIndirectCRL();
      bool1 = bool2;
      return bool1;
    }
    catch (Exception localException)
    {
      throw new ExtCRLException("Exception reading IssuingDistributionPoint", localException);
    }
  }

  private Set loadCRLEntries()
  {
    HashSet localHashSet = new HashSet();
    Enumeration localEnumeration = this.c.getRevokedCertificateEnumeration();
    X509CRLEntryObject localX509CRLEntryObject;
    for (X500Principal localX500Principal = getIssuerX500Principal(); ; localX500Principal = localX509CRLEntryObject.getCertificateIssuer())
    {
      if (!localEnumeration.hasMoreElements())
        return localHashSet;
      localX509CRLEntryObject = new X509CRLEntryObject((TBSCertList.CRLEntry)localEnumeration.nextElement(), this.isIndirect, localX500Principal);
      localHashSet.add(localX509CRLEntryObject);
    }
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
    X509Extensions localX509Extensions = this.c.getTBSCertList().getExtensions();
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
          throw new IllegalStateException("error parsing " + localException.toString());
        }
    }
    return null;
  }

  public Principal getIssuerDN()
  {
    return new X509Principal(this.c.getIssuer());
  }

  public X500Principal getIssuerX500Principal()
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      new ASN1OutputStream(localByteArrayOutputStream).writeObject(this.c.getIssuer());
      X500Principal localX500Principal = new X500Principal(localByteArrayOutputStream.toByteArray());
      return localX500Principal;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalStateException("can't encode issuer DN");
  }

  public Date getNextUpdate()
  {
    if (this.c.getNextUpdate() != null)
      return this.c.getNextUpdate().getDate();
    return null;
  }

  public Set getNonCriticalExtensionOIDs()
  {
    return getExtensionOIDs(false);
  }

  public X509CRLEntry getRevokedCertificate(BigInteger paramBigInteger)
  {
    Enumeration localEnumeration = this.c.getRevokedCertificateEnumeration();
    Object localObject;
    for (X500Principal localX500Principal = getIssuerX500Principal(); ; localX500Principal = ((X509CRLEntryObject)localObject).getCertificateIssuer())
    {
      if (!localEnumeration.hasMoreElements())
        localObject = null;
      TBSCertList.CRLEntry localCRLEntry;
      do
      {
        return localObject;
        localCRLEntry = (TBSCertList.CRLEntry)localEnumeration.nextElement();
        localObject = new X509CRLEntryObject(localCRLEntry, this.isIndirect, localX500Principal);
      }
      while (paramBigInteger.equals(localCRLEntry.getUserCertificate().getValue()));
    }
  }

  public Set getRevokedCertificates()
  {
    Set localSet = loadCRLEntries();
    if (!localSet.isEmpty())
      return Collections.unmodifiableSet(localSet);
    return null;
  }

  public String getSigAlgName()
  {
    return this.sigAlgName;
  }

  public String getSigAlgOID()
  {
    return this.c.getSignatureAlgorithm().getObjectId().getId();
  }

  public byte[] getSigAlgParams()
  {
    if (this.sigAlgParams != null)
    {
      byte[] arrayOfByte = new byte[this.sigAlgParams.length];
      System.arraycopy(this.sigAlgParams, 0, arrayOfByte, 0, arrayOfByte.length);
      return arrayOfByte;
    }
    return null;
  }

  public byte[] getSignature()
  {
    return this.c.getSignature().getBytes();
  }

  public byte[] getTBSCertList()
    throws CRLException
  {
    try
    {
      byte[] arrayOfByte = this.c.getTBSCertList().getEncoded("DER");
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new CRLException(localIOException.toString());
    }
  }

  public Date getThisUpdate()
  {
    return this.c.getThisUpdate().getDate();
  }

  public int getVersion()
  {
    return this.c.getVersion();
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    if (localSet == null);
    do
    {
      return false;
      localSet.remove(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
      localSet.remove(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
    }
    while (localSet.isEmpty());
    return true;
  }

  public boolean isRevoked(Certificate paramCertificate)
  {
    if (!paramCertificate.getType().equals("X.509"))
      throw new RuntimeException("X.509 CRL used with non X.509 Cert");
    TBSCertList.CRLEntry[] arrayOfCRLEntry = this.c.getRevokedCertificates();
    BigInteger localBigInteger;
    if (arrayOfCRLEntry != null)
      localBigInteger = ((X509Certificate)paramCertificate).getSerialNumber();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfCRLEntry.length)
        return false;
      if (arrayOfCRLEntry[i].getUserCertificate().getValue().equals(localBigInteger))
        return true;
    }
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("              Version: ").append(getVersion()).append(str);
    localStringBuffer.append("             IssuerDN: ").append(getIssuerDN()).append(str);
    localStringBuffer.append("          This update: ").append(getThisUpdate()).append(str);
    localStringBuffer.append("          Next update: ").append(getNextUpdate()).append(str);
    localStringBuffer.append("  Signature Algorithm: ").append(getSigAlgName()).append(str);
    byte[] arrayOfByte = getSignature();
    localStringBuffer.append("            Signature: ").append(new String(Hex.encode(arrayOfByte, 0, 20))).append(str);
    int i = 20;
    X509Extensions localX509Extensions;
    Enumeration localEnumeration;
    label204: Iterator localIterator;
    if (i >= arrayOfByte.length)
    {
      localX509Extensions = this.c.getTBSCertList().getExtensions();
      if (localX509Extensions != null)
      {
        localEnumeration = localX509Extensions.oids();
        if (localEnumeration.hasMoreElements())
          localStringBuffer.append("           Extensions: ").append(str);
        if (localEnumeration.hasMoreElements())
          break label335;
      }
      Set localSet = getRevokedCertificates();
      if (localSet != null)
        localIterator = localSet.iterator();
    }
    while (true)
    {
      label335: DERObjectIdentifier localDERObjectIdentifier;
      ASN1InputStream localASN1InputStream;
      while (true)
      {
        if (localIterator.hasNext())
          break label705;
        return localStringBuffer.toString();
        if (i < -20 + arrayOfByte.length)
          localStringBuffer.append("                       ").append(new String(Hex.encode(arrayOfByte, i, 20))).append(str);
        while (true)
        {
          i += 20;
          break;
          localStringBuffer.append("                       ").append(new String(Hex.encode(arrayOfByte, i, arrayOfByte.length - i))).append(str);
        }
        localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
        X509Extension localX509Extension = localX509Extensions.getExtension(localDERObjectIdentifier);
        if (localX509Extension.getValue() == null)
          break label696;
        localASN1InputStream = new ASN1InputStream(localX509Extension.getValue().getOctets());
        localStringBuffer.append("                       critical(").append(localX509Extension.isCritical()).append(") ");
        try
        {
          if (!localDERObjectIdentifier.equals(X509Extensions.CRLNumber))
            break label477;
          localStringBuffer.append(new CRLNumber(DERInteger.getInstance(localASN1InputStream.readObject()).getPositiveValue())).append(str);
        }
        catch (Exception localException)
        {
          localStringBuffer.append(localDERObjectIdentifier.getId());
          localStringBuffer.append(" value = ").append("*****").append(str);
        }
      }
      break label204;
      label477: if (localDERObjectIdentifier.equals(X509Extensions.DeltaCRLIndicator))
      {
        localStringBuffer.append("Base CRL: " + new CRLNumber(DERInteger.getInstance(localASN1InputStream.readObject()).getPositiveValue())).append(str);
        break label204;
      }
      if (localDERObjectIdentifier.equals(X509Extensions.IssuingDistributionPoint))
      {
        IssuingDistributionPoint localIssuingDistributionPoint = new IssuingDistributionPoint((ASN1Sequence)localASN1InputStream.readObject());
        localStringBuffer.append(localIssuingDistributionPoint).append(str);
        break label204;
      }
      if (localDERObjectIdentifier.equals(X509Extensions.CRLDistributionPoints))
      {
        CRLDistPoint localCRLDistPoint1 = new CRLDistPoint((ASN1Sequence)localASN1InputStream.readObject());
        localStringBuffer.append(localCRLDistPoint1).append(str);
        break label204;
      }
      if (localDERObjectIdentifier.equals(X509Extensions.FreshestCRL))
      {
        CRLDistPoint localCRLDistPoint2 = new CRLDistPoint((ASN1Sequence)localASN1InputStream.readObject());
        localStringBuffer.append(localCRLDistPoint2).append(str);
        break label204;
      }
      localStringBuffer.append(localDERObjectIdentifier.getId());
      localStringBuffer.append(" value = ").append(ASN1Dump.dumpAsString(localASN1InputStream.readObject())).append(str);
      break label204;
      label696: localStringBuffer.append(str);
      break label204;
      label705: localStringBuffer.append(localIterator.next());
      localStringBuffer.append(str);
    }
  }

  public void verify(PublicKey paramPublicKey)
    throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    verify(paramPublicKey, BouncyCastleProvider.PROVIDER_NAME);
  }

  public void verify(PublicKey paramPublicKey, String paramString)
    throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    if (!this.c.getSignatureAlgorithm().equals(this.c.getTBSCertList().getSignature()))
      throw new CRLException("Signature algorithm on CertificateList does not match TBSCertList.");
    Signature localSignature = Signature.getInstance(getSigAlgName(), paramString);
    localSignature.initVerify(paramPublicKey);
    localSignature.update(getTBSCertList());
    if (!localSignature.verify(getSignature()))
      throw new SignatureException("CRL does not verify with supplied public key.");
  }
}