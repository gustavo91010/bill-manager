-- Remove a constraint antiga
ALTER TABLE payment DROP CONSTRAINT IF EXISTS fk_payment_category;

-- Cria a nova constraint com ON DELETE SET NULL
ALTER TABLE payment
  ADD CONSTRAINT fk_payment_category
  FOREIGN KEY (category_id) REFERENCES category(id)
  ON DELETE SET NULL;
