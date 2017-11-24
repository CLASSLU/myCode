package kd.idp.common.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.Guid;


public class CommonLogger extends Logger{

	private static String defaultType = "defaultLog";
	private static boolean writeLog = Boolean.parseBoolean(Global.getConfig("writeLog"));
	private static int maxTraceCount = 5;
	
	private String logName;
	
	
	protected CommonLogger(Logger log,String logName){
		super(log.getName());
		repository = log.getLoggerRepository();
		this.parent = log.getParent();
		this.logName = logName;
	}
	public static CommonLogger getLogger(){
        return getLogger(defaultType);
    }
	public static CommonLogger getLogger(String logName)
    {
		Logger log = LogManager.getLogger(CommonLogger.class.getName());
		
        return new CommonLogger(log,logName);
    }
	
	
    public void debug(Object message)
    {
        if(repository.isDisabled(10000))
            return;
        String stackTrace = getstackTrace();
        if(Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.DEBUG, message+" at "+stackTrace, null);
        }
        writeDataBase(message,"debug",stackTrace);
    }

    public void debug(Object message, Throwable t)
    {
        if(repository.isDisabled(10000))
            return;
        String stackTrace = getstackTrace(t);
        if(Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())){
        	forcedLog(CommonLogger.class.getName(), Level.DEBUG, message+" at "+stackTrace, t);
        }
        writeDataBase(message,"debug",stackTrace);
    }
    public void info(Object message)
    {
        if(repository.isDisabled(20000))
            return;
        String stackTrace = getstackTrace();
        if(Level.INFO.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.INFO, message+" at "+stackTrace, null);
        }
        writeDataBase(message,"info",stackTrace);
    }
    public void info(Object message, Throwable t)
    {
        if(repository.isDisabled(20000))
            return;
        String stackTrace = getstackTrace(t);
        if(Level.INFO.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.INFO, message+" at "+stackTrace, t);
        }
        writeDataBase(message,"info",stackTrace);
    }
    public void warn(Object message)
    {
        if(repository.isDisabled(30000))
            return;
        String stackTrace = getstackTrace();
        if(Level.WARN.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.WARN, message+" at "+stackTrace, null);
        }
        writeDataBase(message,"warn",stackTrace);
    }

    public void warn(Object message, Throwable t)
    {
        if(repository.isDisabled(30000))
            return;
        String stackTrace = getstackTrace(t);
        if(Level.WARN.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.WARN, message+" at "+stackTrace, t);
        }
        writeDataBase(message,"warn",stackTrace);
    }
    public void error(Object message)
    {
        if(repository.isDisabled(40000))
            return;
        String stackTrace = getstackTrace();
        if(Level.ERROR.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.ERROR, message+" at "+stackTrace, null);
        }
        writeDataBase(message,"error",stackTrace);
    }

    public void error(Object message, Throwable t)
    {
        if(repository.isDisabled(40000))
            return;
        String stackTrace = getstackTrace(t);
        if(Level.ERROR.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.ERROR, message+" at "+stackTrace, t);
        }
        writeDataBase(message,"error",stackTrace);
    }
    public void fatal(Object message)
    {
        if(repository.isDisabled(50000))
            return;
        String stackTrace = getstackTrace();
        if(Level.FATAL.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.FATAL, message+" at "+stackTrace, null);
        }
        writeDataBase(message,"fatal",stackTrace);
        
    }

    public void fatal(Object message, Throwable t)
    {
        if(repository.isDisabled(50000))
            return;
        String stackTrace = getstackTrace(t);
        if(Level.FATAL.isGreaterOrEqual(getEffectiveLevel())){
            forcedLog(CommonLogger.class.getName(), Level.FATAL, message+" at "+stackTrace, t);
        }
        writeDataBase(message,"fatal",stackTrace);
    }
    private String getstackTrace(){
    	StackTraceElement[] element = Thread.currentThread().getStackTrace();
		String stackTrace = "";
		
		if(element.length>3){
			stackTrace = element[3].toString();//获取调用此方法的类文件
		}
		return stackTrace;
    }
    private String getstackTrace(Throwable t){
    	StackTraceElement[] element = t.getStackTrace();
    	StringBuffer stackTrace = new StringBuffer();
    	int count = element.length;
    	if(count > maxTraceCount){
    		count = maxTraceCount;
    	}
    		
    	for(int i=0;i<count;i++){
    		stackTrace.append(element[i]);
    		stackTrace.append("\n\r");
    	}
    	
    	return stackTrace.toString();
    }
    private void writeDataBase(Object message,String logType,String stackTrace){
    	if(!writeLog){
    		return ;
    	}
    	try {
    		
    		String id = Guid.create();
    		if(stackTrace!=null)
    			stackTrace = stackTrace.replaceAll("'", "''");
    		if(message!=null)
    			message = message.toString().replaceAll("'", "''");
    		String serviceDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    		String sql = "INSERT INTO IDP_JBOSS.AUTO_COMMON_LOG(ID,NAME,STACKTRACE,MESSAGE,DBDATE,SERDATE,LOGTYPE) " +
    				" VALUES('"+id+"','"+logName.replaceAll("'", "''")+"','"+stackTrace+"','"+message+
    				" ',SYSDATE,'"+serviceDate+"','"+logType+"')";
    		
    		DbOper.executeNonQuery(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
