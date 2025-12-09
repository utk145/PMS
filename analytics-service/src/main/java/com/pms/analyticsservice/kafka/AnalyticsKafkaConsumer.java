package com.pms.analyticsservice.kafka;

import analytics.events.PatientEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsKafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // perform business operation related to analytics here...

            logger.info("Analytics event received: {}", patientEvent);

        } catch (InvalidProtocolBufferException e) {
            logger.error("Error parsing patient event from Kafka: {}", e.toString());
        }

    }

}
