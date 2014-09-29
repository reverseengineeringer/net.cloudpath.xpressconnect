package org.bouncycastle2.asn1.x509;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.DERString;
import org.bouncycastle2.asn1.DERUniversalString;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.util.Strings;
import org.bouncycastle2.util.encoders.Hex;

public class X509Name extends ASN1Encodable
{
  public static final DERObjectIdentifier BUSINESS_CATEGORY;
  public static final DERObjectIdentifier C = new DERObjectIdentifier("2.5.4.6");
  public static final DERObjectIdentifier CN;
  public static final DERObjectIdentifier COUNTRY_OF_CITIZENSHIP;
  public static final DERObjectIdentifier COUNTRY_OF_RESIDENCE;
  public static final DERObjectIdentifier DATE_OF_BIRTH;
  public static final DERObjectIdentifier DC;
  public static final DERObjectIdentifier DMD_NAME;
  public static final DERObjectIdentifier DN_QUALIFIER;
  public static final Hashtable DefaultLookUp;
  public static boolean DefaultReverse;
  public static final Hashtable DefaultSymbols;
  public static final DERObjectIdentifier E;
  public static final DERObjectIdentifier EmailAddress;
  private static final Boolean FALSE;
  public static final DERObjectIdentifier GENDER;
  public static final DERObjectIdentifier GENERATION;
  public static final DERObjectIdentifier GIVENNAME;
  public static final DERObjectIdentifier INITIALS;
  public static final DERObjectIdentifier L;
  public static final DERObjectIdentifier NAME;
  public static final DERObjectIdentifier NAME_AT_BIRTH;
  public static final DERObjectIdentifier O = new DERObjectIdentifier("2.5.4.10");
  public static final Hashtable OIDLookUp;
  public static final DERObjectIdentifier OU = new DERObjectIdentifier("2.5.4.11");
  public static final DERObjectIdentifier PLACE_OF_BIRTH;
  public static final DERObjectIdentifier POSTAL_ADDRESS;
  public static final DERObjectIdentifier POSTAL_CODE;
  public static final DERObjectIdentifier PSEUDONYM;
  public static final Hashtable RFC1779Symbols;
  public static final Hashtable RFC2253Symbols;
  public static final DERObjectIdentifier SERIALNUMBER;
  public static final DERObjectIdentifier SN;
  public static final DERObjectIdentifier ST;
  public static final DERObjectIdentifier STREET;
  public static final DERObjectIdentifier SURNAME;
  public static final Hashtable SymbolLookUp;
  public static final DERObjectIdentifier T = new DERObjectIdentifier("2.5.4.12");
  public static final DERObjectIdentifier TELEPHONE_NUMBER;
  private static final Boolean TRUE;
  public static final DERObjectIdentifier UID;
  public static final DERObjectIdentifier UNIQUE_IDENTIFIER;
  public static final DERObjectIdentifier UnstructuredAddress;
  public static final DERObjectIdentifier UnstructuredName;
  private Vector added = new Vector();
  private X509NameEntryConverter converter = null;
  private int hashCodeValue;
  private boolean isHashCodeCalculated;
  private Vector ordering = new Vector();
  private ASN1Sequence seq;
  private Vector values = new Vector();

  static
  {
    CN = new DERObjectIdentifier("2.5.4.3");
    SN = new DERObjectIdentifier("2.5.4.5");
    STREET = new DERObjectIdentifier("2.5.4.9");
    SERIALNUMBER = SN;
    L = new DERObjectIdentifier("2.5.4.7");
    ST = new DERObjectIdentifier("2.5.4.8");
    SURNAME = new DERObjectIdentifier("2.5.4.4");
    GIVENNAME = new DERObjectIdentifier("2.5.4.42");
    INITIALS = new DERObjectIdentifier("2.5.4.43");
    GENERATION = new DERObjectIdentifier("2.5.4.44");
    UNIQUE_IDENTIFIER = new DERObjectIdentifier("2.5.4.45");
    BUSINESS_CATEGORY = new DERObjectIdentifier("2.5.4.15");
    POSTAL_CODE = new DERObjectIdentifier("2.5.4.17");
    DN_QUALIFIER = new DERObjectIdentifier("2.5.4.46");
    PSEUDONYM = new DERObjectIdentifier("2.5.4.65");
    DATE_OF_BIRTH = new DERObjectIdentifier("1.3.6.1.5.5.7.9.1");
    PLACE_OF_BIRTH = new DERObjectIdentifier("1.3.6.1.5.5.7.9.2");
    GENDER = new DERObjectIdentifier("1.3.6.1.5.5.7.9.3");
    COUNTRY_OF_CITIZENSHIP = new DERObjectIdentifier("1.3.6.1.5.5.7.9.4");
    COUNTRY_OF_RESIDENCE = new DERObjectIdentifier("1.3.6.1.5.5.7.9.5");
    NAME_AT_BIRTH = new DERObjectIdentifier("1.3.36.8.3.14");
    POSTAL_ADDRESS = new DERObjectIdentifier("2.5.4.16");
    DMD_NAME = new DERObjectIdentifier("2.5.4.54");
    TELEPHONE_NUMBER = X509ObjectIdentifiers.id_at_telephoneNumber;
    NAME = X509ObjectIdentifiers.id_at_name;
    EmailAddress = PKCSObjectIdentifiers.pkcs_9_at_emailAddress;
    UnstructuredName = PKCSObjectIdentifiers.pkcs_9_at_unstructuredName;
    UnstructuredAddress = PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress;
    E = EmailAddress;
    DC = new DERObjectIdentifier("0.9.2342.19200300.100.1.25");
    UID = new DERObjectIdentifier("0.9.2342.19200300.100.1.1");
    DefaultReverse = false;
    DefaultSymbols = new Hashtable();
    RFC2253Symbols = new Hashtable();
    RFC1779Symbols = new Hashtable();
    DefaultLookUp = new Hashtable();
    OIDLookUp = DefaultSymbols;
    SymbolLookUp = DefaultLookUp;
    TRUE = new Boolean(true);
    FALSE = new Boolean(false);
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
    RFC2253Symbols.put(C, "C");
    RFC2253Symbols.put(O, "O");
    RFC2253Symbols.put(OU, "OU");
    RFC2253Symbols.put(CN, "CN");
    RFC2253Symbols.put(L, "L");
    RFC2253Symbols.put(ST, "ST");
    RFC2253Symbols.put(STREET, "STREET");
    RFC2253Symbols.put(DC, "DC");
    RFC2253Symbols.put(UID, "UID");
    RFC1779Symbols.put(C, "C");
    RFC1779Symbols.put(O, "O");
    RFC1779Symbols.put(OU, "OU");
    RFC1779Symbols.put(CN, "CN");
    RFC1779Symbols.put(L, "L");
    RFC1779Symbols.put(ST, "ST");
    RFC1779Symbols.put(STREET, "STREET");
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

  protected X509Name()
  {
  }

  public X509Name(String paramString)
  {
    this(DefaultReverse, DefaultLookUp, paramString);
  }

  public X509Name(String paramString, X509NameEntryConverter paramX509NameEntryConverter)
  {
    this(DefaultReverse, DefaultLookUp, paramString, paramX509NameEntryConverter);
  }

  public X509Name(Hashtable paramHashtable)
  {
    this(null, paramHashtable);
  }

  public X509Name(Vector paramVector, Hashtable paramHashtable)
  {
    this(paramVector, paramHashtable, new X509DefaultEntryConverter());
  }

  public X509Name(Vector paramVector, Hashtable paramHashtable, X509NameEntryConverter paramX509NameEntryConverter)
  {
    this.converter = paramX509NameEntryConverter;
    int j;
    if (paramVector != null)
    {
      j = 0;
      if (j != paramVector.size());
    }
    label63: for (int i = 0; ; i++)
    {
      if (i == this.ordering.size())
      {
        return;
        this.ordering.addElement(paramVector.elementAt(j));
        this.added.addElement(FALSE);
        j++;
        break;
        Enumeration localEnumeration = paramHashtable.keys();
        while (localEnumeration.hasMoreElements())
        {
          this.ordering.addElement(localEnumeration.nextElement());
          this.added.addElement(FALSE);
        }
        break label63;
      }
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)this.ordering.elementAt(i);
      if (paramHashtable.get(localDERObjectIdentifier) == null)
        throw new IllegalArgumentException("No attribute for object id - " + localDERObjectIdentifier.getId() + " - passed to distinguished name");
      this.values.addElement(paramHashtable.get(localDERObjectIdentifier));
    }
  }

  public X509Name(Vector paramVector1, Vector paramVector2)
  {
    this(paramVector1, paramVector2, new X509DefaultEntryConverter());
  }

  public X509Name(Vector paramVector1, Vector paramVector2, X509NameEntryConverter paramX509NameEntryConverter)
  {
    this.converter = paramX509NameEntryConverter;
    if (paramVector1.size() != paramVector2.size())
      throw new IllegalArgumentException("oids vector must be same length as values.");
    for (int i = 0; ; i++)
    {
      if (i >= paramVector1.size())
        return;
      this.ordering.addElement(paramVector1.elementAt(i));
      this.values.addElement(paramVector2.elementAt(i));
      this.added.addElement(FALSE);
    }
  }

  public X509Name(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    if (!localEnumeration.hasMoreElements())
      return;
    ASN1Set localASN1Set = ASN1Set.getInstance(((DEREncodable)localEnumeration.nextElement()).getDERObject());
    int i = 0;
    label83: DEREncodable localDEREncodable;
    String str;
    label219: Vector localVector;
    if (i < localASN1Set.size())
    {
      ASN1Sequence localASN1Sequence = ASN1Sequence.getInstance(localASN1Set.getObjectAt(i));
      if (localASN1Sequence.size() != 2)
        throw new IllegalArgumentException("badly sized pair");
      this.ordering.addElement(DERObjectIdentifier.getInstance(localASN1Sequence.getObjectAt(0)));
      localDEREncodable = localASN1Sequence.getObjectAt(1);
      if ((!(localDEREncodable instanceof DERString)) || ((localDEREncodable instanceof DERUniversalString)))
        break label260;
      str = ((DERString)localDEREncodable).getString();
      if ((str.length() <= 0) || (str.charAt(0) != '#'))
        break label248;
      this.values.addElement("\\" + str);
      localVector = this.added;
      if (i == 0)
        break label303;
    }
    label260: label303: for (Boolean localBoolean = TRUE; ; localBoolean = FALSE)
    {
      localVector.addElement(localBoolean);
      i++;
      break label83;
      break;
      label248: this.values.addElement(str);
      break label219;
      this.values.addElement("#" + bytesToString(Hex.encode(localDEREncodable.getDERObject().getDEREncoded())));
      break label219;
    }
  }

  public X509Name(boolean paramBoolean, String paramString)
  {
    this(paramBoolean, DefaultLookUp, paramString);
  }

  public X509Name(boolean paramBoolean, String paramString, X509NameEntryConverter paramX509NameEntryConverter)
  {
    this(paramBoolean, DefaultLookUp, paramString, paramX509NameEntryConverter);
  }

  public X509Name(boolean paramBoolean, Hashtable paramHashtable, String paramString)
  {
    this(paramBoolean, paramHashtable, paramString, new X509DefaultEntryConverter());
  }

  public X509Name(boolean paramBoolean, Hashtable paramHashtable, String paramString, X509NameEntryConverter paramX509NameEntryConverter)
  {
    this.converter = paramX509NameEntryConverter;
    X509NameTokenizer localX509NameTokenizer1 = new X509NameTokenizer(paramString);
    Vector localVector1;
    Vector localVector2;
    Vector localVector3;
    int k;
    int m;
    while (true)
    {
      if (!localX509NameTokenizer1.hasMoreTokens())
      {
        if (paramBoolean)
        {
          localVector1 = new Vector();
          localVector2 = new Vector();
          localVector3 = new Vector();
          k = 1;
          m = 0;
          if (m < this.ordering.size())
            break;
          this.ordering = localVector1;
          this.values = localVector2;
          this.added = localVector3;
        }
        return;
      }
      String str1 = localX509NameTokenizer1.nextToken();
      int i = str1.indexOf('=');
      if (i == -1)
        throw new IllegalArgumentException("badly formated directory string");
      String str2 = str1.substring(0, i);
      String str3 = str1.substring(i + 1);
      DERObjectIdentifier localDERObjectIdentifier = decodeOID(str2, paramHashtable);
      if (str3.indexOf('+') > 0)
      {
        X509NameTokenizer localX509NameTokenizer2 = new X509NameTokenizer(str3, '+');
        String str4 = localX509NameTokenizer2.nextToken();
        this.ordering.addElement(localDERObjectIdentifier);
        this.values.addElement(str4);
        this.added.addElement(FALSE);
        while (localX509NameTokenizer2.hasMoreTokens())
        {
          String str5 = localX509NameTokenizer2.nextToken();
          int j = str5.indexOf('=');
          String str6 = str5.substring(0, j);
          String str7 = str5.substring(j + 1);
          this.ordering.addElement(decodeOID(str6, paramHashtable));
          this.values.addElement(str7);
          this.added.addElement(TRUE);
        }
      }
      else
      {
        this.ordering.addElement(localDERObjectIdentifier);
        this.values.addElement(str3);
        this.added.addElement(FALSE);
      }
    }
    if (((Boolean)this.added.elementAt(m)).booleanValue())
    {
      localVector1.insertElementAt(this.ordering.elementAt(m), k);
      localVector2.insertElementAt(this.values.elementAt(m), k);
      localVector3.insertElementAt(this.added.elementAt(m), k);
      k++;
    }
    while (true)
    {
      m++;
      break;
      localVector1.insertElementAt(this.ordering.elementAt(m), 0);
      localVector2.insertElementAt(this.values.elementAt(m), 0);
      localVector3.insertElementAt(this.added.elementAt(m), 0);
      k = 1;
    }
  }

  private void appendValue(StringBuffer paramStringBuffer, Hashtable paramHashtable, DERObjectIdentifier paramDERObjectIdentifier, String paramString)
  {
    String str = (String)paramHashtable.get(paramDERObjectIdentifier);
    int i;
    int j;
    if (str != null)
    {
      paramStringBuffer.append(str);
      paramStringBuffer.append('=');
      i = paramStringBuffer.length();
      paramStringBuffer.append(paramString);
      j = paramStringBuffer.length();
      if ((paramString.length() >= 2) && (paramString.charAt(0) == '\\') && (paramString.charAt(1) == '#'))
        i += 2;
    }
    while (true)
    {
      if (i == j)
      {
        return;
        paramStringBuffer.append(paramDERObjectIdentifier.getId());
        break;
      }
      if ((paramStringBuffer.charAt(i) == ',') || (paramStringBuffer.charAt(i) == '"') || (paramStringBuffer.charAt(i) == '\\') || (paramStringBuffer.charAt(i) == '+') || (paramStringBuffer.charAt(i) == '=') || (paramStringBuffer.charAt(i) == '<') || (paramStringBuffer.charAt(i) == '>') || (paramStringBuffer.charAt(i) == ';'))
      {
        paramStringBuffer.insert(i, "\\");
        i++;
        j++;
      }
      i++;
    }
  }

  private String bytesToString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
        return new String(arrayOfChar);
      arrayOfChar[i] = ((char)(0xFF & paramArrayOfByte[i]));
    }
  }

  private String canonicalize(String paramString)
  {
    String str = Strings.toLowerCase(paramString.trim());
    if ((str.length() > 0) && (str.charAt(0) == '#'))
    {
      ASN1Object localASN1Object = decodeObject(str);
      if ((localASN1Object instanceof DERString))
        str = Strings.toLowerCase(((DERString)localASN1Object).getString().trim());
    }
    return str;
  }

  private DERObjectIdentifier decodeOID(String paramString, Hashtable paramHashtable)
  {
    DERObjectIdentifier localDERObjectIdentifier;
    if (Strings.toUpperCase(paramString).startsWith("OID."))
      localDERObjectIdentifier = new DERObjectIdentifier(paramString.substring(4));
    do
    {
      return localDERObjectIdentifier;
      if ((paramString.charAt(0) >= '0') && (paramString.charAt(0) <= '9'))
        return new DERObjectIdentifier(paramString);
      localDERObjectIdentifier = (DERObjectIdentifier)paramHashtable.get(Strings.toLowerCase(paramString));
    }
    while (localDERObjectIdentifier != null);
    throw new IllegalArgumentException("Unknown object id - " + paramString + " - passed to distinguished name");
  }

  private ASN1Object decodeObject(String paramString)
  {
    try
    {
      ASN1Object localASN1Object = ASN1Object.fromByteArray(Hex.decode(paramString.substring(1)));
      return localASN1Object;
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException("unknown encoding in name: " + localIOException);
    }
  }

  private boolean equivalentStrings(String paramString1, String paramString2)
  {
    String str1 = canonicalize(paramString1);
    String str2 = canonicalize(paramString2);
    return (str1.equals(str2)) || (stripInternalSpaces(str1).equals(stripInternalSpaces(str2)));
  }

  public static X509Name getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof X509Name)))
      return (X509Name)paramObject;
    if ((paramObject instanceof X500Name))
      return new X509Name(ASN1Sequence.getInstance(((X500Name)paramObject).getDERObject()));
    if (paramObject != null)
      return new X509Name(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static X509Name getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  private String stripInternalSpaces(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    char c1;
    if (paramString.length() != 0)
    {
      c1 = paramString.charAt(0);
      localStringBuffer.append(c1);
    }
    for (int i = 1; ; i++)
    {
      if (i >= paramString.length())
        return localStringBuffer.toString();
      char c2 = paramString.charAt(i);
      if ((c1 != ' ') || (c2 != ' '))
        localStringBuffer.append(c2);
      c1 = c2;
    }
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((!(paramObject instanceof X509Name)) && (!(paramObject instanceof ASN1Sequence)))
      return false;
    DERObject localDERObject = ((DEREncodable)paramObject).getDERObject();
    if (getDERObject().equals(localDERObject))
      return true;
    X509Name localX509Name;
    int i;
    try
    {
      localX509Name = getInstance(paramObject);
      i = this.ordering.size();
      if (i != localX509Name.ordering.size())
        return false;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      return false;
    }
    boolean[] arrayOfBoolean = new boolean[i];
    int j;
    int k;
    int m;
    int n;
    if (this.ordering.elementAt(0).equals(localX509Name.ordering.elementAt(0)))
    {
      j = 0;
      k = i;
      m = 1;
      n = j;
    }
    while (true)
    {
      if (n == k)
      {
        return true;
        j = i - 1;
        k = -1;
        m = -1;
        break;
      }
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)this.ordering.elementAt(n);
      String str = (String)this.values.elementAt(n);
      int i1 = 0;
      int i2 = 0;
      if (i1 >= i);
      while (true)
      {
        if (i2 != 0)
          break label263;
        return false;
        if (arrayOfBoolean[i1] != 0);
        while ((!localDERObjectIdentifier.equals((DERObjectIdentifier)localX509Name.ordering.elementAt(i1))) || (!equivalentStrings(str, (String)localX509Name.values.elementAt(i1))))
        {
          i1++;
          break;
        }
        arrayOfBoolean[i1] = true;
        i2 = 1;
      }
      label263: n += m;
    }
  }

  public boolean equals(Object paramObject, boolean paramBoolean)
  {
    if (!paramBoolean)
      return equals(paramObject);
    if (paramObject == this)
      return true;
    if ((!(paramObject instanceof X509Name)) && (!(paramObject instanceof ASN1Sequence)))
      return false;
    DERObject localDERObject = ((DEREncodable)paramObject).getDERObject();
    if (getDERObject().equals(localDERObject))
      return true;
    X509Name localX509Name;
    int i;
    try
    {
      localX509Name = getInstance(paramObject);
      i = this.ordering.size();
      if (i != localX509Name.ordering.size())
        return false;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      return false;
    }
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return true;
      if (((DERObjectIdentifier)this.ordering.elementAt(j)).equals((DERObjectIdentifier)localX509Name.ordering.elementAt(j)))
      {
        if (!equivalentStrings((String)this.values.elementAt(j), (String)localX509Name.values.elementAt(j)))
          return false;
      }
      else
        return false;
    }
  }

  public Vector getOIDs()
  {
    Vector localVector = new Vector();
    for (int i = 0; ; i++)
    {
      if (i == this.ordering.size())
        return localVector;
      localVector.addElement(this.ordering.elementAt(i));
    }
  }

  public Vector getValues()
  {
    Vector localVector = new Vector();
    for (int i = 0; ; i++)
    {
      if (i == this.values.size())
        return localVector;
      localVector.addElement(this.values.elementAt(i));
    }
  }

  public Vector getValues(DERObjectIdentifier paramDERObjectIdentifier)
  {
    Vector localVector = new Vector();
    int i = 0;
    if (i == this.values.size())
      return localVector;
    String str;
    if (this.ordering.elementAt(i).equals(paramDERObjectIdentifier))
    {
      str = (String)this.values.elementAt(i);
      if ((str.length() <= 2) || (str.charAt(0) != '\\') || (str.charAt(1) != '#'))
        break label98;
      localVector.addElement(str.substring(1));
    }
    while (true)
    {
      i++;
      break;
      label98: localVector.addElement(str);
    }
  }

  public int hashCode()
  {
    if (this.isHashCodeCalculated)
      return this.hashCodeValue;
    this.isHashCodeCalculated = true;
    for (int i = 0; ; i++)
    {
      if (i == this.ordering.size())
        return this.hashCodeValue;
      String str = stripInternalSpaces(canonicalize((String)this.values.elementAt(i)));
      this.hashCodeValue ^= this.ordering.elementAt(i).hashCode();
      this.hashCodeValue ^= str.hashCode();
    }
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector1;
    ASN1EncodableVector localASN1EncodableVector2;
    Object localObject;
    int i;
    if (this.seq == null)
    {
      localASN1EncodableVector1 = new ASN1EncodableVector();
      localASN1EncodableVector2 = new ASN1EncodableVector();
      localObject = null;
      i = 0;
      if (i == this.ordering.size())
      {
        localASN1EncodableVector1.add(new DERSet(localASN1EncodableVector2));
        this.seq = new DERSequence(localASN1EncodableVector1);
      }
    }
    else
    {
      return this.seq;
    }
    ASN1EncodableVector localASN1EncodableVector3 = new ASN1EncodableVector();
    DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)this.ordering.elementAt(i);
    localASN1EncodableVector3.add(localDERObjectIdentifier);
    String str = (String)this.values.elementAt(i);
    localASN1EncodableVector3.add(this.converter.getConvertedValue(localDERObjectIdentifier, str));
    if ((localObject == null) || (((Boolean)this.added.elementAt(i)).booleanValue()))
      localASN1EncodableVector2.add(new DERSequence(localASN1EncodableVector3));
    while (true)
    {
      localObject = localDERObjectIdentifier;
      i++;
      break;
      localASN1EncodableVector1.add(new DERSet(localASN1EncodableVector2));
      localASN1EncodableVector2 = new ASN1EncodableVector();
      localASN1EncodableVector2.add(new DERSequence(localASN1EncodableVector3));
    }
  }

  public String toString()
  {
    return toString(DefaultReverse, DefaultSymbols);
  }

  public String toString(boolean paramBoolean, Hashtable paramHashtable)
  {
    StringBuffer localStringBuffer1 = new StringBuffer();
    Vector localVector = new Vector();
    int i = 1;
    StringBuffer localStringBuffer2 = null;
    int j = 0;
    int m;
    if (j >= this.ordering.size())
    {
      if (!paramBoolean)
        break label213;
      m = -1 + localVector.size();
      if (m < 0)
        return localStringBuffer1.toString();
    }
    else
    {
      if (((Boolean)this.added.elementAt(j)).booleanValue())
      {
        localStringBuffer2.append('+');
        appendValue(localStringBuffer2, paramHashtable, (DERObjectIdentifier)this.ordering.elementAt(j), (String)this.values.elementAt(j));
      }
      while (true)
      {
        j++;
        break;
        localStringBuffer2 = new StringBuffer();
        appendValue(localStringBuffer2, paramHashtable, (DERObjectIdentifier)this.ordering.elementAt(j), (String)this.values.elementAt(j));
        localVector.addElement(localStringBuffer2);
      }
    }
    if (i != 0)
      i = 0;
    while (true)
    {
      localStringBuffer1.append(localVector.elementAt(m).toString());
      m--;
      break;
      localStringBuffer1.append(',');
    }
    label213: int k = 0;
    label216: if (k < localVector.size())
    {
      if (i == 0)
        break label255;
      i = 0;
    }
    while (true)
    {
      localStringBuffer1.append(localVector.elementAt(k).toString());
      k++;
      break label216;
      break;
      label255: localStringBuffer1.append(',');
    }
  }
}