package com.crispico.flower.flexdiagram.skin {
	import mx.skins.halo.LinkButtonSkin;
	
	public class SubmenuEntrySkin extends LinkButtonSkin {
		 override protected function updateDisplayList(w:Number, h:Number):void {
           super.updateDisplayList(w, h);

			var cornerRadius:Number = getStyle("cornerRadius");
			var rollOverColor:uint = getStyle("rollOverColor");
			var selectionColor:uint = getStyle("selectionColor");
	
			graphics.clear();
															
			switch (name)
			{			
				case "upSkin":
				{
					// Draw invisible shape so we have a hit area.
					drawRoundRect(
						0, 0, w, h, cornerRadius,
						0, 0);
					break;
				}
				
				case "overSkin":
				{
					drawRoundRect(
						0, 0, w, h, cornerRadius,
						rollOverColor, 1);
					break;
				}
				
				case "downSkin":
				{
					drawRoundRect(
						0, 0, w, h, cornerRadius,
						selectionColor, 1);
					break;
				}
	
				case "disabledSkin":
				{
					// Draw invisible shape so we have a hit area.
					drawRoundRect(
						0, 0, w, h, cornerRadius,
						0, 0);
					break;
				}
            }
        }

	}
}