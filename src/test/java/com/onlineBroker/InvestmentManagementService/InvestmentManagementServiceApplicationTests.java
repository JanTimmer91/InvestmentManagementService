package com.onlineBroker.InvestmentManagementService;

import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.repository.UserRepository;
import com.onlineBroker.InvestmentManagementService.persistence.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class InvestmentManagementServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@Test
	public void findUserEntityTest() {
		UserEntity userEntity = new UserEntity(
				"abc-123",
				new TreeMap<>(),
				0.0,
				0.0,
				new ArrayList<>()
		);
		Mockito.when(userRepository.findByUserId("abc-123")).thenReturn(userEntity);
		assertEquals(userEntity, userService.findUserEntity("abc-123"));
	}

	@Test
	public void saveUserTest() {
		UserEntity userEntity = new UserEntity(
				"abc-123",
				new TreeMap<>(),
				0.0,
				0.0,
				new ArrayList<>()
		);
		Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);
		assertEquals(userEntity, userService.saveUserEntity(userEntity));
	}
}
