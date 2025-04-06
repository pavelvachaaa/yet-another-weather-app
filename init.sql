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


