import joblib
import skl2onnx
from skl2onnx import convert
from skl2onnx.common.data_types import FloatTensorType

# Load your trained model
knn_model = joblib.load("nutrify.pkl")

# Prepare the data sample shape (you need to match the input shape that your model expects)
# Let's assume your features are of shape (n_samples, n_features), where n_features = 3 in this case
initial_type = [("float_input", FloatTensorType([None, macros.shape[1]]))]

# Convert the model to ONNX
onnx_model = convert.convert_sklearn(knn_model, initial_types=initial_type)

# Save the ONNX model to a file
onnx_file_path = "nutrify_knn_model.onnx"
with open(onnx_file_path, "wb") as f:
    f.write(onnx_model.SerializeToString())

print(f"Model saved to {onnx_file_path}")
