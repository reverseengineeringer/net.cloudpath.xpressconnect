package net.cloudpath.xpressconnect.certificates;

public class CertFailureReason
{
  public static final int INVALID_CERTS_IN_CHAIN = 2;
  public static final int NO_CA_CERTS_IN_CHAIN = 3;
  public static final int NO_CERTS_IN_STORE = 4;
  public static final int NO_PRIVATE_KEY = 1;
  public static final int NULL_CERT_IN_CHAIN = 5;
  public static final int UNKNOWN;
}