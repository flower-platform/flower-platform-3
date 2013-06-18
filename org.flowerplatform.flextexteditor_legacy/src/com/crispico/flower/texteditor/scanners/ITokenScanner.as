package com.crispico.flower.texteditor.scanners {
	
	import com.crispico.flower.texteditor.model.Token;
	
	/**
	 * @flowerModelElementId _Mt5yUN3QEeCGOND4c9bKyA
	 */
	public interface ITokenScanner {
		
		/**
		 * The next token detected by this scanner.
		 * 
		 * @flowerModelElementId _0qhDku2uEeCF5Ozw-0NJ0A
		 */
		 function nextToken():Token;
	}
}