package net.cloudpath.xpressconnect.remote;

import java.io.IOException;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.util.Base64;
import org.apache.http.impl.auth.NTLMEngine;
import org.apache.http.impl.auth.NTLMEngineException;

public class JCIFSEngine
  implements NTLMEngine
{
  public String generateType1Msg(String paramString1, String paramString2)
    throws NTLMEngineException
  {
    return Base64.encode(new Type1Message(Type1Message.getDefaultFlags(), paramString1, paramString2).toByteArray());
  }

  public String generateType3Msg(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws NTLMEngineException
  {
    try
    {
      Type2Message localType2Message = new Type2Message(Base64.decode(paramString5));
      return Base64.encode(new Type3Message(localType2Message, paramString2, paramString3, paramString1, paramString4).toByteArray());
    }
    catch (IOException localIOException)
    {
      throw new NTLMEngineException("Invalid Type2 message", localIOException);
    }
  }
}