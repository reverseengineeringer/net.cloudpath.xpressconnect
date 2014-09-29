package org.bouncycastle2.asn1.x509;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERPrintableString;
import org.bouncycastle2.util.Strings;

public abstract class X509NameEntryConverter
{
  protected boolean canBePrintable(String paramString)
  {
    return DERPrintableString.isPrintableString(paramString);
  }

  protected DERObject convertHexEncoded(String paramString, int paramInt)
    throws IOException
  {
    String str = Strings.toLowerCase(paramString);
    byte[] arrayOfByte = new byte[(str.length() - paramInt) / 2];
    int i = 0;
    if (i == arrayOfByte.length)
      return new ASN1InputStream(arrayOfByte).readObject();
    int j = str.charAt(paramInt + i * 2);
    int k = str.charAt(1 + (paramInt + i * 2));
    if (j < 97)
    {
      arrayOfByte[i] = ((byte)(j - 48 << 4));
      label87: if (k >= 97)
        break label137;
      arrayOfByte[i] = ((byte)(arrayOfByte[i] | (byte)(k - 48)));
    }
    while (true)
    {
      i++;
      break;
      arrayOfByte[i] = ((byte)(10 + (j - 97) << 4));
      break label87;
      label137: arrayOfByte[i] = ((byte)(arrayOfByte[i] | (byte)(10 + (k - 97))));
    }
  }

  public abstract DERObject getConvertedValue(DERObjectIdentifier paramDERObjectIdentifier, String paramString);
}