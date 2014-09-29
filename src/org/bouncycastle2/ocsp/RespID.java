package org.bouncycastle2.ocsp;

import java.security.MessageDigest;
import java.security.PublicKey;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.ocsp.ResponderID;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;

public class RespID
{
  ResponderID id;

  public RespID(PublicKey paramPublicKey)
    throws OCSPException
  {
    try
    {
      MessageDigest localMessageDigest = OCSPUtil.createDigestInstance("SHA1", null);
      localMessageDigest.update(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(paramPublicKey.getEncoded()).readObject()).getPublicKeyData().getBytes());
      this.id = new ResponderID(new DEROctetString(localMessageDigest.digest()));
      return;
    }
    catch (Exception localException)
    {
      throw new OCSPException("problem creating ID: " + localException, localException);
    }
  }

  public RespID(X500Principal paramX500Principal)
  {
    this.id = new ResponderID(X500Name.getInstance(paramX500Principal.getEncoded()));
  }

  public RespID(ResponderID paramResponderID)
  {
    this.id = paramResponderID;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof RespID))
      return false;
    RespID localRespID = (RespID)paramObject;
    return this.id.equals(localRespID.id);
  }

  public int hashCode()
  {
    return this.id.hashCode();
  }

  public ResponderID toASN1Object()
  {
    return this.id;
  }
}