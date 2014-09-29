package org.bouncycastle2.jce;

import java.util.Enumeration;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.jce.spec.ECNamedCurveParameterSpec;

public class ECGOST3410NamedCurveTable
{
  public static Enumeration getNames()
  {
    return ECGOST3410NamedCurves.getNames();
  }

  public static ECNamedCurveParameterSpec getParameterSpec(String paramString)
  {
    Object localObject = ECGOST3410NamedCurves.getByName(paramString);
    if (localObject == null);
    try
    {
      ECDomainParameters localECDomainParameters = ECGOST3410NamedCurves.getByOID(new DERObjectIdentifier(paramString));
      localObject = localECDomainParameters;
      if (localObject == null)
        return null;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      return null;
    }
    return new ECNamedCurveParameterSpec(paramString, ((ECDomainParameters)localObject).getCurve(), ((ECDomainParameters)localObject).getG(), ((ECDomainParameters)localObject).getN(), ((ECDomainParameters)localObject).getH(), ((ECDomainParameters)localObject).getSeed());
  }
}