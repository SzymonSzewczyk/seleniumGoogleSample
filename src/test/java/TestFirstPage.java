import com.google.pages.setup.SetupUI;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestFirstPage extends SetupUI {

	@Test
	public void test01_isPopUpVisible() {
		Assert.assertTrue(firstPage.isPopupBeforeYouGoVisible(), "Popup 'Before you go' should be visible");
	}
}
