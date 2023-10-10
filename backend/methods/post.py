from flask import Blueprint, request, jsonify
from config.database import mydb
from config.aws import s3_client
from decouple import config
from pathlib import Path
from bson import ObjectId
import pymongo
import datetime

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

    post_data['timestamp'] = datetime.datetime.now()
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

    post_data['timestamp'] = datetime.datetime.now()
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
        posts = list(mydb.posts.find({}).sort("timestamp", pymongo.DESCENDING))

        # If there are no posts, return an empty list
        if not posts:
            return jsonify([])

        # Serialize the posts to JSON format
        serialized_posts = []
        for post in posts:
            serialized_post = {}
            for key, value in post.items():
                if key == '_id':
                    value = str(post['_id'])
                serialized_post[key] = value
            serialized_posts.append(serialized_post)

        return jsonify(serialized_posts), 200

    except Exception as e:
            return jsonify({'error': str(e)}), 500

@post_module.route('/delete_post/<string:post_id>', methods=['DELETE'])
def delete_post_by_id(post_id):
    try:
        # Convert the post_id to a BSON ObjectId
        post_id = ObjectId(post_id)

        # Check if a post with the given ID exists
        existing_post = mydb.posts.find_one({'_id': post_id})

        if existing_post is None:
            return jsonify({'error': 'Post not found'}), 404

        # Delete the post with the given ID
        mydb.posts.delete_one({'_id': post_id})

        return jsonify({'message': 'Post deleted successfully'}), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 400

    
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
        serialized_posts = []
        
        serialized_post = {}
        for key, value in post.items():
            if key == '_id':
                value = str(post['_id'])
            serialized_post[key] = value
        serialized_posts.append(serialized_post)

        return jsonify(serialized_post), 200

    except Exception as e:
            return jsonify({'error': str(e)}), 500
    

@post_module.route('/get_posts', methods=['GET'])
def get_posts():
    try:
        # Get the filter parameter from the URL query string
        access_filter = request.args.get('access')
        type_filter = request.args.get('type')
        author_filter = request.args.get('author')

        # Define filters to fetch posts based on the parameters
        filters = {}
        if access_filter:
            filters["access"] = access_filter
        if type_filter:
            filters["type"] = type_filter
        if author_filter:
            filters["author"] = author_filter

        # Fetch posts from the 'posts' collection based on the filter
        posts = list(mydb.posts.find(filters))

        # If there are no posts, return an empty list
        if not posts:
            return jsonify([])

        # Serialize the posts to JSON format
        serialized_posts = []

        for post in posts:
            serialized_post = {}
            for key, value in post.items():
                if key == '_id':
                    value = str(post['_id'])
                serialized_post[key] = value
            serialized_posts.append(serialized_post)

        return jsonify(serialized_posts), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 500
    

@post_module.route('/request_access/<string:post_id>', methods=['PUT'])
def request_access(post_id):
    # Get post data from the request JSON
    post_data = request.form.to_dict()
    
    # Ensure that at least one field to update is provided
    if not post_data:
        return jsonify({'error': 'No users requested access'}), 400

    # Update the post in the 'posts' collection based on the post_id
    try:
        target_id = ObjectId(post_id)
        post = mydb.posts.find_one({"_id": target_id})

        if 'requested' in post:
            requests = post_data['requested'] + ','
            if requests not in post['requested']:
                post_data['requested'] = post['requested'] + post_data['requested'] + ',' 
            else:
                post_data['requested'] = post['requested']
        else:
            post_data['requested'] = post_data['requested'] + ','        
        result = mydb.posts.update_one({'_id': target_id}, {'$set': post_data})

        if result.modified_count > 0:
            return jsonify({'message': 'Access requested from the author'}), 200
        else:
            return jsonify({'message': 'No matching post found for update'}), 404

    except Exception as e:
        return jsonify({'error': str(e)}), 400


@post_module.route('/grant_access/<string:post_id>', methods=['PUT'])
def grant_access(post_id):
    # Get post data from the request JSON
    post_data = request.form.to_dict()
    
    # Ensure that at least one field to update is provided
    if not post_data:
        return jsonify({'error': 'No users requested access'}), 400

    # Update the post in the 'posts' collection based on the post_id
    try:
        target_id = ObjectId(post_id)
        post = mydb.posts.find_one({"_id": target_id})

        grants = post_data['granted'] + ','
        if 'granted' in post:
            if grants not in post['granted']:
                post_data['granted'] = post['granted'] + post_data['granted'] + ',' 
            else:
                post_data['granted'] = post['granted']
        else:
            post_data['granted'] = post_data['granted'] + ','  

        if 'requested' in post:
            if grants in post['requested']:
                post_data['requested'] = post['requested'].replace(grants, "")

        result = mydb.posts.update_one({'_id': target_id}, {'$set': post_data})

        if result.modified_count > 0:
            return jsonify({'message': 'Access granted'}), 200
        else:
            return jsonify({'message': 'No matching post found for update'}), 404

    except Exception as e:
        return jsonify({'error': str(e)}), 400


@post_module.route('/get_granted_posts', methods=['GET'])
def get_granted_posts():
    try:
        filter_query = {}
        filters = []

        granted = request.args.get('user') 
        filters.append({"access": 'public'})
        filters.append({"author": granted})
        granted = granted + ','
        filters.append({"granted": {"$regex": granted, "$options": "i"}})

        # Use the $or operator to combine filter conditions
        if filters:
            filter_query["$or"] = filters

        # Fetch posts from the 'posts' collection based on the filter
        posts = list(mydb.posts.find(filter_query))

        # If there are no posts, return an empty list
        if not posts:
            return jsonify([])

        # Serialize the posts to JSON format
        serialized_posts = []

        for post in posts:
            serialized_post = {}
            for key, value in post.items():
                if key == '_id':
                    value = str(post['_id'])
                serialized_post[key] = value
            serialized_posts.append(serialized_post)

        return jsonify(serialized_posts), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 500
    

@post_module.route('/get_not_granted_posts', methods=['GET'])
def get_not_granted_posts():
    try:
        # Get the filter parameter from the URL query string
        user = request.args.get('user') + ','

        # Define filters to fetch posts based on the parameters
        filters = {}
        filters["access"] = 'private'
        filters["granted"] = {"$not": {"$regex": user, "$options": "i"}}

        # Fetch posts from the 'posts' collection based on the filter
        posts = list(mydb.posts.find(filters))

        # If there are no posts, return an empty list
        if not posts:
            return jsonify([])

        # Serialize the posts to JSON format
        serialized_posts = []

        for post in posts:
            serialized_post = {}
            for key, value in post.items():
                if key == '_id':
                    value = str(post['_id'])
                serialized_post[key] = value
            serialized_posts.append(serialized_post)

        return jsonify(serialized_posts), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 500
    
    
@post_module.route('/get_approvals', methods=['GET'])
def get_approvals():
    try:
        # Get the filter parameter from the URL query string
        user = request.args.get('user')

        # Define filters to fetch posts based on the parameters
        filters = {}
        filters["access"] = 'private'
        filters["author"] = user
        filters["requested"] = {"$exists": True, "$ne": ""}

        # Fetch posts from the 'posts' collection based on the filter
        posts = list(mydb.posts.find(filters))

        # If there are no posts, return an empty list
        if not posts:
            return jsonify([])

        # Serialize the posts to JSON format
        serialized_posts = []

        for post in posts:
            serialized_post = {}
            for key, value in post.items():
                if key == '_id':
                    value = str(post['_id'])
                serialized_post[key] = value
            serialized_posts.append(serialized_post)

        return jsonify(serialized_posts), 200

    except Exception as e:
        return jsonify({'error': str(e)}), 500