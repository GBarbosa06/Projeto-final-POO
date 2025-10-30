package main;

import java.util.*;
import dao.*;
import service.*;
import model.*;
import conexao.Conexao;
import java.sql.Connection;
import java.time.LocalDate;

public class Main {
	// Scanner e services como membros de instância (não estáticos)
	private Scanner sc = new Scanner(System.in);
	private Connection connection;

	// Instâncias dos services
	private PacienteService pacienteService;
	private MedicoService medicoService;
	private HospitalService hospitalService;
	private ConsultaService consultaService;
	private ExameService exameService;
	private ResultadoExameService resultadoExameService;

	public static void main(String[] args) {
		// Criar instância e executar o loop principal
		new Main().run();
	}

	// Construtor para inicializar conexão e services
	public Main() {
		Conexao conexao = new Conexao();
		this.connection = conexao.obterConexao();
		if (this.connection == null) {
			System.out.println("Erro: Não foi possível conectar ao banco de dados. Encerrando...");
			System.exit(1);
		}

		// Inicializar DAOs com conexão
        HospitalDAO hospitalDAO = new HospitalDAO(this.connection);
		PacienteDAO pacienteDAO = new PacienteDAO(this.connection, hospitalDAO);
		MedicoDAO medicoDAO = new MedicoDAO(this.connection);
		ConsultaDAO consultaDAO = new ConsultaDAO(this.connection, pacienteDAO, medicoDAO, hospitalDAO);
		ExameDAO exameDAO = new ExameDAO(this.connection, pacienteDAO, hospitalDAO);
		ResultadoExameDAO resultadoExameDAO = new ResultadoExameDAO(this.connection, exameDAO);

		// Inicializar services
		this.pacienteService = new PacienteService(pacienteDAO);
		this.medicoService = new MedicoService(medicoDAO);
		this.hospitalService = new HospitalService(hospitalDAO);
		this.consultaService = new ConsultaService(consultaDAO);
		this.exameService = new ExameService(exameDAO);
		this.resultadoExameService = new ResultadoExameService(resultadoExameDAO);
	}

	// Novo método de execução que contém o loop principal (antes em main)
	private void run() {
		int opcao;

		do {
			System.out.println("\n===== SISTEMA DE GESTÃO HOSPITALAR =====");
			System.out.println("1 - Gerenciar Pacientes");
			System.out.println("2 - Gerenciar Médicos");
			System.out.println("3 - Gerenciar Hospitais");
			System.out.println("4 - Gerenciar Consultas");
			System.out.println("5 - Gerenciar Exames");
			System.out.println("6 - Gerenciar Resultados de Exames");
			System.out.println("0 - Sair");
			System.out.print("Escolha uma opção: ");
			try {
				opcao = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				opcao = -1;
			}

			switch (opcao) {
				case 1 -> menuPaciente();
				case 2 -> menuMedico();
				case 3 -> menuHospital();
				case 4 -> menuConsulta();
				case 5 -> menuExame();
				case 6 -> menuResultadoExame();
				case 0 -> System.out.println("Encerrando o sistema...");
				default -> System.out.println("Opção inválida!");
			}

		} while (opcao != 0);
	}

	// ============ PACIENTE ============
	private void menuPaciente() {
		int opcao;
		do {
			System.out.println("\n--- Menu Paciente ---");
			System.out.println("1 - Cadastrar");
			System.out.println("2 - Listar");
			System.out.println("3 - Atualizar");
			System.out.println("4 - Remover");
			System.out.println("0 - Voltar");
			System.out.print("Opção: ");
			try {
				opcao = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				opcao = -1;
			}

			switch (opcao) {
				case 1 -> {
					try {
						System.out.print("Nome: ");
						String nome = sc.nextLine();
						System.out.print("CPF: ");
						String cpf = sc.nextLine();
						System.out.print("Data de nascimento (AAAA-MM-DD): ");
						LocalDate dataNascimento = LocalDate.parse(sc.nextLine());
						System.out.print("Telefone: ");
						String telefone = sc.nextLine();
						System.out.print("Email: ");
						String email = sc.nextLine();

						// Endereço
						System.out.println("Endereço:");
						System.out.print("Rua: ");
						String rua = sc.nextLine();
						System.out.print("Número: ");
						String numero = sc.nextLine();
						System.out.print("Bairro: ");
						String bairro = sc.nextLine();
						System.out.print("Cidade: ");
						String cidade = sc.nextLine();
						System.out.print("CEP: ");
						String cep = sc.nextLine();
						Endereco endereco = new Endereco(rua, numero, bairro, cidade, cep);

						// Selecionar hospital
						System.out.println("Hospitais disponíveis:");
						List<Hospital> hospitais = hospitalService.listarTodosHospitais();
						for (Hospital h : hospitais) {
							System.out.println(h.getId() + " - " + h.getNome());
						}
						System.out.print("ID do hospital: ");
						long idHospital = Long.parseLong(sc.nextLine());
						Hospital hospital = hospitalService.buscarHospitalPorId(idHospital);
						if (hospital == null) {
							System.out.println("Hospital não encontrado!");
							break;
						}

						Paciente p = new Paciente(0, hospital, nome, cpf, dataNascimento, telefone, email, endereco);
						pacienteService.cadastrarPaciente(p);
						System.out.println("Paciente cadastrado!");
					} catch (Exception e) {
						System.out.println("Erro ao cadastrar paciente: " + e.getMessage());
					}
				}
				case 2 -> {
					try {
						List<Paciente> lista = pacienteService.listarTodosPacientes();
						lista.forEach(p -> {
							System.out.printf("ID: %s | Hospital: %s | Nome: %s | CPF: %s | Data Nascimento: %s | Telefone: %s | Email: %s | " +
											"Rua: %s | Numero: %s | Bairro: %s | Cidade: %s | CEP: %s %n",
									p.getId(),
									p.getHospital().getNome(),
									p.getNome(),
									p.getCpf(),
									p.getDataNascimento(),
									p.getTelefone(),
									p.getEmail(),
									p.getEndereco().getRua(),
									p.getEndereco().getNumero(),
									p.getEndereco().getBairro(),
									p.getEndereco().getCidade(),
									p.getEndereco().getCep());
						});
					} catch (Exception e) {
						System.out.println("Erro ao listar pacientes: " + e.getMessage());
					}
				}
				case 3 -> {
					try {
						System.out.print("ID do paciente: ");
						long id = Long.parseLong(sc.nextLine());
						Paciente existente = pacienteService.buscarPacientePorId(id);
						if (existente == null) {
							System.out.println("Paciente não encontrado!");
							break;
						}

						System.out.print("Novo nome (" + existente.getNome() + "): ");
						String nome = sc.nextLine();
						if (nome.isEmpty()) nome = existente.getNome();

						System.out.print("Novo CPF (" + existente.getCpf() + "): ");
						String cpf = sc.nextLine();
						if (cpf.isEmpty()) cpf = existente.getCpf();

						System.out.print("Nova data nascimento (" + existente.getDataNascimento() + "): ");
						String dataStr = sc.nextLine();
						LocalDate dataNascimento = dataStr.isEmpty() ? existente.getDataNascimento() : LocalDate.parse(dataStr);

						System.out.print("Novo telefone (" + existente.getTelefone() + "): ");
						String telefone = sc.nextLine();
						if (telefone.isEmpty()) telefone = existente.getTelefone();

						System.out.print("Novo email (" + existente.getEmail() + "): ");
						String email = sc.nextLine();
						if (email.isEmpty()) email = existente.getEmail();

						// Atualizar endereço
						Endereco end = existente.getEndereco();
						System.out.print("Nova rua (" + end.getRua() + "): ");
						String rua = sc.nextLine();
						if (!rua.isEmpty()) end.setRua(rua);

						System.out.print("Novo número (" + end.getNumero() + "): ");
						String numero = sc.nextLine();
						if (!numero.isEmpty()) end.setNumero(numero);

						System.out.print("Novo bairro (" + end.getBairro() + "): ");
						String bairro = sc.nextLine();
						if (!bairro.isEmpty()) end.setBairro(bairro);

						System.out.print("Nova cidade (" + end.getCidade() + "): ");
						String cidade = sc.nextLine();
						if (!cidade.isEmpty()) end.setCidade(cidade);

						System.out.print("Novo CEP (" + end.getCep() + "): ");
						String cep = sc.nextLine();
						if (!cep.isEmpty()) end.setCep(cep);

						Paciente atualizado = new Paciente(id, existente.getHospital(), nome, cpf, dataNascimento, telefone, email, end);
						pacienteService.atualizarPaciente(atualizado);
						System.out.println("Paciente atualizado!");
					} catch (Exception e) {
						System.out.println("Erro ao atualizar paciente: " + e.getMessage());
					}
				}
				case 4 -> {
					try {
						System.out.print("ID do paciente: ");
						long id = Long.parseLong(sc.nextLine());
						pacienteService.removerPaciente(id);
						System.out.println("Paciente removido!");
					} catch (Exception e) {
						System.out.println("Erro ao remover paciente: " + e.getMessage());
					}
				}
			}
		} while (opcao != 0);
	}

	// ============ MÉDICO ============
	private void menuMedico() {
		int opcao;
		do {
			System.out.println("\n--- Menu Médico ---");
			System.out.println("1 - Cadastrar");
			System.out.println("2 - Listar");
			System.out.println("3 - Atualizar");
			System.out.println("4 - Remover");
			System.out.println("0 - Voltar");
			System.out.print("Opção: ");
			try {
				opcao = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				opcao = -1;
			}

			switch (opcao) {
				case 1 -> {
					try {
						System.out.print("Nome: ");
						String nome = sc.nextLine();
						System.out.print("CRM: ");
						String crm = sc.nextLine();
						System.out.print("Especialidade: ");
						String especialidade = sc.nextLine();
						System.out.print("Email: ");
						String email = sc.nextLine();
						Medico m = new Medico(0, nome, crm, especialidade, email);
						medicoService.cadastrarMedico(m);
						System.out.println("Médico cadastrado!");
					} catch (Exception e) {
						System.out.println("Erro ao cadastrar médico: " + e.getMessage());
					}
				}
				case 2 -> {
					try {
						medicoService.listarTodosMedicos().forEach(m -> {
							System.out.printf("ID: %s | Nome: %s | CRM: %s | Especialidade: %s | Email: %s %n",
									m.getId(),
									m.getNome(),
									m.getCrm(),
									m.getEspecialidade(),
									m.getEmail());
						});
					} catch (Exception e) {
						System.out.println("Erro ao listar médicos: " + e.getMessage());
					}
				}
				case 3 -> {
					try {
						System.out.print("ID: ");
						long id = Long.parseLong(sc.nextLine());
						Medico existente = medicoService.buscarMedicoPorId(id);
						if (existente == null) {
							System.out.println("Médico não encontrado!");
							break;
						}
						System.out.print("Novo nome (" + existente.getNome() + "): ");
						String nome = sc.nextLine();
						if (nome.isEmpty()) nome = existente.getNome();
						System.out.print("Novo CRM (" + existente.getCrm() + "): ");
						String crm = sc.nextLine();
						if (crm.isEmpty()) crm = existente.getCrm();
						System.out.print("Nova especialidade (" + existente.getEspecialidade() + "): ");
						String especialidade = sc.nextLine();
						if (especialidade.isEmpty()) especialidade = existente.getEspecialidade();
						System.out.print("Novo email (" + existente.getEmail() + "): ");
						String email = sc.nextLine();
						if (email.isEmpty()) email = existente.getEmail();
						Medico atualizado = new Medico(id, nome, crm, especialidade, email);
						medicoService.atualizarMedico(atualizado);
						System.out.println("Médico atualizado!");
					} catch (Exception e) {
						System.out.println("Erro ao atualizar médico: " + e.getMessage());
					}
				}
				case 4 -> {
					try {
						System.out.print("ID: ");
						long id = Long.parseLong(sc.nextLine());
						medicoService.removerMedico(id);
						System.out.println("Médico removido!");
					} catch (Exception e) {
						System.out.println("Erro ao remover médico: " + e.getMessage());
					}
				}
			}
		} while (opcao != 0);
	}

	// ============ HOSPITAL ============
	private void menuHospital() {
		int opcao;
		do {
			System.out.println("\n--- Menu Hospital ---");
			System.out.println("1 - Cadastrar");
			System.out.println("2 - Listar");
			System.out.println("3 - Atualizar");
			System.out.println("4 - Remover");
			System.out.println("0 - Voltar");
			System.out.print("Opção: ");
			try {
				opcao = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				opcao = -1;
			}

			switch (opcao) {
				case 1 -> {
					try {
						System.out.print("Nome: ");
						String nome = sc.nextLine();
						System.out.print("CNPJ: ");
						String cnpj = sc.nextLine();
						System.out.print("Telefone: ");
						String telefone = sc.nextLine();

						// Endereço
						System.out.println("Endereço:");
						System.out.print("Rua: ");
						String rua = sc.nextLine();
						System.out.print("Número: ");
						String numero = sc.nextLine();
						System.out.print("Bairro: ");
						String bairro = sc.nextLine();
						System.out.print("Cidade: ");
						String cidade = sc.nextLine();
						System.out.print("CEP: ");
						String cep = sc.nextLine();
						Endereco endereco = new Endereco(rua, numero, bairro, cidade, cep);

						Hospital h = new Hospital(0L, nome, cnpj, endereco, telefone);
						hospitalService.cadastrarHospital(h);
						System.out.println("Hospital cadastrado!");
					} catch (Exception e) {
						System.out.println("Erro ao cadastrar hospital: " + e.getMessage());
					}
				}
				case 2 -> {
					try {
						hospitalService
								.listarTodosHospitais()
								.forEach((h) -> {
									System.out.printf("ID: %s | Nome %s | CPNJ : %s | Telefone: %s |" +
											" Rua: %s | Numero: %s | Bairro: %s | Cidade: %s | CEP: %s %n",
											h.getId(), h.getNome(), h.getCnpj(), h.getTelefone(),
											h.getEndereco().getRua(), h.getEndereco().getNumero(),
											h.getEndereco().getBairro(), h.getEndereco().getCidade(),
											h.getEndereco().getCep());
								});
					} catch (Exception e) {
						System.out.println("Erro ao listar hospitais: " + e.getMessage());
					}
				}
				case 3 -> {
					try {
						System.out.print("ID: ");
						long id = Long.parseLong(sc.nextLine());
						Hospital existente = hospitalService.buscarHospitalPorId(id);
						if (existente == null) {
							System.out.println("Hospital não encontrado!");
							break;
						}
						System.out.print("Novo nome (" + existente.getNome() + "): ");
						String nome = sc.nextLine();
						if (nome.isEmpty()) nome = existente.getNome();
						System.out.print("Novo CNPJ (" + existente.getCnpj() + "): ");
						String cnpj = sc.nextLine();
						if (cnpj.isEmpty()) cnpj = existente.getCnpj();
						System.out.print("Novo telefone (" + existente.getTelefone() + "): ");
						String telefone = sc.nextLine();
						if (telefone.isEmpty()) telefone = existente.getTelefone();

						// Atualizar endereço
						Endereco end = existente.getEndereco();
						System.out.print("Nova rua (" + end.getRua() + "): ");
						String rua = sc.nextLine();
						if (!rua.isEmpty()) end.setRua(rua);
						System.out.print("Novo número (" + end.getNumero() + "): ");
						String numero = sc.nextLine();
						if (!numero.isEmpty()) end.setNumero(numero);
						System.out.print("Novo bairro (" + end.getBairro() + "): ");
						String bairro = sc.nextLine();
						if (!bairro.isEmpty()) end.setBairro(bairro);
						System.out.print("Nova cidade (" + end.getCidade() + "): ");
						String cidade = sc.nextLine();
						if (!cidade.isEmpty()) end.setCidade(cidade);
						System.out.print("Novo CEP (" + end.getCep() + "): ");
						String cep = sc.nextLine();
						if (!cep.isEmpty()) end.setCep(cep);

						Hospital atualizado = new Hospital(id, nome, cnpj, end, telefone);
						hospitalService.atualizarHospital(atualizado);
						System.out.println("Hospital atualizado!");
					} catch (Exception e) {
						System.out.println("Erro ao atualizar hospital: " + e.getMessage());
					}
				}
				case 4 -> {
					try {
						System.out.print("ID: ");
						long id = Long.parseLong(sc.nextLine());
						hospitalService.removerHospital(id);
						System.out.println("Hospital removido!");
					} catch (Exception e) {
						System.out.println("Erro ao remover hospital: " + e.getMessage());
					}
				}
			}
		} while (opcao != 0);
	}

	// ============ CONSULTA ============
	private void menuConsulta() {
		int opcao;
		do {
			System.out.println("\n--- Menu Consulta ---");
			System.out.println("1 - Agendar");
			System.out.println("2 - Listar");
			System.out.println("3 - Atualizar");
			System.out.println("4 - Cancelar");
			System.out.println("0 - Voltar");
			System.out.print("Opção: ");
			try {
				opcao = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				opcao = -1;
			}

			switch (opcao) {
				case 1 -> {
					try {
						// Selecionar paciente
						System.out.println("Pacientes disponíveis:");
						List<Paciente> pacientes = pacienteService.listarTodosPacientes();
						for (Paciente p : pacientes) {
							System.out.println(p.getId() + " - " + p.getNome());
						}
						System.out.print("ID Paciente: ");
						long idPaciente = Long.parseLong(sc.nextLine());
						Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
						if (paciente == null) {
							System.out.println("Paciente não encontrado!");
							break;
						}

						// Selecionar médico
						System.out.println("Médicos disponíveis:");
						List<Medico> medicos = medicoService.listarTodosMedicos();
						for (Medico m : medicos) {
							System.out.println(m.getId() + " - " + m.getNome() + " (" + m.getEspecialidade() + ")");
						}
						System.out.print("ID Médico: ");
						long idMedico = Long.parseLong(sc.nextLine());
						Medico medico = medicoService.buscarMedicoPorId(idMedico);
						if (medico == null) {
							System.out.println("Médico não encontrado!");
							break;
						}

						System.out.print("Data (AAAA-MM-DD): ");
						LocalDate data = LocalDate.parse(sc.nextLine());
						System.out.print("Observações: ");
						String observacoes = sc.nextLine();

						Consulta c = new Consulta(0, paciente, medico, paciente.getHospital(), data, observacoes, StatusConsulta.AGENDADA);
						consultaService.cadastrarConsulta(c);
						System.out.println("Consulta agendada!");
					} catch (Exception e) {
						System.out.println("Erro ao agendar consulta: " + e.getMessage());
					}
				}
				case 2 -> {
					try {
						consultaService.listarTodasConsultas().forEach(c -> {
							System.out.printf("ID: %s | Paciente ID: %s | Nome do Paciente: %s | Médico ID: %s | Nome do Médico: %s | Hospital ID: %s | Nome do Hospital: %s | Data Consulta: %s | Observações: %s | Status: %s %n",
									c.getId(),
									c.getPaciente().getId(),
									c.getPaciente().getNome(),
									c.getMedico().getId(),
									c.getMedico().getNome(),
									c.getHospital().getId(),
									c.getHospital().getNome(),
									c.getDataConsulta(),
									c.getObservacoes(),
									c.getStatus());
						});
					} catch (Exception e) {
						System.out.println("Erro ao listar consultas: " + e.getMessage());
					}
				}
				case 3 -> {
					try {
						System.out.print("ID da consulta: ");
						long id = Long.parseLong(sc.nextLine());
						Consulta existente = consultaService.buscarConsultaPorId(id);
						if (existente == null) {
							System.out.println("Consulta não encontrada!");
							break;
						}
						System.out.print("Nova data (AAAA-MM-DD) (" + existente.getDataConsulta() + "): ");
						String dataStr = sc.nextLine();
						LocalDate data = dataStr.isEmpty() ? existente.getDataConsulta() : LocalDate.parse(dataStr);
						System.out.print("Novas observações (" + existente.getObservacoes() + "): ");
						String observacoes = sc.nextLine();
						if (observacoes.isEmpty()) observacoes = existente.getObservacoes();

						Consulta atualizada = new Consulta(id, existente.getPaciente(), existente.getMedico(), existente.getHospital(), data, observacoes, existente.getStatus());
						consultaService.atualizarConsulta(atualizada);
						System.out.println("Consulta atualizada!");
					} catch (Exception e) {
						System.out.println("Erro ao atualizar consulta: " + e.getMessage());
					}
				}
				case 4 -> {
					try {
						System.out.print("ID da consulta: ");
						long id = Long.parseLong(sc.nextLine());
						Consulta existente = consultaService.buscarConsultaPorId(id);
						if (existente != null) {
							existente.setStatus(StatusConsulta.CANCELADA);
							consultaService.atualizarConsulta(existente);
						}
						System.out.println("Consulta cancelada!");
					} catch (Exception e) {
						System.out.println("Erro ao cancelar consulta: " + e.getMessage());
					}
				}
			}
		} while (opcao != 0);
	}

	// ============ EXAME ============
	private void menuExame() {
		int opcao;
		do {
			System.out.println("\n--- Menu Exame ---");
			System.out.println("1 - Agendar");
			System.out.println("2 - Listar");
			System.out.println("3 - Atualizar");
			System.out.println("4 - Cancelar");
			System.out.println("0 - Voltar");
			System.out.print("Opção: ");
			try {
				opcao = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				opcao = -1;
			}

			switch (opcao) {
				case 1 -> {
					try {
						// Selecionar paciente
						System.out.println("Pacientes disponíveis:");
						List<Paciente> pacientes = pacienteService.listarTodosPacientes();
						for (Paciente p : pacientes) {
							System.out.println(p.getId() + " - " + p.getNome());
						}
						System.out.print("ID Paciente: ");
						long idPaciente = Long.parseLong(sc.nextLine());
						Paciente paciente = pacienteService.buscarPacientePorId(idPaciente);
						if (paciente == null) {
							System.out.println("Paciente não encontrado!");
							break;
						}

						System.out.print("Tipo de exame: ");
						String tipoExame = sc.nextLine();
						System.out.print("Data (AAAA-MM-DD): ");
						LocalDate data = LocalDate.parse(sc.nextLine());

						Exame e = new Exame(0, paciente, paciente.getHospital(), tipoExame, data, StatusExame.AGENDADO);
						exameService.cadastrarExame(e);
						System.out.println("Exame agendado!");
					} catch (Exception e) {
						System.out.println("Erro ao agendar exame: " + e.getMessage());
					}
				}
				case 2 -> {
					try {
						exameService.listarTodosExames().forEach(e -> {
							System.out.printf(
									"ID: %s | Paciente: %s (ID: %s) | Hospital: %s (ID: %s) | Tipo Exame: %s | Data Exame: %s | Status: %s %n",
									e.getId(),
									e.getPaciente().getNome(),
									e.getPaciente().getId(),
									e.getHospital().getNome(),
									e.getHospital().getId(),
									e.getTipoExame(),
									e.getDataExame(),
									e.getStatus()
							);
						});

					} catch (Exception e) {
						System.out.println("Erro ao listar exames: " + e.getMessage());
					}
				}
				case 3 -> {
					try {
						System.out.print("ID do exame: ");
						long id = Long.parseLong(sc.nextLine());
						Exame existente = exameService.buscarExamePorId(id);
						if (existente == null) {
							System.out.println("Exame não encontrado!");
							break;
						}
						System.out.print("Novo tipo (" + existente.getTipoExame() + "): ");
						String tipo = sc.nextLine();
						if (tipo.isEmpty()) tipo = existente.getTipoExame();
						System.out.print("Nova data (AAAA-MM-DD) (" + existente.getDataExame() + "): ");
						String dataStr = sc.nextLine();
						LocalDate data = dataStr.isEmpty() ? existente.getDataExame() : LocalDate.parse(dataStr);

						Exame atualizado = new Exame(id, existente.getPaciente(), existente.getHospital(), tipo, data, existente.getStatus());
						exameService.atualizarExame(atualizado);
						System.out.println("Exame atualizado!");
					} catch (Exception e) {
						System.out.println("Erro ao atualizar exame: " + e.getMessage());
					}
				}
				case 4 -> {
					try {
						System.out.print("ID do exame: ");
						long id = Long.parseLong(sc.nextLine());
						Exame existente = exameService.buscarExamePorId(id);
						if (existente != null) {
							existente.setStatus(StatusExame.CANCELADO);
							exameService.atualizarExame(existente);
						}
						System.out.println("Exame cancelado!");
					} catch (Exception e) {
						System.out.println("Erro ao cancelar exame: " + e.getMessage());
					}
				}
			}
		} while (opcao != 0);
	}

	// ============ RESULTADO DE EXAME ============
	private void menuResultadoExame() {
		int opcao;
		do {
			System.out.println("\n--- Menu Resultado de Exame ---");
			System.out.println("1 - Registrar");
			System.out.println("2 - Listar");
			System.out.println("3 - Atualizar");
			System.out.println("4 - Remover");
			System.out.println("0 - Voltar");
			System.out.print("Opção: ");
			try {
				opcao = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				opcao = -1;
			}

			switch (opcao) {
				case 1 -> {
					try {
						// Selecionar exame
						System.out.println("Exames disponíveis:");
						List<Exame> exames = exameService.listarTodosExames();
						for (Exame ex : exames) {
							System.out.println(ex.getId() + " - " + ex.getTipoExame() + " (" + ex.getPaciente().getNome() + ")");
						}
						System.out.print("ID do exame: ");
						long idExame = Long.parseLong(sc.nextLine());
						Exame exame = exameService.buscarExamePorId(idExame);
						if (exame == null) {
							System.out.println("Exame não encontrado!");
							break;
						}

						System.out.print("Arquivo (caminho): ");
						String arquivo = sc.nextLine();
						System.out.print("Laudo: ");
						String laudo = sc.nextLine();
						System.out.print("Data do resultado (AAAA-MM-DD): ");
						LocalDate dataResultado = LocalDate.parse(sc.nextLine());

						ResultadoExame r = new ResultadoExame(0, exame, arquivo, laudo, dataResultado);
						resultadoExameService.cadastrarResultadoExame(r);
						System.out.println("Resultado registrado!");
					} catch (Exception e) {
						System.out.println("Erro ao registrar resultado: " + e.getMessage());
					}
				}
				case 2 -> {
					try {
						resultadoExameService.listarTodosResultadosExame().forEach(r -> {
							System.out.printf(
									"ID: %s | Exame: %s (ID: %s) | Arquivo: %s | Laudo: %s | Data Resultado: %s %n",
									r.getId(),
									r.getExame().getTipoExame(),
									r.getExame().getId(),
									r.getArquivo(),
									r.getLaudo(),
									r.getDataResultado()
							);
						});

					} catch (Exception e) {
						System.out.println("Erro ao listar resultados: " + e.getMessage());
					}
				}
				case 3 -> {
					try {
						System.out.print("ID do resultado: ");
						long id = Long.parseLong(sc.nextLine());
						ResultadoExame existente = resultadoExameService.buscarResultadoExamePorId(id);
						if (existente == null) {
							System.out.println("Resultado não encontrado!");
							break;
						}
						System.out.print("Novo arquivo (" + existente.getArquivo() + "): ");
						String arquivo = sc.nextLine();
						if (arquivo.isEmpty()) arquivo = existente.getArquivo();
						System.out.print("Novo laudo (" + existente.getLaudo() + "): ");
						String laudo = sc.nextLine();
						if (laudo.isEmpty()) laudo = existente.getLaudo();
						System.out.print("Nova data (AAAA-MM-DD) (" + existente.getDataResultado() + "): ");
						String dataStr = sc.nextLine();
						LocalDate dataResultado = dataStr.isEmpty() ? existente.getDataResultado() : LocalDate.parse(dataStr);

						ResultadoExame atualizado = new ResultadoExame(id, existente.getExame(), arquivo, laudo, dataResultado);
						resultadoExameService.atualizarResultadoExame(atualizado);
						System.out.println("Resultado atualizado!");
					} catch (Exception e) {
						System.out.println("Erro ao atualizar resultado: " + e.getMessage());
					}
				}
				case 4 -> {
					try {
						System.out.print("ID: ");
						long id = Long.parseLong(sc.nextLine());
						resultadoExameService.removerResultadoExame(id);
						System.out.println("Resultado removido!");
					} catch (Exception e) {
						System.out.println("Erro ao remover resultado: " + e.getMessage());
					}
				}
			}
		} while (opcao != 0);
	}
}
