import torch
import torch.nn as nn
import torch.optim as optim

class CNN:
    def __init__(self, image_size, in_channels, num_classes, learning_rate, epochs, batch_size=32
    ):
        self.image_size = image_size
        self.in_channels = in_channels
        self.num_classes = num_classes
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.batch_size = batch_size
        self.device = ("cuda" if torch.cuda.is_available() else "cpu")
        H, W = image_size

        self.model = nn.Sequential(
            nn.Conv2d(in_channels, 16, kernel_size=3, padding=1),
            nn.ReLU(),
            nn.MaxPool2d(2, 2),

            nn.Conv2d(16, 32, kernel_size=3, padding=1),
            nn.ReLU(),
            nn.MaxPool2d(2, 2),

            nn.Flatten(),
            nn.Linear(32*(H//4)*(W//4), 64),
            nn.ReLU(),
            nn.Linear(64, num_classes)
        ).to(self.device)

        self.criterion = nn.CrossEntropyLoss()
        self.optimizer = optim.SGD(
            self.model.parameters(),
            lr=learning_rate,
            momentum=0.9
        )
        self.epochs = epochs
        self.batch_size = batch_size
        self.image_size = image_size

    def fit(self, X_list, y_list):
        X = torch.tensor(X_list, dtype=torch.float32, device=self.device)
        N = X.shape[0]
        C = self.model[0].in_channels
        H, W = self.image_size
        X = X.view(N, C, H, W)
        y = torch.tensor(y_list, dtype=torch.long, device=self.device)

        self.model.train()
        for epoch in range(1, self.epochs + 1):
            self.optimizer.zero_grad()
            outputs = self.model(X)
            loss = self.criterion(outputs, y)
            loss.backward()
            self.optimizer.step()

            if epoch == 1 or epoch % 50 == 0:
                print(f"Epoch {epoch}/{self.epochs}  loss={loss.item():.4f}")

    def predict(self, X_list):
        X = torch.tensor(X_list, dtype=torch.float32, device=self.device)
        N = X.shape[0]
        C = self.model[0].in_channels
        H, W = self.image_size
        X = X.view(N, C, H, W)

        self.model.eval()
        with torch.no_grad():
            logits = self.model(X)
            preds = logits.argmax(dim=1)
        return preds.cpu().numpy()

    def evaluate(self, X_list, y_list):
        X = torch.tensor(X_list, dtype=torch.float32, device=self.device)
        N = X.shape[0]
        C = self.model[0].in_channels
        H, W = self.image_size
        X = X.view(N, C, H, W)
        y = torch.tensor(y_list, dtype=torch.long, device=self.device)

        self.model.eval()
        with torch.no_grad():
            logits = self.model(X)
            loss = self.criterion(logits, y).item()
            acc = (logits.argmax(dim=1) == y).float().mean().item()
        return loss, acc
