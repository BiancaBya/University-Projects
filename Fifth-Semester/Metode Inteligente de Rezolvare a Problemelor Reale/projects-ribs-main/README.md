# RIBS - Tumor Biomarker Prediction from Lung CT (TTF1 & CK7) - Multimodal Deep Learning + Radiomics

## Who We Are

We are a team of three computer science students working on a medical AI project that combines **deep learning on CT scans** with **radiomics** to predict tumor biomarkers.

- _Pulpă Bianca Gabriela_
- _Pușcaș Alexandra Cristina_
- _Reebeleș Bogdan Calin Florin_

## Problem Statement

In clinical practice, biomarkers like **TTF1** and **CK7** are usually determined from pathology/immunohistochemistry. Our goal is to explore whether we can **predict these biomarkers directly from lung CT scans**, using AI, as a non-invasive supporting signal.

**Task:** Multi-label (two binary targets) prediction for 2 different biomarkers:

- **TTF1**
- **CK7**

---

## Our Approach

We implemented and compared multiple approaches, focusing on two solutions:

1. **3D CT Model + Radiomics (Multimodal, two heads)**  
   Uses a lightweight 3D CNN to learn volumetric features from CT, fused with radiomics features extracted from the same CT.

2. **2D Pretrained Model (slice-based) + Radiomics (Multimodal, two heads)**  
   Uses a pretrained 2D backbone (EfficientNet-style) on informative CT slices, plus radiomics, then aggregates slice predictions into a patient-level decision.

The key idea is: **CT voxels are high-dimensional and our dataset is small**, so radiomics helps by injecting stable, engineered descriptors (texture, intensity, shape).

---

## Dataset & Split Strategy

We worked with **87 patients**.

### Stratified split

We stratified using a combined key:  
`stratify_key = TTF1 + "_" + CK7`

**Total distribution (N=87):**

- (TTF1=0, CK7=0): 14
- (TTF1=0, CK7=1): 12
- (TTF1=1, CK7=0): 31
- (TTF1=1, CK7=1): 30

**Train / Val / Test (10% / 10%):**

- **Train (N=69)**
  - 0/0: 11
  - 0/1: 9
  - 1/0: 25
  - 1/1: 24
- **Validation (N=9)**
  - 0/0: 1
  - 0/1: 2
  - 1/0: 3
  - 1/1: 3
- **Test (N=9)**
  - 0/0: 2
  - 0/1: 1
  - 1/0: 3
  - 1/1: 3

---

## Lung Segmentation (Mask Generation)

To focus radiomics on the relevant anatomical region, we generated **lung masks**.

### CT lung masks (using `lungmask`)

We used:

- `nibabel`, `SimpleITK`, `lungmask`

Workflow:

1. Read CT volume
2. Apply
3. Convert to binary mask
4. Save as `.nrrd` with the same spatial metadata

## Radiomics Feature Extraction (PyRadiomics)

We extracted radiomics using:

- `SimpleITK`
- `pyradiomics`

### Steps

1. For each patient:
   - Identify CT path
   - Identify corresponding lung mask path
2. Run the extractor with the imagePath and the maskPath
3. Save outputs to a per-patient TXT

Radiomics features represent:

- **first-order intensity statistics**
- **texture (GLCM, GLRLM, GLSZM, etc.)**
- **shape descriptors**

---

## Preprocessing (CT + Radiomics)

### CT volumes (3D path)

- Load NIfTI
- Convert to float32, replace NaNs
- Ensure channel dimension exists
- Resize/crop to a fixed target 3D shape
- Build Datasets for training/validation/test

### CT slices (2D path)

For the 2D approach we:

- Loaded 3D CT volume
- Iterated through axial slices
- Resized each slice to `(128, 128)`
- Filtered informative slices
- Created a slice-based dataset, but evaluated **per patient** by aggregating slice predictions (mean probability)

### Radiomics

- Loaded radiomics features from TXT
- Used a saved scaler
- Standardized the radiomics vector before training/inference

---

## Model 1: 3D CT + Radiomics (Multimodal Two-Head Network)

### Architecture summary

**CT branch (3D CNN)**

- Conv3D + BN + ReLU blocks
- MaxPool3D
- GlobalAveragePooling3D
- Dense + Dropout

**Radiomics branch (MLP)**

- Dense(128) + Dropout
- Dense(64)

**Fusion**

- Concatenate CT + radiomics embeddings
- Dense(64) + Dropout

**Two heads**

- `TTF1`: Dense(1, sigmoid)
- `CK7`: Dense(1, sigmoid)

### Training setup

- Loss: binary cross-entropy for both heads
- Optimizer: Adam
- Callbacks:
  - EarlyStopping (restore best weights)
  - ReduceLROnPlateau
  - ModelCheckpoint (best `val_loss`)

### Test results (threshold = 0.5)

**TTF1**

- Accuracy: **0.6667**
- Precision: **0.8000**
- Recall: **0.6667**
- F1-score: **0.7273**
- Confusion matrix `[[TN FP],[FN TP]]`: `[[2,1],[2,4]]`

**CK7**

- Accuracy: **0.8889**
- Precision: **1.0000**
- Recall: **0.7500**
- F1-score: **0.8571**
- Confusion matrix: `[[5,0],[1,3]]`

---

## Model 2 (Final): 2D Pretrained Backbone + Radiomics (Slice → Patient Aggregation)

### Architecture summary

- Train on **informative 2D slices**
- Use a **pretrained 2D backbone**
- Fuse with radiomics
- Aggregate slice predictions into a **patient-level** probability

**TTF1 (threshold = 0.65)**

- Accuracy: **0.7778**
- F1-score: **0.8333**
- Recall: **0.8333**
- Precision: **0.8333**
- Confusion matrix: `[[2,1],[1,5]]`

**CK7 (threshold = 0.70)**

- Accuracy: **0.7778**
- F1-score: **0.6667**
- Recall: **0.5000**
- Precision: **1.0000**
- Confusion matrix: `[[5,0],[2,2]]`

This model is valuable because:

- pretrained features help with small datasets
- slice filtering reduces noise
- threshold tuning allows us to trade recall vs precision depending on clinical preference

---

## How We Trained & Evaluated

1. **Prepare dataset index**
   - Match CT paths, masks, radiomics, labels
   - Keep only valid patients with all required files
2. **Split dataset**
   - Stratified by combined biomarker label
3. **Build tf.data pipelines**
   - 3D: patient-level volumes
   - 2D: slice-level training, patient-level evaluation via aggregation
4. **Train**
   - Adam + BCE
   - Early stopping + LR scheduling
5. **Evaluate**
   - Standard metrics: Accuracy, Precision, Recall, F1
   - Confusion matrix for both targets
   - Threshold sweeps and fixed-threshold reporting

---

## Key Takeaways

- **Radiomics improves stability** when pure 3D CT learning is difficult due to limited sample size.
- **Two-head multitask learning** allows shared representation while predicting two biomarkers.
- **Pretrained 2D backbones** plus aggregation can be very effective in small medical datasets.
- Threshold selection matters a lot, especially with **only 9 patients in the test set**.

---

## Tech Stack

- Python
- TensorFlow / Keras
- NumPy, Pandas
- nibabel, SimpleITK
- lungmask
- PyRadiomics
- scikit-learn (metrics, scaling)
- matplotlib / seaborn (plots)

[RIBS Presentation](https://1drv.ms/p/c/497BEC0C7E0F7605/AdIA2_xsHf5In5tzjIeHCEU?e=DLOSC4)  
[RIBS Demo](https://youtu.be/dfxWUSrHh2M)  
[Radiomics IHC Biomarker System Teaser](https://youtu.be/Oj1WRNHv3zQ)
