USE yet_another_weather_app;


CREATE TABLE Country (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE City (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      latitude DOUBLE NOT NULL,
                      longitude DOUBLE NOT NULL,
                      country_id INT NOT NULL,
                      FOREIGN KEY (country_id) REFERENCES Country(id)
);

CREATE INDEX idx_city_country_id ON City(country_id);

CREATE TABLE Measurement (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             datetime DATETIME NOT NULL,
                             temp DECIMAL(7,2) NOT NULL,
                             pressure DECIMAL(7,2) NOT NULL,
                             humidity DECIMAL(7,2) NOT NULL,
                             temp_min DECIMAL(7,2) NOT NULL,
                             temp_max DECIMAL(7,2) NOT NULL,
                             weather VARCHAR(50) NOT NULL,
                             weather_desc VARCHAR(255) NOT NULL,
                             wind_speed DECIMAL(5,2) NOT NULL,
                             wind_deg INT NOT NULL,
                             city_id INT,
                             FOREIGN KEY (city_id) REFERENCES City(id)
);

CREATE INDEX idx_measurement_city_id ON Measurement(city_id);

CREATE INDEX idx_measurement_datetime ON Measurement(datetime);
CREATE INDEX idx_measurement_weather ON Measurement(weather);


INSERT INTO Country(id, name)
VALUES (1, 'Czech'),
       (2, 'Germany'),
       (3, 'Denmark');

INSERT INTO City(id, name, longitude, latitude, country_id)
VALUES (1, 'Prague', 14.42076, 50.088039, 1),
       (2, 'Liberec', 15.05619, 50.767109, 1),
       (3, 'Berlin', 13.41053, 52.524368, 3),
       (4, 'London', -0.12574, 51.50853, 2),
       (5, 'Copenhagen', 14.83333, 50.900002, 3);