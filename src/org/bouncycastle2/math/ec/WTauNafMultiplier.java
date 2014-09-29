package org.bouncycastle2.math.ec;

import java.math.BigInteger;

class WTauNafMultiplier
  implements ECMultiplier
{
  private static ECPoint.F2m multiplyFromWTnaf(ECPoint.F2m paramF2m, byte[] paramArrayOfByte, PreCompInfo paramPreCompInfo)
  {
    byte b = ((ECCurve.F2m)paramF2m.getCurve()).getA().toBigInteger().byteValue();
    ECPoint.F2m[] arrayOfF2m;
    if ((paramPreCompInfo == null) || (!(paramPreCompInfo instanceof WTauNafPreCompInfo)))
    {
      arrayOfF2m = Tnaf.getPreComp(paramF2m, b);
      paramF2m.setPreCompInfo(new WTauNafPreCompInfo(arrayOfF2m));
    }
    int i;
    while (true)
    {
      localF2m = (ECPoint.F2m)paramF2m.getCurve().getInfinity();
      i = -1 + paramArrayOfByte.length;
      if (i >= 0)
        break;
      return localF2m;
      arrayOfF2m = ((WTauNafPreCompInfo)paramPreCompInfo).getPreComp();
    }
    ECPoint.F2m localF2m = Tnaf.tau(localF2m);
    if (paramArrayOfByte[i] != 0)
      if (paramArrayOfByte[i] <= 0)
        break label127;
    label127: for (localF2m = localF2m.addSimple(arrayOfF2m[paramArrayOfByte[i]]); ; localF2m = localF2m.subtractSimple(arrayOfF2m[(-paramArrayOfByte[i])]))
    {
      i--;
      break;
    }
  }

  private ECPoint.F2m multiplyWTnaf(ECPoint.F2m paramF2m, ZTauElement paramZTauElement, PreCompInfo paramPreCompInfo, byte paramByte1, byte paramByte2)
  {
    if (paramByte1 == 0);
    for (ZTauElement[] arrayOfZTauElement = Tnaf.alpha0; ; arrayOfZTauElement = Tnaf.alpha1)
    {
      BigInteger localBigInteger = Tnaf.getTw(paramByte2, 4);
      return multiplyFromWTnaf(paramF2m, Tnaf.tauAdicWNaf(paramByte2, paramZTauElement, (byte)4, BigInteger.valueOf(16L), localBigInteger, arrayOfZTauElement), paramPreCompInfo);
    }
  }

  public ECPoint multiply(ECPoint paramECPoint, BigInteger paramBigInteger, PreCompInfo paramPreCompInfo)
  {
    if (!(paramECPoint instanceof ECPoint.F2m))
      throw new IllegalArgumentException("Only ECPoint.F2m can be used in WTauNafMultiplier");
    ECPoint.F2m localF2m = (ECPoint.F2m)paramECPoint;
    ECCurve.F2m localF2m1 = (ECCurve.F2m)localF2m.getCurve();
    int i = localF2m1.getM();
    byte b1 = localF2m1.getA().toBigInteger().byteValue();
    byte b2 = localF2m1.getMu();
    return multiplyWTnaf(localF2m, Tnaf.partModReduction(paramBigInteger, i, b1, localF2m1.getSi(), b2, (byte)10), paramPreCompInfo, b1, b2);
  }
}