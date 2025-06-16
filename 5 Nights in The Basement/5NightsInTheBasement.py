import pygame
import sys
from image_renderer import *
from audio_renderer import *
import random
import pickle

display = pygame.display.set_mode([1080,720],pygame.FULLSCREEN)
pygame.display.set_caption("5 Nights in the Basement")
pygame.display.set_icon(icon)

clock = pygame.time.Clock()
FPS = 30

running = True
intro_screen = True
in_game = False

time = 50
times = 1
time2 = 0
times2 = 0
time3 = 0
times3 = 0
time4 = 0
times4 = 0
stars = 0
mode = 0


def win():
    global time
    global times
    global stars
    global mode

    pygame.mixer.Sound.stop(humid_sound)
    pygame.mixer.Sound.play(yay)

    timer = 125

    rightx = 1038
    righty = 20

    while running:

        pickle_out = open("save.pickle","wb")
        pickle.dump(stars, pickle_out)
        pickle_out.close()

        if timer > 0:
            timer -= 1


        if timer != 0:
            display.blit(victory,[0,0])

            if stars == 0:
                stars = 1

                pickle_out = open("save.pickle","wb")
                pickle.dump(stars, pickle_out)
                pickle_out.close()

            if mode == 2:
                if stars == 1:
                    stars = 2
                    mode = 0

                    pickle_out = open("save.pickle","wb")
                    pickle.dump(stars, pickle_out)
                    pickle_out.close()



        pygame.display.update()

        if timer == 0:
            intro()


class button4(object):
    def __init__(self):
        self.x = 300
        self.y = 250
        self.active = False
        self.w = 178


class button5(object):
    def __init__(self):
        self.x = 300
        self.y = 250
        self.active = False
        self.w = 178


class button6(object):
    def __init__(self):
        self.x = 300
        self.y = 250
        self.active = False
        self.w = 178


class button7(object):
    def __init__(self):
        self.x = 300
        self.y = 250
        self.active = False
        self.w = 178


class button8(object):
    def __init__(self):
        self.x = 300
        self.y = 250
        self.active = False
        self.w = 178


def extra():
    global running

    will_time = -3
    will_times = -3
    will_face = 0

    chup_time = -3
    chup_times = -3
    chup_face = 0

    selected = 0

    button4.x = 40
    button4.y = 50
    button4.active = False

    button5.x = 230
    button5.y = 50
    button5.active = False

    button6.x = 420
    button6.y = 50
    button6.active = False

    button7.x = 610
    button7.y = 50
    button7.active = False

    button8.x = 800
    button8.y = 50
    button8.active = False

    while running:

        if selected == 0:
            display.blit(extra_menu,[0,0])

        if selected == 1:
            display.blit(juda_select,[0,0])

        if selected == 2:
            display.blit(dylan_select,[0,0])

        if selected == 3 and will_time == -3:
            display.blit(will_select,[0,0])
            will_time = 0
            will_times = 0

        if selected == 4:
            display.blit(will_select1,[0,0])

        if selected == 5:
            display.blit(will_select2,[0,0])

        if selected == 6:
            display.blit(will_select3,[0,0])

        if selected == 7:
            display.blit(will_select4,[0,0])
            pygame.mixer.Sound.stop(scare)

        if selected == 8 and chup_time == -3:
            display.blit(chup_select,[0,0])
            chup_time = 0
            chup_times = 0

        if selected == 9:
            display.blit(chup_select1,[0,0])

        if selected == 10:
            display.blit(chup_select2,[0,0])

        if selected == 11:
            display.blit(chup_select3,[0,0])
            pygame.mixer.Sound.stop(scare)

        display.blit(back,[button4.x,button4.y])
        display.blit(judah,[button5.x,button5.y])
        display.blit(dylan_button,[button6.x,button6.y])
        display.blit(will_button,[button7.x,button7.y])
        display.blit(chup_button,[button8.x,button8.y])


        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False


        if will_time == 0 and will_times == 0:
            selected = 4
            will_time = 5
            will_times = 1
            pygame.mixer.Sound.stop(humid_sound)
            pygame.mixer.Sound.stop(laugh)
            pygame.mixer.Sound.play(scare)

        if will_time == 0 and will_times == 1:
            selected = 5
            will_time = 5
            will_times = 2

        if will_time == 0 and will_times == 2:
            selected = 6
            will_time = 5
            will_times = 3

        if will_time == 0 and will_times == 3:
            selected = 7
            will_time = -3
            will_times = -3




        if chup_time == 0 and chup_times == 0:
            selected = 9
            chup_time = 5
            chup_times = 1
            pygame.mixer.Sound.play(scare)

        if chup_time == 0 and chup_times == 1:
            selected = 10
            chup_time = 5
            chup_times = 2

        if chup_time == 0 and chup_times == 2:
            selected = 11
            chup_time = -3
            chup_times = -3



        if will_time > 0:
            will_time -= 1

        if chup_time > 0:
            chup_time -= 1


        clock.tick(FPS)

        pygame.display.update()

        mouse = pygame.mouse.get_pos()
        click = pygame.mouse.get_pressed()


        if button4.x+178 > mouse[0] > button4.x and  button4.y+46 > mouse[1] > button4.y:
            button4.active = True
            if click[0] == 1:
                intro()


        else:
            button4.active = False


        if button5.x+178 > mouse[0] > button5.x and  button5.y+46 > mouse[1] > button5.y:
            button5.active = True
            if click[0] == 1:
                selected = 1


        else:
            button5.active = False


        if button6.x+178 > mouse[0] > button6.x and  button6.y+46 > mouse[1] > button6.y:
            button6.active = True
            if click[0] == 1:
                selected = 2


        else:
            button6.active = False


        if button7.x+178 > mouse[0] > button7.x and  button7.y+46 > mouse[1] > button7.y:
            button7.active = True
            if click[0] == 1:
                selected = 3


        else:
            button7.active = False


        if button8.x+178 > mouse[0] > button8.x and  button8.y+46 > mouse[1] > button8.y:
            button8.active = True
            if click[0] == 1:
                selected = 8


        else:
            button8.active = False


        if button4.active == True:
            display.blit(active_back,[button4.x,button4.y])
        if button4.active == False:
            display.blit(back,[button4.x,button4.y])


        if button5.active == True:
            display.blit(judah,[button5.x,button5.y])
        if button5.active == False:
            display.blit(judah,[button5.x,button5.y])

        if button6.active == True:
            display.blit(dylan_button,[button6.x,button6.y])
        if button6.active == False:
            display.blit(dylan_button,[button6.x,button6.y])


    sys.exit()


class button(object):
    def __init__(self):
        self.x = 300
        self.y = 250
        self.active = False
        self.w = 178


class button2(object):
    def __init__(self):
        self.x = 300
        self.y = 280
        self.active = False
        self.w = 178

class button3(object):
    def __init__(self):
        self.x = 300
        self.y = 280
        self.active = False
        self.w = 178


def intro():
    global intro_screen
    global time
    global times
    global save
    global stars

    button.x = 10
    button.y = 400
    button.active = False

    button2.x = 10
    button2.y = 460
    button2.active = False

    button3.x = 10
    button3.y = 520
    button3.active = False

    pygame.mixer.Sound.stop(ambience)
    pygame.mixer.Sound.stop(spirits)
    pygame.mixer.Sound.stop(laugh)
    pygame.mixer.Sound.stop(humid_sound)
    pygame.mixer.Sound.stop(rec1)
    pygame.mixer.Sound.stop(rec2)
    pygame.mixer.Sound.stop(rec3)
    pygame.mixer.Sound.stop(rec4)
    pygame.mixer.Sound.stop(rec5)
    pygame.mixer.Sound.stop(button_click)
    pygame.mixer.Sound.stop(on)
    pygame.mixer.Sound.stop(off)
    pygame.mixer.Sound.stop(snap)
    pygame.mixer.Sound.stop(scare)


    pickle_in = open("save.pickle","rb")
    stars = pickle.load(pickle_in)


    light = 0

    pygame.mixer.music.play(-1)


    while intro_screen:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                intro_screen = False


            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    intro_screen = False


        if intro_screen == True:
            if time < 1 and times == 1:
                times = 2
                light = 0
                time = 50

            if time < 1 and times == 2:
                times = 3
                light = 1
                time = 10

            if time < 1 and times == 3:
                times = 4
                light = 0
                time = 10

            if time < 1 and times == 4:
                times = 5
                light = 1
                time = 25

            if time < 1 and times == 5:
                times = 6
                light = 2
                time = 50

            if time < 1 and times == 6:
                times = 7
                light = 1
                time = 10

            if time < 1 and times ==7:
                times = 8
                light = 3
                time = 25

            if time < 1 and times == 8:
                times = 9
                light = 1
                time = 10

            if time < 1 and times == 9:
                times = 10
                light = 0
                time = 50

            if time < 1 and times == 10:
                times = 11
                light = 1
                time = 10

            if time < 1 and times == 11:
                times = 12
                light = 0
                time = 10

            if time < 1 and times == 12:
                times = 13
                light = 1
                time = 25

            if time < 1 and times == 13:
                times = 1
                light = 0
                time = 25


        if time > 0:
            time -= 1


        clock.tick(FPS)

        mouse = pygame.mouse.get_pos()
        click = pygame.mouse.get_pressed()

        if button.x+178 > mouse[0] > button.x and  button.y+46 > mouse[1] > button.y:
            button.active = True
            if click[0] == 1:
                loop()


        else:
            button.active = False


        if stars > 0:
            if button2.x+178 > mouse[0] > button2.x and  button2.y+46 > mouse[1] > button2.y:
                button2.active = True
                if click[0] == 1:
                    loop2()


            else:
                button2.active = False



        if stars == 2:
            if button3.x+178 > mouse[0] > button3.x and  button3.y+46 > mouse[1] > button3.y:
                button3.active = True
                if click[0] == 1:
                    extra()


            else:
                button3.active = False


        if light == 0:
            display.blit(light_on,[0,0])
        if light == 1:
            display.blit(light_off,[0,0])
        if light == 2:
            display.blit(eyes,[0,0])
        if light == 3:
            display.blit(eyes2,[0,0])


        if stars == 1:
            display.blit(star1,[280,0])

        if stars == 2:
            display.blit(star2,[280,0])


        display.blit(title,[6,10])

        if button.active == True:
            display.blit(active_new,[button.x,button.y])
        if button.active == False:
            display.blit(new_game,[button.x,button.y])

        if stars > 0:
            if button2.active == True:
                display.blit(active_hard,[button2.x,button2.y])
            if button2.active == False:
                display.blit(hard,[button2.x,button2.y])


        if stars == 2:
            if button3.active == True:
                display.blit(active_extras,[button3.x,button3.y])
            if button3.active == False:
                display.blit(extras,[button3.x,button3.y])

        pygame.display.update()

    sys.exit()

time2 = 0
times2 = 0
time3 = 0
times3 = 0
chup_reset = 75
chup_attack = -3
chup_ai = random.randint(1,3)
dylan_reset = 100
dylan_attack = -2
dylan_ai = random.randint(1,3)
will_reset = 75
will_attack = -2
will_ai = random.randint(1,5)

def loop():
    global running
    global night
    global time2
    global times2
    global time3
    global times3
    global time4
    global times4
    global chup_ai
    global chup_attack
    global chup_reset
    global dylan_reset
    global dylan_attack
    global dylan_ai
    global will_reset
    global will_attack
    global will_ai
    global stars

    pickle_out = open("save.pickle","wb")
    pickle.dump(stars, pickle_out)
    pickle_out.close()

    mode = 1

    rightx = 1038
    righty = 20
    turnx = 300
    turny = 646


    chup_scare_time = -3
    chup_scare_times = -3

    will_scare_time = -3
    will_scare_times = -3


    time2 = 0
    times2 = 0
    time3 = 0
    times3 = 0
    chup_reset = 75
    chup_attack = -3
    chup_ai = random.randint(1,2)
    location = 1
    dylan_reset = 100
    dylan_attack = -2
    dylan_ai = random.randint(1,3)
    flash_time = -3
    dylan_gone = True
    juda_ai = 1
    juda_reset = 75
    juda_attack = -2
    juda_wait = -3
    dylan_wait = -3
    recorder_reset = -3
    will_reset = 125
    will_attack = -2
    will_ai = random.randint(1,5)

    ramos_and_ramos = 0
    celino_and_barnes = 0
    william_mattar = 0

    agrippa_voice = 0

    amb_wait = 0

    show = 0
    show2 = 0

    scare_face = 0
    will_scare_face = 0

    chup_show = 0

    call = 0

    alarm_time = 750
    alarm = 12


    recorder_time = -3
    recorder_times = 0
    recorder_break = 1


    humid = False

    look_time = -3

    meter_x = 0
    meter_y = 0

    comp_life = 500

    shine = False

    flash_x = -920
    flash_y = -5

    in_game = True
    press = 0

    location = 1
    lights = True

    pygame.mixer.music.stop()

    map_x = -10
    map_y = 0

    if in_game == True:
        while running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False

                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_SPACE:
                        if location == 3:
                            pygame.mixer.Sound.play(button_click)
                            press = 1
                            comp_life = 500
                            meter_x = 0

                            if chup_ai == 3 or chup_ai == 4 or chup_ai == 5:
                                chup_scare_time = 0
                                chup_scare_times = 0
                                chup_ai = 18



                        if location == 1:
                            if recorder_break == 1:
                                recorder_time = 5
                                recorder_times = 1
                                pygame.mixer.Sound.play(button_click)
                                will_attack = -2
                                will_ai = 1

                            if recorder_break == 2 or recorder_break == 3:
                                ramos_and_ramos = 1
                                recorder_time = 5
                                recorder_times = 6


                        if location == 5:
                            pygame.mixer.Sound.play(on)
                            shine = True
                            pygame.mixer.Sound.stop(laugh)


                    if event.key == pygame.K_ESCAPE:
                        running = False


                if event.type == pygame.KEYUP:
                    if event.key == pygame.K_SPACE:
                        if location == 5:
                            pygame.mixer.Sound.play(off)
                            shine = False


            mouse = pygame.mouse.get_pos()
            click = pygame.mouse.get_pressed()


            if location == 1:
                if lights == True:
                    if turnx+566 > mouse[0] > turnx and  turny+74 > mouse[1] > turny:
                        times2 = 1
                        location = 2
                        time2 = 5


            if chup_scare_time == 0 and chup_scare_times == 0:
                if lights == True:
                    scare_face = 1
                    chup_scare_times = 1
                    chup_scare_time = 5
                    pygame.mixer.Sound.stop(humid_sound)
                    pygame.mixer.Sound.stop(laugh)
                    pygame.mixer.Sound.play(scare)

                if lights == False:
                    scare_face = -5
                    chup_scare_times = -5
                    chup_scare_time = 5
                    pygame.mixer.Sound.stop(humid_sound)
                    pygame.mixer.Sound.stop(laugh)
                    pygame.mixer.Sound.play(scare)
                    lights = 1



            if chup_scare_time == 0 and chup_scare_times == 1:
                scare_face = 2
                chup_scare_times = 2
                chup_scare_time = 5


            if chup_scare_time == 0 and chup_scare_times == 2:
                scare_face = 3
                chup_scare_times = 3
                chup_scare_time = 10


            if chup_scare_time == 0 and chup_scare_times == 3:
                intro()




            if chup_scare_time == 0 and chup_scare_times == -5:
                scare_face = -6
                chup_scare_times = -6
                chup_scare_time = 5


            if chup_scare_time == 0 and chup_scare_times == -6:
                scare_face = -7
                chup_scare_times = -7
                chup_scare_time = 10


            if chup_scare_time == 0 and chup_scare_times == -7:
                intro()



            if chup_scare_time > 0:
                chup_scare_time -= 1


            if time2 < 1 and times2 == 1:
                location = 3

            if will_reset == 0:
                will_ai = random.randint(1,5)

            if will_ai == 1 or will_ai == 2:
                will_reset = 125
                will_ai = 7


            if location == 3:
                if press == 0:
                    if turnx+566 > mouse[0] > turnx and  turny+74 > mouse[1] > turny:
                        times2 = 2
                        location = 2
                        time2 = 5



            if time2 < 1 and times2 == 2:
                location = 1
                map_x = 0
                times2 = 0



            if time4 < 1 and times4 == 2:
                location = 5

            if time4 == 0 and times4 == 3:
                location = 1
                times4 = 0


            if juda_wait > 0:
                juda_wait -= 1


            if press == 1:
                times3 = 1
                time3 = 10
                press = 2


            if will_ai == 1 or will_ai == 2 or will_ai == 7:
                show = 0


            if time3 == 0 and times3 == 1:
                press = 0
                times3 = 0


            if amb_wait > 0:
                amb_wait -= 1

            if amb_wait == 0:
                pygame.mixer.Sound.play(ambience)
                amb_wait = 150


            if will_reset > 0:
                will_reset -= 1


            if will_attack > 0:
                will_attack -= 1


            if juda_reset > 0:
                juda_reset -= 1


            if juda_attack > 0:
                juda_attack -= 1


            if juda_reset == 0:
                juda_ai = random.randint(1,3)


            if juda_ai == 1:
                juda_reset = 125
                juda_ai = 7


            if time2 > 0:
                time2 -= 1

            if time3 > 0:
                time3 -= 1

            if time4 > 0:
                time4 -= 1

            if comp_life > 0:
                comp_life -= 1
                meter_x -= 1


            if comp_life == 0:
                lights = False

            if alarm_time > 0:
                alarm_time -= 1


            if dylan_attack < 1 and shine == True:
                dylan_attack = -3
                dylan_ai = 3


            call = random.randint(1,9)


            if alarm == 12 and alarm_time == 0:
                if call == 1:
                    pygame.mixer.Sound.play(spirits)

                alarm = 1
                alarm_time = 750

            if alarm == 1 and alarm_time == 0:
                if call == 2:
                  pygame.mixer.Sound.play(spirits)

                alarm = 2
                alarm_time = 750

            if alarm < 6 and alarm_time == 0:
                alarm += 1
                alarm_time = 750



            if chup_reset < 1 and chup_reset > -3 and humid == True:
                chup_ai = random.randint(1,5)

            if chup_reset < 1 and chup_reset > -3 and humid == False:
                chup_ai = random.randint(2,5)


            if dylan_reset < 1 and dylan_reset > -3:
                if dylan_ai == 1 or dylan_ai == 2 or dylan_ai == 3 or dylan_ai == 7:
                    dylan_ai = random.randint(1,6)

            if dylan_reset < 1 and dylan_attack == -3:
                dylan_ai = random.randint(1,3)
                dylan_attack = -2


            if chup_reset == -1 and humid == True:
                chup_ai = random.randint(1,2)


            if chup_reset == -1 and humid == False:
                chup_ai = 1


            if dylan_ai == 4 or dylan_ai == 5 or dylan_ai == 6:
                if shine == False:
                    if juda_ai == 2 or juda_ai == 3 or juda_ai == 8:
                        pygame.mixer.Sound.play(laugh)



                if dylan_attack == -2:
                    dylan_wait = 100
                    dylan_attack = -3

                if shine == False:
                    dylan_attack = 75

                if shine == True:
                    dylan_wait = 100


            if dylan_wait > 0:
                dylan_wait -= 1


            if recorder_time > 0:
                recorder_time -= 1


            if dylan_ai == 1 or dylan_ai == 2 or dylan_ai == 3 or dylan_ai == 7:
                dylan_wait = -2


            if dylan_wait == 0:
                humid = True


            if humid == True:
                dylan_ai = -5
                dylan_attack = -5
                dylan_reset = -5
                dylan_wait = -5


            if humid == True:
                recorder_break = -73
                pygame.mixer.Sound.play(humid_sound)



            if chup_ai == 1 or chup_ai == 2:
                chup_reset = 75
                chup_ai = 7

            if dylan_ai == 1 or dylan_ai == 2:
                dylan_reset = 75
                dylan_ai = 7


            if location == 5:
                if lights == True:
                    display.blit(dehumid,[0,0])

                    if dylan_ai == 4 or dylan_ai == 5 or dylan_ai == 6:
                        display.blit(dylan,[820,350])

                else:
                    display.blit(dehumid,[0,0])

                if shine == True:
                    display.blit(flash,[flash_x,flash_y])

                else:
                    display.blit(flash,[flash_x,flash_y])


                display.blit(left,[0,20])



            if shine == True:
                flash_x = -920
                flash_y = -5


            else:
                flash_x = -5
                flash_y = -5


            if location == 5:
                if lights == False:
                    location = 1

                if press == 0:
                    if 0+42 > mouse[0] > 0 and  20+366 > mouse[1] > 20:
                        if click[0] == 1:
                            times4 = 3
                            location = 4
                            time4 = 5


            if location == 4:
                if lights == True:
                    display.blit(turning,[0,0])
                if lights == False:
                    location = 1
                display.blit(right,[500,630])


            if chup_ai == 3 or chup_ai == 4 or chup_ai == 5:
                chup_show = 1


            if location == 3:
                if lights == True:
                    display.blit(computer,[0,0])
                    display.blit(turn,[turnx,turny])


                    if chup_ai == 3 or chup_ai == 4 or chup_ai == 5 and chup_attack == -3:

                        if chup_show  == 1:
                            display.blit(chup,[650,50])

                        chup_reset = -3
                        look_time = 0

                if chup_reset == -3:
                    chup_attack = 125
                    chup_reset = -4


            if lights == False:
                location = 1
                display.blit(turn,[turnx,turny])


            if chup_attack > 0 and look_time > 74:
                chup_show = 0
                chup_attack = -3
                chup_reset = -1
                look_time = -3



            if location == 2:
                if lights == True:
                    display.blit(computer_turn,[0,0])
                if lights == False:
                    location = 1

            if location == 1:
                if lights == True:
                    display.blit(basement,[map_x,map_y])
                    display.blit(turn,[turnx,turny])

                    if will_ai == 3 or will_ai == 4 or will_ai == 5:
                        show = 1
                        will_reset = -3
                        if humid == True:
                            will_attack = 125

                        if humid == False:
                            will_attack = 75

                        will_ai = 8


                    if juda_ai == 2 or juda_ai == 3:
                        #pygame.mixer.Sound.play(confused)
                        show2 = 1
                        juda_reset = -3



            if juda_ai == 2 or juda_ai == 3:
                if location == 1 and map_x > -85:
                    juda_attack = 75
                    juda_ai = 8


            if location == 1 and map_x > -85 and juda_ai == 8 and juda_attack == 0:
                show2 = 0
                juda_attack = -2
                juda_ai = 1
                juda_reset = 125



            if juda_attack == 0:
                if location != 1 or map_x < -84:
                    show2 = 0
                    juda_attack = -2
                    juda_ai = 1
                    juda_reset = 125
                    recorder_break = 2



                if lights == False:
                    display.blit(light_off,[map_x,map_y])
                    chup_scare_time = 5
                    chup_scare_times = -5
                shine = False

                if lights == True:
                    display.blit(turn,[turnx,turny])


            if will_attack == 0:
                if location == 1:
                    will_scare_time = 0
                    will_scare_times = 0
                    will_attack = -2

                if location == 3:
                    times2 = 2
                    location = 2
                    time2 = 5

                if location == 5:
                    times4 = 3
                    location = 4
                    time4 = 5




            if will_scare_time > 0:
                will_scare_time -= 1


            if will_scare_time == 0 and will_scare_times == 0:
                will_scare_face = 1
                will_scare_time = 5
                will_scare_times = 1
                pygame.mixer.Sound.stop(humid_sound)
                pygame.mixer.Sound.stop(laugh)
                pygame.mixer.Sound.play(scare)

            if will_scare_time == 0 and will_scare_times == 1:
                will_scare_face = 2
                will_scare_time = 5
                will_scare_times = 2

            if will_scare_time == 0 and will_scare_times == 2:
                will_scare_face = 3
                will_scare_time = 5
                will_scare_times = 3

            if will_scare_time == 0 and will_scare_times == 3:
                will_scare_face = 4
                will_scare_time = 10
                will_scare_times = 5

            if will_scare_time == 0 and will_scare_times == 5:
                intro()


            if show == 1:
                if location == 1:
                    display.blit(will,[map_x + 1170,200])


            if show2 == 1:
                if location == 1:
                    display.blit(juda,[map_x + 60,370])


            if location == 1:
                if mouse[0] >= 600:
                    map_x -= 15

                if mouse[0] <= 300:
                    map_x += 15

                if map_x + 1000 >= 800:
                    map_x -= 15

                else:
                    if lights == True:
                        display.blit(right,[rightx,righty])

                    if location == 1:
                        if press == 0:
                            if rightx+42 > mouse[0] > rightx and  righty+366 > mouse[1] > righty:
                                if click[0] == 1:
                                    times4 = 2
                                    location = 4
                                    time4 = 5



                if map_x <= -10:
                    map_x += 15


            if press == 1 or press == 2:
                display.blit(hand,[600,150])


            if meter_x > -250:
                display.blit(good,[meter_x,meter_y])

            if meter_x > -350 and meter_x < -250:
                display.blit(meh,[meter_x,meter_y])

            if meter_x < -350:
                display.blit(bad,[meter_x,meter_y])


            display.blit(outline,[0,0])


            if alarm == 12:
                display.blit(twelve,[944,0])

            if alarm == 1:
                display.blit(one,[944,0])

            if alarm == 2:
                display.blit(two,[944,0])

            if alarm == 3:
                display.blit(three,[944,0])

            if alarm == 4:
                display.blit(four,[944,0])

            if alarm == 5:
                display.blit(five,[944,0])

            if alarm == 6:
                display.blit(six,[944,0])


            if location == 1:
                if lights == False:
                    display.blit(light_off,[map_x,map_y])



            if agrippa_voice == 1:
                pygame.mixer.Sound.play(rec1)
                agrippa_voice = 0


            if agrippa_voice == 2:
                pygame.mixer.Sound.play(rec2)
                agrippa_voice = 0

            if agrippa_voice == 3:
                pygame.mixer.Sound.play(rec3)
                agrippa_voice = 0

            if agrippa_voice == 4:
                pygame.mixer.Sound.play(rec4)
                agrippa_voice = 0


            if agrippa_voice == 5:
                pygame.mixer.Sound.play(rec5)
                agrippa_voice = 0



            if look_time > -1:
                look_time += 1


            if chup_reset > 0:
                chup_reset -= 1


            if chup_attack > 0:
                chup_attack -= 1


            if dylan_reset > 0:
                dylan_reset -= 1

            if dylan_attack > 0:
                dylan_attack -= 1


            if recorder_reset > 0:
                recorder_reset -= 1


            if recorder_time < 15 and recorder_times == 1:
                william_mattar = 1
                recorder_time = 15
                recorder_times = 2


            if recorder_time < 15 and recorder_times == 2:
                william_mattar = 2
                recorder_time = 15
                agrippa_voice = random.randint(1,5)
                recorder_times = 3


            if recorder_time < 15 and recorder_times == 3:
                william_mattar = 1
                recorder_time = 25
                recorder_times = 4


            if recorder_time == 0 and recorder_times == 4:
                recorder_break = random.randint(1,3)
                recorder_time = -3
                recorder_times == 0
                william_mattar = 0




            if recorder_time == 0 and recorder_times == 6:
                recorder_time = 25
                recorder_times = 7


            if recorder_time == 0 and recorder_times == 7:
                ramos_and_ramos = 2
                recorder_time = 25
                recorder_times = 8


            if recorder_time == 0 and recorder_times == 8:
                ramos_and_ramos = 3
                recorder_time = 10
                recorder_times = 9

            if recorder_time == 0 and recorder_times == 9:
                ramos_and_ramos = 0
                recorder_break = 1
                recorder_times = -3
                recorder_time = -3
                celino_and_barnes = 0


            recorder_reset = 50

            if alarm == 6:
                win()

            if recorder_reset == 0:
                recorder_break = random.randint(1,5)


            if recorder_break == 1:
                recorder_reset = 50



            if recorder_break == 2 or recorder_break == 3:
                if celino_and_barnes == 0:
                    pygame.mixer.Sound.play(snap)
                    celino_and_barnes = 1


            if ramos_and_ramos == 1:
                if location == 1:
                    display.blit(dot,[200,0])

            if ramos_and_ramos == 2:
                if location == 1:
                    display.blit(dot2,[200,0])

            if ramos_and_ramos == 3:
                if location == 1:
                    display.blit(dot3,[200,0])



            if william_mattar == 1:
                if location == 1:
                    display.blit(hover,[250,30])

            if william_mattar == 2:
                if location == 1:
                    display.blit(press_recorder,[250,30])


            if scare_face == 1:
                display.blit(scare1,[0,0])

            if scare_face == 2:
                display.blit(scare2,[0,0])

            if scare_face == 3:
                display.blit(scare3,[0,0])


            if scare_face == -5:
                display.blit(scare_1,[0,0])

            if scare_face == -6:
                display.blit(scare_2,[0,0])

            if scare_face == -7:
                display.blit(scare_3,[0,0])


            if will_scare_face == 1:
                display.blit(will1,[0,0])

            if will_scare_face == 2:
                display.blit(will2,[0,0])

            if will_scare_face == 3:
                display.blit(will3,[0,0])

            if will_scare_face == 4:
                display.blit(will4,[0,0])



            clock.tick(FPS)


            pygame.display.update()

    sys.exit()

time2 = 0
times2 = 0
time3 = 0
times3 = 0
chup_reset = 50
chup_attack = -3
chup_ai = random.randint(1,2)
dylan_reset = 100
dylan_attack = -2
dylan_ai = random.randint(1,2)
will_reset = 125
will_attack = -2
will_ai = random.randint(1,4)


def loop2():
    global running
    global night
    global time2
    global times2
    global time3
    global times3
    global time4
    global times4
    global chup_ai
    global chup_attack
    global chup_reset
    global dylan_reset
    global dylan_attack
    global dylan_ai
    global will_reset
    global will_attack
    global will_ai
    global stars

    pickle_out = open("save.pickle","wb")
    pickle.dump(stars, pickle_out)
    pickle_out.close()

    mode = 2

    rightx = 1038
    righty = 20
    turnx = 300
    turny = 646


    chup_scare_time = -3
    chup_scare_times = -3

    will_scare_time = -3
    will_scare_times = -3


    time2 = 0
    times2 = 0
    time3 = 0
    times3 = 0
    chup_reset = 75
    chup_attack = -3
    chup_ai = random.randint(1,2)
    location = 1
    dylan_reset = 75
    dylan_attack = -2
    dylan_ai = random.randint(1,3)
    flash_time = -3
    dylan_gone = True
    juda_ai = 1
    juda_reset = 75
    juda_attack = -2
    juda_wait = -3
    dylan_wait = -3
    recorder_reset = -3
    will_reset = 75
    will_attack = -2
    will_ai = random.randint(1,5)

    ramos_and_ramos = 0
    celino_and_barnes = 0
    william_mattar = 0

    agrippa_voice = 0

    amb_wait = 0

    show = 0
    show2 = 0


    scare_face = 0
    will_scare_face = 0

    chup_show = 0

    call = 0

    alarm_time = 1125
    alarm = 12


    recorder_time = -3
    recorder_times = 0
    recorder_break = 1


    humid = False

    look_time = -3

    meter_x = 0
    meter_y = 0

    comp_life = 500

    shine = False

    flash_x = -920
    flash_y = -5

    in_game = True
    press = 0

    location = 1
    lights = True

    pygame.mixer.music.stop()

    map_x = -10
    map_y = 0

    if in_game == True:
        while running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False

                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_SPACE:
                        if location == 3:
                            pygame.mixer.Sound.play(button_click)
                            press = 1
                            comp_life = 500
                            meter_x = 0

                            if chup_ai == 3 or chup_ai == 4 or chup_ai == 5:
                                chup_scare_time = 0
                                chup_scare_times = 0
                                chup_ai = 18



                        if location == 1:
                            if recorder_break == 1:
                                recorder_time = 5
                                recorder_times = 1
                                pygame.mixer.Sound.play(button_click)
                                will_attack = -2
                                will_ai = 1

                            if recorder_break == 2 or recorder_break == 3:
                                ramos_and_ramos = 1
                                recorder_time = 5
                                recorder_times = 6


                        if location == 5:
                            pygame.mixer.Sound.play(on)
                            shine = True
                            pygame.mixer.Sound.stop(laugh)


                    if event.key == pygame.K_ESCAPE:
                        running = False


                if event.type == pygame.KEYUP:
                    if event.key == pygame.K_SPACE:
                        if location == 5:
                            pygame.mixer.Sound.play(off)
                            shine = False


            mouse = pygame.mouse.get_pos()
            click = pygame.mouse.get_pressed()


            if location == 1:
                if lights == True:
                    if turnx+566 > mouse[0] > turnx and  turny+74 > mouse[1] > turny:
                        times2 = 1
                        location = 2
                        time2 = 5


            if chup_scare_time == 0 and chup_scare_times == 0:
                if lights == True:
                    scare_face = 1
                    chup_scare_times = 1
                    chup_scare_time = 5
                    pygame.mixer.Sound.stop(humid_sound)
                    pygame.mixer.Sound.stop(laugh)
                    pygame.mixer.Sound.play(scare)

                if lights == False:
                    scare_face = -5
                    chup_scare_times = -5
                    chup_scare_time = 5
                    pygame.mixer.Sound.stop(humid_sound)
                    pygame.mixer.Sound.stop(laugh)
                    pygame.mixer.Sound.play(scare)
                    lights = 1



            if chup_scare_time == 0 and chup_scare_times == 1:
                scare_face = 2
                chup_scare_times = 2
                chup_scare_time = 5


            if chup_scare_time == 0 and chup_scare_times == 2:
                scare_face = 3
                chup_scare_times = 3
                chup_scare_time = 10


            if chup_scare_time == 0 and chup_scare_times == 3:
                intro()




            if chup_scare_time == 0 and chup_scare_times == -5:
                scare_face = -6
                chup_scare_times = -6
                chup_scare_time = 5


            if chup_scare_time == 0 and chup_scare_times == -6:
                scare_face = -7
                chup_scare_times = -7
                chup_scare_time = 10


            if chup_scare_time == 0 and chup_scare_times == -7:
                intro()



            if chup_scare_time > 0:
                chup_scare_time -= 1


            if time2 < 1 and times2 == 1:
                location = 3

            if will_reset == 0:
                will_ai = random.randint(1,5)

            if will_ai == 1 or will_ai == 2:
                will_reset = 75
                will_ai = 7


            if location == 3:
                if press == 0:
                    if turnx+566 > mouse[0] > turnx and  turny+74 > mouse[1] > turny:
                        times2 = 2
                        location = 2
                        time2 = 5



            if time2 < 1 and times2 == 2:
                location = 1
                map_x = 0
                times2 = 0



            if time4 < 1 and times4 == 2:
                location = 5

            if time4 == 0 and times4 == 3:
                location = 1
                times4 = 0


            if juda_wait > 0:
                juda_wait -= 1


            if press == 1:
                times3 = 1
                time3 = 10
                press = 2


            if will_ai == 1 or will_ai == 2 or will_ai == 7:
                show = 0


            if time3 == 0 and times3 == 1:
                press = 0
                times3 = 0


            if amb_wait > 0:
                amb_wait -= 1

            if amb_wait == 0:
                pygame.mixer.Sound.play(ambience)
                amb_wait = 150


            if will_reset > 0:
                will_reset -= 1


            if will_attack > 0:
                will_attack -= 1


            if juda_reset > 0:
                juda_reset -= 1


            if juda_attack > 0:
                juda_attack -= 1


            if juda_reset == 0:
                juda_ai = random.randint(1,3)


            if juda_ai == 1:
                juda_reset = 125
                juda_ai = 7


            if time2 > 0:
                time2 -= 1

            if time3 > 0:
                time3 -= 1

            if time4 > 0:
                time4 -= 1

            if comp_life > 0:
                comp_life -= 1
                meter_x -= 1


            if comp_life == 0:
                lights = False

            if alarm_time > 0:
                alarm_time -= 1


            if dylan_attack < 1 and shine == True:
                dylan_attack = -3
                dylan_ai = 3


            call = random.randint(1,9)


            if alarm == 12 and alarm_time == 0:
                if call == 1:
                    pygame.mixer.Sound.play(spirits)

                alarm = 1
                alarm_time = 1125

            if alarm == 1 and alarm_time == 0:
                if call == 2:
                  pygame.mixer.Sound.play(spirits)

                alarm = 2
                alarm_time = 1125

            if alarm < 6 and alarm_time == 0:
                alarm += 1
                alarm_time = 1125



            if chup_reset < 1 and chup_reset > -3 and humid == True:
                chup_ai = random.randint(1,5)

            if chup_reset < 1 and chup_reset > -3 and humid == False:
                chup_ai = random.randint(2,5)


            if dylan_reset < 1 and dylan_reset > -3:
                if dylan_ai == 1 or dylan_ai == 2 or dylan_ai == 3 or dylan_ai == 7:
                    dylan_ai = random.randint(1,6)

            if dylan_reset < 1 and dylan_attack == -3:
                dylan_ai = random.randint(1,3)
                dylan_attack = -2


            if chup_reset == -1 and humid == True:
                chup_ai = random.randint(1,2)


            if chup_reset == -1 and humid == False:
                chup_ai = 1


            if dylan_ai == 4 or dylan_ai == 5 or dylan_ai == 6:
                if shine == False:
                    if juda_ai == 2 or juda_ai == 3 or juda_ai == 8:
                        pygame.mixer.Sound.play(laugh)



                if dylan_attack == -2:
                    dylan_wait = 100
                    dylan_attack = -3

                if shine == False:
                    dylan_attack = 75

                if shine == True:
                    dylan_wait = 100


            if dylan_wait > 0:
                dylan_wait -= 1


            if recorder_time > 0:
                recorder_time -= 1


            if dylan_ai == 1 or dylan_ai == 2 or dylan_ai == 3 or dylan_ai == 7:
                dylan_wait = -2


            if dylan_wait == 0:
                humid = True


            if humid == True:
                dylan_ai = -5
                dylan_attack = -5
                dylan_reset = -5
                dylan_wait = -5


            if humid == True:
                recorder_break = -73
                pygame.mixer.Sound.play(humid_sound)



            if chup_ai == 1 or chup_ai == 2:
                chup_reset = 75
                chup_ai = 7

            if dylan_ai == 1 or dylan_ai == 2:
                dylan_reset = 75
                dylan_ai = 7


            if location == 5:
                if lights == True:
                    display.blit(dehumid,[0,0])

                    if dylan_ai == 4 or dylan_ai == 5 or dylan_ai == 6:
                        display.blit(dylan,[820,350])

                else:
                    display.blit(dehumid,[0,0])

                if shine == True:
                    display.blit(flash,[flash_x,flash_y])

                else:
                    display.blit(flash,[flash_x,flash_y])


                display.blit(left,[0,20])



            if shine == True:
                flash_x = -920
                flash_y = -5


            else:
                flash_x = -5
                flash_y = -5


            if location == 5:
                if lights == False:
                    location = 1

                if press == 0:
                    if 0+42 > mouse[0] > 0 and  20+366 > mouse[1] > 20:
                        if click[0] == 1:
                            times4 = 3
                            location = 4
                            time4 = 5


            if location == 4:
                if lights == True:
                    display.blit(turning,[0,0])
                if lights == False:
                    location = 1
                display.blit(right,[500,630])


            if chup_ai == 3 or chup_ai == 4 or chup_ai == 5:
                chup_show = 1


            if location == 3:
                if lights == True:
                    display.blit(computer,[0,0])
                    display.blit(turn,[turnx,turny])


                    if chup_ai == 3 or chup_ai == 4 or chup_ai == 5 and chup_attack == -3:

                        if chup_show == 1:
                            display.blit(chup,[650,50])

                        chup_reset = -3
                        look_time = 0

                if chup_reset == -3:
                    chup_attack = 125
                    chup_reset = -4


            if lights == False:
                location = 1
                display.blit(turn,[turnx,turny])


            if chup_attack > 0 and look_time > 74:
                chup_show = 0
                chup_attack = -3
                chup_reset = -1
                look_time = -3



            if location == 2:
                if lights == True:
                    display.blit(computer_turn,[0,0])
                if lights == False:
                    location = 1

            if location == 1:
                if lights == True:
                    display.blit(basement,[map_x,map_y])
                    display.blit(turn,[turnx,turny])

                    if will_ai == 3 or will_ai == 4 or will_ai == 5:
                        show = 1
                        will_reset = -3
                        if humid == True:
                            will_attack = 125

                        if humid == False:
                            will_attack = 75

                        will_ai = 8


                    if juda_ai == 2 or juda_ai == 3:
                        #pygame.mixer.Sound.play(confused)
                        show2 = 1
                        juda_reset = -3



            if juda_ai == 2 or juda_ai == 3:
                if location == 1 and map_x > -85:
                    juda_attack = 75
                    juda_ai = 8


            if location == 1 and map_x > -85 and juda_ai == 8 and juda_attack == 0:
                show2 = 0
                juda_attack = -2
                juda_ai = 1
                juda_reset = 125



            if juda_attack == 0:
                if location != 1 or map_x < -84:
                    show2 = 0
                    juda_attack = -2
                    juda_ai = 1
                    juda_reset = 125
                    recorder_break = 2



                if lights == False:
                    display.blit(light_off,[map_x,map_y])
                    chup_scare_time = 0
                    chup_scare_times = 0
                shine = False

                if lights == True:
                    display.blit(turn,[turnx,turny])


            if will_attack == 0:
                if location == 1:
                    will_scare_time = 0
                    will_scare_times = 0
                    will_attack = -2

                if location == 3:
                    times2 = 2
                    location = 2
                    time2 = 5

                if location == 5:
                    times4 = 3
                    location = 4
                    time4 = 5




            if will_scare_time > 0:
                will_scare_time -= 1


            if will_scare_time == 0 and will_scare_times == 0:
                will_scare_face = 1
                will_scare_time = 5
                will_scare_times = 1
                pygame.mixer.Sound.stop(humid_sound)
                pygame.mixer.Sound.stop(laugh)
                pygame.mixer.Sound.play(scare)

            if will_scare_time == 0 and will_scare_times == 1:
                will_scare_face = 2
                will_scare_time = 5
                will_scare_times = 2

            if will_scare_time == 0 and will_scare_times == 2:
                will_scare_face = 3
                will_scare_time = 5
                will_scare_times = 3

            if will_scare_time == 0 and will_scare_times == 3:
                will_scare_face = 4
                will_scare_time = 10
                will_scare_times = 5

            if will_scare_time == 0 and will_scare_times == 5:
                intro()


            if show == 1:
                if location == 1:
                    display.blit(will,[map_x + 1170,200])


            if show2 == 1:
                if location == 1:
                    display.blit(juda,[map_x + 60,370])


            if location == 1:
                if mouse[0] >= 600:
                    map_x -= 15

                if mouse[0] <= 300:
                    map_x += 15

                if map_x + 1000 >= 800:
                    map_x -= 15

                else:
                    if lights == True:
                        display.blit(right,[rightx,righty])

                    if location == 1:
                        if press == 0:
                            if rightx+42 > mouse[0] > rightx and  righty+366 > mouse[1] > righty:
                                if click[0] == 1:
                                    times4 = 2
                                    location = 4
                                    time4 = 5



                if map_x <= -10:
                    map_x += 15


            if press == 1 or press == 2:
                display.blit(hand,[600,150])


            if meter_x > -250:
                display.blit(good,[meter_x,meter_y])

            if meter_x > -350 and meter_x < -250:
                display.blit(meh,[meter_x,meter_y])

            if meter_x < -350:
                display.blit(bad,[meter_x,meter_y])


            display.blit(outline,[0,0])


            if alarm == 12:
                display.blit(twelve,[944,0])

            if alarm == 1:
                display.blit(one,[944,0])

            if alarm == 2:
                display.blit(two,[944,0])

            if alarm == 3:
                display.blit(three,[944,0])

            if alarm == 4:
                display.blit(four,[944,0])

            if alarm == 5:
                display.blit(five,[944,0])

            if alarm == 6:
                display.blit(six,[944,0])


            if location == 1:
                if lights == False:
                    display.blit(light_off,[map_x,map_y])



            if agrippa_voice == 1:
                pygame.mixer.Sound.play(rec1)
                agrippa_voice = 0


            if agrippa_voice == 2:
                pygame.mixer.Sound.play(rec2)
                agrippa_voice = 0

            if agrippa_voice == 3:
                pygame.mixer.Sound.play(rec3)
                agrippa_voice = 0

            if agrippa_voice == 4:
                pygame.mixer.Sound.play(rec4)
                agrippa_voice = 0


            if agrippa_voice == 5:
                pygame.mixer.Sound.play(rec5)
                agrippa_voice = 0



            if look_time > -1:
                look_time += 1


            if chup_reset > 0:
                chup_reset -= 1


            if chup_attack > 0:
                chup_attack -= 1


            if dylan_reset > 0:
                dylan_reset -= 1

            if dylan_attack > 0:
                dylan_attack -= 1


            if recorder_reset > 0:
                recorder_reset -= 1


            if recorder_time < 15 and recorder_times == 1:
                william_mattar = 1
                recorder_time = 15
                recorder_times = 2


            if recorder_time < 15 and recorder_times == 2:
                william_mattar = 2
                recorder_time = 15
                agrippa_voice = random.randint(1,5)
                recorder_times = 3


            if recorder_time < 15 and recorder_times == 3:
                william_mattar = 1
                recorder_time = 25
                recorder_times = 4


            if recorder_time == 0 and recorder_times == 4:
                recorder_break = random.randint(1,3)
                recorder_time = -3
                recorder_times == 0
                william_mattar = 0




            if recorder_time == 0 and recorder_times == 6:
                recorder_time = 25
                recorder_times = 7


            if recorder_time == 0 and recorder_times == 7:
                ramos_and_ramos = 2
                recorder_time = 25
                recorder_times = 8


            if recorder_time == 0 and recorder_times == 8:
                ramos_and_ramos = 3
                recorder_time = 10
                recorder_times = 9

            if recorder_time == 0 and recorder_times == 9:
                ramos_and_ramos = 0
                recorder_break = 1
                recorder_times = -3
                recorder_time = -3
                celino_and_barnes = 0


            recorder_reset = 50

            if alarm == 6:
                stars = 2
                win()

            if recorder_reset == 0:
                recorder_break = random.randint(1,5)


            if recorder_break == 1:
                recorder_reset = 50



            if recorder_break == 2 or recorder_break == 3:
                if celino_and_barnes == 0:
                    pygame.mixer.Sound.play(snap)
                    celino_and_barnes = 1


            if ramos_and_ramos == 1:
                if location == 1:
                    display.blit(dot,[200,0])

            if ramos_and_ramos == 2:
                if location == 1:
                    display.blit(dot2,[200,0])

            if ramos_and_ramos == 3:
                if location == 1:
                    display.blit(dot3,[200,0])



            if william_mattar == 1:
                if location == 1:
                    display.blit(hover,[250,30])

            if william_mattar == 2:
                if location == 1:
                    display.blit(press_recorder,[250,30])


            if scare_face == 1:
                display.blit(scare1,[0,0])

            if scare_face == 2:
                display.blit(scare2,[0,0])

            if scare_face == 3:
                display.blit(scare3,[0,0])


            if scare_face == -5:
                display.blit(scare_1,[0,0])

            if scare_face == -6:
                display.blit(scare_2,[0,0])

            if scare_face == -7:
                display.blit(scare_3,[0,0])


            if will_scare_face == 1:
                display.blit(will1,[0,0])

            if will_scare_face == 2:
                display.blit(will2,[0,0])

            if will_scare_face == 3:
                display.blit(will3,[0,0])

            if will_scare_face == 4:
                display.blit(will4,[0,0])



            clock.tick(FPS)


            pygame.display.update()

    sys.exit()

intro()
