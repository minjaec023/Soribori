from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
import subprocess
# task.py의 함수들을 사용하기 위해 import함
import task

app = Flask(__name__)

# 맨 처음 서버 켰을 때 루트로 라우팅(그냥 형식적인 것)
@app.route('/', methods = ['GET', 'POST'])

# 루트로 라우팅되고 upload_file함수 실행(서버 키자마자 실행되는 것과 같음)
def upload_file():

    # 안드로이드에서 POST요청이 오면 실행
    if request.method == 'POST':

        # post_type이 녹음 파일을 prediction하기 위한 것이라면 다음 실행
        if request.form.get('post_type')=='prediction':

            # 전송 온 데이터를 필드네임 'file'로 파싱해서 f라는 변수에 할당
            f = request.files['file']

            # 전송 온 파일의 경로를 지정해줌(secure_filename은 업로드된 파일의 이름이 안전한지 확인해줌) 
            fname = 'should_be_predicted/'+secure_filename(f.filename)

            # 위에서 지정한 경로로 파일을 저장
            f.save(fname)

            # 파일을 예측 함수에 넣어서 결과를 result에 저장
            # 여기서 결과는 keras에서 사용하는 조금 다른 형태의 int기 때문에 json으로 바로 보내면 오류가 생김 -> int로 형변환
            result = int(worker.print_prediction(fname))

            # jsonify함수를 통해 결과를 json형식으로 안드로이드에 날림
            return jsonify(result=result)

        # post_type이 사용자 지정 소리를 받아서 업데이트하기 위한 것이라면 다음 실행
        elif request.form.get('post_type')=='user':
            subprocess.run(['./test.sh'], shell=True)
            worker.make_csv()
            worker.user_label()
            print('user_OK')
            return 'upload success'

    return 'page'

# 서버 실행하면 가장 먼저 실행되는 부분
if __name__ == '__main__':

    # task라는 클래스의 객체인 worker 생성!
    # 이 worker로 모듈화한 함수를 불러서 씀
    worker = task.Task()

    # label이라는 함수는 feature 저장하는 함순데 지금은 피클 파일에 저장되어 있으므로 주석처리!
    # worker.label()

    # 0.0.0.0이라는 주소로 서버 실행
    # 디버그 모드를 활성화했으므로 서버에서 코드 수정이 일어나면 자동으로 재실행됨.
    # 근데 디버그 모드를 활성하면 초기에 서버 실행이 두 번 된다. 이건 어쩔수 없는 듯.
    app.run(host='0.0.0.0', debug = True)
    
    