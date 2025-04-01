import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
import joblib


def prepare_data():
    df = pd.read_csv("model/nutrients_csvfile.csv")
    df = df.drop(columns=['Measure', 'Category'])
    macros = df.drop(columns=['Food'])
    labels = df.loc[:,['Food']]

    return (macros, labels)


def create_knn_model(macros, labels):
    knn = KNeighborsClassifier(n_neighbors=5)
    knn.fit(macros, labels)
    return knn


def export_model(knn):
    joblib.dump(knn, "model/nutrify.pkl")


if __name__ == '__main__':
    macros,labels = prepare_data()
    knn = create_knn_model(macros, labels)
    export_model(knn)

