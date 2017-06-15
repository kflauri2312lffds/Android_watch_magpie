package hevs.aislab.magpie.watch.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
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
    @Index
    private String category;

    private Double value1;
    private Double value2;
    @NotNull
    private long timeStamp;


    @Generated(hash = 694358598)
    public Measure(Long id, @NotNull String category, Double value1, Double value2,
            long timeStamp) {
        this.id = id;
        this.category = category;
        this.value1 = value1;
        this.value2 = value2;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
