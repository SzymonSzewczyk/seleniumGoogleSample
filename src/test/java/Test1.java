import com.google.pages.setup.SetupUI;
import org.testng.annotations.Test;

public class Test1 extends SetupUI {

	@Test
	public void test01_submitPinCode() throws InterruptedException {
		firstPage.flipCartHelp();
		//assert pincode is visible
	}
}
