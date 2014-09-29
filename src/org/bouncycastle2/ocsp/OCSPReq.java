package org.bouncycastle2.ocsp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1OutputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.ocsp.OCSPRequest;
import org.bouncycastle2.asn1.ocsp.Request;
import org.bouncycastle2.asn1.ocsp.TBSRequest;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class OCSPReq
  implements java.security.cert.X509Extension
{
  private OCSPRequest req;

  public OCSPReq(InputStream paramInputStream)
    throws IOException
  {
    this(new ASN1InputStream(paramInputStream));
  }

  private OCSPReq(ASN1InputStream paramASN1InputStream)
    throws IOException
  {
    try
    {
      this.req = OCSPRequest.getInstance(paramASN1InputStream.readObject());
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new IOException("malformed request: " + localIllegalArgumentException.getMessage());
    }
    catch (ClassCastException localClassCastException)
    {
      throw new IOException("malformed request: " + localClassCastException.getMessage());
    }
  }

  public OCSPReq(OCSPRequest paramOCSPRequest)
  {
    this.req = paramOCSPRequest;
  }

  public OCSPReq(byte[] paramArrayOfByte)
    throws IOException
  {
    this(new ASN1InputStream(paramArrayOfByte));
  }

  private List getCertList(String paramString)
    throws OCSPException, NoSuchProviderException
  {
    ArrayList localArrayList = new ArrayList();
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ASN1OutputStream localASN1OutputStream = new ASN1OutputStream(localByteArrayOutputStream);
    while (true)
    {
      CertificateFactory localCertificateFactory;
      Enumeration localEnumeration;
      try
      {
        localCertificateFactory = OCSPUtil.createX509CertificateFactory(paramString);
        ASN1Sequence localASN1Sequence = this.req.getOptionalSignature().getCerts();
        if (localASN1Sequence != null)
        {
          localEnumeration = localASN1Sequence.getObjects();
          if (localEnumeration.hasMoreElements());
        }
        else
        {
          return localArrayList;
        }
      }
      catch (CertificateException localCertificateException1)
      {
        throw new OCSPException("can't get certificate factory.", localCertificateException1);
      }
      try
      {
        localASN1OutputStream.writeObject(localEnumeration.nextElement());
        localArrayList.add(localCertificateFactory.generateCertificate(new ByteArrayInputStream(localByteArrayOutputStream.toByteArray())));
        localByteArrayOutputStream.reset();
      }
      catch (IOException localIOException)
      {
        throw new OCSPException("can't re-encode certificate!", localIOException);
      }
      catch (CertificateException localCertificateException2)
      {
        throw new OCSPException("can't re-encode certificate!", localCertificateException2);
      }
    }
  }

  private Set getExtensionOIDs(boolean paramBoolean)
  {
    HashSet localHashSet = new HashSet();
    X509Extensions localX509Extensions = getRequestExtensions();
    Enumeration localEnumeration;
    if (localX509Extensions != null)
      localEnumeration = localX509Extensions.oids();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return localHashSet;
      ASN1ObjectIdentifier localASN1ObjectIdentifier = (ASN1ObjectIdentifier)localEnumeration.nextElement();
      if (paramBoolean == localX509Extensions.getExtension(localASN1ObjectIdentifier).isCritical())
        localHashSet.add(localASN1ObjectIdentifier.getId());
    }
  }

  public CertStore getCertificates(String paramString1, String paramString2)
    throws NoSuchAlgorithmException, NoSuchProviderException, OCSPException
  {
    if (!isSigned())
      return null;
    try
    {
      CertStore localCertStore = OCSPUtil.createCertStoreInstance(paramString1, new CollectionCertStoreParameters(getCertList(paramString2)), paramString2);
      return localCertStore;
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new OCSPException("can't setup the CertStore", localInvalidAlgorithmParameterException);
    }
  }

  public X509Certificate[] getCerts(String paramString)
    throws OCSPException, NoSuchProviderException
  {
    if (!isSigned())
      return null;
    List localList = getCertList(paramString);
    return (X509Certificate[])localList.toArray(new X509Certificate[localList.size()]);
  }

  public Set getCriticalExtensionOIDs()
  {
    return getExtensionOIDs(true);
  }

  public byte[] getEncoded()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    new ASN1OutputStream(localByteArrayOutputStream).writeObject(this.req);
    return localByteArrayOutputStream.toByteArray();
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = getRequestExtensions();
    if (localX509Extensions != null)
    {
      org.bouncycastle2.asn1.x509.X509Extension localX509Extension = localX509Extensions.getExtension(new ASN1ObjectIdentifier(paramString));
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

  public X509Extensions getRequestExtensions()
  {
    return X509Extensions.getInstance(this.req.getTbsRequest().getRequestExtensions());
  }

  public Req[] getRequestList()
  {
    ASN1Sequence localASN1Sequence = this.req.getTbsRequest().getRequestList();
    Req[] arrayOfReq = new Req[localASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfReq.length)
        return arrayOfReq;
      arrayOfReq[i] = new Req(Request.getInstance(localASN1Sequence.getObjectAt(i)));
    }
  }

  public GeneralName getRequestorName()
  {
    return GeneralName.getInstance(this.req.getTbsRequest().getRequestorName());
  }

  public byte[] getSignature()
  {
    if (!isSigned())
      return null;
    return this.req.getOptionalSignature().getSignature().getBytes();
  }

  public String getSignatureAlgOID()
  {
    if (!isSigned())
      return null;
    return this.req.getOptionalSignature().getSignatureAlgorithm().getObjectId().getId();
  }

  public byte[] getTBSRequest()
    throws OCSPException
  {
    try
    {
      byte[] arrayOfByte = this.req.getTbsRequest().getEncoded();
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new OCSPException("problem encoding tbsRequest", localIOException);
    }
  }

  public int getVersion()
  {
    return 1 + this.req.getTbsRequest().getVersion().getValue().intValue();
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    return (localSet != null) && (!localSet.isEmpty());
  }

  public boolean isSigned()
  {
    return this.req.getOptionalSignature() != null;
  }

  public boolean verify(PublicKey paramPublicKey, String paramString)
    throws OCSPException, NoSuchProviderException
  {
    if (!isSigned())
      throw new OCSPException("attempt to verify signature on unsigned object");
    try
    {
      java.security.Signature localSignature = OCSPUtil.createSignatureInstance(getSignatureAlgOID(), paramString);
      localSignature.initVerify(paramPublicKey);
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      new ASN1OutputStream(localByteArrayOutputStream).writeObject(this.req.getTbsRequest());
      localSignature.update(localByteArrayOutputStream.toByteArray());
      boolean bool = localSignature.verify(getSignature());
      return bool;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw localNoSuchProviderException;
    }
    catch (Exception localException)
    {
      throw new OCSPException("exception processing sig: " + localException, localException);
    }
  }
}