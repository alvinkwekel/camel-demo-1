package net.stedin.demo;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("activemq:exchangepattern.client").routeId("exchangePatternClient")
                .to("log:1")
                .to(ExchangePattern.InOut,"activemq:exchangepattern.server")
                .to("log:2")
                .to("stream:out");

        from("activemq:exchangepattern.server").routeId("exchangePatternServer")
                .setBody(constant("in2"))
                .to("log:3")
                .process(e -> e.getOut().setBody("out2"));


        from("direct:transformations").routeId("transformations")
                .transform(method(MyTransformations.class, "transform(${header.header1, })"))
                .to("stream:out");


        from("direct:properties.client").routeId("propertiesClient")
                .setHeader("MyHeader", constant("headerValue"))
                .setProperty("MyProp", constant("propValue"))
                .to("direct:properties.server");

        from("direct:properties.server").routeId("propertiesServer")
                .log("${header.MyHeader}")
                .log("${exchangeProperty.MyProp}");
    }
}
