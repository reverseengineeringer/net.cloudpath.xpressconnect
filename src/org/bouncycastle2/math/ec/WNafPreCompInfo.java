package org.bouncycastle2.math.ec;

class WNafPreCompInfo
  implements PreCompInfo
{
  private ECPoint[] preComp = null;
  private ECPoint twiceP = null;

  protected ECPoint[] getPreComp()
  {
    return this.preComp;
  }

  protected ECPoint getTwiceP()
  {
    return this.twiceP;
  }

  protected void setPreComp(ECPoint[] paramArrayOfECPoint)
  {
    this.preComp = paramArrayOfECPoint;
  }

  protected void setTwiceP(ECPoint paramECPoint)
  {
    this.twiceP = paramECPoint;
  }
}