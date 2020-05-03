package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.lang.Thread.sleep;

public class dv_recording extends AppCompatActivity {
    boolean isPlaying = false;
    MediaRecorder recorder;
    String filepath;
    MediaPlayer player;
    int position = 0; // 다시 시작 기능을 위한 현재 재생 위치 확인 변수
    String filepath01;
    private Thread recordingThread;
    private AudioRecord mRecorder;
    private boolean isRecording = false;
    private String mFilepathname = Environment.getDataDirectory().getAbsolutePath();
    private String mFileName = Environment.getDataDirectory().getAbsolutePath() + "/recording_test.pcm";
    private short[] sData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dv_recording);

        /////////////녹음 진행/////////////
        Log.i("dv_recording", "저장된 녹음 파일 위치" + Environment.getDataDirectory().getAbsolutePath());
        //녹음 시작 버튼//
        findViewById(R.id.start_recording).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
                File f1 = new File(mFileName); // The location of your PCM file
                File f2 = new File(mFilepathname+"/recording_test.wav"); // The location where you want your WAV file
                try {
                    rawToWave(f1, f2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //녹음 중지 버튼//
        findViewById(R.id.stop_recording).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });


        //녹음된 소리 재생 버튼//
        findViewById(R.id.playaudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        //녹음된 소리 재생 중지 버튼//
        findViewById(R.id.stopaudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //녹음 시작 자동 버튼//
        findViewById(R.id.auto_recording_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //녹음 자동 중지 버튼//
        findViewById(R.id.auto_recording_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
            }
        });

    }

    private void startRecording() {
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                Constants.RECORDER_SAMPLERATE, Constants.RECORDER_CHANNELS,
                Constants.RECORDER_AUDIO_ENCODING, Constants.BufferElements2Rec * Constants.BytesPerElement);

        mRecorder.startRecording();
        isRecording = true;

        Log.i("dv_recording","go sleep");
        try {
            sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i("dv_recording","out sleep");
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;


        Log.i("dv_recording","change start");
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }


    private void writeAudioDataToFile() {
        // Write the output audio in byte

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(mFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format
            mRecorder.read(sData, 0, Constants.BufferElements2Rec);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer

                byte bData[] = short2byte(sData);

                os.write(bData, 0, Constants.BufferElements2Rec * Constants.BytesPerElement);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startPlaying() {
        new Thread(new Runnable() {
            private AudioTrack mPlayer;
            public void run() {
                try {
                    File file = new File(mFileName);
                    byte[] audioData = null;
                    InputStream inputStream = new FileInputStream(mFileName);
                    audioData = new byte[Constants.BufferElements2Rec];

                    mPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, Constants.RECORDER_SAMPLERATE,
                            AudioFormat.CHANNEL_OUT_MONO, Constants.RECORDER_AUDIO_ENCODING,
                            Constants.BufferElements2Rec * Constants.BytesPerElement, AudioTrack.MODE_STREAM);


                    final float duration = (float) file.length() / Constants.RECORDER_SAMPLERATE / 2;

                    Log.i("dv_recording", "PLAYBACK AUDIO");
                    Log.i("dv_recording", String.valueOf(duration));


                    mPlayer.setPositionNotificationPeriod(Constants.RECORDER_SAMPLERATE / 10);
                    mPlayer.setNotificationMarkerPosition(Math.round(duration * Constants.RECORDER_SAMPLERATE));

                    mPlayer.play();

                    int i = 0;
                    while ((i = inputStream.read(audioData)) != -1) {
                        try {
                            mPlayer.write(audioData, 0, i);
                        } catch (Exception e) {
                            Log.e("dv_recording", "Exception: " + e.getLocalizedMessage());
                        }
                    }

                } catch (FileNotFoundException fe) {
                    Log.e("dv_recording", "File not found: " + fe.getLocalizedMessage());
                } catch (IOException io) {
                    Log.e("dv_recording", "IO Exception: " + io.getLocalizedMessage());
                }

            }

        }).start();
    }

    public class Constants {

        final static public int RECORDER_SAMPLERATE = 44100;
        final static public int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
        final static public int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

        final static public int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
        final static public int BytesPerElement = 2; // 2 bytes in 16bit format
    }

    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 44100); // sample rate
            writeInt(output, 44100 * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

    // short array형태의 data를 byte array형태로 변환하여 반환하는 함수
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }

    private void stopRecording() {
        if (mRecorder != null) {
            isRecording = false;
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }
}
