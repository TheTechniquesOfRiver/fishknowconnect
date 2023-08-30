from config.aws import s3_client
from decouple import config
from flask import Blueprint, request, jsonify

aws_s3 = Blueprint('s3', __name__)
@aws_s3.route('/signedUrl', methods=['POST'])
def upload():
    keyName = request.form.get('file_name')
    response = s3_client.generate_presigned_post(
        Bucket = config('AWS_S3'),
        Key = keyName,
        ExpiresIn = 100
    )
    return jsonify(response),200