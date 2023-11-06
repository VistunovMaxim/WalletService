package org.example;

import org.example.controller.PlayerController;
import org.example.controller.impl.PlayerControllerImpl;
import org.example.entity.Player;
import org.example.util.exception.AccessDeniedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserServiceTest {

    @Test
    void createNewPlayerNotNullTest() throws AccessDeniedException {
        PlayerController playerController = new PlayerControllerImpl();
        Player player = playerController.registration("login", "password");
        assertNotNull(player);
    }
}
