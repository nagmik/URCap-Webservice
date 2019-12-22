package com.nast.webservice.style;

import java.awt.*;

public class V3Style extends Style {
		
	private static final Dimension INPUTFIELD_SIZE = new Dimension(170, 30);	
	private static final Dimension INPUTFIELD_SIZE_LONG = new Dimension(300, 30);	
	private static final Dimension LABEL_SIZE = new Dimension(100, 24);
	private static final Dimension LABEL_SIZE_LONG = new Dimension(180, 24);	
	private static final Dimension LABEL_SIZE_LONG_1 = new Dimension(158, 24);
	private static final Dimension BUTTON_SIZE = new Dimension(100, 30);
	private static final Dimension BUTTON_SIZE_SHORT = new Dimension(70, 30);	
	private static final Dimension TEXT_AREA_SIZE_1 = new Dimension(600, 50);
	private static final Dimension COMBOBOX_SIZE = new Dimension(300, 30);
	private static final Dimension COMBOBOX_SIZE_SHORT = new Dimension(140, 30);
	
	@Override
	public Dimension getInputfieldSize() {
		return INPUTFIELD_SIZE;
	}
	
	@Override
	public Dimension getInputfieldSizeLong() {
		return INPUTFIELD_SIZE_LONG;
	}
	
	@Override
	public Dimension getLabelSize() {
		return LABEL_SIZE;
	}
	
	@Override
	public Dimension getLabelSizeLong() {
		return LABEL_SIZE_LONG;
	}
	
	@Override
	public Dimension getLabelSizeLong_1() {
		return LABEL_SIZE_LONG_1;
	}	
	
	@Override
	public Dimension getButtonSize() {
		return BUTTON_SIZE;
	}
	
	@Override
	public Dimension getButtonSizeShort() {
		return BUTTON_SIZE_SHORT;
	}
	
	@Override
	public Dimension getTextAreaSize1() {
		return TEXT_AREA_SIZE_1;
	}
	
	@Override
	public Dimension getComboBoxSize() {
		return COMBOBOX_SIZE;
	}
	
	@Override
	public Dimension getComboBoxSizeShort() {
		return COMBOBOX_SIZE_SHORT;
	}
	
}
