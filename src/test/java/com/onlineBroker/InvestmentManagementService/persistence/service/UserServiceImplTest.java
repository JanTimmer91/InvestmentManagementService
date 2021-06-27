package com.onlineBroker.InvestmentManagementService.persistence.service;

import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @BeforeEach
    void setUp() {
    }


    @Autowired
    private UserServiceImpl userServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Test
    void isUserExisting() {
    }

    @Test
    void createNewUserEntity() {
    }

    @Test
    void findUserEntity() {
        UserEntity userEntity = new UserEntity(
                "abc-123",
                new TreeMap<>(),
                0.0,
                new ArrayList<>()
        );
        Mockito.when(userRepository.findByUserId("abc-123")).thenReturn(userEntity);
        assertEquals(userEntity, userServiceImpl.findUserEntity("abc-123"));
    }

    @Test
    void saveUserEntity() {
        UserEntity userEntity = new UserEntity(
                "abc-123",
                new TreeMap<>(),
                0.0,
                new ArrayList<>()
        );
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);
        assertEquals(userEntity, userServiceImpl.saveUserEntity(userEntity));
    }

    @Test
    void addWatchlistItem() {
    }

    @Test
    void removeWatchlistItem() {
    }

    @Test
    void findWatchlist() {
    }
}