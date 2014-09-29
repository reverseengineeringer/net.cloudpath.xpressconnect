package org.bouncycastle2.ocsp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.ocsp.CertID;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.jce.PrincipalUtil;
import org.bouncycastle2.jce.X509Principal;

public class CertificateID
{
  public static final String HASH_SHA1 = "1.3.14.3.2.26";
  private final CertID id;

  public CertificateID(String paramString, X509Certificate paramX509Certificate, BigInteger paramBigInteger)
    throws OCSPException
  {
    this(paramString, paramX509Certificate, paramBigInteger, "BC");
  }

  public CertificateID(String paramString1, X509Certificate paramX509Certificate, BigInteger paramBigInteger, String paramString2)
    throws OCSPException
  {
    this.id = createCertID(new AlgorithmIdentifier(new DERObjectIdentifier(paramString1), DERNull.INSTANCE), paramX509Certificate, new DERInteger(paramBigInteger), paramString2);
  }

  public CertificateID(CertID paramCertID)
  {
    if (paramCertID == null)
      throw new IllegalArgumentException("'id' cannot be null");
    this.id = paramCertID;
  }

  private static CertID createCertID(AlgorithmIdentifier paramAlgorithmIdentifier, X509Certificate paramX509Certificate, DERInteger paramDERInteger, String paramString)
    throws OCSPException
  {
    try
    {
      MessageDigest localMessageDigest = OCSPUtil.createDigestInstance(paramAlgorithmIdentifier.getAlgorithm().getId(), paramString);
      localMessageDigest.update(PrincipalUtil.getSubjectX509Principal(paramX509Certificate).getEncoded());
      DEROctetString localDEROctetString = new DEROctetString(localMessageDigest.digest());
      localMessageDigest.update(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(paramX509Certificate.getPublicKey().getEncoded()).readObject()).getPublicKeyData().getBytes());
      CertID localCertID = new CertID(paramAlgorithmIdentifier, localDEROctetString, new DEROctetString(localMessageDigest.digest()), paramDERInteger);
      return localCertID;
    }
    catch (Exception localException)
    {
      throw new OCSPException("problem creating ID: " + localException, localException);
    }
  }

  public static CertificateID deriveCertificateID(CertificateID paramCertificateID, BigInteger paramBigInteger)
  {
    return new CertificateID(new CertID(paramCertificateID.id.getHashAlgorithm(), paramCertificateID.id.getIssuerNameHash(), paramCertificateID.id.getIssuerKeyHash(), new DERInteger(paramBigInteger)));
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof CertificateID))
      return false;
    CertificateID localCertificateID = (CertificateID)paramObject;
    return this.id.getDERObject().equals(localCertificateID.id.getDERObject());
  }

  public String getHashAlgOID()
  {
    return this.id.getHashAlgorithm().getObjectId().getId();
  }

  public byte[] getIssuerKeyHash()
  {
    return this.id.getIssuerKeyHash().getOctets();
  }

  public byte[] getIssuerNameHash()
  {
    return this.id.getIssuerNameHash().getOctets();
  }

  public BigInteger getSerialNumber()
  {
    return this.id.getSerialNumber().getValue();
  }

  public int hashCode()
  {
    return this.id.getDERObject().hashCode();
  }

  public boolean matchesIssuer(X509Certificate paramX509Certificate, String paramString)
    throws OCSPException
  {
    return createCertID(this.id.getHashAlgorithm(), paramX509Certificate, this.id.getSerialNumber(), paramString).equals(this.id);
  }

  public CertID toASN1Object()
  {
    return this.id;
  }
}