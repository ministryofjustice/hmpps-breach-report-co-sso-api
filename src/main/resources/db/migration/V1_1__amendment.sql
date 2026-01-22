CREATE TABLE public.amendment
(
    id                     uuid PRIMARY KEY,
    cosso_id               uuid REFERENCES public.cosso(id),
    amendment_details      varchar(20000) null,
    amendment_reason       varchar(20000) null,
    amendment_date         date NULL,
    created_by_user        varchar(100) NOT NULL,
    created_datetime       timestamp without time zone NOT NULL,
    last_updated_user      varchar(100) NOT NULL,
    last_updated_datetime  timestamp without time zone NOT NULL
);

ALTER TABLE public.amendment ADD CONSTRAINT xfk1_amendment_cosso
    FOREIGN KEY (cosso_id) REFERENCES public.cosso (id) ON DELETE No Action ON UPDATE No Action;
