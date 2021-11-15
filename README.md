# Intro
**This project shows an approach of having distributed OAuth2 authentication with API Gateway in Kubernetes environment**

It consists of 3 components:
- agw - API gateway component (AGW) - spring boot application leveraging on spring-cloud-gateway
- portal - FE React app serving as a client application of the system
- customers - simple BE API service

Other important directories:
- k8s - contains system wide kubernetes configuration
# Minikube
## Start minikube
```
npm run start
```

## Check minikube host IP
This IP should be used to reach host services e.g. Keycloak running as a docker container in host machine.
Use this IP to replace oauth related properties in `k8s/oauth-cm.yaml`.

To get you minikube host IP run the following commands:
``` 
minikube ssh
ping host.minikube.internal
```

## Change docker context
Images to be deployed in minikube must be build in minikube docker context. Use below commands to switch local docker context to minikube and vice versa.

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

NOTICE: Before deploying any component to minikube remember to apply the files under k8s directory e.g. by running:
```
npm run k8s
```
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

## Build customers service
```
build:customers
```

