/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class SampleCamelRouter extends RouteBuilder {

    @Override
	public void configure() throws Exception {
		from("timer:hello?period={{timer.period}}").transform(
				method("myBean", "saySomething")).to("stream:out");

		from("direct:property.id.route").id("property.id.route")
				.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.GET))
				.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
				.setHeader("Authorization",constant("Basic aW9zOmRzYXBzZWNyZXQxMjM="))
				
				.setHeader(Exchange.HTTP_PATH, body())
				.to("log:DEBUG?showBody=true&showHeaders=true")
				.to("{{property.id.endpoint}}v2/properties")
				.to("log:DEBUG?showBody=true&showHeaders=true")
				.process(new Processor() {
					
					@Override
					public void process(Exchange arg0) throws Exception {
						System.out.println(arg0.getIn().getBody(String.class));
					}
				});
	}

}
