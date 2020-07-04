package com.kingcall.batch.job.itemReaderAndWriter.reader;
import	java.util.HashMap;
import	java.util.Map;

import com.kingcall.batch.models.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class JdbcBatchItemReader {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;


    @Bean
    public Job jdbcItemReaderJob() {
        return jobBuilderFactory
                .get(this.getClass().getSimpleName())
                .start(jdbcItemReaderJobStep1())
                .build()
                ;
    }

    @Bean
    public Step jdbcItemReaderJobStep1() {
        return stepBuilders
                .get("itemReaderJobReadStep1")
                .<Person, Person>chunk(1)
                .reader(jdbcReader())
                .writer(list -> {
                    list.forEach(line -> System.out.println(String.format("current item is %s", line.toString())));
                })
                .build()
                ;
    }

    @Bean
    public JdbcPagingItemReader<Person> jdbcReader() {
        JdbcPagingItemReader<Person> jdbcPagingItemReader = new JdbcPagingItemReader<>();
        jdbcPagingItemReader.setDataSource(dataSource);
        jdbcPagingItemReader.setFetchSize(100);
        jdbcPagingItemReader.setRowMapper(((resultSet, row) -> {
            return Person
                    .builder()
                    .firstName(resultSet.getString("firstname"))
                    .lastName(resultSet.getString("lastname"))
                    .build();
        }));
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setFromClause("from person");
        provider.setSelectClause("id,firstName,lastname");
        Map<String, Order> order = new HashMap<> ();
        order.put("id", Order.DESCENDING);
        provider.setSortKeys(order);
        jdbcPagingItemReader.setQueryProvider(provider);
        return jdbcPagingItemReader;
    }
}

