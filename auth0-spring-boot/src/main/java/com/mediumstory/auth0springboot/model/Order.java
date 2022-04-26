package com.mediumstory.auth0springboot.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ORDERS")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RESTAURANT_ID")
	private Restaurant restaurant;

	@Column(name = "STATUS", nullable = false)
	private String status;

	@Column(name = "PRICE", nullable = false)
	private Float price;

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "order", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<OrderLine> orderLines= new ArrayList<>(0);

	@Column(name = "USER_ID")
	private String userId;

	public Order() {
	}

	public Order(Long id, Restaurant restaurant, String status, Float price, List<OrderLine> orderLines, String userId) {
		this.id = id;
		this.restaurant = restaurant;
		this.status = status;
		this.price = price;
		this.orderLines = orderLines;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public List<OrderLine> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Order)) {
			return false;
		}
		Order order = (Order) o;
		return Objects.equals(id, order.id) && Objects.equals(restaurant, order.restaurant) && Objects.equals(status, order.status) && Objects.equals(price, order.price) && Objects.equals(orderLines, order.orderLines) && Objects.equals(userId, order.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, restaurant, status, price, orderLines, userId);
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", restaurant=" + restaurant +
				", status='" + status + '\'' +
				", price=" + price +
				", orderLines=" + orderLines +
				", userId='" + userId + '\'' +
				'}';
	}
}
