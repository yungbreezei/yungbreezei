# 🎶 Project: Audio/Video Collection Web App
![AppLogo](https://github.com/yungbreezei/yungbreezei/blob/main/Web%20Development/AudioVideoApp/images/Designer.png)

***

## Task
Front-end, processing, back-end, and general design diagrams illustrating your web app workflow process
- [Prototype made with Figma](https://github.com/yungbreezei/yungbreezei/blob/main/Web%20Development/AudioVideoApp/Prototype.png)
- [Presentation](https://github.com/yungbreezei/yungbreezei/blob/main/Web%20Development/AudioVideoApp/presentation.pdf)



***
- [QUERY RESULTS](https://github.com/yungbreezei/yungbreezei/blob/main/Web%20Development/AudioVideoApp/SQLdata-media.txt)

````sql
CREATE TABLE pictures (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(50),
    photographer VARCHAR(100),
    year_taken INT,
    url VARCHAR(255)
);
INSERT INTO pictures (title, category, photographer, year_taken, url)
VALUES 
    ('Sunset Bliss', 'Nature', 'Annie Leibovitz', 2018, 'http://example.com/sunset-bliss.jpg'),
    ('City Lights', 'Urban', 'Steve McCurry', 2019, 'http://example.com/city-lights.jpg'),
    ('Mountain Peaks', 'Landscape', 'Peter Lik', 2020, 'http://example.com/mountain-peaks.jpg'),
    ('Autumn Leaves', 'Nature', 'Galen Rowell', 2017, 'http://example.com/autumn-leaves.jpg'),
    ('Vintage Car', 'Lifestyle', 'Henri Cartier-Bresson', 2016, 'http://example.com/vintage-car.jpg'),
    ('Ocean Waves', 'Seascape', 'Sebastião Salgado', 2021, 'http://example.com/ocean-waves.jpg'),
    ('Portrait of a Lady', 'Portrait', 'Diane Arbus', 2015, 'http://example.com/portrait-of-a-lady.jpg');


CREATE TABLE media (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    genre VARCHAR(50),
    artist VARCHAR(100),
    type VARCHAR(10),
    year INT,
    url VARCHAR(255)
);
INSERT INTO media (title, genre, artist, type, year, url)
VALUES 
    ('Misery Business', 'ALT POP', 'Paramore', 'MP4', 2007, 'http://example.com/misery-business.mp4'),
INSERT INTO media (title, genre, artist, type, year, url)
VALUES 
    ('Blinding Lights', 'POP', 'The Weeknd', 'MP4', 2020, 'http://example.com/blinding-lights.mp4'),
    ('N95', 'RAP', 'Kendrick Lamar', 'MP4', 2022, 'http://example.com/n95.mp4');


CREATE TABLE video (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(50),
    creator VARCHAR(100),
    release_year INT,
    url VARCHAR(255)
);
INSERT INTO video (title, genre, creator, release_year, url)
VALUES 
    ('Inception', 'Sci-Fi', 'Christopher Nolan', 2010, 'http://example.com/inception.mp4'),
    ('Interstellar', 'Sci-Fi', 'Christopher Nolan', 2014, 'http://example.com/interstellar.mp4'),
    ('Parasite', 'Thriller', 'Bong Joon-ho', 2019, 'http://example.com/parasite.mp4'),
    ('The Matrix', 'Action', 'Wachowski Sisters', 1999, 'http://example.com/the-matrix.mp4'),
    ('The Social Network', 'Drama', 'David Fincher', 2010, 'http://example.com/the-social-network.mp4');


````
