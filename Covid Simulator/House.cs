using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class House : MonoBehaviour
{
    public Human[] family = new Human[5];
    public World world;
    public bool someoneShopping;
    public int slotNum;
    public bool facingRight;    //should help with spawning humans at each house
    private int decider;

    public House()
    {
        someoneShopping = false;
        family = new Human[5];
    }

    // Start is called before the first frame update
    void Start()
    {
        
    }
}
