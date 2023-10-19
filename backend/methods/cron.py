from config.database import mydb
from flask import Blueprint, request, jsonify
from .encryptDecyrpt import decrypt,encrypt
from decouple import config
cron_module = Blueprint('cron', __name__)
@cron_module.route('/age_update', methods=['POST'])
def ageUpdate():
    #get authorization token
    token = request.form.get('token')
    if token == config('authtoken'):
        try:
        # Fetch all users from the 'users' collection
            users = list(mydb.users.find({}))
            # Serialize the users to JSON format
            for user in users:
                for key, value in user.items():
                    if key == 'age':
                        value = int(decrypt(user['age'])) + 1
                        print(value)
                        encryptedValue = encrypt(str(value))
                        #update Value and store into database
                        result = mydb.users.update_one({"_id": user['_id']},{"$set":{"age":encryptedValue}})
                        if not result:
                            raise Exception("Age update is not working")       
            return jsonify({'message': 'Successfully Run Cronjob'}), 200
        except Exception as e:
            return jsonify({'error': str(e)}), 500
    else:
        return jsonify({'message': 'Failed Run Cronjob'}), 500