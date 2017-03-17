// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.reqtraq.data;


public class Schema
{
	/** document version value */
	public static final String VERSION = "ReqTraq 0.0";
	
	/** unique identifier */
	public static final String KEY_ID = "id";
	/** list of pages in the document */
	public static final String KEY_PAGES = "pages";
	/** page nesting level */
	public static final String KEY_PAGE_LEVEL = "level";
	/** page status */
	public static final String KEY_PAGE_STATUS = "status";
	/** page text */
	public static final String KEY_PAGE_TEXT = "text";
	/** page creation timestamp */
	public static final String KEY_PAGE_TIME_CREATED = "created";
	/** page modification timestamp */
	public static final String KEY_PAGE_TIME_MODIFIED = "modified";	
	/** page title */
	public static final String KEY_PAGE_TITLE = "title";
	/** document file version */
	public static final String KEY_VERSION = "version";
}
