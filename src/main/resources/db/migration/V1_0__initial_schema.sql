CREATE TABLE public.cosso
(
    id                    uuid         NOT NULL PRIMARY KEY,
    crn                   char(7)      NOT NULL,
    review_required_date  timestamp without time zone NULL,
    review_event          varchar(100) NULL,
    completed_date        timestamp with time zone NULL,
    last_updated_datetime timestamp without time zone not NULL,
    last_updated_user     varchar(100) not NULL,
    created_by_user       varchar(100) not NULL,
    created_datetime      timestamp without time zone not NULL
);
