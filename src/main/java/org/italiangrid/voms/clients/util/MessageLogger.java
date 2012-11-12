package org.italiangrid.voms.clients.util;

import java.io.PrintStream;
import java.util.EnumSet;

public class MessageLogger {
	
	public enum MessageLevel{
		TRACE,
		INFO,
		WARNING,
		ERROR
	}
	
	public static final EnumSet<MessageLevel> DEFAULT = EnumSet.range(MessageLevel.INFO, MessageLevel.ERROR);
	public static final EnumSet<MessageLevel> QUIET = EnumSet.of(MessageLevel.ERROR, MessageLevel.WARNING);
	public static final EnumSet<MessageLevel> VERBOSE = EnumSet.allOf(MessageLevel.class);
	
	private final EnumSet<MessageLevel> levelFilter;
	
	private final MessageLevel defaultMessageLevel;
	
	private final PrintStream outputStream;
	private final PrintStream errorStream;
	
	public MessageLogger(PrintStream out, PrintStream err, 
			EnumSet<MessageLevel> filter, MessageLevel defaultLevel) {
		outputStream = out;
		errorStream = err;
		levelFilter = filter;
		defaultMessageLevel = defaultLevel;
	}
	
	public MessageLogger() {
		this(System.out, System.err, DEFAULT, MessageLevel.INFO);
	}
	
	public MessageLogger(EnumSet<MessageLevel> filter) {
		this(System.out, System.err, filter, MessageLevel.INFO);
	}
	
	public final void formatMessage(MessageLevel level, String fmt, Object...args) {
		
		PrintStream s = streamFromLevel(level);
		
		if (levelFilter.contains(level)){
			
			if (args == null || args.length == 0)
				s.println(fmt);
			else
				s.format(fmt, args);
		}
			
	}
	
	
	private PrintStream streamFromLevel(MessageLevel level){
		if (level.equals(MessageLevel.ERROR))
			return errorStream;
		
		return outputStream;
	}
	
	
	private void formatMessage(MessageLevel level, String msg, Throwable t) {
		
		if (msg!= null){
			if (t.getMessage()!=null)
				formatMessage(level,"%s - %s\n",msg, t.getMessage());
			else
				formatMessage(level,"%s - %s\n",msg, t.getClass().getName());
		}else{
			if (t.getMessage()!=null)
				formatMessage(level,"%s\n",t.getMessage());
			else
				formatMessage(level,"%s\n",t.getClass().getName());
		}
		
		if (levelFilter.contains(MessageLevel.TRACE))
			t.printStackTrace(streamFromLevel(level));
		
	}
	
	public final void printMessage(MessageLevel level, String msg){
		
		formatMessage(level, "%s\n", msg);
	}
	
	public final void formatMessage(String fmt, Object...args){
		formatMessage(defaultMessageLevel, fmt, args);
	}
	
	public final void printMessage(String msg){
		printMessage(defaultMessageLevel, msg);
	}
	
	public final void trace(String fmt, Object... args){
		formatMessage(MessageLevel.TRACE, fmt, args);
	}
	
	public final void error(String msg, Throwable t){
		formatMessage(MessageLevel.ERROR, msg, t);
	}
	
	public final void warning(String msg, Throwable t){
		formatMessage(MessageLevel.WARNING, msg, t);
	}
	
	public final void info(String msg, Throwable t){
		formatMessage(MessageLevel.INFO, msg, t);
	}
	
	public final void trace(String msg, Throwable t){
		formatMessage(MessageLevel.TRACE, msg, t);
	}
	
	public final void error(String fmt, Object... args){
		formatMessage(MessageLevel.ERROR, fmt, args);
	}
	
	public final void warning(String fmt, Object... args){
		formatMessage(MessageLevel.WARNING, fmt, args);
	}
	
	public final void info(String fmt, Object... args){
		formatMessage(MessageLevel.INFO, fmt, args);
	}
	
	
	public final void error(Throwable t){
		formatMessage(MessageLevel.ERROR, null, t);
	}
	
	public final void warning(Throwable t){
		formatMessage(MessageLevel.WARNING, null, t);
	}
	
	public final void info(Throwable t){
		formatMessage(MessageLevel.INFO, null, t);
	}
	
	public final void trace(Throwable t){
		formatMessage(MessageLevel.TRACE, null, t);
	}

	public PrintStream getOutputStream() {
		return outputStream;
	}

	public PrintStream getErrorStream() {
		return errorStream;
	}
}


