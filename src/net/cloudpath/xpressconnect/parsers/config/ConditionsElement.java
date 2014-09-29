package net.cloudpath.xpressconnect.parsers.config;

import java.util.ArrayList;
import net.cloudpath.xpressconnect.logger.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ConditionsElement
{
  private ConditionElement activeElement = null;
  public ArrayList<ConditionElement> conditions = null;
  private Logger mLogger = null;

  public ConditionsElement(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.conditions = new ArrayList();
  }

  public boolean conditionsAreTrue()
  {
    boolean bool = true;
    for (int i = 0; (bool == true) && (i < this.conditions.size()); i++)
      bool = ((ConditionElement)this.conditions.get(i)).matches();
    return bool;
  }

  public void endConditionsElement(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if ((paramString2 == "condition") && (this.activeElement != null))
    {
      this.activeElement.endConditionElement(paramString1, paramString2, paramString3, paramString4);
      this.conditions.add(this.activeElement);
      this.activeElement = null;
    }
  }

  public void startConditionsElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (paramString2 != "conditions")
    {
      if (this.activeElement == null)
        this.activeElement = new ConditionElement(this.mLogger);
      this.activeElement.startConditionElement(paramString1, paramString2, paramString3, paramAttributes);
    }
  }
}