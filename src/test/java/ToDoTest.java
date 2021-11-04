import com.google.pages.setup.SetupUIGoogle;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;


public class ToDoTest extends SetupUIGoogle {
	private List<String> expectedTexts;

	/**
	 * Test #1
	 * 1. Go to https://todomvc.com/examples/typescript-angular/#/
	 * 2. Enter three TODO items
	 * 3. Verify all items were added and list has correct size
	 */
	@BeforeClass
	public void toDos() {
		String firstText = "Prepare doc";
		String secondText = "Test";
		String thirdText = "Report";
		expectedTexts = Arrays.asList(firstText, secondText, thirdText);

		firstPage.createTodo(firstText)
				.createTodo(secondText)
				.createTodo(thirdText);
	}

	@Test
	public void test01_checkToDoListSize() {
		Assert.assertEquals(firstPage.getNumberOfTodos(), expectedTexts.size(), "Validating list size");
	}

	@Test
	public void test02_checkToDoListLabels() {
		Assert.assertEquals(firstPage.getTodosTexts(), expectedTexts, "Validating all items were added");
	}
}