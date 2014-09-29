package org.bouncycastle2.ocsp;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.ocsp.ResponseData;
import org.bouncycastle2.asn1.ocsp.SingleResponse;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class RespData
  implements java.security.cert.X509Extension
{
  ResponseData data;

  public RespData(ResponseData paramResponseData)
  {
    this.data = paramResponseData;
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

  public Set getCriticalExtensionOIDs()
  {
    return getExtensionOIDs(true);
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

  public int getVersion()
  {
    return 1 + this.data.getVersion().getValue().intValue();
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    Set localSet = getCriticalExtensionOIDs();
    return (localSet != null) && (!localSet.isEmpty());
  }
}