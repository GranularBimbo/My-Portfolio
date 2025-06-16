
block_time = -3
back_up_time = -3
idle_check = -3
attack = 0

class guy(object):
    def __init__(self,x,y,x_change,y_change,shield,max_shield,velocity,lunge_reset,mode,dash,stunned):
        self.x = x
        self.y = y
        self.x_change = x_change
        self.y_change = y_change
        self.shield = shield
        self.max_shield = shield
        self.velocity = velocity
        self.lunge_reset = lunge_reset
        self.mode = mode #string
        self.dash = dash
        
    def attack_up(self):
        self.velocity = self.dash
        self.lunge_reset = 0
        self.mode = "attack up"
        
    def attack_down(self):
        self.velocity = self.dash
        self.lunge_reset = 0
        self.mode = "attack down" 
              
    def block_up(self):
        self.mode = "block up"
        
    def block_down(self):
        self.mode = "block down"  
        
        
        
        
        
class upgrade():
    def __init__(self,price,level):
        self.price = price
        self.level = level

        
  
        
