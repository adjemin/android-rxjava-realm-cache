package work.beltran.rxrealmcache;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);

        component = DaggerWeatherComponent
                .builder()
                .weatherServiceModule(new WeatherServiceModule(getString(R.string.base_url)))
                .build();
    }
}
