# Issues and Problems
1. I did not manage to get the "generateRequest" method inside the
"KafkaRequestsGenerator" klass to work using a random time interval. 
I wanted it to spit out a random request every x seconds with "x" being a random integer.
But Quarkus interpreted that to mean that I want it randomized once and then for every request
to be sent out every "THAT" seconds... I'll be using a hardcoded time interval now instead
as it's not that big of a problem, but I should look into that at a later date.  
  
Good advice; When configuring quarkus dev-mode for kafka usage, you will eventually come across various types of  
"kafka configurations", ways to configure your kafka broker for partition, naming ID:groups and similar.  
When working with quarkus dev-mode these configurations can (often) be placed inside your Application.properties,  
even if your editor does not recognize the configuration and "grays it out".  
This tends to happen because your editor is trying to recognize quarkus or java related configs, but since kafka exists
as an external "entity", your editor may think your configuration doesn't exist even though it both exists and 
works while placed in properties!  
  
Always try to use a configuration from Application.properties before giving up on dev-mode altogether.