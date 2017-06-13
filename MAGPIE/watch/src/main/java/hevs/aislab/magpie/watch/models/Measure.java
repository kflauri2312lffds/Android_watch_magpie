package hevs.aislab.magpie.watch.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by teuft on 31.05.2017.
 */

/**
*
* Entity that represent a measure that has been taken, like the pulse, glucose level.
* */
@Entity(
        nameInDb = "Measures"
)
public class Measure {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String category;
    @NotNull
    private double value;
    @NotNull
    private long timeStamp;






    @Generated(hash = 1406889111)
    public Measure(Long id, @NotNull String category, double value, long timeStamp) {
        this.id = id;
        this.category = category;
        this.value = value;
        this.timeStamp = timeStamp;
    }

    @Generated(hash = 1840334633)
    public Measure() {
    }






    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String name) {
        this.category = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
