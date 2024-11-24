package codes.entity;

import codes.controller.GameModeEnum;

import java.time.Duration;

public record GameModeRequestBody(GameModeEnum gameLevelPlayed, Duration elapsedTimeForThisLevel) {
}
