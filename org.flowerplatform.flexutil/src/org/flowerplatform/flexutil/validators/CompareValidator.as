package org.flowerplatform.flexutil.validators
{
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	/**
	 * Validator used to compare 2 objects.
	 * 
	 * @author Cristina
	 */ 
	public class CompareValidator extends Validator {
	
		public var valueToCompare:Object;
   		
   		public var errorMessage:String = "Value does not match.";
		
     	override protected function doValidation(value:Object):Array {
  	 		var results:Array = [];
     		var srcVal:Object = this.getValueFromSource();
 
     		if (srcVal != valueToCompare) {
        		results.push(new ValidationResult(true, null, "Match", errorMessage));
       		}
     		return results;
		}
	}
}