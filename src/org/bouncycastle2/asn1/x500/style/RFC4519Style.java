package org.bouncycastle2.asn1.x500.style;

import java.io.IOException;
import java.util.Hashtable;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERUTF8String;
import org.bouncycastle2.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle2.asn1.x500.RDN;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x500.X500NameStyle;

public class RFC4519Style
  implements X500NameStyle
{
  private static final Hashtable DefaultLookUp;
  private static final Hashtable DefaultSymbols;
  public static final X500NameStyle INSTANCE = new RFC4519Style();
  public static final ASN1ObjectIdentifier businessCategory = new ASN1ObjectIdentifier("2.5.4.15");
  public static final ASN1ObjectIdentifier c = new ASN1ObjectIdentifier("2.5.4.6");
  public static final ASN1ObjectIdentifier cn = new ASN1ObjectIdentifier("2.5.4.3");
  public static final ASN1ObjectIdentifier dc = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
  public static final ASN1ObjectIdentifier description = new ASN1ObjectIdentifier("2.5.4.13");
  public static final ASN1ObjectIdentifier destinationIndicator = new ASN1ObjectIdentifier("2.5.4.27");
  public static final ASN1ObjectIdentifier distinguishedName = new ASN1ObjectIdentifier("2.5.4.49");
  public static final ASN1ObjectIdentifier dnQualifier = new ASN1ObjectIdentifier("2.5.4.46");
  public static final ASN1ObjectIdentifier enhancedSearchGuide = new ASN1ObjectIdentifier("2.5.4.47");
  public static final ASN1ObjectIdentifier facsimileTelephoneNumber = new ASN1ObjectIdentifier("2.5.4.23");
  public static final ASN1ObjectIdentifier generationQualifier = new ASN1ObjectIdentifier("2.5.4.44");
  public static final ASN1ObjectIdentifier givenName = new ASN1ObjectIdentifier("2.5.4.42");
  public static final ASN1ObjectIdentifier houseIdentifier = new ASN1ObjectIdentifier("2.5.4.51");
  public static final ASN1ObjectIdentifier initials = new ASN1ObjectIdentifier("2.5.4.43");
  public static final ASN1ObjectIdentifier internationalISDNNumber = new ASN1ObjectIdentifier("2.5.4.25");
  public static final ASN1ObjectIdentifier l = new ASN1ObjectIdentifier("2.5.4.7");
  public static final ASN1ObjectIdentifier member = new ASN1ObjectIdentifier("2.5.4.31");
  public static final ASN1ObjectIdentifier name = new ASN1ObjectIdentifier("2.5.4.41");
  public static final ASN1ObjectIdentifier o = new ASN1ObjectIdentifier("2.5.4.10");
  public static final ASN1ObjectIdentifier ou = new ASN1ObjectIdentifier("2.5.4.11");
  public static final ASN1ObjectIdentifier owner = new ASN1ObjectIdentifier("2.5.4.32");
  public static final ASN1ObjectIdentifier physicalDeliveryOfficeName = new ASN1ObjectIdentifier("2.5.4.19");
  public static final ASN1ObjectIdentifier postOfficeBox;
  public static final ASN1ObjectIdentifier postalAddress = new ASN1ObjectIdentifier("2.5.4.16");
  public static final ASN1ObjectIdentifier postalCode = new ASN1ObjectIdentifier("2.5.4.17");
  public static final ASN1ObjectIdentifier preferredDeliveryMethod;
  public static final ASN1ObjectIdentifier registeredAddress;
  public static final ASN1ObjectIdentifier roleOccupant;
  public static final ASN1ObjectIdentifier searchGuide;
  public static final ASN1ObjectIdentifier seeAlso;
  public static final ASN1ObjectIdentifier serialNumber;
  public static final ASN1ObjectIdentifier sn;
  public static final ASN1ObjectIdentifier st;
  public static final ASN1ObjectIdentifier street;
  public static final ASN1ObjectIdentifier telephoneNumber;
  public static final ASN1ObjectIdentifier teletexTerminalIdentifier;
  public static final ASN1ObjectIdentifier telexNumber;
  public static final ASN1ObjectIdentifier title;
  public static final ASN1ObjectIdentifier uid;
  public static final ASN1ObjectIdentifier uniqueMember;
  public static final ASN1ObjectIdentifier userPassword;
  public static final ASN1ObjectIdentifier x121Address;
  public static final ASN1ObjectIdentifier x500UniqueIdentifier;

  static
  {
    postOfficeBox = new ASN1ObjectIdentifier("2.5.4.18");
    preferredDeliveryMethod = new ASN1ObjectIdentifier("2.5.4.28");
    registeredAddress = new ASN1ObjectIdentifier("2.5.4.26");
    roleOccupant = new ASN1ObjectIdentifier("2.5.4.33");
    searchGuide = new ASN1ObjectIdentifier("2.5.4.14");
    seeAlso = new ASN1ObjectIdentifier("2.5.4.34");
    serialNumber = new ASN1ObjectIdentifier("2.5.4.5");
    sn = new ASN1ObjectIdentifier("2.5.4.4");
    st = new ASN1ObjectIdentifier("2.5.4.8");
    street = new ASN1ObjectIdentifier("2.5.4.9");
    telephoneNumber = new ASN1ObjectIdentifier("2.5.4.20");
    teletexTerminalIdentifier = new ASN1ObjectIdentifier("2.5.4.22");
    telexNumber = new ASN1ObjectIdentifier("2.5.4.21");
    title = new ASN1ObjectIdentifier("2.5.4.12");
    uid = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
    uniqueMember = new ASN1ObjectIdentifier("2.5.4.50");
    userPassword = new ASN1ObjectIdentifier("2.5.4.35");
    x121Address = new ASN1ObjectIdentifier("2.5.4.24");
    x500UniqueIdentifier = new ASN1ObjectIdentifier("2.5.4.45");
    DefaultSymbols = new Hashtable();
    DefaultLookUp = new Hashtable();
    DefaultSymbols.put(businessCategory, "businessCategory");
    DefaultSymbols.put(c, "c");
    DefaultSymbols.put(cn, "cn");
    DefaultSymbols.put(dc, "dc");
    DefaultSymbols.put(description, "description");
    DefaultSymbols.put(destinationIndicator, "destinationIndicator");
    DefaultSymbols.put(distinguishedName, "distinguishedName");
    DefaultSymbols.put(dnQualifier, "dnQualifier");
    DefaultSymbols.put(enhancedSearchGuide, "enhancedSearchGuide");
    DefaultSymbols.put(facsimileTelephoneNumber, "facsimileTelephoneNumber");
    DefaultSymbols.put(generationQualifier, "generationQualifier");
    DefaultSymbols.put(givenName, "givenName");
    DefaultSymbols.put(houseIdentifier, "houseIdentifier");
    DefaultSymbols.put(initials, "initials");
    DefaultSymbols.put(internationalISDNNumber, "internationalISDNNumber");
    DefaultSymbols.put(l, "l");
    DefaultSymbols.put(member, "member");
    DefaultSymbols.put(name, "name");
    DefaultSymbols.put(o, "o");
    DefaultSymbols.put(ou, "ou");
    DefaultSymbols.put(owner, "owner");
    DefaultSymbols.put(physicalDeliveryOfficeName, "physicalDeliveryOfficeName");
    DefaultSymbols.put(postalAddress, "postalAddress");
    DefaultSymbols.put(postalCode, "postalCode");
    DefaultSymbols.put(postOfficeBox, "postOfficeBox");
    DefaultSymbols.put(preferredDeliveryMethod, "preferredDeliveryMethod");
    DefaultSymbols.put(registeredAddress, "registeredAddress");
    DefaultSymbols.put(roleOccupant, "roleOccupant");
    DefaultSymbols.put(searchGuide, "searchGuide");
    DefaultSymbols.put(seeAlso, "seeAlso");
    DefaultSymbols.put(serialNumber, "serialNumber");
    DefaultSymbols.put(sn, "sn");
    DefaultSymbols.put(st, "st");
    DefaultSymbols.put(street, "street");
    DefaultSymbols.put(telephoneNumber, "telephoneNumber");
    DefaultSymbols.put(teletexTerminalIdentifier, "teletexTerminalIdentifier");
    DefaultSymbols.put(telexNumber, "telexNumber");
    DefaultSymbols.put(title, "title");
    DefaultSymbols.put(uid, "uid");
    DefaultSymbols.put(uniqueMember, "uniqueMember");
    DefaultSymbols.put(userPassword, "userPassword");
    DefaultSymbols.put(x121Address, "x121Address");
    DefaultSymbols.put(x500UniqueIdentifier, "x500UniqueIdentifier");
    DefaultLookUp.put("businesscategory", businessCategory);
    DefaultLookUp.put("c", c);
    DefaultLookUp.put("cn", cn);
    DefaultLookUp.put("dc", dc);
    DefaultLookUp.put("description", description);
    DefaultLookUp.put("destinationindicator", destinationIndicator);
    DefaultLookUp.put("distinguishedname", distinguishedName);
    DefaultLookUp.put("dnqualifier", dnQualifier);
    DefaultLookUp.put("enhancedsearchguide", enhancedSearchGuide);
    DefaultLookUp.put("facsimiletelephonenumber", facsimileTelephoneNumber);
    DefaultLookUp.put("generationqualifier", generationQualifier);
    DefaultLookUp.put("givenname", givenName);
    DefaultLookUp.put("houseidentifier", houseIdentifier);
    DefaultLookUp.put("initials", initials);
    DefaultLookUp.put("internationalisdnnumber", internationalISDNNumber);
    DefaultLookUp.put("l", l);
    DefaultLookUp.put("member", member);
    DefaultLookUp.put("name", name);
    DefaultLookUp.put("o", o);
    DefaultLookUp.put("ou", ou);
    DefaultLookUp.put("owner", owner);
    DefaultLookUp.put("physicaldeliveryofficename", physicalDeliveryOfficeName);
    DefaultLookUp.put("postaladdress", postalAddress);
    DefaultLookUp.put("postalcode", postalCode);
    DefaultLookUp.put("postofficebox", postOfficeBox);
    DefaultLookUp.put("preferreddeliverymethod", preferredDeliveryMethod);
    DefaultLookUp.put("registeredaddress", registeredAddress);
    DefaultLookUp.put("roleoccupant", roleOccupant);
    DefaultLookUp.put("searchguide", searchGuide);
    DefaultLookUp.put("seealso", seeAlso);
    DefaultLookUp.put("serialnumber", serialNumber);
    DefaultLookUp.put("sn", sn);
    DefaultLookUp.put("st", st);
    DefaultLookUp.put("street", street);
    DefaultLookUp.put("telephonenumber", telephoneNumber);
    DefaultLookUp.put("teletexterminalidentifier", teletexTerminalIdentifier);
    DefaultLookUp.put("telexnumber", telexNumber);
    DefaultLookUp.put("title", title);
    DefaultLookUp.put("uid", uid);
    DefaultLookUp.put("uniquemember", uniqueMember);
    DefaultLookUp.put("userpassword", userPassword);
    DefaultLookUp.put("x121address", x121Address);
    DefaultLookUp.put("x500uniqueidentifier", x500UniqueIdentifier);
  }

  private boolean atvAreEqual(AttributeTypeAndValue paramAttributeTypeAndValue1, AttributeTypeAndValue paramAttributeTypeAndValue2)
  {
    if (paramAttributeTypeAndValue1 == paramAttributeTypeAndValue2);
    do
    {
      return true;
      if (paramAttributeTypeAndValue1 == null)
        return false;
      if (paramAttributeTypeAndValue2 == null)
        return false;
      if (!paramAttributeTypeAndValue1.getType().equals(paramAttributeTypeAndValue2.getType()))
        return false;
    }
    while (IETFUtils.canonicalize(IETFUtils.valueToString(paramAttributeTypeAndValue1.getValue())).equals(IETFUtils.canonicalize(IETFUtils.valueToString(paramAttributeTypeAndValue2.getValue()))));
    return false;
  }

  private int calcHashCode(ASN1Encodable paramASN1Encodable)
  {
    return IETFUtils.canonicalize(IETFUtils.valueToString(paramASN1Encodable)).hashCode();
  }

  private boolean foundMatch(boolean paramBoolean, RDN paramRDN, RDN[] paramArrayOfRDN)
  {
    int j;
    if (paramBoolean)
    {
      j = -1 + paramArrayOfRDN.length;
      if (j >= 0);
    }
    while (true)
    {
      return false;
      if ((paramArrayOfRDN[j] != null) && (rdnAreEqual(paramRDN, paramArrayOfRDN[j])))
      {
        paramArrayOfRDN[j] = null;
        return true;
      }
      j--;
      break;
      for (int i = 0; i != paramArrayOfRDN.length; i++)
        if ((paramArrayOfRDN[i] != null) && (rdnAreEqual(paramRDN, paramArrayOfRDN[i])))
        {
          paramArrayOfRDN[i] = null;
          return true;
        }
    }
  }

  public boolean areEqual(X500Name paramX500Name1, X500Name paramX500Name2)
  {
    RDN[] arrayOfRDN1 = paramX500Name1.getRDNs();
    RDN[] arrayOfRDN2 = paramX500Name2.getRDNs();
    if (arrayOfRDN1.length != arrayOfRDN2.length)
      return false;
    AttributeTypeAndValue localAttributeTypeAndValue1 = arrayOfRDN1[0].getFirst();
    boolean bool = false;
    if (localAttributeTypeAndValue1 != null)
    {
      AttributeTypeAndValue localAttributeTypeAndValue2 = arrayOfRDN2[0].getFirst();
      bool = false;
      if (localAttributeTypeAndValue2 != null)
      {
        if (!arrayOfRDN1[0].getFirst().getType().equals(arrayOfRDN2[0].getFirst().getType()))
          break label94;
        bool = false;
      }
    }
    label82: for (int i = 0; ; i++)
    {
      if (i == arrayOfRDN1.length)
      {
        return true;
        label94: bool = true;
        break label82;
      }
      if (!foundMatch(bool, arrayOfRDN1[i], arrayOfRDN2))
        break;
    }
  }

  public ASN1ObjectIdentifier attrNameToOID(String paramString)
  {
    return IETFUtils.decodeAttrName(paramString, DefaultLookUp);
  }

  public int calculateHashCode(X500Name paramX500Name)
  {
    int i = 0;
    RDN[] arrayOfRDN = paramX500Name.getRDNs();
    int j = 0;
    if (j == arrayOfRDN.length)
      return i;
    AttributeTypeAndValue[] arrayOfAttributeTypeAndValue;
    int k;
    if (arrayOfRDN[j].isMultiValued())
    {
      arrayOfAttributeTypeAndValue = arrayOfRDN[j].getTypesAndValues();
      k = 0;
      label41: if (k != arrayOfAttributeTypeAndValue.length);
    }
    while (true)
    {
      j++;
      break;
      i = i ^ arrayOfAttributeTypeAndValue[k].getType().hashCode() ^ calcHashCode(arrayOfAttributeTypeAndValue[k].getValue());
      k++;
      break label41;
      i = i ^ arrayOfRDN[j].getFirst().getType().hashCode() ^ calcHashCode(arrayOfRDN[j].getFirst().getValue());
    }
  }

  public RDN[] fromString(String paramString)
  {
    RDN[] arrayOfRDN1 = IETFUtils.rDNsFromString(paramString, this);
    RDN[] arrayOfRDN2 = new RDN[arrayOfRDN1.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfRDN1.length)
        return arrayOfRDN2;
      arrayOfRDN2[(-1 + (arrayOfRDN2.length - i))] = arrayOfRDN1[i];
    }
  }

  protected boolean rdnAreEqual(RDN paramRDN1, RDN paramRDN2)
  {
    if (paramRDN1.isMultiValued())
      if (paramRDN2.isMultiValued())
      {
        arrayOfAttributeTypeAndValue1 = paramRDN1.getTypesAndValues();
        arrayOfAttributeTypeAndValue2 = paramRDN2.getTypesAndValues();
        if (arrayOfAttributeTypeAndValue1.length == arrayOfAttributeTypeAndValue2.length)
          break label35;
      }
    label35: 
    while (paramRDN2.isMultiValued())
    {
      AttributeTypeAndValue[] arrayOfAttributeTypeAndValue1;
      AttributeTypeAndValue[] arrayOfAttributeTypeAndValue2;
      return false;
      for (int i = 0; ; i++)
      {
        if (i == arrayOfAttributeTypeAndValue1.length)
          return true;
        if (!atvAreEqual(arrayOfAttributeTypeAndValue1[i], arrayOfAttributeTypeAndValue2[i]))
          break;
      }
    }
    return atvAreEqual(paramRDN1.getFirst(), paramRDN2.getFirst());
  }

  public ASN1Encodable stringToValue(ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString)
  {
    if ((paramString.length() != 0) && (paramString.charAt(0) == '#'))
      try
      {
        ASN1Encodable localASN1Encodable = IETFUtils.valueFromHexString(paramString, 1);
        return localASN1Encodable;
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("can't recode value for oid " + paramASN1ObjectIdentifier.getId());
      }
    if ((paramString.length() != 0) && (paramString.charAt(0) == '\\'))
      paramString = paramString.substring(1);
    if (paramASN1ObjectIdentifier.equals(dc))
      return new DERIA5String(paramString);
    if ((paramASN1ObjectIdentifier.equals(c)) || (paramASN1ObjectIdentifier.equals(serialNumber)) || (paramASN1ObjectIdentifier.equals(dnQualifier)) || (paramASN1ObjectIdentifier.equals(telephoneNumber)))
      return new DERPrintableString(paramString);
    return new DERUTF8String(paramString);
  }

  public String toString(X500Name paramX500Name)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 1;
    RDN[] arrayOfRDN = paramX500Name.getRDNs();
    int j = -1 + arrayOfRDN.length;
    if (j < 0)
      return localStringBuffer.toString();
    label39: AttributeTypeAndValue[] arrayOfAttributeTypeAndValue;
    int k;
    int m;
    if (i != 0)
    {
      i = 0;
      if (!arrayOfRDN[j].isMultiValued())
        break label126;
      arrayOfAttributeTypeAndValue = arrayOfRDN[j].getTypesAndValues();
      k = 1;
      m = 0;
      if (m != arrayOfAttributeTypeAndValue.length)
        break label90;
    }
    while (true)
    {
      j--;
      break;
      localStringBuffer.append(',');
      break label39;
      label90: if (k != 0)
        k = 0;
      while (true)
      {
        IETFUtils.appendTypeAndValue(localStringBuffer, arrayOfAttributeTypeAndValue[m], DefaultSymbols);
        m++;
        break;
        localStringBuffer.append('+');
      }
      label126: IETFUtils.appendTypeAndValue(localStringBuffer, arrayOfRDN[j].getFirst(), DefaultSymbols);
    }
  }
}