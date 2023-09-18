from flask import Blueprint, request, jsonify
from config.database import mydb
from config.aws import s3_client
from werkzeug.utils import secure_filename
from decouple import config
import os
from pathlib import Path
from bson import ObjectId

post_module = Blueprint('post', __name__)

# Define a function to upload an image to S3
def upload_image_to_s3(image_file, image_filename):
    path = r"C:\Users\NishadiEdirisinghe\Pictures\Screenshots"
    s3_key = os.path.join(path, image_filename)
    s3_client.upload_file(s3_key, config('AWS_S3'), s3_key)
    s3_url = f"https://{config('AWS_S3')}.s3.amazonaws.com/{s3_key}"
    return s3_url

# New route to handle post creation
@post_module.route('/create_post', methods=['POST'])
def create_post():
    # Get post data from the request JSON
    post_data = request.form.to_dict()

    # Ensure that all required fields are provided in the request
    required_fields = ['id', 'title', 'type', 'content']
    for field in required_fields:
        if field not in post_data:
            return jsonify({'error': f'Missing required field: {field}'}), 400

    # Check if there's a file in the request
    if 'file' in request.files:
        image_file = request.files['file']
        if image_file.filename != '':
            # Securely generate a filename for the image
            image_filename = image_file.filename
            # Upload the image to S3
            s3_url = upload_image_to_s3(image_file, image_filename)
            # Add the S3 URL to the post data
            post_data['file_url'] = s3_url

    # Insert the new post into the 'posts' collection
    try:
        mydb.posts.insert_one(post_data)
        return jsonify({'message': 'Post created successfully'}), 201

    except Exception as e:
        return jsonify({'error': str(e)}), 400

# New route to handle post update
@post_module.route('/update_post/<string:post_id>', methods=['PUT'])
def update_post(post_id):
    # Get post data from the request JSON
    post_data = request.form.to_dict()

    # Ensure that at least one field to update is provided
    if not post_data and not 'file' in request.files:
        return jsonify({'error': 'No fields to update provided'}), 400

    # Check if there's a file in the request
    if 'file' in request.files:
        image_file = request.files['file']
        if image_file.filename != '':
            # Securely generate a filename for the image
            image_filename = image_file.filename
            # Upload the image to S3
            s3_url = upload_image_to_s3(image_file, image_filename)
            # Add the S3 URL to the post data
            post_data['file_url'] = s3_url

    # Update the post in the 'posts' collection based on the post_id
    try:
        target_id = ObjectId(post_id)
        result = mydb.posts.update_one({'_id': target_id}, {'$set': post_data})

        if result.modified_count > 0:
            return jsonify({'message': 'Post updated successfully'}), 200
        else:
            return jsonify({'message': 'No matching post found for update'}), 404

    except Exception as e:
        return jsonify({'error': str(e)}), 400

@post_module.route('/get_all_posts', methods=['GET'])
def get_all_posts():
    try:
        # Fetch all posts from the 'posts' collection
        posts = list(mydb.posts.find({}))

        # If there are no posts, return an empty list
        if not posts:
            return jsonify([])

        # Serialize the posts to JSON format
        serialized_posts = []
        for post in posts:
            serialized_posts.append({
                'id': post['id'],
                #'title': post['title'],
                #'type': post['type'],
                #'content': post['content'],
                'image_url': post.get('image_url', None)  # Handle posts without images
            })

        return jsonify(serialized_posts), 200

    except Exception as e:
            return jsonify({'error': str(e)}), 500