import joblib
import numpy as np
import pandas as pd
from scipy import stats

from flask import Flask, request

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
    df = pd.DataFrame(predictions)
    majority_vote = df.mode().values[0]

    return str(majority_vote)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=False)
