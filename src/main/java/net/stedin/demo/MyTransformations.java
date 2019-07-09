package net.stedin.demo;

import org.apache.camel.Header;
import org.apache.camel.language.Simple;
import org.springframework.stereotype.Component;

@Component
public class MyTransformations {

    public String transform(MyObject myObject, @Header("Header1") String header1, @Simple("${body.field1}") String field1) {

        return String.join(",", field1, header1, myObject.getField2());
    }

}
