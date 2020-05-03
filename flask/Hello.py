from flask import Flask, request
from werkzeug.utils import secure_filename
import task

app = Flask(__name__)

@app.route('/', methods = ['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        
        f = request.files['file']
        fname = 'should_be_predicted/'+secure_filename(f.filename)
        f.save(fname)
        worker.print_prediction(fname)
        print("POST OK")
        return 'upload success'
    return 'page'

if __name__ == '__main__':
    worker = task.Task()
    #worker.label()
    app.run(host='0.0.0.0', debug = True)
    