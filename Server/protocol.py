import json
import event_manager

def increment_string_int(num:str):
    return str(int(num) + 1)

def new_user(username:str, password:str):
    with open("accounts.json", "r+") as f:
        file = json.load(f)
        number_of_accounts = file['number_of_accounts']
        accounts = file["accounts"]
        for account in accounts:
            if account["username"] == username:
                print("duplicate user")
                return 0
        user_id = increment_string_int(number_of_accounts)
        accounts.append({"username":username, "password":password, "user-id": user_id})
        file['number_of_accounts'] = user_id
        f.seek(0)
        json.dump(file, f, indent=4)
        return user_id

def authentication(username:str, password:str):
    with open("accounts.json", "r+") as f:
        file = json.load(f)
        accounts = file["accounts"]
        for account in accounts:
            if account["username"] == username and account["password"] == password:
                return account["user-id"]
    return 0

def protocol_handler(post_data:str):
    request = json.loads(post_data)
    request_type = request["Type"]
    return_json = {}
    if request_type == "New-User":
        d = request["Data"]
        u, p = d["Username"], d["Password"]
        i = new_user(u, p)
        return_json = {
            "Type" : "New-User-Reply",
            "Data" : {
                "User-ID" : str(i)
            }
        }

    elif request_type == "Authentication":
        d = request["Data"]
        u, p = d["Username"], d["Password"]
        i = authentication(u, p)
        return_json = {
            "Type" : "Authentication-Reply",
            "Data" : {
                "User-ID" : str(i)
            } 
        }
    return return_json

def main():
    print(authentication("temp", "pass"))

main()