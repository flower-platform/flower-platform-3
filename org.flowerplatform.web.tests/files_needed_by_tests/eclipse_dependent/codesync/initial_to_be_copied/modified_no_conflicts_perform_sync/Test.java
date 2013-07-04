import javax.persistence.OneToMany;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

@Deprecated
public class Test extends Test2 implements IFromSource {

	/**
	 * modified from source
	 * @author test
	 */
	private int x = 1;
	
	private int y;
	
	private int z = 3;
	
	@OneToMany(mappedBy="test")
	public static int test(final String st) {
		return x;
	}

	@OverrideAnnotationOf(x+y)
	public Test getTest() {
		return x;
	}

	public enum ActionType {
		
		ACTION_TYPE_COPY_LEFT_RIGHT(new Test()) {
			
		},
		ACTION_TYPE_COPY_RIGHT_LEFT(new InternalClsFromSource());
		
		public Object diffAction;
		
		private ActionType(Object action) {
			this.diffAction = action;
		}
	}
	
	public @interface AnnotationTest {
		
		boolean value1() default true;
		boolean value2() default false;
		
	}
	
	public class InternalClsFromSource {
		private int x;
	}
	
}
