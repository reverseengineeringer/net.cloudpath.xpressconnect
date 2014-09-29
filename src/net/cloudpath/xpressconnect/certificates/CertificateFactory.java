package net.cloudpath.xpressconnect.certificates;

import android.annotation.SuppressLint;
import android.util.Log;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import javax.security.auth.x500.X500Principal;
import jcifs.util.Base64;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class CertificateFactory
{
  private KeyPair mKeyPair = null;
  private Logger mLogger = null;
  private X500Principal mSubject = null;
  private PKCS10CsrFactory pkcsRequest = null;

  public CertificateFactory(X500Principal paramX500Principal, Logger paramLogger)
  {
    this.mSubject = paramX500Principal;
    this.mLogger = paramLogger;
    PRNGFixes.apply();
  }

  @SuppressLint({"TrulyRandom"})
  public void generateCSR()
    throws Exception
  {
    Util.log(this.mLogger, "Generating key pair.");
    this.mKeyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    Util.log(this.mLogger, "Building CSR.");
    this.pkcsRequest = new PKCS10CsrFactory("SHA1withRSA", this.mSubject, this.mKeyPair, this.mLogger);
  }

  public byte[] getCsrDERencoded()
  {
    if (this.pkcsRequest == null)
      return null;
    return this.pkcsRequest.getDERencoded();
  }

  public String getCsrPEMencoded()
  {
    if (this.pkcsRequest == null)
      return "";
    String str1 = "-----BEGIN CERTIFICATE REQUEST-----\n";
    String str2 = Base64.encode(this.pkcsRequest.getDERencoded());
    Log.d("XPC", "sdata length = " + str2.length() + "  div 64 = " + str2.length() / 64);
    for (int i = 0; i < str2.length() / 64; i++)
    {
      String str5 = str2.substring(i * 64, 64 + i * 64);
      Log.d("XPC", str5);
      String str6 = str1 + str5;
      str1 = str6 + "\n";
    }
    String str3 = str2.substring(64 * (str2.length() / 64));
    Log.d("XPC", str3);
    String str4 = str1 + str3;
    return str4 + "\n-----END CERTIFICATE REQUEST-----\n";
  }

  public String getCsrPEMencodedForHttpGet()
  {
    return getCsrPEMencoded().replace("+", "%2B").replace(" ", "+").replace("\n", "%0A");
  }

  public PrivateKey getPrivateKey()
  {
    if (this.mKeyPair == null)
      return null;
    return this.mKeyPair.getPrivate();
  }

  public byte[] getPrivateKeyDERencoded()
    throws InvalidKeyException, NoSuchAlgorithmException
  {
    if ((this.mKeyPair == null) || (this.mKeyPair.getPrivate() == null))
      return null;
    return this.mKeyPair.getPrivate().getEncoded();
  }
}