from flask import Blueprint, request, jsonify
from config.database import mydb
import boto3

post_module = Blueprint('post', __name__)
   
# S3 bucket configuration
s3_bucket_name = "your_s3_bucket_name"
s3_folder = "post_images/"  # Folder in S3 where images will be stored

# Initialize AWS S3 client
s3 = boto3.client('s3')

# Define a function to upload an image to S3
def upload_image_to_s3(image_file, image_filename):
    s3_key = os.path.join(s3_folder, image_filename)
    s3.upload_file(image_file, s3_bucket_name, s3_key)
    s3_url = f"https://{s3_bucket_name}.s3.amazonaws.com/{s3_key}"
    return s3_url

# Create a new route to handle post creation
@post_module.route('/create_post', methods=['POST'])
def create_post():
    # Get post data from the request JSON
    post_data = request.json

    # Ensure that all required fields are provided in the request
    required_fields = ['id', 'title', 'type', 'content']
    for field in required_fields:
        if field not in post_data:
            return jsonify({'error': f'Missing required field: {field}'}), 400

    # Check if there's a file in the request
    if 'image' in request.files:
        image_file = request.files['image']
        if image_file.filename != '':
            # Securely generate a filename for the image
            image_filename = secure_filename(image_file.filename)
            # Upload the image to S3
            s3_url = upload_image_to_s3(image_file, image_filename)
            # Add the S3 URL to the post data
            post_data['image_url'] = s3_url

    # Insert the new post into the 'posts' collection
    try:
        mydb.posts.insert_one(post_data)
        return jsonify({'message': 'Post created successfully'}), 201

    except Exception as e:
        return jsonify({'error': str(e)}), 400