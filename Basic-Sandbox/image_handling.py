import dearpygui.dearpygui as dpg
from pathlib import Path


def load_images():
    current_dir = Path(__file__).parent
    image_path = current_dir / "images" / "chest.png"
    image_data = dpg.load_image(str(image_path))

    if image_data is not None:
        width, height, channels, data = image_data
        with dpg.texture_registry(show=False):
            dpg.add_static_texture(width=width, height=height, default_value=data, tag="chest_image_texture")
    else:
        print(f"[X] Could not load image at: {image_path.resolve()}")
        # Setting dummy dimensions so the script doesn't crash if the image is missing
        width, height = 100, 100