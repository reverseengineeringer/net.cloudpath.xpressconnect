package org.bouncycastle2.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CRL;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.Certificate;
import java.util.Collection;
import org.bouncycastle2.jce.X509LDAPCertStoreParameters;

public class X509LDAPCertStoreSpi extends CertStoreSpi
{
  private static String LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
  private static String REFERRALS_IGNORE = "ignore";
  private static final String SEARCH_SECURITY_LEVEL = "none";
  private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
  private X509LDAPCertStoreParameters params;

  public X509LDAPCertStoreSpi(CertStoreParameters paramCertStoreParameters)
    throws InvalidAlgorithmParameterException
  {
    super(paramCertStoreParameters);
    if (!(paramCertStoreParameters instanceof X509LDAPCertStoreParameters))
      throw new InvalidAlgorithmParameterException(X509LDAPCertStoreSpi.class.getName() + ": parameter must be a " + X509LDAPCertStoreParameters.class.getName() + " object\n" + paramCertStoreParameters.toString());
    this.params = ((X509LDAPCertStoreParameters)paramCertStoreParameters);
  }

  private String parseDN(String paramString1, String paramString2)
  {
    String str1 = paramString1.substring(paramString1.toLowerCase().indexOf(paramString2.toLowerCase()) + paramString2.length());
    int i = str1.indexOf(',');
    if (i == -1)
      i = str1.length();
    while (true)
    {
      if (str1.charAt(i - 1) != '\\')
      {
        String str2 = str1.substring(0, i);
        String str3 = str2.substring(1 + str2.indexOf('='));
        if (str3.charAt(0) == ' ')
          str3 = str3.substring(1);
        if (str3.startsWith("\""))
          str3 = str3.substring(1);
        if (str3.endsWith("\""))
          str3 = str3.substring(0, -1 + str3.length());
        return str3;
      }
      i = str1.indexOf(',', i + 1);
      if (i == -1)
        i = str1.length();
    }
  }

  public Collection<? extends CRL> engineGetCRLs(CRLSelector paramCRLSelector)
    throws CertStoreException
  {
    return null;
  }

  public Collection<? extends Certificate> engineGetCertificates(CertSelector paramCertSelector)
    throws CertStoreException
  {
    return null;
  }
}