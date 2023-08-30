import boto3 
from botocore.exceptions import NoCredentialsError
from decouple import config
s3_client = boto3.client(
    's3',
    region_name=config('AWS_REGION'),
    aws_access_key_id=config('AWS_ACCESS_KEY'),
    aws_secret_access_key=config('AWS_SRCREATE_KEY'),
)
