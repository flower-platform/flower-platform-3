package com.crispico.flower.util.skin {
	
	import mx.skins.halo.LinkButtonSkin;
	
	/**
	 * Skin for a toggle <code>LinkButton</code>.
	 * http://blog.flexexamples.com/2008/09/06/creating-a-toggleable-linkbutton-control-in-flex/
	 * 
	 * @author Cristi
	 * @author Cristina	
	 */ 
	public class ToggleLinkButtonSkin extends LinkButtonSkin {
		
        override protected function updateDisplayList(w:Number, h:Number):void {
            super.updateDisplayList(w, h);

            var cornerRadius:Number = getStyle("cornerRadius");
            var rollOverColor:uint = getStyle("rollOverColor");
            var selectionColor:uint = getStyle("selectionColor");

            graphics.clear();

            switch (name) {
                case "upSkin":
                    // Draw invisible shape so we have a hit area.
                    drawRoundRect(0, 0, w, h, cornerRadius, 0, 0);
                    break;
                case "selectedUpSkin":
                case "selectedOverSkin":
                case "overSkin":
                    drawRoundRect(0, 0, w, h, cornerRadius, rollOverColor, 1);
                    break;
                case "selectedDownSkin":
                case "downSkin":
                    drawRoundRect(0, 0, w, h, cornerRadius, selectionColor, 1);
                    break;
                case "selectedDisabledSkin":
                case "disabledSkin":
                    // Draw invisible shape so we have a hit area.
                    drawRoundRect(0, 0, w, h, cornerRadius, 0, 0);
                    break;
            }
        }

	}
}