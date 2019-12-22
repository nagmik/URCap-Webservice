package com.nast.webservice.style;

import java.awt.*;

public abstract class Style {
	
	// HORIZONTAL		
	private static final int HORIZONTAL_SPACING = 10;	
	//  VERTICAL
	private static final int VERTICAL_SPACING = 10;
	
		
	public int getVerticalSpacing() {
		return VERTICAL_SPACING;
	}	
	
	
	public int getHorizontalSpacing() {
		return HORIZONTAL_SPACING;
	}
				
		
	public abstract Dimension getInputfieldSize();	
	
	public abstract Dimension getInputfieldSizeLong();
	
	public abstract Dimension getLabelSize();
	
	public abstract Dimension getLabelSizeLong();	
	
	public abstract Dimension getLabelSizeLong_1();
	
	public abstract Dimension getButtonSize();	
	
	public abstract Dimension getButtonSizeShort();	
	
	public abstract Dimension getTextAreaSize1();
	
	public abstract Dimension getComboBoxSize();
	
	public abstract Dimension getComboBoxSizeShort();
	
}
