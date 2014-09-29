package org.bouncycastle2.x509.extension;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;

public class SubjectKeyIdentifierStructure extends SubjectKeyIdentifier
{
  public SubjectKeyIdentifierStructure(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    super(fromPublicKey(paramPublicKey));
  }

  public SubjectKeyIdentifierStructure(byte[] paramArrayOfByte)
    throws IOException
  {
    super((ASN1OctetString)X509ExtensionUtil.fromExtensionValue(paramArrayOfByte));
  }

  private static ASN1OctetString fromPublicKey(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    try
    {
      ASN1OctetString localASN1OctetString = (ASN1OctetString)new SubjectKeyIdentifier(SubjectPublicKeyInfo.getInstance(paramPublicKey.getEncoded())).toASN1Object();
      return localASN1OctetString;
    }
    catch (Exception localException)
    {
      throw new InvalidKeyException("Exception extracting key details: " + localException.toString());
    }
  }
}