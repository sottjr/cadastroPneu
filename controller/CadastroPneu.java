package com.lecom.workflow.cadastros.pneus.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.log4j.Logger;
import org.apache.http.client.ClientProtocolException;

import com.lecom.tecnologia.db.DBUtils;
import com.lecom.workflow.cadastros.common.util.Funcoes;
import com.lecom.workflow.vo.IntegracaoVO;

import br.com.lecom.atos.servicos.annotation.Execution;
import br.com.lecom.atos.servicos.annotation.IntegrationModule;
import br.com.lecom.atos.servicos.annotation.Version;

@IntegrationModule("CadastroPneu")
@Version({ 1, 0, 0 })
public class CadastroPneu {
	private final Logger logger = Logger.getLogger(this.getClass());
	// private static final Logger logger =
	// Logger.getLogger(this.CadastroPneu.class);

	@Execution
	public void realizarCadastroPneu(IntegracaoVO integracaoVO) throws Exception {
		// String msgPneu = "";
		logger.info("====== INICIO CadastroPneu ======");

		try {
			// try (Connection conWF2 = DBUtils.getConnection("WorkFlow")) {
			// deleteItensGrid(integracaoVO, conWF2, "G_relat_aux_idiINSCRICAO");
			// }
			integracaoVO.setConexao("BPM_AUX");

			List<Map<String, Object>> listaGrid = integracaoVO.getDadosModeloGrid("CADPNEU");
			logger.info("resultado insert: " + listaGrid);
			for (Map<String, Object> ob : listaGrid) {
				logger.debug("# ob: " + ob);
				logger.info("Isto é o listaGrid: " + listaGrid);
				logger.info("Isto é o ob: " + ob);

				try (Connection con = integracaoVO.getConexao()) {
					StringBuilder inserirPneu = new StringBuilder();
					inserirPneu.append("INSERT INTO 	             ");
					inserirPneu.append("	pneu(            		 ");
					inserirPneu.append("    cd_pneu,                 ");
					inserirPneu.append("    nm_fabrica,                 ");
					inserirPneu.append("    cd_fabrica,                  ");
					inserirPneu.append("    nm_modelo,              ");
					inserirPneu.append("    cd_modelo,     ");
					inserirPneu.append("    nm_tipo_borra,         ");
					inserirPneu.append("    cd_tipo_borra, ");
					inserirPneu.append("    nm_desenho,         ");
					inserirPneu.append("    cd_desenho,         ");
					inserirPneu.append("    nr_serie,         ");
					inserirPneu.append("    nr_vida,         ");
					inserirPneu.append("    libras,         ");
					inserirPneu.append("    dot,         ");
					inserirPneu.append("    dimensao,         ");
					inserirPneu.append("    cd_dimensao,         ");
					inserirPneu.append("    sulco1,         ");
					inserirPneu.append("    sulco2,         ");
					inserirPneu.append("    sulco3,         ");
					inserirPneu.append("    sulco4,         ");
					inserirPneu.append("    sulco5,         ");
					inserirPneu.append("    cd_fornecedor,         ");
					inserirPneu.append("    valor,         ");
					inserirPneu.append("    nm_usuario_inclusao,         ");
					inserirPneu.append("    dt_inclusao,         ");
					inserirPneu.append("    status_ativo,         ");
					inserirPneu.append("    cd_evento)         ");
					inserirPneu.append("VALUES                     ");
					inserirPneu.append(
							"	(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '0', 2)    ");

					String ltFabrica = Funcoes.nulo(ob.get("CAD_LT_NEW_FABRICA"), "");
					String lsFabrica = Funcoes.nulo(ob.get("CAD_LS_NM_FABRICA"), "");
					String ltDimensao = Funcoes.nulo(ob.get("CAD_LT_NEW_DIMENSAO"), "");
					String lsDimensao = Funcoes.nulo(ob.get("CAD_LS_DIMENSAO"), "");
					String ltModelo = Funcoes.nulo(ob.get("CAD_LT_NEW_MODELO"), "");
					String lsModelo = Funcoes.nulo(ob.get("CAD_LS_NM_MODELO"), "");
					String ltDesenho = Funcoes.nulo(ob.get("CAD_LT_NEW_DESENHO"), "");
					String lsDesenho = Funcoes.nulo(ob.get("CAD_LS_NM_DESENHO"), "");
					String ltTipoBorracha = Funcoes.nulo(ob.get("CAD_LT_NEW_TIPO_BORRA"), "");
					String lsTipoBorracha = Funcoes.nulo(ob.get("CAD_LS_NM_TIPO_BORRA"), "");
					String ltFornecedor = Funcoes.nulo(ob.get("CAD_LS_FORNECEDOR"), "");
					String lsFornecedor = Funcoes.nulo(ob.get("CAD_LT_NEW_FORNECEDOR "), "");

					int codigoFabrica = 0;
					int codigoDimensaoPneu = 0;
					int codigoModelo = 0;
					int codigoDesenho = 0;
					int codigoTipoBorracha = 0;
					int codigoFornecedor = 0;

					if (ltFabrica.equals("")) {
						ltFabrica = lsFabrica;

						codigoFabrica = buscaCodigoTabelaPneu(integracaoVO, "nm_fabrica", "cd_fabrica", ltFabrica);

					}

					else {
						int novoCodigoFabrica = buscaNovoCodigoTabelaPneu(integracaoVO, "cd_fabrica");

						codigoFabrica = novoCodigoFabrica;
					}

					if (ltDimensao.equals("")) {
						ltDimensao = lsDimensao;

						codigoDimensaoPneu = buscaCodigoTabelaPneu(integracaoVO, "dimensao", "cd_dimensao", lsDimensao);
					} else {
						int novoCodigoDimensaoPneu = buscaNovoCodigoTabelaPneu(integracaoVO, "cd_dimensao");

						codigoDimensaoPneu = novoCodigoDimensaoPneu;
					}

					if (ltModelo.equals("")) {
						ltModelo = lsModelo;

						codigoModelo = buscaCodigoTabelaPneu(integracaoVO, "nm_modelo", "cd_modelo", lsModelo);

					} else {

						int novoCodigoModelo = buscaNovoCodigoTabelaPneu(integracaoVO, "cd_dimensao");

						codigoModelo = novoCodigoModelo;
					}

					if (ltDesenho.equals("")) {
						ltDesenho = lsDesenho;

						codigoDesenho = buscaCodigoTabelaPneu(integracaoVO, "nm_desenho", "cd_desenho", lsDesenho);

					} else {

						int novoCodigoDesenho = buscaNovoCodigoTabelaPneu(integracaoVO, "cd_desenho");

						codigoDesenho = novoCodigoDesenho;
					}

					if (ltTipoBorracha.equals("")) {
						ltTipoBorracha = lsTipoBorracha;

						codigoTipoBorracha = buscaCodigoTabelaPneu(integracaoVO, "nm_tipo_borra", "cd_tipo_borra",
								lsTipoBorracha);

					} else {

						int novoCodigoTipoBorracha = buscaNovoCodigoTabelaPneu(integracaoVO, "cd_desenho");

						codigoTipoBorracha = novoCodigoTipoBorracha;
					}

					if (ltFornecedor.equals("")) {
						ltFornecedor = lsFornecedor;

						StringBuilder consultaCodigoFornecedor = new StringBuilder();
						consultaCodigoFornecedor.append("select distinct ");
						consultaCodigoFornecedor.append("		cd_fornecedor ");
						consultaCodigoFornecedor.append("from ");
						consultaCodigoFornecedor.append("		fornecedor ");
						consultaCodigoFornecedor.append("where ");
						consultaCodigoFornecedor.append("		nm_fornecedor = ?");
						consultaCodigoFornecedor.append("and ");
						consultaCodigoFornecedor.append("		cd_fornecedor != 0");

						try (Connection conNew = integracaoVO.getConexao()) {
							try (PreparedStatement pst = con.prepareStatement(consultaCodigoFornecedor.toString())) {
								int i = 1;
								pst.setString(i++, lsFornecedor);
								try (ResultSet rs = pst.executeQuery()) {
									while (rs.next()) {
										codigoFornecedor = rs.getInt("cd_fornecedor");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else if (ltFornecedor != "") {

						StringBuilder consultaNovoCodigoFornecedor = new StringBuilder();
						consultaNovoCodigoFornecedor.append("select ");
						consultaNovoCodigoFornecedor.append("     MAX(cd_fornecedor) + 1 as cd_fornecedor");
						consultaNovoCodigoFornecedor.append("from ");
						consultaNovoCodigoFornecedor.append("     fornecedor");

						try (PreparedStatement pst = con.prepareStatement(consultaNovoCodigoFornecedor.toString())) {
							try (ResultSet rs = pst.executeQuery()) {
								codigoFornecedor = rs.getInt("cd_fornecedor");
								insertTabelaFornecedor(integracaoVO, codigoFornecedor, ltFornecedor);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					try (PreparedStatement pst1 = con.prepareStatement(inserirPneu.toString())) {
						int i = 1;

						LocalDateTime Data = LocalDateTime.now();
						String dataFormatada = Data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_LT_COD_PNEU"), ""));
						pst1.setString(i++, ltFabrica);
						pst1.setInt(i++, codigoFabrica);
						pst1.setString(i++, ltModelo);
						pst1.setInt(i++, codigoModelo);
						pst1.setString(i++, ltTipoBorracha);
						pst1.setInt(i++, codigoTipoBorracha);
						pst1.setString(i++, ltDesenho);
						pst1.setInt(i++, codigoDesenho);
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_LT_NR_SERIE"), "0"));
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_LT_NR_VIDA"), "0"));
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_LT_LIBRAS"), "0"));
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_LT_DOT"), "0"));
						pst1.setString(i++, ltDimensao);
						pst1.setInt(i++, codigoDimensaoPneu);
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_ND_SULCO1"), "0"));
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_ND_SULCO2"), "0"));
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_ND_SULCO3"), "0"));
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_ND_SULCO4"), "0"));
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_ND_SULCO5"), "0"));
						pst1.setInt(i++, codigoFornecedor);
						pst1.setString(i++, Funcoes.nulo(ob.get("CAD_MT_VALOR"), "0"));
						pst1.setString(i++, "ADM");
						pst1.setString(i++, dataFormatada);

						logger.info("Este é o ltFabrica" + ltFabrica);
						logger.info("Este é o lsFabrica" + lsFabrica);

						pst1.executeUpdate();
						// if (resultado < 1) {
						// logger.error(" ==== [ (rep2)ERRO AO INSERIR DADOS ] ==== ");
						// msgPneu = "99|Erro ao salvar cadastro dos Pneus no Lecom";
						// } else {
						// msgPneu = "0|Pneus Atualizados com sucesso! ";
						// }
					} catch (Exception e) {
						logger.error(" ==== [ ] ==== ", e);
						// msgPneu = "99|Erro ao salvar";
					}
				}
			}
			logger.info("# codProcesso = " + integracaoVO.getCodProcesso());
			logger.info("# codEtapa = " + integracaoVO.getCodEtapa());

			logger.info("====== FIM CadastroPneu ======");
			logger.info(new String(new char[100]).replace("\0", "#"));

			// return "0| Integra��o processada";
		} catch (Exception e) {
			logger.error("Erro ao executar integra��o", e);

		}
	}

	public void insertTabelaFornecedor(IntegracaoVO integracaoVO, int codigoFornecedor, String nomeFornecedor) {
		try {
			integracaoVO.setConexao("BPM_AUX");
			try (Connection con = integracaoVO.getConexao()) {
				StringBuilder inserirFornecedor = new StringBuilder();
				inserirFornecedor.append("INSERT INTO");
				inserirFornecedor.append("		fornecedor");
				inserirFornecedor.append("		(cd_fornecedor");
				inserirFornecedor.append("		,nm_fornecedor)");
				inserirFornecedor.append("Values");
				inserirFornecedor.append("(?,?);");

				try (PreparedStatement pst = con.prepareStatement(inserirFornecedor.toString())) {
					pst.setInt(1, codigoFornecedor);
					pst.setString(2, nomeFornecedor);
					pst.executeUpdate();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int buscaCodigoTabelaPneu(IntegracaoVO integracaoVO, String nomeColuna, String codigoColuna,
			String lsValue) {
		int codigoEncontrado = 0;
		try {

			StringBuilder buscarCodigo = new StringBuilder();
			buscarCodigo.append(" select distinct " + codigoColuna + " as codigo ");
			buscarCodigo.append(" from ");
			buscarCodigo.append("		pneu ");
			buscarCodigo.append(" where " + nomeColuna + " = ? ");
			buscarCodigo.append(" and " + codigoColuna + " != 0");

			try (Connection con = integracaoVO.getConexao()) {
				try (PreparedStatement pst = con.prepareStatement(buscarCodigo.toString())) {
					int i = 1;
					pst.setString(i++, lsValue);
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
							codigoEncontrado = rs.getInt("codigo");

						}

					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return codigoEncontrado;
	}

	public int buscaNovoCodigoTabelaPneu(IntegracaoVO integracaoVO, String codigoColuna) {

		int codigoNovo = 0;

		try {
			StringBuilder buscarNovoCodigoAddMaisUm = new StringBuilder();
			buscarNovoCodigoAddMaisUm.append(" select MAX(" + codigoColuna + ") + 1 as codigo ");
			buscarNovoCodigoAddMaisUm.append(" from ");
			buscarNovoCodigoAddMaisUm.append("	pneu ");

			try (Connection con = integracaoVO.getConexao()) {
				logger.info(buscarNovoCodigoAddMaisUm.toString());
				try (PreparedStatement pst = con.prepareStatement(buscarNovoCodigoAddMaisUm.toString())) {
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
							logger.info(rs.getInt("codigo"));
							codigoNovo = rs.getInt("codigo");
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(codigoNovo);
		return codigoNovo;
	}
}
