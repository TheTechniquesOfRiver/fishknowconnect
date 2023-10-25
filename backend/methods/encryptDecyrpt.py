from decouple import config
from botocore.exceptions import ClientError
from cryptography.fernet import Fernet
import base64, hashlib

#generate key
def gen_fernet_key(passcode:bytes) -> bytes:
    assert isinstance(passcode, bytes)
    hlib = hashlib.md5()
    hlib.update(passcode)
    return base64.urlsafe_b64encode(hlib.hexdigest().encode('latin-1'))

#data encrypt function
def encrypt(data):
        try:
            passcode = config('ecrypt')
            key = gen_fernet_key(passcode.encode('utf-8'))
            fernet = Fernet(key)
            cipher_text = fernet.encrypt(data.encode('utf-8'))
        except ClientError as err:
            print("Couldn't encrypt text. Here's why: %s",
                  err.response['Error']['Message'])
        else:
            return cipher_text

#data dencrypt function
def decrypt(data):
        try:
            passcode = config('ecrypt')
            key = gen_fernet_key(passcode.encode('utf-8'))
            fernet = Fernet(key)
            plainText = fernet.decrypt(data).decode('utf-8')
        except ClientError as err:
            print("Couldn't decrypt your ciphertext. Here's why: %s",
                    err.response['Error']['Message'])
        else:
            return plainText
            
            
            