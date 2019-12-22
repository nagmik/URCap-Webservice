package com.nast.webservice.style;

import java.awt.*;

public class V5Style extends Style {
	
	private static final Dimension INPUTFIELD_SIZE = new Dimension(220, 30);
	private static final Dimension INPUTFIELD_SIZE_LONG = new Dimension(400, 30);
	private static final Dimension LABEL_SIZE = new Dimension(100, 24);
	private static final Dimension LABEL_SIZE_LONG = new Dimension(250, 24);
	private static final Dimension LABEL_SIZE_LONG_1 = new Dimension(228, 24);
	private static final Dimension BUTTON_SIZE = new Dimension(105, 30);
	private static final Dimension BUTTON_SIZE_SHORT = new Dimension(70, 30);
	private static final Dimension TEXT_AREA_SIZE_1 = new Dimension(500, 50);	
	private static final Dimension COMBOBOX_SIZE = new Dimension(400, 30);
	private static final Dimension COMBOBOX_SIZE_SHORT = new Dimension(200, 30);
	
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
