import tkinter as tk
from tkinter import filedialog, messagebox, scrolledtext, ttk
import threading
import sys
import os
import numpy as np
import nibabel as nib
from PIL import Image, ImageTk 

APP_TITLE = "Lung Cancer AI & CT Viewer"
MODEL_FILENAME = "model_2d_efficientnet_v3_FIXED_LR.keras"
SCALER_FILENAME = "radiomics_scaler.joblib"
THEME_FONT = "Segoe UI" if os.name == 'nt' else "Helvetica"

try:
    from test import LungCancerPipeline
except ImportError:
    messagebox.showerror("Critical Error", "Could not find 'lung_pipeline.py'.")
    sys.exit(1)

class StdoutRedirector:
    def __init__(self, widget, tag="stdout"):
        self.widget = widget
        self.tag = tag
    def write(self, string):
        try:
            self.widget.configure(state="normal")
            self.widget.insert("end", string, (self.tag,))
            self.widget.see("end")
            self.widget.configure(state="disabled")
            self.widget.update_idletasks()
        except: pass
    def flush(self): pass

class CTViewer(tk.Frame):
    def __init__(self, parent, width=400, height=400):
        super().__init__(parent, bg="black", bd=2, relief="sunken")
        self.width = width
        self.height = height
        self.volume = None 
        self.current_slice = 0
        
        self.canvas = tk.Canvas(self, width=width, height=height, bg="black", highlightthickness=0)
        self.canvas.pack(fill="both", expand=True)
        
        self.slider = ttk.Scale(self, from_=0, to=100, orient="horizontal", command=self.on_slider_change)
        self.slider.pack(fill="x", padx=10, pady=5)
        self.slider.state(["disabled"]) 
        
        self.lbl_info = tk.Label(self, text="No Scan Loaded", bg="black", fg="white", font=("Consolas", 10))
        self.lbl_info.pack(pady=(0, 5))

        self.img_ref = None 

    def load_nifti(self, nifti_path):
        try:
            img = nib.load(nifti_path)
            vol = img.get_fdata()
            
            min_hu, max_hu = -1000, 400
            vol = np.clip(vol, min_hu, max_hu)
            vol = (vol - min_hu) / (max_hu - min_hu) * 255.0
            self.volume = vol.astype(np.uint8)
            
            self.max_slices = self.volume.shape[2] - 1
            self.slider.config(to=self.max_slices, value=self.max_slices // 2)
            self.slider.state(["!disabled"])
            
            self.show_slice(self.max_slices // 2)
        except Exception as e:
            print(f"[VIEWER ERROR] Could not load scan: {e}")

    def on_slider_change(self, val):
        if self.volume is not None:
            idx = int(float(val))
            self.show_slice(idx)

    def show_slice(self, z_index):
        if self.volume is None: return
        
        slice_data = np.rot90(self.volume[:, :, z_index])
        
        img = Image.fromarray(slice_data)
        img = img.resize((self.width, self.height), Image.Resampling.LANCZOS)
        
        self.img_ref = ImageTk.PhotoImage(img)
        self.canvas.create_image(self.width//2, self.height//2, image=self.img_ref)
        
        self.lbl_info.config(text=f"Slice: {z_index} / {self.max_slices}")
        self.current_slice = z_index

class LungCancerApp:
    def __init__(self, root):
        self.root = root
        self.root.title(APP_TITLE)
        self.root.geometry("1200x800") 
        self.root.configure(bg="#f4f6f9")

        self.setup_styles()

        self.paned = ttk.PanedWindow(root, orient="horizontal")
        self.paned.pack(fill="both", expand=True, padx=10, pady=10)

        self.left_frame = ttk.Frame(self.paned, padding=10)
        self.paned.add(self.left_frame, weight=1)

        self.right_frame = ttk.Frame(self.paned, padding=10)
        self.paned.add(self.right_frame, weight=2) 

        self.create_header(self.left_frame)
        self.create_controls(self.left_frame)
        self.create_results_area(self.left_frame)
        self.create_log_area(self.left_frame)

        self.create_viewer_area(self.right_frame)

        self.status_var = tk.StringVar(value="Initializing System...")
        self.status_bar = tk.Label(root, textvariable=self.status_var, bd=1, relief=tk.SUNKEN, anchor=tk.W, bg="#e0e0e0", font=("Segoe UI", 9))
        self.status_bar.pack(side=tk.BOTTOM, fill=tk.X)

        self.pipeline = None
        self.temp_dir = "temp_gui_processing"
        if not os.path.exists(self.temp_dir): os.makedirs(self.temp_dir)

        sys.stdout = StdoutRedirector(self.log_text)
        sys.stderr = StdoutRedirector(self.log_text, "stderr")

        threading.Thread(target=self.init_pipeline, daemon=True).start()

    def setup_styles(self):
        style = ttk.Style()
        style.theme_use('clam')
        bg_color = "#f4f6f9"
        style.configure("TFrame", background=bg_color)
        style.configure("TLabelframe", background=bg_color, relief="groove")
        style.configure("TLabelframe.Label", background=bg_color, font=(THEME_FONT, 11, "bold"), foreground="#495057")
        style.configure("TLabel", background=bg_color, font=(THEME_FONT, 10), foreground="#333")
        style.configure("Header.TLabel", font=(THEME_FONT, 18, "bold"), foreground="#2c3e50")
        style.configure("Accent.TButton", font=(THEME_FONT, 11, "bold"), background="#007bff", foreground="white", borderwidth=0)
        style.map("Accent.TButton", background=[("active", "#0056b3")])

    def create_viewer_area(self, parent):
        lbl = ttk.Label(parent, text="CT Scan Viewer (Axial View)", font=(THEME_FONT, 14, "bold"))
        lbl.pack(pady=(0, 10))
        
        self.viewer = CTViewer(parent, width=512, height=512)
        self.viewer.pack(fill="both", expand=True)

    def create_header(self, parent):
        ttk.Label(parent, text="RIBS", style="Header.TLabel").pack(anchor="w")
        ttk.Separator(parent, orient="horizontal").pack(fill="x", pady=(10, 20))

    def create_controls(self, parent):
        frame = ttk.LabelFrame(parent, text=" Control ", padding=15)
        frame.pack(fill="x", pady=(0, 20))
        self.btn_load = ttk.Button(frame, text="📂 Load DICOM Folder", style="Accent.TButton", command=self.select_folder)
        self.btn_load.pack(fill="x")
        
        self.progress = ttk.Progressbar(frame, orient="horizontal", mode="indeterminate")
        self.progress.pack(fill="x", pady=(10, 0))

    def create_results_area(self, parent):
        frame = ttk.LabelFrame(parent, text=" Analysis Results ", padding=10)
        frame.pack(fill="x", pady=(0, 20))
        
        self.lbl_ttf1 = tk.Label(frame, text="TTF1: WAITING", font=(THEME_FONT, 12, "bold"), bg="#f4f6f9", fg="#aaa")
        self.lbl_ttf1.pack(anchor="w")
        
        self.lbl_ck7 = tk.Label(frame, text="CK7:  WAITING", font=(THEME_FONT, 12, "bold"), bg="#f4f6f9", fg="#aaa")
        self.lbl_ck7.pack(anchor="w", pady=(5,0))

    def create_log_area(self, parent):
        frame = ttk.LabelFrame(parent, text=" Logs ", padding=5)
        frame.pack(fill="both", expand=True)
        self.log_text = scrolledtext.ScrolledText(frame, state='disabled', height=10, bg="#212529", fg="#f8f9fa", font=("Consolas", 9))
        self.log_text.pack(fill="both", expand=True)
        self.log_text.tag_config("stderr", foreground="#ff6b6b")

    def init_pipeline(self):
        print("[INFO] Loading AI Models...")
        if os.path.exists(MODEL_FILENAME) and os.path.exists(SCALER_FILENAME):
            try:
                self.pipeline = LungCancerPipeline(MODEL_FILENAME, SCALER_FILENAME)
                print("[SUCCESS] System Ready.")
                self.status_var.set("Ready.")
                self.btn_load.configure(state="normal")
            except Exception as e:
                messagebox.showerror("Error", str(e))
        else:
            messagebox.showwarning("Missing Files", "Model files not found.")

    def select_folder(self):
        if not self.pipeline: return
        path = filedialog.askdirectory()
        if path:
            self.progress.start(10)
            self.btn_load.config(state="disabled")
            
            self.lbl_ttf1.config(text="TTF1: PROCESSING...", fg="orange")
            self.lbl_ck7.config(text="CK7:  PROCESSING...", fg="orange")
            threading.Thread(target=self.process_patient, args=(path,), daemon=True).start()

    def process_patient(self, path):
        try:
            print(f"\n[START] Processing: {os.path.basename(path)}")
            
            nifti_path = os.path.join(self.temp_dir, "scan.nii.gz")
            self.pipeline.convert_dicom_to_nifti(path, nifti_path)
            
            self.root.after(0, lambda: self.viewer.load_nifti(nifti_path))
            
            mask_path = os.path.join(self.temp_dir, "mask.nii.gz")
            self.pipeline.generate_lung_mask(nifti_path, mask_path)
            prep_ct, prep_mask = self.pipeline.preprocess_images(nifti_path, mask_path, self.temp_dir)
            
            feats = self.pipeline.extract_radiomics(prep_ct, prep_mask)
            if feats is not None:
                rez = self.pipeline.predict(prep_ct, feats)
                self.root.after(0, lambda: self.show_results(rez))
            else:
                print("[ERROR] Feature extraction failed.")
                self.stop_ui()

        except Exception as e:
            print(f"[ERROR] {e}")
            import traceback
            traceback.print_exc()
            self.stop_ui()

    def show_results(self, rez):
        c1 = "#28a745" if "POZITIV" in rez['TTF1_Class'] else "#dc3545"
        c2 = "#28a745" if "POZITIV" in rez['CK7_Class'] else "#dc3545"
        
        self.lbl_ttf1.config(text=f"TTF1: {rez['TTF1_Class']} ({rez['Raw_TTF1']:.1%})", fg=c1)
        self.lbl_ck7.config(text=f"CK7:  {rez['CK7_Class']} ({rez['Raw_CK7']:.1%})", fg=c2)
        
        print("[DONE] Analysis Complete.")
        self.stop_ui()

    def stop_ui(self):
        self.root.after(0, lambda: self.progress.stop())
        self.root.after(0, lambda: self.btn_load.config(state="normal"))
        self.root.after(0, lambda: self.status_var.set("Idle."))

if __name__ == "__main__":
    try:
        from ctypes import windll
        windll.shcore.SetProcessDpiAwareness(1)
    except: pass
    root = tk.Tk()
    app = LungCancerApp(root)
    root.mainloop()