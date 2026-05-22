UPDATE public.screen_information set field_text = 'Summarise the risk of serious harm assessment at the commencement of the sentence and explain any changes to the level of risk, factors that impact on the likelihood of future harm, the nature of the risk or who is at risk. Explain whether changes in risk have resulted from non-compliance, poor behaviour, or other factors and, where relevant, how changes in risk impact on future compliance and steps that are being taken to manage it.' WHERE field_name = 'risk_of_serious_harm';

UPDATE public.screen_information set field_text = 'This needs to include witness details to the BREACH OF THE REQUIREMENT not the offences. So for example if the person had been set voluntary work and didn''t arrive the witness might be the work organiser. This is not stored in Delius.
At a minimum this should include the allocated practitioner. Provide the availability of others involved in supervising the requirement(s) breached, the decision making for breach, or witnesses to unacceptable behaviour.' WHERE field_name = 'witness_availability';


