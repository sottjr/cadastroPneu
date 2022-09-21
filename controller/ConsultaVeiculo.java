package com.lecom.workflow.cadastros.pneus.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.lecom.tecnologia.db.DBUtils;
import com.lecom.workflow.cadastros.common.util.Funcoes;
import com.lecom.workflow.cadastros.cpfcnpjlote.controller.EnviaPrimeiroEmail;
import com.lecom.workflow.vo.IntegracaoVO;
import org.apache.log4j.Logger;



import br.com.lecom.atos.utils.view.ControllerServlet;

@WebServlet("/app/public/ConsultaVeiculo")
public class ConsultaVeiculo extends ControllerServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ConsultaVeiculo.class);

	@SuppressWarnings("unchecked")
	public void consultarPlacaECodigoVeiculo(IntegracaoVO integracaoVO) {
		try {
			logger.info(" ======== Inicio ConsultaVeiculo ======== ");

			Map<String, String> campo = integracaoVO.getMapCamposFormulario();
			
			String ltPlacaVeiculo = Funcoes.nulo(campo.get("LT_PLACA_VEICULO"), "");
			String ltCodVeiculo = Funcoes.nulo(campo.get("LT_CD_VEICULO"), "");
			List<Map<String, String>> retornoConsultaPneu = consultaPneu(integracaoVO, ltCodVeiculo, ltPlacaVeiculo);
			List<Map<String, Object>> retornoConsultaPneuGrid = consultaPneuGrid(integracaoVO, ltCodVeiculo, ltPlacaVeiculo);
			
			  try {
	            	 List<Map<String, String>> dadosDaConsultaPneu = retornoConsultaPneu;
	            	 dadosDaConsultaPneu.forEach(informacoes -> {
	            		 
	            		
		        	});
			
			

		} catch (Exception e) {
			logger.error(" ==== [ ] ==== ", e);
		}
	}

	public List<Map<String, Object>> consultaPneuGrid(IntegracaoVO integracaoVO, String ltCodVeiculo,
			String ltPlacaVeiculo) {
		
		List<Map<String, Object>> listaConsultaPneu = new ArrayList<>();

		try {
			StringBuilder sSql = new StringBuilder();
			sSql.append("       SELECT      ");
			sSql.append("       ,VP.cd_pneu AS cd_pneu  ");
			sSql.append("       ,VP.cd_posicao AS cd_posicao        ");
			sSql.append("       ,VP.dt_instalacao AS dt_instalacao  ");
			sSql.append("       ,VP.dt_desinstalacao AS dt_desinstalacao    ");
			sSql.append("   FROM veiculo AS V   ");
			sSql.append("   LEFT JOIN veiculo_pneu AS VP ON (VP.cd_veiculo = V.cd_veiculo)  ");
			sSql.append("   WHERE V.cd_veiculo = '" + ltCodVeiculo + "'   ");
			sSql.append("   or V.placa = '" + ltPlacaVeiculo + "'   ");

			try (Connection con = integracaoVO.getConexao()) {
				try (PreparedStatement pst = con.prepareStatement(sSql.toString())) {
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {

							Map<String, Object> linha = new HashMap<>();

							
							
							//grid
							linha.put("LT_CODI_PNEU_VEICULO", Funcoes.nulo(rs.getString("cd_pneu"), ""));
							linha.put("LT_POS_PNEU_VEICULO",Funcoes.nulo(rs.getString("cd_posicao "), ""));
							linha.put("DT_INST_PNEU_VEICULO",Funcoes.nulo(rs.getString("dt_instalacao"), ""));
							linha.put("DT_DESINST_PNEU_VEICULO",Funcoes.nulo(rs.getString("dt_desinstalacao"), ""));
							listaConsultaPneu.add(linha);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaConsultaPneu;
	} 
	public List<Map<String, String>> consultaPneu(IntegracaoVO integracaoVO, String ltCodVeiculo,
			String ltPlacaVeiculo) {
		
		List<Map<String, String>> listaConsultaPneu = new ArrayList<>();

		try {
			StringBuilder sSql = new StringBuilder();
			sSql.append("       SELECT      ");
			sSql.append("       V.cd_veiculo AS cd_veiculo  ");
			sSql.append("       ,V.tipo_veiculo AS tipo_veiculo ");
			sSql.append("       ,V.placa AS placa   ");
			sSql.append("       ,V.qtd_Pneus AS qtd_Pneus       ");
			sSql.append("   FROM veiculo AS V   ");
			sSql.append("   LEFT JOIN veiculo_pneu AS VP ON (VP.cd_veiculo = V.cd_veiculo)  ");
			sSql.append("   WHERE V.cd_veiculo = '" + ltCodVeiculo + "'   ");
			sSql.append("   or V.placa = '" + ltPlacaVeiculo + "'   ");

			try (Connection con = integracaoVO.getConexao()) {
				try (PreparedStatement pst = con.prepareStatement(sSql.toString())) {
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {

							Map<String, String> linha = new HashMap<>();

							linha.put("tipo_veiculo", Funcoes.nulo(rs.getString("tipo_veiculo"), ""));
							linha.put("qtd_Pneus",Funcoes.nulo(rs.getString("qtd_Pneus"), ""));
							linha.put("placa", Funcoes.nulo(rs.getString("placa"), ""));
							linha.put("cd_veiculo", Funcoes.nulo(rs.getString("cd_veiculo"), ""));
							
							listaConsultaPneu.add(linha);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaConsultaPneu;
	} 
}
