# Intro
**This project shows an approach of having distributed OAuth2 authentication with API Gateway in Kubernetes environment**

It consists of 3 components:
- agw - API gateway component (AGW) - Spring boot application leveraging on spring-cloud-gateway
- portal - FE React app serving as a client application of the system
- customers - simple BE API service

Other important directories:
- k8s - contains system wide kubernetes configuration
# Minikube
## Start minikube
```
npm run start
```
Verify that all minikube elements are running
```
minikube status
```
## Kubernetes host
Current system k8s configuration (`k8s/system-cm.yaml`) and ingress configuration (`agw/k8s/ingress.yaml`) are using host `k8s.test` (`k8s.test` is an arbitrary name, you can change it according to your preferences).
In order to reach the application please add a vhost entry like
```
<enter your minikube IP here> k8s.test
```
To get your minikube IP value please run:
```
minikube ip
```

## IDP
In this example the IDP provider is a local instance of [Keycloak](https://www.keycloak.org/) running as a docker container.
Hence, the OAuth2 config (`k8s/oauth-cm.yaml`) is referring to a local keycloak instance, realm `msagw` and client with id `App`.

**NOTICE:** You need to replace the `CLIENT_SECRET` property in the OAuth2 config with the one defined in your IDP client registration.

AGW and Customers service need to communicate with IDP server to complete OAuth2 authentication, which means that IDP server must be reachable from inside the cluster.

As mentioned above in this example IDP server is a docker container, so it has to be exposed to the k8s cluster.
To do so, you need to check at what IP k8s cluster sees your host machine by running:
```
minikube ssh
ping host.minikube.internal
```
Then take the presented IP and put in `k8s/oauth-cm.yaml` for all urls pointing to IDP provider (`JWKS_HOST`, `TOKEN_URI`, `AUTHORIZATION_URI`, `LOGOUT_URI`).

### Local keycloak instance
Keycloak configuration is out of scope of this example, however below are some crucial parts that should be configured. 
Example docker compose for a local keycloak instance:
```
version: '3.7'
services:
  keycloak:
    image: jboss/keycloak
    environment:
      KEYCLOAK_USER: admin
      KEYCLOAK_PASSWORD: admin
    volumes:
      - keycloak_data:/opt/jboss/keycloak/standalone/data
    ports:
      - 8080:8080
      - 8433:8433
volumes:
  keycloak_data:
    driver: local
```

Keycloak client registration requires a `redirect_uri`, which must point to the code exchange endpoint. The default Spring security endpoint for this is `http://k8s.test/login/oauth2/code/<registration key from application.yaml>` which in this case would be `http://k8s.test/login/oauth2/code/iam`.
Using wildcard uri like `http://k8s.test/*` should work just as fine.

In order to authenticate, the user needs to exist in Keycloak realm under which the client is registered.
To allow the system to properly parse the access token, it must contain two claims:
* `email` - provided by default in Keycloak,
* `roles` - needs to be configured manually e.g. as a client mapper (mapping user attribute to an access token claim).

For more details please refer to Keycloak documentation.

## Change docker context
Images to be deployed in minikube must be build in minikube docker context. Use below commands to switch local docker context to minikube (and vice versa) when building the components.

### from local to minikube
```
eval $(minikube docker-env)
```
### from minikube to local
```
eval $(minikube docker-env -u)
```

# Building the system
Minikube must be up and running and docker context in the shell must be set to minikube [see](#from-local-to-minikube)

**NOTICE:** Before deploying any component to minikube remember to apply the files under k8s directory e.g. by running:
```
npm run k8s-config
```
It contains config-maps required for services to start properly.

## Build AGW
```
npm run build:agw
```

## Build portal
Make sure that npm dependencies are installed:
```
cd portal
npm ci
cd ..
```
Build the portal
```
npm run build:portal
```

## Build customers service
```
npm run build:customers
```

