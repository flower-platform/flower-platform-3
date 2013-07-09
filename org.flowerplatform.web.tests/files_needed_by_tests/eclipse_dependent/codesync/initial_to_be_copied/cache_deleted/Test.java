import javax.persistence.OneToMany;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

@Deprecated
public class Test extends Test2 implements ITest {

	private int x;
	
	private int y;
	
	@OneToMany(mappedBy="test")
	public int test(String st) {
		return x;
	}

	@OverrideAnnotationOf(x+y)
	public static Test getTest() {
		return x;
	}
	
}
