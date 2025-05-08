package com.champlain.artistservice;

import com.champlain.artistservice.dataaccesslayer.Artist;
import com.champlain.artistservice.dataaccesslayer.ArtistIdentifier;
import com.champlain.artistservice.dataaccesslayer.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

import java.util.List;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class
})
public class ArtistServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtistServiceApplication.class, args);
    }

    @Autowired
    MongoOperations mongoTemplate;
    @EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {
        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate.getConverter().getMappingContext();
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);
        IndexOperations indexOps = mongoTemplate.indexOps(Artist.class);
        resolver.resolveIndexFor(Artist.class).forEach(e -> indexOps.ensureIndex(e));
    }

    @Bean
    CommandLineRunner initData(ArtistRepository artistRepository) {
        return args -> {
            artistRepository.deleteAll();
            if (artistRepository.count() == 0) {
                artistRepository.saveAll(List.of(
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440000"),
                                "Stefani",
                                "Germanotta",
                                "Lady Gaga"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440001"),
                                "Marshall",
                                "Mathers",
                                "Eminem"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440002"),
                                "Aubrey",
                                "Graham",
                                "Drake"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440003"),
                                "Robyn",
                                "Fenty",
                                "Rihanna"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440004"),
                                "Shawn",
                                "Carter",
                                "Jay-Z"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440005"),
                                "Katherine",
                                "Hudson",
                                "Katy Perry"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440006"),
                                "Calvin",
                                "Broadus",
                                "Snoop Dogg"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440007"),
                                "Alicia",
                                "Cook",
                                "Alicia Keys"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440008"),
                                "Eric",
                                "Bishop",
                                "Jamie Foxx"
                        ),
                        new Artist(
                                new ArtistIdentifier("550e8400-e29b-41d4-a716-446655440009"),
                                "Reginald",
                                "Dwight",
                                "Elton John"
                        )
                ));
            }
        };
    }
}
