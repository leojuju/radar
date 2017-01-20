package infratruction.db;

import java.util.UUID;

public class TestUUID {
	public static void test() {
		String  requestID1 = UUID.randomUUID().toString();
		String  requestID2 = UUID.randomUUID().toString();
		System.out.println(requestID1);
		System.out.println(requestID2);
	}
}
