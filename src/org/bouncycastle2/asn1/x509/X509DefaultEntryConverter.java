package org.bouncycastle2.asn1.x509;

import java.io.IOException;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERUTF8String;

public class X509DefaultEntryConverter extends X509NameEntryConverter
{
  public DERObject getConvertedValue(DERObjectIdentifier paramDERObjectIdentifier, String paramString)
  {
    if ((paramString.length() != 0) && (paramString.charAt(0) == '#'))
      try
      {
        DERObject localDERObject = convertHexEncoded(paramString, 1);
        return localDERObject;
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("can't recode value for oid " + paramDERObjectIdentifier.getId());
      }
    if ((paramString.length() != 0) && (paramString.charAt(0) == '\\'))
      paramString = paramString.substring(1);
    if ((paramDERObjectIdentifier.equals(X509Name.EmailAddress)) || (paramDERObjectIdentifier.equals(X509Name.DC)))
      return new DERIA5String(paramString);
    if (paramDERObjectIdentifier.equals(X509Name.DATE_OF_BIRTH))
      return new DERGeneralizedTime(paramString);
    if ((paramDERObjectIdentifier.equals(X509Name.C)) || (paramDERObjectIdentifier.equals(X509Name.SN)) || (paramDERObjectIdentifier.equals(X509Name.DN_QUALIFIER)) || (paramDERObjectIdentifier.equals(X509Name.TELEPHONE_NUMBER)))
      return new DERPrintableString(paramString);
    return new DERUTF8String(paramString);
  }
}