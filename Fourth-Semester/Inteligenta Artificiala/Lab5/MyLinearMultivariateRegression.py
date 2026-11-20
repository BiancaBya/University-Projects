class MyLinearMultivariateRegression:
    def __init__(self):
        self.weights = []  # w0, w1, ..., wn

    # W = (X^T * X)^(-1) * X^T * y

    def fit(self, X, y):

        X_aug = [[1.0] + row for row in X]
        XT = self.transpusa(X_aug)
        XT_X = self.inmultire(XT, X_aug)
        XT_y = self.inmultire(XT, [[val] for val in y])
        XT_X_inv = self.inversa(XT_X)
        W = self.inmultire(XT_X_inv, XT_y)
        self.weights = [w[0] for w in W]

    def transpusa(self, matrix):
        return [[row[i] for row in matrix] for i in range(len(matrix[0]))]

    def inmultire(self, A, B):
        result = []
        for i in range(len(A)):
            row = []
            for j in range(len(B[0])):
                val = sum(A[i][k] * B[k][j] for k in range(len(B)))
                row.append(val)
            result.append(row)
        return result

    def inversa(self, matrix):
        size = len(matrix)
        identity = [[float(i == j) for j in range(size)] for i in range(size)]
        for i in range(size):
            pivot = matrix[i][i]
            if pivot == 0:
                raise Exception("Matrix could not be inverted")
            for j in range(size):
                matrix[i][j] /= pivot
                identity[i][j] /= pivot
            for k in range(size):
                if k != i:
                    factor = matrix[k][i]
                    for j in range(size):
                        matrix[k][j] -= factor * matrix[i][j]
                        identity[k][j] -= factor * identity[i][j]
        return identity

