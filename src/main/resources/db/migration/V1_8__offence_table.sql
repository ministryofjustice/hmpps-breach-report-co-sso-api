CREATE TABLE public.cosso_offence(id uuid not null primary key,
                                      cosso_id uuid not null,
                                      description varchar(200),
                                      code varchar(200),
                                      created_by_user varchar(50) not null,
                                      created_datetime timestamp without time zone not null,
                                      last_updated_user varchar(50) not null,
                                      last_updated_datetime timestamp without time zone not null
);

ALTER TABLE public.cosso_offence ADD CONSTRAINT xfk1_cosso_offence
    FOREIGN KEY (cosso_id) REFERENCES public.cosso (id) ON DELETE No Action ON UPDATE No Action;


