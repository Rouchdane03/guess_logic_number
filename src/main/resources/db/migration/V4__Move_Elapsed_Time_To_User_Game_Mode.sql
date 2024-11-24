ALTER TABLE users
DROP COLUMN elapsed_time;

ALTER TABLE game_modes
ADD COLUMN elapsed_time_for_this_level VARCHAR(10);

ALTER TABLE game_modes
ALTER COLUMN elapsed_time_for_this_level SET NOT NULL;