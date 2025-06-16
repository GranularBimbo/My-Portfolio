using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class careDays : MonoBehaviour
{
    public GameObject Origin, point;
    public int circleNum;
    public int fontSize;
    public Font font;
    public World world;
    public Sprite circleSprite;
    public RectTransform graphContainter;

    public void Start()
    {

        fontSize = 15;
    }

    public void changeDeathPosition()
    {
        for (int i = 1; i < circleNum; i++)
        {
            Origin = GameObject.Find("Carecircle0");

            point = GameObject.Find("Carecircle" + i);

            float distanceToFirstPoint = point.transform.position.x - Origin.transform.position.x;

            float newPointX = point.transform.position.x - (distanceToFirstPoint / 2);

            point.transform.SetPositionAndRotation(new Vector3(newPointX, point.transform.position.y, point.transform.position.z), point.transform.rotation);
        }
    }

    public void createCircle(Vector2 anchoredPosition)
    {
        
        GameObject circle = new GameObject("Carecircle" + circleNum, typeof(Image)); //creats point
        circleNum++;

        circle.transform.SetParent(graphContainter, true); //sets the point to be a child of the graphContainer
        circle.GetComponent<Image>().sprite = circleSprite;
        RectTransform rectTransform = circle.GetComponent<RectTransform>();
        rectTransform.anchoredPosition = anchoredPosition;
        rectTransform.sizeDelta = new Vector2(11, 11);
        rectTransform.anchorMin = new Vector2(0, 0);
        rectTransform.anchorMax = new Vector2(0, 0);
    }
}
