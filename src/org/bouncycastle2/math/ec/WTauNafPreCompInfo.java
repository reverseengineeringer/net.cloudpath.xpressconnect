package org.bouncycastle2.math.ec;

class WTauNafPreCompInfo
  implements PreCompInfo
{
  private ECPoint.F2m[] preComp = null;

  WTauNafPreCompInfo(ECPoint.F2m[] paramArrayOfF2m)
  {
    this.preComp = paramArrayOfF2m;
  }

  protected ECPoint.F2m[] getPreComp()
  {
    return this.preComp;
  }
}