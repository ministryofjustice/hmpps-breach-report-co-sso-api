ALTER TABLE public.cosso ADD COLUMN ro_title_and_full_name varchar(200);
ALTER TABLE public.cosso ADD COLUMN ro_telephone_number  varchar(35);
ALTER TABLE public.cosso ADD COLUMN ro_email_address varchar(200);
ALTER TABLE public.cosso ADD COLUMN offence_details_saved boolean;
ALTER TABLE public.cosso ADD COLUMN failures_and_enforcement_saved  boolean;
ALTER TABLE public.cosso ADD COLUMN ro_and_witness_details_saved  boolean;
ALTER TABLE public.cosso ADD COLUMN compliance_to_date_saved boolean;