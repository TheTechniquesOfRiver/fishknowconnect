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
    s3_client.upload_fileobj(image_file, config('AWS_S3'), image_filename, ExtraArgs={'ACL':'public-read'})
    s3_url = f"https://{config('AWS_S3')}.s3.amazonaws.com/{image_filename}"
    return s3_url

# New route to handle post creation
@post_module.route('/create_post', methods=['POST'])
def create_post():
    # Get post data from the request JSON
    post_data = request.form.to_dict()

    # Ensure that all required fields are provided in the request
    required_fields = ['title', 'type', 'content']
    for field in required_fields:
        if field not in post_data:
            return jsonify({'error': f'Missing required field: {field}'}), 400

    # Check if there's a file in the request
    s3_url = ''
    if 'file' in request.files:
        for i in request.files.getlist('file'):
            if i.filename != '':
                # Securely generate a filename for the image
                image_filename = i.filename
                # Upload the image to S3
                if s3_url == '':
                    s3_url = upload_image_to_s3(i, image_filename)
                else:
                    s3_url = s3_url + ',' + upload_image_to_s3(i, image_filename)
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
    s3_url = ''
    if 'file' in request.files:
        for i in request.files.getlist('file'):
            if i.filename != '':
                # Securely generate a filename for the image
                image_filename = i.filename
                # Upload the image to S3
                if s3_url == '':
                    s3_url = upload_image_to_s3(i, image_filename)
                else:
                    s3_url = s3_url + ',' + upload_image_to_s3(i, image_filename)               

    # Update the post in the 'posts' collection based on the post_id
    try:
        target_id = ObjectId(post_id)
        post = mydb.posts.find_one({"_id": target_id})

        if post['file_url'] == '':
            post_data['file_url'] = s3_url            
        else:
            s3_url = post['file_url'] + ',' + s3_url
            post_data['file_url'] = s3_url     

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
                'id': str(post['_id']),
                'title': post['title'],
                'type': post['type'],
                'content': post['content'],
                'file_url': post.get('file_url', None)  # Handle posts without images
            })

        return jsonify(serialized_posts), 200

    except Exception as e:
            return jsonify({'error': str(e)}), 500
    
@post_module.route('/get_post_by_id/<string:post_id>', methods=['GET'])
def get_post_by_id(post_id):
    try:
        # Fetch a from the 'posts' collection
        target_id = ObjectId(post_id)
        post = mydb.posts.find_one({"_id": target_id})

        # If there is no posts, return nothing
        if not post:
            return jsonify([])

        # Serialize the post to JSON format
        serialized_post = {
            'id': str(post['_id']),
            'title': post['title'],
            'type': post['type'],
            'content': post['content'],
            'file_url': post.get('file_url', None)  # Handle posts without images
        }

        return jsonify(serialized_post), 200

    except Exception as e:
            return jsonify({'error': str(e)}), 500