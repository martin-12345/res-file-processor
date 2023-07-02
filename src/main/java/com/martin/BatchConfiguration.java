package com.martin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	@StepScope
	public FileCallbackHandler headerLineCallback(){
		return new FileCallbackHandler();
	}

	@Bean
	public Job importUserJob(JobNotificationListener listener) {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.start(initialStep())
				.next(subStep())
				.build();
	}

	@Bean
	public Step initialStep(){
		return stepBuilderFactory.get("initialStep")
				.tasklet(fileDeletingTasklet())
				.build();
	}


	@Bean
	public FileDeletingTasklet fileDeletingTasklet() {
		return new FileDeletingTasklet();
	}

@Bean
	@Qualifier("subStep")
	public Step subStep() {

		return stepBuilderFactory.get("subStep")
				.<Person, Person>chunk(10)
				.reader(personItemReader(null))
				.processor(processor())
				.writer(personItemWriter("header", null))
				.listener(headerLineCallback())
				.listener(linecounter())
				.build();
	}

	private ChunkListener linecounter() {
		return new LineCounter();
	}

	@Bean
	@StepScope
	@Qualifier("personItemReader")
	public FlatFileItemReader<Person> personItemReader(@Value("#{jobParameters['input.file.name']}") String filename) {

		logger.debug("Filename={}",filename);

		return new FlatFileItemReaderBuilder<Person>()
				.name("personItemReader")
				.resource(new FileSystemResource(filename))
				.delimited()
				.names("firstName", "lastName")
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
					setTargetType(Person.class);
				}})
				.linesToSkip(1)
				.skippedLinesCallback(headerLineCallback())
				.build();
	}

	@Bean
	@StepScope
	@Qualifier("personItemWriter")
	public FlatFileItemWriter<Person> personItemWriter(@Value("#{stepExecutionContext[header]}") String header, @Value("#{jobExecutionContext['output.file']}") String outputFile) {


		logger.debug("output file={}", outputFile);

		return new FlatFileItemWriterBuilder<Person>()
				.name("personItemWriter")
				.resource(new FileSystemResource(outputFile))
				.append(false)
				.headerCallback(outputHeaderCallback(header))
				.lineAggregator(new DelimitedLineAggregator<Person>() {
					/*
                    Gets passed an object, in this case a Person object and the LineAggregator extracts the attribute listed
                    in the setNames below (by calling the getters by means of the BeanWrapperFieldExtractor), aggregates the
                    values, separated by comma, the delimiter, and the FileWriter writes the line to the output file.
                     */
					{
						setDelimiter(",");
						setFieldExtractor(new BeanWrapperFieldExtractor<Person>() {
							{
								setNames(new String[]{"firstName", "lastName", "value"});
							}
						});
					}
				}).build();
	}

	public FlatFileHeaderCallback outputHeaderCallback(String header) {
		return new OutputHeaderCallback(header);
	}
}
