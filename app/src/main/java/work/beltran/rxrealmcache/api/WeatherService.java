package work.beltran.rxrealmcache.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import work.beltran.rxrealmcache.api.model.WeatherResponse;

/**
 * Created by Miquel Beltran on 9/24/16
 * More on http://beltran.work
 */
public interface WeatherService {
    @GET("weather")
    Observable<WeatherResponse> getWeather(@Query("q") String city, @Query("apikey") String apiKey);
}
