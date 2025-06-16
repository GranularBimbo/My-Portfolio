import pygame
import sys
from image_renderer import *
from audio_renderer import *
from combat import battle_info
import ctypes
import random
import pickle

pygame.init()

display_width = 1080
display_height = 720

display = pygame.display.set_mode([display_width,display_height],pygame.FULLSCREEN)
pygame.display.set_caption("Dynamic Equilibrium")
pygame.display.set_icon(icon)

clock = pygame.time.Clock()
FPS = 30

running = True
display.blit(menu,[0,0])

pygame.mouse.set_visible(False)

flex_seal = 1

agression = 1

slot1 = "open"
slot2 = "open"
slot3 = "open"

max_timer = 875
timer = max_timer

fans = 0
max_fans = 50000
myfont = pygame.font.SysFont("Arial",100,False,False)
font2 = pygame.font.SysFont("Arial",30,False,False)
font3 = pygame.font.SysFont("Arial",26,True,False)
money = 0

money_text = font2.render(str(money),False,(0,0,0))
fan_counter = font2.render(str(fans),False,(0,0,0))

player = battle_info.guy(32,610,0,0,0,0,0,1,"idle",15,False)
player.shield = player.max_shield
enemy = battle_info.guy(display_width-(64*2),610,0,0,0,0,0,1,"idle",-15,False)
enemy.shield = enemy.max_shield
fan_multiplier = 1
cont = 0

volume = 100

#the upgrade class instances, not the variables
fan_mult = battle_info.upgrade(20000,1)
shield_upgrade = battle_info.upgrade(10000,0)
bleachers_upgrade = battle_info.upgrade(70000,1)

tutorial = 1

#dictionary holding all save data
stats = {"fans": fans,"max_shield": player.max_shield,"fan mult": fan_multiplier,"max fans": max_fans,"money": money,"continue": cont,"fan level": fan_mult.level,
         "shield level": shield_upgrade.level,"bleachers level": bleachers_upgrade.level,"max timer": max_timer,"slot1": slot1,"slot2": slot2,"slot3": slot3,
         "shield cost": shield_upgrade.price,"fan cost": fan_mult.price,"bleachers cost": bleachers_upgrade.price,"tutorial": tutorial}

clinks = 0
jabs = 0

def is_admin():
    try:
        return ctypes.windll.shell32.IsUserAnAdmin()
    except:
        return False


def buy(cost):
    if money >= cost:
        money -= cost




def save():
    global fans
    global player
    global max_fans
    global money
    global cont
    global fan_mult
    global shield_upgrade
    global bleachers_upgrade
    global max_timer
    global slot1
    global slot2
    global slot3
    global tutorial
    
    stats["fans"] = fans
    stats["max shield"] = player.max_shield
    stats["fan mult"] = fan_multiplier
    stats["max fans"] = max_fans
    stats["money"] = money
    stats["continue"] = cont
    stats["fan level"] = fan_mult.level
    stats["shield level"] = shield_upgrade.level
    stats["bleachers level"] = bleachers_upgrade.level
    stats["max timer"] = max_timer
    stats["slot1"] = slot1
    stats["slot2"] = slot2
    stats["slot3"] = slot3
    stats["fan cost"] = fan_mult.price
    stats["shield cost"] = shield_upgrade.price
    stats["bleachers cost"] = bleachers_upgrade.price
    stats["tutorial"] = tutorial
    
    pickle_out = open("data/save.dat","wb")
    pickle.dump(stats, pickle_out)
    
    try:
        pickle_out.close()
    except:
        return False
    
    
def load():
    global fans
    global player
    global max_fans
    global money
    global cont
    global fan_mult
    global shield_upgrade
    global bleachers_upgrade
    global max_timer
    global slot1
    global slot2
    global slot3
    global tutorial
    
    pickle_in = open("data/save.dat","rb")
    stats = pickle.load(pickle_in) 
    
    fans = stats["fans"]
    player.max_shield = stats["max_shield"]
    fan_multiplier = stats["fan mult"]
    max_fans = stats["max fans"]
    money = stats["money"]
    cont = stats["continue"] 
    fan_mult.level = stats["fan level"]
    shield_upgrade.level = stats["shield level"]
    bleachers_upgrade.level = stats["bleachers level"]
    max_timer = stats["max timer"]
    slot1 = stats["slot1"]
    slot2 = stats["slot2"]
    slot3 = stats["slot3"]
    fan_mult.price = stats["fan cost"]
    shield_upgrade.price = stats["shield cost"]
    bleachers_upgrade.price = stats["bleachers cost"]
    tutorial = stats["tutorial"]
    
    try:
        pickle_in.close()
    except:
        return False
        
        


def calculate_fans():
    global jabs
    global clinks
    global max_timer
    global timer
    global fan_multiplier
    global fans                    # /0.001 to avoid division by 0
                                   # /25 so  that it's an accurate number of seconds because every 25 is 1 second
    fans += int((float((jabs+clinks)/(0.001)))/((max_timer-timer)/25))*fan_multiplier
        
        
        
def calculate_money():
    global fans
    global money
    global jabs
    global clinks
    global max_fans
    global max_timer
    global timer
    
    money += int(((fans+((jabs+clinks)*.001))/((max_timer-timer)/25)))
        
        

class button(object):
    def __init__(self,x,y,w,h,active):
        self.x = x
        self.y = y
        self.w = w
        self.h = h
        self.active = active
        
        
class icon(object):
    def __init__(self,x,y):
        self.x = x
        self.y = y
        self.w = 32
        self.h = 32
        
        
#buttons      
to_de = button(0,350,300,80,False)
to_de.x = display_width/2 - (to_de.w/2) 

to_men = button(0,250,300,80,False)
to_men.x = display_width/2 - (to_men.w/2) 

upgrade = button(10,350,300,80,False)

shield_icon = icon(0,64)
        
        
def tut():
    global running
    global money
    global to_de
    global to_men
    global upgrade
    global shield_icon
    global tutorial
    
    yeet = -3
    yeet2 = -3
    count = -3
    counts = 1
    
    arena_mode = button(10,250,300,80,False)
    
    tutFont = font3.render("Press or hold 'W' to block up",False,(0,0,0))
    objective = 1
    
    #to keep track of which upgrade is selected
    select = 0  
    
    #purchase and back buttons
    back_up = button(650,600,300,80,False)
    purchase = button(100,600,300,80,False)
    
    #upgrade buttons- one is fan mult, two is shield, and three is bleachers
    one = button(250,140,11,11,False)
    two = button(290,348,11,11,False)
    three = button(270,250,11,11,False)    
    
    r_down = False
    l_down = False
    
    icons = 0
    
    r = 0
    l = 0
    
    while running:
        
        mouse = pygame.mouse.get_pos()
        click = pygame.mouse.get_pressed()        
        
        #rendering text
        fan_cost_text = font3.render("$" + str(fan_mult.price),False,(0,0,0))
        
        shield_cost_text = font3.render("$" + str(shield_upgrade.price),False,(0,0,0))
        
        bleachers_cost_text = font3.render("$" + str(bleachers_upgrade.price),False,(0,0,0))          
        
        fan_level_text = font3.render(str(fan_mult.level),False,(0,0,0))
        shield_level_text = font3.render(str(shield_upgrade.level),False,(0,0,0))
        bleachers_level_text = font3.render(str(bleachers_upgrade.level),False,(0,0,0))        
        
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                
            if event.type == pygame.KEYDOWN:
                #blocking
                if event.key == pygame.K_s:
                    player.block_down()
                    if objective == 2:
                        objective = 3
                        tutFont = font3.render("Press or hold 'A' or 'D' to move around",False,(0,0,0))
                        
                        
                if event.key == pygame.K_w:
                    player.block_up()
                    if objective == 1:
                        objective = 2
                        tutFont = font3.render("Press or hold 'S' to block down",False,(0,0,0))
                    
                if player.mode == "block down":
                    player.lunge_reset = 1
                    if event.key == pygame.K_d:
                        if player.lunge_reset == 1:
                            player.attack_down()
                            if objective == 5:
                                objective = 6
                                tutFont = font3.render("<-- These icons show your fans and money",False,(0,0,0))
                                icons = 1
                                count = 25
                        
                if player.mode == "block up":
                    player.lunge_reset = 1
                    if event.key == pygame.K_d:
                        if player.lunge_reset == 1:
                            player.attack_up()
                            if objective == 4:
                                objective = 5
                                tutFont = font3.render("To perform a low jab, press 'D' while blocking down",False,(0,0,0))
                 
                #walking   
                if event.key == pygame.K_a:
                    l_down = True
                    r_down = False
                    if objective == 3:
                        l = 1
                    
                if event.key == pygame.K_d:
                    r_down = True
                    l_down = False
                    if objective == 3:
                        r = 1
                    
                    
            if event.type == pygame.KEYUP:
                #returning to neutral stance
                if event.key == pygame.K_s and player.mode == "block down":
                    player.lunge_reset = 1
                    player.mode = "idle"
                if event.key == pygame.K_w and player.mode == "block up":
                    player.lunge_reset = 1
                    player.mode = "idle"
                    
                if event.key == pygame.K_a:
                    l_down = False
                    
                if event.key == pygame.K_d:
                    r_down = False  
                    
                    
            if back_up.x+back_up.w > mouse[0] > back_up.x and back_up.y+back_up.h > mouse[1] > back_up.y:
                back_up.active = True
                if click[0] == 1:
                    pass
                
                
            else:
                back_up.active = False
                
                
                
            if purchase.x+purchase.w > mouse[0] > purchase.x and purchase.y+purchase.h > mouse[1] > purchase.y:
                purchase.active = True
                if click[0] == 1:
                    if select == 1:
                        if fan_mult.level < 3:
                            if money >= fan_mult.price:
                                money -= fan_mult.price
                                fan_mult.level += 1
                                fan_multiplier += 1
                                fan_mult.price += 70000
                                if fan_mult.level == 2:
                                    if slot1 == "open":
                                        slot1 = "fan mult"
                                        slot2 = "shield"
                                    else:
                                        slot2 = "fan mult"
                                save()
                            
                    if select == 2:
                        if shield_upgrade.level < 5:
                            if money >= shield_upgrade.price:
                                shield_upgrade.level += 1
                                money -= shield_upgrade.price
                                shield_upgrade.price += 30000
                                if shield_upgrade.level == 1:
                                    if slot1 == "open":
                                        slot1 = "shield"
                                        slot2 = "fan mult"
                                    else:
                                        slot2 = "shield"
                                save()
                                    
                                    
                    if select == 3:
                        if bleachers_upgrade.level < 3:
                            if money >= bleachers_upgrade.price:
                                money -= bleachers_upgrade.price
                                bleachers_upgrade.level += 1
                                max_fans += 50000
                                bleachers_upgrade.price += 100000
                                
                            save()
                
                
            else:
                purchase.active = False
                
                
            save()
                
                
            if one.x+one.w > mouse[0] > one.x and one.y+one.h > mouse[1] > one.y:
                one.active = True
                one.w = 32
                one.h = 32
                if click[0] == 1:
                    select = 1
                
                
            else:
                one.active = False
                one.w = 11
                one.h = 11
                
                
            if two.x+two.w > mouse[0] > two.x and two.y+two.h > mouse[1] > two.y:
                two.active = True
                two.w = 32
                two.h = 32
                if click[0] == 1:
                    select = 2
                    if objective == 10:
                        objective = 11
                        tutFont = font3.render("All 3 boxes are different upgrades",False,(0,0,0))
                        count = 150
                
                
            else:
                two.active = False
                two.w = 11
                two.h = 11
                
                
            if three.x+three.w > mouse[0] > three.x and three.y+three.h > mouse[1] > three.y:
                three.active = True
                three.w = 32
                three.h = 32
                if click[0] == 1:
                    select = 3
                
                
            else:
                three.active = False
                three.w = 11
                three.h = 11
                
            #the arena mode button  
            if arena_mode.x + arena_mode.w > mouse[0] > arena_mode.x and arena_mode.y + arena_mode.h > mouse[1] > arena_mode.y:
                arena_mode.active = True
                if flex_seal != 2:
                    if click[0] == True:
                        pass
        
            else:
                arena_mode.active = False
        
        
            if upgrade.x + upgrade.w > mouse[0] > upgrade.x and upgrade.y + upgrade.h > mouse[1] > upgrade.y:
                upgrade.active = True
                if flex_seal != 2:
                    if click[0] == True:
                        pass
        
            else:
                upgrade.active = False
        
        
        
            if to_de.x + to_de.w > mouse[0] > to_de.x and to_de.y + to_de.h > mouse[1] > to_de.y:
                to_de.active = True
                if click[0] == True:
                    pass
        
            else:
                to_de.active = False            
                
                
            money_text = font2.render(str(money),False,(0,0,0))            
                    
                    
        if yeet > 0:
            yeet -= 1
            
        if yeet2 > 0:
            yeet2 -= 1
            
        if count > 0:
            count -= 1
           
        #flashes icons 
        if count == 0 and counts == 1:
            count = 25
            counts = 2
            icons = 0
            
        if count == 0 and counts == 2:
            count = 25
            counts = 3
            icons = 1
            
        if count == 0 and counts == 3:
            count = 25
            counts = 4
            icons = 0
            
        if count == 0 and counts == 4:
            count = 50
            counts = 5
            icons = 1
            
        if count == 0 and counts == 5:
            count = 75
            counts = 6
            icons = 1
            objective = 7
            tutFont = font3.render("<-- Additional upgrades will show up here",False,(0,0,0))
            
        if count == 0 and counts == 6:
            count = 100
            counts = 7
            objective = 8
            tutFont = font3.render("If you & the enemy collide with 1 high & 1 low jab",False,(0,0,0))
            
        if count == 0 and counts == 7:
            count = 75
            counts = 8
            tutFont = font3.render("The person with the high jab will win",False,(0,0,0))
            
        if count == 0 and counts == 8:
            count = 75
            counts = 9
            objective = 9
            icons = 2
            tutFont = font3.render("Now for the upgrades menu",False,(0,0,0))
            
        if count == 0 and counts == 9:
            count = -3
            counts = 10
            objective = 10
            tutFont2 = myfont.render("O",False,(255,0,0))
            tutFont = font3.render("Click the circled box to view the upgrade",False,(0,0,0))
            
        if count == 0 and counts == 10:
            count = 125
            counts = 11
            objective = 12
            tutFont = font3.render("The enemy will gain shield over time",False,(0,0,0))
            
        if count == 0 and counts == 11:
            count = 100
            counts = 12
            objective = 13
            tutFont = font3.render("You will be alerted on the main menu",False,(0,0,0))
            
        if count == 0 and counts == 12:
            objective = 14
            count = 100
            counts = 13
            tutFont = font3.render("You gain money based on your number of fans",False,(0,0,0))
            
        if count == 0 and counts == 13:
            objective = 15
            count = 100
            counts = 14
            tutFont = font3.render("You gain fans based on how well you do each round",False,(0,0,0))
            
        if count == 0 and counts == 14:
            objective = 16
            count = 100
            counts = 15
            tutFont = font3.render("But if you die everything restarts",False,(0,0,0))
            
        if count == 0 and counts == 15:
            objective = 17
            count = 100
            counts = 16
            tutFont = font3.render("You're on your own now, so go and spill some odio blood for me!",False,(0,0,0))
            
        if count == 0 and counts == 16:
            tutorial = 0
            count = -3
            counts = 17
            main_menu()
            save()
        
        #makes message wait until displaying next message            
        if r == 1 and l == 1:
            objective = 4
            tutorial = 0
            save()
            tutFont = font3.render("Now you're going to attack",False,(0,0,0))
            yeet = 50 #waits 2 seconds
            l = 0
            
        if yeet == 0:
            tutFont = font3.render("While blocking up, press 'D' for a high jab",False,(0,0,0))
            yeet = -3
                    
        if r_down == True and l_down == False:
            player.x_change = 5
            
        if l_down == True and r_down == False:
            player.x_change = -5
            
        if l_down == False and r_down == False:
            player.x_change = 0
        
                
                
        mouse = pygame.mouse.get_pos() 
        
        player.x += player.velocity
                
                
        
        if player.velocity > 0:
            player.velocity -= 1
            
        if player.velocity == 0 and player.mode == "attack up":
            player.mode = "idle"
            
        if player.velocity == 0 and player.mode == "attack down":
            player.mode = "idle"
        
        if objective > 2:
            player.x += player.x_change
             
        if objective < 9:          
            display.blit(battle_area,[0,0])
            
        else:
            display.blit(upgrade_menu_display,[0,0])
            
            
        #text display  
        if objective > 8:
            if select == 0:    
                display.blit(upgrade_menu_display,[0,0])
                
            elif select == 1:
                display.blit(fan,[0,0])
                display.blit(fan_cost_text,[100,495])
                display.blit(fan_level_text,[100,530])  
                
            elif select == 2:
                display.blit(shield,[0,0])
                display.blit(shield_cost_text,[100,495])
                display.blit(shield_level_text,[100,530])    
                
            elif select == 3:
                display.blit(bleachers,[0,0])
                display.blit(bleachers_cost_text,[100,495])
                display.blit(bleachers_level_text,[100,530])
                
                
        if objective > 13:
            display.blit(menu,[0,0])
        
        if objective == 1:
            display.blit(tutFont,[380,50])
            
        if objective == 2:
            display.blit(tutFont,[350,50])
            
        if objective == 3:
            display.blit(tutFont,[320,50])
            
        if objective == 4 and yeet != -3:
            display.blit(tutFont,[400,50])
            
        if objective == 4 and yeet == -3:
            display.blit(tutFont,[320,50])
            
        if objective == 5:
            display.blit(tutFont,[250,50])
            
        if objective == 6:
            display.blit(tutFont,[60,25])
            
        if objective == 7:
            display.blit(tutFont,[60,100])
            
        if objective == 8 and counts == 7:
            display.blit(tutFont,[250,50])
            
        if objective == 8 and counts == 8:
            display.blit(tutFont,[310,50])
            
        if objective == 9:
            display.blit(tutFont,[650,200])
            
        if objective == 10:
            display.blit(tutFont2,[260,300])
            display.blit(tutFont,[560,348])
            
        if objective == 11:
            display.blit(tutFont2,[260,300])
            display.blit(tutFont2,[220,90])
            display.blit(tutFont2,[240,200])
            display.blit(tutFont,[560,348])
            
        if objective == 12:
            display.blit(tutFont,[560,348])
            
        if objective == 13:
            display.blit(tutFont,[560,348])
            
        if objective == 14:
            display.blit(tutFont,[260,650])
            
        if objective == 15:
            display.blit(tutFont,[230,650])
            
        if objective == 16:
            display.blit(tutFont,[340,650])
            
        if objective == 17:
            display.blit(tutFont,[170,650])
            
            
        if objective < 9:
            if player.mode == "idle":
                display.blit(sword,[player.x,player.y])
            if player.mode == "block up":
                display.blit(sword_up,[player.x,player.y])
            if player.mode == "block down":
                display.blit(sword_down,[player.x,player.y])
            if player.mode == "attack up":
                display.blit(jab_up,[player.x,player.y])
            if player.mode == "attack down":
                display.blit(jab_down,[player.x,player.y]) 
                
            if enemy.mode == "idle":
                display.blit(sword2,[enemy.x,enemy.y])
            if enemy.mode == "block up":
                display.blit(sword_up2,[enemy.x,enemy.y])
            if enemy.mode == "block down":
                display.blit(sword_down2,[enemy.x,enemy.y])
            if enemy.mode == "attack up":
                display.blit(jab_up2,[enemy.x,enemy.y])
            if enemy.mode == "attack down":
                display.blit(jab_down2,[enemy.x,enemy.y])  
                
            
            
                if to_de.active == False:
                    display.blit(to_desktop,[to_de.x,to_de.y])
                else:
                    display.blit(to_desktop_active,[to_de.x,to_de.y])
            
            
                display.blit(money_icon,[0,688])
                display.blit(money_text,[40,688])
                display.blit(fan_icon,[0,656])
                display.blit(fan_counter,[40,656])        
                
                
            
        if objective > 8 and objective < 14:    
            if back_up.active == False:
                display.blit(back,[back_up.x,back_up.y])
        
            else:
                display.blit(back_active,[back_up.x,back_up.y])
        
        
            if select > 0: 
                if purchase.active == False:
                    display.blit(purchase_button,[purchase.x,purchase.y])
        
                else:
                    display.blit(purchase_button_active,[purchase.x,purchase.y])
        
        
            if one.active == False:
                display.blit(marker,[one.x,one.y])
        
            else:
                display.blit(marker_active,[one.x,one.y])
        
        
            if two.active == False:
                display.blit(marker,[two.x,two.y])
        
            else:
                display.blit(marker_active,[two.x,two.y])
        
        
            if three.active == False:
                display.blit(marker,[three.x,three.y])
        
            else:
                display.blit(marker_active,[three.x,three.y])    
                
                
        #highlights the button when mouse is hovering over it
        if objective > 13:  
            if cont == 0:
                if arena_mode.active == False:
                    display.blit(arena,[arena_mode.x,arena_mode.y])
    
                else:
                    display.blit(arena_active,[arena_mode.x,arena_mode.y])
    
            else:
                if arena_mode.active == False:
                    display.blit(continue_run,[arena_mode.x,arena_mode.y])
    
                else:
                    display.blit(continue_run_active,[arena_mode.x,arena_mode.y])
    
    
            if upgrade.active == False:
                display.blit(upgrades,[upgrade.x,upgrade.y])
    
            else:
                display.blit(upgrades_active,[upgrade.x,upgrade.y])    
                
                
            if to_de.active == False:
                display.blit(to_desktop,[to_de.x,to_de.y])
            else:
                display.blit(to_desktop_active,[to_de.x,to_de.y])            
        
                
        if player.x < 32:
                player.x += 5
                
        if player.x > enemy.x - 64:
            player.x -= 5 
            
         
        if icons == 1:   
            display.blit(fan_icon,[0,0])
            display.blit(fan_counter,[40,0])
            display.blit(money_icon,[0,32])
            display.blit(money_text,[40,32])
            
        if icons == 2:
            display.blit(money_icon,[10,680])
            display.blit(money_text,[50,680])      
        
        if objective > 8:    
            display.blit(cursor,[mouse[0],mouse[1]-5])
        
        clock.tick(FPS)
        pygame.display.update()
        
    sys.exit()
        

def main_menu():
    global running
    global fans
    global player
    global stats
    global to_de
    global upgrade_button
    global slot1
    global slot2
    global slot3
    global cont
    global max_fans
    global money
    global max_timer
    global money_text
    global fan_counter
    global flex_seal
    global tutorial
    
    pygame.mixer.music.stop()
    
    flex_seal = 1
    
    upgrade.x = 10
    
    warning = 0
    warn_time = -3
    warn_times = 0
    
    load()
    save()
    
    
    if fans == 0:
        cont = 0
     
    
    arena_mode = button(10,250,300,80,False)
    to_de.x = 10
    to_de.y = 450
    
    if fans > max_fans:
        fans = max_fans
        save()
    
    if is_admin():
        load()
        
    else:
        ctypes.windll.shell32.ShellExecuteW(None, "runas", sys.executable, "", None, 1)    
    
    enemy.shield = enemy.max_shield
    
    #decides when the enemy upgrades shield
    if cont >= 10 and cont < 25:
        enemy.max_shield = 1
        max_timer = 1125
        save()
        
    if cont >= 25 and cont < 45:
        enemy.max_shield = 2
        max_timer = 1375
        save()
       
        
    if cont >= 45 and cont < 60:
        enemy.max_shield = 3
        max_timer = 1625
        save()       
        
        
    if cont >= 60 and cont < 90:
        enemy.max_shield = 4
        max_timer = 1875
        save()
        
        
    if cont >= 90:
        enemy.max_shield = 5
        save()
        
    #shows warning sign    
    if cont == 10:
        if flex_seal == 1:
            warning = 1
            warn_time = 25
            warn_times = 1
            flex_seal = 2 
            
    if cont == 25:
        if flex_seal == 1:
            warning = 1
            warn_time = 25
            warn_times = 1
            flex_seal = 2 
        
    if cont == 45:
        if flex_seal == 1:
            warning = 1
            warn_time = 25
            warn_times = 1
            flex_seal = 2 
            
    if cont == 60:
        if flex_seal == 1:
            warning = 1
            warn_time = 25
            warn_times = 1
            flex_seal = 2 
            
    if cont == 90:
        if flex_seal == 1:
            warning = 1
            warn_time = 25
            warn_times = 1
            flex_seal = 2 
        
    
    
    #moves all slots down when enemy has shield
    if enemy.max_shield > 0:
        if slot1 == "fan mult":
            slot1 = "bad shield"
            slot2 = "fan mult"
            slot3 = "shield"
                
                
        elif slot1 == "shield":
            slot1 = "bad shield"
            slot2 = "shield"
            slot3 = "fan mult"
                
        save()
    
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                        
                
        mouse = pygame.mouse.get_pos()
        click = pygame.mouse.get_pressed()
                
        #the arena mode button  
        if arena_mode.x + arena_mode.w > mouse[0] > arena_mode.x and arena_mode.y + arena_mode.h > mouse[1] > arena_mode.y:
            arena_mode.active = True
            if flex_seal != 2:
                if click[0] == True:
                    save()             
                    loop()
            
        else:
            arena_mode.active = False
            
            
        if upgrade.x + upgrade.w > mouse[0] > upgrade.x and upgrade.y + upgrade.h > mouse[1] > upgrade.y:
            upgrade.active = True
            if flex_seal != 2:
                if click[0] == True:
                    upgrade_menu()
            
        else:
            upgrade.active = False
            
            
            
        if to_de.x + to_de.w > mouse[0] > to_de.x and to_de.y + to_de.h > mouse[1] > to_de.y:
            to_de.active = True
            if click[0] == True:
                save()
                running = False
            
        else:
            to_de.active = False
                
                
        display.blit(menu,[0,0])
        
        money_text = font2.render(str(money),False,(0,0,0))
        fan_counter = font2.render(str(fans),False,(0,0,0))  
        
        
        if warn_time > 0:
            warn_time -= 1
            
         
        #plays tutorial on first start up   
        if tutorial == 1:
            tut()
            tutorial = 0
            save()
        
        
        #warning animation
        if warn_time == 0 and warn_times == 1:
            warning = 0
            warn_time = 25
            warn_times = 2
            
        if warn_time == 0 and warn_times == 2:
            warning = 1
            warn_time = 25
            warn_times = 3
            
        if warn_time == 0 and warn_times == 3:
            warning = 0
            warn_time = 25
            warn_times = 4
            
        if warn_time == 0 and warn_times == 4:
            warning = 1
            warn_time = 25
            warn_times = 5
            
        if warn_time == 0 and warn_times == 5:
            warning = 0
            warn_time = -3
            warn_times = 0
            flex_seal = 3
        
        #highlights the button when mouse is hovering over it
        if cont == 0:
            if arena_mode.active == False:
                display.blit(arena,[arena_mode.x,arena_mode.y])
                
            else:
                display.blit(arena_active,[arena_mode.x,arena_mode.y])
                
        else:
            if arena_mode.active == False:
                display.blit(continue_run,[arena_mode.x,arena_mode.y])
                
            else:
                display.blit(continue_run_active,[arena_mode.x,arena_mode.y])
                
                
        if upgrade.active == False:
                display.blit(upgrades,[upgrade.x,upgrade.y])
                
        else:
            display.blit(upgrades_active,[upgrade.x,upgrade.y])
            
            
        if to_de.active == False:
            display.blit(to_desktop,[to_de.x,to_de.y])
        else:
            display.blit(to_desktop_active,[to_de.x,to_de.y])
            
            
        display.blit(money_icon,[0,688])
        display.blit(money_text,[40,688])
        display.blit(fan_icon,[0,656])
        display.blit(fan_counter,[40,656])
        
        if warning == 1:
            display.blit(warn,(350,200))
            
            
        display.blit(cursor,[mouse[0],mouse[1]-5])
            
                
        clock.tick(FPS)
        pygame.display.update()
        
    sys.exit()
    


def upgrade_menu():
    global running
    global stats
    global font2
    global money
    global fan_multiplier
    global fans
    global max_fans
    global slot1
    global slot2
    global slot3
    global player
    global shield_upgrade
    global fan_mult
    global bleachers_upgrade
    global enemy
    global player
    global max_timer
    global money_text
    global flex_seal
    
    flex_seal = 0
    
    pygame.mixer.music.stop()
    
    
    #to keep track of which upgrade is selected
    select = 0  
    
    #purchase and back buttons
    back_up = button(650,600,300,80,False)
    purchase = button(100,600,300,80,False)
    
    #upgrade buttons- one is fan mult, two is shield, and three is bleachers
    one = button(250,140,11,11,False)
    two = button(290,348,11,11,False)
    three = button(270,250,11,11,False)
    
    load()
    
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                
                
        mouse = pygame.mouse.get_pos()
        click = pygame.mouse.get_pressed()
        
        
        #rendering text
        fan_cost_text = font3.render("$" + str(fan_mult.price),False,(0,0,0))
        
        shield_cost_text = font3.render("$" + str(shield_upgrade.price),False,(0,0,0))
        
        bleachers_cost_text = font3.render("$" + str(bleachers_upgrade.price),False,(0,0,0))          
        
        fan_level_text = font3.render(str(fan_mult.level),False,(0,0,0))
        shield_level_text = font3.render(str(shield_upgrade.level),False,(0,0,0))
        bleachers_level_text = font3.render(str(bleachers_upgrade.level),False,(0,0,0))
                
           
        #buttons     
        if back_up.x+back_up.w > mouse[0] > back_up.x and back_up.y+back_up.h > mouse[1] > back_up.y:
            back_up.active = True
            if click[0] == 1:
                main_menu()
            
            
        else:
            back_up.active = False
            
            
            
        if purchase.x+purchase.w > mouse[0] > purchase.x and purchase.y+purchase.h > mouse[1] > purchase.y:
            purchase.active = True
            if click[0] == 1:
                if select == 1:
                    if fan_mult.level < 3:
                        if money >= fan_mult.price:
                            money -= fan_mult.price
                            fan_mult.level += 1
                            fan_multiplier += 1
                            fan_mult.price += 70000
                            if fan_mult.level == 2:
                                if slot1 == "open":
                                    slot1 = "fan mult"
                                    slot2 = "shield"
                                else:
                                    slot2 = "fan mult"
                            save()
                        
                if select == 2:
                    if shield_upgrade.level < 5:
                        if money >= shield_upgrade.price:
                            shield_upgrade.level += 1
                            money -= shield_upgrade.price
                            shield_upgrade.price += 30000
                            if shield_upgrade.level == 1:
                                if slot1 == "open":
                                    slot1 = "shield"
                                    slot2 = "fan mult"
                                else:
                                    slot2 = "shield"
                            save()
                                
                                
                if select == 3:
                    if bleachers_upgrade.level < 3:
                        if money >= bleachers_upgrade.price:
                            money -= bleachers_upgrade.price
                            bleachers_upgrade.level += 1
                            max_fans += 50000
                            bleachers_upgrade.price += 100000
                            
                        save()
            
            
        else:
            purchase.active = False
            
            
        save()
            
            
        if one.x+one.w > mouse[0] > one.x and one.y+one.h > mouse[1] > one.y:
            one.active = True
            one.w = 32
            one.h = 32
            if click[0] == 1:
                select = 1
            
            
        else:
            one.active = False
            one.w = 11
            one.h = 11
            
            
        if two.x+two.w > mouse[0] > two.x and two.y+two.h > mouse[1] > two.y:
            two.active = True
            two.w = 32
            two.h = 32
            if click[0] == 1:
                select = 2
            
            
        else:
            two.active = False
            two.w = 11
            two.h = 11
            
            
        if three.x+three.w > mouse[0] > three.x and three.y+three.h > mouse[1] > three.y:
            three.active = True
            three.w = 32
            three.h = 32
            if click[0] == 1:
                select = 3
            
            
        else:
            three.active = False
            three.w = 11
            three.h = 11
            
            
        money_text = font2.render(str(money),False,(0,0,0))
         
        #text display   
        if select == 0:    
            display.blit(upgrade_menu_display,[0,0])
            
        elif select == 1:
            display.blit(fan,[0,0])
            display.blit(fan_cost_text,[100,495])
            display.blit(fan_level_text,[100,530])  
            
        elif select == 2:
            display.blit(shield,[0,0])
            display.blit(shield_cost_text,[100,495])
            display.blit(shield_level_text,[100,530])    
            
        elif select == 3:
            display.blit(bleachers,[0,0])
            display.blit(bleachers_cost_text,[100,495])
            display.blit(bleachers_level_text,[100,530])             
        
        
        #button display
        if back_up.active == False:
            display.blit(back,[back_up.x,back_up.y])
            
        else:
            display.blit(back_active,[back_up.x,back_up.y])
            
           
        if select > 0: 
            if purchase.active == False:
                display.blit(purchase_button,[purchase.x,purchase.y])
                
            else:
                display.blit(purchase_button_active,[purchase.x,purchase.y])
            
            
        if one.active == False:
            display.blit(marker,[one.x,one.y])
            
        else:
            display.blit(marker_active,[one.x,one.y])
            
            
        if two.active == False:
            display.blit(marker,[two.x,two.y])
            
        else:
            display.blit(marker_active,[two.x,two.y])
            
            
        if three.active == False:
            display.blit(marker,[three.x,three.y])
            
        else:
            display.blit(marker_active,[three.x,three.y])
            
            
        display.blit(money_icon,[10,680])
        display.blit(money_text,[50,680])
        
        display.blit(cursor,[mouse[0],mouse[1]-5])
            
                
                
        clock.tick(FPS)
        pygame.display.update()
        
    sys.exit()



def victory():
    global running
    global stats
    global agression
    global to_de
    global to_men
    global slot1
    global slot2
    global money
    global cont
    global fans
    global max_fans
    global money_text
    global fan_counter
    
    to_de.x = display_width/2 - (to_de.w/2) 
    to_de.y = 450
    
    upgrade.x = ((display_width/2)-(upgrade.w/2))
    upgrade.y = 350
    
    to_men.y = 250
    
    player.mode = "idle" 
    
    load()
    
    if fans > max_fans:
        fans = max_fans
    
    save()
    
    
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                
        mouse = pygame.mouse.get_pos()
        click = pygame.mouse.get_pressed()
                
        #buttons       
        if to_men.x+to_men.w > mouse[0] > to_men.x and to_men.y+to_men.h > mouse[1] > to_men.y:
            to_men.active = True
            if click[0] == 1:
                main_menu()
            
        else:
            to_men.active = False
            
            
        if upgrade.x+upgrade.w > mouse[0] > upgrade.x and upgrade.y+upgrade.h > mouse[1] > upgrade.y:
            upgrade.active = True
            if click[0] == 1:
                upgrade_menu()
            
        else:
            upgrade.active = False
            
            
        if to_de.x+to_de.w > mouse[0] > to_de.x and to_de.y+to_de.h > mouse[1] > to_de.y:
            to_de.active = True
            if click[0] == 1:
                cont = stats["continue"]
                save()
                running = False
            
        else:
            to_de.active = False
                
                
        display.blit(arena_victory,[0,0])
        fan_counter = font2.render(str(fans),False,(0,0,0))
        
        #shows the crowd
        if fans < 10000:
            display.blit(q,[96,224])
            
        if fans >= 10000 and fans < 25000:
            display.blit(half,[96,224])
            
        if fans < 50000 and fans >= 25000:
            display.blit(fq,[96,224])
            
        if fans >= 50000:
            display.blit(full,[96,224])    
                
                
        #second bleachers   
        if fans >= 50000 and fans < 60000 and bleachers_upgrade.level == 2:
            display.blit(q2,[96,288])
            
        if fans >= 60000 and fans < 85000:
            display.blit(half2,[96,288])
            
        if fans >= 85000 and fans < 100000:
            display.blit(fq2,[96,288])
            
        if fans >= 100000:
            display.blit(full2,[96,288])
            
            
        #third bleachers   
        if fans >= 100000 and fans < 110000 and bleachers_upgrade.level == 3:
            display.blit(q2,[96,352])
            
        if fans >= 110000 and fans < 125000:
            display.blit(half2,[96,352])
            
        if fans >= 125000 and fans < 150000:
            display.blit(fq2,[96,352])
            
        if fans >= 150000:
            display.blit(full2,[96,352])
            
        money_text = font2.render(str(money),False,(0,0,0))
            
        #shows buttons  
        if to_men.active == False:
            display.blit(to_menu,[to_men.x,to_men.y])
        else:
            display.blit(to_menu_active,[to_men.x,to_men.y])
            
        if to_de.active == False:
            display.blit(to_desktop,[to_de.x,to_de.y])
        else:
            display.blit(to_desktop_active,[to_de.x,to_de.y])
            
        if upgrade.active == False:
            display.blit(upgrades,[upgrade.x,upgrade.y])
        else:
            display.blit(upgrades_active,[upgrade.x,upgrade.y])
            
            
        display.blit(fan_icon,[0,0])
        display.blit(fan_counter,[40,0])
        display.blit(money_icon,[0,32])
        display.blit(money_text,[40,32])
        
        display.blit(cursor,[mouse[0],mouse[1]-5])

                
        clock.tick(FPS)
        pygame.display.update()
        
    sys.exit()


def defeat():
    global running
    global stats
    global agression
    global to_de
    global fans
    global max_fans
    global cont
    global player
    global fan_multiplier
    global money
    global to_men
    global upgrade
    global slot1
    global slot2
    global slot3
    global max_timer
    
    to_de.x = display_width/2 - (to_de.w/2)
    to_de.y = 450
    
    upgrade.x = ((display_width/2)-(upgrade.w/2))
    upgrade.y = 350    
    
    to_men.y = 250
    
    player.mode = "idle"
    
    
    fans = 0
    max_fans = 50000
    cont = 0
    player.max_shield = 0
    fan_multiplier = 1
    enemy.max_shield = 0
    max_timer = 875
    money = 0
    slot1 = "open"
    slot2 = "open"
    slot3 = "open"
    fan_mult.level = 1
    fan_mult.price = 20000
    shield_upgrade.level = 0
    shield_upgrade.price = 10000
    bleachers_upgrade.level = 1
    bleachers_upgrade.price = 70000

    save()
    
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                
        mouse = pygame.mouse.get_pos()
        click = pygame.mouse.get_pressed()
                
        #buttons            
        if to_men.x+to_men.w > mouse[0] > to_men.x and to_men.y+to_men.h > mouse[1] > to_men.y:
            to_men.active = True
            if click[0] == 1:
                main_menu()
            
        else:
            to_men.active = False
            
            
        if to_de.x+to_de.w > mouse[0] > to_de.x and to_de.y+to_de.h > mouse[1] > to_de.y:
            to_de.active = True
            if click[0] == 1:
                running = False
            
        else:
            to_de.active = False
            
            
        if upgrade.x+upgrade.w > mouse[0] > upgrade.x and upgrade.y+upgrade.h > mouse[1] > upgrade.y:
            upgrade.active = True
            if click[0] == 1:
                upgrade_menu()
            
        else:
            upgrade.active = False
                
                
        display.blit(arena_defeat,[0,0])
        
        #shows the crowd
        if fans < 10000:
            display.blit(q,[96,224])
            
        if fans >= 10000 and fans < 25000:
            display.blit(half,[96,224])
            
        if fans < 50000 and fans >= 25000:
            display.blit(fq,[96,224])
            
        if fans >= 50000:
            display.blit(full,[96,224])     
                
                
        #second bleachers   
        if fans > 50000 and fans < 60000:
            display.blit(q2,[96,288])
            
        if fans >= 60000 and fans < 85000:
            display.blit(half2,[96,288])
            
        if fans >= 85000 and fans < 100000:
            display.blit(fq2,[96,288])
            
        if fans >= 100000:
            display.blit(full2,[96,288])
            
        #shows buttons 
        if to_men.active == False:
            display.blit(to_menu,[to_men.x,to_men.y])
        else:
            display.blit(to_menu_active,[to_men.x,to_men.y])
            
        if to_de.active == False:
            display.blit(to_desktop,[to_de.x,to_de.y])
        else:
            display.blit(to_desktop_active,[to_de.x,to_de.y])
            
        if upgrade.active == False:
            display.blit(upgrades,[upgrade.x,upgrade.y])
        else:
            display.blit(upgrades_active,[upgrade.x,upgrade.y])
            
            
        display.blit(cursor,[mouse[0],mouse[1]-5])
            
                
        clock.tick(FPS)
        pygame.display.update()
        
    sys.exit()

def loop():
    global myfont
    global running
    global fans
    global clinks
    global jabs
    global max_timer
    global timer
    global cont
    global agression  
    global slot2
    global slot2
    global slot3
    global player
    global money_text
    global fan_counter
    global flex_seal
    
    timer = max_timer
    
    flex_seal = 0
    
    jabs = 0
    clinks = 0
    
    player.x = 32
    enemy.x = display_width-(64*2)
    player.mode == "idle"
    enemy.mode == "idle"
    
    load()
    
    player.max_shield = shield_upgrade.level
    player.shield = player.max_shield
    
    enemy.shield = enemy.max_shield
    
    pygame.mixer.music.play(-1)
    
    cont += 1
    
    save()
    
    #agression = random.randint(1,2)
    agression = 1
    predict = 0    
    
    flex_tape = 1
    flex_tape2 = 1
    
    r_down = False
    l_down = False   
    
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                
            if event.type == pygame.KEYDOWN:
                #blocking
                if event.key == pygame.K_s:
                    player.block_down()
                if event.key == pygame.K_w:
                    player.block_up()
                    
                if player.mode == "block down":
                    player.lunge_reset = 1
                    if event.key == pygame.K_d:
                        if player.lunge_reset == 1:
                            player.attack_down()
                            jabs += 1
                        
                if player.mode == "block up":
                    player.lunge_reset = 1
                    if event.key == pygame.K_d:
                        if player.lunge_reset == 1:
                            player.attack_up()
                            jabs += 1
                 
                #walking   
                if event.key == pygame.K_a:
                    l_down = True
                    r_down = False
                    
                if event.key == pygame.K_d:
                    r_down = True
                    l_down = False
                    
                    
            if event.type == pygame.KEYUP:
                #returning to neutral stance
                if event.key == pygame.K_s and player.mode == "block down":
                    player.lunge_reset = 1
                    player.mode = "idle"
                if event.key == pygame.K_w and player.mode == "block up":
                    player.lunge_reset = 1
                    player.mode = "idle"
                    
                if event.key == pygame.K_a:
                    l_down = False
                    
                if event.key == pygame.K_d:
                    r_down = False
           
        #game's timer   
        text = myfont.render(str(timer/25),False,(0,0,0))
        
        if timer > 0:
            timer -= 1
            
        if timer == 0:
            defeat()
            timer = -3   
            
            
        fan_counter = font2.render(str(fans),False,(0,0,0))
        
        
        shield_text = font2.render(str(player.shield),False,(0,0,0))
        bad_shield_text = font2.render(str(enemy.shield),False,(0,0,0))
        money_text = font2.render(str(money),False,(0,0,0))
                    
        #=============================================================================enemy AI==================================================================#  
        
        #handles the AI's block duration 
        if battle_info.block_time > 0:
            battle_info.block_time -= 1
            
        #handles the idle check
        if battle_info.idle_check > 0:
            battle_info.idle_check -= 1
            
        if battle_info.idle_check == 0:
            battle_info.attack = random.randint(1,2)
            battle_info.idle_check = -3
            
            
        #attacking, not as a reaction to the player's movements
        if battle_info.attack == 1 and agression == 1:
            enemy.attack_up()
            battle_info.attack = 0
            
        if battle_info.attack == 2 and agression == 1:
            enemy.attack_down()
            battle_info.attack = 0
            
        if player.x + 64 > enemy.x - 100 and agression == 0 and flex_tape2 == 1:
            battle_info.idle_check = 15
            flex_tape2 = 0
            
        if enemy.x <= player.x + 64 and enemy.mode == "idle":
            agression = 2
        
        #agressive combat
        if agression == 1:
            if player.x + 64 < enemy.x - 100:
                if enemy.mode == "idle":
                    enemy.x_change = -5
                
            else:
                enemy.x_change = 0
                
        
        if player.x + 64 >= enemy.x - 100 and agression == 1 and flex_tape2 == 1:
            battle_info.idle_check = 15
            flex_tape2 = 0
             
        
        if player.x + 64 >= enemy.x - 100:        
            if player.mode == "block up" and flex_tape == 1:
                battle_info.idle_check = -3
                predict = random.randint(1,2)
                flex_tape = 0
                agression = 0
            if player.mode == "block down" and flex_tape == 1:
                battle_info.idle_check = -3
                predict = random.randint(1,2)
                flex_tape = 0
                agression = 0
                
                
        if agression == 0 and player.x + 64 < enemy.x - 100:
            agression = 1
            
            
        #defensive combat
        if agression == 4:
            flex_tape = 1
            if player.x + 64 >= enemy.x - 100:
                if player.mode == "block up" and flex_tape == 1:
                    enemy.block_up()
                    battle_info.block_time = 7
                    flex_tape = 0
                    agression = 1
                if player.mode == "block down" and flex_tape == 1:
                    enemy.attack_up()
                    flex_tape = 0
                    agression = 1
                    
                if player.mode == "attack up":
                    enemy.x_change = 0
                    battle_info.back_up_time = -3
                    enemy.block_up()
                    battle_info.block_time = 7
                    
                if player.mode == "attack down":
                    enemy.x_change = 0
                    battle_info.back_up_time = -3
                    enemy.block_down()
                    battle_info.block_time = 7
            
            
        #getting hit
        if player.x + 64 >= enemy.x:
            if player.mode == "attack up":
                if enemy.mode == "idle" or enemy.mode == "block down" or enemy.mode == "attack down":
                    if enemy.shield == 0:
                        calculate_money()
                        calculate_fans()
                        save()
                        victory()
                        
                    else:
                        pygame.mixer.Sound.play(shweep)
                        player.mode = "idle"
                        player.velocity = 0                        
                        enemy.shield -= 1
                    
            if player.mode == "attack down":
                if enemy.mode == "idle" or enemy.mode == "block up":
                    if enemy.shield == 0:
                        calculate_money()
                        calculate_fans()  
                        save()
                        victory()
                        
                    else:
                        pygame.mixer.Sound.play(shweep)
                        player.mode = "idle"
                        player.velocity = 0
                        enemy.shield -= 1
                
                
               
        #non agressive combat
        if agression == 2 or agression == 3:
            flex_tape2 = 1
            battle_info.back_up_time = random.randint(10,25)
            agression = 4
            
        if battle_info.back_up_time == 0:
            agression = 1
            
        if battle_info.back_up_time > 0 and enemy.mode == "idle":
            enemy.x_change = 5
            battle_info.back_up_time -= 1
                
        #blocking attacks
        if player.x + 64 > enemy.x - 100 and agression == 1:
            if player.mode == "attack up":
                if enemy.mode == "idle":
                    agression = 2
                    battle_info.block_time = 17
            if player.mode == "attack down":
                if enemy.mode == "idle":
                    enemy.attack_up()
                    
        #switching to idle from blocks
        if enemy.mode == "block down" or enemy.mode == "block up":
            if battle_info.block_time == 0:
                agression = 4
                flex_tape2 = 0
                battle_info.idle_check = -3
                enemy.lunge_reset = 0
                enemy.mode = "idle"
                battle_info.idle_check = -3
                battle_info.block_time = -3
           
        #predicts whether you will attack up or just block
        if enemy.velocity == 0:
            if predict == 1 and player.mode == "block up":
                battle_info.idle_check = -3
                enemy.block_up()
                battle_info.block_time = 17
                predict = 0
                enemy.x_change = 0
                
            if predict == 2 and player.mode == "block up" and agression == 0:
                battle_info.idle_check = -3
                enemy.attack_down()
                predict = 0
                enemy.x_change = 0
                
                
            if predict == 1 and player.mode == "block down":
                battle_info.idle_check = -3
                enemy.block_down()
                battle_info.block_time = 17
                predict = 0
                enemy.x_change = 0
            
            if predict == 2 and player.mode == "block down" and agression == 0:
                battle_info.idle_check = -3
                enemy.attack_up()
                predict = 0
                enemy.x_change = 0
        
        #enemy's velocity
        if enemy.velocity < 0:
            enemy.velocity += 1
            
        enemy.x += enemy.velocity
        
        if enemy.velocity > -5 and enemy.mode == "attack up":
            enemy.mode = "idle"
            enemy.lunge_reset = 1
            agression = random.randint(1,2)
            flex_tape2 = 1
            
            
        if enemy.velocity > -5 and enemy.mode == "attack down":
            enemy.mode = "idle"
            enemy.lunge_reset = 1 
            agression = random.randint(1,2)
            flex_tape2 = 1
            
            
        #handles the AI's x position/ boundaries
        enemy.x += enemy.x_change
        
        if enemy.x < player.x + 64:
            enemy.x += 5
            enemy.velocity = 0
            
        if enemy.x + 64 > 1048:
            enemy.x = 1048 - 64        
        
        #=======================================================================================================================================================#
                    
           
        #stops character from moving         
        if l_down == True and r_down == False:
            player.x_change = -5
            
        if r_down == True and l_down == False:
            player.x_change = 5
            
        elif r_down == False and l_down == False:
            player.x_change = 0
            
            
        if player.mode == "idle":
            player.x += player.x_change
            
        if player.x > enemy.x - 64:
            player.x -= 5
            player.velocity = 0
            
            
        #sounds
        if player.x + 66 >= enemy.x - 5 and player.mode == "attack up":
            if enemy.mode == "block up":
                pygame.mixer.Sound.play(clink)
                clinks += 1
                player.mode = "idle"
                agression = 2  #sets agression so that the AI backs up after blocking
                enemy.lunge_reset = 0
                
            if enemy.mode == "attack up":
                pygame.mixer.Sound.play(clink)
                clinks += 1
           
                
        if player.x + 66 >= enemy.x - 5 and player.mode == "attack down":
            if enemy.mode == "block down":
                pygame.mixer.Sound.play(clink)
                clinks += 1
                player.mode = "idle"
                agression = 2
                enemy.lunge_reset = 0
                
            if enemy.mode == "attack down":
                pygame.mixer.Sound.play(clink)
                clinks += 1
                
                
                
        if player.x + 66 >= enemy.x - 5 and enemy.mode == "attack up":
            if player.mode == "block up":
                pygame.mixer.Sound.play(clink)
                clinks += 1
                
            if player.mode == "attack up":
                pygame.mixer.Sound.play(clink)
                clinks += 1
           
                
        if player.x + 66 >= enemy.x - 5 and enemy.mode == "attack down":
            if player.mode == "block down":
                pygame.mixer.Sound.play(clink)
                clinks += 1
                
            if player.mode == "attack down":
                pygame.mixer.Sound.play(clink)
                clinks += 1
                
            
        #getting hit   
        if enemy.x <= player.x + 67:
            if enemy.mode == "attack up":
                if player.mode == "idle" or player.mode == "block down" or player.mode == "attack down":
                    if player.shield == 0:
                        defeat()
                        
                    else:
                        pygame.mixer.Sound.play(shweep)
                        enemy.velocity = 0
                        enemy.mode = "idle"
                        agression = 2
                        player.shield -= 1
                    
            if enemy.mode == "attack down":
                if player.mode == "idle" or player.mode == "block up":
                    if player.shield == 0:
                        defeat()
                        
                    else:
                        pygame.mixer.Sound.play(shweep)
                        enemy.velocity = 0
                        enemy.mode = "idle"
                        agression = 2                        
                        player.shield -= 1
                    
        #velocity for attacks            
        if player.velocity > 0:
            player.velocity -= 1
            
        if player.velocity < 5 and player.mode == "attack up":
            player.mode = "idle"
            player.lunge_reset = 1
            
            
        if player.velocity < 5 and player.mode == "attack down":
            player.mode = "idle"
            player.lunge_reset = 1
                    
                    
        player.x += player.velocity
        
        #displaying everything to the screen        
        display.blit(battle_area,[0,0])
        
        if player.velocity > 0:
            player.x_change = 0
        
        if player.x < 32:
            player.x += 5
        
        if player.mode == "idle":
            display.blit(sword,[player.x,player.y])
        if player.mode == "block up":
            display.blit(sword_up,[player.x,player.y])
        if player.mode == "block down":
            display.blit(sword_down,[player.x,player.y])
        if player.mode == "attack up":
            display.blit(jab_up,[player.x,player.y])
        if player.mode == "attack down":
            display.blit(jab_down,[player.x,player.y]) 
            
        if enemy.mode == "idle":
            display.blit(sword2,[enemy.x,enemy.y])
        if enemy.mode == "block up":
            display.blit(sword_up2,[enemy.x,enemy.y])
        if enemy.mode == "block down":
            display.blit(sword_down2,[enemy.x,enemy.y])
        if enemy.mode == "attack up":
            display.blit(jab_up2,[enemy.x,enemy.y])
        if enemy.mode == "attack down":
            display.blit(jab_down2,[enemy.x,enemy.y])  
            
            
        if fans > max_fans:
            fans = max_fans
            
        #shows the crowd
        if fans < 10000:
            display.blit(q,[96,224])
            
        if fans >= 10000 and fans < 25000:
            display.blit(half,[96,224])
            
        if fans < 50000 and fans >= 25000:
            display.blit(fq,[96,224])
            
        if fans >= 50000:
            display.blit(full,[96,224])    
                
                
        #second bleachers   
        if fans >= 50000 and fans < 60000 and bleachers_upgrade.level == 2:
            display.blit(q2,[96,288])
            
        if fans >= 60000 and fans < 85000:
            display.blit(half2,[96,288])
            
        if fans >= 85000 and fans < 100000:
            display.blit(fq2,[96,288])
            
        if fans >= 100000:
            display.blit(full2,[96,288])
            
            
        #third bleachers   
        if fans >= 100000 and fans < 110000 and bleachers_upgrade.level == 3:
            display.blit(q2,[96,352])
            
        if fans >= 110000 and fans < 125000:
            display.blit(half2,[96,352])
            
        if fans >= 125000 and fans < 150000:
            display.blit(fq2,[96,352])
            
        if fans >= 150000:
            display.blit(full2,[96,352])
                
            
        display.blit(text,[500,50])
        
        display.blit(fan_icon,[0,0])
        display.blit(fan_counter,[40,0])
        display.blit(money_icon,[0,32])
        display.blit(money_text,[40,32])
        
        
        if fan_mult.level > 1:
            if slot1 == "fan mult":
                display.blit(fan_mult_icon,[0,64])
                
            if slot2 == "fan mult":
                display.blit(fan_mult_icon,[0,96])
                
            if slot3 == "fan mult":
                slot2 = "shield"
                display.blit(fan_mult_icon,[0,128])
            
        if shield_upgrade.level > 0:   
            if slot1 == "shield":
                display.blit(icoon,[0,64])
                display.blit(shield_text,[40,64])
                
            if slot2 == "shield":
                display.blit(icoon,[0,96])
                display.blit(shield_text,[40,96])
                
            if slot3 == "shield":
                slot2 = "fan mult"
                display.blit(icoon,[0,128])
                display.blit(shield_text,[40,128])
                
                
        if enemy.max_shield > 0:
            if slot1 == "bad shield":
                display.blit(shield_icon2,[0,64])
                display.blit(bad_shield_text,[40,64])
                
        clock.tick(FPS)
        pygame.display.update()
        
    sys.exit()
    

