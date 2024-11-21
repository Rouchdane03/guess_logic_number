CREATE TABLE opinions(
    opinion_id BIGSERIAL PRIMARY KEY,
    message TEXT,
    given_stars INTEGER,
    CONSTRAINT must_stay_between_one_and_five CHECK ( given_stars>=1 AND given_stars<=5 )
);
CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY ,
    username TEXT NOT NULL ,
    email TEXT NOT NULL ,
    user_opinion_id INTEGER,
    CONSTRAINT must_be_different UNIQUE(email),
    CONSTRAINT fk_users_opinions FOREIGN KEY (user_opinion_id )  REFERENCES opinions (opinion_id)
);