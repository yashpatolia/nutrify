import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
import joblib
import skl2onnx
from skl2onnx import convert
from skl2onnx.common.data_types import FloatTensorType

def prepare_data():
    df = pd.read_csv("/Users/justinkwinecki/Documents/School/Year3/Semester2/3A04/Project/3A04-D3/model/nutrients_csvfile.csv")
    df = df.drop(columns=['Measure', 'Category'])
    macros = df.drop(columns=['Food'])
    labels = df.loc[:,['Food']]

    return (macros, labels)


def create_knn_model(macros, labels):
    knn = KNeighborsClassifier(n_neighbors=5)
    knn.fit(macros, labels)
    return knn


def export_model(knn):
    joblib.dump(knn, "nutrify.pkl")


def convert_to_onnx(knn, macros):

    initial_type = [("float_input", FloatTensorType([None, macros.shape[1]]))]
    onnx_model = skl2onnx.convert.convert_sklearn(knn, initial_types=initial_type)
    
    onnx_model_file = "/Users/justinkwinecki/Documents/School/Year3/Semester2/3A04/Project/3A04-D3/model/nutrify_knn_model.onnx"
    with open(onnx_model_file, "wb") as f:
        f.write(onnx_model.SerializeToString())
    
    print(f"Model successfully converted to ONNX and saved as {onnx_model_file}")

if __name__ == '__main__':
    macros,labels = prepare_data()
    knn = create_knn_model(macros, labels)
    export_model(knn)
    convert_to_onnx(knn, macros)

