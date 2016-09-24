package work.beltran.rxrealmcache;

import android.app.Application;

import work.beltran.rxrealmcache.di.WeatherServiceModule;

/**
 * Created by Miquel Beltran on 9/24/16
 * More on http://beltran.work
 */
public class App extends Application {

    public WeatherComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerWeatherComponent
                .builder()
                .weatherServiceModule(new WeatherServiceModule(getString(R.string.base_url)))
                .build();
    }
}
