
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TodoJUnit {
	WebDriver webDriver;

	@BeforeAll
	public static void setup() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void configureWebDriver() {
		webDriver = new ChromeDriver();

		webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		webDriver.manage().timeouts().setScriptTimeout(Duration.ofMinutes(5));
	}

	@AfterEach
	public void quitter() throws InterruptedException {
		webDriver.quit();
	}

	
	@Test
	public void todoTest() throws InterruptedException {
		webDriver.get("https://todomvc.com");
		select("Backbone.js");

		Thread.sleep(2000);

		ajouterTodo("TODO 1");
		ajouterTodo("TODO 2");
		ajouterTodo("TODO 3");
		ajouterTodo("TODO 4");
		ajouterTodo("TODO 5");

		Thread.sleep(2000);

		click();

		CheckItemsLeft(3);
	}

	@ParameterizedTest
	@ValueSource(strings = { "Backbone.js", "AngularJS", "KnockoutJS" })
	public void todoTests(String selection) throws InterruptedException {
		webDriver.get("https://todomvc.com");

		select(selection);

		Thread.sleep(2000);

		ajouterTodo("TODO 1");
		ajouterTodo("TODO 2");
		ajouterTodo("TODO 3");
		ajouterTodo("TODO 4");
		ajouterTodo("TODO 5");

		Thread.sleep(2000);

		click();

		CheckItemsLeft(3);
	}

	private void select(String selection) {
		WebElement webElement = webDriver.findElement(By.linkText(selection));
		webElement.click();
	}

	private void ajouterTodo(String todo) {
		WebElement webElement = webDriver.findElement(By.className("new-todo"));

		webElement.sendKeys(todo);
		webElement.sendKeys(Keys.RETURN);
	}

	private void click() {

		WebElement webElement = webDriver.findElement(By.cssSelector("li:nth-child(1) .toggle"));
		webElement.click();

		webElement = webDriver.findElement(By.cssSelector("li:nth-child(2) .toggle"));
		webElement.click();
	}

	private void compareText(WebElement webElement, String text) {
		ExpectedConditions.textToBePresentInElement(webElement, text);
	}
	
	private void CheckItemsLeft(int excpectedNumberOfUncheckedTodos) {
		WebElement webElement = webDriver.findElement(By.xpath("//footer/*/span | //footer/span"));

		if (excpectedNumberOfUncheckedTodos == 1) {
			String text = String.format("$d item left", excpectedNumberOfUncheckedTodos);
			compareText(webElement, text);
		} else {
			String text = String.format("$d items left", excpectedNumberOfUncheckedTodos);
			compareText(webElement, text);
		}
	}



}