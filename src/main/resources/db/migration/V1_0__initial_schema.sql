CREATE TABLE public.address
(
    id                     uuid PRIMARY KEY,
    address_id             bigint,
    status                 varchar(100),
    building_name          varchar(35),
    address_number         varchar(35),
    street_name            varchar(35),
    district               varchar(35),
    town_city              varchar(35),
    county                 varchar(35),
    postcode               varchar(8),
    created_by_user        varchar(100) NOT NULL,
    created_datetime       timestamp    NOT NULL,
    last_updated_user      varchar(100) NOT NULL,
    last_updated_datetime  timestamp    NOT NULL,
    office_description     varchar(50)
);

CREATE TABLE public.cosso
(
    id                          uuid PRIMARY KEY,
    crn                         char(7) NOT NULL,
    title_and_full_name         varchar(200),
    date_of_form                timestamp,
    sheet_sent_by               varchar(100),
    telephone_number            varchar(35),
    mobile_number               varchar(35),
    email_address               varchar(200),
    completed_date              timestamp with time zone,
    postal_address_id           uuid REFERENCES public.address(id),
    date_of_birth               timestamp,
    prison_number               varchar(50),
    work_address_id             uuid REFERENCES public.address(id),
    probation_area              varchar(100),
    witness_availability        varchar(20000),
    main_offence                text,
    additional_offence          varchar(20000),
    sentencing_court            varchar(200),
    sentence_type               varchar(100),
    sentence_length             varchar(100),
    length_units                varchar(35),
    suspended_custody_length    varchar(100),
    second_length               varchar(100),
    second_length_units         varchar(35),
    requirement_type            varchar(100),
    requirement_length          varchar(100),
    requirement_second_length   varchar(100),
    amendment_details           varchar(20000),
    amendment_reason            varchar(20000),
    why_in_breach               varchar(20000),
    steps_to_prevent_breach     varchar(20000),
    compliance_o_date           varchar(20000),
    risk_history                text,
    recommendations             varchar(20000),
    supporting_comments         varchar(20000),
    basic_details_saved         boolean null,
    confirm_equalities          boolean null,
    risk_of_harm_changed        boolean null,
    sign_and_send_saved         boolean null,
    contact_saved               boolean null,
    review_required_date        timestamp NULL,
    review_event                varchar(100) NULL,
    created_by_user             varchar(100) NOT NULL,
    created_datetime            timestamp    NOT NULL,
    last_updated_user           varchar(100) NOT NULL,
    last_updated_datetime       timestamp    NOT NULL
);

CREATE TABLE public.contact
(
    id                        uuid PRIMARY KEY,
    cosso_id                  uuid REFERENCES public.cosso(id),
    contact_type_description  varchar(200),
    contact_person            varchar(200),
    contact_location_id       uuid NULL,
    form_sent                 boolean,
    created_by_user           varchar(100) NOT NULL,
    created_datetime          timestamp    NOT NULL,
    last_updated_user         varchar(100) NOT NULL,
    last_updated_datetime     timestamp    NOT NULL
);

CREATE TABLE public.screen_information
(
    id          uuid PRIMARY KEY,
    screen_name varchar(200),
    field_name  varchar(200),
    field_text  varchar(20000)
);

INSERT INTO public.screen_information (id, screen_name, field_name, field_text)
VALUES
    (gen_random_uuid(), 'failures_and_enforcements', 'breach_reason',
     'This section should allow the court to understand why the breach allegation has been brought including the rationale for any decisions e.g., why evidence has not been accepted, or why the breach deviates from standard practice (e.g., relates to only one failure to attend):

     Provide details of the induction or appointments where enforcement/acceptable behaviour were explained. State if you consider the person understood this information and provide signed paperwork as evidence.

     Explain why the person is in breach:

     For example, dates of each missed appointment, how the person was notified of each of these, and the type of appointment/s missed; details of a curfew or Alcohol Abstinence breach; details of failure to notify/approve a change of address.

     Outline any evidence, or reasons provided by the person to explain the breach and explain why these have not been accepted.

     For incidents of unacceptable behaviour outline what happened, when and where, who was present, why the behaviour is considered unacceptable and how the behaviour affected those present.

     Explain how diversity considerations or other circumstances have impacted on the breach or state if none are relevant, so the court can decide whether to take these into consideration.

     Sentencing Guidelines require the court to consider circumstances such as disability, mental health, or learning disabilities. Other factors may be (but are not limited to): literacy; physical health; immaturity; experience of care, trauma, racism/discrimination; substance misuse; protected characteristics (age, race, religion/belief, disability, pregnancy/maternity, sexual orientation, gender reassignment, marriage or civil partnership). Employment, caring responsibilities, or rurality may also be relevant.

     Consult additional guidance, where relevant:

     Maturity Aide-Memoire; Women Aide-Memoire; 7-Minute Briefing: Trauma.'
    ),

    (gen_random_uuid(), 'failures_and_enforcements', 'preventative_steps',
     'What steps have been taken to prevent this breach
     To enable the court to understand how non-compliance was dealt with prior to the breach:

     Provide details/dates of enforcement letters sent and evaluate the effectiveness of any additional actions taken to increase compliance/address non-compliance, attempts to re-engage the person, or explain why no other actions were taken.

     Examples of additional actions: addressing barriers to engagement/diversity considerations;
     actions taken following the sending of an initial enforcement letter; home visits; contact with or support provided by other agencies; modifications to the management of the sentence (e.g., amended Unpaid Work hours, changed appointment times).'
    ),

    (gen_random_uuid(), 'failures_and_enforcements', 'compliance_to_date',
     'Compliance with Order / sentence to date
     By assessing both compliance and engagement regarding all elements of the sentence to date and likely future engagement, the court can make an informed decision on its response to the breach:

     Compliance:
     Outline the person’s level of attendance on all elements of the Order/PSS...'
    ),

    (gen_random_uuid(), 'failures_and_enforcements', 'risk_of_serious_harm',
     'summarise the risk of serious harm assessment at the commencement of the sentence and explain any changes to the level of risk...'
    ),

    (gen_random_uuid(), 'failures_and_enforcements', 'recommendation',
     'The proposal should be commensurate with the seriousness of the breach, the original offence, and the preceding analysis...'
    );
