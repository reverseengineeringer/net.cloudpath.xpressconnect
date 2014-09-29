package org.bouncycastle2.jce.provider;

class ReasonsMask
{
  static final ReasonsMask allReasons = new ReasonsMask(33023);
  private int _reasons;

  ReasonsMask()
  {
    this(0);
  }

  ReasonsMask(int paramInt)
  {
    this._reasons = paramInt;
  }

  void addReasons(ReasonsMask paramReasonsMask)
  {
    this._reasons |= paramReasonsMask.getReasons();
  }

  int getReasons()
  {
    return this._reasons;
  }

  boolean hasNewReasons(ReasonsMask paramReasonsMask)
  {
    return (this._reasons | paramReasonsMask.getReasons() ^ this._reasons) != 0;
  }

  ReasonsMask intersect(ReasonsMask paramReasonsMask)
  {
    ReasonsMask localReasonsMask = new ReasonsMask();
    localReasonsMask.addReasons(new ReasonsMask(this._reasons & paramReasonsMask.getReasons()));
    return localReasonsMask;
  }

  boolean isAllReasons()
  {
    return this._reasons == allReasons._reasons;
  }
}