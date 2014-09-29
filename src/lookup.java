import java.io.PrintStream;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class lookup
{
  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    int i = 1;
    int j = paramArrayOfString.length;
    int k = 0;
    if (j > 2)
    {
      boolean bool = paramArrayOfString[0].equals("-t");
      k = 0;
      if (bool)
      {
        i = Type.value(paramArrayOfString[1]);
        if (i < 0)
          throw new IllegalArgumentException("invalid type");
        k = 2;
      }
    }
    for (int m = k; m < paramArrayOfString.length; m++)
    {
      Lookup localLookup = new Lookup(paramArrayOfString[m], i);
      localLookup.run();
      printAnswer(paramArrayOfString[m], localLookup);
    }
  }

  public static void printAnswer(String paramString, Lookup paramLookup)
  {
    System.out.print(paramString + ":");
    if (paramLookup.getResult() != 0)
      System.out.print(" " + paramLookup.getErrorString());
    System.out.println();
    Name[] arrayOfName = paramLookup.getAliases();
    if (arrayOfName.length > 0)
    {
      System.out.print("# aliases: ");
      for (int j = 0; j < arrayOfName.length; j++)
      {
        System.out.print(arrayOfName[j]);
        if (j < -1 + arrayOfName.length)
          System.out.print(" ");
      }
      System.out.println();
    }
    if (paramLookup.getResult() == 0)
    {
      Record[] arrayOfRecord = paramLookup.getAnswers();
      for (int i = 0; i < arrayOfRecord.length; i++)
        System.out.println(arrayOfRecord[i]);
    }
  }
}