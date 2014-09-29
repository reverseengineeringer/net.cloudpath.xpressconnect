package net.cloudpath.xpressconnect.certificates;

import java.util.ArrayList;
import java.util.Iterator;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class PEMValidator
{
  private String mCertificate = null;
  private ArrayList<String[]> mCerts = null;
  private int mExpectedCount = -1;
  private Logger mLogger = null;
  private boolean mTestingCsr = false;
  private String pemFooter = "-----END CERTIFICATE-----";
  private String pemHeader = "-----BEGIN CERTIFICATE-----";

  public PEMValidator(Logger paramLogger, String paramString)
  {
    this.mCertificate = paramString;
    this.mLogger = paramLogger;
    if (paramString == null)
      Util.log(this.mLogger, "Certificate passed to PEMValidator is null.");
  }

  private boolean canParseCert(String paramString)
  {
    boolean bool = true;
    if (paramString == null)
    {
      Util.log(this.mLogger, "Certificate data is NULL.");
      return false;
    }
    if (!certHasHeaderFooter(paramString))
    {
      Util.log(this.mLogger, "Certificate appears to be missing the header or footer.");
      bool = false;
    }
    if ((!this.mTestingCsr) && (!certCanBeLoaded(paramString)))
    {
      Util.log(this.mLogger, "Certificate can't be loaded by BC.");
      bool = false;
    }
    if (!certLinesValid(paramString))
    {
      Util.log(this.mLogger, "Certificate lines aren't valid.");
      bool = false;
    }
    if (!bool)
    {
      Util.log(this.mLogger, "Cert : " + paramString);
      Util.log(this.mLogger, Util.dumpByteValues(paramString));
    }
    return bool;
  }

  private boolean convertedCertsAreValid(String[] paramArrayOfString)
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
      if (!canParseCert(paramArrayOfString[i]))
      {
        Util.log(this.mLogger, "Failed to parse certificate #" + i + " after reformatting.");
        return false;
      }
    return true;
  }

  private boolean fixSingleLineLayout(String paramString)
  {
    if (Util.stringIsEmpty(paramString))
      Util.log(this.mLogger, "cert is empty in fixSingleLineLayout.");
    String[] arrayOfString2;
    do
    {
      return false;
      String[] arrayOfString1 = splitCertsFromSingleLine(paramString);
      if (arrayOfString1 == null)
      {
        Util.log(this.mLogger, "Split certificates resulted in an empty set.");
        return false;
      }
      arrayOfString2 = formatCertsFromArray(arrayOfString1);
      if ((arrayOfString2 == null) || (arrayOfString2.length <= 0))
      {
        Util.log(this.mLogger, "formattedCerts was empty.");
        return false;
      }
    }
    while ((!convertedCertsAreValid(arrayOfString2)) || (!repopulateMemberVariables(arrayOfString2)));
    return true;
  }

  private String[] formatCertsFromArray(String[] paramArrayOfString)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramArrayOfString.length; i++)
      localArrayList.add(formatIndividualCert(paramArrayOfString[i]));
    if (localArrayList.size() <= 0)
    {
      Util.log(this.mLogger, "formattedCerts ended up null.");
      return null;
    }
    return toStringArray(localArrayList.toArray());
  }

  private String formatIndividualCert(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    StringBuilder localStringBuilder1 = new StringBuilder();
    if (paramString == null)
    {
      Util.log(this.mLogger, "No valid string passed to formatIndividualCert()!");
      return null;
    }
    if (!paramString.startsWith(this.pemHeader))
    {
      Util.log(this.mLogger, "Single line cert didn't have a valid header!  Cert : " + paramString);
      return null;
    }
    if (paramString.endsWith("\n"))
      paramString = paramString.substring(0, -1 + paramString.length());
    if (!paramString.endsWith(this.pemFooter))
    {
      Util.log(this.mLogger, "Single line cert didn't have a valid footer!  Cert : " + paramString);
      return null;
    }
    localArrayList.add(this.pemHeader);
    for (int j = this.pemHeader.length(); j < paramString.length() - this.pemFooter.length(); j++)
    {
      i++;
      localStringBuilder1.append(paramString.charAt(j));
      if (i >= 64)
      {
        localArrayList.add(localStringBuilder1.toString());
        localStringBuilder1 = new StringBuilder();
        i = 0;
      }
    }
    if (localStringBuilder1.toString().length() > 0)
      localArrayList.add(localStringBuilder1.toString());
    localArrayList.add(this.pemFooter);
    StringBuilder localStringBuilder2 = new StringBuilder();
    for (int k = 0; k < localArrayList.size(); k++)
      localStringBuilder2.append((String)localArrayList.get(k) + "\n");
    return localStringBuilder2.toString();
  }

  private boolean repopulateMemberVariables(String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramArrayOfString.length; i++)
      localStringBuilder.append(paramArrayOfString[i] + "\n");
    this.mCertificate = localStringBuilder.toString();
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < paramArrayOfString.length; j++)
      localArrayList.add(paramArrayOfString[j].split("\n"));
    this.mCerts = localArrayList;
    return true;
  }

  private boolean splitCaCerts()
  {
    int i = 0;
    ArrayList localArrayList = null;
    if (Util.stringIsEmpty(this.mCertificate) == true)
    {
      Util.log(this.mLogger, "Target certificate is null.  Defaulting to true.");
      return true;
    }
    this.mCerts = new ArrayList();
    String[] arrayOfString = this.mCertificate.split("\n");
    int j = 0;
    label50: if (j < arrayOfString.length)
    {
      arrayOfString[j] = arrayOfString[j].replace("\r", "");
      if (!arrayOfString[j].contentEquals(this.pemHeader))
        break label125;
      if (i == 1)
        Util.log(this.mLogger, "Got multiple headers while parsing data!?");
      i = 1;
      localArrayList = new ArrayList();
      localArrayList.add(arrayOfString[j]);
    }
    while (true)
    {
      j++;
      break label50;
      break;
      label125: if (arrayOfString[j].contentEquals(this.pemFooter))
      {
        if (i == 0)
          Util.log(this.mLogger, "Got multiple footers while parsing data!?");
        if (localArrayList != null)
        {
          localArrayList.add(arrayOfString[j]);
          this.mCerts.add(toStringArray(localArrayList.toArray()));
        }
        localArrayList = null;
        i = 0;
      }
      else
      {
        if ((arrayOfString[j].contains(this.pemHeader)) && (arrayOfString[j].contains(this.pemFooter)))
        {
          Util.log(this.mLogger, "Cert is on a single line.  Declairing invalid.");
          Util.log(this.mLogger, "Data : ");
          Util.log(this.mLogger, Util.dumpByteValues(arrayOfString[j]));
          return false;
        }
        if (i == 1)
          localArrayList.add(arrayOfString[j]);
      }
    }
  }

  private String[] splitCertsFromSingleLine(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int j = 0;
    while (i == 0)
    {
      j = paramString.indexOf(this.pemHeader, j);
      int k = paramString.indexOf(this.pemFooter, j);
      if ((k <= j) || (j < 0) || (k < 0))
      {
        if ((j == -1) && (k > 0));
        while (true)
        {
          i = 1;
          break;
          Util.log(this.mLogger, "Invalid certificate data found.  Discarding.");
        }
      }
      int m = k + this.pemFooter.length();
      localArrayList.add(paramString.substring(j, m));
      j = m;
      if (j >= this.mCertificate.length())
        i = 1;
    }
    if (localArrayList.size() <= 0)
    {
      Util.log(this.mLogger, "Split certificates ended up null.");
      return null;
    }
    return toStringArray(localArrayList.toArray());
  }

  public static String stripSingleCert(String paramString)
  {
    return paramString.substring(paramString.indexOf("-----BEGIN CERTIFICATE-----"), paramString.indexOf("-----END CERTIFICATE-----") + "-----END CERTIFICATE-----".length());
  }

  private String[] toStringArray(Object[] paramArrayOfObject)
  {
    String[] arrayOfString = new String[paramArrayOfObject.length];
    for (int i = 0; i < paramArrayOfObject.length; i++)
      arrayOfString[i] = ((String)paramArrayOfObject[i]);
    return arrayOfString;
  }

  public boolean attemptToFix()
  {
    if (this.mCertificate == null)
    {
      Util.log(this.mLogger, "mCertificate is null in attemptToFix.");
      return false;
    }
    String[] arrayOfString = this.mCertificate.split("\n");
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < arrayOfString.length; i++)
      if (!Util.stringIsEmpty(arrayOfString[i]))
        localStringBuilder.append(arrayOfString[i]);
    return fixSingleLineLayout(localStringBuilder.toString());
  }

  public boolean base64validCharacters(String paramString)
  {
    boolean bool = true;
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "Line is empty in base64validCharacters.");
      return false;
    }
    for (int i = 0; i < paramString.length(); i++)
      if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".indexOf(paramString.charAt(i)) < 0)
      {
        Util.log(this.mLogger, "Invalid character '" + paramString.charAt(i) + "' in line!");
        bool = false;
      }
    return bool;
  }

  public boolean certCanBeLoaded(String paramString)
  {
    if (Util.stringIsEmpty(paramString))
      Util.log(this.mLogger, "Cert data was empty in certCanBeLoaded.");
    while (new CertUtils(this.mLogger).getX509Cert(paramString) == null)
      return false;
    return true;
  }

  public boolean certHasHeaderFooter(String paramString)
  {
    boolean bool = true;
    if (paramString == null)
    {
      Util.log(this.mLogger, "cert value was null in certHasHeaderFooter.");
      return false;
    }
    if (!paramString.startsWith(this.pemHeader))
    {
      Util.log(this.mLogger, "Cert appears to be missing header!");
      bool = false;
    }
    if ((!paramString.endsWith(this.pemFooter + "\n")) && (!paramString.endsWith(this.pemFooter)))
    {
      Util.log(this.mLogger, "Cert appears to be missing footer!");
      bool = false;
    }
    return bool;
  }

  public boolean certLinesValid(String paramString)
  {
    boolean bool = true;
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "Line is empty!");
      return false;
    }
    String[] arrayOfString = paramString.split("\n");
    for (int i = 0; i < arrayOfString.length; i++)
      if (!lineIsValid(arrayOfString[i]))
      {
        bool = false;
        Util.log(this.mLogger, "Base 64 line is invalid.");
        Util.log(this.mLogger, "Line : " + arrayOfString[i]);
      }
    return bool;
  }

  public byte[] getAsBytes()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = getCertData().iterator();
    while (localIterator.hasNext())
    {
      String[] arrayOfString = (String[])localIterator.next();
      for (int i = 0; i < arrayOfString.length; i++)
        localStringBuilder.append(arrayOfString[i] + "\n");
    }
    return localStringBuilder.toString().getBytes();
  }

  public ArrayList<String[]> getCertData()
  {
    return this.mCerts;
  }

  public boolean isValid()
  {
    boolean bool = true;
    if (!splitCaCerts())
      return false;
    if (this.mCerts == null)
    {
      Util.log(this.mLogger, "mCerts was null in isValid().");
      Util.log(this.mLogger, Util.getStackTrace(new Exception()));
      return false;
    }
    if (this.mCerts.size() <= 0)
    {
      Util.log(this.mLogger, "mCerts was empty in isValid().");
      Util.log(this.mLogger, Util.getStackTrace(new Exception()));
      return false;
    }
    if ((this.mExpectedCount != -1) && (this.mCerts.size() != this.mExpectedCount))
    {
      Util.log(this.mLogger, "Expected number of certs is wrong.  Expected : " + this.mExpectedCount + "   Got : " + this.mCerts.size());
      return false;
    }
    for (int i = 0; i < this.mCerts.size(); i++)
    {
      String[] arrayOfString = (String[])this.mCerts.get(i);
      StringBuilder localStringBuilder = new StringBuilder();
      for (int j = 0; j < arrayOfString.length; j++)
        localStringBuilder.append(arrayOfString[j] + "\n");
      if (!canParseCert(localStringBuilder.toString()))
        bool = false;
    }
    return bool;
  }

  public boolean lineIsValid(String paramString)
  {
    boolean bool1 = true;
    boolean bool2 = true;
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "Stubby line in the base64 data.");
      bool1 = false;
    }
    while ((paramString.contentEquals(this.pemHeader)) || (paramString.contentEquals(this.pemFooter)))
      return bool1;
    if (paramString.length() > 64)
    {
      Util.log(this.mLogger, "Base64 line is invalid length.  Expected 64, got " + paramString.length());
      bool2 = false;
    }
    if ((paramString.length() < 64) && (paramString.length() % 4 != 0))
    {
      Util.log(this.mLogger, "Short line isn't a multiple of 4.");
      bool2 = false;
    }
    if (!base64validCharacters(paramString))
      bool2 = false;
    return bool2;
  }

  public void setExpectedCerts(int paramInt)
  {
    this.mExpectedCount = paramInt;
  }

  public void setPemIsCsr()
  {
    this.mTestingCsr = true;
    this.pemHeader = "-----BEGIN CERTIFICATE REQUEST-----";
    this.pemFooter = "-----END CERTIFICATE REQUEST-----";
  }
}