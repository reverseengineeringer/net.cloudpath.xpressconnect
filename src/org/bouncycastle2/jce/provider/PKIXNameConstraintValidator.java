package org.bouncycastle2.jce.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.GeneralSubtree;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.Strings;

public class PKIXNameConstraintValidator
{
  private Set excludedSubtreesDN = new HashSet();
  private Set excludedSubtreesDNS = new HashSet();
  private Set excludedSubtreesEmail = new HashSet();
  private Set excludedSubtreesIP = new HashSet();
  private Set excludedSubtreesURI = new HashSet();
  private Set permittedSubtreesDN;
  private Set permittedSubtreesDNS;
  private Set permittedSubtreesEmail;
  private Set permittedSubtreesIP;
  private Set permittedSubtreesURI;

  private void checkExcludedDN(Set paramSet, ASN1Sequence paramASN1Sequence)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet.isEmpty());
    Iterator localIterator;
    do
    {
      return;
      while (!localIterator.hasNext())
        localIterator = paramSet.iterator();
    }
    while (!withinDNSubtree(paramASN1Sequence, (ASN1Sequence)localIterator.next()));
    throw new PKIXNameConstraintValidatorException("Subject distinguished name is from an excluded subtree");
  }

  private void checkExcludedDNS(Set paramSet, String paramString)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet.isEmpty());
    String str;
    do
    {
      return;
      Iterator localIterator;
      while (!localIterator.hasNext())
        localIterator = paramSet.iterator();
      str = (String)localIterator.next();
    }
    while ((!withinDomain(paramString, str)) && (!paramString.equalsIgnoreCase(str)));
    throw new PKIXNameConstraintValidatorException("DNS is from an excluded subtree.");
  }

  private void checkExcludedEmail(Set paramSet, String paramString)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet.isEmpty());
    Iterator localIterator;
    do
    {
      return;
      while (!localIterator.hasNext())
        localIterator = paramSet.iterator();
    }
    while (!emailIsConstrained(paramString, (String)localIterator.next()));
    throw new PKIXNameConstraintValidatorException("Email address is from an excluded subtree.");
  }

  private void checkExcludedIP(Set paramSet, byte[] paramArrayOfByte)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet.isEmpty());
    Iterator localIterator;
    do
    {
      return;
      while (!localIterator.hasNext())
        localIterator = paramSet.iterator();
    }
    while (!isIPConstrained(paramArrayOfByte, (byte[])localIterator.next()));
    throw new PKIXNameConstraintValidatorException("IP is from an excluded subtree.");
  }

  private void checkExcludedURI(Set paramSet, String paramString)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet.isEmpty());
    Iterator localIterator;
    do
    {
      return;
      while (!localIterator.hasNext())
        localIterator = paramSet.iterator();
    }
    while (!isUriConstrained(paramString, (String)localIterator.next()));
    throw new PKIXNameConstraintValidatorException("URI is from an excluded subtree.");
  }

  private void checkPermittedDN(Set paramSet, ASN1Sequence paramASN1Sequence)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet == null);
    while ((paramSet.isEmpty()) && (paramASN1Sequence.size() == 0))
      return;
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
        throw new PKIXNameConstraintValidatorException("Subject distinguished name is not from a permitted subtree");
    while (!withinDNSubtree(paramASN1Sequence, (ASN1Sequence)localIterator.next()));
  }

  private void checkPermittedDNS(Set paramSet, String paramString)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet == null)
      return;
    Iterator localIterator = paramSet.iterator();
    String str;
    do
    {
      if (!localIterator.hasNext())
      {
        if ((paramString.length() == 0) && (paramSet.size() == 0))
          break;
        throw new PKIXNameConstraintValidatorException("DNS is not from a permitted subtree.");
      }
      str = (String)localIterator.next();
      if (withinDomain(paramString, str))
        break;
    }
    while (!paramString.equalsIgnoreCase(str));
  }

  private void checkPermittedEmail(Set paramSet, String paramString)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet == null)
      return;
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
      {
        if ((paramString.length() == 0) && (paramSet.size() == 0))
          break;
        throw new PKIXNameConstraintValidatorException("Subject email address is not from a permitted subtree.");
      }
    while (!emailIsConstrained(paramString, (String)localIterator.next()));
  }

  private void checkPermittedIP(Set paramSet, byte[] paramArrayOfByte)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet == null)
      return;
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
      {
        if ((paramArrayOfByte.length == 0) && (paramSet.size() == 0))
          break;
        throw new PKIXNameConstraintValidatorException("IP is not from a permitted subtree.");
      }
    while (!isIPConstrained(paramArrayOfByte, (byte[])localIterator.next()));
  }

  private void checkPermittedURI(Set paramSet, String paramString)
    throws PKIXNameConstraintValidatorException
  {
    if (paramSet == null)
      return;
    Iterator localIterator = paramSet.iterator();
    do
      if (!localIterator.hasNext())
      {
        if ((paramString.length() == 0) && (paramSet.size() == 0))
          break;
        throw new PKIXNameConstraintValidatorException("URI is not from a permitted subtree.");
      }
    while (!isUriConstrained(paramString, (String)localIterator.next()));
  }

  private boolean collectionsAreEqual(Collection paramCollection1, Collection paramCollection2)
  {
    if (paramCollection1 == paramCollection2)
      return true;
    if ((paramCollection1 == null) || (paramCollection2 == null))
      return false;
    if (paramCollection1.size() != paramCollection2.size())
      return false;
    Iterator localIterator1 = paramCollection1.iterator();
    label110: 
    while (localIterator1.hasNext())
    {
      Object localObject = localIterator1.next();
      Iterator localIterator2 = paramCollection2.iterator();
      boolean bool = localIterator2.hasNext();
      int i = 0;
      if (!bool);
      while (true)
      {
        if (i != 0)
          break label110;
        return false;
        if (!equals(localObject, localIterator2.next()))
          break;
        i = 1;
      }
    }
  }

  private static int compareTo(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if (Arrays.areEqual(paramArrayOfByte1, paramArrayOfByte2))
      return 0;
    if (Arrays.areEqual(max(paramArrayOfByte1, paramArrayOfByte2), paramArrayOfByte1))
      return 1;
    return -1;
  }

  private boolean emailIsConstrained(String paramString1, String paramString2)
  {
    String str = paramString1.substring(1 + paramString1.indexOf('@'));
    if (paramString2.indexOf('@') != -1)
    {
      if (!paramString1.equalsIgnoreCase(paramString2));
    }
    else
      do
      {
        return true;
        if (paramString2.charAt(0) == '.')
          break;
      }
      while (str.equalsIgnoreCase(paramString2));
    while (!withinDomain(str, paramString2))
      return false;
    return true;
  }

  private boolean equals(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == paramObject2)
      return true;
    if ((paramObject1 == null) || (paramObject2 == null))
      return false;
    if (((paramObject1 instanceof byte[])) && ((paramObject2 instanceof byte[])))
      return Arrays.areEqual((byte[])paramObject1, (byte[])paramObject2);
    return paramObject1.equals(paramObject2);
  }

  private static String extractHostFromURL(String paramString)
  {
    String str1 = paramString.substring(1 + paramString.indexOf(':'));
    if (str1.indexOf("//") != -1)
      str1 = str1.substring(2 + str1.indexOf("//"));
    if (str1.lastIndexOf(':') != -1)
      str1 = str1.substring(0, str1.lastIndexOf(':'));
    String str2 = str1.substring(1 + str1.indexOf(':'));
    String str3 = str2.substring(1 + str2.indexOf('@'));
    if (str3.indexOf('/') != -1)
      str3 = str3.substring(0, str3.indexOf('/'));
    return str3;
  }

  private byte[][] extractIPsAndSubnetMasks(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length / 2;
    byte[] arrayOfByte1 = new byte[i];
    byte[] arrayOfByte2 = new byte[i];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, i);
    System.arraycopy(paramArrayOfByte1, i, arrayOfByte2, 0, i);
    byte[] arrayOfByte3 = new byte[i];
    byte[] arrayOfByte4 = new byte[i];
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte3, 0, i);
    System.arraycopy(paramArrayOfByte2, i, arrayOfByte4, 0, i);
    return new byte[][] { arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4 };
  }

  private String extractNameAsString(GeneralName paramGeneralName)
  {
    return DERIA5String.getInstance(paramGeneralName.getName()).getString();
  }

  private int hashCollection(Collection paramCollection)
  {
    int i;
    if (paramCollection == null)
      i = 0;
    while (true)
    {
      return i;
      i = 0;
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if ((localObject instanceof byte[]))
          i += Arrays.hashCode((byte[])localObject);
        else
          i += localObject.hashCode();
      }
    }
  }

  private Set intersectDN(Set paramSet1, Set paramSet2)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = paramSet2.iterator();
    while (true)
    {
      if (!localIterator1.hasNext())
        return localHashSet;
      ASN1Sequence localASN1Sequence1 = ASN1Sequence.getInstance(((GeneralSubtree)localIterator1.next()).getBase().getName().getDERObject());
      if (paramSet1 == null)
      {
        if (localASN1Sequence1 != null)
          localHashSet.add(localASN1Sequence1);
      }
      else
      {
        Iterator localIterator2 = paramSet1.iterator();
        while (localIterator2.hasNext())
        {
          ASN1Sequence localASN1Sequence2 = (ASN1Sequence)localIterator2.next();
          if (withinDNSubtree(localASN1Sequence1, localASN1Sequence2))
            localHashSet.add(localASN1Sequence1);
          else if (withinDNSubtree(localASN1Sequence2, localASN1Sequence1))
            localHashSet.add(localASN1Sequence2);
        }
      }
    }
  }

  private Set intersectDNS(Set paramSet1, Set paramSet2)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = paramSet2.iterator();
    while (true)
    {
      if (!localIterator1.hasNext())
        return localHashSet;
      String str1 = extractNameAsString(((GeneralSubtree)localIterator1.next()).getBase());
      if (paramSet1 == null)
      {
        if (str1 != null)
          localHashSet.add(str1);
      }
      else
      {
        Iterator localIterator2 = paramSet1.iterator();
        while (localIterator2.hasNext())
        {
          String str2 = (String)localIterator2.next();
          if (withinDomain(str2, str1))
            localHashSet.add(str2);
          else if (withinDomain(str1, str2))
            localHashSet.add(str1);
        }
      }
    }
  }

  private Set intersectEmail(Set paramSet1, Set paramSet2)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = paramSet2.iterator();
    while (true)
    {
      if (!localIterator1.hasNext())
        return localHashSet;
      String str = extractNameAsString(((GeneralSubtree)localIterator1.next()).getBase());
      if (paramSet1 == null)
      {
        if (str != null)
          localHashSet.add(str);
      }
      else
      {
        Iterator localIterator2 = paramSet1.iterator();
        while (localIterator2.hasNext())
          intersectEmail(str, (String)localIterator2.next(), localHashSet);
      }
    }
  }

  private void intersectEmail(String paramString1, String paramString2, Set paramSet)
  {
    String str;
    if (paramString1.indexOf('@') != -1)
    {
      str = paramString1.substring(1 + paramString1.indexOf('@'));
      if (paramString2.indexOf('@') != -1)
        if (paramString1.equalsIgnoreCase(paramString2))
          paramSet.add(paramString1);
    }
    label216: 
    do
    {
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                do
                {
                  do
                  {
                    return;
                    if (!paramString2.startsWith("."))
                      break;
                  }
                  while (!withinDomain(str, paramString2));
                  paramSet.add(paramString1);
                  return;
                }
                while (!str.equalsIgnoreCase(paramString2));
                paramSet.add(paramString1);
                return;
                if (!paramString1.startsWith("."))
                  break label216;
                if (paramString2.indexOf('@') == -1)
                  break;
              }
              while (!withinDomain(paramString2.substring(1 + paramString1.indexOf('@')), paramString1));
              paramSet.add(paramString2);
              return;
              if (!paramString2.startsWith("."))
                break;
              if ((withinDomain(paramString1, paramString2)) || (paramString1.equalsIgnoreCase(paramString2)))
              {
                paramSet.add(paramString1);
                return;
              }
            }
            while (!withinDomain(paramString2, paramString1));
            paramSet.add(paramString2);
            return;
          }
          while (!withinDomain(paramString2, paramString1));
          paramSet.add(paramString2);
          return;
          if (paramString2.indexOf('@') == -1)
            break;
        }
        while (!paramString2.substring(1 + paramString2.indexOf('@')).equalsIgnoreCase(paramString1));
        paramSet.add(paramString2);
        return;
        if (!paramString2.startsWith("."))
          break;
      }
      while (!withinDomain(paramString1, paramString2));
      paramSet.add(paramString1);
      return;
    }
    while (!paramString1.equalsIgnoreCase(paramString2));
    paramSet.add(paramString1);
  }

  private Set intersectIP(Set paramSet1, Set paramSet2)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = paramSet2.iterator();
    while (true)
    {
      if (!localIterator1.hasNext())
        return localHashSet;
      byte[] arrayOfByte = ASN1OctetString.getInstance(((GeneralSubtree)localIterator1.next()).getBase().getName()).getOctets();
      if (paramSet1 == null)
      {
        if (arrayOfByte != null)
          localHashSet.add(arrayOfByte);
      }
      else
      {
        Iterator localIterator2 = paramSet1.iterator();
        while (localIterator2.hasNext())
          localHashSet.addAll(intersectIPRange((byte[])localIterator2.next(), arrayOfByte));
      }
    }
  }

  private Set intersectIPRange(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if (paramArrayOfByte1.length != paramArrayOfByte2.length)
      return Collections.EMPTY_SET;
    byte[][] arrayOfByte1 = extractIPsAndSubnetMasks(paramArrayOfByte1, paramArrayOfByte2);
    byte[] arrayOfByte2 = arrayOfByte1[0];
    byte[] arrayOfByte3 = arrayOfByte1[1];
    byte[] arrayOfByte4 = arrayOfByte1[2];
    byte[] arrayOfByte5 = arrayOfByte1[3];
    byte[][] arrayOfByte6 = minMaxIPs(arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte5);
    byte[] arrayOfByte7 = min(arrayOfByte6[1], arrayOfByte6[3]);
    if (compareTo(max(arrayOfByte6[0], arrayOfByte6[2]), arrayOfByte7) == 1)
      return Collections.EMPTY_SET;
    return Collections.singleton(ipWithSubnetMask(or(arrayOfByte6[0], arrayOfByte6[2]), or(arrayOfByte3, arrayOfByte5)));
  }

  private Set intersectURI(Set paramSet1, Set paramSet2)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = paramSet2.iterator();
    while (true)
    {
      if (!localIterator1.hasNext())
        return localHashSet;
      String str = extractNameAsString(((GeneralSubtree)localIterator1.next()).getBase());
      if (paramSet1 == null)
      {
        if (str != null)
          localHashSet.add(str);
      }
      else
      {
        Iterator localIterator2 = paramSet1.iterator();
        while (localIterator2.hasNext())
          intersectURI((String)localIterator2.next(), str, localHashSet);
      }
    }
  }

  private void intersectURI(String paramString1, String paramString2, Set paramSet)
  {
    String str;
    if (paramString1.indexOf('@') != -1)
    {
      str = paramString1.substring(1 + paramString1.indexOf('@'));
      if (paramString2.indexOf('@') != -1)
        if (paramString1.equalsIgnoreCase(paramString2))
          paramSet.add(paramString1);
    }
    label216: 
    do
    {
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                do
                {
                  do
                  {
                    return;
                    if (!paramString2.startsWith("."))
                      break;
                  }
                  while (!withinDomain(str, paramString2));
                  paramSet.add(paramString1);
                  return;
                }
                while (!str.equalsIgnoreCase(paramString2));
                paramSet.add(paramString1);
                return;
                if (!paramString1.startsWith("."))
                  break label216;
                if (paramString2.indexOf('@') == -1)
                  break;
              }
              while (!withinDomain(paramString2.substring(1 + paramString1.indexOf('@')), paramString1));
              paramSet.add(paramString2);
              return;
              if (!paramString2.startsWith("."))
                break;
              if ((withinDomain(paramString1, paramString2)) || (paramString1.equalsIgnoreCase(paramString2)))
              {
                paramSet.add(paramString1);
                return;
              }
            }
            while (!withinDomain(paramString2, paramString1));
            paramSet.add(paramString2);
            return;
          }
          while (!withinDomain(paramString2, paramString1));
          paramSet.add(paramString2);
          return;
          if (paramString2.indexOf('@') == -1)
            break;
        }
        while (!paramString2.substring(1 + paramString2.indexOf('@')).equalsIgnoreCase(paramString1));
        paramSet.add(paramString2);
        return;
        if (!paramString2.startsWith("."))
          break;
      }
      while (!withinDomain(paramString1, paramString2));
      paramSet.add(paramString1);
      return;
    }
    while (!paramString1.equalsIgnoreCase(paramString2));
    paramSet.add(paramString1);
  }

  private byte[] ipWithSubnetMask(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length;
    byte[] arrayOfByte = new byte[i * 2];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, i);
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, i, i);
    return arrayOfByte;
  }

  private boolean isIPConstrained(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length;
    if (i != paramArrayOfByte2.length / 2)
      return false;
    byte[] arrayOfByte1 = new byte[i];
    System.arraycopy(paramArrayOfByte2, i, arrayOfByte1, 0, i);
    byte[] arrayOfByte2 = new byte[i];
    byte[] arrayOfByte3 = new byte[i];
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return Arrays.areEqual(arrayOfByte2, arrayOfByte3);
      arrayOfByte2[j] = ((byte)(paramArrayOfByte2[j] & arrayOfByte1[j]));
      arrayOfByte3[j] = ((byte)(paramArrayOfByte1[j] & arrayOfByte1[j]));
    }
  }

  private boolean isUriConstrained(String paramString1, String paramString2)
  {
    String str = extractHostFromURL(paramString1);
    if (!paramString2.startsWith("."))
    {
      if (!str.equalsIgnoreCase(paramString2));
    }
    else
      while (withinDomain(str, paramString2))
        return true;
    return false;
  }

  private static byte[] max(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte1.length)
        paramArrayOfByte1 = paramArrayOfByte2;
      while ((0xFFFF & paramArrayOfByte1[i]) > (0xFFFF & paramArrayOfByte2[i]))
        return paramArrayOfByte1;
    }
  }

  private static byte[] min(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte1.length)
        paramArrayOfByte1 = paramArrayOfByte2;
      while ((0xFFFF & paramArrayOfByte1[i]) < (0xFFFF & paramArrayOfByte2[i]))
        return paramArrayOfByte1;
    }
  }

  private byte[][] minMaxIPs(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4)
  {
    int i = paramArrayOfByte1.length;
    byte[] arrayOfByte1 = new byte[i];
    byte[] arrayOfByte2 = new byte[i];
    byte[] arrayOfByte3 = new byte[i];
    byte[] arrayOfByte4 = new byte[i];
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return new byte[][] { arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4 };
      arrayOfByte1[j] = ((byte)(paramArrayOfByte1[j] & paramArrayOfByte2[j]));
      arrayOfByte2[j] = ((byte)(paramArrayOfByte1[j] & paramArrayOfByte2[j] | 0xFFFFFFFF ^ paramArrayOfByte2[j]));
      arrayOfByte3[j] = ((byte)(paramArrayOfByte3[j] & paramArrayOfByte4[j]));
      arrayOfByte4[j] = ((byte)(paramArrayOfByte3[j] & paramArrayOfByte4[j] | 0xFFFFFFFF ^ paramArrayOfByte4[j]));
    }
  }

  private static byte[] or(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte1.length];
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfByte1.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)(paramArrayOfByte1[i] | paramArrayOfByte2[i]));
    }
  }

  private String stringifyIP(byte[] paramArrayOfByte)
  {
    String str1 = "";
    int i = 0;
    String str2;
    if (i >= paramArrayOfByte.length / 2)
      str2 = str1.substring(0, -1 + str1.length()) + "/";
    for (int j = paramArrayOfByte.length / 2; ; j++)
    {
      if (j >= paramArrayOfByte.length)
      {
        return str2.substring(0, -1 + str2.length());
        str1 = str1 + Integer.toString(0xFF & paramArrayOfByte[i]) + ".";
        i++;
        break;
      }
      str2 = str2 + Integer.toString(0xFF & paramArrayOfByte[j]) + ".";
    }
  }

  private String stringifyIPCollection(Set paramSet)
  {
    String str = "" + "[";
    Iterator localIterator = paramSet.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        if (str.length() > 1)
          str = str.substring(0, -1 + str.length());
        return str + "]";
      }
      str = str + stringifyIP((byte[])localIterator.next()) + ",";
    }
  }

  private Set unionDN(Set paramSet, ASN1Sequence paramASN1Sequence)
  {
    if (paramSet.isEmpty())
    {
      if (paramASN1Sequence == null)
        return paramSet;
      paramSet.add(paramASN1Sequence);
      return paramSet;
    }
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramSet.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      ASN1Sequence localASN1Sequence = (ASN1Sequence)localIterator.next();
      if (withinDNSubtree(paramASN1Sequence, localASN1Sequence))
      {
        localHashSet.add(localASN1Sequence);
      }
      else if (withinDNSubtree(localASN1Sequence, paramASN1Sequence))
      {
        localHashSet.add(paramASN1Sequence);
      }
      else
      {
        localHashSet.add(localASN1Sequence);
        localHashSet.add(paramASN1Sequence);
      }
    }
  }

  private Set unionEmail(Set paramSet, String paramString)
  {
    if (paramSet.isEmpty())
    {
      if (paramString == null)
        return paramSet;
      paramSet.add(paramString);
      return paramSet;
    }
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramSet.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      unionEmail((String)localIterator.next(), paramString, localHashSet);
    }
  }

  private void unionEmail(String paramString1, String paramString2, Set paramSet)
  {
    if (paramString1.indexOf('@') != -1)
    {
      String str = paramString1.substring(1 + paramString1.indexOf('@'));
      if (paramString2.indexOf('@') != -1)
      {
        if (paramString1.equalsIgnoreCase(paramString2))
        {
          paramSet.add(paramString1);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (paramString2.startsWith("."))
      {
        if (withinDomain(str, paramString2))
        {
          paramSet.add(paramString2);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (str.equalsIgnoreCase(paramString2))
      {
        paramSet.add(paramString2);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString1.startsWith("."))
    {
      if (paramString2.indexOf('@') != -1)
      {
        if (withinDomain(paramString2.substring(1 + paramString1.indexOf('@')), paramString1))
        {
          paramSet.add(paramString1);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (paramString2.startsWith("."))
      {
        if ((withinDomain(paramString1, paramString2)) || (paramString1.equalsIgnoreCase(paramString2)))
        {
          paramSet.add(paramString2);
          return;
        }
        if (withinDomain(paramString2, paramString1))
        {
          paramSet.add(paramString1);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (withinDomain(paramString2, paramString1))
      {
        paramSet.add(paramString1);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString2.indexOf('@') != -1)
    {
      if (paramString2.substring(1 + paramString1.indexOf('@')).equalsIgnoreCase(paramString1))
      {
        paramSet.add(paramString1);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString2.startsWith("."))
    {
      if (withinDomain(paramString1, paramString2))
      {
        paramSet.add(paramString2);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString1.equalsIgnoreCase(paramString2))
    {
      paramSet.add(paramString1);
      return;
    }
    paramSet.add(paramString1);
    paramSet.add(paramString2);
  }

  private Set unionIP(Set paramSet, byte[] paramArrayOfByte)
  {
    if (paramSet.isEmpty())
    {
      if (paramArrayOfByte == null)
        return paramSet;
      paramSet.add(paramArrayOfByte);
      return paramSet;
    }
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramSet.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      localHashSet.addAll(unionIPRange((byte[])localIterator.next(), paramArrayOfByte));
    }
  }

  private Set unionIPRange(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    HashSet localHashSet = new HashSet();
    if (Arrays.areEqual(paramArrayOfByte1, paramArrayOfByte2))
    {
      localHashSet.add(paramArrayOfByte1);
      return localHashSet;
    }
    localHashSet.add(paramArrayOfByte1);
    localHashSet.add(paramArrayOfByte2);
    return localHashSet;
  }

  private Set unionURI(Set paramSet, String paramString)
  {
    if (paramSet.isEmpty())
    {
      if (paramString == null)
        return paramSet;
      paramSet.add(paramString);
      return paramSet;
    }
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramSet.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      unionURI((String)localIterator.next(), paramString, localHashSet);
    }
  }

  private void unionURI(String paramString1, String paramString2, Set paramSet)
  {
    if (paramString1.indexOf('@') != -1)
    {
      String str = paramString1.substring(1 + paramString1.indexOf('@'));
      if (paramString2.indexOf('@') != -1)
      {
        if (paramString1.equalsIgnoreCase(paramString2))
        {
          paramSet.add(paramString1);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (paramString2.startsWith("."))
      {
        if (withinDomain(str, paramString2))
        {
          paramSet.add(paramString2);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (str.equalsIgnoreCase(paramString2))
      {
        paramSet.add(paramString2);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString1.startsWith("."))
    {
      if (paramString2.indexOf('@') != -1)
      {
        if (withinDomain(paramString2.substring(1 + paramString1.indexOf('@')), paramString1))
        {
          paramSet.add(paramString1);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (paramString2.startsWith("."))
      {
        if ((withinDomain(paramString1, paramString2)) || (paramString1.equalsIgnoreCase(paramString2)))
        {
          paramSet.add(paramString2);
          return;
        }
        if (withinDomain(paramString2, paramString1))
        {
          paramSet.add(paramString1);
          return;
        }
        paramSet.add(paramString1);
        paramSet.add(paramString2);
        return;
      }
      if (withinDomain(paramString2, paramString1))
      {
        paramSet.add(paramString1);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString2.indexOf('@') != -1)
    {
      if (paramString2.substring(1 + paramString1.indexOf('@')).equalsIgnoreCase(paramString1))
      {
        paramSet.add(paramString1);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString2.startsWith("."))
    {
      if (withinDomain(paramString1, paramString2))
      {
        paramSet.add(paramString2);
        return;
      }
      paramSet.add(paramString1);
      paramSet.add(paramString2);
      return;
    }
    if (paramString1.equalsIgnoreCase(paramString2))
    {
      paramSet.add(paramString1);
      return;
    }
    paramSet.add(paramString1);
    paramSet.add(paramString2);
  }

  private static boolean withinDNSubtree(ASN1Sequence paramASN1Sequence1, ASN1Sequence paramASN1Sequence2)
  {
    if (paramASN1Sequence2.size() < 1);
    while (paramASN1Sequence2.size() > paramASN1Sequence1.size())
      return false;
    for (int i = -1 + paramASN1Sequence2.size(); ; i--)
    {
      if (i < 0)
        return true;
      if (!paramASN1Sequence2.getObjectAt(i).equals(paramASN1Sequence1.getObjectAt(i)))
        break;
    }
  }

  private boolean withinDomain(String paramString1, String paramString2)
  {
    String str = paramString2;
    if (str.startsWith("."))
      str = str.substring(1);
    String[] arrayOfString1 = Strings.split(str, '.');
    String[] arrayOfString2 = Strings.split(paramString1, '.');
    if (arrayOfString2.length <= arrayOfString1.length);
    int i;
    int j;
    do
    {
      return false;
      i = arrayOfString2.length - arrayOfString1.length;
      j = -1;
      if (j >= arrayOfString1.length)
        return true;
      if (j != -1)
        break;
    }
    while (arrayOfString2[(j + i)].equals(""));
    while (arrayOfString1[j].equalsIgnoreCase(arrayOfString2[(j + i)]))
    {
      j++;
      break;
    }
    return false;
  }

  public void addExcludedSubtree(GeneralSubtree paramGeneralSubtree)
  {
    GeneralName localGeneralName = paramGeneralSubtree.getBase();
    switch (localGeneralName.getTagNo())
    {
    case 3:
    case 5:
    default:
      return;
    case 1:
      this.excludedSubtreesEmail = unionEmail(this.excludedSubtreesEmail, extractNameAsString(localGeneralName));
      return;
    case 2:
      this.excludedSubtreesDNS = unionDNS(this.excludedSubtreesDNS, extractNameAsString(localGeneralName));
      return;
    case 4:
      this.excludedSubtreesDN = unionDN(this.excludedSubtreesDN, (ASN1Sequence)localGeneralName.getName().getDERObject());
      return;
    case 6:
      this.excludedSubtreesURI = unionURI(this.excludedSubtreesURI, extractNameAsString(localGeneralName));
      return;
    case 7:
    }
    this.excludedSubtreesIP = unionIP(this.excludedSubtreesIP, ASN1OctetString.getInstance(localGeneralName.getName()).getOctets());
  }

  public void checkExcluded(GeneralName paramGeneralName)
    throws PKIXNameConstraintValidatorException
  {
    switch (paramGeneralName.getTagNo())
    {
    case 3:
    case 5:
    default:
      return;
    case 1:
      checkExcludedEmail(this.excludedSubtreesEmail, extractNameAsString(paramGeneralName));
      return;
    case 2:
      checkExcludedDNS(this.excludedSubtreesDNS, DERIA5String.getInstance(paramGeneralName.getName()).getString());
      return;
    case 4:
      checkExcludedDN(ASN1Sequence.getInstance(paramGeneralName.getName().getDERObject()));
      return;
    case 6:
      checkExcludedURI(this.excludedSubtreesURI, DERIA5String.getInstance(paramGeneralName.getName()).getString());
      return;
    case 7:
    }
    byte[] arrayOfByte = ASN1OctetString.getInstance(paramGeneralName.getName()).getOctets();
    checkExcludedIP(this.excludedSubtreesIP, arrayOfByte);
  }

  public void checkExcludedDN(ASN1Sequence paramASN1Sequence)
    throws PKIXNameConstraintValidatorException
  {
    checkExcludedDN(this.excludedSubtreesDN, paramASN1Sequence);
  }

  public void checkPermitted(GeneralName paramGeneralName)
    throws PKIXNameConstraintValidatorException
  {
    switch (paramGeneralName.getTagNo())
    {
    case 3:
    case 5:
    default:
      return;
    case 1:
      checkPermittedEmail(this.permittedSubtreesEmail, extractNameAsString(paramGeneralName));
      return;
    case 2:
      checkPermittedDNS(this.permittedSubtreesDNS, DERIA5String.getInstance(paramGeneralName.getName()).getString());
      return;
    case 4:
      checkPermittedDN(ASN1Sequence.getInstance(paramGeneralName.getName().getDERObject()));
      return;
    case 6:
      checkPermittedURI(this.permittedSubtreesURI, DERIA5String.getInstance(paramGeneralName.getName()).getString());
      return;
    case 7:
    }
    byte[] arrayOfByte = ASN1OctetString.getInstance(paramGeneralName.getName()).getOctets();
    checkPermittedIP(this.permittedSubtreesIP, arrayOfByte);
  }

  public void checkPermittedDN(ASN1Sequence paramASN1Sequence)
    throws PKIXNameConstraintValidatorException
  {
    checkPermittedDN(this.permittedSubtreesDN, paramASN1Sequence);
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof PKIXNameConstraintValidator));
    PKIXNameConstraintValidator localPKIXNameConstraintValidator;
    do
    {
      return false;
      localPKIXNameConstraintValidator = (PKIXNameConstraintValidator)paramObject;
    }
    while ((!collectionsAreEqual(localPKIXNameConstraintValidator.excludedSubtreesDN, this.excludedSubtreesDN)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.excludedSubtreesDNS, this.excludedSubtreesDNS)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.excludedSubtreesEmail, this.excludedSubtreesEmail)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.excludedSubtreesIP, this.excludedSubtreesIP)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.excludedSubtreesURI, this.excludedSubtreesURI)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.permittedSubtreesDN, this.permittedSubtreesDN)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.permittedSubtreesDNS, this.permittedSubtreesDNS)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.permittedSubtreesEmail, this.permittedSubtreesEmail)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.permittedSubtreesIP, this.permittedSubtreesIP)) || (!collectionsAreEqual(localPKIXNameConstraintValidator.permittedSubtreesURI, this.permittedSubtreesURI)));
    return true;
  }

  public int hashCode()
  {
    return hashCollection(this.excludedSubtreesDN) + hashCollection(this.excludedSubtreesDNS) + hashCollection(this.excludedSubtreesEmail) + hashCollection(this.excludedSubtreesIP) + hashCollection(this.excludedSubtreesURI) + hashCollection(this.permittedSubtreesDN) + hashCollection(this.permittedSubtreesDNS) + hashCollection(this.permittedSubtreesEmail) + hashCollection(this.permittedSubtreesIP) + hashCollection(this.permittedSubtreesURI);
  }

  public void intersectEmptyPermittedSubtree(int paramInt)
  {
    switch (paramInt)
    {
    case 3:
    case 5:
    default:
      return;
    case 1:
      this.permittedSubtreesEmail = new HashSet();
      return;
    case 2:
      this.permittedSubtreesDNS = new HashSet();
      return;
    case 4:
      this.permittedSubtreesDN = new HashSet();
      return;
    case 6:
      this.permittedSubtreesURI = new HashSet();
      return;
    case 7:
    }
    this.permittedSubtreesIP = new HashSet();
  }

  public void intersectPermittedSubtree(ASN1Sequence paramASN1Sequence)
  {
    HashMap localHashMap = new HashMap();
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    Iterator localIterator;
    if (!localEnumeration.hasMoreElements())
      localIterator = localHashMap.entrySet().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        return;
        GeneralSubtree localGeneralSubtree = GeneralSubtree.getInstance(localEnumeration.nextElement());
        Integer localInteger = new Integer(localGeneralSubtree.getBase().getTagNo());
        if (localHashMap.get(localInteger) == null)
          localHashMap.put(localInteger, new HashSet());
        ((Set)localHashMap.get(localInteger)).add(localGeneralSubtree);
        break;
      }
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      switch (((Integer)localEntry.getKey()).intValue())
      {
      case 3:
      case 5:
      default:
        break;
      case 1:
        this.permittedSubtreesEmail = intersectEmail(this.permittedSubtreesEmail, (Set)localEntry.getValue());
        break;
      case 2:
        this.permittedSubtreesDNS = intersectDNS(this.permittedSubtreesDNS, (Set)localEntry.getValue());
        break;
      case 4:
        this.permittedSubtreesDN = intersectDN(this.permittedSubtreesDN, (Set)localEntry.getValue());
        break;
      case 6:
        this.permittedSubtreesURI = intersectURI(this.permittedSubtreesURI, (Set)localEntry.getValue());
        break;
      case 7:
        this.permittedSubtreesIP = intersectIP(this.permittedSubtreesIP, (Set)localEntry.getValue());
      }
    }
  }

  public String toString()
  {
    String str1 = "" + "permitted:\n";
    if (this.permittedSubtreesDN != null)
      str1 = new StringBuilder(String.valueOf(str1)).append("DN:\n").toString() + this.permittedSubtreesDN.toString() + "\n";
    if (this.permittedSubtreesDNS != null)
      str1 = new StringBuilder(String.valueOf(str1)).append("DNS:\n").toString() + this.permittedSubtreesDNS.toString() + "\n";
    if (this.permittedSubtreesEmail != null)
      str1 = new StringBuilder(String.valueOf(str1)).append("Email:\n").toString() + this.permittedSubtreesEmail.toString() + "\n";
    if (this.permittedSubtreesURI != null)
      str1 = new StringBuilder(String.valueOf(str1)).append("URI:\n").toString() + this.permittedSubtreesURI.toString() + "\n";
    if (this.permittedSubtreesIP != null)
      str1 = new StringBuilder(String.valueOf(str1)).append("IP:\n").toString() + stringifyIPCollection(this.permittedSubtreesIP) + "\n";
    String str2 = str1 + "excluded:\n";
    if (!this.excludedSubtreesDN.isEmpty())
      str2 = new StringBuilder(String.valueOf(str2)).append("DN:\n").toString() + this.excludedSubtreesDN.toString() + "\n";
    if (!this.excludedSubtreesDNS.isEmpty())
      str2 = new StringBuilder(String.valueOf(str2)).append("DNS:\n").toString() + this.excludedSubtreesDNS.toString() + "\n";
    if (!this.excludedSubtreesEmail.isEmpty())
      str2 = new StringBuilder(String.valueOf(str2)).append("Email:\n").toString() + this.excludedSubtreesEmail.toString() + "\n";
    if (!this.excludedSubtreesURI.isEmpty())
      str2 = new StringBuilder(String.valueOf(str2)).append("URI:\n").toString() + this.excludedSubtreesURI.toString() + "\n";
    if (!this.excludedSubtreesIP.isEmpty())
      str2 = new StringBuilder(String.valueOf(str2)).append("IP:\n").toString() + stringifyIPCollection(this.excludedSubtreesIP) + "\n";
    return str2;
  }

  protected Set unionDNS(Set paramSet, String paramString)
  {
    if (paramSet.isEmpty())
    {
      if (paramString == null)
        return paramSet;
      paramSet.add(paramString);
      return paramSet;
    }
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramSet.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return localHashSet;
      String str = (String)localIterator.next();
      if (withinDomain(str, paramString))
      {
        localHashSet.add(paramString);
      }
      else if (withinDomain(paramString, str))
      {
        localHashSet.add(str);
      }
      else
      {
        localHashSet.add(str);
        localHashSet.add(paramString);
      }
    }
  }
}