## Report Generation Engine

This is a `template based report generation engine` built on `Java` using `poi-tl`.

### Setup Instructions

 - Download Project

 ```sh
git clone https://github.com/aamitn/Report.git
cd report
```
- Get the Dependencies

 ```sh
mvn clean install
```
- Run Project

 ```sh
mvn spring-boot:run
```

Engine should be up at `http://localhost:8080`

### How to interact with the engine using API

- Sample Template ([Download](blob:https://github.com/bf7337b8-6a21-431f-9093-ddae170b7a63))
- **Upload a template (Only .docx templates supported)**

 ```sh
curl --location ' 
http://localhost:8080/api/templates/upload' \
--header 'Content-Type: application/json' \
--header 'Accept: application/    octet-stream' \
--form 'file=@"/C:/Users/bigwiz/Desktop/    template.docx"
'
```
you will get response containing the unique template name like `your-template_xx99.docx`


- **Generate Report/Document  using the uploaded template**

 ```sh
curl --location '
http://localhost:8080/api/reports/generate' \
--header 'Content-Type: application/json' \
--header 'Accept: application/octet-stream' \
--data '{
  "template": "your-template_xx99.docx",

  "title": {
    "text": "Styled Report Title",
    "style": {
      "bold": true,
      "italic": true,
      "underline": true,
      "strike": true,
      "color": "FF0000",
      "fontFamily": "Micro Elegant Black",
      "fontSize": 26,
      "link": "https://www.google.com"
    }
  },
  "logo": {
    "path": "C:\\Users\\bigwiz\\Pictures\\6.png",
    "width": 120,
    "height": 120
  },
  "banner": {
    "url": "https://ssl.gstatic.com/ui/v1/icons/mail/rfr/logo_gmail_lockup_dark_2x_r5.png",
    "width": 600,
    "height": 200
  },

 "logoStream": {
    "stream": "C:\\Users\\bigwiz\\Pictures\\6.png",
    "width": 120,
    "height": 120
  },

 "logoBuffered": {
    "buffered": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII",
    "width": 100,
    "height": 100
  },

 "tableData": {
    "table": [
      ["Name", "Age", "Country"],
      ["Alice", "30", "USA"],
      ["Bob", "25", "UK"],
      ["Charlie", "28", "Canada"]
    ]
  }
  
}
'
```

in the above example request `tableData` , `logoBuffered`, `logoStream` , `banner`, `logo` and `title` are custom tagnames used in our sample template, these can be named anything.


## Contributions
Create PR