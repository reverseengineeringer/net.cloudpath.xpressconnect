package net.cloudpath.xpressconnect;

public class FailureResults
{
  public static final int ACTIVITY_FAILED = -1;
  public static final int BAD_CREDENTIALS = 11;
  public static final int CHANGE_CERT_TYPE = 25;
  public static final int CHANGE_CREDS = 10;
  public static final int CHECKED_URL_ERROR = 18;
  public static final int CONFIG_FAILED = 21;
  public static final int CONNECT_ATTEMPT_FAILED = -3;
  public static final int CONNECT_ONLY = 24;
  public static final int CONNECT_REQUEST_FAILED = 14;
  public static final int CONTINUE_CONFIG = 23;
  public static final int DHCP_FAILED = 12;
  public static final int DONT_CARE = 0;
  public static final int FAILED_QUIT = 20;
  public static final int FORCE_ENABLE_KEYSTORE = 27;
  public static final int GENERAL_FAILURE = 15;
  public static final int GOTO_NFC_PROGRAMMER = 39;
  public static final int GO_BACK = -2;
  public static final int GO_TO_PRE_URL_CHECK = 19;
  public static final int INSTALLED = 38;
  public static final int INTERNAL_ERROR = 17;
  public static final int KEYSTORE_UNLOCK_FAILED = 22;
  public static final int MAX_FAILURE_VALUE = 41;
  public static final int MDM_FAILED = 35;
  public static final int MIN_FAILURE_VALUE = -10;
  public static final int NET_CREATION_FAILED = 13;
  public static final int NOT_OUR_CONFIG = 32;
  public static final int NO_CONFIG_AVAILABLE = 34;
  public static final int NO_NET_AVAILABLE = 26;
  public static final int PARSE_FAILED = 30;
  public static final int RETRY = 16;
  public static final int RETRY_FROM_SCRATCH = 31;
  public static final int SHOW_REPORT_DATA = 40;
  public static final int SKIPPED = 37;
  public static final int START_OVER = 28;
  public static final int SUCCESS = 1;
  public static final int UNUSED = -10;
  public static final int USER_QUIT = 29;
  public static final int WEB_SITE_CHECK_FAILED = 33;

  public class FailIssue
  {
    public static final int CERTIFICATE_ISSUE = 2;
    public static final int CONFIG_FILE_ISSUE = 5;
    public static final int CONNECT_ISSUE = 10;
    public static final int CREDENTIAL_ISSUE = 1;
    public static final int DEVICE_INVALID = 9;
    public static final int DHCP_ISSUE = 3;
    public static final int INTERNAL_ERROR = 6;
    public static final int KEYING_ISSUE = 11;
    public static final int KEYSTORE_ISSUE = 7;
    public static final int MDM_FAILURE = 12;
    public static final int NONE = 0;
    public static final int PROXY_ERROR = 8;
    public static final int UNKNOWN = -1;
    public static final int USER_CANCELLED = 13;
    public static final int WIFI_ISSUE = 4;

    public FailIssue()
    {
    }
  }
}