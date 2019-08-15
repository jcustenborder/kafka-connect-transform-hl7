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

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import com.google.common.collect.ImmutableSet;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class SchemaConverter {
  static final Set<Class<?>> SKIP_TYPES = ImmutableSet.copyOf(
      Arrays.asList(
          ClassLoader.class,
          Class.class,
          java.util.TimeZone.class,
          org.hl7.fhir.dstu3.model.Extension.class,
          org.hl7.fhir.dstu3.model.Type.class
      )
  );
  static final Set<Class<?>> ALLOWED_TYPES = ImmutableSet.copyOf(
      Arrays.asList(
          org.hl7.fhir.dstu3.model.Type.class
      )
  );
  private static final Logger log = LoggerFactory.getLogger(SchemaConverter.class);
  final Map<Class<? extends IElement>, Schema> schemas;

  public SchemaConverter() {
    schemas = new HashMap<>();
  }

  SchemaBuilder builder(Class<? extends IElement> type, Description description) {
    SchemaBuilder builder = null;

    if (org.hl7.fhir.instance.model.api.IPrimitiveType.class.isAssignableFrom(type)) {
      log.trace("primitive type");
    } else {
      log.trace("not primitive type");
    }

    return builder;
  }

  Schema convert(Class<? extends IElement> cls) {

    Schema result = this.schemas.computeIfAbsent(cls, c -> {
      log.trace("convert() - Building schema for {}", c.getName());

      final List<FieldAttributes> fields = Arrays.stream(c.getDeclaredFields())
          .filter(field -> null != field.getAnnotation(Child.class))
          .map(field -> new FieldAttributes(field))
          .sorted(Comparator.comparingInt(o -> o.child.order()))
          .collect(Collectors.toList());

      final SchemaBuilder builder = SchemaBuilder.struct()
          .parameter("className", cls.getName())
          .name(cls.getName());
      for (FieldAttributes field : fields) {
        Class<? extends IElement> firstType = field.child.type()[0];


        log.trace("convert() - field = {} ", field.name, field.child.type());

//        field.child.
      }


      return builder.build();
    });

    return result;
  }

  static class FieldAttributes {
    public final String name;
    public final Description description;
    public final Child child;
    public final Field field;

    FieldAttributes(Field field) {
      this.field = field;
      this.description = field.getAnnotation(Description.class);
      this.child = field.getAnnotation(Child.class);
      this.name = this.child.name();
    }
  }
}
