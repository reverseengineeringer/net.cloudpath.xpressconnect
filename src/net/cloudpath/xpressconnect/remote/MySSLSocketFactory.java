package net.cloudpath.xpressconnect.remote;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import net.cloudpath.xpressconnect.logger.Logger;

public class MySSLSocketFactory extends org.apache.http.conn.ssl.SSLSocketFactory
{
  private SSLContext sslContext = SSLContext.getInstance("TLS");

  public MySSLSocketFactory(Logger paramLogger, KeyStore paramKeyStore, boolean paramBoolean)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
  {
    super(null);
    SSLContext localSSLContext = this.sslContext;
    TrustManager[] arrayOfTrustManager = new TrustManager[1];
    arrayOfTrustManager[0] = new HybridTrustManager(paramLogger, paramKeyStore, paramBoolean);
    localSSLContext.init(null, arrayOfTrustManager, new SecureRandom());
  }

  public Socket createSocket()
    throws IOException
  {
    return this.sslContext.getSocketFactory().createSocket();
  }

  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException, UnknownHostException
  {
    return this.sslContext.getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
  }
}