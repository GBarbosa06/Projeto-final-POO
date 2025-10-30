CREATE TABLE hospital (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL,
    telefone VARCHAR(20),
    rua VARCHAR(255),
    numero VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    cep VARCHAR(20)
);

CREATE TABLE paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    data_nascimento DATE,
    telefone VARCHAR(20),
    email VARCHAR(255),
    rua VARCHAR(255),
    numero VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    cep VARCHAR(20),
    FOREIGN KEY (hospital_id) REFERENCES hospital(id)
);

CREATE TABLE medico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    crm VARCHAR(20) NOT NULL,
    especialidade VARCHAR(100),
    email VARCHAR(255)
);

CREATE TABLE exame (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    hospital_id BIGINT NOT NULL,
    tipo_exame VARCHAR(100),
    data_exame DATE,
    status VARCHAR(50),
    FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    FOREIGN KEY (hospital_id) REFERENCES hospital(id)
);

CREATE TABLE consulta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    hospital_id BIGINT NOT NULL,
    data_consulta DATE,
    observacoes TEXT,
    status VARCHAR(50),
    FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    FOREIGN KEY (medico_id) REFERENCES medico(id),
    FOREIGN KEY (hospital_id) REFERENCES hospital(id)
);

CREATE TABLE resultado_exame (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exame_id BIGINT NOT NULL,
    arquivo VARCHAR(255),
    laudo TEXT,
    data_resultado DATE,
    FOREIGN KEY (exame_id) REFERENCES exame(id)
);

INSERT INTO hospital (nome, cnpj, telefone, rua, numero, bairro, cidade, cep) VALUES
('Hospital Santa Luzia', '12.345.678/0001-90', '(11) 3456-7890', 'Av. Paulista', '1000', 'Bela Vista', 'São Paulo', '01310-100'),
('Hospital Vida Plena', '98.765.432/0001-21', '(21) 99876-5432', 'Rua das Flores', '200', 'Centro', 'Rio de Janeiro', '20010-010'),
('Hospital São Rafael', '55.222.333/0001-44', '(31) 3344-5566', 'Av. Afonso Pena', '500', 'Funcionários', 'Belo Horizonte', '30130-001');

INSERT INTO medico (nome, crm, especialidade, email) VALUES
('Dr. João Silva', 'CRM-SP-12345', 'Cardiologia', 'joao.silva@hospital.com'),
('Dra. Marina Costa', 'CRM-RJ-67890', 'Pediatria', 'marina.costa@hospital.com'),
('Dr. Pedro Almeida', 'CRM-MG-54321', 'Ortopedia', 'pedro.almeida@hospital.com');

INSERT INTO paciente (hospital_id, nome, cpf, data_nascimento, telefone, email, rua, numero, bairro, cidade, cep) VALUES
(1, 'Ana Souza', '111.222.333-44', '1990-03-15', '(11) 91234-5678', 'ana.souza@gmail.com', 'Rua Vergueiro', '150', 'Liberdade', 'São Paulo', '01504-001'),
(2, 'Carlos Pereira', '222.333.444-55', '1985-07-22', '(21) 97777-8888', 'carlos.pereira@yahoo.com', 'Rua México', '80', 'Centro', 'Rio de Janeiro', '20031-142'),
(3, 'Fernanda Lima', '333.444.555-66', '1995-11-02', '(31) 98888-9999', 'fernanda.lima@hotmail.com', 'Rua da Bahia', '320', 'Funcionários', 'Belo Horizonte', '30160-011');

INSERT INTO exame (paciente_id, hospital_id, tipo_exame, data_exame, status) VALUES
(1, 1, 'Hemograma', '2025-10-20', 'Concluído'),
(2, 2, 'Raio-X', '2025-10-22', 'Pendente'),
(3, 3, 'Ressonância Magnética', '2025-10-25', 'Concluído');

INSERT INTO consulta (paciente_id, medico_id, hospital_id, data_consulta, observacoes, status) VALUES
(1, 1, 1, '2025-10-15', 'Paciente com dores no peito.', 'Concluída'),
(2, 2, 2, '2025-10-18', 'Consulta de rotina pediátrica.', 'Agendada'),
(3, 3, 3, '2025-10-20', 'Dor no joelho após atividade física.', 'Concluída');

INSERT INTO resultado_exame (exame_id, arquivo, laudo, data_resultado) VALUES
(1, 'hemograma_ana.pdf', 'Exame dentro dos parâmetros normais.', '2025-10-21'),
(3, 'ressonancia_fernanda.pdf', 'Lesão leve detectada no joelho esquerdo.', '2025-10-26');
