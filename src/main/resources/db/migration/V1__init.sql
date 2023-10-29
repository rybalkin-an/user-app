CREATE TABLE IF NOT EXISTS public.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name varchar(50),
    last_name varchar(50),
    birthdate timestamp,
    registration_date timestamp,
    version integer
);