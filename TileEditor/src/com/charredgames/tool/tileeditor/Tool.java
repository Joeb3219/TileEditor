package com.charredgames.tool.tileeditor;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 30, 2013
 */
public enum Tool {

	SINGLE("Single", "MENU_TOOL_SINGLE"), DUAL("Dual", "MENU_TOOL_DUAL"),
	BUCKET("Bucket", "MENU_TOOL_BUCKET"), ERASER("Eraser", "MENU_TOOL_ERASER");
	
	public final String displayName, componentName;
	
	private Tool(String displayName, String componentName){
		this.displayName = displayName;
		this.componentName = componentName;
	}
}
