import librosa
from scipy.io import wavfile as wavfile
import numpy as np
import matplotlib.pyplot as pylt
import librosa.display
import pandas as pd
import os
from sklearn.preprocessing import LabelEncoder
from keras.utils import to_categorical
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Flatten
from keras.layers import Convolution2D, MaxPooling2D
from keras.optimizers import Adam
from keras.utils import np_utils
from sklearn import metrics
from sklearn.model_selection import KFold
from sklearn.model_selection import cross_val_score
from sklearn.linear_model import LogisticRegression
from keras.callbacks import ModelCheckpoint
from keras.models import load_model
from datetime import datetime 
import pickle
import time
import glob
import csv

class Task:
    def __init__(self):
        self.features = []
        self.fulldatasetpath = 'sound/'
    
    def user_label(self):
        features = []
        metadata = pd.read_csv('data/metadata.csv')
        length = metadata.shape[0]-1
        class_label = metadata.iloc[length]['classID']
        for i in range(16):
            file_name = 'mixing_test/output_mixed/' + str(i) + '.wav'
            data = self.extract_features(file_name)
            features.append([data, class_label])
        
        new_featuresdf = pd.DataFrame(features, columns = ['feature', 'class_label'])

        with open('sample_feature.pickle', 'rb') as f:
            featuresdf = pickle.load(f)

        featuresdf = featuresdf.append(new_featuresdf)
        
        with open('sample_feature.pickle', 'wb') as f:
            pickle.dump(featuresdf, f)

    def label(self):
        self.features = []
        metadata = pd.read_csv('data/metadata.csv')
        sum = 0
        # Iterate through each sound file and extract the features
        for index, row in metadata.iterrows():
            file_name = os.path.join(os.path.abspath(self.fulldatasetpath),'fold'+str(row["fold"])+'/',str(row["slice_file_name"]))      
            class_label = row["classID"]
            data = self.extract_features(file_name)
            self.features.append([data, class_label])

        featuresdf = pd.DataFrame(self.features, columns=['feature','class_label'])
        with open('sample_feature.pickle', 'wb') as f:
            pickle.dump(featuresdf, f)

    def extract_features(self, file_name):
        # try:
        audio, sample_rate = librosa.load(file_name, res_type='kaiser_fast')
        mfccs = librosa.feature.mfcc(y=audio, sr=sample_rate, n_mfcc=40)
        #print(mfccs.shape)
        mfccsscaled = np.mean(mfccs.T,axis=0)

        return mfccsscaled

    def training_execute(self):
        # Convert into a Panda dataframe 
        featuresdf = pd.DataFrame(self.features, columns=['feature','class_label'])
        le = LabelEncoder()
        # Convert features and corresponding classification labels into numpy arrays
        X = np.array(featuresdf.feature.tolist())
        y = np.array(featuresdf.class_label.tolist())
        # Encode the classification labels
        
        yy = to_categorical(le.fit_transform(y))

        x_train, x_test, y_train, y_test = train_test_split(X, yy, test_size=0.2, random_state = 42)
        
        num_labels = yy.shape[1]
        filter_size = 2

        # Construct model 
        model = Sequential()

        model.add(Dense(256, input_shape=(40, )))
        model.add(Activation('relu'))
        model.add(Dropout(0.5))

        model.add(Dense(256))
        model.add(Activation('relu'))
        model.add(Dropout(0.5))

        model.add(Dense(num_labels))
        model.add(Activation('softmax'))

        model.compile(loss='categorical_crossentropy', metrics=['accuracy'], optimizer='adam')

        # Display model architecture summary 
        model.summary()

        # Calculate pre-training accuracy 
        score = model.evaluate(x_test, y_test, verbose=0)

        accuracy = 100*score[1]

        num_epochs = 100
        num_batch_size = 32

        checkpointer = ModelCheckpoint(filepath='weights.best.basic_mlp.hdf5', 
                                    verbose=1, save_best_only=True)
        start = datetime.now()

        model.fit(x_train, y_train, batch_size=num_batch_size, epochs=num_epochs, validation_data=(x_test, y_test), callbacks=[checkpointer], verbose=1)


        duration = datetime.now() - start

        # Evaluating the model on the training and testing set
        score = model.evaluate(x_train, y_train, verbose=0)
        print("Training Accuracy: ", score[1])

        score = model.evaluate(x_test, y_test, verbose=0)
        print("Testing Accuracy: ", score[1])

    def print_prediction(self, file_name):
        le = LabelEncoder()
        # Convert into a Panda dataframe 
        #featuresdf = pd.DataFrame(self.features, columns=['feature','class_label'])
        with open('sample_feature.pickle', 'rb') as f:
            featuresdf = pickle.load(f)
        # Convert features and corresponding classification labels into numpy arrays
        y = np.array(featuresdf.class_label.tolist())

        # Encode the classification labels
        
        le.fit_transform(y)

        model = load_model('weights.best.basic_mlp.hdf5')
        a = time.time()
        prediction_feature = np.array([self.extract_features(file_name)]) 
        b = time.time()
        predicted_vector = model.predict_classes(prediction_feature)
        predicted_class = le.inverse_transform(predicted_vector) 
        print("The predicted class is:", predicted_class[0], '\n') 
        print(b-a)
        predicted_proba_vector = model.predict_proba(prediction_feature) 
        predicted_proba = predicted_proba_vector[0]
        for i in range(len(predicted_proba)): 
            category = le.inverse_transform(np.array([i]))
            print(category[0], "\t\t : ", format(predicted_proba[i], '.32f') )

        return predicted_class[0]
    
    def make_csv(self):
        # [fold1, fold2, ... , custom1, custom2, ...] 폴더들이 포함된 폴더 경로 입력
        path = "./sound/"
        file_list = os.listdir(path)
        custom_fold_list = []

        for i in range(len(file_list)):
            if('custom' in file_list[i]):
                custom_fold_list.append(file_list[i])

        # 메타데이터 경로 입력
        metadata_path = './data/metadata.csv'
        f = open(metadata_path,'a',newline='')
        wr = csv.writer(f)
        custom_fold = custom_fold_list[-1]
        custom_file_list = glob.glob('./sound/' +custom_fold+ '/*.wav')
        
        print(len(custom_file_list))
        print(custom_fold)
        for i in range(len(custom_file_list)):
            slice_file_name = custom_file_list[i]
            #slice_file_name = slice_file_name.replace(custom_fold,'')
            #slice_file_name = slice_file_name.replace("\\",'')
            slice_file_name = slice_file_name.replace("./sound/", '')
            slice_file_name = slice_file_name.replace("/", '(')
            slice_file_name = slice_file_name.replace(".", ').')
            fold = len(custom_fold_list)+9
            class_ = custom_fold_list[-1]
            wr.writerow([slice_file_name,fold,fold,class_])
        f.close()
            


