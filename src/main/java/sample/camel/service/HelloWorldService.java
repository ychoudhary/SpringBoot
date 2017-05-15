package sample.camel.service;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.CamelRouteContextFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sample.camel.bean.Greeting;


@RestController
public class HelloWorldService {

	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @Autowired
    private ProducerTemplate camelRoute;

	
	@RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
	
	@RequestMapping("/properties/{propertyId}")
	public String getPropertyById(@PathVariable String propertyId){
		Exchange exchange = (Exchange) camelRoute.requestBody("direct:property.id.route", propertyId);
		return (String)exchange.getIn().getBody(); 
	}
}
