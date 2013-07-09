import javax.persistence.OneToMany;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

@Deprecated
public class Test extends TestSource implements ITest {

	private Test x;
	
	private TestSource y;
	
	@OneToMany(mappedBy="test_source")
	public int test(String st) {
		return x;
	}

	@OverrideAnnotationOf(x+y)
	public static Test getTest() {
		return x;
	}
	
}
