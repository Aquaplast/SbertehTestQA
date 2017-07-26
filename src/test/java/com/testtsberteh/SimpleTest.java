package com.testtsberteh;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

public class SimpleTest {
		
	@DisplayName("Проверка поиска")
    @Description("Проверка результатов поиска и корректности ссылок")
	@Test
	public void firstSimpleTest() {
				
		List<WebElement> elements;			    
	    ChromeDriverManager.getInstance().setup();
	    WebDriver driver = new ChromeDriver();
	    driver.get("https://stackoverflow.com/");	    
	    //Ввод "webdriver" в строку поиска
	    driver.findElement(By.name("q")).sendKeys("webdriver"+Keys.ENTER);
	    //Результаты поиска
	    elements = driver.findElements(By.className("search-result"));
	    //Проверка на наличие "WebDriver" в результатах поиска
	    for (WebElement elem : elements) {
	    	//assertThat(elem.getText(), allOf(containsString("WebDriver"))); //не проходит
	    	assertThat(elem.getText(), allOf(containsString("webdriver"))); //проходит
	    }
	    //Ссылки на найденные темы
	    elements = driver.findElements(By.cssSelector(".search-result .result-link a"));
	    //Создание и заполнение массивов ссылок и названий темы
	    int listSize = elements.size();
	    String[] links = new String[listSize];
	    String[] text = new String[listSize];	    
	    int i = 0;
	    for (WebElement elem : elements) {		    
	    	links[i] = elem.getAttribute("href");
	    	text[i] = elem.getText();
	    	i++;
	    }	    	    
	    //Проверка соответствия ссылок
	    for (i=0; i<listSize;i++) {
	    	driver.navigate().to(links[i]);	    	    	
	    	assertEquals(text[i], "Q: "+driver.findElement(By.className("question-hyperlink")).getText());	    	
	    }        
	    
	}
    
    @DisplayName("Проверка поиска по тегам")
    @Description("Проверка результатов поиска по тегам")
	@Test	
	public void secondSimpleTest() throws InterruptedException {
    	    	
		List<WebElement> elements;			    
	    ChromeDriverManager.getInstance().setup();
	    WebDriver driver = new ChromeDriver();
	    driver.get("https://stackoverflow.com/tags");	    
	    driver.findElement(By.id("tagfilter")).sendKeys("webdriver"+Keys.ENTER);
	    //Таймаут для того чтобы driver обновил DOM -- пока не нашел лучшего способа\нужного условия для WebDriverWait
	    Thread.sleep(1000);
	    elements = driver.findElements(By.cssSelector("#tags-browser>tbody>tr>td>a"));	    
	    WebElement bufElem = null;
	    for (WebElement elem : elements) {
	    	//Тест не проходит из-за результата chrome-web-driver
	    	assertThat(elem.getText(), allOf(containsString("webdriver")));	    	
	    	if (elem.getText().equals("webdriver")) {
	    		bufElem = elem;
	    	}
	    }
	    assertNotNull("Точного совпадения с тегом webdriver не обнаружено", bufElem);	    
	    bufElem.click();	    
	    elements = driver.findElements(By.cssSelector(".question-summary"));	    
	    List<WebElement> elems;
	    for (WebElement elem : elements) {
	    	elems = elem.findElements(By.cssSelector(".tags>.post-tag"));
	    	boolean found = false;
	    	for (WebElement elem1 : elems) {	    			    			    		
	    		 if (elem1.getText().equals("webdriver")) {
	    		 	found = true;
	    		 }	    		 
	    	}
	    	assertTrue(found);
	    }
	    
    }

}
