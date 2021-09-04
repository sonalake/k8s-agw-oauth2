#Minikube
## Start minikube
```
npm run start
```

## Check minikube host ip - should be used to reach host services e.g. Keycloak in msagw-oauth ConfigMap (oauth-cm.yaml)
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

#Building the system
Minikube must be up and running and docker context in the shell must be set to minikube [see](#from-local-to-minikube)

## Build AGW
```
build:agw
```

## Build portal
```
build:portal
```

## Build users service
```
build:users
```

