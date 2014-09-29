package net.cloudpath.xpressconnect;

import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;

public class MdmDetection
{
  public static boolean needToConfigureMdm(NetworkConfigParser paramNetworkConfigParser)
  {
    boolean bool = true;
    if ((paramNetworkConfigParser == null) || (paramNetworkConfigParser.selectedProfile == null))
      bool = false;
    while ((paramNetworkConfigParser.selectedProfile.getSetting(0, 40073) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40072) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40071) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40070) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40069) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40068) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40067) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40066) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40065) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40064) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40063) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40062) != null) || (paramNetworkConfigParser.selectedProfile.getSetting(0, 40061) != null))
      return bool;
    return false;
  }
}