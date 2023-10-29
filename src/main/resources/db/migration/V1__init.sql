CREATE TABLE IF NOT EXISTS public.users (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    first_name varchar(50),
    last_name varchar(50),
    birthdate timestamp,
    registration_date timestamp,
    version integer
);