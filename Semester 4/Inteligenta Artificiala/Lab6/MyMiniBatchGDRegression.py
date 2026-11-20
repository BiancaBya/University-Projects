import random

class MyMiniBatchGDRegression:
    # X = lista de liste cu inputuri
    # learning_rate = cat de repede sare moelul
    # no_epochs = de cate ori se parcurge setul de date
    # batch_size = cate inputuri procesam o singura data
    # self.coef_ – lista coeficientilor
    # self.intercept_ – bias-ul (w0)

    def __init__(self, learning_rate=0.01, no_epochs=1000, batch_size=16):
        self.learning_rate = learning_rate
        self.no_epochs = no_epochs
        self.batch_size = batch_size
        self.coef_ = []
        self.intercept_ = 0.0

    def fit(self, X, y):

        n = len(X)
        self.coef_ = [0.0 for _ in range(len(X[0]))]
        self.intercept_ = 0.0

        for epoch in range(self.no_epochs):

            indices = list(range(n))
            random.shuffle(indices)
            X_shuffled = [X[i] for i in indices]
            y_shuffled = [y[i] for i in indices]

            for start in range(0, n, self.batch_size):
                end = min(start + self.batch_size, n)
                X_batch = X_shuffled[start:end]
                y_batch = y_shuffled[start:end]

                batch_n = len(X_batch)
                y_hat = [self._predict_row(x) for x in X_batch]
                errors = [y_batch[i] - y_hat[i] for i in range(batch_n)]

                self.intercept_ += self.learning_rate * sum(errors) / batch_n

                for j in range(len(self.coef_)):
                    gradient = sum(errors[i] * X_batch[i][j] for i in range(batch_n)) / batch_n
                    self.coef_[j] += self.learning_rate * gradient

    def _predict_row(self, x_row):
        return self.intercept_ + sum(c * f for c, f in zip(self.coef_, x_row))

    def predict(self, X):
        return [self._predict_row(x_row) for x_row in X]


