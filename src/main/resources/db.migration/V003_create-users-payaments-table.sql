CREATE TABLE users_payaments (
    users_id bigserial REFERENCES users(id),
    payament_id bigserial REFERENCES payament(id),
    PRIMARY KEY (users_id, payament_id)
);

