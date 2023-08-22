from flask import Flask
from pymongo.mongo_client import MongoClient
from decouple import config
from config.database import client
from methods.auth import auth_module

app = Flask(__name__)
try:
    client.admin.command('ping')
    print("Pinged your deployment. You successfully connected to MongoDB!")
except Exception as e:
    print(e)

app.register_blueprint(auth_module)

if __name__ == '__main__':
    app.run(debug=True,host='localhost',port=3000)