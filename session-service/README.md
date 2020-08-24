# Session Service

Session service is essentially a [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) API that acts on top of the database Session DB. We use session service in order to manage the user sessions. Well in fact we don't manage sessions but [Json Web Tokens (JWT)](https://jwt.io/introduction/). Each session is represented by a JWT. 

> Session service is written in java & is based on the Spring Framework.

JWT can be used for a number of different scenarios in our scenario we use JWT to autorize client requests.

> Authorization: This is the most common scenario for using JWT. Once the user is logged in, each subsequent request will include the JWT, allowing the user to access routes, services, and resources that are permitted  with that token. Single Sign On is a feature that widely uses JWT nowadays, because of its small overhead and its ability to be easily used across dget dom value
ifferent domains.


## JWT Generation

Aprart from the CRUD operations, this service also is responsible for the generation of the JWT token. Lets take a small look at this procedure shall we ?

### Client Registration Endpoint.

First of all lets start from the client registration endpoint. This service provides the endpoint for the client registration which is : `https://3djuumpinfinite.teams.akka.eu/zuul/session-service/session/register`. By posting a json model of the `TockenRequestModel` class, and if the information you provided are valid, a record in the `sessionDB` will be created (or overwritten if exists) and a custom self signed token will be returned to you. More info regarding this procedure you can find on the [authentication-guide](https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-x-teams/-/blob/master/3djuump-x-teams-authentication.md).

### Session Managment & Registration

Before we generate the self-signed jwt token, we need to validate the information that the client has provide to us. To do so we use the [`graph-service`](https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-x-teams/-/tree/production/graph-api). To validate the information we ask from [Microsoft Graph](https://docs.microsoft.com/en-us/graph/overview) to return to as the corresponding user information base ofc on the token that he provided to us. If the token is valid and not expired, the user info will be return and mocked by the class `MicrosoftTeamsUser`.

```java

     public SessionInfo register_user_by_AAD_token(TockenRequestModel auth_model) throws AADAuthenticationInformationNonSetException, SessionAlreadyExists, JsonProcessingException {
        logger.info("Register Request has been triggered. Validation process.");
        if (auth_model == null || auth_model.getAccess_token().isEmpty()) {
            logger.log(Level.SEVERE, "The authentication informations where not included on the request.");
            throw new AADAuthenticationInformationNonSetException("The authentication informations where not found");
        }
        // Requesting user information based on givven token
        // Microsoft token validation. If the token provided is valid,
        // Microsoft Graph will return the user's information.
        MicrosoftTeamsUser user = null;
        ResponseEntity obj = graphService.userInfo(new UserToken(auth_model.getAccess_token()));
        if (obj.getStatusCode() == HttpStatus.OK) {
            user = mapper.convertValue(obj.getBody(), MicrosoftTeamsUser.class);
        }

        return manageSession(user, auth_model);
    }

```

After we verify and get the user information from [Microsoft Graph](https://docs.microsoft.com/en-us/graph/overview) we create a new record on the `sessionDB`, if the record exists, we simply overwrite it. The record has 5 attributes, the `id-token`, the `access-token`, the `username`, the `token-expiration-date` and finally the corresponding Microsoft generated `user-id`. 


```java
   private SessionInfo manageSession(MicrosoftTeamsUser user, TockenRequestModel auth_model) {
        //Generate Session
        SessionInfo session = new SessionInfo(
                user.getId().toString(),
                user.getDisplayName(),
                auth_model.getId_token(),
                auth_model.getAccess_token(),
                Integer.parseInt(auth_model.getExpires_in())
        );
        //register session on the database
        //each time user tries to register a session simpley overwrites the previus one
        this.sessionRepo.createSession(session);
        logger.log(Level.INFO, "User : {0} has been registerd to the database.", user.getDisplayName());
        //generate new selfSigned token && overwrite the session token
        session.setAccessToken(
                tokeUtil.generateToken(
                        session,
                        Integer.parseInt(auth_model.getExpires_in()) - 100
                )
        );
        //Overwrite the expiring time of the token 
        // That way the self signed token will always expire before the microsoft one
        //so as to trigger silent authentication on the client side
        session.setExpiring(Integer.parseInt(auth_model.getExpires_in()) - 100);
        return session;
    }
    
```

Next we generate a new self signed-token, with the appropriate user information and with an expiration date 100 milliseconds before the one of the Microsoft Token. To generate the Json Web Token we use the `JwtTokenUtil`. `JwtTokenUtil` class models the functionality of a Json Web Token, this class provides functions such as `getUsernameFromToken` `getExpirationDateFromToken` `getClaimFromToken` `generateToken` and `validateToken`. The token is signed/encrypted based on a signature that we have create. This signature is distributed by the cloud-config-server. The signature can be found on the configuration repository on the file [session-service-docker.yml](https://gitlab.akka.eu/Georgios.FIOTAKIS/3djuump-config/-/blob/master/session-service-docker.yml). The signature is stored encrypted, and gets decrypted uppon the runtime of the `cloud-config-server`. The corresonding attribute that holds the signature is called `base-config.jwtSecret`.

```java
    @Component
    public class JwtTokenUtil implements Serializable {

        private static final long serialVersionUID = -2550185165626007488L;
    //    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

        //Retrieve Secret Configuration from Cloud Config Server
        @Autowired
        private BaseConfiguration config;
        //retrieve username from jwt token

        public String getUsernameFromToken(String token) {
            return getClaimFromToken(token, Claims::getSubject);
        }
        //retrieve expiration date from jwt token

        public Date getExpirationDateFromToken(String token) {
            return getClaimFromToken(token, Claims::getExpiration);
        }

        public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        }

        //for retrieveing any information from token we will need the secret key
        private Claims getAllClaimsFromToken(String token) {
            return Jwts.parser().setSigningKey(config.getJwtSecret()).parseClaimsJws(token).getBody();
        }

        //check if the token has expired
        private Boolean isTokenExpired(String token) {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        }

        //generate token for user
        public String generateToken(UserDetails userDetails, int expiresIn) {
            Map<String, Object> claims = new HashMap<>();
            return doGenerateToken(claims, userDetails.getPassword(), expiresIn);
        }

        //while creating the token -
        //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
        //2. Sign the JWT using the HS512 algorithm and secret key.
        //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
        //   compaction of the JWT to a URL-safe string
        private String doGenerateToken(Map<String, Object> claims, String subject, int expiresIn) {
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiresIn * 1000))
                    .signWith(SignatureAlgorithm.HS512, config.getJwtSecret()).compact();
        }

        //validate token
        public Boolean validateToken(String token, UserDetails userDetails) {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getPassword()) && !isTokenExpired(token));
        }
    }
```

## Session Service endpoint table

URL-Prefix | endpoint| external-url (proxy) | Method | Model | Description 
------------ | ------------- | ------- | ---------- | ------------ |------------------ 
`/cloner` | `/analyze_site` | `https://3djuumpinfinite.teams.akka.eu/zuul/clone-service/cloner/analyze_site`| POST  | `Clone_req_temp` | By posting a json representation of a `Clone_req_temp` on this endpoint you will get as response the stracture of the posted share-point-folder (if this folder is registered on a sharepoint on your domain ofc). 

## Author & Licence

Original Author         :   [George Fiotakis](https://www.linkedin.com/in/george-fiotakis-320967159/). <br>
Contact Information     :   georgios.FIOTAKIS@akka.eu <br>
Contact Information     :   g.fiotakis@hotmail.com <br>

*This document and it's sub components & documents are Confidential INFORMATION which belongs to the group of AKKA Technologies & REAL FUSIO. Any
attempt of replication and distribution of this document without AKKA Technology's and REAL FUSIO's consent, will be persecuted legally.*

