SET SQL_SAFE_UPDATES = 0;

DELETE FROM culture_reference;

INSERT INTO culture_reference (culture_name, optimal_temperature, optimal_weekly_rainfall, planting_month, harvest_month) VALUES
  ('ONION', 24, 25, 'APRIL', 'JULY'),
  ('BEANS', 22, 25, 'MAY', 'JUNE'),
  ('TOMATO', 26, 30, 'MAY', 'JULY'),
  ('POTATO', 20, 20, 'APRIL', 'AUGUST'),
  ('ZUCCINI', 25, 25, 'MAY', 'JULY'),
  ('CORN', 27, 30, 'APRIL', 'AUGUST'),
  ('CHERRY', 18, 20, NULL, 'JUNE'),
  ('APPLE', 21, 25, NULL, 'SEPTEMBER'),
  ('PLUM', 22, 25, NULL, 'AUGUST'),
  ('WATERMELON', 30, 35, 'MAY', 'AUGUST'),
  ('GRAPE', 28, 30, 'MARCH', 'SEPTEMBER');

SET SQL_SAFE_UPDATES = 1;