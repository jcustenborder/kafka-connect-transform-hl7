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

import ca.uhn.fhir.context.FhirVersionEnum;
import com.github.jcustenborder.kafka.connect.utils.config.ConfigUtils;
import com.github.jcustenborder.kafka.connect.utils.config.ValidEnum;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

class FromFHIRConfig extends AbstractConfig {
  public static final String FHIR_VERSION_CONF = "fhir.version";
  public static final String FHIR_INPUT_FORMAT_CONF = "fhir.input.format";
  static final String FHIR_VERSION_DOC = "The FHIR version.";
  static final String FHIR_INPUT_FORMAT_DOC = "The format of the incoming data.";
  public final FhirVersionEnum version;
  public final InputFormat inputFormat;


  public FromFHIRConfig(Map<?, ?> originals) {
    super(config(), originals);
    this.version = ConfigUtils.getEnum(FhirVersionEnum.class, this, FHIR_VERSION_CONF);
    this.inputFormat = ConfigUtils.getEnum(InputFormat.class, this, FHIR_INPUT_FORMAT_CONF);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(FHIR_VERSION_CONF, ConfigDef.Type.STRING, ConfigDef.NO_DEFAULT_VALUE, ValidEnum.of(FhirVersionEnum.class), ConfigDef.Importance.HIGH, FHIR_VERSION_DOC)
        .define(FHIR_INPUT_FORMAT_CONF, ConfigDef.Type.STRING, ConfigDef.NO_DEFAULT_VALUE, ValidEnum.of(InputFormat.class), ConfigDef.Importance.HIGH, FHIR_INPUT_FORMAT_DOC);
  }

  public enum InputFormat {
    JSON,
    XML
  }
}
