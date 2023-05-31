# ASE Deliery



## Getting started

```
cd existing_repo
git remote add origin https://gitlab.lrz.de/ase-22-23/team11/ase-deliery.git
git branch -M main
git push -uf origin main
```





## Raspberry Pi

After turning RaspberryPi on you should type:
```
cd Desktop/Project
python3 readRFID.py
```

Reading the RFID-tokens will happen automatically along with authentication and authorization.

Configurations can be found under 
```
cat config.json.
```
List of current deliverers and customer can be found under 
```
cat usersInfo.json
```

In order to manually change RaspberryPi's ID
```
nano config.json
```

## Backend Sevices

Run backend services (Spring Boot Maven Projects) in the following order:

- discovery-server
- cas-service
- delivery-service
- api-gateway

## React Frontend

Run frontend UI-Service
```
cd ui-service
npm install --legacy-peer-deps
npm start 
```

## Login Information

Role "Dispatcher"  
email: `dispatcher1@gmail.com`  
password: `123`

