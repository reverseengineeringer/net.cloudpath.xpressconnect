package net.cloudpath.xpressconnect.certificates;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class SortCertChains
{
  private ArrayList<X509Certificate> mCertificates = new ArrayList();
  private ArrayList<ArrayList<X509Certificate>> mCerts = new ArrayList();
  private Logger mLogger = null;

  public SortCertChains(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  private void findChainCerts(ArrayList<X509Certificate> paramArrayList)
  {
    int i = -1 + paramArrayList.size();
    if (i >= 0)
      for (int j = 0; ; j++)
        if (j < this.mCerts.size())
        {
          X509Certificate localX509Certificate1 = (X509Certificate)paramArrayList.get(i);
          ArrayList localArrayList = (ArrayList)this.mCerts.get(j);
          X509Certificate localX509Certificate2 = (X509Certificate)localArrayList.get(-1 + localArrayList.size());
          if (localX509Certificate1.getIssuerDN().equals(localX509Certificate2.getSubjectDN()))
          {
            localArrayList.add(localX509Certificate1);
            paramArrayList.remove(i);
          }
        }
        else
        {
          i--;
          break;
        }
  }

  private void findRoots(ArrayList<X509Certificate> paramArrayList)
  {
    CertUtils localCertUtils = new CertUtils(this.mLogger);
    for (int i = -1 + paramArrayList.size(); i >= 0; i--)
      if (localCertUtils.certIsRoot((Certificate)paramArrayList.get(i)))
      {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(paramArrayList.get(i));
        this.mCerts.add(localArrayList);
        paramArrayList.remove(i);
      }
  }

  private void logChainData()
  {
    Util.log(this.mLogger, "There were " + this.mCerts.size() + " chain(s) found.");
    for (int i = 0; i < this.mCerts.size(); i++)
    {
      ArrayList localArrayList = (ArrayList)this.mCerts.get(i);
      Util.log(this.mLogger, "Chain " + i + " has " + localArrayList.size() + " cert(s) in it.");
      for (int j = 0; j < localArrayList.size(); j++)
      {
        X509Certificate localX509Certificate = (X509Certificate)localArrayList.get(j);
        Util.log(this.mLogger, "    Cert " + j + " : ");
        Util.log(this.mLogger, "         Issuer : " + localX509Certificate.getIssuerDN().getName());
        Util.log(this.mLogger, "         Subject : " + localX509Certificate.getSubjectDN().getName());
      }
    }
  }

  public boolean addCert(String paramString)
  {
    if (Util.stringIsEmpty(paramString));
    X509Certificate localX509Certificate;
    do
    {
      return false;
      localX509Certificate = new CertUtils(this.mLogger).getX509Cert(paramString);
    }
    while (localX509Certificate == null);
    this.mCertificates.add(localX509Certificate);
    return true;
  }

  public int chainForUserCert(String paramString)
  {
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "No user certificate passed in to SortCertChains.");
      return -1;
    }
    X509Certificate localX509Certificate1 = new CertUtils(this.mLogger).getX509Cert(paramString);
    if (localX509Certificate1 == null)
    {
      Util.log(this.mLogger, "Unable to generate X509Certificate structure from PEM data in chainForUserCert()!");
      return -1;
    }
    for (int i = 0; i < this.mCerts.size(); i++)
    {
      ArrayList localArrayList = (ArrayList)this.mCerts.get(i);
      X509Certificate localX509Certificate2 = (X509Certificate)localArrayList.get(-1 + localArrayList.size());
      if (localX509Certificate1.getIssuerDN().equals(localX509Certificate2.getSubjectDN()))
      {
        Util.log(this.mLogger, "User cert chained in chain " + i + ".");
        return i;
      }
    }
    return -1;
  }

  public void clear()
  {
    this.mCertificates.clear();
    this.mCerts.clear();
  }

  public boolean findChains()
  {
    int i = -1;
    new ArrayList();
    ArrayList localArrayList = (ArrayList)this.mCertificates.clone();
    findRoots(localArrayList);
    if (this.mCerts.size() <= 0)
      Util.log(this.mLogger, "No root CAs found in the list.  Won't build chains.");
    do
    {
      return false;
      while ((i != localArrayList.size()) && (localArrayList.size() > 0))
      {
        i = localArrayList.size();
        findChainCerts(localArrayList);
      }
      logChainData();
    }
    while (localArrayList.size() > 0);
    return true;
  }

  public ArrayList<X509Certificate> getChain(int paramInt)
  {
    return (ArrayList)this.mCerts.get(paramInt);
  }

  public int size()
  {
    return this.mCerts.size();
  }
}