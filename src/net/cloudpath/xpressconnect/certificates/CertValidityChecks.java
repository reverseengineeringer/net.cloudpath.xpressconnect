package net.cloudpath.xpressconnect.certificates;

import android.content.Context;
import android.content.res.Resources;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class CertValidityChecks
{
  private X509Certificate mCert = null;
  private Context mCtx = null;
  private ParcelHelper mHelp = null;
  private Logger mLogger = null;

  public CertValidityChecks(Logger paramLogger, Context paramContext)
  {
    this.mLogger = paramLogger;
    this.mCtx = paramContext;
    this.mHelp = new ParcelHelper("", this.mCtx);
  }

  public boolean checkValidity(String paramString, FailureReason paramFailureReason)
  {
    if (!setCertificateToCheck(paramString))
    {
      Util.log(this.mLogger, "Failed to convert stripped cert to an X509 structure!");
      if (paramFailureReason != null)
        paramFailureReason.setFailReason(FailureReason.useErrorIcon, this.mCtx.getResources().getString(this.mHelp.getIdentifier("xpc_cert_data_invalid", "string")), 2);
    }
    do
    {
      return false;
      if (isValid() == 0)
        break;
    }
    while (paramFailureReason == null);
    paramFailureReason.setFailReason(FailureReason.useWarningIcon, this.mCtx.getResources().getString(this.mHelp.getIdentifier("xpc_cert_invalid_time", "string")), 2);
    return false;
    return true;
  }

  public int isValid()
  {
    return isValidByDate(new Date());
  }

  public int isValidByDate(Date paramDate)
  {
    Util.log(this.mLogger, "Check Date : " + paramDate.toString());
    Util.log(this.mLogger, "Not After : " + this.mCert.getNotAfter().toString());
    Util.log(this.mLogger, "Not Before : " + this.mCert.getNotBefore().toString());
    try
    {
      this.mCert.checkValidity(paramDate);
      return 0;
    }
    catch (CertificateExpiredException localCertificateExpiredException)
    {
      Util.log(this.mLogger, "Certificate has expired.");
      return 1;
    }
    catch (CertificateNotYetValidException localCertificateNotYetValidException)
    {
      Util.log(this.mLogger, "Certificate isn't valid yet.");
    }
    return -1;
  }

  public boolean setCertificateToCheck(String paramString)
  {
    this.mCert = new CertUtils(this.mLogger).getX509Cert(paramString);
    return this.mCert != null;
  }
}