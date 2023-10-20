package labcqrs.common;

import io.cucumber.spring.CucumberContextConfiguration;
import labcqrs.DeliveryApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { DeliveryApplication.class })
public class CucumberSpingConfiguration {}
