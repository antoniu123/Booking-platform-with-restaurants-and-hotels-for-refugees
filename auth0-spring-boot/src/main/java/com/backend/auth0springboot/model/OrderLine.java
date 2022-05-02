package com.backend.auth0springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS_DETAIL")
public class OrderLine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDERS_ID")
	private Order order;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_RESTAURANT_ID")
	private MenuRestaurant menuRestaurant;

	@Column(name = "QUANTITY", nullable = false)
	private Integer quantity;

	@Column(name = "PRICE", nullable = false)
	private float price;

	public OrderLine() {
	}

	public OrderLine(Long id, Order order, MenuRestaurant menuRestaurant, Integer quantity, float price) {
		this.id = id;
		this.order = order;
		this.menuRestaurant = menuRestaurant;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public MenuRestaurant getMenuRestaurant() {
		return menuRestaurant;
	}

	public void setMenuRestaurant(MenuRestaurant menuRestaurant) {
		this.menuRestaurant = menuRestaurant;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderLine{" +
				"id=" + id +
				", menuRestaurant=" + menuRestaurant +
				", quantity=" + quantity +
				", price=" + price +
				'}';
	}
}
