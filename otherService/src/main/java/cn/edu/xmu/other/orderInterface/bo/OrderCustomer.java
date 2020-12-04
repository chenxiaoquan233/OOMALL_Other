package cn.edu.xmu.other.orderInterface.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomer {
    private Long id;
    private String userName;
    private String realName;

    public OrderCustomer(Long id) {
        this.id = id;
    }
}
