package STTA.MavenProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarNameValuePair;
import net.lightbody.bmp.core.har.HarPostDataParam;
import net.lightbody.bmp.proxy.CaptureType;

public class webdriverTest {
	
	String driverPath = "C:\\Users\\admin\\Desktop\\ChromeDriver Latest\\chromedriver.exe";
	String sFileName = "C:\\Users\\admin\\Desktop\\Output\\SeleniumEasy.har";
	
	public WebDriver driver;
	public BrowserMobProxy proxy;
	public static String finalresult;
	private static String finalresult2;
	@BeforeTest
	public void setUp() {
		
	   // start the proxy
	    proxy = new BrowserMobProxyServer();
	    proxy.start(0);

	   
	    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

	   
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
		
	   
		System.setProperty("webdriver.chrome.driver", driverPath);
		driver = new ChromeDriver(capabilities);
		
	   
	    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

	   
	    proxy.newHar("DataLicious");

	    // Googling Datalicious ..
	    System.out.println("Navigating to Google.com");
		 driver.get("http://www.google.com");
        
	}
	
	@Test(priority=1) // SELECT first result diplayed in google result
	public void testCaseOne() throws InterruptedException {
		  
         Thread.sleep(2500);
         System.out.println("Selecting first result ");
         WebElement element = driver.findElement(By.name("q"));
         element.sendKeys("Datalicious");
         element.submit();	
         
         System.out.println(driver.getTitle());
      Thread.sleep(4500);
      driver.findElement(By.xpath("//h3/a")).click();
     Thread.sleep(4000);
         System.out.println(driver.getCurrentUrl());
	}
	
	
	
	// Converting .har to json obj
	@Test(priority=2)
	public void tearDown() throws IOException {
	// get the HAR data
	Har har = proxy.getHar();

	// Write HAR Data in a File
	File harFile = new File(sFileName);
	try {
		har.writeTo(harFile);
	} catch (IOException ex) {
		 System.out.println (ex.toString());
	     System.out.println("Could not find file " + sFileName);
	}
	net.lightbody.bmp.core.har.Har har1 = proxy.getHar();

	java.io.StringWriter writer = new java.io.StringWriter();
	try {
	            har1.writeTo(writer);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	String harAsString = writer.toString();
	
	com.google.gson.JsonElement harAsJson = new com.google.gson.Gson().toJsonTree(writer.toString());
	try (FileWriter file = new FileWriter("C:\\Users\\admin\\Desktop\\Output\\new 2.txt")) {
		file.write(harAsJson.toString());
		System.out.println("Successfully Copied JSON Object to File...");
		//System.out.println("\nJSON Object: " + obj);
	}
	


	}
	// TASK 2 : Request is made to google analytics , dc.optimahub.com
	@Test(priority=3)
	public void TestcaseThree(){
		
		File file = new File("C:\\Users\\admin\\Desktop\\Output\\new 2.txt");
		try {
		    Scanner scanner = new Scanner(file);
		    
		    //now read the file line by line...
		    int lineNum = 0;
		   
		    	
		    
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        lineNum++;
		        if(line.contains("dc.optimahub.com")) { 
		            System.out.println("Optima hub reuest found ! " +lineNum);
		        }
		       
		        
		    }
		    
		} catch(FileNotFoundException e) { 
		    //handle this
		}
		try {
		    Scanner scanner = new Scanner(file);
		    
		    //now read the file line by line...
		    int lineNum = 0;
		   
		    	
		    
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        lineNum++;
		        if(line.contains("https://www.google-analytics.com/analytics.js")) { 
		            System.out.println("GET reuest to google anaylitics made  !" +lineNum);
		        }
		       
		        
		    }
		    
		} catch(FileNotFoundException e) { 
		    //handle this
		}
	}	
// TASK 3 : parameter dt, dp assertion and logging into .csv file 
	@Test(priority=4)
	public void TestCaseFour() throws FileNotFoundException
	
	{
		File file = new File("C:\\Users\\admin\\Desktop\\Output\\new 2.txt");
		try {
		    Scanner scanner = new Scanner(file);
		    
		    //now read the file line by line...
		    int lineNum = 0;
		   
		    	
		    
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        lineNum++;
		        if(line.contains("&dt=")) { 
		         //   System.out.println("dt parameneter found   !"+ lineNum );
		            try{
		          int index=  line.indexOf("&dt=");
		           System.out.println(index); 	
		            String result= line.substring(index, index+100);
		            System.out.println("Initial Scrap"+result);
		          
		            String [] newresultset = result.split("&");
		            for(int i = 0; i<newresultset.length ;i++){
		            	
		            	if(newresultset[i].contains("dt=")){
		            		webdriverTest.finalresult=newresultset[i].split("=")[1].replaceAll("%20", " ").replaceAll("%7C", "|");
		            		System.out.println(finalresult);
		            		
		            	}
		            }
		            
		            
		            }
		            catch(Exception ger)
		            {
		            	ger.printStackTrace();
		            }
		        }
		       
		        
		    }
		    
		} catch(FileNotFoundException e) { 
		    //handle this
		}
		
		 PrintWriter pw = new PrintWriter(new File("C:\\Users\\admin\\Desktop\\Output\\licious.csv"));
		  StringBuilder sb = new StringBuilder();
		  sb.append("Dt Value");
	     

	       
	        sb.append('\n');
	        sb.append(webdriverTest.finalresult);
	        pw.write(sb.toString());
	        pw.close();
	        
	        
	}
	@Test(priority=5)
	public void TestCaseFive() throws FileNotFoundException
	
	{
		File file = new File("C:\\Users\\admin\\Desktop\\Output\\new 2.txt");
		try {
		    Scanner scanner = new Scanner(file);
		    
		    //now read the file line by line...
		    int lineNum = 0;
		   
		    	
		    
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        lineNum++;
		        if(line.contains("&dp=")) { 
		         //   System.out.println("dt parameneter found   !"+ lineNum );
		            try{
		          int index=  line.indexOf("&dp=");
		           System.out.println(index); 	
		            String result= line.substring(index, index+100);
		            System.out.println("Initial Scrap"+result);
		          
		            String [] newresultset = result.split("&");
		            for(int i = 0; i<newresultset.length ;i++){
		            	
		            	if(newresultset[i].contains("dt=")){
		            		webdriverTest.finalresult2=newresultset[i].split("=")[1].replaceAll("%20", " ").replaceAll("%7C", "|");
		            		System.out.println(finalresult2);
		            		
		            	}
		            }
		            
		            
		            }
		            catch(Exception ger)
		            {
		            	ger.printStackTrace();
		            }
		        }
		       
		        
		    }
		    
		} catch(FileNotFoundException e) { 
		    //handle this
		}
		
		 PrintWriter pw = new PrintWriter(new File("C:\\Users\\admin\\Desktop\\Output\\licious2.csv"));
		  StringBuilder sb = new StringBuilder();
		  sb.append("Dp Value");
	     

	       
	        sb.append('\n');
	        sb.append(webdriverTest.finalresult2);
	        pw.write(sb.toString());
	        pw.close();
	        
	        
	}
	
	@AfterTest
	public void exit()
	{
		proxy.stop();
		driver.quit();
	}
}