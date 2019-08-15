/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.kafka.connect.transform.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//import org.hl7.fhir.instance.model.HumanName;
//import org.hl7.fhir.instance.model.Identifier;
//import org.hl7.fhir.instance.model.Patient;

public class FromFHIRTest {
  private static final Logger log = LoggerFactory.getLogger(FromFHIRTest.class);

  @Test
  public void test() throws IOException {
    FhirContext context = new FhirContext(FhirVersionEnum.DSTU3);
    IParser parser = context.newJsonParser();
    parser.setPrettyPrint(true);
    Patient patient;
    try (InputStream inputStream = this.getClass().getResourceAsStream("json/patient-example.json")) {
      try (Reader reader = new InputStreamReader(inputStream)) {
        patient = parser.parseResource(Patient.class, reader);
      }
    }

//    patient.getName().get(0).

    IBaseResource resource;

    try (InputStream inputStream = this.getClass().getResourceAsStream("json/patient-example.json")) {
      try (Reader reader = new InputStreamReader(inputStream)) {
        resource = parser.parseResource(reader);
      }
    }
    assertNotNull(resource);

  }
}
