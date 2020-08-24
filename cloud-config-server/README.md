# cloud-configuration-server

This is the implementation of the cloud-config service. This service provides sensitive information encryptio/decryption as well as cloud enviroment configuration. It is binded with a git repository that holds all the configuration yml for each service. Last but not least cloud-config is registered with a Rabbit MQ Message Broker so as to provide notifications on the services uppon configuration change. As mentioned the cloud-config server is based on the [spring-cloud-config](https://cloud.spring.io/spring-cloud-config/reference/html/) open-source framework.

> This service is not registered as client to Eurika server both for sequrity & performance reassons. Therefore its endpoints are not rechable from the `zuul-proxy`. 

## Git Repository Bind.
To state the git repository from which cloud-config will retrieve the configurations you need to navigate to the `application.yml` of the service. The `application.yml` can be found on the [`src/main/resources/application.ymp`](-/blob/production/cloud-config-server/src/main/resources/application.yml).

```yaml
    server:
        port: 8888
    spring:
        # Here we can see the configuration of the Rabbit MQ subsription which provides the on-demand configuration update.
        # The password of the user abstract is encrypted and gets decrypted by the cloud config-server uppor run-time.   
        rabbitmq:
            host: rabbit-server
            port: 5672
            username: abstract
            password: '{cipher}AQBpx0l+T9zbvZ2M97tX2xfxjv3lc0ptyRzFkJnxKrGJmj2OQTnAtfRoRCIET23k8cUGVwncJScNLzeMQGvFgjbJ96FlSE19FKJ1/R1R1zyPdUoP/26iiy1U0xT6obxyY5MaqY4i26bu6ac6GitjAl3VCo0CGOgUMSRuVbrVhnuGjhSJSZxfufbWV5kWBhfQGLiLQwf/LxjtzvBPWrCASyhFBRXkEQ8yUIPtoqiWl7jv/f4GNvikDsB0PtcvQ7hF0BZmMx8MvmXZUrY9mpsXMMUEc8yrATwxRGqti5ytd2A3obTiywc1A3LkjlMTmfcC4FnSFRtBpVv4cy+8TEK72jx7lqWVMK5wwMFJDtK3EdvvM2ZDkPz1ZSgLSnckhCPX2oE='
        cloud:
            config:
                # You can see the definition of the git repository, the username & the password here.
                # For the production version it would be wise to create a repository and and SSH key. State the SSH key here instead of the password of the account so as to clone with ssh instead of https. You can find the detailed guide on the spring-cloud-documentation
                server: {git: {clone-on-start: true, uri: 'https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-config.git', username: 'georgios.fiotakis' , password: '{cipher}AQAAigdNfGbe2VlaN2nbjyBL5xqU7gTU/wRz45TBaxjbdZ5RP9T92BQFQ4gzD0ReaNFuOo0ML/92Oj5M0V+lhdrbyg77Z3BqJRgwjNhBh45Ua44x89y/pTq9FAVXDeifyApewT7GdJVyWBxg8hSoQl9HsFJsKWZFQas0Y9ndccT0x65Ni2tN0lcEyHrrvx0OScS6sjn+Iv/8u4xWpFsMNbL5SWVqqruWU8jC0mPOAXXx6AWtzdwLlts82Nxynfodi60zUkB2o1TloQfXALdnXBqejKG9u3qGh/3J4xXtF6Rn/gW0AJfibpr0fAfR77+auQDEkUlH0iMFlDJJ89vwo744xRrxR+gLgVtLeN6/+65i3l0UhiVOGa3iFhOyvxmtUxXuhyfce1UMfxk9EywfBRx7'}}
        application:
            name: config-server
    # We expose the /bus-refresh endpoint so as to be able to notify on-demand the ecosystem uppon configuration change, with the help of Rabbit MQ.
    management:
        endpoints:
            web:
                exposure: {include: bus-refresh }
```

> The default configuration git repository can be found : https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-config

> As you can see the password of the git repository is encrypted and instead of the password we store the corresponding hash. To encrypt/decrypt the password you need to use the  `encrypt/decrypt` endpoints, that the cloud config server provides. For more information on the subject please see the [coresponding documentation](https://cloud.spring.io/spring-cloud-config/reference/html/#_encryption_and_decryption).

> The RSA key compination & the encryption certificate are compined on the [keys/micro-env-key.jks](-/blob/production/cloud-config-server/keys/micro-env-key.jks) file. You can generate a new `jks` rsa encryption key by following [this tutorial](https://docs.oracle.com/cd/E19509-01/820-3503/ggfen/index.html). Then you simply have to overwrite the existing one (*in that case you will have to re-encrypt the attributes & replace them on the [configuration-repository](https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-config)*).

> For more details regarding the distribution of the configurations please reffair to the 3djuump-x-teams [software-architecture document](https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-x-teams/-/blob/master/3djuump-x-teams-software-architecture.md#enviroment-cloud-configuration).


## Cloud config server endpoint table

URL-Prefix | endpoint| external-url (proxy) | Method | Model | Description 
------------ | ------------- | ------- | ---------- | ------------ |------------------ 
`/cloner` | `/analyze_site` | `https://3djuumpinfinite.teams.akka.eu/zuul/clone-service/cloner/analyze_site`| POST  | `Clone_req_temp` | By posting a json representation of a `Clone_req_temp` on this endpoint you will get as response the stracture of the posted share-point-folder (if this folder is registered on a sharepoint on your domain ofc). 

> Cloud config server is not registered with Eureka server both for security & performace reassons. So, to trigger a general service configuration refresh you should gain a shell to the container or expose the port to host machine.

## Author & Licence

Original Author         :   [George Fiotakis](https://www.linkedin.com/in/george-fiotakis-320967159/). <br>
Contact Information     :   georgios.FIOTAKIS@akka.eu <br>
Contact Information     :   g.fiotakis@hotmail.com <br>

*This document and it's sub components & documents are Confidential INFORMATION which belongs to the group of AKKA Technologies & REAL FUSIO. Any
attempt of replication and distribution of this document without AKKA Technology's and REAL FUSIO's consent, will be persecuted legally.*


