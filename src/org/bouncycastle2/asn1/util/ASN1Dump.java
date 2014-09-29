package org.bouncycastle2.asn1.util;

import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.BERApplicationSpecific;
import org.bouncycastle2.asn1.BERConstructedOctetString;
import org.bouncycastle2.asn1.BERSequence;
import org.bouncycastle2.asn1.BERSet;
import org.bouncycastle2.asn1.BERTaggedObject;
import org.bouncycastle2.asn1.DERApplicationSpecific;
import org.bouncycastle2.asn1.DERBMPString;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERBoolean;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DEREnumerated;
import org.bouncycastle2.asn1.DERExternal;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.DERT61String;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.DERUTCTime;
import org.bouncycastle2.asn1.DERUTF8String;
import org.bouncycastle2.asn1.DERUnknownTag;
import org.bouncycastle2.asn1.DERVisibleString;
import org.bouncycastle2.util.encoders.Hex;

public class ASN1Dump
{
  private static final int SAMPLE_SIZE = 32;
  private static final String TAB = "    ";

  static void _dumpAsString(String paramString, boolean paramBoolean, DERObject paramDERObject, StringBuffer paramStringBuffer)
  {
    String str1 = System.getProperty("line.separator");
    Enumeration localEnumeration3;
    String str6;
    if ((paramDERObject instanceof ASN1Sequence))
    {
      localEnumeration3 = ((ASN1Sequence)paramDERObject).getObjects();
      str6 = paramString + "    ";
      paramStringBuffer.append(paramString);
      if ((paramDERObject instanceof BERSequence))
      {
        paramStringBuffer.append("BER Sequence");
        paramStringBuffer.append(str1);
        label71: if (localEnumeration3.hasMoreElements())
          break label109;
      }
    }
    while (true)
    {
      return;
      if ((paramDERObject instanceof DERSequence))
      {
        paramStringBuffer.append("DER Sequence");
        break;
      }
      paramStringBuffer.append("Sequence");
      break;
      label109: Object localObject3 = localEnumeration3.nextElement();
      if ((localObject3 == null) || (localObject3.equals(new DERNull())))
      {
        paramStringBuffer.append(str6);
        paramStringBuffer.append("NULL");
        paramStringBuffer.append(str1);
        break label71;
      }
      if ((localObject3 instanceof DERObject))
      {
        _dumpAsString(str6, paramBoolean, (DERObject)localObject3, paramStringBuffer);
        break label71;
      }
      _dumpAsString(str6, paramBoolean, ((DEREncodable)localObject3).getDERObject(), paramStringBuffer);
      break label71;
      if ((paramDERObject instanceof DERTaggedObject))
      {
        String str5 = paramString + "    ";
        paramStringBuffer.append(paramString);
        if ((paramDERObject instanceof BERTaggedObject))
          paramStringBuffer.append("BER Tagged [");
        DERTaggedObject localDERTaggedObject;
        while (true)
        {
          localDERTaggedObject = (DERTaggedObject)paramDERObject;
          paramStringBuffer.append(Integer.toString(localDERTaggedObject.getTagNo()));
          paramStringBuffer.append(']');
          if (!localDERTaggedObject.isExplicit())
            paramStringBuffer.append(" IMPLICIT ");
          paramStringBuffer.append(str1);
          if (!localDERTaggedObject.isEmpty())
            break;
          paramStringBuffer.append(str5);
          paramStringBuffer.append("EMPTY");
          paramStringBuffer.append(str1);
          return;
          paramStringBuffer.append("Tagged [");
        }
        _dumpAsString(str5, paramBoolean, localDERTaggedObject.getObject(), paramStringBuffer);
        return;
      }
      if ((paramDERObject instanceof BERSet))
      {
        Enumeration localEnumeration2 = ((ASN1Set)paramDERObject).getObjects();
        String str4 = paramString + "    ";
        paramStringBuffer.append(paramString);
        paramStringBuffer.append("BER Set");
        paramStringBuffer.append(str1);
        while (localEnumeration2.hasMoreElements())
        {
          Object localObject2 = localEnumeration2.nextElement();
          if (localObject2 == null)
          {
            paramStringBuffer.append(str4);
            paramStringBuffer.append("NULL");
            paramStringBuffer.append(str1);
          }
          else if ((localObject2 instanceof DERObject))
          {
            _dumpAsString(str4, paramBoolean, (DERObject)localObject2, paramStringBuffer);
          }
          else
          {
            _dumpAsString(str4, paramBoolean, ((DEREncodable)localObject2).getDERObject(), paramStringBuffer);
          }
        }
      }
      else
      {
        if (!(paramDERObject instanceof DERSet))
          break label650;
        Enumeration localEnumeration1 = ((ASN1Set)paramDERObject).getObjects();
        String str3 = paramString + "    ";
        paramStringBuffer.append(paramString);
        paramStringBuffer.append("DER Set");
        paramStringBuffer.append(str1);
        while (localEnumeration1.hasMoreElements())
        {
          Object localObject1 = localEnumeration1.nextElement();
          if (localObject1 == null)
          {
            paramStringBuffer.append(str3);
            paramStringBuffer.append("NULL");
            paramStringBuffer.append(str1);
          }
          else if ((localObject1 instanceof DERObject))
          {
            _dumpAsString(str3, paramBoolean, (DERObject)localObject1, paramStringBuffer);
          }
          else
          {
            _dumpAsString(str3, paramBoolean, ((DEREncodable)localObject1).getDERObject(), paramStringBuffer);
          }
        }
      }
    }
    label650: if ((paramDERObject instanceof DERObjectIdentifier))
    {
      paramStringBuffer.append(paramString + "ObjectIdentifier(" + ((DERObjectIdentifier)paramDERObject).getId() + ")" + str1);
      return;
    }
    if ((paramDERObject instanceof DERBoolean))
    {
      paramStringBuffer.append(paramString + "Boolean(" + ((DERBoolean)paramDERObject).isTrue() + ")" + str1);
      return;
    }
    if ((paramDERObject instanceof DERInteger))
    {
      paramStringBuffer.append(paramString + "Integer(" + ((DERInteger)paramDERObject).getValue() + ")" + str1);
      return;
    }
    if ((paramDERObject instanceof BERConstructedOctetString))
    {
      ASN1OctetString localASN1OctetString2 = (ASN1OctetString)paramDERObject;
      paramStringBuffer.append(paramString + "BER Constructed Octet String" + "[" + localASN1OctetString2.getOctets().length + "] ");
      if (paramBoolean)
      {
        paramStringBuffer.append(dumpBinaryDataAsString(paramString, localASN1OctetString2.getOctets()));
        return;
      }
      paramStringBuffer.append(str1);
      return;
    }
    if ((paramDERObject instanceof DEROctetString))
    {
      ASN1OctetString localASN1OctetString1 = (ASN1OctetString)paramDERObject;
      paramStringBuffer.append(paramString + "DER Octet String" + "[" + localASN1OctetString1.getOctets().length + "] ");
      if (paramBoolean)
      {
        paramStringBuffer.append(dumpBinaryDataAsString(paramString, localASN1OctetString1.getOctets()));
        return;
      }
      paramStringBuffer.append(str1);
      return;
    }
    if ((paramDERObject instanceof DERBitString))
    {
      DERBitString localDERBitString = (DERBitString)paramDERObject;
      paramStringBuffer.append(paramString + "DER Bit String" + "[" + localDERBitString.getBytes().length + ", " + localDERBitString.getPadBits() + "] ");
      if (paramBoolean)
      {
        paramStringBuffer.append(dumpBinaryDataAsString(paramString, localDERBitString.getBytes()));
        return;
      }
      paramStringBuffer.append(str1);
      return;
    }
    if ((paramDERObject instanceof DERIA5String))
    {
      paramStringBuffer.append(paramString + "IA5String(" + ((DERIA5String)paramDERObject).getString() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERUTF8String))
    {
      paramStringBuffer.append(paramString + "UTF8String(" + ((DERUTF8String)paramDERObject).getString() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERPrintableString))
    {
      paramStringBuffer.append(paramString + "PrintableString(" + ((DERPrintableString)paramDERObject).getString() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERVisibleString))
    {
      paramStringBuffer.append(paramString + "VisibleString(" + ((DERVisibleString)paramDERObject).getString() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERBMPString))
    {
      paramStringBuffer.append(paramString + "BMPString(" + ((DERBMPString)paramDERObject).getString() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERT61String))
    {
      paramStringBuffer.append(paramString + "T61String(" + ((DERT61String)paramDERObject).getString() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERUTCTime))
    {
      paramStringBuffer.append(paramString + "UTCTime(" + ((DERUTCTime)paramDERObject).getTime() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERGeneralizedTime))
    {
      paramStringBuffer.append(paramString + "GeneralizedTime(" + ((DERGeneralizedTime)paramDERObject).getTime() + ") " + str1);
      return;
    }
    if ((paramDERObject instanceof DERUnknownTag))
    {
      paramStringBuffer.append(paramString + "Unknown " + Integer.toString(((DERUnknownTag)paramDERObject).getTag(), 16) + " " + new String(Hex.encode(((DERUnknownTag)paramDERObject).getData())) + str1);
      return;
    }
    if ((paramDERObject instanceof BERApplicationSpecific))
    {
      paramStringBuffer.append(outputApplicationSpecific("BER", paramString, paramBoolean, paramDERObject, str1));
      return;
    }
    if ((paramDERObject instanceof DERApplicationSpecific))
    {
      paramStringBuffer.append(outputApplicationSpecific("DER", paramString, paramBoolean, paramDERObject, str1));
      return;
    }
    if ((paramDERObject instanceof DEREnumerated))
    {
      DEREnumerated localDEREnumerated = (DEREnumerated)paramDERObject;
      paramStringBuffer.append(paramString + "DER Enumerated(" + localDEREnumerated.getValue() + ")" + str1);
      return;
    }
    if ((paramDERObject instanceof DERExternal))
    {
      DERExternal localDERExternal = (DERExternal)paramDERObject;
      paramStringBuffer.append(paramString + "External " + str1);
      String str2 = paramString + "    ";
      if (localDERExternal.getDirectReference() != null)
        paramStringBuffer.append(str2 + "Direct Reference: " + localDERExternal.getDirectReference().getId() + str1);
      if (localDERExternal.getIndirectReference() != null)
        paramStringBuffer.append(str2 + "Indirect Reference: " + localDERExternal.getIndirectReference().toString() + str1);
      if (localDERExternal.getDataValueDescriptor() != null)
        _dumpAsString(str2, paramBoolean, localDERExternal.getDataValueDescriptor(), paramStringBuffer);
      paramStringBuffer.append(str2 + "Encoding: " + localDERExternal.getEncoding() + str1);
      _dumpAsString(str2, paramBoolean, localDERExternal.getExternalContent(), paramStringBuffer);
      return;
    }
    paramStringBuffer.append(paramString + paramDERObject.toString() + str1);
  }

  private static String calculateAscString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = paramInt1; ; i++)
    {
      if (i == paramInt1 + paramInt2)
        return localStringBuffer.toString();
      if ((paramArrayOfByte[i] >= 32) && (paramArrayOfByte[i] <= 126))
        localStringBuffer.append((char)paramArrayOfByte[i]);
    }
  }

  public static String dumpAsString(Object paramObject)
  {
    return dumpAsString(paramObject, false);
  }

  public static String dumpAsString(Object paramObject, boolean paramBoolean)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if ((paramObject instanceof DERObject))
      _dumpAsString("", paramBoolean, (DERObject)paramObject, localStringBuffer);
    while (true)
    {
      return localStringBuffer.toString();
      if (!(paramObject instanceof DEREncodable))
        break;
      _dumpAsString("", paramBoolean, ((DEREncodable)paramObject).getDERObject(), localStringBuffer);
    }
    return "unknown object type " + paramObject.toString();
  }

  private static String dumpBinaryDataAsString(String paramString, byte[] paramArrayOfByte)
  {
    String str1 = System.getProperty("line.separator");
    StringBuffer localStringBuffer = new StringBuffer();
    String str2 = paramString + "    ";
    localStringBuffer.append(str1);
    for (int i = 0; ; i += 32)
    {
      if (i >= paramArrayOfByte.length)
        return localStringBuffer.toString();
      if (paramArrayOfByte.length - i <= 32)
        break;
      localStringBuffer.append(str2);
      localStringBuffer.append(new String(Hex.encode(paramArrayOfByte, i, 32)));
      localStringBuffer.append("    ");
      localStringBuffer.append(calculateAscString(paramArrayOfByte, i, 32));
      localStringBuffer.append(str1);
    }
    localStringBuffer.append(str2);
    localStringBuffer.append(new String(Hex.encode(paramArrayOfByte, i, paramArrayOfByte.length - i)));
    for (int j = paramArrayOfByte.length - i; ; j++)
    {
      if (j == 32)
      {
        localStringBuffer.append("    ");
        localStringBuffer.append(calculateAscString(paramArrayOfByte, i, paramArrayOfByte.length - i));
        localStringBuffer.append(str1);
        break;
      }
      localStringBuffer.append("  ");
    }
  }

  private static String outputApplicationSpecific(String paramString1, String paramString2, boolean paramBoolean, DERObject paramDERObject, String paramString3)
  {
    DERApplicationSpecific localDERApplicationSpecific = (DERApplicationSpecific)paramDERObject;
    StringBuffer localStringBuffer = new StringBuffer();
    if (localDERApplicationSpecific.isConstructed())
      try
      {
        ASN1Sequence localASN1Sequence = ASN1Sequence.getInstance(localDERApplicationSpecific.getObject(16));
        localStringBuffer.append(paramString2 + paramString1 + " ApplicationSpecific[" + localDERApplicationSpecific.getApplicationTag() + "]" + paramString3);
        Enumeration localEnumeration = localASN1Sequence.getObjects();
        while (true)
        {
          boolean bool = localEnumeration.hasMoreElements();
          if (!bool)
            return localStringBuffer.toString();
          _dumpAsString(paramString2 + "    ", paramBoolean, (DERObject)localEnumeration.nextElement(), localStringBuffer);
        }
      }
      catch (IOException localIOException)
      {
        while (true)
          localStringBuffer.append(localIOException);
      }
    return paramString2 + paramString1 + " ApplicationSpecific[" + localDERApplicationSpecific.getApplicationTag() + "] (" + new String(Hex.encode(localDERApplicationSpecific.getContents())) + ")" + paramString3;
  }
}