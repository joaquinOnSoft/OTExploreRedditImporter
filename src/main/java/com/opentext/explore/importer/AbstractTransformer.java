package com.opentext.explore.importer;

import java.util.Date;

import org.jdom2.Element;

import com.opentext.explore.util.DateUtil;

public abstract class AbstractTransformer {
	
	protected static Element createElementField(String name, Date content) {
		return createElementField(name, DateUtil.dateToUTC(content));
	}

	protected static Element createElementField(String name, long content) {
		return createElementField(name, Long.toString(content));
	}
	
	protected static Element createElementField(String name, int content) {
		return createElementField(name, Integer.toString(content));
	}

	protected static Element createElementField(String name, double content) {
		return createElementField(name, Double.toString(content));
	}	
	
	private static Element createElementField(String name, String content) {
		Element elementField = new Element("field");
		elementField.setAttribute("name", name);
		elementField.addContent(content);
		return elementField;
	}
}
