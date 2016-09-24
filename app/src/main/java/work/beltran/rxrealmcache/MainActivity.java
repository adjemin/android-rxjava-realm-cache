package work.beltran.rxrealmcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import work.beltran.rxrealmcache.api.WeatherService;
import work.beltran.rxrealmcache.api.model.WeatherResponse;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Inject
    public WeatherService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplication()).component.inject(this);

        service.getWeather("Berlin", "test")
                .subscribeOn(Schedulers.io())
                .subscribe(this::display, this::processError);
    }

    private void processError(Throwable e) {
        Log.e(TAG, e.getLocalizedMessage(), e);
    }

    private void display(WeatherResponse response) {
        Log.d(TAG, response.toString());
    }
}
