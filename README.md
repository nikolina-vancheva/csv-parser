# csv-parser

**Description:**

Spring Boot application that exposes only one API:
- POST "/invoices/csv/upload?outputFormat={outputFormat}}".

The API expects CSV file with invoice data to be uploaded and then is able to produce two output formats: "csv" or "xml".
The query parameter {outputFormat} is mandatory and its value should be one of them.

When "csv" is selected as an output format the data from the uploaded CSV file is grouped by "buyer" and new
CSV file is created for every group. The newly created output CSV files are then downloaded as zip.

When "xml" is selected as an output format the data from the uploaded CSV file is grouped by "buyer" and new
XML file is created for every group. The newly created output XML files are then downloaded as zip. The difference here
is that the output XML files don`t contain "invoice_image" column but instead its values are Base64 decoded and
stored as images in "/src/main/resources/images".
