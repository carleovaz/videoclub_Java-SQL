/*
 * Estructura de ConsultaAlquiler
 */
package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaAlquilerFK implements WindowListener, ActionListener
{
	Frame ventanaAlquilerConsulta = new Frame("Consulta");
	TextArea listadoAlquilerConsulta = new TextArea(5, 30);
	Button botonPdf = new Button("PDF");
	Label labelMensajeAlquierConsulta = new Label("");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ConsultaAlquilerFK(String usuario)
	{
		this.usuario = usuario;
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT idAlquiler, nombreCliente, nombrePelicula FROM alquileres JOIN  clientes ON alquileres.idClientesFK2 = clientes.idCliente JOIN peliculas ON alquileres.idPeliculasFK3 = peliculas.idPelicula";

		
		try
		{
			//CREAMOS LA SENTENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//CREAMOS EL RESULT SET
			rs = statement.executeQuery(sentencia);
			listadoAlquilerConsulta.selectAll();
			listadoAlquilerConsulta.setText("");
			listadoAlquilerConsulta.append("id\tNombre Cliente\tNombre Pelicula\n");
			while(rs.next())
			{
				listadoAlquilerConsulta.append(rs.getInt("idAlquiler")
						+"-"+rs.getString("nombreCliente") +"-"+rs.getString("nombrePelicula")+"\n");
			}
		}
		//EN EL CASO QUE FALLE
		catch (SQLException sqle)
		{
			labelMensajeAlquierConsulta.setText("Error");
		}
		finally
		{
			ventanaAlquilerConsulta.setLayout(new FlowLayout());
			listadoAlquilerConsulta.setEditable(false);
			ventanaAlquilerConsulta.add(listadoAlquilerConsulta);
			ventanaAlquilerConsulta.add(botonPdf);
			botonPdf.addActionListener(this);
			ventanaAlquilerConsulta.setSize(250,200);
			ventanaAlquilerConsulta.setResizable(false);
			ventanaAlquilerConsulta.setLocationRelativeTo(null);
			ventanaAlquilerConsulta.addWindowListener(this);
			ventanaAlquilerConsulta.setVisible(true);
			bd.desconectar(connection);
		}
		
	}

	public void windowClosing(WindowEvent e)
	{
		log.guardar(usuario, "Ha salido de Consulta Alquiler.");
		if(ventanaAlquilerConsulta.isActive())
		{
			ventanaAlquilerConsulta.setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonPdf))
		{
			log.guardar(usuario, "Ha solicitado el pdf de consulta de alquileres.");
		}
	}
	public void windowActivated(WindowEvent we) 
	{
		
	}
	public void windowClosed(WindowEvent we) 
	{
		
	}
	public void windowDeactivated(WindowEvent we) 
	{
		
	}
	public void windowDeiconified(WindowEvent we) 
	{
		
	}
	public void windowIconified(WindowEvent we) 
	{
		
	}
	public void windowOpened(WindowEvent we) 
	{
		
	}
}