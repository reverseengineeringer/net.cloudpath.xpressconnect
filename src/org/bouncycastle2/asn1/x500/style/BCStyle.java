package org.bouncycastle2.asn1.x500.style;

import java.io.IOException;
import java.util.Hashtable;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERUTF8String;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle2.asn1.x500.RDN;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x500.X500NameStyle;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;

public class BCStyle
  implements X500NameStyle
{
  public static final ASN1ObjectIdentifier BUSINESS_CATEGORY;
  public static final ASN1ObjectIdentifier C;
  public static final ASN1ObjectIdentifier CN;
  public static final ASN1ObjectIdentifier COUNTRY_OF_CITIZENSHIP;
  public static final ASN1ObjectIdentifier COUNTRY_OF_RESIDENCE;
  public static final ASN1ObjectIdentifier DATE_OF_BIRTH;
  public static final ASN1ObjectIdentifier DC;
  public static final ASN1ObjectIdentifier DMD_NAME;
  public static final ASN1ObjectIdentifier DN_QUALIFIER;
  private static final Hashtable DefaultLookUp;
  private static final Hashtable DefaultSymbols;
  public static final ASN1ObjectIdentifier E;
  public static final ASN1ObjectIdentifier EmailAddress;
  public static final ASN1ObjectIdentifier GENDER;
  public static final ASN1ObjectIdentifier GENERATION;
  public static final ASN1ObjectIdentifier GIVENNAME;
  public static final ASN1ObjectIdentifier INITIALS;
  public static final X500NameStyle INSTANCE = new BCStyle();
  public static final ASN1ObjectIdentifier L;
  public static final ASN1ObjectIdentifier NAME;
  public static final ASN1ObjectIdentifier NAME_AT_BIRTH;
  public static final ASN1ObjectIdentifier O;
  public static final ASN1ObjectIdentifier OU;
  public static final ASN1ObjectIdentifier PLACE_OF_BIRTH;
  public static final ASN1ObjectIdentifier POSTAL_ADDRESS;
  public static final ASN1ObjectIdentifier POSTAL_CODE;
  public static final ASN1ObjectIdentifier PSEUDONYM;
  public static final ASN1ObjectIdentifier SERIALNUMBER;
  public static final ASN1ObjectIdentifier SN;
  public static final ASN1ObjectIdentifier ST;
  public static final ASN1ObjectIdentifier STREET;
  public static final ASN1ObjectIdentifier SURNAME;
  public static final ASN1ObjectIdentifier T;
  public static final ASN1ObjectIdentifier TELEPHONE_NUMBER;
  public static final ASN1ObjectIdentifier UID;
  public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER;
  public static final ASN1ObjectIdentifier UnstructuredAddress;
  public static final ASN1ObjectIdentifier UnstructuredName;

  static
  {
    C = new ASN1ObjectIdentifier("2.5.4.6");
    O = new ASN1ObjectIdentifier("2.5.4.10");
    OU = new ASN1ObjectIdentifier("2.5.4.11");
    T = new ASN1ObjectIdentifier("2.5.4.12");
    CN = new ASN1ObjectIdentifier("2.5.4.3");
    SN = new ASN1ObjectIdentifier("2.5.4.5");
    STREET = new ASN1ObjectIdentifier("2.5.4.9");
    SERIALNUMBER = SN;
    L = new ASN1ObjectIdentifier("2.5.4.7");
    ST = new ASN1ObjectIdentifier("2.5.4.8");
    SURNAME = new ASN1ObjectIdentifier("2.5.4.4");
    GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42");
    INITIALS = new ASN1ObjectIdentifier("2.5.4.43");
    GENERATION = new ASN1ObjectIdentifier("2.5.4.44");
    UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45");
    BUSINESS_CATEGORY = new ASN1ObjectIdentifier("2.5.4.15");
    POSTAL_CODE = new ASN1ObjectIdentifier("2.5.4.17");
    DN_QUALIFIER = new ASN1ObjectIdentifier("2.5.4.46");
    PSEUDONYM = new ASN1ObjectIdentifier("2.5.4.65");
    DATE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.1");
    PLACE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.2");
    GENDER = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.3");
    COUNTRY_OF_CITIZENSHIP = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.4");
    COUNTRY_OF_RESIDENCE = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.5");
    NAME_AT_BIRTH = new ASN1ObjectIdentifier("1.3.36.8.3.14");
    POSTAL_ADDRESS = new ASN1ObjectIdentifier("2.5.4.16");
    DMD_NAME = new ASN1ObjectIdentifier("2.5.4.54");
    TELEPHONE_NUMBER = X509ObjectIdentifiers.id_at_telephoneNumber;
    NAME = X509ObjectIdentifiers.id_at_name;
    EmailAddress = PKCSObjectIdentifiers.pkcs_9_at_emailAddress;
    UnstructuredName = PKCSObjectIdentifiers.pkcs_9_at_unstructuredName;
    UnstructuredAddress = PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress;
    E = EmailAddress;
    DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
    UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
    DefaultSymbols = new Hashtable();
    DefaultLookUp = new Hashtable();
    DefaultSymbols.put(C, "C");
    DefaultSymbols.put(O, "O");
    DefaultSymbols.put(T, "T");
    DefaultSymbols.put(OU, "OU");
    DefaultSymbols.put(CN, "CN");
    DefaultSymbols.put(L, "L");
    DefaultSymbols.put(ST, "ST");
    DefaultSymbols.put(SN, "SERIALNUMBER");
    DefaultSymbols.put(EmailAddress, "E");
    DefaultSymbols.put(DC, "DC");
    DefaultSymbols.put(UID, "UID");
    DefaultSymbols.put(STREET, "STREET");
    DefaultSymbols.put(SURNAME, "SURNAME");
    DefaultSymbols.put(GIVENNAME, "GIVENNAME");
    DefaultSymbols.put(INITIALS, "INITIALS");
    DefaultSymbols.put(GENERATION, "GENERATION");
    DefaultSymbols.put(UnstructuredAddress, "unstructuredAddress");
    DefaultSymbols.put(UnstructuredName, "unstructuredName");
    DefaultSymbols.put(UNIQUE_IDENTIFIER, "UniqueIdentifier");
    DefaultSymbols.put(DN_QUALIFIER, "DN");
    DefaultSymbols.put(PSEUDONYM, "Pseudonym");
    DefaultSymbols.put(POSTAL_ADDRESS, "PostalAddress");
    DefaultSymbols.put(NAME_AT_BIRTH, "NameAtBirth");
    DefaultSymbols.put(COUNTRY_OF_CITIZENSHIP, "CountryOfCitizenship");
    DefaultSymbols.put(COUNTRY_OF_RESIDENCE, "CountryOfResidence");
    DefaultSymbols.put(GENDER, "Gender");
    DefaultSymbols.put(PLACE_OF_BIRTH, "PlaceOfBirth");
    DefaultSymbols.put(DATE_OF_BIRTH, "DateOfBirth");
    DefaultSymbols.put(POSTAL_CODE, "PostalCode");
    DefaultSymbols.put(BUSINESS_CATEGORY, "BusinessCategory");
    DefaultSymbols.put(TELEPHONE_NUMBER, "TelephoneNumber");
    DefaultSymbols.put(NAME, "Name");
    DefaultLookUp.put("c", C);
    DefaultLookUp.put("o", O);
    DefaultLookUp.put("t", T);
    DefaultLookUp.put("ou", OU);
    DefaultLookUp.put("cn", CN);
    DefaultLookUp.put("l", L);
    DefaultLookUp.put("st", ST);
    DefaultLookUp.put("sn", SN);
    DefaultLookUp.put("serialnumber", SN);
    DefaultLookUp.put("street", STREET);
    DefaultLookUp.put("emailaddress", E);
    DefaultLookUp.put("dc", DC);
    DefaultLookUp.put("e", E);
    DefaultLookUp.put("uid", UID);
    DefaultLookUp.put("surname", SURNAME);
    DefaultLookUp.put("givenname", GIVENNAME);
    DefaultLookUp.put("initials", INITIALS);
    DefaultLookUp.put("generation", GENERATION);
    DefaultLookUp.put("unstructuredaddress", UnstructuredAddress);
    DefaultLookUp.put("unstructuredname", UnstructuredName);
    DefaultLookUp.put("uniqueidentifier", UNIQUE_IDENTIFIER);
    DefaultLookUp.put("dn", DN_QUALIFIER);
    DefaultLookUp.put("pseudonym", PSEUDONYM);
    DefaultLookUp.put("postaladdress", POSTAL_ADDRESS);
    DefaultLookUp.put("nameofbirth", NAME_AT_BIRTH);
    DefaultLookUp.put("countryofcitizenship", COUNTRY_OF_CITIZENSHIP);
    DefaultLookUp.put("countryofresidence", COUNTRY_OF_RESIDENCE);
    DefaultLookUp.put("gender", GENDER);
    DefaultLookUp.put("placeofbirth", PLACE_OF_BIRTH);
    DefaultLookUp.put("dateofbirth", DATE_OF_BIRTH);
    DefaultLookUp.put("postalcode", POSTAL_CODE);
    DefaultLookUp.put("businesscategory", BUSINESS_CATEGORY);
    DefaultLookUp.put("telephonenumber", TELEPHONE_NUMBER);
    DefaultLookUp.put("name", NAME);
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
    return IETFUtils.rDNsFromString(paramString, this);
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
    if ((paramASN1ObjectIdentifier.equals(EmailAddress)) || (paramASN1ObjectIdentifier.equals(DC)))
      return new DERIA5String(paramString);
    if (paramASN1ObjectIdentifier.equals(DATE_OF_BIRTH))
      return new DERGeneralizedTime(paramString);
    if ((paramASN1ObjectIdentifier.equals(C)) || (paramASN1ObjectIdentifier.equals(SN)) || (paramASN1ObjectIdentifier.equals(DN_QUALIFIER)) || (paramASN1ObjectIdentifier.equals(TELEPHONE_NUMBER)))
      return new DERPrintableString(paramString);
    return new DERUTF8String(paramString);
  }

  public String toString(X500Name paramX500Name)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 1;
    RDN[] arrayOfRDN = paramX500Name.getRDNs();
    int j = 0;
    if (j >= arrayOfRDN.length)
      return localStringBuffer.toString();
    label38: AttributeTypeAndValue[] arrayOfAttributeTypeAndValue;
    int k;
    int m;
    if (i != 0)
    {
      i = 0;
      if (!arrayOfRDN[j].isMultiValued())
        break label125;
      arrayOfAttributeTypeAndValue = arrayOfRDN[j].getTypesAndValues();
      k = 1;
      m = 0;
      if (m != arrayOfAttributeTypeAndValue.length)
        break label89;
    }
    while (true)
    {
      j++;
      break;
      localStringBuffer.append(',');
      break label38;
      label89: if (k != 0)
        k = 0;
      while (true)
      {
        IETFUtils.appendTypeAndValue(localStringBuffer, arrayOfAttributeTypeAndValue[m], DefaultSymbols);
        m++;
        break;
        localStringBuffer.append('+');
      }
      label125: IETFUtils.appendTypeAndValue(localStringBuffer, arrayOfRDN[j].getFirst(), DefaultSymbols);
    }
  }
}