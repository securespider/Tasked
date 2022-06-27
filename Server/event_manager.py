import firebase_admin
import time

CERTIFICATE_NAME = "canvas-hybrid-350510-firebase-adminsdk-9biyk-d4d6446df9.json"
DATABASE_URL = "https://console.firebase.google.com/u/2/project/canvas-hybrid-350510/database/canvas-hybrid-350510-default-rtdb/data/~2F"
cred_obj = firebase_admin.credentials.Certificate(CERTIFICATE_NAME)
FA = firebase_admin.initialize_app(cred_obj, {'databaseURL': DATABASE_URL})

def create_event(user_id:str, event_details:dict):
    
def get_events():
    #To be completed
    raise NotImplementedError

