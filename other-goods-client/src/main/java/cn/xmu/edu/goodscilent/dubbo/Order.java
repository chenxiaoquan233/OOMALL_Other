package cn.xmu.edu.goodscilent.dubbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	private Long id;
	private Customer customer;
	private Shop shop;
	private String orderSn;
	private List<Order> subOrders;
	private Long pid;
	private String consignee;
	private Long regionId;
	private String address;
	private String mobile;
	private String message;
	private Integer orderType;
	private Long freightPrice;
	private Long couponId;
	private Long couponActivityId;
	private Long discountPrice;
	private Long originPrice;
	private Long presaleId;
	private Long grouponId;
	private Long grouponDiscount;
	private Integer rebateNum;
	private LocalDateTime confirmTime;
	private String shipmentSn;
	private Integer state;
	private Integer subState;
	private Integer beDeleted;
	private List<OrderItem> orderItems;
	private LocalDateTime gmtCreated;
	private LocalDateTime gmtModified;


	public Order createAndAddSubOrder(Shop shop, List<OrderItem> orderItems) {
		Order subOrder = new Order();

		subOrder.customer = this.customer;

		subOrder.shop = shop;

		subOrder.orderItems = new ArrayList<>();
		subOrder.orderItems.addAll(orderItems);

		subOrder.consignee = this.consignee;
		subOrder.regionId = this.regionId;
		subOrder.address = this.address;
		subOrder.mobile = this.mobile;
		subOrder.message = this.message;

		this.subOrders.add(subOrder);

		return subOrder;
	}

	public void calcAndSetParentOrderOriginPrice() {
		Long price = 0L;
		for (Order o : getSubOrders()) {
			price += o.getOriginPrice();
		}
		originPrice = price;
	}

	public void calcAndSetSubOrdersOriginPrice() {
		for (Order subOrder : getSubOrders()) {
			long price = 0L;
			for (OrderItem oi : subOrder.getOrderItems()) {
				price += oi.getQuantity() * oi.getPrice();
			}
			subOrder.setOriginPrice(price);
		}
	}

	public Integer calcAndGetRebate() {
		if (originPrice == null) {
			return 0;
		}
		return Math.toIntExact(originPrice / 25L);
	}
}
