package net.stedin.demo;

import org.apache.camel.Converter;

@Converter
public class MyTypeConverter {

    @Converter
    public static String toString(MyObject myObject) {
        return String.join(",", "myTypeConverter: ", myObject.getField1(), myObject.getField2());
    }
}
