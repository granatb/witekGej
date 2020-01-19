from flask import Flask, request, jsonify, render_template
from imageai.Detection import ObjectDetection
from sklearn.externals import joblib
from sklearn.ensemble import RandomForestRegressor
import math, json

app = Flask(__name__)
app.config["DEBUG"] = True


class Point():
  def __init__(self,x, y, path):
    self.x = x
    self.y = y
    self.path = path
  

  @property
  def serialize(self):
     return {
        'xsCoord': int(self.x),
        'ysCoord': int(self.y),
         'pathString': self.path
     }

def makeANewPoint(x,y, path):
   addedpoint = Point(x=x, y=y, path = path)
   return addedpoint.serialize




@app.route('/', methods=['GET'])
def home():
    return render_template('index.html')


@app.route('/upload', methods=['POST'])
def predict_path():
    if request.method == 'POST':
        
        path = request.args.get('path')
        import os
        cwd = os.getcwd()
        print(cwd)
        detector = ObjectDetection()

        model_path = "./models/yolo-tiny.h5"
        input_path = path
        output_path = "C:/Users/bgranat/Desktop/final/rest-api/target/classes/static/" + path.rsplit('/', 1)[-1] + "newimage.jpg"
        
        detector.setModelTypeAsTinyYOLOv3()
        detector.setModelPath(model_path)
        detector.loadModel()
        detection = detector.detectObjectsFromImage(input_image=input_path, output_image_path=output_path)
        jsondata = {}

        rf1 = joblib.load("./models/x.pkl")
        rf2 = joblib.load("./models/y.pkl")
        i = 0

        for eachItem in detection:
            if(eachItem["name"] == "person"):
                x1 = eachItem["box_points"][0]
                y1 = eachItem["box_points"][1]
                x2 = eachItem["box_points"][2]
                y2 = eachItem["box_points"][3]

                x = rf1.predict([[x1,x2]])
                y = rf2.predict([[y1,y2]])
                print(x,y)
                point = makeANewPoint(str(math.floor(x)), str(math.floor(y)), input_path)
                jsondata[i] = point
                i = i+1


        return jsonify(jsondata)

app.run()