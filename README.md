# Dependencies
 - scala 2.12.3
 - sbt
 - python 3

# How to run

### Install flask server requirements
```
pip3 install -r requirements.txt
```

### Build application jar
```
sbt assembly
```

### Run unit tests
```
sbt test
```

### Set paths to jar and excel sheet in config.py 
```
{
  "jar_path": "/path/to/assembly/jar",
  "sheet_path": "/path/to/xlsx/sheet"
}
```

### Start the server
```
python3 server.py -c config.py
```
go to http://127.0.0.1:5000/

 
