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