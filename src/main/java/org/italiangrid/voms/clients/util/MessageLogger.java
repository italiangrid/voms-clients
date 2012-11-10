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
		defaultMessageLevel = defaultLevel;
		levelFilter = filter;
	}
	
	public MessageLogger() {
		this(System.out, System.err, DEFAULT, MessageLevel.INFO);
	}
	
	public MessageLogger(EnumSet<MessageLevel> filter) {
		this(System.out, System.err, DEFAULT, MessageLevel.INFO);
	}
	
	public final void formatMessage(MessageLevel level, String fmt, Object...args) {
		
		if (levelFilter.contains(level)){
			if (level.equals(MessageLevel.ERROR))
				errorStream.format(fmt, args);
			else
				outputStream.format(fmt, args);
		}
	}
	
	
	private PrintStream streamFromLevel(MessageLevel level){
		if (level.equals(MessageLevel.ERROR))
			return errorStream;
		
		return outputStream;
	}
	
	
	private void formatMessage(MessageLevel level, Throwable t, String fmt, Object...args) {
		
		PrintStream s = streamFromLevel(level);
		
		s.format(fmt, args);
		
		if ( t != null)
			t.printStackTrace(s);
	}
	
	public final void printMessage(MessageLevel level, String msg){
		if (levelFilter.contains(level)){
			if (level.equals(MessageLevel.ERROR))
				errorStream.println(msg);
			else
				outputStream.println(msg);
		}
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
	
	public final void error(Throwable t, String fmt, Object... args){
		formatMessage(MessageLevel.ERROR, t, fmt, args);
	}
	
	public final void warning(Throwable t, String fmt, Object... args){
		formatMessage(MessageLevel.WARNING, t, fmt, args);
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
	
	
}
