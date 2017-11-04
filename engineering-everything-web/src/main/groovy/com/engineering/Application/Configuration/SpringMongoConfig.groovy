package com.engineering.Application.Configuration;

import com.mongodb.Mongo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableMongoRepositories
@EnableConfigurationProperties(MongoProperties.class)
public class SpringMongoConfig extends AbstractMongoConfiguration {


	@Autowired
	private MongoProperties props;



	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception{

		String host = props.getHost();
		int port = props.getPort();
		String db   = props.getDatabase();

		return new SimpleMongoDbFactory(new MongoClient(host, port), db);
	}

	 @Bean(name = "mongoTemplate")
	 public MongoTemplate mongoTemplate() throws Exception {

		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
	    mongoTemplate.indexOps("profilepictures.files").ensureIndex(new Index().on("filename", Sort.Direction.ASC).unique());
		return mongoTemplate;

	}
	@Override
	protected String getDatabaseName() {
		return "mydatabase";
	}

    @Bean(name = "assignments")
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"assignments");
    }

    @Bean(name = "notes")
    public GridFsTemplate gridFsTemplate1() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"notes");
    }

    @Bean(name = "questionpapers")
    public GridFsTemplate gridFsTemplate2() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"questionpapers");
    }

    @Bean(name = "syllabus")
    public GridFsTemplate gridFsTemplate3() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"syllabus");
    }

    @Bean(name = "timeline-files")
    public GridFsTemplate gridFsTemplate4() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"timeline-files");
    }

	@Bean(name = "coachingcentres-files")
	public GridFsTemplate gridFsTemplate5() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"coachingcentres-files");
	}

	@Bean(name = "profilepictures")
	public GridFsTemplate gridFsTemplate6() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"profilepictures");
	}

	@Bean(name= "TeacherAssignmentUpload")
	public GridFsTemplate gridFsTemplate7() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter(),"TeacherAssignmentUpload");
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient(props.getHost());
	}
		
}