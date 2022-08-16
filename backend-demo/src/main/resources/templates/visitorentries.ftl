<#macro visitorlist>
{
  "id": "listColumn",
  "type": "Column",
  "padding": "DEFAULT",
  "children": [
    {
      "type": "Label",
      "style": "BODY1BOLD",
      "text": "List of last visitors"
    }
    <#list visitors as visitor>
  ,
    {
      "type": "Label",
      "style": "BODY1",
      "text": "${visitor}"
    }
    <#else>
  ,
    {
      "type": "Label",
      "style": "BODY1",
      "text": "None yet"
    }
    </#list>
  ]
}
</#macro>