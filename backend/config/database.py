import certifi
from pymongo.mongo_client import MongoClient
from decouple import config
client = MongoClient(config('mongodb'), tlsCAFile=certifi.where())
mydb = client["fishknow"]