ALTER TABLE public.cosso
    ADD COLUMN terminated boolean NOT NULL DEFAULT false,
    ADD COLUMN terminated_unterminated_date timestamp without time zone NULL;

ALTER TABLE public.address
    ALTER COLUMN building_name TYPE varchar(80),
    ALTER COLUMN street_name TYPE varchar(80),
    ALTER COLUMN district TYPE varchar(80);
