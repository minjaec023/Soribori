package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.kakao.sdk.newtoneapi.SpeechRecognizerActivity;

import java.util.ArrayList;
import java.util.List;

public class VoiceRecognizeActivity extends SpeechRecognizerActivity {


    public static String EXTRA_KEY_RESULT_ARRAY = "result_array"; // 결과값 목록. ArrayList<String>
    public static String EXTRA_KEY_MARKED = "marked"; // 첫번째 값의 신뢰도가 현저하게 높은 경우 true. 아니면 false. Boolean
    public static String EXTRA_KEY_ERROR_CODE = "error_code"; // 에러가 발생했을 때 코드값. 코드값은 SpeechRecognizerClient를 참조. Integer
    public static String EXTRA_KEY_ERROR_MESSAGE = "error_msg"; // 에러 메시지. String

    protected void putStringFromId(RES_STRINGS key, int id) {
        putString(key, getResources().getString(id));
    }

    // 이 함수를 꼭 상속받아서 구현해야 한다.
    // 내부에서 사용하는 리소스 id를 모두 등록해준다.
    // 일부 표시하지 않을 view의 경우에는 layout에서 제거하지 말고 visibility를 hidden으로 설정하는 것을 권장한다.
    @Override
    protected void onResourcesWillInitialize() {
        // string error handle MSG
        putStringFromId(RES_STRINGS.PLEASE_SPEAK, R.string.kakao_voice_search_please_speak);
        putStringFromId(RES_STRINGS.THIS_DEVICE_MAY_BE_NOT_SUPPORTED, R.string.kakao_voice_search_warn_not_support_device);
        putStringFromId(RES_STRINGS.SELECT_THAT_YOU_SPEAK_FROM_SUGGEST, R.string.kakao_voice_search_suggest_guide);
        putStringFromId(RES_STRINGS.RETRY_SPEAKING_WITH_TOUCH_BUTTON, R.string.kakao_voice_search_tip_retry_button);
        putStringFromId(RES_STRINGS.TOUCH_BUTTON_TO_USE_RESULT_INSTANTLY, R.string.kakao_voice_search_tip_instant_search);
        putStringFromId(RES_STRINGS.SORRY_RECOGNITION_FAILED, R.string.kakao_voice_search_recognition_failed);
        putStringFromId(RES_STRINGS.RETRY_AFTER_A_WHILE, R.string.kakao_voice_search_tip_retry_button);
        putStringFromId(RES_STRINGS.PLEASE_SPEAK_NATURAL, R.string.kakao_voice_search_tip_natural);
        putStringFromId(RES_STRINGS.NETWORK_IS_UNSTABLE, R.string.kakao_voice_search_error_network);

        // view id
        // 더 추가 필요함.
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognize);
    }

    @Override
    public void finish() {
        super.finish();

        // activity가 사라질 때 transition 효과 지정
        // overridePendingTransition(android.R.anim.fade_out, R.anim.com_kakao_sdk_asr_shrink_height_from_bottom);
    }

    @Override
    protected void onRecognitionSuccess(List<String> result, boolean marked) {
        // result는 선택된 결과 목록이 담겨있다.
        // 첫번째 값의 신뢰도가 낮아 후보 단어를 선택하는 과정을 거쳤을 경우에는 그 때 선택된 값이 가장 처음으로 오게 된다.
        // 첫번째 값의 신뢰도가 현저하게 높았거나, 이용자가 선택을 했을 경우에는 marked 값은 true가 된다. 이 이외에는 false가 된다.
        Intent intent = new Intent().
                putStringArrayListExtra(EXTRA_KEY_RESULT_ARRAY, new ArrayList<String>(result)).
                putExtra(EXTRA_KEY_MARKED, marked);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRecognitionFailed(int errorCode, String errorMsg) {
        Intent intent = new Intent().
                putExtra(EXTRA_KEY_ERROR_CODE, errorCode).
                putExtra(EXTRA_KEY_ERROR_MESSAGE, errorMsg);

        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
