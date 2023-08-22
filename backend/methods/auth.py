from flask import Blueprint, request, jsonify
from config.database import mydb
from werkzeug.security import generate_password_hash, check_password_hash

auth_module = Blueprint('auth', __name__)
@auth_module.route('/register', methods=['POST'])
def register():
    # Get registration data from request headers or body
    username = request.form.get('username')
    password = request.form.get('password')


    existing_user = mydb.users.find_one({'username': username})
    if existing_user:
        return jsonify({'error': 'Username already exists.'}), 409
    hashed_password = generate_password_hash(password)

    try:
        # Create a new user object
        mydb.users.insert_one({"username": username, "password": hashed_password})
        return jsonify({'message': 'Registration successful'}), 201

    except Exception as e:
        return jsonify({'error': str(e)}), 400


@auth_module.route('/login', methods=['POST'])
def login():
    # Get login data from request headers or body
    username = request.form.get('username')
    password = request.form.get('password')

 # Retrieve the user from the database
    user = mydb.users.find_one({'username': username})
    if not user or not check_password_hash(user['password'], password):
        return jsonify({'error': 'Invalid username or password.'}), 401
    if user and check_password_hash(user['password'], password):
        return jsonify({'message': 'Login successful'})

    return jsonify({'error': 'Invalid username or password'}), 401
