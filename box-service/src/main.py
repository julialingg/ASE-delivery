import RPi.GPIO as GPIO
import time
import json
from mfrc522 import SimpleMFRC522
import requests

hostname = "172.20.10.7"
port = 10789
api_url = "http://" + hostname + ":" + str(port) + "/box"
params = {
    "mode": "cors",
    "cache": "no-cache",
    "credentials": "include",
    "redirect": "folow",
    "referrerPolicy": "origin-when-cross-origin"
}
delivery_status_api_url = "http://" + hostname + ":" + str(port) + "/delivery/status"
user_api_url = "http://" + hostname + ":" + str(port) + "/user/getUser"
reader = SimpleMFRC522()
GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
GPIO.setup(40, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(38, GPIO.OUT, initial=GPIO.LOW)
pin = 36
GPIO.setup(pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)
turnOff = GPIO.LOW
turnOn = GPIO.HIGH
red = 40
green = 38
boxInfo = {}
#headers = {}

def getXSRFToken():
    r = requests.get("http://" + hostname + ":" + str(port) + "/auth/csrf")
    #--------------------- we ask for csrf ------------------------
    print(r.text)
    return r.cookies.get_dict()["XSRF-TOKEN"]

def getHeaders(token) :
    return {
        "Content−Type": "application/json",
        "x-xsrf-token" : token
    }

def light_led(colour, mode):
    GPIO.output(colour, mode)


def isClosed():
    if GPIO.input(pin) == 1:
        print("there is no light")
        return "True"
    elif GPIO.input(pin) == 0:
        print(GPIO.input(pin))
        print(" there is a light")
        return "False"


def writeUserIds():
    userIdsFile = open("userIds.json", "w")
    #--------Find current deliverers and customer----------
    config = open("config.json")
    boxName = json.load(config)
    global boxInfo
    boxInfo = requests.get(api_url + "/" + boxName["ID"], headers = headers)
    print(boxInfo.content)
    boxInfo = boxInfo.json()
    userIdsFile.write("[")
    #--------Write all customers and deliverers to the userIds.json -----------
    for delivery in boxInfo["deliveries"]:
        delivererInfo = requests.get(user_api_url + "/" + delivery["delivererEmail"], headers = headers)
        print("THIS IS THE DELIVERER EMAIL - GETUSER")
        print(delivererInfo.json())
        userIdsFile.write(str(json.dumps(delivererInfo.json())) + ",")
    customerInfo = requests.get(user_api_url + "/" + boxInfo["customerEmail"], headers = headers)
    userIdsFile.write(str(json.dumps(customerInfo.json())))
    userIdsFile.write("]")
    userIdsFile.close()


# ToDo write function to check configs too





def authenticate(userId):
    userIdsFile = open("userIds.json")
    credentials = json.load(userIdsFile)
    for credential in credentials:
        print(credential["id"])
        print("do these parameters match?", credential["rfidToken"] ==(str(userId)))
        if credential["rfidToken"] == str(userId):
            print("match found!", credential["role"])

            if credential["role"] == "CUSTOMER":

                if boxInfo["deliveries"] is None:
                    print("cant open box, cause its empty, dear customer")
                    return False
                else:
                    light_led(green, turnOn)
                    time.sleep(10)
                    light_led(green, turnOff)
                    print("isClosed?" + isClosed())
                    if isClosed() == "True":
                        for delivery in boxInfo["deliveries"]:
                            if delivery["status"] == "DELIVERED":
                                payload = {"id": delivery["id"], "status": "COLLECTED", "boxId": boxInfo["id"]}
                                res = requests.put(delivery_status_api_url, params = payload, headers = headers)
                                print(res)
                        return True
                    else:
                        print("box is not closed!")
                        light_led(red, turnOn)
                        time.sleep(5)
                        light_led(red, turnOff)
                        # ToDo what to do?

            else:
                light_led(green, turnOn)
                time.sleep(10)
                light_led(green, turnOff)
                if isClosed() == "True":
                # this is a deliverer, we have to update his deliveries
                    for delivery in boxInfo["deliveries"]:
                        if delivery["delivererEmail"] == credential["email"]:
                            payload = {"id": delivery["id"], "status": "DELIVERED", "boxId": boxInfo["id"]}
                            res = requests.put(delivery_status_api_url, params = payload, headers = headers)
                            print(res)
                else:
                    print("box is not closed!")
                    light_led(red, turnOn)
                    time.sleep(5)
                    light_led(red, turnOff)
                    # ToDo what to do?
            return True
    return False


try:
    token = getXSRFToken()
    global headers
    headers = getHeaders(str(token))
    print("THIS IS THE CSRF TOKEN:")
    print(headers)

    while True:
        print("Hold your tag near RFID-reader!")
        id, text = reader.read()
        GPIO.output(38, GPIO.LOW)
        print(id, text)
        print("calling function auth")
        writeUserIds()
        if authenticate(id):
            print("we already called green light in authenticate")
            continue
        else:
            print("we call red in the while loop false")
            light_led(red, turnOn)
            time.sleep(5)
            light_led(red, turnOff)
            continue

except KeyboardInterrupt:
    GPIO.cleanup()
    raise
