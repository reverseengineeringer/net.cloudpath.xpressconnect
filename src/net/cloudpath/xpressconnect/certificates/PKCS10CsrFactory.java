package net.cloudpath.xpressconnect.certificates;

import java.security.KeyPair;
import javax.security.auth.x500.X500Principal;
import net.cloudpath.xpressconnect.logger.Logger;
import org.bouncycastle2.jce.PKCS10CertificationRequest;

public class PKCS10CsrFactory
{
  PKCS10CertificationRequest bcpk10 = null;
  byte[] derCert = null;
  Logger mLogger = null;

  public PKCS10CsrFactory(String paramString, X500Principal paramX500Principal, KeyPair paramKeyPair, Logger paramLogger)
    throws Exception
  {
    this.mLogger = paramLogger;
    buildDerCert(paramString, paramX500Principal, paramKeyPair);
  }

  private void buildDerCert(String paramString, X500Principal paramX500Principal, KeyPair paramKeyPair)
    throws Exception
  {
    this.bcpk10 = new PKCS10CertificationRequest("SHA1withRSA", paramX500Principal, paramKeyPair.getPublic(), null, paramKeyPair.getPrivate());
    this.derCert = ((byte[])this.bcpk10.getEncoded());
  }

  public byte[] getDERencoded()
  {
    return this.derCert;
  }
}