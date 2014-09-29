package org.bouncycastle2.asn1.isismtt.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.x500.DirectoryString;

public class AdditionalInformationSyntax extends ASN1Encodable
{
  private DirectoryString information;

  public AdditionalInformationSyntax(String paramString)
  {
    this(new DirectoryString(paramString));
  }

  private AdditionalInformationSyntax(DirectoryString paramDirectoryString)
  {
    this.information = paramDirectoryString;
  }

  public static AdditionalInformationSyntax getInstance(Object paramObject)
  {
    if ((paramObject instanceof AdditionalInformationSyntax))
      return (AdditionalInformationSyntax)paramObject;
    if ((paramObject instanceof ASN1String))
      return new AdditionalInformationSyntax(DirectoryString.getInstance(paramObject));
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public DirectoryString getInformation()
  {
    return this.information;
  }

  public DERObject toASN1Object()
  {
    return this.information.toASN1Object();
  }
}