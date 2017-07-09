package com.github.jcustenborder.kafka.connect.transform;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractPrimitive;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.NoValidation;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class HL7Test {
  private static final Logger log = LoggerFactory.getLogger(HL7Test.class);

  @TestFactory
  Stream<DynamicTest> parse() throws IOException {
    List<String> inputFiles = Arrays.asList(
        "ORU_R01_Long.txt",
        "ORU_R01_Medium.txt",
        "VXQ_V0_Vaccine_Query.txt",
        "VXX_V02_Vaccine_Query_Response_Multiple_Matches.txt"
    );
//
//    Map<String, String> tests = new LinkedHashMap<>();
//    for (String inputFile : inputFiles) {
//      try (InputStream inputStream = this.getClass().getResourceAsStream(inputFile)) {
//        String text = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
//        tests.put(inputFile, text);
//      }
//    }

    return inputFiles.stream().map(inputFile -> {
      InputStream inputStream = this.getClass().getResourceAsStream(inputFile);
      String text;
      try {
        text = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
      return dynamicTest(inputFile, () -> test(text));
    });

  }

  void test(String input) throws HL7Exception {
    HapiContext context = new DefaultHapiContext();
    context.setValidationContext(new NoValidation());
    context.setModelClassFactory(new CanonicalModelClassFactory("2.6"));
    Parser parser = context.getPipeParser();

//    parser.get

    Message message = parser.parse(input);

    System.out.println(message.getName());

    System.out.println(message);
    System.out.println(message.getClass());
  }

  @Test
  public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage("ca.uhn.hl7v2.model"))
        .setScanners(new SubTypesScanner())
        .filterInputsBy(
            new FilterBuilder().includePackage("ca.uhn.hl7v2.model").excludePackage("ca.uhn.hl7v2.model.primitive")
        ));


    Set<Class<? extends AbstractPrimitive>> subTypes = reflections.getSubTypesOf(AbstractPrimitive.class);

    for (Class<? extends AbstractPrimitive> primitiveType : subTypes) {
      Constructor<? extends AbstractPrimitive> constructor = primitiveType.getDeclaredConstructor(Message.class);
      Message message = mock(Message.class, withSettings().verboseLogging());

      log.info("creating instance of {}", primitiveType);
      AbstractPrimitive abstractPrimitive = constructor.newInstance(message);

      log.info("name={}", abstractPrimitive.getName());
    }







//    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SomeAnnotation.class);


  }


  class TestCase {
    public String input;

  }

}
