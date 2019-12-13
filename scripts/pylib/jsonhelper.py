from json import dump, load

def save_json(file, obj):
    with open(file, 'w+') as instance:
        dump(obj, instance)

def read_json(file):
    with open(file, 'r') as instance:
        return load(instance)
