package work.beltran.rxrealmcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import work.beltran.rxrealmcache.api.WeatherService;
import work.beltran.rxrealmcache.api.model.WeatherResponse;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Inject
    public WeatherService service;

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_temp)
    TextView textTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).component.inject(this);

        // Request API data on IO Scheduler
        service.getWeather("Berlin", getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::display, this::processError);
        
    }

    private void processError(Throwable e) {
        Log.e(TAG, e.getLocalizedMessage(), e);
    }

    private void display(WeatherResponse response) {
        Log.d(TAG, "City: " + response.getName() + ", Temp: " + response.getMain().getTemp());
        textName.setText(response.getName());
        textTemp.setText(String.valueOf(response.getMain().getTemp()));
    }
}
