package org.bouncycastle2.x509.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.x509.CertificatePair;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.jce.X509LDAPCertStoreParameters;
import org.bouncycastle2.jce.provider.X509AttrCertParser;
import org.bouncycastle2.jce.provider.X509CRLParser;
import org.bouncycastle2.jce.provider.X509CertPairParser;
import org.bouncycastle2.jce.provider.X509CertParser;
import org.bouncycastle2.util.StoreException;
import org.bouncycastle2.x509.X509AttributeCertStoreSelector;
import org.bouncycastle2.x509.X509AttributeCertificate;
import org.bouncycastle2.x509.X509CRLStoreSelector;
import org.bouncycastle2.x509.X509CertPairStoreSelector;
import org.bouncycastle2.x509.X509CertStoreSelector;
import org.bouncycastle2.x509.X509CertificatePair;

public class LDAPStoreHelper
{
  private static String LDAP_PROVIDER = "com.sun.jndi.ldap.LdapCtxFactory";
  private static String REFERRALS_IGNORE = "ignore";
  private static final String SEARCH_SECURITY_LEVEL = "none";
  private static final String URL_CONTEXT_PREFIX = "com.sun.jndi.url";
  private static int cacheSize = 32;
  private static long lifeTime = 60000L;
  private Map cacheMap = new HashMap(cacheSize);
  private X509LDAPCertStoreParameters params;

  public LDAPStoreHelper(X509LDAPCertStoreParameters paramX509LDAPCertStoreParameters)
  {
    this.params = paramX509LDAPCertStoreParameters;
  }

  private void addToCache(String paramString, List paramList)
  {
    while (true)
    {
      Iterator localIterator;
      long l1;
      Object localObject2;
      try
      {
        Date localDate = new Date(System.currentTimeMillis());
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(localDate);
        localArrayList.add(paramList);
        if (this.cacheMap.containsKey(paramString))
        {
          this.cacheMap.put(paramString, localArrayList);
          return;
        }
        if (this.cacheMap.size() >= cacheSize)
        {
          localIterator = this.cacheMap.entrySet().iterator();
          l1 = localDate.getTime();
          localObject2 = null;
          if (!localIterator.hasNext())
            this.cacheMap.remove(localObject2);
        }
        else
        {
          this.cacheMap.put(paramString, localArrayList);
          continue;
        }
      }
      finally
      {
      }
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      long l2 = ((Date)((List)localEntry.getValue()).get(0)).getTime();
      if (l2 < l1)
      {
        l1 = l2;
        Object localObject3 = localEntry.getKey();
        localObject2 = localObject3;
      }
    }
  }

  private Set createAttributeCertificates(List paramList, X509AttributeCertStoreSelector paramX509AttributeCertStoreSelector)
    throws StoreException
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramList.iterator();
    X509AttrCertParser localX509AttrCertParser = new X509AttrCertParser();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      try
      {
        localX509AttrCertParser.engineInit(new ByteArrayInputStream((byte[])localIterator.next()));
        X509AttributeCertificate localX509AttributeCertificate = (X509AttributeCertificate)localX509AttrCertParser.engineRead();
        if (paramX509AttributeCertStoreSelector.match(localX509AttributeCertificate))
          localHashSet.add(localX509AttributeCertificate);
      }
      catch (StreamParsingException localStreamParsingException)
      {
      }
    }
  }

  private Set createCRLs(List paramList, X509CRLStoreSelector paramX509CRLStoreSelector)
    throws StoreException
  {
    HashSet localHashSet = new HashSet();
    X509CRLParser localX509CRLParser = new X509CRLParser();
    Iterator localIterator = paramList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      try
      {
        localX509CRLParser.engineInit(new ByteArrayInputStream((byte[])localIterator.next()));
        X509CRL localX509CRL = (X509CRL)localX509CRLParser.engineRead();
        if (paramX509CRLStoreSelector.match(localX509CRL))
          localHashSet.add(localX509CRL);
      }
      catch (StreamParsingException localStreamParsingException)
      {
      }
    }
  }

  private Set createCerts(List paramList, X509CertStoreSelector paramX509CertStoreSelector)
    throws StoreException
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramList.iterator();
    X509CertParser localX509CertParser = new X509CertParser();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      try
      {
        localX509CertParser.engineInit(new ByteArrayInputStream((byte[])localIterator.next()));
        X509Certificate localX509Certificate = (X509Certificate)localX509CertParser.engineRead();
        if (paramX509CertStoreSelector.match(localX509Certificate))
          localHashSet.add(localX509Certificate);
      }
      catch (Exception localException)
      {
      }
    }
  }

  private Set createCrossCertificatePairs(List paramList, X509CertPairStoreSelector paramX509CertPairStoreSelector)
    throws StoreException
  {
    HashSet localHashSet = new HashSet();
    for (int i = 0; ; i++)
    {
      if (i >= paramList.size())
        return localHashSet;
      try
      {
        X509CertPairParser localX509CertPairParser = new X509CertPairParser();
        localX509CertPairParser.engineInit(new ByteArrayInputStream((byte[])paramList.get(i)));
        localX509CertificatePair = (X509CertificatePair)localX509CertPairParser.engineRead();
        if (paramX509CertPairStoreSelector.match(localX509CertificatePair))
          localHashSet.add(localX509CertificatePair);
      }
      catch (StreamParsingException localStreamParsingException)
      {
        while (true)
        {
          byte[] arrayOfByte1 = (byte[])paramList.get(i);
          byte[] arrayOfByte2 = (byte[])paramList.get(i + 1);
          X509CertificatePair localX509CertificatePair = new X509CertificatePair(new CertificatePair(X509CertificateStructure.getInstance(new ASN1InputStream(arrayOfByte1).readObject()), X509CertificateStructure.getInstance(new ASN1InputStream(arrayOfByte2).readObject())));
          i++;
        }
      }
      catch (IOException localIOException)
      {
      }
      catch (CertificateParsingException localCertificateParsingException)
      {
      }
    }
  }

  private X500Principal getCertificateIssuer(X509Certificate paramX509Certificate)
  {
    return paramX509Certificate.getIssuerX500Principal();
  }

  private List getFromCache(String paramString)
  {
    List localList = (List)this.cacheMap.get(paramString);
    long l = System.currentTimeMillis();
    if (localList != null)
    {
      if (((Date)localList.get(0)).getTime() < l - lifeTime)
        return null;
      return (List)localList.get(1);
    }
    return null;
  }

  private String getSubjectAsString(X509CertStoreSelector paramX509CertStoreSelector)
  {
    try
    {
      byte[] arrayOfByte = paramX509CertStoreSelector.getSubjectAsBytes();
      if (arrayOfByte != null)
      {
        String str = new X500Principal(arrayOfByte).getName("RFC1779");
        return str;
      }
    }
    catch (IOException localIOException)
    {
      throw new StoreException("exception processing name: " + localIOException.getMessage(), localIOException);
    }
    return null;
  }

  private String parseDN(String paramString1, String paramString2)
  {
    int i = paramString1.toLowerCase().indexOf(paramString2.toLowerCase() + "=");
    if (i == -1)
      return "";
    String str1 = paramString1.substring(i + paramString2.length());
    int j = str1.indexOf(',');
    if (j == -1)
      j = str1.length();
    while (true)
    {
      if (str1.charAt(j - 1) != '\\')
      {
        String str2 = str1.substring(0, j);
        String str3 = str2.substring(1 + str2.indexOf('='));
        if (str3.charAt(0) == ' ')
          str3 = str3.substring(1);
        if (str3.startsWith("\""))
          str3 = str3.substring(1);
        if (str3.endsWith("\""))
          str3 = str3.substring(0, -1 + str3.length());
        return str3;
      }
      j = str1.indexOf(',', j + 1);
      if (j == -1)
        j = str1.length();
    }
  }

  private String[] splitString(String paramString)
  {
    return paramString.split("\\s+");
  }
}