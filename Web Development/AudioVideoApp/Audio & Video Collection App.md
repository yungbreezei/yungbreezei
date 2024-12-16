# 🎶 Project: Audio/Video Collection Web App
![AppLogo](https://github.com/yungbreezei/yungbreezei/blob/main/Web%20Development/AudioVideoApp/images/Designer.png)

***

## Task
Front-end, processing, back-end, and general design diagrams illustrating your web app workflow process
- [Prototype made with Figma](https://github.com/yungbreezei/yungbreezei/blob/main/Web%20Development/AudioVideoApp/Prototype.png)
- [Presentation]()



***
- [QUERY RESULTS](https://github.com/yungbreezei/yungbreezei/blob/main/Web%20Development/AudioVideoApp/BriaWright-SQLdata-media.txt)

````sql
CREATE TABLE pictures (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(50),
    photographer VARCHAR(100),
    year_taken INT,
    url VARCHAR(255)
);

CREATE TABLE media (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    genre VARCHAR(50),
    artist VARCHAR(100),
    type VARCHAR(10),
    year INT,
    url VARCHAR(255)
);

CREATE TABLE video (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(50),
    creator VARCHAR(100),
    release_year INT,
    url VARCHAR(255)
);

````
