{
  "manifest": {
    "appName": "Demo App (with backend)",
    "appVersion": ${appversion},
    "targetMosaikVersion": 0,
    "minMosaikVersion": 0,
    "baseUrl": "${hostaddress}",
    "cacheLifetime": 0
  },
  "actions": [
    {
      "type": "BackendRequestAction",
      "id": "proceedButtonClicked",
      "url": "/proceed"
    }
  ],
  "view": {
    "type": "Column",
    "children": [
      {
        "type": "Label",
        "style": "HEADLINE2",
        "text": "Demo application with server"
      },
      {
        "type": "Label",
        "style": "BODY1BOLD",
        "text": "Served from ${hostaddress}"
      },
      {
        "type": "Box",
        "padding": "TWICE",
        "children": [
          {
            "type": "Label",
            "style": "BODY1",
            "text": "Launch this demo without running server demo to see some layouting and offline capability demos.\n\nEnter your name below and tap or click the button.",
            "textAlignment": "CENTER"
          }
        ]
      },
      {
        "type": "Box",
        "padding": "DEFAULT",
        "children": [
          {
            "type": "TextInputField",
            "id": "userName",
            "value": "",
            "placeholder": "Your name"
          }
        ]
      },
      {
        "type": "Button",
        "onClick": "proceedButtonClicked",
        "text": "Proceed"
      }
    ]
  }
}