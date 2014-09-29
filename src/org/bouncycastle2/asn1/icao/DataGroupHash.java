package org.bouncycastle2.asn1.icao;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class DataGroupHash extends ASN1Encodable
{
  ASN1OctetString dataGroupHashValue;
  DERInteger dataGroupNumber;

  public DataGroupHash(int paramInt, ASN1OctetString paramASN1OctetString)
  {
    this.dataGroupNumber = new DERInteger(paramInt);
    this.dataGroupHashValue = paramASN1OctetString;
  }

  private DataGroupHash(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.dataGroupNumber = DERInteger.getInstance(localEnumeration.nextElement());
    this.dataGroupHashValue = ASN1OctetString.getInstance(localEnumeration.nextElement());
  }

  public static DataGroupHash getInstance(Object paramObject)
  {
    if ((paramObject instanceof DataGroupHash))
      return (DataGroupHash)paramObject;
    if (paramObject != null)
      return new DataGroupHash(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public ASN1OctetString getDataGroupHashValue()
  {
    return this.dataGroupHashValue;
  }

  public int getDataGroupNumber()
  {
    return this.dataGroupNumber.getValue().intValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.dataGroupNumber);
    localASN1EncodableVector.add(this.dataGroupHashValue);
    return new DERSequence(localASN1EncodableVector);
  }
}