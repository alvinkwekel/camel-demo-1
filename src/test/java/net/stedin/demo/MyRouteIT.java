package net.stedin.demo;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DisableJmx
@UseAdviceWith
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MyRouteIT {

    @Autowired
    ModelCamelContext camelContext;

    @EndpointInject(uri = "activemq:exchangepattern.client")
    ProducerTemplate exchangePatternTester;

    @EndpointInject(uri = "direct:transformations")
    ProducerTemplate transformationsTester;

    @EndpointInject(uri = "direct:properties.client")
    ProducerTemplate propertiesTester;

    @EndpointInject(uri = "mock:end")
    MockEndpoint mock;

    @Before
    public void init() throws Exception {
        camelContext.getRouteDefinition("exchangePatternClient")
                .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        weaveAddLast().to(mock.getEndpointUri());
                    }
                });

        camelContext.getRouteDefinition("transformations")
                .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        weaveAddLast().to(mock.getEndpointUri());
                    }
                });

        camelContext.getRouteDefinition("propertiesServer")
                .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        weaveAddLast().to(mock.getEndpointUri());
                    }
                });

        camelContext.start();
    }

    @After
    public void clean() throws Exception {

        mock.reset();
        camelContext.stop();
    }

    @Test
    public void testExchangePattern() throws Exception {


        mock.expectedMessageCount(1);
        exchangePatternTester.requestBody("in");
        mock.assertIsSatisfied(2000);
    }

    @Test
    public void testTransformations() throws Exception {

        mock.expectedMessageCount(1);

        MyObject myObject = MyObject.builder().field1("value1").field2("value2").build();
        transformationsTester.requestBodyAndHeader(myObject, "Header1", "headervalue1");
        mock.assertIsSatisfied();
    }

    @Test
    public void testProperties() throws Exception {

        mock.expectedMessageCount(1);
        propertiesTester.sendBody(null);
        mock.assertIsSatisfied();
    }
}
