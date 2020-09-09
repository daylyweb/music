# music


## Introduction

An application for searching and playing music.  

Music resources (json type) are provided by QQ music API service (Tencent Holdings Ltd.)

## Main function

1. User login and information management
2. Music search and play 
3. Feedback (comment, good button, etc.)


## technology stack

### Build Tool : Maven

   Manage the reference packages more effectively.
   
### Framework : SpringMVC + log4j2 + Mybatis

   Spring MVC : Use dispatchservlet API to distribute requests to specified services.  
    
   log4j2 : Monitor the runtime information of server side program.  
   
   Mybatis : Improve the mantainability by writing SQL in XML file and making java code more simplified.
    
### Database : Mysql 

   Simple CRUD operations.
   
### Future Possible Improvement
   
   1. Add a cache layer by Redis.
   
   2. Implement a third-party authorized login by Springboot Security Oauth2.
   


    
   
    
    
            

            


