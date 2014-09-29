package org.bouncycastle2.x509;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.AttCertValidityPeriod;
import org.bouncycastle2.asn1.x509.AttributeCertificate;
import org.bouncycastle2.asn1.x509.AttributeCertificateInfo;
import org.bouncycastle2.asn1.x509.Holder;
import org.bouncycastle2.asn1.x509.X509Extension;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.util.Arrays;

public class X509V2AttributeCertificate
  implements X509AttributeCertificate
{
  private AttributeCertificate cert;
  private Date notAfter;
  private Date notBefore;

  public X509V2AttributeCertificate(InputStream paramInputStream)
    throws IOException
  {
    this(getObject(paramInputStream));
  }

  X509V2AttributeCertificate(AttributeCertificate paramAttributeCertificate)
    throws IOException
  {
    this.cert = paramAttributeCertificate;
    try
    {
      this.notAfter = paramAttributeCertificate.getAcinfo().getAttrCertValidityPeriod().getNotAfterTime().getDate();
      this.notBefore = paramAttributeCertificate.getAcinfo().getAttrCertValidityPeriod().getNotBeforeTime().getDate();
      return;
    }
    catch (ParseException localParseException)
    {
    }
    throw new IOException("invalid data structure in certificate!");
  }

  public X509V2AttributeCertificate(byte[] paramArrayOfByte)
    throws IOException
  {
    this(new ByteArrayInputStream(paramArrayOfByte));
  }

  private Set getExtensionOIDs(boolean paramBoolean)
  {
    X509Extensions localX509Extensions = this.cert.getAcinfo().getExtensions();
    if (localX509Extensions != null)
    {
      HashSet localHashSet = new HashSet();
      Enumeration localEnumeration = localX509Extensions.oids();
      while (true)
      {
        if (!localEnumeration.hasMoreElements())
          return localHashSet;
        DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
        if (localX509Extensions.getExtension(localDERObjectIdentifier).isCritical() == paramBoolean)
          localHashSet.add(localDERObjectIdentifier.getId());
      }
    }
    return null;
  }

  private static AttributeCertificate getObject(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      AttributeCertificate localAttributeCertificate = AttributeCertificate.getInstance(new ASN1InputStream(paramInputStream).readObject());
      return localAttributeCertificate;
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (Exception localException)
    {
      throw new IOException("exception decoding certificate structure: " + localException.toString());
    }
  }

  public void checkValidity()
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    checkValidity(new Date());
  }

  public void checkValidity(Date paramDate)
    throws CertificateExpiredException, CertificateNotYetValidException
  {
    if (paramDate.after(getNotAfter()))
      throw new CertificateExpiredException("certificate expired on " + getNotAfter());
    if (paramDate.before(getNotBefore()))
      throw new CertificateNotYetValidException("certificate not valid till " + getNotBefore());
  }

  public boolean equals(Object paramObject)
  {
    boolean bool2;
    if (paramObject == this)
      bool2 = true;
    boolean bool1;
    do
    {
      return bool2;
      bool1 = paramObject instanceof X509AttributeCertificate;
      bool2 = false;
    }
    while (!bool1);
    X509AttributeCertificate localX509AttributeCertificate = (X509AttributeCertificate)paramObject;
    try
    {
      boolean bool3 = Arrays.areEqual(getEncoded(), localX509AttributeCertificate.getEncoded());
      return bool3;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public X509Attribute[] getAttributes()
  {
    ASN1Sequence localASN1Sequence = this.cert.getAcinfo().getAttributes();
    X509Attribute[] arrayOfX509Attribute = new X509Attribute[localASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i == localASN1Sequence.size())
        return arrayOfX509Attribute;
      arrayOfX509Attribute[i] = new X509Attribute((ASN1Encodable)localASN1Sequence.getObjectAt(i));
    }
  }

  public X509Attribute[] getAttributes(String paramString)
  {
    ASN1Sequence localASN1Sequence = this.cert.getAcinfo().getAttributes();
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; ; i++)
    {
      if (i == localASN1Sequence.size())
      {
        if (localArrayList.size() != 0)
          break;
        return null;
      }
      X509Attribute localX509Attribute = new X509Attribute((ASN1Encodable)localASN1Sequence.getObjectAt(i));
      if (localX509Attribute.getOID().equals(paramString))
        localArrayList.add(localX509Attribute);
    }
    return (X509Attribute[])localArrayList.toArray(new X509Attribute[localArrayList.size()]);
  }

  public Set getCriticalExtensionOIDs()
  {
    return getExtensionOIDs(true);
  }

  public byte[] getEncoded()
    throws IOException
  {
    return this.cert.getEncoded();
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = this.cert.getAcinfo().getExtensions();
    if (localX509Extensions != null)
    {
      X509Extension localX509Extension = localX509Extensions.getExtension(new DERObjectIdentifier(paramString));
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

  public AttributeCertificateHolder getHolder()
  {
    return new AttributeCertificateHolder((ASN1Sequence)this.cert.getAcinfo().getHolder().toASN1Object());
  }

  public AttributeCertificateIssuer getIssuer()
  {
    return new AttributeCertificateIssuer(this.cert.getAcinfo().getIssuer());
  }

  public boolean[] getIssuerUniqueID()
  {
    DERBitString localDERBitString = this.cert.getAcinfo().getIssuerUniqueID();
    if (localDERBitString != null)
    {
      byte[] arrayOfByte = localDERBitString.getBytes();
      boolean[] arrayOfBoolean = new boolean[8 * arrayOfByte.length - localDERBitString.getPadBits()];
      int i = 0;
      if (i == arrayOfBoolean.length)
        return arrayOfBoolean;
      if ((arrayOfByte[(i / 8)] & 128 >>> i % 8) != 0);
      for (int j = 1; ; j = 0)
      {
        arrayOfBoolean[i] = j;
        i++;
        break;
      }
    }
    return null;
  }

  public Set getNonCriticalExtensionOIDs()
  {
    return getExtensionOIDs(false);
  }

  public Date getNotAfter()
  {
    return this.notAfter;
  }

  public Date getNotBefore()
  {
    return this.notBefore;
  }

  public BigInteger getSerialNumber()
  {
    return this.cert.getAcinfo().getSerialNumber().getValue();
  }

  public byte[] getSignature()
  {
    return this.cert.getSignatureValue().getBytes();
  }

  public int getVersion()
  {
    return 1 + this.cert.getAcinfo().getVersion().getValue().intValue();
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    return (localSet != null) && (!localSet.isEmpty());
  }

  public int hashCode()
  {
    try
    {
      int i = Arrays.hashCode(getEncoded());
      return i;
    }
    catch (IOException localIOException)
    {
    }
    return 0;
  }

  public final void verify(PublicKey paramPublicKey, String paramString)
    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
  {
    if (!this.cert.getSignatureAlgorithm().equals(this.cert.getAcinfo().getSignature()))
      throw new CertificateException("Signature algorithm in certificate info not same as outer certificate");
    Signature localSignature = Signature.getInstance(this.cert.getSignatureAlgorithm().getObjectId().getId(), paramString);
    localSignature.initVerify(paramPublicKey);
    try
    {
      localSignature.update(this.cert.getAcinfo().getEncoded());
      if (!localSignature.verify(getSignature()))
        throw new InvalidKeyException("Public key presented not for certificate signature");
    }
    catch (IOException localIOException)
    {
      throw new SignatureException("Exception encoding certificate info object");
    }
  }
}