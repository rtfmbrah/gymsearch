# GYMSEARCH

> [!IMPORTANT]
> GYMSEARCH is in very early development. Due to this, there might be constant changes to the whole project.

GYMSEARCH is a discount comparison service tool with primary focus on fitness related products sold in different local market chains. 
This repository holds the source of the GYMSEARCH backend, while there are plans for an frontend webapp to enable UI
inside a Webbrowser (or potentialy an application) for convinient use on multiple platforms.

The backend is powered by Spring Boot 3.5.3 (Java 21) as Spring (Boot) provides a number of tools for an easy creation and maintanence of 
a modern Webservice and has experienced wide use in the industry.

The current main focus of this service is convinience of two IT nerds while being able to have an interesting learning experience.
Due to the focus on convinience, we want to cover as many different chains as possible. 

## Setup

> [!NOTE]
> Before any further steps, make sure you have Maven with the Java version 21 SDK installed on your system and configured properly.

Clone the repository:
```
git clone https://github.com/rtfmbrah/gymsearch.git
cd gymsearch
```

Declare environmential variables:
```
ADMIN_PASSWORD
ADMIN_USERNAME
KEYSTORE            <- Path to .p12
KEYSTORE_PASSWORD   <- Password used for .p12 creation
```

> [!CAUTION]
> This repository does not contain the certificate and key for accessing any API endpoints.
> Visit [rewe_discounts](https://github.com/torbenpfohl/rewe-discounts) by [@torbenpfohl](www.github.com/torbenpfohl)
> for a guide on how to obtain the certificate and key pair to generate the .p12 cert/key pair.

Run the Application:
```
mvn spring-boot:run
```

## Attribution

- [@foo-git](https://github.com/foo-git) -:- [rewe-discounts](https://github.com/foo-git/rewe-discounts)
- [@torbenpfohl](https://github.com/torbenpfohl) -:- [rewe-discounts](https://github.com/torbenpfohl/rewe-discounts)
- [@ByteSizedMarius](https://github.com/ByteSizedMarius) -:- [rewerse-engineering](https://github.com/ByteSizedMarius/rewerse-engineering)

Without these people we would have no way of accessing REWE discount data. Thank you for your work. <3

More (probably) to come!
