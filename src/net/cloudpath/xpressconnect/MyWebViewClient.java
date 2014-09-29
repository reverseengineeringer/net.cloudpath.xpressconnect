package net.cloudpath.xpressconnect;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MyWebViewClient extends WebViewClient
{
  private LoadConfigCallback mCallback;

  public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError)
  {
    System.out.println("+_+_+_+_+_+_+_+_+_+_ SSL error.");
    paramSslErrorHandler.proceed();
  }

  public void setConfigCallback(LoadConfigCallback paramLoadConfigCallback)
  {
    this.mCallback = paramLoadConfigCallback;
  }

  @SuppressLint({"DefaultLocale"})
  public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
  {
    System.out.println("URL : " + paramString);
    try
    {
      URL localURL = new URL(paramString);
      System.out.println("protocol = " + localURL.getProtocol());
      System.out.println("authority = " + localURL.getAuthority());
      System.out.println("host = " + localURL.getHost());
      System.out.println("port = " + localURL.getPort());
      System.out.println("path = " + localURL.getPath());
      System.out.println("query = " + localURL.getQuery());
      System.out.println("filename = " + localURL.getFile());
      System.out.println("ref = " + localURL.getRef());
      if (localURL.getPath().toLowerCase().endsWith("android.netconfig"))
      {
        System.out.println("!!!!! Load config!");
        if (this.mCallback != null)
          this.mCallback.loadFromUrl(paramString);
        return true;
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      localMalformedURLException.printStackTrace();
      return false;
    }
    paramWebView.loadUrl(paramString);
    return true;
  }
}