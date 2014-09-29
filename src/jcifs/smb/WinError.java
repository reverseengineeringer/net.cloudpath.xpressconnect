package jcifs.smb;

public abstract interface WinError
{
  public static final int ERROR_ACCESS_DENIED = 5;
  public static final int ERROR_BAD_PIPE = 230;
  public static final int ERROR_MORE_DATA = 234;
  public static final int ERROR_NO_BROWSER_SERVERS_FOUND = 6118;
  public static final int ERROR_NO_DATA = 232;
  public static final int ERROR_PIPE_BUSY = 231;
  public static final int ERROR_PIPE_NOT_CONNECTED = 233;
  public static final int ERROR_REQ_NOT_ACCEP = 71;
  public static final int ERROR_SUCCESS;
  public static final int[] WINERR_CODES = { 0, 5, 71, 230, 231, 232, 233, 234, 6118 };
  public static final String[] WINERR_MESSAGES = { "The operation completed successfully.", "Access is denied.", "No more connections can be made to this remote computer at this time because there are already as many connections as the computer can accept.", "The pipe state is invalid.", "All pipe instances are busy.", "The pipe is being closed.", "No process is on the other end of the pipe.", "More data is available.", "The list of servers for this workgroup is not currently available." };
}