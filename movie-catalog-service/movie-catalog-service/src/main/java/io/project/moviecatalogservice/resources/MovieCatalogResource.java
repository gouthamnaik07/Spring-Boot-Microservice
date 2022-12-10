package io.project.moviecatalogservice.resources;

import io.project.moviecatalogservice.modules.Movie;
import io.project.moviecatalogservice.modules.Rating;
import io.project.moviecatalogservice.modules.CatalogItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import io.project.moviecatalogservice.modules.UserRating;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){



        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/foo"+ userId,UserRating.class);

        /*Arrays.asList(
                new Rating("1234",4),
                new Rating("5678",3)
        ); */

        return ratings.getUserRating().stream().map(rating-> {
           /// for each movie ID , call movie info service and get details
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

    /*
                Movie movie = webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8082/movies/" + rating.getMovieId())
                        .retrieve()
                        .bodyToMono(Movie.class)
                        .block();
    */

            ///        putting them all together
                    return new CatalogItem(movie.getName(), "Test", rating.getRating());
                })
                .collect(Collectors.toList());

    }
}

