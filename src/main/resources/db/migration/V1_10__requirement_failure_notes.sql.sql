ALTER TABLE public.cosso_requirement
    ADD COLUMN notes varchar(20000),
    ADD COLUMN failure boolean DEFAULT false,
    ADD COLUMN failure_reason varchar(200);
