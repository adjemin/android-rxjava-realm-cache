package work.beltran.rxrealmcache.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Miquel Beltran on 9/25/16
 * More on http://beltran.work
 */
public class WeatherRealm extends RealmObject {
    @PrimaryKey
    private String name;
    private Double temp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
