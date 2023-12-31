from flask import Flask
from pymongo.mongo_client import MongoClient
from decouple import config
from config.database import client
from methods.auth import auth_module
from methods.s3Signed import aws_s3
from methods.post import post_module
from methods.profile import profile_module
from methods.cron import cron_module

app = Flask(__name__)
try:
    client.admin.command('ping')
    print("You successfully connected to MongoDB!")
except Exception as e:
    print(e)

app.register_blueprint(auth_module)
app.register_blueprint(aws_s3)
app.register_blueprint(post_module)
app.register_blueprint(profile_module)
app.register_blueprint(cron_module)

if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0',port=3000)