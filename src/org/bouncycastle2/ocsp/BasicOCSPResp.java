package org.bouncycastle2.ocsp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1OutputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.ocsp.BasicOCSPResponse;
import org.bouncycastle2.asn1.ocsp.ResponseData;
import org.bouncycastle2.asn1.ocsp.SingleResponse;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class BasicOCSPResp
  implements java.security.cert.X509Extension
{
  X509Certificate[] chain = null;
  ResponseData data;
  BasicOCSPResponse resp;

  public BasicOCSPResp(BasicOCSPResponse paramBasicOCSPResponse)
  {
    this.resp = paramBasicOCSPResponse;
    this.data = paramBasicOCSPResponse.getTbsResponseData();
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
        ASN1Sequence localASN1Sequence = this.resp.getCerts();
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
    X509Extensions localX509Extensions = getResponseExtensions();
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

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if (!(paramObject instanceof BasicOCSPResp))
      return false;
    BasicOCSPResp localBasicOCSPResp = (BasicOCSPResp)paramObject;
    return this.resp.equals(localBasicOCSPResp.resp);
  }

  public CertStore getCertificates(String paramString1, String paramString2)
    throws NoSuchAlgorithmException, NoSuchProviderException, OCSPException
  {
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
    return this.resp.getEncoded();
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = getResponseExtensions();
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

  public Date getProducedAt()
  {
    try
    {
      Date localDate = this.data.getProducedAt().getDate();
      return localDate;
    }
    catch (ParseException localParseException)
    {
      throw new IllegalStateException("ParseException:" + localParseException.getMessage());
    }
  }

  public RespID getResponderId()
  {
    return new RespID(this.data.getResponderID());
  }

  public RespData getResponseData()
  {
    return new RespData(this.resp.getTbsResponseData());
  }

  public X509Extensions getResponseExtensions()
  {
    return this.data.getResponseExtensions();
  }

  public SingleResp[] getResponses()
  {
    ASN1Sequence localASN1Sequence = this.data.getResponses();
    SingleResp[] arrayOfSingleResp = new SingleResp[localASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfSingleResp.length)
        return arrayOfSingleResp;
      arrayOfSingleResp[i] = new SingleResp(SingleResponse.getInstance(localASN1Sequence.getObjectAt(i)));
    }
  }

  public byte[] getSignature()
  {
    return this.resp.getSignature().getBytes();
  }

  public String getSignatureAlgName()
  {
    return OCSPUtil.getAlgorithmName(this.resp.getSignatureAlgorithm().getObjectId());
  }

  public String getSignatureAlgOID()
  {
    return this.resp.getSignatureAlgorithm().getObjectId().getId();
  }

  public byte[] getTBSResponseData()
    throws OCSPException
  {
    try
    {
      byte[] arrayOfByte = this.resp.getTbsResponseData().getEncoded();
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new OCSPException("problem encoding tbsResponseData", localIOException);
    }
  }

  public int getVersion()
  {
    return 1 + this.data.getVersion().getValue().intValue();
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    return (localSet != null) && (!localSet.isEmpty());
  }

  public int hashCode()
  {
    return this.resp.hashCode();
  }

  public boolean verify(PublicKey paramPublicKey, String paramString)
    throws OCSPException, NoSuchProviderException
  {
    try
    {
      Signature localSignature = OCSPUtil.createSignatureInstance(getSignatureAlgName(), paramString);
      localSignature.initVerify(paramPublicKey);
      localSignature.update(this.resp.getTbsResponseData().getEncoded("DER"));
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