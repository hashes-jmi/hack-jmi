from flask import jsonify, Flask, request, redirect
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
import pandas as pd
import numpy as np
import random

malefemaledicti={"M":0,"F":1}

dataset=pd.read_excel("./records.xlsx")
dataset['gender'] = dataset["gender"].apply(lambda x:malefemaledicti[x])


X=dataset.iloc[:,1:-1].values
y=dataset.iloc[:,-1].values


from sklearn.cross_validation import train_test_split
X_train,X_test,y_train,y_test=train_test_split(X,y,train_size=0.65)
clf=RandomForestClassifier()
clf.fit(X_train,y_train)

symptoms_list=['fever', 'headache', 'rash', 'nosebleed', 'backpain',
       'diziness', 'stomachache', 'laziness', 'cough', 'sneeze']


app = Flask(__name__)

dictionaryWithShortUrlAsKey = {}


# print(dictionaryWithShortUrlAsKey)


@app.route('/', methods=['GET', 'POST'])
def mainPage():
    return "This is REST API"


@app.route('/fetch/', methods=['GET', 'POST'])
def returnAnswer():
    content = request.get_json(silent=True)
    print(content)
    if content == None:
        returnResponse = {"status": "FAILED", "status_codes": ["INVALID_URLS"]}
        return str(jsonify(returnResponse))

    else:
        content=dict(content)
        myList=content["symptoms"]
        ans=[random.choice([0,1])]
        for i in symptoms_list:
            if i in myList:
                ans.append(1)
            else:
                ans.append(0)


        return str(clf.predict([myList])[0])

app.run(host="0.0.0.0", debug=True, port=2345)
# "http://0.0.0.0:9233/3d9g9b6f/"
