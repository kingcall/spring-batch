package com.kingcall.batch.Processor;

import com.kingcall.batch.models.Person;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.FileSystemResource;

public class CommonXmlReaderProcessor<T> extends StaxEventItemReader<T> {
    public CommonXmlReaderProcessor(String path, String rootName) {
        setName("xmlReader");
        setResource(new FileSystemResource(path));
        setFragmentRootElementName(rootName);
    }
}
