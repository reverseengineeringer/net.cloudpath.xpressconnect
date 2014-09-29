package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class OtherHash extends ASN1Encodable
  implements ASN1Choice
{
  private OtherHashAlgAndValue otherHash;
  private ASN1OctetString sha1Hash;

  private OtherHash(ASN1OctetString paramASN1OctetString)
  {
    this.sha1Hash = paramASN1OctetString;
  }

  public OtherHash(OtherHashAlgAndValue paramOtherHashAlgAndValue)
  {
    this.otherHash = paramOtherHashAlgAndValue;
  }

  public OtherHash(byte[] paramArrayOfByte)
  {
    this.sha1Hash = new DEROctetString(paramArrayOfByte);
  }

  public static OtherHash getInstance(Object paramObject)
  {
    if ((paramObject instanceof OtherHash))
      return (OtherHash)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new OtherHash((ASN1OctetString)paramObject);
    return new OtherHash(OtherHashAlgAndValue.getInstance(paramObject));
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    if (this.otherHash == null)
      return new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1);
    return this.otherHash.getHashAlgorithm();
  }

  public byte[] getHashValue()
  {
    if (this.otherHash == null)
      return this.sha1Hash.getOctets();
    return this.otherHash.getHashValue().getOctets();
  }

  public DERObject toASN1Object()
  {
    if (this.otherHash == null)
      return this.sha1Hash;
    return this.otherHash.toASN1Object();
  }
}