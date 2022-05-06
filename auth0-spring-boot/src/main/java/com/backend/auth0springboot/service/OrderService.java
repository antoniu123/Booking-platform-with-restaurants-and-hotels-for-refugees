package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.OrderDto;
import com.backend.auth0springboot.dto.OrderLineDto;
import com.backend.auth0springboot.model.MenuRestaurant;
import com.backend.auth0springboot.model.Order;
import com.backend.auth0springboot.model.OrderLine;
import com.backend.auth0springboot.model.Restaurant;
import com.backend.auth0springboot.repository.MenuRestaurantRepository;
import com.backend.auth0springboot.repository.OrderLineRepository;
import com.backend.auth0springboot.repository.OrderRepository;
import com.backend.auth0springboot.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	private final OrderLineRepository orderLineRepository;

	private final JwtService jwtService;

	private final RestaurantRepository restaurantRepository;

	private final MenuRestaurantRepository menuRestaurantRepository;

	@Autowired
	public OrderService(final OrderRepository orderRepository,
						final OrderLineRepository orderLineRepository,
						final JwtService jwtService,
						final RestaurantRepository restaurantRepository, MenuRestaurantRepository menuRestaurantRepository) {
		this.orderRepository = orderRepository;
		this.orderLineRepository = orderLineRepository;
		this.jwtService = jwtService;
		this.restaurantRepository = restaurantRepository;
		this.menuRestaurantRepository = menuRestaurantRepository;
	}

	public List<OrderDto> findAllNewOrders() {
		return orderRepository.findAll().stream()
				.filter(order -> Objects.equals(order.getStatus(), "NEW") && order.getUserId().equals(jwtService.getUserId()))
				.map(o -> {
					List<OrderLineDto> orderLineDtoList = o.getOrderLines().stream()
							.map(ol -> new OrderLineDto(ol.getId(), ol.getMenuRestaurant().getName(), ol.getQuantity(), ol.getPrice()))
							.collect(Collectors.toList());
					return new OrderDto(o.getId(), o.getRestaurant().getName(), o.getStatus(), o.getPrice(), orderLineDtoList, o.getUserId());
				})
				.collect(Collectors.toList());
	}

	public List<OrderDto> findAllNewOrdersForRestaurantId(Long restaurantId) {
		return orderRepository.findAll().stream()
				.filter(order -> Objects.equals(order.getStatus(), "NEW") &&
						         order.getRestaurant().getId().equals(restaurantId) &&
						         order.getUserId().equals(jwtService.getUserId()))
				.map(o -> {
					List<OrderLineDto> orderLineDtoList = o.getOrderLines().stream()
							.map(ol -> new OrderLineDto(ol.getId(), ol.getMenuRestaurant().getName(), ol.getQuantity(), ol.getPrice()))
							.collect(Collectors.toList());
					return new OrderDto(o.getId(), o.getRestaurant().getName(), o.getStatus(), o.getPrice(), orderLineDtoList, o.getUserId());
				})
				.collect(Collectors.toList());
	}

	public List<OrderDto> findAllCompletedOrders() {
		if(jwtService.getRoles().contains("ADMIN")){
			return orderRepository.findAll().stream()
					.filter(order -> Objects.equals(order.getStatus(), "COMPLETED"))
					.map(o -> {
						List<OrderLineDto> orderLineDtoList = o.getOrderLines().stream()
								.map(ol -> new OrderLineDto(ol.getId(), ol.getMenuRestaurant().getName(), ol.getQuantity(), ol.getPrice()))
								.collect(Collectors.toList());
						return new OrderDto(o.getId(), o.getRestaurant().getName(), o.getStatus(), o.getPrice(), orderLineDtoList, o.getUserId());
					})
					.collect(Collectors.toList());
		}
		else{
			return orderRepository.findAll().stream()
					.filter(order -> Objects.equals(order.getStatus(), "COMPLETED") && order.getUserId().equals(jwtService.getUserId()))
					.map(o -> {
						List<OrderLineDto> orderLineDtoList = o.getOrderLines().stream()
								.map(ol -> new OrderLineDto(ol.getId(), ol.getMenuRestaurant().getName(), ol.getQuantity(), ol.getPrice()))
								.collect(Collectors.toList());
						return new OrderDto(o.getId(), o.getRestaurant().getName(), o.getStatus(), o.getPrice(), orderLineDtoList, o.getUserId());
					})
					.collect(Collectors.toList());
		}

	}

	public OrderDto addOrUpdateOrder(final OrderDto orderDto) {
		final Restaurant restaurant = restaurantRepository.findRestaurantByName(orderDto.getRestaurantName());
		final Order order = new Order(orderDto.getId(), restaurant,
				orderDto.getStatus(), orderDto.getPrice(), null, jwtService.getUserId());
		final List<OrderLine> orderLines = orderDto.getOrderLines().stream()
				//delete if one line has 0 qty
				.map(ol->{
					if (ol.getQuantity()==0){
						orderLineRepository.findById(ol.getId()).ifPresent(orderLineRepository::delete);
						return null;
					}
					return ol;
				 })
				.filter(Objects::nonNull)
				.map(orderLineDto -> {
			final MenuRestaurant menuRestaurant = menuRestaurantRepository.findMenuRestaurantByName(orderLineDto.getMenuRestaurantName());
			return new OrderLine(orderLineDto.getId(), order, menuRestaurant, orderLineDto.getQuantity(), orderLineDto.getPrice());
		}).collect(Collectors.toList());
		final Float totalPrice = orderLines.stream().map(ol->ol.getQuantity()*ol.getPrice()).reduce(0f, Float::sum);
		order.setOrderLines(orderLines);
		order.setPrice(totalPrice);
		final Order newOrder = orderRepository.save(order);
		final List<OrderLineDto> orderLineDtoList = newOrder.getOrderLines().stream()
				.map(ol -> new OrderLineDto(ol.getId(), ol.getMenuRestaurant().getName(), ol.getQuantity(), ol.getPrice())).collect(Collectors.toList());
		return new OrderDto(newOrder.getId(), newOrder.getRestaurant().getName(), newOrder.getStatus(), newOrder.getPrice(), orderLineDtoList, newOrder.getUserId());
	}

	public OrderDto getOrderById(Long orderId){
		return orderRepository.findById(orderId)
				.map(o->{
					List<OrderLineDto> orderLineDtoList = o.getOrderLines().stream()
							.map(ol -> new OrderLineDto(ol.getId(), ol.getMenuRestaurant().getName(), ol.getQuantity(), ol.getPrice()))
							.collect(Collectors.toList());
					return new OrderDto(o.getId(), o.getRestaurant().getName(), o.getStatus(), o.getPrice(),
							orderLineDtoList, o.getUserId());
				}).orElseGet(OrderDto::new);

	}

	public void sendOrder(final Long orderId) {
		final Order order = orderRepository.getById(orderId);
		order.setStatus("COMPLETED");
		orderRepository.save(order);
	}
}
