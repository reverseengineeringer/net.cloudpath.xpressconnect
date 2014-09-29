package org.bouncycastle2.ocsp;

import java.io.IOException;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle2.asn1.ocsp.OCSPResponse;
import org.bouncycastle2.asn1.ocsp.OCSPResponseStatus;
import org.bouncycastle2.asn1.ocsp.ResponseBytes;

public class OCSPRespGenerator
{
  public static final int INTERNAL_ERROR = 2;
  public static final int MALFORMED_REQUEST = 1;
  public static final int SIG_REQUIRED = 5;
  public static final int SUCCESSFUL = 0;
  public static final int TRY_LATER = 3;
  public static final int UNAUTHORIZED = 6;

  public OCSPResp generate(int paramInt, Object paramObject)
    throws OCSPException
  {
    if (paramObject == null)
      return new OCSPResp(new OCSPResponse(new OCSPResponseStatus(paramInt), null));
    if ((paramObject instanceof BasicOCSPResp))
    {
      BasicOCSPResp localBasicOCSPResp = (BasicOCSPResp)paramObject;
      try
      {
        DEROctetString localDEROctetString = new DEROctetString(localBasicOCSPResp.getEncoded());
        ResponseBytes localResponseBytes = new ResponseBytes(OCSPObjectIdentifiers.id_pkix_ocsp_basic, localDEROctetString);
        return new OCSPResp(new OCSPResponse(new OCSPResponseStatus(paramInt), localResponseBytes));
      }
      catch (IOException localIOException)
      {
        throw new OCSPException("can't encode object.", localIOException);
      }
    }
    throw new OCSPException("unknown response object");
  }
}