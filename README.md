# Intro
**This project shows an approach of having distributed OAuth2 authentication with API Gateway in Kubernetes environment**

It consists of 3 components:
- agw - API gateway component (AGW) - spring boot application leveraging on spring-cloud-gateway
- portal - frontend React app serving as a client application of the system
- users - simple BE API service

Other important directories:
- k8s - contains systemwise kubernetes configuration
# Minikube
## Start minikube
```
npm run start
```

## Check minikube host ip
This IP should be used to reach host services e.g. Keycloak running as a docker container in host machine.
You can see it in msagw-oauth ConfigMap (oauth-cm.yaml).

``` 
minikube ssh
ping host.minikube.internal
```

## Change docker context
Images to be deployed in minikube must be build in minikube docker context

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

NOTICE: Before deploying any component to minikube remember to apply the files under k8s (e.g. by running `npm run k8s`) directory.
It contains config-maps required for services to start properly.

## Build AGW
```
build:agw
```

## Build portal
```
build:portal
```
Make sure npm dependencies are installed first.

## Build users service
```
build:users
```

