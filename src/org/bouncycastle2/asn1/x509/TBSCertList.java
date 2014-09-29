package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.DERUTCTime;

public class TBSCertList extends ASN1Encodable
{
  X509Extensions crlExtensions;
  X509Name issuer;
  Time nextUpdate;
  ASN1Sequence revokedCertificates;
  ASN1Sequence seq;
  AlgorithmIdentifier signature;
  Time thisUpdate;
  DERInteger version;

  public TBSCertList(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 3) || (paramASN1Sequence.size() > 7))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.seq = paramASN1Sequence;
    int i;
    int m;
    int n;
    if ((paramASN1Sequence.getObjectAt(0) instanceof DERInteger))
    {
      int i2 = 0 + 1;
      this.version = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
      i = i2;
      int j = i + 1;
      this.signature = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(i));
      int k = j + 1;
      this.issuer = X509Name.getInstance(paramASN1Sequence.getObjectAt(j));
      m = k + 1;
      this.thisUpdate = Time.getInstance(paramASN1Sequence.getObjectAt(k));
      if ((m >= paramASN1Sequence.size()) || ((!(paramASN1Sequence.getObjectAt(m) instanceof DERUTCTime)) && (!(paramASN1Sequence.getObjectAt(m) instanceof DERGeneralizedTime)) && (!(paramASN1Sequence.getObjectAt(m) instanceof Time))))
        break label296;
      n = m + 1;
      this.nextUpdate = Time.getInstance(paramASN1Sequence.getObjectAt(m));
    }
    while (true)
    {
      if ((n < paramASN1Sequence.size()) && (!(paramASN1Sequence.getObjectAt(n) instanceof DERTaggedObject)))
      {
        int i1 = n + 1;
        this.revokedCertificates = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(n));
        n = i1;
      }
      if ((n < paramASN1Sequence.size()) && ((paramASN1Sequence.getObjectAt(n) instanceof DERTaggedObject)))
        this.crlExtensions = X509Extensions.getInstance(paramASN1Sequence.getObjectAt(n));
      return;
      this.version = new DERInteger(0);
      i = 0;
      break;
      label296: n = m;
    }
  }

  public static TBSCertList getInstance(Object paramObject)
  {
    if ((paramObject instanceof TBSCertList))
      return (TBSCertList)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new TBSCertList((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static TBSCertList getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public X509Extensions getExtensions()
  {
    return this.crlExtensions;
  }

  public X509Name getIssuer()
  {
    return this.issuer;
  }

  public Time getNextUpdate()
  {
    return this.nextUpdate;
  }

  public Enumeration getRevokedCertificateEnumeration()
  {
    if (this.revokedCertificates == null)
      return new EmptyEnumeration(null);
    return new RevokedCertificatesEnumeration(this.revokedCertificates.getObjects());
  }

  public CRLEntry[] getRevokedCertificates()
  {
    CRLEntry[] arrayOfCRLEntry;
    if (this.revokedCertificates == null)
      arrayOfCRLEntry = new CRLEntry[0];
    while (true)
    {
      return arrayOfCRLEntry;
      arrayOfCRLEntry = new CRLEntry[this.revokedCertificates.size()];
      for (int i = 0; i < arrayOfCRLEntry.length; i++)
        arrayOfCRLEntry[i] = new CRLEntry(ASN1Sequence.getInstance(this.revokedCertificates.getObjectAt(i)));
    }
  }

  public AlgorithmIdentifier getSignature()
  {
    return this.signature;
  }

  public Time getThisUpdate()
  {
    return this.thisUpdate;
  }

  public int getVersion()
  {
    return 1 + this.version.getValue().intValue();
  }

  public DERInteger getVersionNumber()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }

  public static class CRLEntry extends ASN1Encodable
  {
    X509Extensions crlEntryExtensions;
    Time revocationDate;
    ASN1Sequence seq;
    DERInteger userCertificate;

    public CRLEntry(ASN1Sequence paramASN1Sequence)
    {
      if ((paramASN1Sequence.size() < 2) || (paramASN1Sequence.size() > 3))
        throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
      this.seq = paramASN1Sequence;
      this.userCertificate = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
      this.revocationDate = Time.getInstance(paramASN1Sequence.getObjectAt(1));
    }

    public X509Extensions getExtensions()
    {
      if ((this.crlEntryExtensions == null) && (this.seq.size() == 3))
        this.crlEntryExtensions = X509Extensions.getInstance(this.seq.getObjectAt(2));
      return this.crlEntryExtensions;
    }

    public Time getRevocationDate()
    {
      return this.revocationDate;
    }

    public DERInteger getUserCertificate()
    {
      return this.userCertificate;
    }

    public DERObject toASN1Object()
    {
      return this.seq;
    }
  }

  private class EmptyEnumeration
    implements Enumeration
  {
    private EmptyEnumeration()
    {
    }

    public boolean hasMoreElements()
    {
      return false;
    }

    public Object nextElement()
    {
      return null;
    }
  }

  private class RevokedCertificatesEnumeration
    implements Enumeration
  {
    private final Enumeration en;

    RevokedCertificatesEnumeration(Enumeration arg2)
    {
      Object localObject;
      this.en = localObject;
    }

    public boolean hasMoreElements()
    {
      return this.en.hasMoreElements();
    }

    public Object nextElement()
    {
      return new TBSCertList.CRLEntry(ASN1Sequence.getInstance(this.en.nextElement()));
    }
  }
}