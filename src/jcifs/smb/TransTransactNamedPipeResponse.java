package jcifs.smb;

class TransTransactNamedPipeResponse extends SmbComTransactionResponse
{
  private SmbNamedPipe pipe;

  TransTransactNamedPipeResponse(SmbNamedPipe paramSmbNamedPipe)
  {
    this.pipe = paramSmbNamedPipe;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (this.pipe.pipeIn != null)
    {
      TransactNamedPipeInputStream localTransactNamedPipeInputStream = (TransactNamedPipeInputStream)this.pipe.pipeIn;
      synchronized (localTransactNamedPipeInputStream.lock)
      {
        localTransactNamedPipeInputStream.receive(paramArrayOfByte, paramInt1, paramInt2);
        localTransactNamedPipeInputStream.lock.notify();
        return paramInt2;
      }
    }
    return paramInt2;
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  public String toString()
  {
    return new String("TransTransactNamedPipeResponse[" + super.toString() + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}