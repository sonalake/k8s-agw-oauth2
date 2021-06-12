### Start minikube
```
minikube start --vm-driver virtualbox --host-only-cidr 192.168.XX.XX/24
```

### Check minikube host ip - should be used to reach host services e.g. Keycloak
``` 
minikube ssh
ping host.minikube.internal
```

### Change docker context
#### local -> minikube context
```
eval $(minikube docker-env)
```
#### back from minikube -> local context
```
eval $(minikube docker-env -u)
```

### Build AGW
```
./gradlew clean build
cd agw
docker build -f Dockerfile -t msagw-agw build/libs
```

### Build portal
```
cd portal
docker build -t msagw-portal .
```

### Build users service
```
./gradlew clean build
cd users
docker build -f Dockerfile -t msagw-users build/libs
```

