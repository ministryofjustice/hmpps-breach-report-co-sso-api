INSERT INTO public.screen_information (id, screen_name, field_name, field_text)
VALUES
    (gen_random_uuid(), 'witness_details', 'witness_availability',
     'This needs to include witness details to the BREACH OF THE REQUIREMENT not the offences. So for example if the person had been set voluntary work and didnt arrive the witness might be the work organiser. This is not stored in Delius.
At a minimum this should include the allocated practitioner. Provide the availability of others involved in supervising the requirement(s) breached, the decision making for breach, or witnesses to unacceptable behaviour.'
    );

UPDATE public.screen_information SET screen_name = 'compliance' where field_name = 'compliance_to_date';



