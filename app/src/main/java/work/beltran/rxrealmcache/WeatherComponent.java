package work.beltran.rxrealmcache;

import javax.inject.Singleton;

import dagger.Component;
import work.beltran.rxrealmcache.di.WeatherServiceModule;

/**
 * Created by Miquel Beltran on 9/24/16
 * More on http://beltran.work
 */
@Singleton
@Component(modules = {
        WeatherServiceModule.class
})
public interface WeatherComponent {
    void inject(MainActivity activity);
}
