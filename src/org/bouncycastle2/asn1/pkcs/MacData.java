package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.DigestInfo;

public class MacData extends ASN1Encodable
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  DigestInfo digInfo;
  BigInteger iterationCount;
  byte[] salt;

  public MacData(ASN1Sequence paramASN1Sequence)
  {
    this.digInfo = DigestInfo.getInstance(paramASN1Sequence.getObjectAt(0));
    this.salt = ((ASN1OctetString)paramASN1Sequence.getObjectAt(1)).getOctets();
    if (paramASN1Sequence.size() == 3)
    {
      this.iterationCount = ((DERInteger)paramASN1Sequence.getObjectAt(2)).getValue();
      return;
    }
    this.iterationCount = ONE;
  }

  public MacData(DigestInfo paramDigestInfo, byte[] paramArrayOfByte, int paramInt)
  {
    this.digInfo = paramDigestInfo;
    this.salt = paramArrayOfByte;
    this.iterationCount = BigInteger.valueOf(paramInt);
  }

  public static MacData getInstance(Object paramObject)
  {
    if ((paramObject instanceof MacData))
      return (MacData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new MacData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public BigInteger getIterationCount()
  {
    return this.iterationCount;
  }

  public DigestInfo getMac()
  {
    return this.digInfo;
  }

  public byte[] getSalt()
  {
    return this.salt;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.digInfo);
    localASN1EncodableVector.add(new DEROctetString(this.salt));
    if (!this.iterationCount.equals(ONE))
      localASN1EncodableVector.add(new DERInteger(this.iterationCount));
    return new DERSequence(localASN1EncodableVector);
  }
}