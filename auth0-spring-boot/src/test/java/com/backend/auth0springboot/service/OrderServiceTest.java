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
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderServiceTest {
	@MockBean
	private OrderRepository orderRepository;
	@MockBean
	private OrderLineRepository orderLineRepository;
	@MockBean
	private JwtService jwtService;
	@MockBean
	private RestaurantRepository restaurantRepository;
	@MockBean
	private MenuRestaurantRepository menuRestaurantRepository;

	private OrderService orderService;

	@Before
	public void setup(){
		orderService = new OrderService(orderRepository,orderLineRepository,jwtService,restaurantRepository,menuRestaurantRepository);
	}

	@Test
	public void shouldReturnListOfOrders(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		MenuRestaurant menuRestaurant = new MenuRestaurant(1L, "name", restaurant, 1.0f, "image1");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		Order order2 = new Order(2L, restaurant,"COMPLETED",
				2f,ImmutableList.of(new OrderLine()),"user2");
		Order order3 = new Order(3L, restaurant,"NEW",
				2f,ImmutableList.of(new OrderLine()),"user1");
		OrderLine orderLineForOrder1 = new OrderLine(1L, order1, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder2 = new OrderLine(2L, order2, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder3 = new OrderLine(3L, order3, menuRestaurant, 1, 1.0f);
		order1.setOrderLines(ImmutableList.of(orderLineForOrder1));
		order2.setOrderLines(ImmutableList.of(orderLineForOrder2));
		order3.setOrderLines(ImmutableList.of(orderLineForOrder3));
		when(jwtService.getRoles()).thenReturn(Collections.singletonList("USER"));
		when(jwtService.getUserId()).thenReturn("user1");
		when(orderRepository.findAll()).thenReturn(ImmutableList.of(order1, order2, order3));
		Assert.assertEquals(1,orderService.findAllCompletedOrders().size());
		Assert.assertEquals(1,orderService.findAllNewOrders().size());
	}

	@Test
	public void shouldReturnEmptyListOfOrders(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		MenuRestaurant menuRestaurant = new MenuRestaurant(1L, "name", restaurant, 1.0f, "image1");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		Order order2 = new Order(2L, restaurant,"COMPLETED",
				2f,ImmutableList.of(new OrderLine()),"user2");
		Order order3 = new Order(3L, restaurant,"NEW",
				2f,ImmutableList.of(new OrderLine()),"user1");
		OrderLine orderLineForOrder1 = new OrderLine(1L, order1, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder2 = new OrderLine(2L, order2, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder3 = new OrderLine(3L, order3, menuRestaurant, 1, 1.0f);
		order1.setOrderLines(ImmutableList.of(orderLineForOrder1));
		order2.setOrderLines(ImmutableList.of(orderLineForOrder2));
		order3.setOrderLines(ImmutableList.of(orderLineForOrder3));
		when(jwtService.getRoles()).thenReturn(Collections.singletonList("USER"));
		when(jwtService.getUserId()).thenReturn("user3");
		when(orderRepository.findAll()).thenReturn(ImmutableList.of(order1, order2, order3));
		Assert.assertEquals(0,orderService.findAllCompletedOrders().size());
		Assert.assertEquals(0,orderService.findAllNewOrders().size());
	}

	@Test
	public void shouldReturnListOfNewOrdersByRestaurant(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		MenuRestaurant menuRestaurant = new MenuRestaurant(1L, "name", restaurant, 1.0f, "image1");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		Order order2 = new Order(2L, restaurant,"COMPLETED",
				2f,ImmutableList.of(new OrderLine()),"user2");
		Order order3 = new Order(3L, restaurant,"NEW",
				2f,ImmutableList.of(new OrderLine()),"user1");
		OrderLine orderLineForOrder1 = new OrderLine(1L, order1, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder2 = new OrderLine(2L, order2, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder3 = new OrderLine(3L, order3, menuRestaurant, 1, 1.0f);
		order1.setOrderLines(ImmutableList.of(orderLineForOrder1));
		order2.setOrderLines(ImmutableList.of(orderLineForOrder2));
		order3.setOrderLines(ImmutableList.of(orderLineForOrder3));
		when(jwtService.getRoles()).thenReturn(Collections.singletonList("USER"));
		when(jwtService.getUserId()).thenReturn("user1");
		when(orderRepository.findAll()).thenReturn(ImmutableList.of(order1, order2, order3));
		Assert.assertEquals(1,orderService.findAllNewOrdersForRestaurantId(1L).size());
	}

	@Test
	public void shouldReturnEmptyListOfNewOrdersByRestaurant(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		MenuRestaurant menuRestaurant = new MenuRestaurant(1L, "name", restaurant, 1.0f, "image1");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		Order order2 = new Order(2L, restaurant,"COMPLETED",
				2f,ImmutableList.of(new OrderLine()),"user2");
		Order order3 = new Order(3L, restaurant,"NEW",
				2f,ImmutableList.of(new OrderLine()),"user1");
		OrderLine orderLineForOrder1 = new OrderLine(1L, order1, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder2 = new OrderLine(2L, order2, menuRestaurant, 1, 1.0f);
		OrderLine orderLineForOrder3 = new OrderLine(3L, order3, menuRestaurant, 1, 1.0f);
		order1.setOrderLines(ImmutableList.of(orderLineForOrder1));
		order2.setOrderLines(ImmutableList.of(orderLineForOrder2));
		order3.setOrderLines(ImmutableList.of(orderLineForOrder3));
		when(jwtService.getRoles()).thenReturn(Collections.singletonList("USER"));
		when(jwtService.getUserId()).thenReturn("user1");
		when(orderRepository.findAll()).thenReturn(ImmutableList.of(order1, order2, order3));
		Assert.assertEquals(0,orderService.findAllNewOrdersForRestaurantId(2L).size());
	}

	@Test
	public void shouldAddOrUpdateOrder(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		MenuRestaurant menuRestaurant1 = new MenuRestaurant(1L, "name1", restaurant, 1.0f, "image1");
		MenuRestaurant menuRestaurant2 = new MenuRestaurant(2L, "name2", restaurant, 2.0f, "image2");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		OrderDto orderDto = new OrderDto(order1.getId(), restaurant.getName(), order1.getStatus(), order1.getPrice(), null, "user1");
		OrderLine orderLineForOrder1 = new OrderLine(1L, order1, menuRestaurant1, 1, 1.0f);
		OrderLine orderLineForOrder2 = new OrderLine(2L, order1, menuRestaurant2, 0, 0.0f);
		OrderLineDto orderLineDtoForOrder1 = new OrderLineDto(1L, menuRestaurant1.getName(),  1, 1.0f);
		OrderLineDto orderLineDtoForOrder2 = new OrderLineDto(2L, menuRestaurant2.getName(), 0, 0.0f);
		orderDto.setOrderLineDtoList(ImmutableList.of(orderLineDtoForOrder1, orderLineDtoForOrder2));
		order1.setOrderLines(ImmutableList.of(orderLineForOrder1,orderLineForOrder2));
		when(restaurantRepository.findRestaurantByName("name1")).thenReturn(restaurant);
		when(jwtService.getUserId()).thenReturn("user1");
		when(orderLineRepository.findById(1L)).thenReturn(Optional.of(orderLineForOrder1));
		when(orderLineRepository.findById(2L)).thenReturn(Optional.of(orderLineForOrder2));
		when(menuRestaurantRepository.findMenuRestaurantByName("name1")).thenReturn(menuRestaurant1);
		when(menuRestaurantRepository.findMenuRestaurantByName("name2")).thenReturn(menuRestaurant2);
		Order orderAfter = new Order(order1.getId(), order1.getRestaurant(), order1.getStatus(), order1.getPrice(), new ArrayList<>(), order1.getUserId());
		orderAfter.setOrderLines(ImmutableList.of(orderLineForOrder1));
		when(orderRepository.save(any())).thenReturn(orderAfter);
		OrderDto orderDtoActual = orderService.addOrUpdateOrder(orderDto);
		Assert.assertEquals(1,orderDtoActual.getOrderLines().size());
		verify(orderLineRepository).delete(orderLineForOrder2);
	}

	@Test(expected = JpaSystemException.class)
	public void shouldAddOrUpdateOrderThrowAnException(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		MenuRestaurant menuRestaurant1 = new MenuRestaurant(1L, "name1", restaurant, 1.0f, "image1");
		MenuRestaurant menuRestaurant2 = new MenuRestaurant(2L, "name2", restaurant, 2.0f, "image2");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		OrderDto orderDto = new OrderDto(order1.getId(), restaurant.getName(), order1.getStatus(), order1.getPrice(), null, "user1");
		OrderLine orderLineForOrder1 = new OrderLine(1L, order1, menuRestaurant1, 1, 1.0f);
		OrderLine orderLineForOrder2 = new OrderLine(2L, order1, menuRestaurant2, 0, 0.0f);
		OrderLineDto orderLineDtoForOrder1 = new OrderLineDto(1L, menuRestaurant1.getName(),  1, 1.0f);
		OrderLineDto orderLineDtoForOrder2 = new OrderLineDto(2L, menuRestaurant2.getName(), 0, 0.0f);
		orderDto.setOrderLineDtoList(ImmutableList.of(orderLineDtoForOrder1, orderLineDtoForOrder2));
		order1.setOrderLines(ImmutableList.of(orderLineForOrder1,orderLineForOrder2));
		when(restaurantRepository.findRestaurantByName("name1")).thenReturn(restaurant);
		when(jwtService.getUserId()).thenReturn("user1");
		when(orderLineRepository.findById(1L)).thenReturn(Optional.of(orderLineForOrder1));
		when(orderLineRepository.findById(2L)).thenReturn(Optional.of(orderLineForOrder2));
		when(menuRestaurantRepository.findMenuRestaurantByName("name1")).thenReturn(menuRestaurant1);
		when(menuRestaurantRepository.findMenuRestaurantByName("name2")).thenReturn(menuRestaurant2);
		Order orderAfter = new Order(order1.getId(), order1.getRestaurant(), order1.getStatus(), order1.getPrice(), new ArrayList<>(), order1.getUserId());
		orderAfter.setOrderLines(ImmutableList.of(orderLineForOrder1));
		when(orderRepository.save(any())).thenReturn(orderAfter);
		doThrow(new JpaSystemException(new RuntimeException("error at delete"))).when(orderLineRepository).delete(orderLineForOrder2);
		orderService.addOrUpdateOrder(orderDto);
	}

	@Test
	public void shouldGetOrderById(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
		final OrderDto actualOrder = orderService.getOrderById(1L);
		Assert.assertEquals(1L,actualOrder.getId().longValue());
	}

	@Test
	public void shouldGetEmptyOrder(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		Order order1 = new Order(1L, restaurant,"COMPLETED",
				1f,ImmutableList.of(),"user1");
		when(orderRepository.getById(1L)).thenReturn(null);
		final OrderDto actualOrder = orderService.getOrderById(1L);
		Assert.assertEquals(new OrderDto(), actualOrder);
	}

	@Test
	public void shouldSendOrder(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		Order order = new Order(1L, restaurant,"NEW",
				1f,ImmutableList.of(),"user1");
		final Order orderAfter = new Order(1L, restaurant, "COMPLETED", 1f, ImmutableList.of(), "user1");
		when(orderRepository.save(order)).thenReturn(orderAfter);
		Assert.assertEquals("COMPLETED",orderAfter.getStatus());
	}

	@Test
	public void shouldNotSendOrder(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		Order order = new Order(1L, restaurant,"NEW",
				1f,ImmutableList.of(),"user1");
		when(orderRepository.save(order)).thenReturn(order);
		Assert.assertEquals("NEW",order.getStatus());
	}
}
