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
	int codigoFornecedor = 0;

	@Execution
	public void realizarCadastroPneu(IntegracaoVO integracaoVO) throws Exception {
		logger.info("====== INICIO CadastroPneu ======");

		try {
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
					String ltFornecedor = Funcoes.nulo(ob.get("CAD_LT_NEW_FORNECEDOR"), "");
					String lsFornecedor = Funcoes.nulo(ob.get("CAD_LS_FORNECEDOR"), "");

					int codigoFabrica = 0;
					int codigoDimensaoPneu = 0;
					int codigoModelo = 0;
					int codigoDesenho = 0;
					int codigoTipoBorracha = 0;

					if (ltFabrica.equals("")) {
						ltFabrica = lsFabrica;
						codigoFabrica = buscaCodigoTabelaPneu(integracaoVO, "nm_fabrica", "cd_fabrica", ltFabrica);
					} else {
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

						codigoFornecedor = buscaCodigoTabelaFornecedor(integracaoVO, "nm_fornecedor", "cd_fornecedor",
								lsFornecedor);

					} else {
						insertTabelaFornecedor(integracaoVO, ltFornecedor);

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

						pst1.executeUpdate();

					} catch (Exception e) {
						logger.error(" ==== [ ] ==== ", e);

					}
				}
			}
			logger.info("# codProcesso = " + integracaoVO.getCodProcesso());
			logger.info("# codEtapa = " + integracaoVO.getCodEtapa());
			logger.info("====== FIM CadastroPneu ======");
			logger.info(new String(new char[100]).replace("\0", "#"));

		} catch (Exception e) {
			logger.error("Erro ao executar integração", e);

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
				try (PreparedStatement pst = con.prepareStatement(buscarNovoCodigoAddMaisUm.toString())) {
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
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
		return codigoNovo;
	}

	public int buscaNovoCodigoTabelaFornecedor(IntegracaoVO integracaoVO) {

		int codigoNovoFornecedor = 1;

		try {
			StringBuilder buscarNovoCodigoAddMaisUmFornecedores = new StringBuilder();
			buscarNovoCodigoAddMaisUmFornecedores.append(" select MAX(cd_fornecedor) as codigo ");
			buscarNovoCodigoAddMaisUmFornecedores.append(" from ");
			buscarNovoCodigoAddMaisUmFornecedores.append("	fornecedor ");

			try (Connection con = integracaoVO.getConexao()) {
				try (PreparedStatement pst = con.prepareStatement(buscarNovoCodigoAddMaisUmFornecedores.toString())) {
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
							codigoNovoFornecedor = rs.getInt("codigo");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return codigoNovoFornecedor;
	}

	public int buscaCodigoTabelaFornecedor(IntegracaoVO integracaoVO, String nomeColuna, String codigoColuna,
			String lsValue) {

		int codigoFornecedorEncontrado = 0;

		try {
			StringBuilder buscarCodigoFornecedor = new StringBuilder();
			buscarCodigoFornecedor.append(" select distinct " + codigoColuna + " as codigo ");
			buscarCodigoFornecedor.append(" from ");
			buscarCodigoFornecedor.append("		fornecedor ");
			buscarCodigoFornecedor.append(" where " + nomeColuna + " = ? ");
			buscarCodigoFornecedor.append(" and " + codigoColuna + " != 0 ");

			try (Connection con = integracaoVO.getConexao()) {
				try (PreparedStatement pst = con.prepareStatement(buscarCodigoFornecedor.toString())) {
					int i = 1;
					pst.setString(i++, lsValue);

					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
							codigoFornecedorEncontrado = rs.getInt("codigo");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codigoFornecedorEncontrado;
	}

	public void insertTabelaFornecedor(IntegracaoVO integracaoVO, String nomeFornecedor) {
		try {
			StringBuilder inserirFornecedor = new StringBuilder();
			inserirFornecedor.append(" INSERT INTO ");
			inserirFornecedor.append("		fornecedor ");
			inserirFornecedor.append("		(nm_fornecedor) ");
			inserirFornecedor.append(" Values (?) ");

			try (Connection con = integracaoVO.getConexao()) {
				try (PreparedStatement pst = con.prepareStatement(inserirFornecedor.toString())) {
					pst.setString(1, nomeFornecedor);
					pst.executeUpdate();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		codigoFornecedor = buscaNovoCodigoTabelaFornecedor(integracaoVO);

	}

}
