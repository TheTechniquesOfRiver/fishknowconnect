from flask import Blueprint, request, jsonify
from config.database import mydb
from .encryptDecyrpt import decrypt

profile_module = Blueprint('profile', __name__)
#get profile route
@profile_module.route("/get_profile", methods=['GET'])
def getProfile():
    username = request.args.get('username')
    if username:
        profile = mydb.users.find_one({"username": username})
    #return responce convert into json formate
        data = {'username': profile['username'],
                'age': str(decrypt(profile['age'])).lstrip("b'").rstrip("'"),
                'location':  str(decrypt(profile['location'])).lstrip("b'").rstrip("'")}
        return jsonify(data), 200
    else:
        return jsonify({}), 200
