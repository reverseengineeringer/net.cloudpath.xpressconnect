package org.bouncycastle2.asn1.x500.style;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERUniversalString;
import org.bouncycastle2.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle2.asn1.x500.RDN;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x500.X500NameBuilder;
import org.bouncycastle2.asn1.x500.X500NameStyle;
import org.bouncycastle2.util.Strings;
import org.bouncycastle2.util.encoders.Hex;

public class IETFUtils
{
  public static void appendTypeAndValue(StringBuffer paramStringBuffer, AttributeTypeAndValue paramAttributeTypeAndValue, Hashtable paramHashtable)
  {
    String str = (String)paramHashtable.get(paramAttributeTypeAndValue.getType());
    if (str != null)
      paramStringBuffer.append(str);
    while (true)
    {
      paramStringBuffer.append('=');
      paramStringBuffer.append(valueToString(paramAttributeTypeAndValue.getValue()));
      return;
      paramStringBuffer.append(paramAttributeTypeAndValue.getType().getId());
    }
  }

  private static String bytesToString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
        return new String(arrayOfChar);
      arrayOfChar[i] = ((char)(0xFF & paramArrayOfByte[i]));
    }
  }

  public static String canonicalize(String paramString)
  {
    String str = Strings.toLowerCase(paramString.trim());
    if ((str.length() > 0) && (str.charAt(0) == '#'))
    {
      ASN1Object localASN1Object = decodeObject(str);
      if ((localASN1Object instanceof ASN1String))
        str = Strings.toLowerCase(((ASN1String)localASN1Object).getString().trim());
    }
    return stripInternalSpaces(str);
  }

  public static ASN1ObjectIdentifier decodeAttrName(String paramString, Hashtable paramHashtable)
  {
    ASN1ObjectIdentifier localASN1ObjectIdentifier;
    if (Strings.toUpperCase(paramString).startsWith("OID."))
      localASN1ObjectIdentifier = new ASN1ObjectIdentifier(paramString.substring(4));
    do
    {
      return localASN1ObjectIdentifier;
      if ((paramString.charAt(0) >= '0') && (paramString.charAt(0) <= '9'))
        return new ASN1ObjectIdentifier(paramString);
      localASN1ObjectIdentifier = (ASN1ObjectIdentifier)paramHashtable.get(Strings.toLowerCase(paramString));
    }
    while (localASN1ObjectIdentifier != null);
    throw new IllegalArgumentException("Unknown object id - " + paramString + " - passed to distinguished name");
  }

  private static ASN1Object decodeObject(String paramString)
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

  public static RDN[] rDNsFromString(String paramString, X500NameStyle paramX500NameStyle)
  {
    X500NameTokenizer localX500NameTokenizer1 = new X500NameTokenizer(paramString);
    X500NameBuilder localX500NameBuilder = new X500NameBuilder(paramX500NameStyle);
    while (true)
    {
      if (!localX500NameTokenizer1.hasMoreTokens())
        return localX500NameBuilder.build().getRDNs();
      String str1 = localX500NameTokenizer1.nextToken();
      int i = str1.indexOf('=');
      if (i == -1)
        throw new IllegalArgumentException("badly formated directory string");
      String str2 = str1.substring(0, i);
      String str3 = str1.substring(i + 1);
      ASN1ObjectIdentifier localASN1ObjectIdentifier = paramX500NameStyle.attrNameToOID(str2);
      if (str3.indexOf('+') > 0)
      {
        X500NameTokenizer localX500NameTokenizer2 = new X500NameTokenizer(str3, '+');
        String str4 = localX500NameTokenizer2.nextToken();
        Vector localVector1 = new Vector();
        Vector localVector2 = new Vector();
        localVector1.addElement(localASN1ObjectIdentifier);
        localVector2.addElement(str4);
        while (true)
        {
          if (!localX500NameTokenizer2.hasMoreTokens())
          {
            localX500NameBuilder.addMultiValuedRDN(toOIDArray(localVector1), toValueArray(localVector2));
            break;
          }
          String str5 = localX500NameTokenizer2.nextToken();
          int j = str5.indexOf('=');
          String str6 = str5.substring(0, j);
          String str7 = str5.substring(j + 1);
          localVector1.addElement(paramX500NameStyle.attrNameToOID(str6));
          localVector2.addElement(str7);
        }
      }
      localX500NameBuilder.addRDN(localASN1ObjectIdentifier, str3);
    }
  }

  public static String stripInternalSpaces(String paramString)
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

  private static ASN1ObjectIdentifier[] toOIDArray(Vector paramVector)
  {
    ASN1ObjectIdentifier[] arrayOfASN1ObjectIdentifier = new ASN1ObjectIdentifier[paramVector.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfASN1ObjectIdentifier.length)
        return arrayOfASN1ObjectIdentifier;
      arrayOfASN1ObjectIdentifier[i] = ((ASN1ObjectIdentifier)paramVector.elementAt(i));
    }
  }

  private static String[] toValueArray(Vector paramVector)
  {
    String[] arrayOfString = new String[paramVector.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfString.length)
        return arrayOfString;
      arrayOfString[i] = ((String)paramVector.elementAt(i));
    }
  }

  public static ASN1Encodable valueFromHexString(String paramString, int paramInt)
    throws IOException
  {
    String str = Strings.toLowerCase(paramString);
    byte[] arrayOfByte = new byte[(str.length() - paramInt) / 2];
    int i = 0;
    if (i == arrayOfByte.length)
      return ASN1Object.fromByteArray(arrayOfByte);
    int j = str.charAt(paramInt + i * 2);
    int k = str.charAt(1 + (paramInt + i * 2));
    if (j < 97)
    {
      arrayOfByte[i] = ((byte)(j - 48 << 4));
      label76: if (k >= 97)
        break label123;
      arrayOfByte[i] = ((byte)(arrayOfByte[i] | (byte)(k - 48)));
    }
    while (true)
    {
      i++;
      break;
      arrayOfByte[i] = ((byte)(10 + (j - 97) << 4));
      break label76;
      label123: arrayOfByte[i] = ((byte)(arrayOfByte[i] | (byte)(10 + (k - 97))));
    }
  }

  public static String valueToString(ASN1Encodable paramASN1Encodable)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str;
    int i;
    if (((paramASN1Encodable instanceof ASN1String)) && (!(paramASN1Encodable instanceof DERUniversalString)))
    {
      str = ((ASN1String)paramASN1Encodable).getString();
      if ((str.length() > 0) && (str.charAt(0) == '#'))
      {
        localStringBuffer.append("\\" + str);
        i = localStringBuffer.length();
        int j = localStringBuffer.length();
        k = 0;
        if (j >= 2)
        {
          int m = localStringBuffer.charAt(0);
          k = 0;
          if (m == 92)
          {
            int n = localStringBuffer.charAt(1);
            k = 0;
            if (n != 35);
          }
        }
      }
    }
    for (int k = 0 + 2; ; k++)
    {
      if (k == i)
      {
        return localStringBuffer.toString();
        localStringBuffer.append(str);
        break;
        localStringBuffer.append("#" + bytesToString(Hex.encode(paramASN1Encodable.getDERObject().getDEREncoded())));
        break;
      }
      if ((localStringBuffer.charAt(k) == ',') || (localStringBuffer.charAt(k) == '"') || (localStringBuffer.charAt(k) == '\\') || (localStringBuffer.charAt(k) == '+') || (localStringBuffer.charAt(k) == '=') || (localStringBuffer.charAt(k) == '<') || (localStringBuffer.charAt(k) == '>') || (localStringBuffer.charAt(k) == ';'))
      {
        localStringBuffer.insert(k, "\\");
        k++;
        i++;
      }
    }
  }
}