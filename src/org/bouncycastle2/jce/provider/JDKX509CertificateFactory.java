package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactorySpi;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.SignedData;
import org.bouncycastle2.asn1.x509.CertificateList;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;

public class JDKX509CertificateFactory extends CertificateFactorySpi
{
  private static final PEMUtil PEM_CERT_PARSER = new PEMUtil("CERTIFICATE");
  private static final PEMUtil PEM_CRL_PARSER = new PEMUtil("CRL");
  private InputStream currentCrlStream = null;
  private InputStream currentStream = null;
  private ASN1Set sCrlData = null;
  private int sCrlDataObjectCount = 0;
  private ASN1Set sData = null;
  private int sDataObjectCount = 0;

  private CRL getCRL()
    throws CRLException
  {
    if ((this.sCrlData == null) || (this.sCrlDataObjectCount >= this.sCrlData.size()))
      return null;
    ASN1Set localASN1Set = this.sCrlData;
    int i = this.sCrlDataObjectCount;
    this.sCrlDataObjectCount = (i + 1);
    return createCRL(CertificateList.getInstance(localASN1Set.getObjectAt(i)));
  }

  private Certificate getCertificate()
    throws CertificateParsingException
  {
    if (this.sData != null);
    DEREncodable localDEREncodable;
    do
    {
      if (this.sDataObjectCount >= this.sData.size())
        return null;
      ASN1Set localASN1Set = this.sData;
      int i = this.sDataObjectCount;
      this.sDataObjectCount = (i + 1);
      localDEREncodable = localASN1Set.getObjectAt(i);
    }
    while (!(localDEREncodable instanceof ASN1Sequence));
    return new X509CertificateObject(X509CertificateStructure.getInstance(localDEREncodable));
  }

  private CRL readDERCRL(ASN1InputStream paramASN1InputStream)
    throws IOException, CRLException
  {
    ASN1Sequence localASN1Sequence = (ASN1Sequence)paramASN1InputStream.readObject();
    if ((localASN1Sequence.size() > 1) && ((localASN1Sequence.getObjectAt(0) instanceof DERObjectIdentifier)) && (localASN1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)))
    {
      this.sCrlData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)localASN1Sequence.getObjectAt(1), true)).getCRLs();
      return getCRL();
    }
    return createCRL(CertificateList.getInstance(localASN1Sequence));
  }

  private Certificate readDERCertificate(ASN1InputStream paramASN1InputStream)
    throws IOException, CertificateParsingException
  {
    ASN1Sequence localASN1Sequence = (ASN1Sequence)paramASN1InputStream.readObject();
    if ((localASN1Sequence.size() > 1) && ((localASN1Sequence.getObjectAt(0) instanceof DERObjectIdentifier)) && (localASN1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)))
    {
      this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)localASN1Sequence.getObjectAt(1), true)).getCertificates();
      return getCertificate();
    }
    return new X509CertificateObject(X509CertificateStructure.getInstance(localASN1Sequence));
  }

  private CRL readPEMCRL(InputStream paramInputStream)
    throws IOException, CRLException
  {
    ASN1Sequence localASN1Sequence = PEM_CRL_PARSER.readPEMObject(paramInputStream);
    if (localASN1Sequence != null)
      return createCRL(CertificateList.getInstance(localASN1Sequence));
    return null;
  }

  private Certificate readPEMCertificate(InputStream paramInputStream)
    throws IOException, CertificateParsingException
  {
    ASN1Sequence localASN1Sequence = PEM_CERT_PARSER.readPEMObject(paramInputStream);
    if (localASN1Sequence != null)
      return new X509CertificateObject(X509CertificateStructure.getInstance(localASN1Sequence));
    return null;
  }

  protected CRL createCRL(CertificateList paramCertificateList)
    throws CRLException
  {
    return new X509CRLObject(paramCertificateList);
  }

  public CRL engineGenerateCRL(InputStream paramInputStream)
    throws CRLException
  {
    if (this.currentCrlStream == null)
    {
      this.currentCrlStream = paramInputStream;
      this.sCrlData = null;
      this.sCrlDataObjectCount = 0;
    }
    try
    {
      while (true)
      {
        if (this.sCrlData == null)
          break label97;
        if (this.sCrlDataObjectCount == this.sCrlData.size())
          break;
        CRL localCRL3 = getCRL();
        localCRL1 = localCRL3;
        return localCRL1;
        if (this.currentCrlStream != paramInputStream)
        {
          this.currentCrlStream = paramInputStream;
          this.sCrlData = null;
          this.sCrlDataObjectCount = 0;
        }
      }
      this.sCrlData = null;
      this.sCrlDataObjectCount = 0;
      return null;
    }
    catch (CRLException localCRLException)
    {
      int i;
      PushbackInputStream localPushbackInputStream;
      int j;
      do
      {
        throw localCRLException;
        i = ProviderUtil.getReadLimit(paramInputStream);
        localPushbackInputStream = new PushbackInputStream(paramInputStream);
        j = localPushbackInputStream.read();
        CRL localCRL1 = null;
      }
      while (j == -1);
      localPushbackInputStream.unread(j);
      if (j != 48)
        return readPEMCRL(localPushbackInputStream);
      CRL localCRL2 = readDERCRL(new ASN1InputStream(localPushbackInputStream, i, true));
      return localCRL2;
    }
    catch (Exception localException)
    {
      label97: throw new CRLException(localException.toString());
    }
  }

  public Collection engineGenerateCRLs(InputStream paramInputStream)
    throws CRLException
  {
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      CRL localCRL = engineGenerateCRL(paramInputStream);
      if (localCRL == null)
        return localArrayList;
      localArrayList.add(localCRL);
    }
  }

  public CertPath engineGenerateCertPath(InputStream paramInputStream)
    throws CertificateException
  {
    return engineGenerateCertPath(paramInputStream, "PkiPath");
  }

  public CertPath engineGenerateCertPath(InputStream paramInputStream, String paramString)
    throws CertificateException
  {
    return new PKIXCertPath(paramInputStream, paramString);
  }

  public CertPath engineGenerateCertPath(List paramList)
    throws CertificateException
  {
    Iterator localIterator = paramList.iterator();
    Object localObject;
    do
    {
      if (!localIterator.hasNext())
        return new PKIXCertPath(paramList);
      localObject = localIterator.next();
    }
    while ((localObject == null) || ((localObject instanceof X509Certificate)));
    throw new CertificateException("list contains non X509Certificate object while creating CertPath\n" + localObject.toString());
  }

  public Certificate engineGenerateCertificate(InputStream paramInputStream)
    throws CertificateException
  {
    if (this.currentStream == null)
    {
      this.currentStream = paramInputStream;
      this.sData = null;
      this.sDataObjectCount = 0;
    }
    int i;
    PushbackInputStream localPushbackInputStream;
    int j;
    do
    {
      try
      {
        while (this.sData != null)
          if (this.sDataObjectCount != this.sData.size())
          {
            Certificate localCertificate3 = getCertificate();
            localCertificate1 = localCertificate3;
            return localCertificate1;
            if (this.currentStream != paramInputStream)
            {
              this.currentStream = paramInputStream;
              this.sData = null;
              this.sDataObjectCount = 0;
            }
          }
          else
          {
            this.sData = null;
            this.sDataObjectCount = 0;
            return null;
          }
      }
      catch (Exception localException)
      {
        throw new CertificateException(localException);
      }
      i = ProviderUtil.getReadLimit(paramInputStream);
      localPushbackInputStream = new PushbackInputStream(paramInputStream);
      j = localPushbackInputStream.read();
      Certificate localCertificate1 = null;
    }
    while (j == -1);
    localPushbackInputStream.unread(j);
    if (j != 48)
      return readPEMCertificate(localPushbackInputStream);
    Certificate localCertificate2 = readDERCertificate(new ASN1InputStream(localPushbackInputStream, i));
    return localCertificate2;
  }

  public Collection engineGenerateCertificates(InputStream paramInputStream)
    throws CertificateException
  {
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      Certificate localCertificate = engineGenerateCertificate(paramInputStream);
      if (localCertificate == null)
        return localArrayList;
      localArrayList.add(localCertificate);
    }
  }

  public Iterator engineGetCertPathEncodings()
  {
    return PKIXCertPath.certPathEncodings.iterator();
  }
}