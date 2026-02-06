ALTER TABLE public.cosso_requirement DROP COLUMN IF EXISTS requirement_length;
ALTER TABLE public.cosso_requirement DROP COLUMN IF EXISTS requirement_second_length;

ALTER TABLE public.cosso_requirement ADD COLUMN requirement_length varchar(100);
ALTER TABLE public.cosso_requirement ADD COLUMN requirement_second_length varchar(100);