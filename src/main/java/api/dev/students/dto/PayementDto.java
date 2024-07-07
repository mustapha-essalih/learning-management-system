package api.dev.students.dto;

import java.math.BigDecimal;

public class PayementDto {
    private String courseName;
    private BigDecimal amount;

    
    public String getCourseName() {
        return courseName;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    
}
