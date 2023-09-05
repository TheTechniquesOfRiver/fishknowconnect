from config.aws import kms_client
from decouple import config
from botocore.exceptions import ClientError


def encrypt(data):
        try:
            cipher_text = kms_client.encrypt(
                KeyId=config('AWS_KEY_ARN'), Plaintext=data.encode())['CiphertextBlob']
        except ClientError as err:
            print("Couldn't encrypt text. Here's why: %s",
                  err.response['Error']['Message'])
        else:
            print(f"Your ciphertext is: {cipher_text}")
            return cipher_text


def decrypt(data):
        try:
            plainText = kms_client.decrypt(
                KeyId=config('AWS_KEY_ARN'), CiphertextBlob=data)['Plaintext']
        except ClientError as err:
            print("Couldn't decrypt your ciphertext. Here's why: %s",
                    err.response['Error']['Message'])
        else:
            print(f"Your plian text is: {plainText}")
            return plainText