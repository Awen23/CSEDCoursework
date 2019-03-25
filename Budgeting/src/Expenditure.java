import java.time.LocalDateTime;

public class Expenditure {
    private String category;
    private float amount;
    private LocalDateTime datetime;

    public Expenditure(String c, float a, LocalDateTime d){
        category = c;
        amount = a;
        datetime = d;
    }

    public String getCategory() {
        return category;
    }

    public float getAmount() {
        return amount;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }
}
