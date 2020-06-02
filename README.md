# Anti-Virus
Anti-Virus project for 13 class.
support all platform, tested on windows 10 and ubuntu version 18

## Need to be installed:
* java
* python 3
* yara-python
* mysql

**all the defualt setting can be changed in the [application.properties](AntiVirus\src\main\resources\application.properties)**

## Functionality:
The program have two scanning threads, one scan is scheduled scan the runs on all the file system and analyze it, the second is requested user scan that can be started using the web user interface.

The program go through the file-system using DFS algorithm (by defualt, can be changed in the code), and save every file in mysql database.

The program go through the saved files in the database and analyze each file.

The program raises web graphical user interface in http://localhost:4060/ for the user to request scan on a spacific folder.

# File system scan:
The program scan the file system or the requested folder using DFS algorithm, each file it saves on the database.

## database defualt setting:
**all the defualt setting can be changed in the [application.properties](AntiVirus\src\main\resources\application.properties)**

defualt username and password: root : root

database name: mysql

table name: filedb

```sql
+-------+--------------+------+-----+---------+-------+
| Field | Type         | Null | Key | Default | Extra |
+-------+--------------+------+-----+---------+-------+
| id    | bigint(20)   | NO   | PRI | NULL    |       |
| hash  | varchar(255) | YES  |     | NULL    |       |
| name  | varchar(255) | YES  |     | NULL    |       |
| path  | varchar(255) | YES  | UNI | NULL    |       |
+-------+--------------+------+-----+---------+-------+
```

table name: resultscan

```sql
+---------------------+------------+------+-----+---------+-------+
| Field               | Type       | Null | Key | Default | Extra |
+---------------------+------------+------+-----+---------+-------+
| result              | int(11)    | YES  |     | NULL    |       |
| result_analyzerjson | longtext   | YES  |     | NULL    |       |
| filedb_id           | bigint(20) | NO   | PRI | NULL    |       |
+---------------------+------------+------+-----+---------+-------+
```

# Analyze files:
The program runs on all the files on the database and analyze each file (if need to be analyzed).

## hash analyzer:
For each file to be analyzed the hash file send to 2 API, [MalShare](https://malshare.com/) and [virusTotal](virustotal.com/gui/).

**both of the api require API KEY the need to be set in the [application.properties](AntiVirus\src\main\resources\application.properties) file** 

### MalShare API:
The free account in MalShare API supports 2000 API calls per day, each file sent for hash check for this API.

### VirusTotal API:
The free account in VirusTotal API supports 4 API calls per minute and 1000 API calls per day, only suspicious files are send to this API.

## yara analyzer:
For each file to be analyzed the file is sent to python script along with yara rules file found in [this folder](AntiVirus\src\main\resources\YaraRules). If the yara rule found in the file, the program check if the rule is found in the blacklist yara rules list found in [application.properties](AntiVirus\src\main\resources\application.properties) file, if not after 3 yara rules found the program declare the file as suspicious file.

# Running example:
console:

```cmd
java -jar Anti-Virus.jar
```

![console.png](MD_images\Console.png)

user interface:

![gui](MD_images\gui.png)