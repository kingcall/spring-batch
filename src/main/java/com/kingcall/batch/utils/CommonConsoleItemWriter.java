package com.kingcall.batch.utils;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonConsoleItemWriter implements ItemWriter {
    @Override
    public void write(List list) throws Exception {
        list.forEach(line -> System.out.println(String.format("current item is %s", line.toString())));
    }
}
