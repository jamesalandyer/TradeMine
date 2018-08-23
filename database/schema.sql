-- *************************************************************************************************
-- This script creates all of the database objects (tables, sequences, etc) for the database
-- *************************************************************************************************

BEGIN;

-- CREATE statements go here
DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS portfolio;
DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS game;

CREATE TABLE app_user (
  id SERIAL PRIMARY KEY,
  user_name varchar(32) NOT NULL UNIQUE,
  password varchar(32) NOT NULL,
  role varchar(32),
  salt varchar(255) NOT NULL
);

CREATE TABLE game
(
	game_id SERIAL PRIMARY KEY,
	game_name VARCHAR(200) NOT NULL,
  creator_id INTEGER NOT NULL,
	end_date VARCHAR(100) NOT NULL,
  ended boolean NOT NULL,

  CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES app_user(id)
);

CREATE TABLE player
(
  amount_left bigint NOT NULL,
  inviter_id INTEGER NOT NULL,
  joined boolean NOT NULL,
	user_id INTEGER NOT NULL,
  game_id INTEGER NOT NULL,

  CONSTRAINT pk_player_user_id_game_id PRIMARY KEY (user_id, game_id),
  CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user(id),
  CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES game(game_id),
  CONSTRAINT fk_inviter_id FOREIGN KEY (inviter_id) REFERENCES app_user(id)
);

CREATE TABLE portfolio
(
	user_id INTEGER NOT NULL,
  game_id INTEGER NOT NULL,
  stock_symbol VARCHAR(100) NOT NULL,
	shares INTEGER NOT NULL,

  CONSTRAINT pk_portfolio_stock_symbol_game_id_user_id PRIMARY KEY (stock_symbol, game_id, user_id),
  CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user(id),
  CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES game(game_id)
);

CREATE TABLE sale
(
	sale_id SERIAL PRIMARY KEY,
	game_id INTEGER NOT NULL,
	user_id INTEGER NOT NULL,
	purchase boolean NOT NULL,
	stock_symbol VARCHAR(100) NOT NULL,
	shares INTEGER NOT NULL,
  price_per_share FLOAT NOT NULL,
	transaction_date timestamp NOT NULL,

  CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user(id),
  CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES game(game_id)
);

COMMIT;