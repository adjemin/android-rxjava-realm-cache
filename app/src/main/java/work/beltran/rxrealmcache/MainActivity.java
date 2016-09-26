package work.beltran.rxrealmcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import work.beltran.rxrealmcache.api.WeatherService;
import work.beltran.rxrealmcache.api.model.WeatherResponse;
import work.beltran.rxrealmcache.realm.WeatherRealm;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Inject
    public WeatherService service;

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_temp)
    TextView textTemp;

    private Realm realmUI;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).component.inject(this);

        realmUI = Realm.getDefaultInstance();

        String name = "Berlin";

        // Request API data on IO Scheduler
        Observable<WeatherRealm> observable =
                service.getWeather(name, getString(R.string.api_key))
                        // One second delay for demo purposes
                        .delay(1L, java.util.concurrent.TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        // Write to Realm on Computation scheduler
                        .observeOn(Schedulers.computation())
                        .map(this::writeToRealm)
                        // Read results in Android Main Thread (UI)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(this::readFromRealm);

        // Read any cached results
        WeatherRealm cachedWeather = readFromRealm(name);
        if (cachedWeather != null)
            // Merge with the observable from API
            observable = observable.mergeWith(Observable.just(cachedWeather));

        // Subscription happens on Main Thread
        subscription = observable.subscribe(this::display, this::processError);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        realmUI.close();
    }

    private WeatherRealm readFromRealm(String name) {
        return findInRealm(realmUI, name);
    }

    private String writeToRealm(WeatherResponse weatherResponse) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transactionRealm -> {
            WeatherRealm weatherRealm = findInRealm(transactionRealm, weatherResponse.getName());
            if (weatherRealm == null)
                weatherRealm = transactionRealm.createObject(WeatherRealm.class, weatherResponse.getName());
            weatherRealm.setTemp(weatherResponse.getMain().getTemp());
        });
        realm.close();
        return weatherResponse.getName();
    }

    private WeatherRealm findInRealm(Realm realm, String name) {
        return realm.where(WeatherRealm.class).equalTo("name", name).findFirst();
    }

    private void processError(Throwable e) {
        Log.e(TAG, e.getLocalizedMessage(), e);
    }

    private void display(WeatherRealm weatherRealm) {
        Log.d(TAG, "City: " + weatherRealm.getName() + ", Temp: " + weatherRealm.getTemp());
        textName.setText(weatherRealm.getName());
        textTemp.setText(String.valueOf(weatherRealm.getTemp()));
    }
}
