package com.ace.springBatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.ace.springBatch.batch.vo.Mobile;
import com.ace.springBatch.entity.Phone;

@Configuration
public class springBatchConfigToLoadCsvFileInDb {

	@Autowired
	private ResourceLoader resourceLoader;


	@Bean(name = "csvDbjob")
	public Job csvDbjob(JobBuilderFactory jobBuilderFactory,
			StepBuilderFactory stepBuilderFactory,
			@Qualifier("csvDbMobileReder") ItemReader<Mobile> reader,
			@Qualifier("csvDbMobileProcessor") ItemProcessor<Mobile, Phone> processor,
			@Qualifier("csvDbMobileWriter") ItemWriter<Phone> writer) {
		
		// Job can Have Multiple Step
		// One step consist of reader , processor and writer
		Step step = stepBuilderFactory
				.get("ETL_load_mobile")
				.<Mobile, Phone>chunk(5)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();

		Job job = jobBuilderFactory
				.get("ETL_load_mobile")
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
		
		return job;
	}

	
	//read from CSV file
	@Bean(name = "csvDbMobileReder")
	public FlatFileItemReader<Mobile> csvDbMobileReder() {
		final Resource resource = resourceLoader.getResource("classpath:phone.csv");

		FlatFileItemReader<Mobile> flatFileItemReader = new FlatFileItemReader<Mobile>();
		flatFileItemReader.setName("csv-reder");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setLineMapper(csvDblineMapper());
		return flatFileItemReader;
	}

	private LineMapper<Mobile> csvDblineMapper() {
		DefaultLineMapper<Mobile> defaultLineMapper = new DefaultLineMapper<Mobile>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "employeeId", "mobileNumber", "prefered" });
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		BeanWrapperFieldSetMapper<Mobile> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Mobile>();
		beanWrapperFieldSetMapper.setTargetType(Mobile.class);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;
	}

	

}
