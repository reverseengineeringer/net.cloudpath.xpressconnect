package jcifs.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jcifs.ntlmssp.NtlmFlags;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.util.Base64;

public class NtlmSsp
  implements NtlmFlags
{
  public static NtlmPasswordAuthentication authenticate(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, byte[] paramArrayOfByte)
    throws IOException, ServletException
  {
    String str1 = paramHttpServletRequest.getHeader("Authorization");
    byte[] arrayOfByte1;
    if ((str1 != null) && (str1.startsWith("NTLM ")))
    {
      arrayOfByte1 = Base64.decode(str1.substring(5));
      if (arrayOfByte1[8] == 1)
      {
        String str2 = Base64.encode(new Type2Message(new Type1Message(arrayOfByte1), paramArrayOfByte, null).toByteArray());
        paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM " + str2);
      }
    }
    while (true)
    {
      paramHttpServletResponse.setStatus(401);
      paramHttpServletResponse.setContentLength(0);
      paramHttpServletResponse.flushBuffer();
      return null;
      if (arrayOfByte1[8] == 3)
      {
        Type3Message localType3Message = new Type3Message(arrayOfByte1);
        byte[] arrayOfByte2 = localType3Message.getLMResponse();
        if (arrayOfByte2 == null)
          arrayOfByte2 = new byte[0];
        byte[] arrayOfByte3 = localType3Message.getNTResponse();
        if (arrayOfByte3 == null)
          arrayOfByte3 = new byte[0];
        return new NtlmPasswordAuthentication(localType3Message.getDomain(), localType3Message.getUser(), paramArrayOfByte, arrayOfByte2, arrayOfByte3);
        paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM");
      }
    }
  }

  public NtlmPasswordAuthentication doAuthentication(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, byte[] paramArrayOfByte)
    throws IOException, ServletException
  {
    return authenticate(paramHttpServletRequest, paramHttpServletResponse, paramArrayOfByte);
  }
}