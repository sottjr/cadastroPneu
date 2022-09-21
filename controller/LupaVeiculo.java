package com.lecom.workflow.cadastros.pneus.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.lecom.tecnologia.db.DBUtils;
import com.lecom.workflow.cadastros.common.util.Funcoes;

import br.com.lecom.atos.utils.view.ControllerServlet;

@WebServlet("/app/public/LupaVeiculo")
public class LupaVeiculo extends ControllerServlet{
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LupaVeiculo.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.info(" ======== Inicio LupaVeiculo ======== ");

		PrintWriter out = response.getWriter();
		JSONArray collect = new JSONArray();
		JSONObject retorno = new JSONObject();
		String codPlaca = Funcoes.nulo(request.getParameter("codPlaca"), "");
		String codVeiculo 	= Funcoes.nulo(request.getParameter("codVeiculo"),"");
		String status 	= Funcoes.nulo(request.getParameter("status"),"");
		

		logger.error("O codveiculo é " + codVeiculo);
		logger.error("O codveiculo é " + codPlaca);
		
		
		try {
			
			try(Connection con = DBUtils.getConnection("BPM_AUX")) {
				
				StringBuilder sSql = new StringBuilder();
				sSql.append("		SELECT		");
				sSql.append("		V.cd_veiculo AS cd_veiculo	");
				sSql.append("		,V.tipo_veiculo	AS tipo_veiculo	");
				sSql.append("		,V.placa AS placa	");
				sSql.append("		,V.qtd_Pneus AS qtd_Pneus		");
				sSql.append("	FROM veiculo AS V	");
				sSql.append("	WHERE V.cd_veiculo like '%" + codVeiculo + "%'	");
				sSql.append("	OR V.cd_veiculo like '%" + codPlaca + "%'	");


				
				logger.error(sSql);
				try(PreparedStatement pst = con.prepareStatement(sSql.toString())){
					
					int indice = 1;
					
					try(ResultSet rs = pst.executeQuery()){
						while(rs.next()) {
							if((status.equals("1") && !Funcoes.nulo(rs.getString("cd_veiculo"), "").equals("")) || (status.equals("0") && Funcoes.nulo(rs.getString("cd_veiculo"), "").equals(""))) {
								
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("cd_veiculo", Funcoes.nulo(rs.getString("cd_veiculo"), ""));					
								jsonObject.put("tipo_veiculo", Funcoes.nulo(rs.getString("tipo_veiculo"), ""));
								jsonObject.put("placa", Funcoes.nulo(rs.getString("placa"), ""));
								jsonObject.put("qtd_Pneus", Funcoes.nulo(rs.getString("qtd_Pneus"), ""));
								collect.add(jsonObject);
							}
							
						}
					}
				}
			}
			
			retorno.put("dados", collect);
			response.setContentType("application/json");
			out.print(retorno.toString());
			
		} catch (Exception e) {
			logger.error("Exception : ", e);
		} finally {
			out.flush();
			out.close();
		}
	}

}

