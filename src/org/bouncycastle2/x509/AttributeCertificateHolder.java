package org.bouncycastle2.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertSelector;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREnumerated;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.GeneralNames;
import org.bouncycastle2.asn1.x509.Holder;
import org.bouncycastle2.asn1.x509.IssuerSerial;
import org.bouncycastle2.asn1.x509.ObjectDigestInfo;
import org.bouncycastle2.jce.PrincipalUtil;
import org.bouncycastle2.jce.X509Principal;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.Selector;

public class AttributeCertificateHolder
  implements CertSelector, Selector
{
  final Holder holder;

  public AttributeCertificateHolder(int paramInt, String paramString1, String paramString2, byte[] paramArrayOfByte)
  {
    this.holder = new Holder(new ObjectDigestInfo(paramInt, paramString2, new AlgorithmIdentifier(paramString1), Arrays.clone(paramArrayOfByte)));
  }

  public AttributeCertificateHolder(X509Certificate paramX509Certificate)
    throws CertificateParsingException
  {
    try
    {
      X509Principal localX509Principal = PrincipalUtil.getIssuerX509Principal(paramX509Certificate);
      this.holder = new Holder(new IssuerSerial(generateGeneralNames(localX509Principal), new DERInteger(paramX509Certificate.getSerialNumber())));
      return;
    }
    catch (Exception localException)
    {
      throw new CertificateParsingException(localException.getMessage());
    }
  }

  public AttributeCertificateHolder(X500Principal paramX500Principal)
  {
    this(X509Util.convertPrincipal(paramX500Principal));
  }

  public AttributeCertificateHolder(X500Principal paramX500Principal, BigInteger paramBigInteger)
  {
    this(X509Util.convertPrincipal(paramX500Principal), paramBigInteger);
  }

  AttributeCertificateHolder(ASN1Sequence paramASN1Sequence)
  {
    this.holder = Holder.getInstance(paramASN1Sequence);
  }

  public AttributeCertificateHolder(X509Principal paramX509Principal)
  {
    this.holder = new Holder(generateGeneralNames(paramX509Principal));
  }

  public AttributeCertificateHolder(X509Principal paramX509Principal, BigInteger paramBigInteger)
  {
    this.holder = new Holder(new IssuerSerial(new GeneralNames(new DERSequence(new GeneralName(paramX509Principal))), new DERInteger(paramBigInteger)));
  }

  private GeneralNames generateGeneralNames(X509Principal paramX509Principal)
  {
    return new GeneralNames(new DERSequence(new GeneralName(paramX509Principal)));
  }

  private Object[] getNames(GeneralName[] paramArrayOfGeneralName)
  {
    ArrayList localArrayList = new ArrayList(paramArrayOfGeneralName.length);
    int i = 0;
    while (true)
    {
      if (i == paramArrayOfGeneralName.length)
        return localArrayList.toArray(new Object[localArrayList.size()]);
      if (paramArrayOfGeneralName[i].getTagNo() == 4);
      try
      {
        localArrayList.add(new X500Principal(((ASN1Encodable)paramArrayOfGeneralName[i].getName()).getEncoded()));
        i++;
      }
      catch (IOException localIOException)
      {
      }
    }
    throw new RuntimeException("badly formed Name object");
  }

  private Principal[] getPrincipals(GeneralNames paramGeneralNames)
  {
    Object[] arrayOfObject = getNames(paramGeneralNames.getNames());
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; ; i++)
    {
      if (i == arrayOfObject.length)
        return (Principal[])localArrayList.toArray(new Principal[localArrayList.size()]);
      if ((arrayOfObject[i] instanceof Principal))
        localArrayList.add(arrayOfObject[i]);
    }
  }

  private boolean matchesDN(X509Principal paramX509Principal, GeneralNames paramGeneralNames)
  {
    GeneralName[] arrayOfGeneralName = paramGeneralNames.getNames();
    for (int i = 0; ; i++)
    {
      if (i == arrayOfGeneralName.length)
        return false;
      GeneralName localGeneralName = arrayOfGeneralName[i];
      if (localGeneralName.getTagNo() == 4)
        try
        {
          boolean bool = new X509Principal(((ASN1Encodable)localGeneralName.getName()).getEncoded()).equals(paramX509Principal);
          if (bool)
            return true;
        }
        catch (IOException localIOException)
        {
        }
    }
  }

  public Object clone()
  {
    return new AttributeCertificateHolder((ASN1Sequence)this.holder.toASN1Object());
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if (!(paramObject instanceof AttributeCertificateHolder))
      return false;
    AttributeCertificateHolder localAttributeCertificateHolder = (AttributeCertificateHolder)paramObject;
    return this.holder.equals(localAttributeCertificateHolder.holder);
  }

  public String getDigestAlgorithm()
  {
    if (this.holder.getObjectDigestInfo() != null)
      return this.holder.getObjectDigestInfo().getDigestAlgorithm().getObjectId().getId();
    return null;
  }

  public int getDigestedObjectType()
  {
    if (this.holder.getObjectDigestInfo() != null)
      return this.holder.getObjectDigestInfo().getDigestedObjectType().getValue().intValue();
    return -1;
  }

  public Principal[] getEntityNames()
  {
    if (this.holder.getEntityName() != null)
      return getPrincipals(this.holder.getEntityName());
    return null;
  }

  public Principal[] getIssuer()
  {
    if (this.holder.getBaseCertificateID() != null)
      return getPrincipals(this.holder.getBaseCertificateID().getIssuer());
    return null;
  }

  public byte[] getObjectDigest()
  {
    if (this.holder.getObjectDigestInfo() != null)
      return this.holder.getObjectDigestInfo().getObjectDigest().getBytes();
    return null;
  }

  public String getOtherObjectTypeID()
  {
    if (this.holder.getObjectDigestInfo() != null)
      this.holder.getObjectDigestInfo().getOtherObjectTypeID().getId();
    return null;
  }

  public BigInteger getSerialNumber()
  {
    if (this.holder.getBaseCertificateID() != null)
      return this.holder.getBaseCertificateID().getSerial().getValue();
    return null;
  }

  public int hashCode()
  {
    return this.holder.hashCode();
  }

  public boolean match(Object paramObject)
  {
    if (!(paramObject instanceof X509Certificate))
      return false;
    return match((Certificate)paramObject);
  }

  public boolean match(Certificate paramCertificate)
  {
    if (!(paramCertificate instanceof X509Certificate));
    while (true)
    {
      return false;
      X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
      try
      {
        if (this.holder.getBaseCertificateID() != null)
        {
          if ((this.holder.getBaseCertificateID().getSerial().getValue().equals(localX509Certificate.getSerialNumber())) && (matchesDN(PrincipalUtil.getIssuerX509Principal(localX509Certificate), this.holder.getBaseCertificateID().getIssuer())))
            return true;
        }
        else
        {
          if ((this.holder.getEntityName() != null) && (matchesDN(PrincipalUtil.getSubjectX509Principal(localX509Certificate), this.holder.getEntityName())))
            return true;
          ObjectDigestInfo localObjectDigestInfo = this.holder.getObjectDigestInfo();
          if (localObjectDigestInfo != null)
            try
            {
              MessageDigest localMessageDigest = MessageDigest.getInstance(getDigestAlgorithm(), "BC");
              switch (getDigestedObjectType())
              {
              default:
              case 0:
              case 1:
              }
              while (!Arrays.areEqual(localMessageDigest.digest(), getObjectDigest()))
              {
                return false;
                localMessageDigest.update(paramCertificate.getPublicKey().getEncoded());
                continue;
                localMessageDigest.update(paramCertificate.getEncoded());
              }
            }
            catch (Exception localException)
            {
              return false;
            }
        }
      }
      catch (CertificateEncodingException localCertificateEncodingException)
      {
      }
    }
    return false;
  }
}