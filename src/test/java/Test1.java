import com.google.pages.setup.SetupUIGoogle;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Test1 extends SetupUIGoogle {

	@Test
	public void test01_isPopUpVisible() {
		Assert.assertTrue(firstPage.isPopupBeforeYouGoVisible(), "Popup 'Before you go' should be visible");
	}
}
