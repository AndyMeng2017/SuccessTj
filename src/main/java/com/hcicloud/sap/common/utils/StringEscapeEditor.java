package com.hcicloud.sap.common.utils;

import java.beans.PropertyEditorSupport;

public class StringEscapeEditor extends PropertyEditorSupport {
	private boolean escapeHTML;
	private boolean escapeJavaScript;

	public StringEscapeEditor() {
	}

	public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript) {
		this.escapeHTML = escapeHTML;
		this.escapeJavaScript = escapeJavaScript;
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return value != null ? value.toString() : "";
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		} else {
			String value = text;
			value.replace("\r\n", "<br/>");
		/*	if (this.escapeHTML) {
				value = HtmlUtils.htmlEscape(value);
			}
			if (this.escapeJavaScript) {
				value = JavaScriptUtils.javaScriptEscape(value);
			}*/
			setValue(value);
		}
	}
}