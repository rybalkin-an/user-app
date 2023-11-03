CREATE TABLE IF NOT EXISTS public.users (
    id UUID DEFAULT RANDOM_UUID(),
    first_name varchar(50),
    last_name varchar(50),
    user_data json,
    birthdate timestamp,
    registration_date timestamp,
    version integer
);