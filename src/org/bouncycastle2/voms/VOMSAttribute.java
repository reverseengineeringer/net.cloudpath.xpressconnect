package org.bouncycastle2.voms;

import java.util.List;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.GeneralNames;
import org.bouncycastle2.asn1.x509.IetfAttrSyntax;
import org.bouncycastle2.x509.X509Attribute;
import org.bouncycastle2.x509.X509AttributeCertificate;

public class VOMSAttribute
{
  public static final String VOMS_ATTR_OID = "1.3.6.1.4.1.8005.100.100.4";
  private X509AttributeCertificate myAC;
  private Vector myFQANs = new Vector();
  private String myHostPort;
  private Vector myStringList = new Vector();
  private String myVo;

  public VOMSAttribute(X509AttributeCertificate paramX509AttributeCertificate)
  {
    if (paramX509AttributeCertificate == null)
      throw new IllegalArgumentException("VOMSAttribute: AttributeCertificate is NULL");
    this.myAC = paramX509AttributeCertificate;
    X509Attribute[] arrayOfX509Attribute = paramX509AttributeCertificate.getAttributes("1.3.6.1.4.1.8005.100.100.4");
    if (arrayOfX509Attribute == null);
    int i;
    IetfAttrSyntax localIetfAttrSyntax;
    while (true)
    {
      return;
      i = 0;
      try
      {
        if (i != arrayOfX509Attribute.length)
        {
          localIetfAttrSyntax = new IetfAttrSyntax((ASN1Sequence)arrayOfX509Attribute[i].getValues()[0]);
          str1 = ((DERIA5String)GeneralName.getInstance(((ASN1Sequence)localIetfAttrSyntax.getPolicyAuthority().getDERObject()).getObjectAt(0)).getName()).getString();
          j = str1.indexOf("://");
          if ((j < 0) || (j == -1 + str1.length()))
            throw new IllegalArgumentException("Bad encoding of VOMS policyAuthority : [" + str1 + "]");
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        String str1;
        int j;
        throw localIllegalArgumentException;
        this.myVo = str1.substring(0, j);
        this.myHostPort = str1.substring(j + 3);
        if (localIetfAttrSyntax.getValueType() != 1)
          throw new IllegalArgumentException("VOMS attribute values are not encoded as octet strings, policyAuthority = " + str1);
      }
      catch (Exception localException)
      {
        throw new IllegalArgumentException("Badly encoded VOMS extension in AC issued by " + paramX509AttributeCertificate.getIssuer());
      }
    }
    ASN1OctetString[] arrayOfASN1OctetString = (ASN1OctetString[])localIetfAttrSyntax.getValues();
    for (int k = 0; ; k++)
    {
      if (k == arrayOfASN1OctetString.length)
      {
        i++;
        break;
      }
      String str2 = new String(arrayOfASN1OctetString[k].getOctets());
      FQAN localFQAN = new FQAN(str2);
      if ((!this.myStringList.contains(str2)) && (str2.startsWith("/" + this.myVo + "/")))
      {
        this.myStringList.add(str2);
        this.myFQANs.add(localFQAN);
      }
    }
  }

  public X509AttributeCertificate getAC()
  {
    return this.myAC;
  }

  public List getFullyQualifiedAttributes()
  {
    return this.myStringList;
  }

  public String getHostPort()
  {
    return this.myHostPort;
  }

  public List getListOfFQAN()
  {
    return this.myFQANs;
  }

  public String getVO()
  {
    return this.myVo;
  }

  public String toString()
  {
    return "VO      :" + this.myVo + "\n" + "HostPort:" + this.myHostPort + "\n" + "FQANs   :" + this.myFQANs;
  }

  public class FQAN
  {
    String capability;
    String fqan;
    String group;
    String role;

    public FQAN(String arg2)
    {
      Object localObject;
      this.fqan = localObject;
    }

    public FQAN(String paramString1, String paramString2, String arg4)
    {
      this.group = paramString1;
      this.role = paramString2;
      Object localObject;
      this.capability = localObject;
    }

    public String getCapability()
    {
      if ((this.group == null) && (this.fqan != null))
        split();
      return this.capability;
    }

    public String getFQAN()
    {
      if (this.fqan != null)
        return this.fqan;
      StringBuilder localStringBuilder1 = new StringBuilder(String.valueOf(this.group)).append("/Role=");
      String str1;
      StringBuilder localStringBuilder2;
      if (this.role != null)
      {
        str1 = this.role;
        localStringBuilder2 = localStringBuilder1.append(str1);
        if (this.capability == null)
          break label102;
      }
      label102: for (String str2 = "/Capability=" + this.capability; ; str2 = "")
      {
        this.fqan = str2;
        return this.fqan;
        str1 = "";
        break;
      }
    }

    public String getGroup()
    {
      if ((this.group == null) && (this.fqan != null))
        split();
      return this.group;
    }

    public String getRole()
    {
      if ((this.group == null) && (this.fqan != null))
        split();
      return this.role;
    }

    protected void split()
    {
      this.fqan.length();
      int i = this.fqan.indexOf("/Role=");
      if (i < 0)
        return;
      this.group = this.fqan.substring(0, i);
      int j = this.fqan.indexOf("/Capability=", i + 6);
      String str1;
      if (j < 0)
      {
        str1 = this.fqan.substring(i + 6);
        if (str1.length() == 0)
          str1 = null;
        this.role = str1;
        if (j >= 0)
          break label131;
      }
      label131: for (String str2 = null; ; str2 = this.fqan.substring(j + 12))
      {
        if ((str2 == null) || (str2.length() == 0))
          str2 = null;
        this.capability = str2;
        return;
        str1 = this.fqan.substring(i + 6, j);
        break;
      }
    }

    public String toString()
    {
      return getFQAN();
    }
  }
}