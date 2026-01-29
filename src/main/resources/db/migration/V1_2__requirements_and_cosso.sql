CREATE TABLE public.cosso_requirement(id uuid not null primary key,
                                      cosso_id uuid not null,
                                      delius_requirement_id bigint not null,
                                      requirement_type_main_category_description varchar(200) NULL,
                                      requirement_type_sub_category_description varchar(200) NULL,
                                      requirement_length bigint,
                                      requirement_second_length bigint,
                                      created_by_user varchar(50) not null,
                                      created_datetime timestamp without time zone NULL,
                                      last_updated_user varchar(50) not null,
                                      last_updated_datetime timestamp without time zone NULL);
);

ALTER TABLE public.cosso_requirement ADD CONSTRAINT xfk1_cosso_requirement
    FOREIGN KEY (cosso_id) REFERENCES public.cosso (id) ON DELETE No Action ON UPDATE No Action;

ALTER TABLE public.cosso DROP COLUMN requirement_type;
ALTER TABLE public.cosso DROP COLUMN requirement_length;
ALTER TABLE public.cosso DROP COLUMN requirement_second_length;
