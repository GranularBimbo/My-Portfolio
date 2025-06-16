import random
import pygame
import pickle
from image_renderer import *
from audio_renderer import *
from main import *


time2 = 0
times2 = 0
time3 = 0
times3 = 0
chup_reset = 50
chup_attack = -3
chup_ai = random.randint(1,2)
dylan_reset = 75
dylan_attack = -2
dylan_ai = random.randint(1,2)
will_reset = 50
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


    time2 = 0
    times2 = 0
    time3 = 0
    times3 = 0
    chup_reset = 50
    chup_attack = -3
    chup_ai = random.randint(1,2)
    location = 1
    dylan_reset = 75
    dylan_attack = -2
    dylan_ai = random.randint(1,2)
    flash_time = -3
    dylan_gone = True
    juda_ai = 3
    juda_reset = 50
    juda_attack = -2
    juda_wait = -3
    dylan_wait = -3
    recorder_reset = -3
    will_reset = 75
    will_attack = -2
    will_ai = random.randint(1,4)

    ramos_and_ramos = 0
    celino_and_barnes = 0
    william_mattar = 0

    agrippa_voice = 0

    amb_wait = 0

    show = 0
    show2 = 0

    gone = True

    call = 0

    alarm_time = 1500
    alarm = 12


    recorder_time = -3
    recorder_times = 0
    recorder_break = 1


    humid = False

    look_time = -3

    meter_x = 0
    meter_y = 0

    comp_life = 375

    shine = False

    flash_x = -600
    flash_y = -200

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
                            comp_life = 375
                            meter_x = 0

                            if chup_ai == 3 or chup_ai == 4 or chup_ai == 5:
                                main.intro()


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


                if event.type == pygame.KEYUP:
                    if event.key == pygame.K_SPACE:
                        if location == 5:
                            pygame.mixer.Sound.play(off)
                            shine = False


            mouse = pygame.mouse.get_pos()
            click = pygame.mouse.get_pressed()


            if location == 1:
                if lights == True:
                    if 100+566 > mouse[0] > 100 and  630+74 > mouse[1] > 630:
                        times2 = 1
                        location = 2
                        time2 = 5


            if time2 < 1 and times2 == 1:
                location = 3

            if will_reset == 0:
                will_ai = random.randint(1,5)

            if will_ai == 1 or will_ai == 2:
                will_reset = 125
                will_ai = 7


            if location == 3:
                if press == 0:
                    if 100+566 > mouse[0] > 100 and  630+74 > mouse[1] > 630:
                        times2 = 2
                        location = 2
                        time2 = 5



            if time2 < 1 and times2 == 2:
                location = 1
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
                chup_ai = random.randint(1,4)

            if chup_reset < 1 and chup_reset > -3 and humid == False:
                chup_ai = random.randint(1,4)


            if dylan_reset < 1 and dylan_reset > -3:
                if dylan_ai == 1 or dylan_ai == 2 or dylan_ai == 3 or dylan_ai == 7:
                    dylan_ai = random.randint(1,5)

            if dylan_reset < 1 and dylan_attack == -3:
                dylan_ai = random.randint(1,2)
                dylan_attack = -2


            if chup_reset == -1 and humid == True:
                chup_ai = 1


            if chup_reset == -1 and humid == False:
                chup_ai = 1


            if dylan_ai == 3 or dylan_ai == 4 or dylan_ai == 5:
                if shine == False:
                    if juda_ai == 2 or juda_ai == 3 or juda_ai == 8:
                        pygame.mixer.Sound.play(laugh)



                if dylan_attack == -2:
                    dylan_wait = 75
                    dylan_attack = -3

                if shine == False:
                    dylan_attack = 75

                if shine == True:
                    dylan_wait = 100


            if dylan_wait > 0:
                dylan_wait -= 1


            if recorder_time > 0:
                recorder_time -= 1


            if dylan_ai == 1 or dylan_ai == 2 or dylan_ai == 7:
                dylan_wait = -2


            if dylan_wait == 0:
                humid = True


            if humid == True:
                dylan_ai = -5
                dylan_attack = -5
                dylan_reset = -5
                dylan_wait = -5


            if humid == True:
                pygame.mixer.Sound.play(humid_sound)



            if chup_ai == 1:
                chup_reset = 50
                chup_ai = 7

            if dylan_ai == 1 or dylan_ai == 2:
                dylan_reset = 75
                dylan_ai = 7


            if location == 5:
                if lights == True:
                    display.blit(dehumid,[0,0])

                    if dylan_ai == 3 or dylan_ai == 4 or dylan_ai == 5:
                        display.blit(dylan,[600,300])

                else:
                    display.blit(dehumid,[0,0])

                if shine == True:
                    display.blit(flash,[flash_x,flash_y])

                else:
                    display.blit(flash,[flash_x,flash_y])


                display.blit(left,[0,20])



            if shine == True:
                flash_x = -600
                flash_y = -600


            else:
                flash_x = -10
                flash_y = -200


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
                display.blit(right,[100,630])


            if chup_ai == 2 or chup_ai == 3 or chup_ai == 4 or chup_ai == 5:
                if chup_attack > 0 and look_time < 75:
                    gone = False


            if location == 3:
                if lights == True:
                    display.blit(computer,[0,0])
                    display.blit(turn,[100,630])


                    if chup_ai == 2 or chup_ai == 3 or chup_ai == 4 or chup_ai == 5 and chup_attack == -3:

                        if gone == False:
                            display.blit(chup,[650,50])

                        chup_reset = -3
                        look_time = 0

                if chup_reset == -3:
                    chup_attack = 125
                    chup_reset = -4


            if lights == False:
                location = 1
                display.blit(turn,[100,630])


            if chup_attack > 0 and look_time > 74:
                gone = True
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
                    display.blit(turn,[100,630])

                    if will_ai == 2 or will_ai == 3 or will_ai == 4:
                        show = 1
                        will_reset = -3
                        if humid == True:
                            will_attack = 75

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
                juda_reset = 75



            if juda_attack == 0:
                if location != 1 or map_x < -84:
                    show2 = 0
                    juda_attack = -2
                    juda_ai = 1
                    juda_reset = 75
                    recorder_break = 2



                if lights == False:
                    display.blit(light_off,[map_x,map_y])
                shine = False

                if lights == True:
                    display.blit(turn,[100,630])


            if will_attack == 0:
                main.intro()


            if show == 1:
                if location == 1:
                    display.blit(will,[map_x + 910,200])


            if show2 == 1:
                if location == 1:
                    display.blit(juda,[map_x + 40,370])


            if location == 1:
                if mouse[0] >= 600:
                    map_x -= 15

                if mouse[0] <= 300:
                    map_x += 15

                if map_x + 1000 >= 800:
                    map_x -= 15

                else:
                    if lights == True:
                        display.blit(right,[760,20])

                    if location == 1:
                        if press == 0:
                            if 760+42 > mouse[0] > 760 and  20+366 > mouse[1] > 20:
                                if click[0] == 1:
                                    times4 = 2
                                    location = 4
                                    time4 = 5



                if map_x <= -10:
                    map_x += 15


            if press == 1 or press == 2:
                display.blit(hand,[450,150])


            if meter_x > -250:
                display.blit(good,[meter_x,meter_y])

            if meter_x > -350 and meter_x < -250:
                display.blit(meh,[meter_x,meter_y])

            if meter_x < -350:
                display.blit(bad,[meter_x,meter_y])


            display.blit(outline,[0,0])


            if alarm == 12:
                display.blit(twelve,[650,0])

            if alarm == 1:
                display.blit(one,[650,0])

            if alarm == 2:
                display.blit(two,[650,0])

            if alarm == 3:
                display.blit(three,[650,0])

            if alarm == 4:
                display.blit(four,[650,0])

            if alarm == 5:
                display.blit(five,[650,0])

            if alarm == 6:
                display.blit(six,[650,0])


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
                win2()

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
                    display.blit(dot,[0,0])

            if ramos_and_ramos == 2:
                if location == 1:
                    display.blit(dot2,[0,0])

            if ramos_and_ramos == 3:
                if location == 1:
                    display.blit(dot3,[0,0])



            if william_mattar == 1:
                if location == 1:
                    display.blit(hover,[0,0])

            if william_mattar == 2:
                if location == 1:
                    display.blit(press_recorder,[0,0])


            clock.tick(FPS)


            pygame.display.update()

    pygame.quit()
    quit()
