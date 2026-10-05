import dearpygui.dearpygui as dpg
import random
import math

MAP_COLS = 15
MAP_ROWS = 10
HEX_RADIUS = 20

TERRAIN_COLORS = {
    0: [34, 139, 34],    # Plains (Green)
    1: [218, 165, 32],  # Desert (Gold)
    2: [70, 130, 180],   # Water (Blue)
    3: [112, 128, 144]   # Mountains (Slate)
}

world_grid = [[random.randint(0, 3) for c in range(MAP_COLS)] for r in range(MAP_ROWS)]

def get_hex_center(row, col, radius):
    """Calculates the pixel (x, y) center of a hex in an Even-Row Offset grid."""
    # Horizontal spacing between adjacent hex centers
    width = radius * math.sqrt(3)
    
    # Vertical spacing between rows (3/4 of the total hex height)
    height = radius * 1.5
    
    # Calculate base pixel coordinates
    x = col * width
    y = row * height
    
    # If it's an odd row, offset it horizontally by half a hex width
    if row % 2 == 1:
        x += width / 2
        
    # Add padding so it doesn't clip against the very top/left edges
    return x + radius + 10, y + radius + 10

def get_hex_points(cx, cy, radius):
    """Calculates the 6 outer vertex points for a pointy-topped hex."""
    points = []
    for i in range(6):
        # 30-degree rotation offset centers the flat edges correctly for "pointy-topped" hexes
        angle_rad = math.radians(60 * i - 30)
        px = cx + radius * math.cos(angle_rad)
        py = cy + radius * math.sin(angle_rad)
        points.append([px, py])
    return points

# --- DRAWING SYSTEM ---

def draw_hex_map():
    dpg.delete_item("map_drawlist", children_only=True)
    
    for r in range(MAP_ROWS):
        for c in range(MAP_COLS):
            terrain_type = world_grid[r][c]
            fill_color = TERRAIN_COLORS[terrain_type]
            
            # Find where it belongs on screen
            cx, cy = get_hex_center(r, c, HEX_RADIUS)
            vertices = get_hex_points(cx, cy, HEX_RADIUS)
            
            # Draw the hex polygon
            dpg.draw_polygon(
                points=vertices,
                fill=fill_color,
                color=[50, 50, 50], # Border color (Dark gray)
                thickness=1,
                parent="map_drawlist"
            )
            
            # Optional: Draw the array coordinate text right on the hex for debugging
            dpg.draw_text(
                pos=[cx - 12, cy - 8], 
                text=f"{r},{c}", 
                size=11, 
                color=[255, 255, 255, 180], 
                parent="map_drawlist"
            )