package hevs.aislab.magpie.watch.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by teuft on 07.07.2017.
 */

@Entity(
        nameInDb = "Rules",
        indexes = {
                @Index(value = "category", unique = true)
        }
)

public class CustomRules {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String category;

    private Double val_1_min;

    private Double val_1_max;

    private Double val__2_min;

    private Double val_2_max;

    private String constraint_1;

    private String constraint_2;

    private String constraint_3;

    private Long timeWindow;

    @Generated(hash = 1519990713)
    public CustomRules(Long id, @NotNull String category, Double val_1_min,
                       Double val_1_max, Double val__2_min, Double val_2_max,
                       String constraint_1, String constraint_2, String constraint_3,
                       Long timeWindow) {
        this.id = id;
        this.category = category;
        this.val_1_min = val_1_min;
        this.val_1_max = val_1_max;
        this.val__2_min = val__2_min;
        this.val_2_max = val_2_max;
        this.constraint_1 = constraint_1;
        this.constraint_2 = constraint_2;
        this.constraint_3 = constraint_3;
        this.timeWindow = timeWindow;
    }

    @Generated(hash = 922913921)
    public CustomRules() {
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

    public Double getVal_1_min() {
        return val_1_min;
    }

    public void setVal_1_min(Double val_1_min) {
        this.val_1_min = val_1_min;
    }

    public Double getVal_1_max() {
        return val_1_max;
    }

    public void setVal_1_max(Double val_1_max) {
        this.val_1_max = val_1_max;
    }

    public Double getVal__2_min() {
        return val__2_min;
    }

    public void setVal__2_min(Double val__2_min) {
        this.val__2_min = val__2_min;
    }

    public Double getVal_2_max() {
        return val_2_max;
    }

    public void setVal_2_max(Double val_2_max) {
        this.val_2_max = val_2_max;
    }

    public String getConstraint_1() {
        return constraint_1;
    }

    public void setConstraint_1(String constraint_1) {
        this.constraint_1 = constraint_1;
    }

    public String getConstraint_2() {
        return constraint_2;
    }

    public void setConstraint_2(String constraint_2) {
        this.constraint_2 = constraint_2;
    }

    public String getConstraint_3() {
        return constraint_3;
    }

    public void setConstraint_3(String constraint_3) {
        this.constraint_3 = constraint_3;
    }


    public Long getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(Long timeWindow) {
        this.timeWindow = timeWindow;
    }

}
