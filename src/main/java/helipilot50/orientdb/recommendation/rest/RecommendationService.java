package helipilot50.orientdb.recommendation.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

import helipilot50.orientdb.recommendation.Constants;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RecommendationService {
	private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);
	;
	
	@Bean(destroyMethod = "close") public OrientGraphFactory graphFactory(){
		Properties props = System.getProperties();
		String dbURL = (String) props.get("db");
		if (dbURL == null || dbURL.isEmpty())
			dbURL = Constants.DEFAULT_DB;
		OrientGraphFactory factory = new OrientGraphFactory(dbURL);
		return factory;
	}


	
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		return new MultipartConfigElement("");
	}


	public static void main(String[] args) throws ParseException, IOException{

		Options options = new Options();
		options.addOption("db", "database", true, "Database string: " + Constants.DEFAULT_DB);
		options.addOption("u", "usage", false, "Print usage.");
		
		// parse the command line args
		CommandLineParser parser = new PosixParser();
		CommandLine cl = parser.parse(options, args, false);

		if (cl.hasOption("u")) {
			logUsage(options);
			return;
		}
		String db = cl.getOptionValue("db", Constants.DEFAULT_DB);
		log.info("Database: " + db);	
		
			// run as a RESTful service
			// set properties
			Properties as = System.getProperties();
			
			as.put("dataBase", db);

			// start app
			SpringApplication.run(RecommendationService.class, args);
	}
	private static void logUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String syntax = RecommendationService.class.getName() + " [<options>]";
		formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
		log.info(sw.toString());
	}

}
