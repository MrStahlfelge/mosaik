{
  <#import "common.ftl" as common>
  <#import "visitorentries.ftl" as visitorentries>
  "manifest": <@common.manifest />,
  "actions": [
    {
      "type": "BackendRequestAction",
      "id": "proceedButtonClicked",
      "url": "/add"
    }
  ],
  "view": {
    "type": "Column",
    "children": [
      {
        "type": "Label",
        "style": "HEADLINE2",
        "text": "List input demo application"
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
            "text": "This demo shows how the Mosaik client (running in wallet application or 'Mosaik executor') can interact with a Mosaik backend.\nData entered here will be transferred to the backend which will respond with a UI refresh for the client.",
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
            "minValue": 1,
            "onImeAction": "proceedButtonClicked",
            "placeholder": "Your name"
          }
        ]
      },
      {
        "type": "Button",
        "onClick": "proceedButtonClicked",
        "text": "Proceed"
      },
      <@visitorentries.visitorlist />
    ]
  }
}