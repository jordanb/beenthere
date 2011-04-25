package com.beenthere.kml;

import java.util.Properties;

/**
 * This class holds all configuration information required
 * by gekmllib.
 * 
 * At the moment, it is a single property determining whether or
 * not ids are automatically generated for all objects.
 * 
 * @author power
 *
 */
public class Configuration
{
    public static Properties properties;
    
    public static final String GENERATE_IDS = "GenerateIDs";

    public static final String ON = "On";
    public static final String OFF = "Off";
    
    static
    {
	properties = new Properties();
	properties.put(GENERATE_IDS, ON);
    }
}
