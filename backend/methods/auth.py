from flask import Blueprint, request, jsonify
from .encryptDecyrpt import encrypt
from config.database import mydb
from werkzeug.security import generate_password_hash, check_password_hash
from bson import ObjectId

auth_module = Blueprint('auth', __name__)
@auth_module.route('/register', methods=['POST'])
def register():
    # Get registration data from request headers or body
    username = request.form.get('username')
    password = request.form.get('password')
    age = request.form.get('age')
    location = request.form.get('location')

    # check user name exists or not
    existing_user = mydb.users.find_one({'username': username})
    if existing_user:
        return jsonify({'message': 'Username already exist'}), 401
    if not username:
        return jsonify({'message': 'Enter Username'}), 401
    if not password:
        return jsonify({'message': 'Enter Password'}), 401
    
    # Created hased password 
    hashed_password = generate_password_hash(password)
    # Encrypt age and location
    if age:
        encrypt_age = encrypt(age)
    else:
        encrypt_age = None
    if location:
        encrypt_location = encrypt(location)
    else:
        encrypt_location = None
    try:
        # Create a new user object
        mydb.users.insert_one({"username": username, "password": hashed_password, "age": encrypt_age,"location": encrypt_location })
        return jsonify({'message': 'Successfully registered'}), 200

    except Exception as e:
        return jsonify({'message': str(e)}), 400


@auth_module.route('/login', methods=['POST'])
def login():
    # Get login data from request headers or body
    username = request.form.get('username')
    password = request.form.get('password')
    if not username:
        return jsonify({'message': 'Enter Username'}), 401
    if not password:
        return jsonify({'message': 'Enter Password'}), 401

    #check username and password 
    try:
        user = mydb.users.find_one({'username': username})
        if not user or not check_password_hash(user['password'], password):
            return jsonify({'message': 'Invalid username or password.'}), 401
        if user and check_password_hash(user['password'], password):
            return jsonify({'message': 'Successfully Login'}), 200
        return jsonify({'message': 'Invalid username or password'}), 401
    except Exception as e:
        return jsonify({'message': str(e)}), 400   


@auth_module.route('/get_all_users', methods=['GET'])
def get_all_users():
    try:
        # Fetch all users from the 'users' collection
        users = list(mydb.users.find({}))

        # If there are no users, return an empty list
        if not users:
            return jsonify([])

        # Serialize the users to JSON format
        serialized_users = []
        for user in users:
            serialized_user = {}
            for key, value in user.items():
                if key == '_id':
                    value = str(user['_id'])
                serialized_user[key] = value
            serialized_users.append(serialized_user)

        return jsonify(serialized_users), 200

    except Exception as e:
            return jsonify({'error': str(e)}), 500
    
@auth_module.route('/get_user_by_id/<string:user_id>', methods=['GET'])
def get_user_by_id(user_id):
    try:
        # Fetch a from the 'users' collection
        target_id = ObjectId(user_id)
        user = mydb.users.find_one({"_id": target_id})

        # If there is no users, return nothing
        if not user:
            return jsonify([])

        # Serialize the user to JSON format
        serialized_users = []
        
        serialized_user = {}
        for key, value in user.items():
            if key == '_id':
                value = str(user['_id'])
            serialized_user[key] = value
        serialized_users.append(serialized_user)

        return jsonify(serialized_user), 200

    except Exception as e:
            return jsonify({'error': str(e)}), 500