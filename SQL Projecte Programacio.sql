
-- Taula de graus (Degree)
CREATE TABLE IF NOT EXISTS Degree (
    Degree_id CHAR(3) NOT NULL PRIMARY KEY,
    Name VARCHAR(30) NOT NULL UNIQUE,
    Description VARCHAR(255)
);

-- Taula d'empreses (Companies) amb clau forana per degree_id
CREATE TABLE IF NOT EXISTS Companies (
    Id CHAR(3) NOT NULL PRIMARY KEY,
    Name VARCHAR(30) NOT NULL,
    Description VARCHAR(255),
    Logo BLOB,
    Degree_id CHAR(3) NOT NULL,
    CONSTRAINT FKdegree_id FOREIGN KEY (Degree_id) REFERENCES Degree(Degree_id)
);

-- Taula de seus o delegacions (Branches) amb clau forana per company_id
CREATE TABLE IF NOT EXISTS Branches (
Address VARCHAR(50) NOT NULL,
  Zipcode VARCHAR(5) NOT NULL,
  City VARCHAR(30) NOT NULL,
  Province VARCHAR(30),
  Id CHAR(3) NOT NULL,
  CONSTRAINT FKcompany_id FOREIGN KEY (Id) REFERENCES Companies(Id)
);

-- Inserir dades a Degree
INSERT INTO Degree (Degree_id, name, description) VALUES 
('001', 'DAM', 'Desenvolupament d''Aplicacions Multiplataforma'),
('002', 'DAW', 'Desenvolupament d''Aplicacions Web'),
('003', 'ASIX', 'Administració de Sistemes Informàtics en Xarxa');

-- Inserir dades a Companies (ara amb degree_id)
INSERT INTO Companies (id, name, description, logo, Degree_id) VALUES
('001', 'SEMIC', 'Somos una empresa de servicios y soluciones IT con más de 40 años de experiencia en el sector. Proporcionamos las mejores y más novedosas soluciones de IT para que las empresas puedan adaptarse a los nuevos retos y formas de trabajo sin perder el ritmo.', '', '003'), -- ASIX
('002', 'Mon Sant Benet', 'Món Sant Benet es un complejo cultural, turístico y de ocio único, impulsado por la Fundació Catalunya La Pedrera, que combina historia, gastronomía, naturaleza y cultura.', '', '003'), -- ASIX
('003', 'THOR', 'THOR Especialidades, S.A. es una filial de Grupo THOR, empresa multinacional de origen inglés fabricante de una innovadora y tecnológica gama de especialidades químicas; respaldados por un soporte técnico especializado enfocado en lograr una óptima relación precio-efectividad.', '', '001'), -- DAM
('004', 'SEMIC', 'Somos una empresa de servicios y soluciones IT con más de 40 años de experiencia en el sector. Proporcionamos las mejores y más novedosas soluciones de IT para que las empresas puedan adaptarse a los nuevos retos y formas de trabajo sin perder el ritmo.', '', '001'), -- DAM
('005', 'UVE', 'UVE (UVE Solutions) es una empresa española que se enfoca en impulsar el negocio de fabricantes y distribuidores en el sector del gran consumo, utilizando datos, análisis y soluciones de ejecución para optimizar el "Route to Market', '', '001'), -- DAM
('006', 'Social Lovers', 'Social Lovers es una empresa de informática que ofrece una amplia gama de productos y servicios tecnológicos tanto a empresas como a particulares.', '', '002'), -- DAW
('007', 'Control Grup', 'Aportamos soluciones de impresión, gestión documental, servicios informáticos, de seguridad y comunicaciones para conseguir la máxima eficiencia en tu empresa, despacho profesional o administración pública.', '', '002'); -- DAW

-- Inserir dades a Branches (ara amb company_id)
INSERT INTO Branches (address, zipcode, city, province, id) VALUES
('Moixeró, 19', '08272', 'Sant Fruitós de Bages', 'Barcelona', '001'), -- SEMIC
('Carrer de Gaspar Fàbregas i Roses, 88', '08950', 'Esplugues de Llobregat', 'Barcelona', '001'), -- SEMIC
('Camí de Sant Benet, Local 1', '08272', 'Sant Fruitós de Bages', 'Barcelona', '002'), -- Mon Sant Benet
('Polígon Industrial Pla del Camí, Avenida de Industria, 1', '08297', 'Castellgalí', 'Barcelona', '003'), -- THOR
('Moixeró, 19', '08272', 'Sant Fruitós de Bages', 'Barcelona', '004'), -- SEMIC (DAM)
('Carrer de Gaspar Fàbregas i Roses, 88', '08950', 'Esplugues de Llobregat', 'Barcelona', '004'), -- SEMIC (DAM)
('Plaça de la Ciència, 1', '08243', 'Manresa', 'Barcelona', '005'), -- UVE
('Complex Catalana Parc, Carrer Jesús Serra Santamans, 4, Planta 3, Oficina 2', '08174', 'Sant Cugat del Vallès', 'Barcelona', '005'), -- UVE
('Carrer Àngel Guimerà, 26', '08272', 'Sant Fruitós de Bages', 'Barcelona', '006'), -- Social Lovers
('Carrer de Sant Fruitós, 10, BAIXOS', '08242', 'Manresa', 'Barcelona', '006'), -- Social Lovers
('Polígon Industrial Casanova, Carrer Montsant, 14-16', '08272', 'Sant Fruitós de Bages', 'Barcelona', '007'); -- Control Grup

