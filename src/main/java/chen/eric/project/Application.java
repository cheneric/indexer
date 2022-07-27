package chen.eric.project;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executor;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {
	private Log log;

	protected void setLog(Log log) {
		this.log = log;
	}

	@Bean
	@Scope("prototype")
	// https://medium.com/simars/inject-loggers-in-spring-java-or-kotlin-87162d02d068
	public Log provideLog(final InjectionPoint injectionPoint) {
		return LogFactory.getLog(
			of(injectionPoint.getMethodParameter())
				.<Class>map(MethodParameter::getContainingClass)
				.orElseGet(() ->
					ofNullable(injectionPoint.getField())
						.map(Field::getDeclaringClass)
						.orElseThrow(IllegalArgumentException::new)
				)
		);
	}

	@Bean
	public Executor taskExecutor(
		@Value("${project.taskExecutor.corePoolSize}") int taskExecutorCorePoolSize,
		@Value("${project.taskExecutor.maxPoolSize}") int taskExecutorMaxPoolSize,
		@Value("${project.taskExecutor.queueCapacity}") int taskExecutorQueueCapacity,
		@Value("${project.taskExecutor.threadNamePrefix}") String threadNamePrefix)
	{
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(taskExecutorCorePoolSize);
		executor.setMaxPoolSize(taskExecutorMaxPoolSize);
		executor.setQueueCapacity(taskExecutorQueueCapacity);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.initialize();
		return executor;
	}

	@Override
	public void run(String... args) throws IOException {
		final String defaultDir = "src/test/resources/chen/eric/project/skilling-j 2";
		String dir;
		if (args == null || args.length == 0) {
			System.out.println("Usage:  run <directory>; <directory> is required");
			System.out.println("Using default dir: " + defaultDir);
			dir = defaultDir;
		}
		else {
			dir = args[0];
		}
		final File file = new File(dir);
		final Parser parser = new Parser();
		parser.parseDirectory(file);

		acceptQueries(parser);
	}

	public void acceptQueries(Parser parser) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			String term;
			while (true) {
				System.out.println("Search term? ");
				term = scanner.next();
				if (!term.isBlank()) {
					final Set<File> matchingFiles = parser.search(term);
					for (final File matchingFile : matchingFiles) {
						System.out.println(matchingFile.getCanonicalPath());
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void main(String... args) {
		final ApplicationContext context = SpringApplication.run(Application.class, args);
		final Log log = LogFactory.getLog(Application.class);
		log.debug("Beans provided by Spring Boot:");
		final String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		Arrays.stream(beanNames)
			.forEach(log::trace);
	}
}
