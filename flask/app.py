import json

from flask import Flask, request, jsonify
import numpy as np
import joblib
from scipy import stats

app = Flask(__name__)

models_directory = 'models/'

# Load the models at startup
models = [joblib.load(f'{models_directory}naive_bayes_model_{i}.joblib') for i in range(10)]

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    data = data['data']

    # Reshape if just one sample
    features = np.array(data['features']).reshape(1, -1)

    # Collect predictions from each model
    predictions = np.array([model.predict(features) for model in models])

    # Apply majority voting
    majority_vote = stats.mode(predictions, axis=0)

    return str(majority_vote.mode[0])

if __name__ == '__main__':
    app.run(debug=True)
