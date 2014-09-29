package org.bouncycastle2.jce.provider;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.pkcs.ContentInfo;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.SignedData;
import org.bouncycastle2.openssl.PEMWriter;

public class PKIXCertPath extends CertPath
{
  static final List certPathEncodings = Collections.unmodifiableList(localArrayList);
  private List certificates;

  static
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("PkiPath");
    localArrayList.add("PEM");
    localArrayList.add("PKCS7");
  }

  PKIXCertPath(InputStream paramInputStream, String paramString)
    throws CertificateException
  {
    super("X.509");
    try
    {
      if (paramString.equalsIgnoreCase("PkiPath"))
      {
        localDERObject = new ASN1InputStream(paramInputStream).readObject();
        if (!(localDERObject instanceof ASN1Sequence))
          throw new CertificateException("input stream does not contain a ASN1 SEQUENCE while reading PkiPath encoded data to load CertPath");
      }
    }
    catch (IOException localIOException1)
    {
      DERObject localDERObject;
      throw new CertificateException("IOException throw while decoding CertPath:\n" + localIOException1.toString());
      Enumeration localEnumeration = ((ASN1Sequence)localDERObject).getObjects();
      this.certificates = new ArrayList();
      CertificateFactory localCertificateFactory2 = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
      while (true)
      {
        boolean bool = localEnumeration.hasMoreElements();
        if (!bool)
        {
          this.certificates = sortCerts(this.certificates);
          return;
        }
        byte[] arrayOfByte = ((ASN1Encodable)localEnumeration.nextElement()).getEncoded("DER");
        this.certificates.add(0, localCertificateFactory2.generateCertificate(new ByteArrayInputStream(arrayOfByte)));
      }
    }
    catch (NoSuchProviderException localNoSuchProviderException1)
    {
    }
    while (true)
      while (true)
      {
        throw new CertificateException("BouncyCastle provider not found while trying to get a CertificateFactory:\n" + localNoSuchProviderException1.toString());
        BufferedInputStream localBufferedInputStream;
        if ((paramString.equalsIgnoreCase("PKCS7")) || (paramString.equalsIgnoreCase("PEM")))
          localBufferedInputStream = new BufferedInputStream(paramInputStream);
        try
        {
          this.certificates = new ArrayList();
          CertificateFactory localCertificateFactory1 = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
          while (true)
          {
            Certificate localCertificate = localCertificateFactory1.generateCertificate(localBufferedInputStream);
            if (localCertificate == null)
              break;
            this.certificates.add(localCertificate);
          }
        }
        catch (IOException localIOException2)
        {
          break;
          throw new CertificateException("unsupported encoding: " + paramString);
        }
        catch (NoSuchProviderException localNoSuchProviderException2)
        {
        }
      }
  }

  PKIXCertPath(List paramList)
  {
    super("X.509");
    this.certificates = sortCerts(new ArrayList(paramList));
  }

  private List sortCerts(List paramList)
  {
    if (paramList.size() < 2);
    ArrayList localArrayList1;
    ArrayList localArrayList2;
    int k;
    label144: 
    while (true)
    {
      return paramList;
      X500Principal localX500Principal1 = ((X509Certificate)paramList.get(0)).getIssuerX500Principal();
      int i = 1;
      int j = 1;
      if (j == paramList.size());
      while (true)
      {
        if (i != 0)
          break label144;
        localArrayList1 = new ArrayList(paramList.size());
        localArrayList2 = new ArrayList(paramList);
        k = 0;
        if (k < paramList.size())
          break label146;
        if (localArrayList1.size() <= 1)
          break label251;
        return localArrayList2;
        if (localX500Principal1.equals(((X509Certificate)paramList.get(j)).getSubjectX500Principal()))
        {
          localX500Principal1 = ((X509Certificate)paramList.get(j)).getIssuerX500Principal();
          j++;
          break;
        }
        i = 0;
      }
    }
    label146: X509Certificate localX509Certificate1 = (X509Certificate)paramList.get(k);
    X500Principal localX500Principal2 = localX509Certificate1.getSubjectX500Principal();
    label245: for (int m = 0; ; m++)
    {
      int n = paramList.size();
      int i1 = 0;
      if (m == n);
      while (true)
      {
        if (i1 == 0)
        {
          localArrayList1.add(localX509Certificate1);
          paramList.remove(k);
        }
        k++;
        break;
        if (!((X509Certificate)paramList.get(m)).getIssuerX500Principal().equals(localX500Principal2))
          break label245;
        i1 = 1;
      }
    }
    label251: int i2 = 0;
    if (i2 == localArrayList1.size())
    {
      if (paramList.size() > 0)
        return localArrayList2;
    }
    else
    {
      X500Principal localX500Principal3 = ((X509Certificate)localArrayList1.get(i2)).getIssuerX500Principal();
      label363: for (int i3 = 0; ; i3++)
      {
        if (i3 >= paramList.size());
        while (true)
        {
          i2++;
          break;
          X509Certificate localX509Certificate2 = (X509Certificate)paramList.get(i3);
          if (!localX500Principal3.equals(localX509Certificate2.getSubjectX500Principal()))
            break label363;
          localArrayList1.add(localX509Certificate2);
          paramList.remove(i3);
        }
      }
    }
    return localArrayList1;
  }

  private DERObject toASN1Object(X509Certificate paramX509Certificate)
    throws CertificateEncodingException
  {
    try
    {
      DERObject localDERObject = new ASN1InputStream(paramX509Certificate.getEncoded()).readObject();
      return localDERObject;
    }
    catch (Exception localException)
    {
      throw new CertificateEncodingException("Exception while encoding certificate: " + localException.toString());
    }
  }

  private byte[] toDEREncoded(ASN1Encodable paramASN1Encodable)
    throws CertificateEncodingException
  {
    try
    {
      byte[] arrayOfByte = paramASN1Encodable.getEncoded("DER");
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new CertificateEncodingException("Exception thrown: " + localIOException);
    }
  }

  public List getCertificates()
  {
    return Collections.unmodifiableList(new ArrayList(this.certificates));
  }

  public byte[] getEncoded()
    throws CertificateEncodingException
  {
    Iterator localIterator = getEncodings();
    if (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if ((localObject instanceof String))
        return getEncoded((String)localObject);
    }
    return null;
  }

  public byte[] getEncoded(String paramString)
    throws CertificateEncodingException
  {
    if (paramString.equalsIgnoreCase("PkiPath"))
    {
      ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
      ListIterator localListIterator = this.certificates.listIterator(this.certificates.size());
      while (true)
      {
        if (!localListIterator.hasPrevious())
          return toDEREncoded(new DERSequence(localASN1EncodableVector1));
        localASN1EncodableVector1.add(toASN1Object((X509Certificate)localListIterator.previous()));
      }
    }
    if (paramString.equalsIgnoreCase("PKCS7"))
    {
      ContentInfo localContentInfo = new ContentInfo(PKCSObjectIdentifiers.data, null);
      ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
      for (int i = 0; ; i++)
      {
        if (i == this.certificates.size())
        {
          SignedData localSignedData = new SignedData(new DERInteger(1), new DERSet(), localContentInfo, new DERSet(localASN1EncodableVector2), null, new DERSet());
          return toDEREncoded(new ContentInfo(PKCSObjectIdentifiers.signedData, localSignedData));
        }
        localASN1EncodableVector2.add(toASN1Object((X509Certificate)this.certificates.get(i)));
      }
    }
    if (paramString.equalsIgnoreCase("PEM"))
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      PEMWriter localPEMWriter = new PEMWriter(new OutputStreamWriter(localByteArrayOutputStream));
      int j = 0;
      try
      {
        while (true)
        {
          if (j == this.certificates.size())
          {
            localPEMWriter.close();
            return localByteArrayOutputStream.toByteArray();
          }
          localPEMWriter.writeObject(this.certificates.get(j));
          j++;
        }
      }
      catch (Exception localException)
      {
        throw new CertificateEncodingException("can't encode certificate for PEM encoded path");
      }
    }
    throw new CertificateEncodingException("unsupported encoding: " + paramString);
  }

  public Iterator getEncodings()
  {
    return certPathEncodings.iterator();
  }
}