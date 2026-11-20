import numpy as np

class MyMiniBatchLogisticRegression:
    def __init__(self, learning_rate=0.1, epochs=1000, batch_size=16):
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.batch_size = batch_size

    def sigmoid(self, z):
        return 1 / (1 + np.exp(-z))

    def fit(self, X, y):
        m, n = X.shape
        self.weights = np.zeros(n)
        self.bias = 0

        for epoch in range(self.epochs):
            indices = np.arange(m)
            np.random.shuffle(indices)
            X_shuffled = X[indices]
            y_shuffled = y[indices]

            for start in range(0, m, self.batch_size):
                end = min(start + self.batch_size, m)
                X_batch = X_shuffled[start:end]
                y_batch = y_shuffled[start:end]

                model = np.dot(X_batch, self.weights) + self.bias
                predictions = self.sigmoid(model)

                errors = predictions - y_batch
                dw = np.dot(X_batch.T, errors) / len(y_batch)
                db = np.sum(errors) / len(y_batch)

                self.weights -= self.learning_rate * dw
                self.bias -= self.learning_rate * db

    def predict_proba(self, X):
        return self.sigmoid(np.dot(X, self.weights) + self.bias)

    def predict(self, X):
        return (self.predict_proba(X) >= 0.5).astype(int)

