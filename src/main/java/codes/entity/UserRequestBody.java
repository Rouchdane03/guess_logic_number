package codes.entity;

import codes.controller.GameModeEnum;

import java.time.Duration;
import java.util.List;

//j'ai enlevé le LocalDateTime passedAt car jpa je génère automatiquement
public record UserRequestBody(String username, List<GameModeRequestBody> gameModes, ReviewRequestBody review) {}

