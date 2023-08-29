CREATE TABLE users_payments (
    users_id bigserial REFERENCES users(id),
    payment_id bigserial REFERENCES payment(id),
    PRIMARY KEY (users_id, payment_id)
);

