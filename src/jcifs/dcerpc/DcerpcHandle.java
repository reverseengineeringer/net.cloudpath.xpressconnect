package jcifs.dcerpc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.Principal;
import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.smb.BufferCache;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbNamedPipe;

public abstract class DcerpcHandle
  implements DcerpcConstants
{
  private static int call_id = 1;
  protected DcerpcBinding binding;
  protected int max_recv = this.max_xmit;
  protected int max_xmit = 4280;
  protected int state = 0;

  public static DcerpcHandle getHandle(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws UnknownHostException, MalformedURLException, DcerpcException
  {
    if (paramString.startsWith("ncacn_np:"))
      return new DcerpcPipeHandle(paramString, paramNtlmPasswordAuthentication);
    throw new DcerpcException("DCERPC transport not supported: " + paramString);
  }

  protected static DcerpcBinding parseBinding(String paramString)
    throws DcerpcException
  {
    char[] arrayOfChar = paramString.toCharArray();
    String str1 = null;
    String str2 = null;
    DcerpcBinding localDcerpcBinding = null;
    int i = 0;
    int j = 0;
    int k = 0;
    int m = arrayOfChar[i];
    switch (k)
    {
    case 3:
    case 4:
    default:
      i = arrayOfChar.length;
    case 0:
    case 1:
    case 2:
    case 5:
    }
    while (true)
    {
      i++;
      if (i < arrayOfChar.length)
        break;
      if ((localDcerpcBinding != null) && (localDcerpcBinding.endpoint != null))
        break label298;
      throw new DcerpcException("Invalid binding URL: " + paramString);
      if (m == 58)
      {
        str1 = paramString.substring(j, i);
        j = i + 1;
        k = 1;
        continue;
        if (m == 92)
        {
          j = i + 1;
        }
        else
        {
          k = 2;
          if (m == 91)
          {
            if (paramString.substring(j, i).trim().length() == 0);
            localDcerpcBinding = new DcerpcBinding(str1, paramString.substring(j, i));
            j = i + 1;
            k = 5;
            continue;
            if (m == 61)
            {
              str2 = paramString.substring(j, i).trim();
              j = i + 1;
            }
            else if ((m == 44) || (m == 93))
            {
              String str3 = paramString.substring(j, i).trim();
              if (str2 == null)
                str2 = "endpoint";
              localDcerpcBinding.setOption(str2, str3);
              str2 = null;
            }
          }
        }
      }
    }
    label298: return localDcerpcBinding;
  }

  public abstract void close()
    throws IOException;

  protected abstract void doReceiveFragment(byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException;

  protected abstract void doSendFragment(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    throws IOException;

  public Principal getPrincipal()
  {
    if ((this instanceof DcerpcPipeHandle))
      return ((DcerpcPipeHandle)this).pipe.getPrincipal();
    return null;
  }

  public String getServer()
  {
    if ((this instanceof DcerpcPipeHandle))
      return ((DcerpcPipeHandle)this).pipe.getServer();
    return null;
  }

  public void sendrecv(DcerpcMessage paramDcerpcMessage)
    throws DcerpcException, IOException
  {
    if (this.state == 0)
    {
      this.state = 1;
      sendrecv(new DcerpcBind(this.binding, this));
    }
    boolean bool = paramDcerpcMessage instanceof DcerpcBind;
    Object localObject1;
    NdrBuffer localNdrBuffer1;
    while (true)
    {
      int i;
      int j;
      try
      {
        byte[] arrayOfByte1 = BufferCache.getBuffer();
        localObject1 = arrayOfByte1;
        try
        {
          localNdrBuffer1 = new NdrBuffer((byte[])localObject1, 0);
          paramDcerpcMessage.flags = 3;
          paramDcerpcMessage.call_id = call_id;
          paramDcerpcMessage.encode(localNdrBuffer1);
          i = localNdrBuffer1.getLength();
          j = 0;
          if (j >= i)
            break;
          int k = call_id;
          call_id = k + 1;
          paramDcerpcMessage.call_id = k;
          if (i - j > this.max_xmit)
            throw new DcerpcException("Fragmented request PDUs currently not supported");
        }
        finally
        {
          BufferCache.releaseBuffer((byte[])localObject1);
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        throw new IOException(localInterruptedException.getMessage());
      }
      int m = i - j;
      doSendFragment((byte[])localObject1, j, m, bool);
      j += m;
    }
    doReceiveFragment((byte[])localObject1, bool);
    localNdrBuffer1.reset();
    paramDcerpcMessage.decode_header(localNdrBuffer1);
    int n = 24;
    if ((paramDcerpcMessage.ptype == 2) && (!paramDcerpcMessage.isFlagSet(2)))
      n = paramDcerpcMessage.length;
    while (true)
    {
      byte[] arrayOfByte2;
      NdrBuffer localNdrBuffer2;
      if (!paramDcerpcMessage.isFlagSet(2))
      {
        if (arrayOfByte2 == null)
        {
          arrayOfByte2 = new byte[this.max_recv];
          localNdrBuffer2 = new NdrBuffer(arrayOfByte2, 0);
        }
        doReceiveFragment(arrayOfByte2, bool);
        localNdrBuffer2.reset();
        paramDcerpcMessage.decode_header(localNdrBuffer2);
        int i1 = -24 + paramDcerpcMessage.length;
        if (n + i1 > localObject1.length)
        {
          byte[] arrayOfByte3 = new byte[n + i1];
          System.arraycopy(localObject1, 0, arrayOfByte3, 0, n);
          localObject1 = arrayOfByte3;
        }
        System.arraycopy(arrayOfByte2, 24, localObject1, n, i1);
        n += i1;
      }
      else
      {
        paramDcerpcMessage.decode(new NdrBuffer((byte[])localObject1, 0));
        BufferCache.releaseBuffer((byte[])localObject1);
        DcerpcException localDcerpcException = paramDcerpcMessage.getResult();
        if (localDcerpcException != null)
          throw localDcerpcException;
        return;
        localNdrBuffer2 = null;
        arrayOfByte2 = null;
      }
    }
  }

  public String toString()
  {
    return this.binding.toString();
  }
}