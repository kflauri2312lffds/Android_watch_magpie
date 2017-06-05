package hevs.aislab.magpie.watch.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by teuft on 05.06.2017.
 */

@Entity(
        nameInDb = "Rules",
        indexes = {
                @Index(value = "category,maxValue,minValue", unique = true)
        }
)
public class Rules {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    @Index
    private String category;
    @NotNull
    private double maxValue;
    @NotNull
    private double minValue;
    @NotNull
    private int severity;
    @NotNull
    private String description;

    @Generated(hash = 1249226838)
    public Rules(Long id, @NotNull String category, double maxValue,
            double minValue, int severity, @NotNull String description) {
        this.id = id;
        this.category = category;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.severity = severity;
        this.description = description;
    }

    @Generated(hash = 1692174665)
    public Rules() {
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

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





}
