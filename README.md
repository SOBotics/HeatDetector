# HeatDetector


HeatDetector is an API that can classify content (used for classifing comments on SE network). The classifier use both ML and regex to find "Heat". Heat is an user beeing snarky, offensive or rude but also content that indicate that a user is feeling attacked. The api can also be used to track certain words/phrases using regex that is editable via web interface. Read more at [Heat Detector - analysing comments to find heat](https://stackapps.com/questions/7001/heat-detector-analysing-comments-to-find-heat)

The application contains both a spring bot and an angular application. The spring bot is the actual api and the angular application allows logged users to modify thier domain settings.

## Usage

To usage the api you need to request an api key. The api has a very "crude" rate limiter that allows you to call it every 30s but with upto 100 different texts to classify. If you call it more often a backoff will be sent and if this is not respected the ip will be rate limited for 5 minutes.

###Example usage of API

**Request**

    {
     "domain": "stackoverflow", //domain for regex
     "minScore": "4", //if classifed score is below it will not return result on contents entry  
     "contents": [
            {
            "id":1, //Identification of content (long)
            "text": "fuck you, you are an arse" //Text to classify
            },
                {
            "id":2,
            "text": "I need help @MrBean. please don't insult.",
            "href": "https://stackoverflow.com/questions/51588616/when-click-button-to-open-another-activity-my-app-crashing-sometimes#comment90145474_51588616" //link to comment only used in web interface, not compulsory
            }
        ]
     }
    
**Response**

    {
     "domain": "stackoverflow",
     "result": [
        {
            "id": 1,
            "nb": 0.9999999209847878,
            "op": 0.5,
            "bad": {
                "regex": "(?i)(f\\W{1,4}k|f.{0,2}u.{0,2}c.{0,2}k)\\s(of|e.{0,2}r|u|yo)",
                "type": 3
            },
            "score": 10
        },
        {
            "id": 2,
            "nb": 0.9447963253138169,
            "op": 0.5,
            "bad": {
                "regex": "(?i)\\binsults?\\b(?! to injury)",
                "type": 3
            },
            "score": 6
        }
     ],
     "backOff": 0
    }



 
