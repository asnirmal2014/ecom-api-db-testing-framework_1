package com.webprj.ecom.tests.base;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.io.InputStream;
import java.util.Properties;

public class BaseTest 
{
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeClass
    public void setup() throws Exception 
    {
        Properties prop = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
        
        if (input == null) 
        {
            throw new RuntimeException("❌ config.properties file not found!");
        }
        
        prop.load(input);
        RestAssured.baseURI = prop.getProperty("base.url");
        
        System.out.println("✅ RestAssured.baseURI set to: " + RestAssured.baseURI);
        log.info("✅ RestAssured.baseURI: {}", RestAssured.baseURI);

        System.out.println("🔄 No automatic database cleanup enabled.");
        System.out.println("📊 Test data will be preserved in the database.");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() 
    {
        System.out.println("✅ Test class completed successfully.");
        System.out.println("📊 Test data (except deleted records) has been preserved in the database.");
        log.info("✅ Test class teardown completed - data preserved.");
    }
}