package org.bouncycastle2.ocsp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.ocsp.BasicOCSPResponse;
import org.bouncycastle2.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle2.asn1.ocsp.OCSPResponse;
import org.bouncycastle2.asn1.ocsp.OCSPResponseStatus;
import org.bouncycastle2.asn1.ocsp.ResponseBytes;

public class OCSPResp
{
  private OCSPResponse resp;

  public OCSPResp(InputStream paramInputStream)
    throws IOException
  {
    this(new ASN1InputStream(paramInputStream));
  }

  private OCSPResp(ASN1InputStream paramASN1InputStream)
    throws IOException
  {
    try
    {
      this.resp = OCSPResponse.getInstance(paramASN1InputStream.readObject());
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new IOException("malformed response: " + localIllegalArgumentException.getMessage());
    }
    catch (ClassCastException localClassCastException)
    {
      throw new IOException("malformed response: " + localClassCastException.getMessage());
    }
  }

  public OCSPResp(OCSPResponse paramOCSPResponse)
  {
    this.resp = paramOCSPResponse;
  }

  public OCSPResp(byte[] paramArrayOfByte)
    throws IOException
  {
    this(new ASN1InputStream(paramArrayOfByte));
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if (!(paramObject instanceof OCSPResp))
      return false;
    OCSPResp localOCSPResp = (OCSPResp)paramObject;
    return this.resp.equals(localOCSPResp.resp);
  }

  public byte[] getEncoded()
    throws IOException
  {
    return this.resp.getEncoded();
  }

  public Object getResponseObject()
    throws OCSPException
  {
    ResponseBytes localResponseBytes = this.resp.getResponseBytes();
    if (localResponseBytes == null)
      return null;
    if (localResponseBytes.getResponseType().equals(OCSPObjectIdentifiers.id_pkix_ocsp_basic))
      try
      {
        BasicOCSPResp localBasicOCSPResp = new BasicOCSPResp(BasicOCSPResponse.getInstance(ASN1Object.fromByteArray(localResponseBytes.getResponse().getOctets())));
        return localBasicOCSPResp;
      }
      catch (Exception localException)
      {
        throw new OCSPException("problem decoding object: " + localException, localException);
      }
    return localResponseBytes.getResponse();
  }

  public int getStatus()
  {
    return this.resp.getResponseStatus().getValue().intValue();
  }

  public int hashCode()
  {
    return this.resp.hashCode();
  }
}