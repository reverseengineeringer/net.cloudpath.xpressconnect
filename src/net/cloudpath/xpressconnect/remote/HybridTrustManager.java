package net.cloudpath.xpressconnect.remote;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class HybridTrustManager
  implements X509TrustManager
{
  private boolean mIgnoreCerts;
  private Logger mLogger;
  private X509TrustManager mSecondaryTm = null;
  private X509TrustManager x509Tm;

  public HybridTrustManager(Logger paramLogger, KeyStore paramKeyStore, boolean paramBoolean)
    throws NoSuchAlgorithmException, KeyStoreException
  {
    this.mLogger = paramLogger;
    this.mIgnoreCerts = paramBoolean;
    this.x509Tm = getTrustManager(null);
    if (paramKeyStore != null)
      this.mSecondaryTm = getTrustManager(paramKeyStore);
  }

  private X509TrustManager getTrustManager(KeyStore paramKeyStore)
    throws KeyStoreException, NoSuchAlgorithmException
  {
    TrustManagerFactory localTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    localTrustManagerFactory.init(paramKeyStore);
    for (TrustManager localTrustManager : localTrustManagerFactory.getTrustManagers())
      if ((localTrustManager instanceof X509TrustManager))
        return (X509TrustManager)localTrustManager;
    return null;
  }

  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
    this.x509Tm.checkClientTrusted(paramArrayOfX509Certificate, paramString);
  }

  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
    int i = 1;
    if (this.x509Tm == null)
    {
      Util.log(this.mLogger, "No default trust manager available to validate the server certificate!");
      throw new CertificateException("No default trust manager available to validate the server certificate!");
    }
    if (this.mSecondaryTm != null)
      try
      {
        this.mSecondaryTm.checkServerTrusted(paramArrayOfX509Certificate, paramString);
        if (i == 1)
          return;
      }
      catch (CertificateException localCertificateException2)
      {
        while (true)
        {
          Util.log(this.mLogger, "Failed to validate against secondary chains : " + localCertificateException2.getMessage());
          i = 0;
        }
      }
    try
    {
      this.x509Tm.checkServerTrusted(paramArrayOfX509Certificate, paramString);
      return;
    }
    catch (CertificateException localCertificateException1)
    {
      if (this.mIgnoreCerts)
      {
        Util.log(this.mLogger, "WARNING : Server cert failed validation, but has been ignored.");
        return;
      }
      Util.log(this.mLogger, "WARNING : Server cert failed validation.");
      throw localCertificateException1;
    }
  }

  public X509Certificate[] getAcceptedIssuers()
  {
    if (this.mIgnoreCerts)
      return new X509Certificate[0];
    if (this.mSecondaryTm != null)
    {
      int i = this.x509Tm.getAcceptedIssuers().length;
      int j = this.mSecondaryTm.getAcceptedIssuers().length;
      X509Certificate[] arrayOfX509Certificate = new X509Certificate[i + j];
      System.arraycopy(this.x509Tm.getAcceptedIssuers(), 0, arrayOfX509Certificate, 0, i);
      System.arraycopy(this.mSecondaryTm, 0, arrayOfX509Certificate, i, j);
      return arrayOfX509Certificate;
    }
    return this.x509Tm.getAcceptedIssuers();
  }
}