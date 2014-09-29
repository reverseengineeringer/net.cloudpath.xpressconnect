package net.cloudpath.xpressconnect;

public class Testing
{
  public static boolean areTesting = false;
  public static boolean clearKeyguard = false;
  public static boolean skipWireless = false;
  public static boolean testHit = false;

  public static void clearhit()
  {
    testHit = false;
  }

  public static void settesting()
  {
    areTesting = true;
  }

  public static void testhit()
  {
    testHit = true;
  }
}