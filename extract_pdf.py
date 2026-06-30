import fitz
import os

pdf_path = r"C:\Users\User\Downloads\Projeto - Final - APO1 - Aprovado (4).pdf"
out_dir = r"c:\Users\User\Desktop\Projeto Final - APO2\projeto-apo2\documentacao\imagens"

os.makedirs(out_dir, exist_ok=True)
os.makedirs(os.path.join(out_dir, "processo_asis"), exist_ok=True)
os.makedirs(os.path.join(out_dir, "epicos"), exist_ok=True)

doc = fitz.open(pdf_path)

mapping = {
    1: os.path.join("processo_asis", "parte_1.png"),
    2: os.path.join("processo_asis", "parte_2.png"),
    3: os.path.join("processo_asis", "parte_3.png"),
    4: "cinco_porques.png",
    5: os.path.join("epicos", "parte_1.png"),
    6: os.path.join("epicos", "parte_2.png"),
    7: "casos_de_uso.png",
    8: "classes_simplificado.png",
    9: "classes_dominio.png",
    10: "seq_comprar.png",
    11: "seq_cancelar.png",
    12: "seq_limpeza.png"
}

for page_idx, out_name in mapping.items():
    if page_idx < len(doc):
        page = doc.load_page(page_idx)
        # Increase resolution (DPI)
        zoom = 2.0
        mat = fitz.Matrix(zoom, zoom)
        pix = page.get_pixmap(matrix=mat)
        pix.save(os.path.join(out_dir, out_name))

print("Extração concluída com sucesso!")
