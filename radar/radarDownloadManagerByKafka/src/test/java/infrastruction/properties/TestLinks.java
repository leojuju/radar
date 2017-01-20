package infrastruction.properties;

import org.junit.Assert;
import org.junit.Test;

public class TestLinks {

	@Test
	public void test1(){
		Links.init();
		Assert.assertTrue(Links.links.size()==190);
		System.out.println(Links.links.get("Z9796"));
		System.out.println(Links.links.get("Z9999"));
	}
}
