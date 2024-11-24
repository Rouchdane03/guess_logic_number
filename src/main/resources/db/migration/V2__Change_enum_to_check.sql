ALTER TABLE game_modes
ALTER COLUMN game_level_played TYPE VARCHAR(10);

ALTER TABLE game_modes
ADD CONSTRAINT check_game_level CHECK (game_level_played IN ('EASY', 'MEDIUM', 'HARD'));

DROP TYPE IF EXISTS game_mode_type_enum;
