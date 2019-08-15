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

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.hl7.fhir.dstu3.model.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.jcustenborder.kafka.connect.utils.AssertSchema.assertSchema;

public class SchemaConverterTest {
  private static final Logger log = LoggerFactory.getLogger(SchemaConverterTest.class);
  SchemaConverter converter;

  @BeforeEach
  public void before() {
    this.converter = new SchemaConverter();
  }

  @Test
  public void convert() {
    final Schema expected = SchemaBuilder.struct()
        .name("org.hl7.fhir.dstu3.model.Identifier")
        .parameter("className", "org.hl7.fhir.dstu3.model.Identifier")
        .build();

    final Schema schema = this.converter.convert(Identifier.class);
    log.info("Schema {}", schema);
    assertSchema(expected, schema);
  }

}
