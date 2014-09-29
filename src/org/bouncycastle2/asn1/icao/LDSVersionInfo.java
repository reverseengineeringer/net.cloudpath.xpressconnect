package org.bouncycastle2.asn1.icao;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERSequence;

public class LDSVersionInfo extends ASN1Encodable
{
  private DERPrintableString ldsVersion;
  private DERPrintableString unicodeVersion;

  public LDSVersionInfo(String paramString1, String paramString2)
  {
    this.ldsVersion = new DERPrintableString(paramString1);
    this.unicodeVersion = new DERPrintableString(paramString2);
  }

  private LDSVersionInfo(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("sequence wrong size for LDSVersionInfo");
    this.ldsVersion = DERPrintableString.getInstance(paramASN1Sequence.getObjectAt(0));
    this.unicodeVersion = DERPrintableString.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public static LDSVersionInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof LDSVersionInfo))
      return (LDSVersionInfo)paramObject;
    if (paramObject != null)
      return new LDSVersionInfo(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public String getLdsVersion()
  {
    return this.ldsVersion.getString();
  }

  public String getUnicodeVersion()
  {
    return this.unicodeVersion.getString();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.ldsVersion);
    localASN1EncodableVector.add(this.unicodeVersion);
    return new DERSequence(localASN1EncodableVector);
  }
}