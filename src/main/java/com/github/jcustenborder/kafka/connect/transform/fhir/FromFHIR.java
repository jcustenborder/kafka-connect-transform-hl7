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
import ca.uhn.fhir.parser.IParser;
import com.github.jcustenborder.kafka.connect.utils.config.Description;
import com.github.jcustenborder.kafka.connect.utils.config.Title;
import com.github.jcustenborder.kafka.connect.utils.transformation.BaseKeyValueTransformation;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.DataException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Title("FromFHIR(Key)")
@Description("This transformation is used to rename fields in the key of an input struct based on a regular expression and a replacement string.")
public abstract class FromFHIR<R extends ConnectRecord<R>> extends BaseKeyValueTransformation<R> {
  private static final Logger log = LoggerFactory.getLogger(FromFHIR.class);

  protected FromFHIRConfig config;
  FhirContext context;
  IParser parser;

  protected FromFHIR(boolean isKey) {
    super(isKey);
  }

  @Override
  public ConfigDef config() {
    return FromFHIRConfig.config();
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> settings) {
    this.config = new FromFHIRConfig(settings);
    log.info("Creating FhirContext with version {}", this.config.version);
    this.context = new FhirContext(this.config.version);

    switch (this.config.inputFormat) {
      case XML:
        this.parser = this.context.newXmlParser();
        break;
      case JSON:
        this.parser = this.context.newJsonParser();
        break;
      default:
        throw new UnsupportedOperationException(
            String.format("%s is not a supported format", this.config.inputFormat)
        );
    }
  }

  @Override
  protected SchemaAndValue processBytes(R record, Schema inputSchema, byte[] input) {
    final IBaseResource resource;
    try (InputStream inputStream = new ByteArrayInputStream(input)) {
      try (Reader reader = new InputStreamReader(inputStream)) {
        resource = this.parser.parseResource(reader);
      }
    } catch (IOException e) {
      throw new DataException(
          "Exception thrown while parsing resource",
          e
      );
    }
    return null;
  }

  @Override
  protected SchemaAndValue processString(R record, Schema inputSchema, String input) {
    final IBaseResource resource = this.parser.parseResource(input);
    return null;
  }


  public static class Key<R extends ConnectRecord<R>> extends FromFHIR<R> {
    public Key() {
      super(true);
    }
  }

  public static class Value<R extends ConnectRecord<R>> extends FromFHIR<R> {
    public Value() {
      super(true);
    }
  }
}