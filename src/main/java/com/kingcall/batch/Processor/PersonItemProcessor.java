package com.kingcall.batch.Processor;

import com.kingcall.batch.models.Person;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

@Log4j2
public class PersonItemProcessor implements ItemProcessor<Person, String> {

        private static final Logger LOGGER = LoggerFactory.getLogger(PersonItemProcessor.class);

        @Override
        public String process(Person person) {
            String greeting = "Hello " + person.getFirstName() + " "
                    + person.getLastName() + "!";

            LOGGER.info("converting '{}' into '{}'", person, greeting);
            return greeting;
        }

}
