CREATE TABLE reviews(
    id BIGSERIAL PRIMARY KEY,
    given_stars INTEGER,
    message TEXT,
    CONSTRAINT must_stay_between_one_and_five CHECK ( given_stars>=1 AND given_stars<=5 )
);
CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY ,
    username TEXT NOT NULL,
    elapsed_time VARCHAR(10) NOT NULL,
    user_review_id INTEGER,
    passed_at TIMESTAMP NOT NULL,
    CONSTRAINT must_be_unique UNIQUE (username),
    CONSTRAINT fk_users_opinions FOREIGN KEY (user_review_id )  REFERENCES reviews (id)
);

CREATE TYPE game_mode_type_enum AS ENUM ('EASY', 'MEDIUM', 'HARD');

CREATE TABLE game_modes(
    id BIGSERIAL PRIMARY KEY,
    game_level_played game_mode_type_enum NOT NULL ,
    user_id INTEGER,
    CONSTRAINT fk_bind_game_modes_and_users FOREIGN KEY (user_id )  REFERENCES users (id)
);