package com.github.jcustenborder.kafka.connect.transform;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

public class HL7 implements Transformation<SourceRecord> {
  @Override
  public SourceRecord apply(SourceRecord sourceRecord) {
    return null;
  }

  @Override
  public ConfigDef config() {
    return null;
  }

  @Override
  public void close() {



  }

  @Override
  public void configure(Map<String, ?> map) {

  }
}
