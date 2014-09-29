package org.bouncycastle2.asn1.icao;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class LDSSecurityObject extends ASN1Encodable
  implements ICAOObjectIdentifiers
{
  public static final int ub_DataGroups = 16;
  private DataGroupHash[] datagroupHash;
  private AlgorithmIdentifier digestAlgorithmIdentifier;
  private DERInteger version = new DERInteger(0);
  private LDSVersionInfo versionInfo;

  private LDSSecurityObject(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence == null) || (paramASN1Sequence.size() == 0))
      throw new IllegalArgumentException("null or empty sequence passed.");
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.version = DERInteger.getInstance(localEnumeration.nextElement());
    this.digestAlgorithmIdentifier = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    ASN1Sequence localASN1Sequence = ASN1Sequence.getInstance(localEnumeration.nextElement());
    if (this.version.getValue().intValue() == 1)
      this.versionInfo = LDSVersionInfo.getInstance(localEnumeration.nextElement());
    checkDatagroupHashSeqSize(localASN1Sequence.size());
    this.datagroupHash = new DataGroupHash[localASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i >= localASN1Sequence.size())
        return;
      this.datagroupHash[i] = DataGroupHash.getInstance(localASN1Sequence.getObjectAt(i));
    }
  }

  public LDSSecurityObject(AlgorithmIdentifier paramAlgorithmIdentifier, DataGroupHash[] paramArrayOfDataGroupHash)
  {
    this.version = new DERInteger(0);
    this.digestAlgorithmIdentifier = paramAlgorithmIdentifier;
    this.datagroupHash = paramArrayOfDataGroupHash;
    checkDatagroupHashSeqSize(paramArrayOfDataGroupHash.length);
  }

  public LDSSecurityObject(AlgorithmIdentifier paramAlgorithmIdentifier, DataGroupHash[] paramArrayOfDataGroupHash, LDSVersionInfo paramLDSVersionInfo)
  {
    this.version = new DERInteger(1);
    this.digestAlgorithmIdentifier = paramAlgorithmIdentifier;
    this.datagroupHash = paramArrayOfDataGroupHash;
    this.versionInfo = paramLDSVersionInfo;
    checkDatagroupHashSeqSize(paramArrayOfDataGroupHash.length);
  }

  private void checkDatagroupHashSeqSize(int paramInt)
  {
    if ((paramInt < 2) || (paramInt > 16))
      throw new IllegalArgumentException("wrong size in DataGroupHashValues : not in (2..16)");
  }

  public static LDSSecurityObject getInstance(Object paramObject)
  {
    if ((paramObject instanceof LDSSecurityObject))
      return (LDSSecurityObject)paramObject;
    if (paramObject != null)
      return new LDSSecurityObject(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public DataGroupHash[] getDatagroupHash()
  {
    return this.datagroupHash;
  }

  public AlgorithmIdentifier getDigestAlgorithmIdentifier()
  {
    return this.digestAlgorithmIdentifier;
  }

  public int getVersion()
  {
    return this.version.getValue().intValue();
  }

  public LDSVersionInfo getVersionInfo()
  {
    return this.versionInfo;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    localASN1EncodableVector1.add(this.version);
    localASN1EncodableVector1.add(this.digestAlgorithmIdentifier);
    ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= this.datagroupHash.length)
      {
        localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
        if (this.versionInfo != null)
          localASN1EncodableVector1.add(this.versionInfo);
        return new DERSequence(localASN1EncodableVector1);
      }
      localASN1EncodableVector2.add(this.datagroupHash[i]);
    }
  }
}