package org.bouncycastle2.jce;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.nist.NISTNamedCurves;
import org.bouncycastle2.asn1.sec.SECNamedCurves;
import org.bouncycastle2.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle2.asn1.x9.X962NamedCurves;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.jce.spec.ECNamedCurveParameterSpec;

public class ECNamedCurveTable
{
  private static void addEnumeration(Vector paramVector, Enumeration paramEnumeration)
  {
    while (true)
    {
      if (!paramEnumeration.hasMoreElements())
        return;
      paramVector.addElement(paramEnumeration.nextElement());
    }
  }

  public static Enumeration getNames()
  {
    Vector localVector = new Vector();
    addEnumeration(localVector, X962NamedCurves.getNames());
    addEnumeration(localVector, SECNamedCurves.getNames());
    addEnumeration(localVector, NISTNamedCurves.getNames());
    addEnumeration(localVector, TeleTrusTNamedCurves.getNames());
    return localVector.elements();
  }

  public static ECNamedCurveParameterSpec getParameterSpec(String paramString)
  {
    Object localObject = X962NamedCurves.getByName(paramString);
    if (localObject == null);
    try
    {
      X9ECParameters localX9ECParameters3 = X962NamedCurves.getByOID(new DERObjectIdentifier(paramString));
      localObject = localX9ECParameters3;
      label25: if (localObject == null)
      {
        localObject = SECNamedCurves.getByName(paramString);
        if (localObject != null);
      }
      try
      {
        X9ECParameters localX9ECParameters2 = SECNamedCurves.getByOID(new DERObjectIdentifier(paramString));
        localObject = localX9ECParameters2;
        label54: if (localObject == null)
        {
          localObject = TeleTrusTNamedCurves.getByName(paramString);
          if (localObject != null);
        }
        try
        {
          X9ECParameters localX9ECParameters1 = TeleTrusTNamedCurves.getByOID(new DERObjectIdentifier(paramString));
          localObject = localX9ECParameters1;
          label81: if (localObject == null)
            localObject = NISTNamedCurves.getByName(paramString);
          if (localObject == null)
            return null;
          return new ECNamedCurveParameterSpec(paramString, ((X9ECParameters)localObject).getCurve(), ((X9ECParameters)localObject).getG(), ((X9ECParameters)localObject).getN(), ((X9ECParameters)localObject).getH(), ((X9ECParameters)localObject).getSeed());
        }
        catch (IllegalArgumentException localIllegalArgumentException1)
        {
          break label81;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException2)
      {
        break label54;
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException3)
    {
      break label25;
    }
  }
}