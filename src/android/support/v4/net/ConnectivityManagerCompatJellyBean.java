package android.support.v4.net;

import android.net.ConnectivityManager;

class ConnectivityManagerCompatJellyBean
{
  public static boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
  {
    return paramConnectivityManager.isActiveNetworkMetered();
  }
}