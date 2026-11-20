import numpy as np

class ANN:
    def __init__(self, input_size, hidden_size=16, learning_rate=0.001, epochs=2000):
        self.input_size = input_size
        self.hidden_size = hidden_size
        self.learning_rate = learning_rate
        self.epochs = epochs

        self.W1 = np.random.randn(input_size, hidden_size) * 0.01
        self.b1 = np.zeros((1, hidden_size))
        self.W2 = np.random.randn(hidden_size, 2) * 0.01
        self.b2 = np.zeros((1, 2))

    def _relu(self, Z):
        return np.maximum(0, Z)

    def _relu_derivative(self, Z):
        return (Z > 0).astype(float)

    def _softmax(self, Z):
        expZ = np.exp(Z - np.max(Z, axis=1, keepdims=True))
        return expZ / expZ.sum(axis=1, keepdims=True)

    def fit(self, X_list, y_list):
        X = np.array(X_list)
        y = np.array(y_list)

        Y = np.zeros((y.size, 2))
        Y[np.arange(y.size), y] = 1

        m = X.shape[0]

        for epoch in range(self.epochs):

            Z1 = X.dot(self.W1) + self.b1
            A1 = self._relu(Z1)
            Z2 = A1.dot(self.W2) + self.b2
            A2 = self._softmax(Z2)


            dZ2 = A2 - Y
            dW2 = (A1.T.dot(dZ2)) / m
            db2 = np.sum(dZ2, axis=0, keepdims=True) / m

            dA1 = dZ2.dot(self.W2.T)
            dZ1 = dA1 * self._relu_derivative(Z1)
            dW1 = (X.T.dot(dZ1)) / m
            db1 = np.sum(dZ1, axis=0, keepdims=True) / m

            # updates
            self.W2 -= self.learning_rate * dW2
            self.b2 -= self.learning_rate * db2
            self.W1 -= self.learning_rate * dW1
            self.b1 -= self.learning_rate * db1

    def predict(self, X_list):
        X = np.array(X_list)
        Z1 = X.dot(self.W1) + self.b1
        A1 = self._relu(Z1)
        Z2 = A1.dot(self.W2) + self.b2
        A2 = self._softmax(Z2)

        return np.argmax(A2, axis=1)
