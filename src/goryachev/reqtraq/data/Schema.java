// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;


public class Schema
{
	/** document version value */
	public static final String VERSION = "ReqTraq.0";
	
	/** global section */
	public static final String SECTION_GLOBAL = "global";
	/** section contains tree of pages */
	public static final String SECTION_PAGES = "pages";
	
	/** identifier */
	public static final String ID = "id";
	/** image attachment */
	public static final String IMAGE = "image";
	/** parent page id */
	public static final String PARENT = "parent";
	/** page status */
	public static final String STATUS = "status";
	/** page text */
	public static final String TEXT = "text";
	/** page creation timestamp */
	public static final String TIME_CREATED = "created";
	/** page modification timestamp */
	public static final String TIME_MODIFIED = "modified";	
	/** page title */
	public static final String TITLE = "title";
}
