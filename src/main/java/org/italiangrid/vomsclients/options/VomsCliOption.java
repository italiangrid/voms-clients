package org.italiangrid.vomsclients.options;

/**
 * Common interface for the different client options sets.
 * 
 * @author Daniele Andreotti
 * 
 */
public interface VomsCliOption {

	public String getOpt();

	public String getLongOpt();

	public String getDescription();

	public boolean hasArg();

	public String getArgDescription();
}
