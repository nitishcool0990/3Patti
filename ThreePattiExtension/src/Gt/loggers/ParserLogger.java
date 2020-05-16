package Gt.loggers;


import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import Gt.controller.GameController;
import Gt.interfaces.Controller;

public class ParserLogger
{
  FileWriter logFileStream = null;
  public boolean traceEnabled = true;
  private Controller c = null;
  private static PatternLayout patternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %m  %n");
  Logger logger = null;
  
  public void setThreePattiParser(GameController gc, int tableID)
  {
    
    this.c = gc;
    if (this.traceEnabled)
    {
      String path = "logs/ThreePatti/"+"ThreePatti_" + tableID + ".log";
      createLogger(path);
    }
  }
  
  public void writeLog(String str)
  {
    this.logger.info(str);
  }
  
  private boolean createLogger(String path)
  {
    boolean ret = false;
    try
    {
      this.logger = Logger.getLogger(path);
      FileAppender appender = new DailyRollingFileAppender(patternLayout, path, "'.'yyyy-MM-dd-HH");
      this.logger.addAppender(appender);
      this.logger.setAdditivity(false);
      this.logger.setLevel(Level.DEBUG);
      ret = true;
    }
    catch (IOException e)
    {
      this.c.error("Unable to create logger object", e);
    }
    return ret;
  }
}
