CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS public.users (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    first_name varchar(50),
    last_name varchar(50),
    user_data jsonb,
    birthdate timestamp,
    registration_date timestamp,
    version integer
);