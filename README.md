# Message exchange patterns
sendBody - to: in
inOut: in2
getOut().setBody("out2): out2
requestBody: out2
jms:queue?exchangePattern=InOut (client and server)
.to(ExchangePattern.InOut, "activemq:exchangepattern.server")

# Transforming
Expression Language Annotations
Inspect Camel context

# Type converter registry
https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.2/html/apache_camel_development_guide/TypeConv-Impl#TypeConv-Impl-PTC
Use for data format not for data model transformations

# Properties and headers
Use direct component
Objects as properties using parallel splitter

# Integration tests
@UseAdviceWith
ProducerTemplate
MockEndpoint
adviceWith