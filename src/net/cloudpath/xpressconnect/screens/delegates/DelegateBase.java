package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Context;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.screens.ScreenBase;

public class DelegateBase
{
  protected ScreenBase mActivityParent = null;
  protected Context mContext = null;
  public int mDialogShown = -1;
  protected FailureReason mFailure = null;
  protected GetGlobals mGlobals = null;
  protected Logger mLogger = null;
  protected ParcelHelper mParcelHelper = null;
  protected NetworkConfigParser mParser = null;

  public DelegateBase(Context paramContext)
  {
    this.mContext = paramContext;
    this.mParcelHelper = new ParcelHelper("", this.mContext);
  }

  public DelegateBase(ScreenBase paramScreenBase)
  {
    this.mActivityParent = paramScreenBase;
    this.mContext = paramScreenBase;
    this.mParcelHelper = new ParcelHelper("", this.mActivityParent);
  }

  public DelegateBase(ScreenBase paramScreenBase, Context paramContext)
  {
    this.mActivityParent = paramScreenBase;
    this.mContext = paramContext;
    this.mParcelHelper = new ParcelHelper("", this.mContext);
  }

  protected void done(int paramInt)
  {
    if (this.mActivityParent != null)
      this.mActivityParent.done(paramInt);
  }

  public void onActivityResult(int paramInt1, int paramInt2)
  {
  }

  public void setRequiredSettings(Logger paramLogger)
  {
    setRequiredSettings(paramLogger, null, null, null);
  }

  public void setRequiredSettings(Logger paramLogger, GetGlobals paramGetGlobals)
  {
    setRequiredSettings(paramLogger, null, paramGetGlobals, null);
  }

  public void setRequiredSettings(Logger paramLogger, GetGlobals paramGetGlobals, FailureReason paramFailureReason)
  {
    setRequiredSettings(paramLogger, null, paramGetGlobals, paramFailureReason);
  }

  public void setRequiredSettings(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser)
  {
    setRequiredSettings(paramLogger, paramNetworkConfigParser, null, null);
  }

  public void setRequiredSettings(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, FailureReason paramFailureReason)
  {
    setRequiredSettings(paramLogger, paramNetworkConfigParser, null, paramFailureReason);
  }

  public void setRequiredSettings(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, GetGlobals paramGetGlobals, FailureReason paramFailureReason)
  {
    this.mLogger = paramLogger;
    this.mParser = paramNetworkConfigParser;
    this.mGlobals = paramGetGlobals;
    this.mFailure = paramFailureReason;
  }

  protected void showMyDialog(int paramInt)
  {
    this.mDialogShown = paramInt;
    if (this.mActivityParent != null)
      this.mActivityParent.showMyDialog(paramInt);
  }
}