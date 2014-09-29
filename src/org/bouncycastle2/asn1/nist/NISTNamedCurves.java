package org.bouncycastle2.asn1.nist;

import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.sec.SECNamedCurves;
import org.bouncycastle2.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.util.Strings;

public class NISTNamedCurves
{
  static final Hashtable names;
  static final Hashtable objIds = new Hashtable();

  static
  {
    names = new Hashtable();
    defineCurve("B-571", SECObjectIdentifiers.sect571r1);
    defineCurve("B-409", SECObjectIdentifiers.sect409r1);
    defineCurve("B-283", SECObjectIdentifiers.sect283r1);
    defineCurve("B-233", SECObjectIdentifiers.sect233r1);
    defineCurve("B-163", SECObjectIdentifiers.sect163r2);
    defineCurve("P-521", SECObjectIdentifiers.secp521r1);
    defineCurve("P-384", SECObjectIdentifiers.secp384r1);
    defineCurve("P-256", SECObjectIdentifiers.secp256r1);
    defineCurve("P-224", SECObjectIdentifiers.secp224r1);
    defineCurve("P-192", SECObjectIdentifiers.secp192r1);
  }

  static void defineCurve(String paramString, DERObjectIdentifier paramDERObjectIdentifier)
  {
    objIds.put(paramString, paramDERObjectIdentifier);
    names.put(paramDERObjectIdentifier, paramString);
  }

  public static X9ECParameters getByName(String paramString)
  {
    DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)objIds.get(Strings.toUpperCase(paramString));
    if (localDERObjectIdentifier != null)
      return getByOID(localDERObjectIdentifier);
    return null;
  }

  public static X9ECParameters getByOID(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return SECNamedCurves.getByOID(paramDERObjectIdentifier);
  }

  public static String getName(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return (String)names.get(paramDERObjectIdentifier);
  }

  public static Enumeration getNames()
  {
    return objIds.keys();
  }

  public static DERObjectIdentifier getOID(String paramString)
  {
    return (DERObjectIdentifier)objIds.get(Strings.toUpperCase(paramString));
  }
}